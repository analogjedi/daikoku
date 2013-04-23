package com.primateer.daikoku.views.forms;


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
	 * Returns the data from the form in an object. The returned data must be
	 * valid.
	 * 
	 * @return object constructed from form input
	 * @throws InvalidDataException
	 *             on invalid input
	 */
	T getData() throws InvalidDataException;

	/**
	 * Sets the form contents corresponding to the arugment data.
	 * 
	 * @param data
	 *            new form contents. {@code null} is allowed and synonymous to
	 *            {@code wipe()}.
	 * @throws IllegalArgumentException
	 *             on invalid argument
	 */
	void setData(T data) throws IllegalArgumentException;

	/**
	 * Reset the form to an empty state.
	 */
	void wipe();

	/**
	 * @return Title of the form, may or may not be shown.
	 */
	String getTitle();
}
