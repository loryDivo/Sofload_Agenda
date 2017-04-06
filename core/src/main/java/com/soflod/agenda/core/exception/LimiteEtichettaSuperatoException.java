package com.soflod.agenda.core.exception;

public class LimiteEtichettaSuperatoException extends Exception {

	public LimiteEtichettaSuperatoException() {
		super("Il massimo numero di caratteri per l'etichetta e' stato " +
				"superato");
	}

	public LimiteEtichettaSuperatoException(String message) {
		super(message);
	}

	public LimiteEtichettaSuperatoException(Throwable cause) {
		super(cause);
	}

	public LimiteEtichettaSuperatoException(String message, Throwable cause) {
		super(message, cause);
	}

	public LimiteEtichettaSuperatoException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
