package com.soflod.agenda.core;

import com.soflod.agenda.core.exception.PrioritaNonValidaException;

public class Priorita {
	
	private byte valorePriorita;
	private static final byte PRIORITA_DEFAULT = 1;
	
	public Priorita(byte priorita) throws PrioritaNonValidaException {
		
		if (verificaPriorita(priorita))
			this.valorePriorita = priorita;
		else
			throw new PrioritaNonValidaException();
	}
	
	public Priorita() {
		this.valorePriorita = PRIORITA_DEFAULT;
	}
	
	private boolean verificaPriorita(byte priorita) {
		return priorita > 0 && priorita <= 10;
	}

	public byte getValorePriorita() {
		return valorePriorita;
	}

	public void setValorePriorita(byte priorita) throws PrioritaNonValidaException {
		if (verificaPriorita(priorita))
			this.valorePriorita = priorita;
		else
			throw new PrioritaNonValidaException();
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return true;
		
		if (obj instanceof Priorita) {
			Priorita priorita = (Priorita) obj;
			if (this.valorePriorita == priorita.getValorePriorita())
				return true;
		}
		
		return false;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + valorePriorita;
		return result;
	}


	@Override
	public String toString() {
		return "\"priorita\" : " + valorePriorita;
	}
	
	public int compareTo(Priorita altraPriorita) {
		
		Byte v1 = this.valorePriorita;
		Byte v2 = altraPriorita.getValorePriorita();
		return v1.compareTo(v2);
		
	}

}
