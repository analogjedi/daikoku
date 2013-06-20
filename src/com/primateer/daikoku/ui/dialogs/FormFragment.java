package com.primateer.daikoku.ui.dialogs;

import java.lang.reflect.Constructor;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.primateer.daikoku.Application;
import com.primateer.daikoku.Event;
import com.primateer.daikoku.Helper;
import com.primateer.daikoku.R;
import com.primateer.daikoku.Event.Listener;
import com.primateer.daikoku.db.DBController;
import com.primateer.daikoku.model.ValueObject;
import com.primateer.daikoku.ui.forms.Form;
import com.primateer.daikoku.ui.forms.InvalidDataException;
import com.primateer.daikoku.ui.widgets.Separator;

public class FormFragment<T> extends DialogFragment implements Event.Registry {

	public static class AcceptEvent<T> extends Event {
		public final T data;

		public AcceptEvent(T data) {
			this.data = data;
		}
	}

	public static class CancelEvent extends Event {
	}

	private Form<T> form;
	private Event.SimpleDispatcher dispatcher = new Event.SimpleDispatcher();

	public Form<T> getForm() {
		return form;
	}
	
	@SuppressWarnings("unchecked")
	public void setupForm(Context context, T data) {
		this.setupForm(context, (Class<T>)data.getClass());
		form.setData(data);
	}

	@SuppressWarnings("unchecked")
	public void setupForm(Context context, Class<T> dataClass) {
		try {
			Class<Form<T>> formClass = (Class<Form<T>>) Class
					.forName("com.primateer.daikoku.ui.views.forms."
							+ dataClass.getSimpleName() + "Form");
			Constructor<Form<T>> constructor = formClass
					.getConstructor(Context.class);
			form = (Form<T>) constructor.newInstance(context);
		} catch (Exception e) {
			// anything that goes wrong here should be reflection related
			throw new RuntimeException(e);
		}
	}

	public void show(Context context) {
		if (form != null) {
			FormFragment.this.show(
					((FragmentActivity) context).getSupportFragmentManager(),
					null);
		}
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		final Dialog dialog = new Dialog(getActivity());
		dialog.setTitle(form.getTitle());
		ViewGroup content = (ViewGroup) form.getView();
		content.setBackgroundColor(Color.WHITE); // FIXME

		ImageButton cancelButton = new ImageButton(getActivity());
		cancelButton.setImageResource(Application.ICON_DENY);
		cancelButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dispatcher.dispatch(new CancelEvent());
				dialog.dismiss();
			}
		});

		ImageButton okButton = new ImageButton(getActivity());
		okButton.setImageResource(Application.ICON_ACCEPT);
		okButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				T data;
				try {
					form.validate();
					data = form.getData();
					if (data instanceof ValueObject) {
						DBController.getInstance().register(
								(ValueObject) form.getData());
					}
				} catch (InvalidDataException e) {
					Helper.displayErrorMessage(getActivity(), getResources()
							.getString(R.string.form_error_title), e
							.getMessage());
					Helper.logErrorStackTrace(this, e, "Invalid Form Input");
					return;
				}
				try {
					if (data == null) {
						data = form.getData();
					}
					dispatcher.dispatch(new AcceptEvent<T>(data));
				} catch (InvalidDataException e) {
					Helper.logErrorStackTrace(FormFragment.this, e,
							"Unable to notify observers");
				}
				dialog.dismiss();
			}
		});

		LinearLayout buttonLayout = new LinearLayout(getActivity());
		buttonLayout.setOrientation(LinearLayout.HORIZONTAL);
		buttonLayout.addView(okButton, new LayoutParams(0,
				LayoutParams.WRAP_CONTENT, 0.5f));
		buttonLayout.addView(cancelButton, new LayoutParams(0,
				LayoutParams.WRAP_CONTENT, 0.5f));
		content.addView(new Separator(getActivity()));
		content.addView(buttonLayout);
		dialog.setContentView(content);

		return dialog;
	}

	@Override
	public void addEventListener(Class<? extends Event> type, Listener listener) {
		dispatcher.addEventListener(type, listener);
	}

	@Override
	public void removeEventListener(Class<? extends Event> type,
			Listener listener) {
		dispatcher.removeEventListener(type, listener);
	}

	public void connectLauncher(View launcher) {
		if (launcher instanceof TextView) {
			((TextView) launcher).setText(form.getTitle());
		}
		launcher.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				FormFragment.this.show(v.getContext());
			}
		});
	}
}
