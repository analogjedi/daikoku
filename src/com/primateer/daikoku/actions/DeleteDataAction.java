package com.primateer.daikoku.actions;

import android.content.Context;

import com.primateer.daikoku.Application;
import com.primateer.daikoku.Helper;
import com.primateer.daikoku.R;
import com.primateer.daikoku.db.Database;
import com.primateer.daikoku.model.ValueObject;

public class DeleteDataAction<T extends ValueObject> implements Action {

	private T item;
	private Context confirmationContext;

	public DeleteDataAction(T item, Context confirmationContext) {
		this.item = item;
		this.confirmationContext = confirmationContext;
	}

	@Override
	public void run() {
		if (confirmationContext != null) {
			Helper.executeUponConfirmation(
					confirmationContext,
					confirmationContext.getResources().getString(
							R.string.dialog_confirm_delete_title),
					confirmationContext.getResources().getString(
							R.string.dialog_confirm_delete_msg)
							+ " " + item.toString(), new Runnable() {
						@Override
						public void run() {
							delete();
						}
					});
		} else {
			delete();
		}
	}

	private void delete() {
		Database.getInstance().delete(item);
	}

	@Override
	public boolean isReady() {
		return true;
	}

	@Override
	public int getIcon() {
		return Application.ICON_DELETE;
	}

}
