package com.primateer.daikoku.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.primateer.daikoku.R;
import com.primateer.daikoku.model.Observable;
import com.primateer.daikoku.model.Observer;
import com.primateer.daikoku.model.SimpleObservable;
import com.primateer.daikoku.views.forms.Form;
import com.primateer.daikoku.views.forms.InvalidDataException;

public class FormFragment<T> extends DialogFragment implements Observable<T> {

	private Form<T> form;
	private SimpleObservable<T> observable = new SimpleObservable<T>();
	
	public void setForm(Form<T> form) {
		this.form = form;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(form.getTitle());
		form.getView().setBackgroundColor(Color.WHITE); // FIXME
		builder.setView(form.getView());
		builder.setNegativeButton(R.string.cancel, null);
		builder.setPositiveButton(R.string.ok,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						try {
							observable.notifyObservers(form.getData());
						} catch (InvalidDataException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
		return builder.create();
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
