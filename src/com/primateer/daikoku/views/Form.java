package com.primateer.daikoku.views;


public interface Form<T> {

	/**
	 * Does nothing if the current input is valid.
	 * 
	 * @throws InvalidDataException otherwise.
	 */
	void validate() throws InvalidDataException;	
	
	/**
	 * Returns the data from the form in an object.
	 * The returned data must be valid.
	 * 
	 * @return object constructed from form input
	 * @throws InvalidDataException on invalid input
	 */
	T getData() throws InvalidDataException;
	
	/**
	 * Sets the form contents corresponding to the arugment data.
	 * 
	 * @param data new form contents
	 * @throws IllegalArgumentException on invalid argument
	 */
	void setData(T data) throws IllegalArgumentException;
}
