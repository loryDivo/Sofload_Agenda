package com.soflod.agenda.persistence.exception;

public class AttivitaNonValidaException extends Exception {

	public AttivitaNonValidaException() {
		super("Attivita non valida");
	}

	public AttivitaNonValidaException(String arg0) {
		super(arg0);
	}

	public AttivitaNonValidaException(Throwable arg0) {
		super(arg0);
	}

	public AttivitaNonValidaException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public AttivitaNonValidaException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

}
