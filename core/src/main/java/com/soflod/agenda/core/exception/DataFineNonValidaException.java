package com.soflod.agenda.core.exception;

public class DataFineNonValidaException extends Exception {

	public DataFineNonValidaException() {
		super("Data fine non valida. Ricorda che la data "
				+ "di inizio di default è il timestamp attuale.");
	}

	public DataFineNonValidaException(String arg0) {
		super(arg0);
	}

	public DataFineNonValidaException(Throwable arg0) {
		super(arg0);
	}

	public DataFineNonValidaException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public DataFineNonValidaException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

}
