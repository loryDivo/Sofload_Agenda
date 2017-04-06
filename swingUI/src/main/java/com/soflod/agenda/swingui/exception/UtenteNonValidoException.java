package com.soflod.agenda.swingui.exception;

public class UtenteNonValidoException extends Exception {

	public UtenteNonValidoException() {
		super("Registrazione non avvenuta");
	}

	public UtenteNonValidoException(String arg0) {
		super(arg0);
	}

	public UtenteNonValidoException(Throwable arg0) {
		super(arg0);
	}

	public UtenteNonValidoException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public UtenteNonValidoException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

}
