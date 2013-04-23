package com.primateer.daikoku.views.widgets;

import java.lang.reflect.Constructor;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;

import com.primateer.daikoku.dialogs.FormFragment;
import com.primateer.daikoku.views.forms.Form;

public class FormDialogButton<T> extends Button {

	private Form<T> form;
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
				FormFragment<T> frag = new FormFragment<T>();
				frag.setForm(form);
				frag.show(((FragmentActivity) getContext())
						.getSupportFragmentManager(), null);
			}
		});
	}
	
	public Form<T> getForm() {
		return form;
	}

	@SuppressWarnings("unchecked")
	private Form<T> getForm(Class<T> type) {
		if (formUsed || form == null) {
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
	
}
