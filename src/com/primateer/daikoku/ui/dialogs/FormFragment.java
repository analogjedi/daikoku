package com.primateer.daikoku.ui.dialogs;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.primateer.daikoku.Application;
import com.primateer.daikoku.Helper;
import com.primateer.daikoku.R;
import com.primateer.daikoku.model.Event;
import com.primateer.daikoku.model.Event.Listener;
import com.primateer.daikoku.model.ValueObject;
import com.primateer.daikoku.ui.actions.Action;
import com.primateer.daikoku.ui.actions.SaveDataAction;
import com.primateer.daikoku.ui.views.forms.Form;
import com.primateer.daikoku.ui.views.forms.InvalidDataException;
import com.primateer.daikoku.ui.views.widgets.Separator;

public class FormFragment<T> extends DialogFragment implements Event.Dispatcher {
	
	public static class OKEvent<T> extends Event {
		public final T data;
		public OKEvent(T data) {
			this.data = data;
		}
	}
	
	public static class CancelEvent extends Event {
	}

	private Form<T> form;
	private Event.SimpleDispatcher dispatcher = new Event.SimpleDispatcher();

	public void setForm(Form<T> form) {
		this.form = form;
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
						@SuppressWarnings({ "rawtypes", "unchecked" })
						Action action = new SaveDataAction((ValueObject) form
								.getData());
						Application.getInstance().dispatch(action);
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
					dispatcher.dispatch(new OKEvent<T>(data));
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
}
