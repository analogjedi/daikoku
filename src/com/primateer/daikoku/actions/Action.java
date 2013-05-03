package com.primateer.daikoku.actions;

public interface Action extends Runnable {

	/**
	 * @return {@code true} if it can be run in its current state.
	 */
	boolean isReady();
	int getIcon();
}
