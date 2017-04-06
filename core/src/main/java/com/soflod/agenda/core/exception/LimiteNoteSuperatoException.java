package com.soflod.agenda.core.exception;

public class LimiteNoteSuperatoException extends Exception {

	public LimiteNoteSuperatoException() {
		super("Massimo numero di note possibili è stato superato");
	}

	public LimiteNoteSuperatoException(String message) {
		super(message);
	}

	public LimiteNoteSuperatoException(Throwable cause) {
		super(cause);
	}

	public LimiteNoteSuperatoException(String message, Throwable cause) {
		super(message, cause);
	}

	public LimiteNoteSuperatoException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
