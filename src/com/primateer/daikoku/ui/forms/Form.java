package com.primateer.daikoku.ui.forms;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewParent;
import android.widget.LinearLayout;

import com.primateer.daikoku.Event;
import com.primateer.daikoku.Event.Listener;
import com.primateer.daikoku.model.ValueObject;

public abstract class Form<T> extends LinearLayout implements Event.Registry {

	public static class InvalidDataException extends Exception {
		public InvalidDataException(String msg) {
			super(msg);
		}
	}

	public static class DataChangedEvent<T> extends Event {
		public final T data;

		public DataChangedEvent(T data) {
			this.data = data;
		}
	}

	private static final int PADDING_LEFT = 50;

	private long dataId = new ValueObject().getId();
	protected Event.SimpleDispatcher dispatcher = new Event.SimpleDispatcher();

	public Form(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.setOrientation(LinearLayout.VERTICAL);
	}

	public Form(Context context) {
		this(context, null);
	}

	/**
	 * @return the {@code View} containing the form, usually {@code this}
	 */
	public final View getView() {
		return this;
	}

	/**
	 * Returns the data from the form in an object. If data is returned, it must
	 * be valid. {@code null} may be returned to indicate an empty form.
	 * 
	 * @return object constructed from form input, or {@code null}
	 * @throws InvalidDataException
	 *             on invalid input
	 */
	public final T getData() throws InvalidDataException {
		validate();
		T data = gatherData();
		if (data instanceof ValueObject) {
			((ValueObject) data).setId(dataId);
		}
		return data;
	}

	/**
	 * Sets the form contents corresponding to the arugment data.
	 * 
	 * @param data
	 *            new form contents. {@code null} is allowed and synonymous to
	 *            {@code clear()}.
	 * @throws IllegalArgumentException
	 *             on invalid argument
	 */
	public final void setData(T data) throws IllegalArgumentException {
		// clear();
		if (data == null) {
			return;
		}
		if (data instanceof ValueObject) {
			dataId = ((ValueObject) data).getId();
		}
		fillFields(data);
		try {
			dispatcher.dispatch(new DataChangedEvent<T>(getData()));
		} catch (InvalidDataException e) {
			// don't notify on invalid data
		}
	}

	@Override
	public int getPaddingLeft() {
		ViewParent parent = this.getParent();
		if (parent instanceof View) { // FIXME ViewParents are never Views
			return ((View) parent).getPaddingLeft() + PADDING_LEFT;
		}
		return super.getPaddingLeft();
	}

	protected abstract T gatherData() throws InvalidDataException;

	protected abstract void fillFields(T data) throws IllegalArgumentException;

	@Override
	public void addEventListener(Class<? extends Event> type, Listener listener) {
		dispatcher.addEventListener(type, listener);
	}

	@Override
	public void removeEventListener(Class<? extends Event> type,
			Listener listener) {
		dispatcher.removeEventListener(type, listener);
	}

	/**
	 * Does nothing if the current input is valid.
	 * 
	 * @throws InvalidDataException
	 *             otherwise.
	 */
	public abstract void validate() throws InvalidDataException;

	/**
	 * Reset the form to an empty state.
	 */
	public abstract void clear();

	/**
	 * @return Title of the form, may or may not be shown.
	 */
	public abstract String getTitle();
}
