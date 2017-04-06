package com.soflod.agenda.swingui.exception;

public class PasswordException extends UtenteNonValidoException {

	public PasswordException() {
		super("La password non e' valida.");
	}

	public PasswordException(String arg0) {
		super(arg0);
	}

	public PasswordException(Throwable arg0) {
		super(arg0);
	}

	public PasswordException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public PasswordException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

}
