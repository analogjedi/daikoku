package com.primateer.daikoku.views.forms;

import java.lang.reflect.Constructor;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;

import com.primateer.daikoku.dialogs.FormFragment;
import com.primateer.daikoku.model.Observer;

public class FormDialogButton<T> extends Button implements Form<T>, Observer<T> {

	private Form<T> form;
	private FormFragment<T> fragment;
	private boolean formUsed = false;
	private T data;

	public FormDialogButton(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public FormDialogButton(Context context) {
		this(context, null);
	}

	public void register(final Class<T> type) {
		this.setText(getForm(type).getTitle());
		this.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Form<T> form = getForm(type);
				form.setData(data);
				formUsed = true;
				fragment = new FormFragment<T>();
				fragment.setForm(form);
				fragment.addObserver(FormDialogButton.this);
				fragment.show(((FragmentActivity) getContext())
						.getSupportFragmentManager(), null);
			}
		});
	}

	@SuppressWarnings("unchecked")
	private Form<T> getForm(Class<T> type) {
		if (formUsed) {
			fragment.removeObserver(this);
			form = null;
			formUsed = false;
		}
		if (form == null) {
			try {
				Class<Form<T>> formClass = (Class<Form<T>>) Class
						.forName("com.primateer.daikoku.views.forms."
								+ type.getSimpleName() + "Form");
				Constructor<Form<T>> constructor = formClass
						.getConstructor(Context.class);
				form = (Form<T>) constructor.newInstance(getContext());
				formUsed = false;
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
		this.data = data;
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update(T data) {
		this.data = data;
	}
}
