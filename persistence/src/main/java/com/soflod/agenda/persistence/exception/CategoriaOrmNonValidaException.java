package com.soflod.agenda.persistence.exception;

public class CategoriaOrmNonValidaException extends Exception {

	public CategoriaOrmNonValidaException() {
		super("Categoria ORM non valida");
	}

	public CategoriaOrmNonValidaException(String message) {
		super(message);
	}

	public CategoriaOrmNonValidaException(Throwable cause) {
		super(cause);
	}

	public CategoriaOrmNonValidaException(String message, Throwable cause) {
		super(message, cause);
	}

	public CategoriaOrmNonValidaException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
