package com.soflod.agenda.persistence.exception;

public class InizializzazioneAgendaException extends Exception {

	public InizializzazioneAgendaException() {
		super("Inizializzaione agenda non avvenuta correttamente");
	}

	public InizializzazioneAgendaException(String message) {
		super(message);
	}

	public InizializzazioneAgendaException(Throwable cause) {
		super(cause);
	}

	public InizializzazioneAgendaException(String message, Throwable cause) {
		super(message, cause);
	}

	public InizializzazioneAgendaException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
