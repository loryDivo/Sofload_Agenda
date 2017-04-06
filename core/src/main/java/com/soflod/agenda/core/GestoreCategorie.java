package com.soflod.agenda.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.soflod.agenda.core.exception.CategorieNonValideException;

public class GestoreCategorie {

	private List<Categoria> categorie;

	public GestoreCategorie() {
		super();
		this.categorie = new ArrayList<Categoria>();
	}
	
	public GestoreCategorie(List<Categoria> categorie) throws CategorieNonValideException {

		super();
		try {
			verificaCategorie(categorie);
		} catch (CategorieNonValideException cnv) {
			throw cnv;
		}

		/*
		 * IMPORTANTE: così creo un duplicato di categorie, ed è corretto. Non
		 * voglio che this.categorie sia lo stesso oggetto di categorie, voglio
		 * solo i suoi valori.
		 */
		this.categorie = new ArrayList<Categoria>(categorie);
	}

	/**
	 * Aggiunge una categoria al contatto, se la categoria è già presente,
	 * questa non viene aggiunta, senza però generare errori; in tal caso
	 * l'esito dell'aggiunta sarà comunque true.
	 * 
	 * @param categoria
	 * @return
	 */
	public boolean aggiungiCategoria(Categoria categoria) {

		if (this.categorie.contains(categoria))
			return true;
		return this.categorie.add(categoria);
	}

	/**
	 * Rimuove la categoria dal contatto. Se la categoria non è presente, viene
	 * restituito null, altrimenti viene ritornata la Categoria eliminata.
	 * 
	 * Nel contatto deve essere sempre presente almeno una categoria, altrimenti
	 * viene lanciata una eccezione.
	 * 
	 * @param categoria
	 * @return
	 * @throws CategorieNonValideException
	 */
	public Categoria rimuoviCategoria(Categoria categoria) throws CategorieNonValideException {

		if (this.categorie.contains(categoria)) {

			if (this.categorie.size() == 1)
				throw new CategorieNonValideException("Deve essere presente almeno una categoria nel contatto.");

			this.categorie.remove(categoria);
			return categoria;
		} else
			return null;
	}

	/**
	 * Ritorna true se categorie non contiene due oggetti di Categoria uguali.
	 * 
	 * @param categorie
	 *            le categorie da analizzare
	 * @return
	 */
	private static boolean verificaNoDoppioniCategorie(final List<Categoria> categorie) {

		/*
		 * L'attributo final mi dice che categorie (intesa come variabile) non
		 * può essere riassegnata, ma potrei comunque accedere e modificare
		 * l'oggetto referenziato dalla variabile, quindi è bene tenere a mente
		 * che non ho un oggetto IMMUTABILE.
		 */

		/*
		 * Ricordare che se avessi fatto: tempCategorie = this.categorie sarebbe
		 * stato un errore, in quanto avrei assegnato il puntatore di categorie
		 * a tempCategorie e avrei modificato indirettamente il parametro
		 * categorie.
		 * 
		 */
		List<Categoria> tmpCategorie = new ArrayList<Categoria>(categorie);
		List<Categoria> noDoppi = new ArrayList<Categoria>();
		Iterator<Categoria> catIterator = tmpCategorie.iterator();
		Categoria tmp;
		while (catIterator.hasNext()) {

			tmp = catIterator.next();
			catIterator.remove();

			// se contiene un doppione
			if (tmpCategorie.contains(tmp))
				return false;

			noDoppi.add(tmp);
		}

		return true;
	}

	private static void verificaCategorie(List<Categoria> categorie) throws CategorieNonValideException {
		if (categorie.isEmpty())
			throw new CategorieNonValideException("Deve essere presente almeno una" + "categoria valida");
		if (!verificaNoDoppioniCategorie(categorie))
			throw new CategorieNonValideException("Tra le categorie non ci devono" + "essere doppioni");

	}
	
	public List<Categoria> getCategorie() {
		return categorie;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((categorie == null) ? 0 : categorie.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		
		if (obj == null)
			return false;
		
		if (obj instanceof GestoreCategorie) {
			
			GestoreCategorie other = (GestoreCategorie) obj;
			
			if (this.categorie.size() != other.getCategorie().size())
				return false;

			for (Categoria categoria : other.getCategorie()) {
				if (!this.categorie.contains(categoria))
					return false;
			}
			
			return true;
		}
		
		return false;
	}
	
	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		
		Iterator<Categoria> catIt = this.getCategorie().iterator();
		Categoria temp;
		
		while (catIt.hasNext()) {
			temp = catIt.next();
			
			if (catIt.hasNext())
				str.append(temp.toString() + ", ");
			else
				str.append(temp.toString());
		}
		return str.toString();
	}

}
