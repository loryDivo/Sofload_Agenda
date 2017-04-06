package com.soflod.agenda.persistence.exception;

public class RimozioneContattoException extends Exception {

	public RimozioneContattoException() {
		super("Non e' stato possibile rimuovere il contatto.");
	}

	public RimozioneContattoException(String message) {
		super(message);
	}

	public RimozioneContattoException(Throwable cause) {
		super(cause);
	}

	public RimozioneContattoException(String message, Throwable cause) {
		super(message, cause);
	}

	public RimozioneContattoException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
