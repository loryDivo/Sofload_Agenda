package com.soflod.agenda.core;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.soflod.agenda.core.exception.NumeroTelefonoNonValidoException;

public class Telefono {
	
	private String numeroTelefono;

	private static final Pattern TELEFONO_PATTERN =
			Pattern.compile("^[+]?[\\d]{1,30}");
	
	public Telefono(String telefono) throws NumeroTelefonoNonValidoException {
		super();
		if (verificaTelefono(telefono))
			this.numeroTelefono = telefono;
		else
			throw new NumeroTelefonoNonValidoException();
	}
	
	public String getNumeroTelefono() {
		return numeroTelefono;
	}

	public void setNumeroTelefono(String telefono) throws NumeroTelefonoNonValidoException {
		if (verificaTelefono(telefono))
			this.numeroTelefono = telefono;
		else
			throw new NumeroTelefonoNonValidoException();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		
		if (obj instanceof Telefono)
			return this.numeroTelefono.equals(((Telefono) obj).getNumeroTelefono());
		
		return false;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((numeroTelefono == null) ? 0 : numeroTelefono.hashCode());
		return result;
	}

	
	
	@Override
	public String toString() {
		return "{\"telefono\" : \"" + this.numeroTelefono + "\"}"; 
	}
	
	private boolean verificaTelefono(String telefono) {
		Matcher matcher = TELEFONO_PATTERN.matcher(telefono);
		return matcher.matches();
	}
	
	
	
	
	
}
