package com.primateer.daikoku.ui.lists;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.primateer.daikoku.Application;
import com.primateer.daikoku.Event;
import com.primateer.daikoku.Event.Listener;
import com.primateer.daikoku.model.ValueObject;
import com.primateer.daikoku.ui.dialogs.FormFragment;
import com.primateer.daikoku.ui.forms.Form;
import com.primateer.daikoku.ui.widgets.DeleteRowButton;

public class CatalogRowWidget<T extends ValueObject> extends LinearLayout
		implements DataRowWidget<T> {

	public static class SelectedEvent extends Event {
	}

	private T bufferedData;
	private ImageButton deleteButton;
	private ImageButton editButton;
	private TextView selectView;
	private Form<T> dataForm;

	private Event.SimpleDispatcher dispatcher = new Event.SimpleDispatcher();

	public CatalogRowWidget(Context context) {
		this(context, null);
	}

	public CatalogRowWidget(Context context, AttributeSet attrs) {
		super(context, attrs);

		this.setMinimumHeight(30);

		deleteButton = new DeleteRowButton(context, dispatcher);
		LinearLayout.LayoutParams deleteLayout = new LayoutParams(0,
				LayoutParams.WRAP_CONTENT, 0.25f);
		deleteLayout.gravity = Gravity.CENTER;

		editButton = new ImageButton(context);
		editButton.setImageResource(Application.ICON_EDIT);
		editButton.setBackgroundColor(Color.TRANSPARENT);
		LinearLayout.LayoutParams editLayout = new LayoutParams(0,
				LayoutParams.WRAP_CONTENT, 0.25f);
		editLayout.gravity = Gravity.CENTER;

		selectView = new TextView(context);
		selectView.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
		LinearLayout.LayoutParams selectLayout = new LayoutParams(0,
				LayoutParams.MATCH_PARENT, 1.5f);
		selectLayout.gravity = Gravity.CENTER_VERTICAL;
		selectView.setPadding(5, 0, 0, 0);
		selectView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dispatcher.dispatch(new SelectedEvent());
			}
		});

		this.setOrientation(LinearLayout.HORIZONTAL);
		this.addView(deleteButton, deleteLayout);
		this.addView(editButton, editLayout);
		this.addView(selectView, selectLayout);

		this.setEditable(false);
	}

	private void setEditable(boolean editable) {
		deleteButton.setVisibility(editable ? View.VISIBLE : View.GONE);
		editButton.setVisibility(editable ? View.VISIBLE : View.GONE);
	}

	public void setupForm(Class<T> dataClass) { // TODO make this private
		FormFragment<T> fragment = new FormFragment<T>();
		fragment.setupForm(getContext(), dataClass);
		fragment.connectLauncher(editButton);
		dataForm = fragment.getForm();
		dataForm.addEventListener(Form.DataChangedEvent.class,
				new Event.Listener() {
					@SuppressWarnings("unchecked")
					@Override
					public void onEvent(Event event) {
						bufferedData = ((Form.DataChangedEvent<T>) event).data;
						selectView.setText(bufferedData.toString());
					}
				});
		setEditable(true);
	}

	@Override
	public void setRowData(T data) {
		if (dataForm != null) {
			dataForm.setData(data);
		} else {
			selectView.setText(data.toString());
			bufferedData = data;
		}
	}

	@Override
	public T getRowData() {
		return bufferedData;
	}

	@Override
	public void storeRowPosition(int pos) {
		this.setTag(pos);
	}

	@Override
	public int restoreRowPosition() {
		return (Integer) this.getTag();
	}

	@Override
	public View getView() {
		return this;
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