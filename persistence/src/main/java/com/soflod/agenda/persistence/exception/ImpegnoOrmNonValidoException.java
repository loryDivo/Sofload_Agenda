package com.soflod.agenda.persistence.exception;

public class ImpegnoOrmNonValidoException extends Exception {

	public ImpegnoOrmNonValidoException() {
		super();
	}

	public ImpegnoOrmNonValidoException(String message) {
		super(message);
	}

	public ImpegnoOrmNonValidoException(Throwable cause) {
		super(cause);
	}

	public ImpegnoOrmNonValidoException(String message, Throwable cause) {
		super(message, cause);
	}

	public ImpegnoOrmNonValidoException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
