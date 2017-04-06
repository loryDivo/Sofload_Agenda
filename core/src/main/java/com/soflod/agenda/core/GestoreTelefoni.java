package com.soflod.agenda.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.soflod.agenda.core.exception.TelefoniNonValidiException;

public class GestoreTelefoni {

	private List<Telefono> telefoni;

	public GestoreTelefoni() {
		super();
		this.telefoni = new ArrayList<Telefono>();
	}

	public GestoreTelefoni(List<Telefono> telefoni) throws TelefoniNonValidiException {
		super();
		if (!verificaNoDoppioniTelefoni(telefoni))
			throw new TelefoniNonValidiException("I telefoni aggiunti" + "non devono contenere doppioni.");

		this.telefoni = new ArrayList<Telefono>(telefoni);
	}

	public List<Telefono> getTelefoni() {
		return telefoni;
	}

	/**
	 * Aggiunge un telefono al contatto, se il telefono è già presente, questo
	 * non viene aggiunto, senza però generare errori; in tal caso l'esito
	 * dell'aggiunta sarà comunque true.
	 * 
	 * @param telefono
	 * @return
	 */
	public boolean aggiungiTelefono(Telefono telefono) {
		if (this.telefoni.contains(telefono))
			return true;
		return this.telefoni.add(telefono);
	}

	/**
	 * Rimuove il telefono dal contatto. Se il telefono non è presente, viene
	 * restituito null, altrimenti viene ritornato il Telefono eliminato.
	 * 
	 * @param telefono
	 * @return
	 */
	public Telefono rimuoviTelefono(Telefono telefono) {
		if (this.telefoni.contains(telefono)) {
			this.telefoni.remove(telefono);

			return telefono;
		}

		return null;
	}

	private static boolean verificaNoDoppioniTelefoni(final List<Telefono> telefoni) {

		List<Telefono> tmpTelefoni = new ArrayList<Telefono>(telefoni);
		List<Telefono> noDoppi = new ArrayList<Telefono>();
		Iterator<Telefono> telefonoIterator = tmpTelefoni.iterator();
		Telefono tmp;
		while (telefonoIterator.hasNext()) {

			tmp = telefonoIterator.next();
			telefonoIterator.remove();

			// se contiene un doppione
			if (tmpTelefoni.contains(tmp))
				return false;

			noDoppi.add(tmp);
		}
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((telefoni == null) ? 0 : telefoni.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;

		if (obj == null)
			return false;

		if (obj instanceof GestoreTelefoni) {

			GestoreTelefoni other = (GestoreTelefoni) obj;

			// equals sui telefoni, da ridistribuire nella extract class
			if (this.telefoni.size() != other.getTelefoni().size())
				return false;

			for (Telefono telefono : other.getTelefoni()) {
				if (!this.telefoni.contains(telefono))
					return false;
			}

			return true;
		}
		return false;
	}
	
	@Override
	public String toString(){
		StringBuilder str = new StringBuilder();
		
		Iterator<Telefono> telIt = this.getTelefoni().iterator();
		Telefono temp;
		
		while (telIt.hasNext()) {
			temp = telIt.next();
			
			if (telIt.hasNext())
				str.append(temp.toString() + ", ");
			else
				str.append(temp.toString());
		}
		
		return str.toString();
	}

}
