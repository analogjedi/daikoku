package com.primateer.daikoku.ui.views.forms;


import android.view.View;

public interface Form<T> {
	/**
	 * @return the {@code View} containing the form, usually {@code this}
	 */
	View getView();

	/**
	 * Does nothing if the current input is valid.
	 * 
	 * @throws InvalidDataException
	 *             otherwise.
	 */
	void validate() throws InvalidDataException;

	/**
	 * Returns the data from the form in an object.
	 * If data is returned, it must be valid.
	 * {@code null} may be returned to indicate an empty form.
	 * 
	 * @return object constructed from form input, or {@code null}
	 * @throws InvalidDataException
	 *             on invalid input
	 */
	T getData() throws InvalidDataException;

	/**
	 * Sets the form contents corresponding to the arugment data.
	 * 
	 * @param data
	 *            new form contents. {@code null} is allowed and synonymous to
	 *            {@code clear()}.
	 * @throws IllegalArgumentException
	 *             on invalid argument
	 */
	void setData(T data) throws IllegalArgumentException;

	/**
	 * Reset the form to an empty state.
	 */
	void clear();

	/**
	 * @return Title of the form, may or may not be shown.
	 */
	String getTitle();
}