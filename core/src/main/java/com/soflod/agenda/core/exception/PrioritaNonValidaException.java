package com.soflod.agenda.core.exception;

public class PrioritaNonValidaException extends Exception {

	public PrioritaNonValidaException() {
		super ("La priorita' deve rientrare nel range [1, 10]");
	}

	public PrioritaNonValidaException(String message) {
		super(message);
	}

	public PrioritaNonValidaException(Throwable cause) {
		super(cause);
	}

	public PrioritaNonValidaException(String message, Throwable cause) {
		super(message, cause);
	}

	public PrioritaNonValidaException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
