package com.soflod.agenda.persistence.exception;

public class ConnessioneException extends Exception {

	public ConnessioneException() {
		super("Impossibile stabilire la connessione con il db");
	}

	public ConnessioneException(String arg0) {
		super(arg0);
	}

	public ConnessioneException(Throwable arg0) {
		super(arg0);
	}

	public ConnessioneException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public ConnessioneException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

}
