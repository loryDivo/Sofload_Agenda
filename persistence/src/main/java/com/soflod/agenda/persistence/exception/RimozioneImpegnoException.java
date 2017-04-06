package com.soflod.agenda.persistence.exception;

public class RimozioneImpegnoException extends Exception {

	public RimozioneImpegnoException() {
		super("Non e' stato possibile eliminare l'impegno.");
	}

	public RimozioneImpegnoException(String message) {
		super(message);
	}

	public RimozioneImpegnoException(Throwable cause) {
		super(cause);
	}

	public RimozioneImpegnoException(String message, Throwable cause) {
		super(message, cause);
	}

	public RimozioneImpegnoException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
