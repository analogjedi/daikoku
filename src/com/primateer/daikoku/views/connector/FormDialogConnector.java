package com.primateer.daikoku.views.connector;

import java.lang.reflect.Constructor;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.primateer.daikoku.dialogs.FormFragment;
import com.primateer.daikoku.model.Observable;
import com.primateer.daikoku.model.Observer;
import com.primateer.daikoku.model.SimpleObservable;
import com.primateer.daikoku.views.forms.Form;
import com.primateer.daikoku.views.forms.InvalidDataException;

public class FormDialogConnector<T> implements Form<T>, Observer<T>,
		Observable<T> {

	private SimpleObservable<T> observable = new SimpleObservable<T>();
	private Form<T> form;
	private FormFragment<T> fragment;
	private boolean viewUsed = false;
	private T data;
	private Class<T> dataClass;
	private Context context;

	public FormDialogConnector(Class<T> dataClass, View launcher) {
		this(dataClass, launcher.getContext());
		if (launcher instanceof TextView) {
			((TextView) launcher).setText(getForm(dataClass,
					launcher.getContext()).getTitle());
		}
		launcher.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				FormDialogConnector.this.showDialog();
			}
		});
	}

	public FormDialogConnector(Class<T> dataClass, Context context) {
		this.dataClass = dataClass;
		this.context = context;
	}

	public void showDialog() {
		Form<T> form = getForm(dataClass, this.context);
		form.setData(data);
		fragment = new FormFragment<T>();
		fragment.setForm(form);
		fragment.addObserver(FormDialogConnector.this);
		fragment.show(
				((FragmentActivity) this.context).getSupportFragmentManager(),
				null);
		viewUsed = true;
	}

	@SuppressWarnings("unchecked")
	private Form<T> getForm(Class<T> type, Context context) {
		if (viewUsed) {
			fragment.removeObserver(this);
			form = null;
			viewUsed = false;
		}
		if (form == null) {
			try {
				Class<Form<T>> formClass = (Class<Form<T>>) Class
						.forName("com.primateer.daikoku.views.forms."
								+ type.getSimpleName() + "Form");
				Constructor<Form<T>> constructor = formClass
						.getConstructor(Context.class);
				form = (Form<T>) constructor.newInstance(context);
				viewUsed = false;
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		return form;
	}

	@Override
	public View getView() {
		if (form != null) {
			return form.getView();
		}
		return null;
	}

	@Override
	public void validate() throws InvalidDataException {
		if (form != null) {
			form.validate();
		}
	}

	@Override
	public T getData() throws InvalidDataException {
		return data;
	}

	@Override
	public void setData(T data) throws IllegalArgumentException {
		update(data);
		if (form != null) {
			form.setData(data);
		}
	}

	@Override
	public void clear() {
		if (form != null) {
			form.clear();
		}
		data = null;
	}

	@Override
	public String getTitle() {
		if (form != null) {
			return form.getTitle();
		}
		if (data != null) {
			return data.toString();
		}
		return null;
	}

	@Override
	public void update(T data) {
		this.data = data;
		observable.notifyObservers(data);
	}

	@Override
	public void addObserver(Observer<T> observer) {
		observable.addObserver(observer);
	}

	@Override
	public void removeObserver(Observer<T> observer) {
		observable.removeObserver(observer);
	}
}
