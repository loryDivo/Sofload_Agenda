package com.soflod.agenda.core.exception;

public class CategorieNonValideException extends Exception {

	public CategorieNonValideException() {
		super("Le categorie inserite non sono valide.");
	}

	public CategorieNonValideException(String message) {
		super(message);
	}

	public CategorieNonValideException(Throwable cause) {
		super(cause);
	}

	public CategorieNonValideException(String message, Throwable cause) {
		super(message, cause);
	}

	public CategorieNonValideException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
