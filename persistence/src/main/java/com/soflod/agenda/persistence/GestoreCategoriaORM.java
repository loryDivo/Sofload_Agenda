package com.soflod.agenda.persistence;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.soflod.agenda.core.Categoria;
import com.soflod.agenda.persistence.exception.AggiuntaCategoriaException;
import com.soflod.agenda.persistence.exception.CategoriaOrmNonValidaException;
import com.soflod.agenda.persistence.exception.ModificaCategoriaException;
import com.soflod.agenda.persistence.exception.RimozioneCategoriaException;

public class GestoreCategoriaORM {

	private List<CategoriaORM> categorieOrm;
	
	private Memory memory;
	
	public GestoreCategoriaORM(Memory memory) {
		this.categorieOrm = new ArrayList<CategoriaORM>();
		this.memory = memory;
	}

	/**
	 * Viene aggiunta una categoria
	 * tutte le eccezioni vengono reindirizzate 
	 * a "AggiuntaCategoriaException" che verrà
	 * gestita successivamente
	 * Vi è la possibilità che la categoria esista già
	 * oppure che l'inserimento non avvenga correttamente
	 * @param categoria
	 * @throws AggiuntaCategoriaException
	 */
	
	public void aggiungiCategoria(Categoria categoria) throws  AggiuntaCategoriaException {

		try {
			if (memory.verificaEsistenzaCategoria(categoria)){
				throw new CategoriaOrmNonValidaException("La categoria e' già presente nel DB.");
			}
		
			// qui CategoriaORM ha ancora id nullo
			CategoriaORM categoriaORM = new CategoriaORM(categoria, memory.getCategorieDAO());

			if (memory.inserisciCategoriaORM(categoriaORM)) {
				/* se l'inserimento è andato a buon fine, CategoriaORM contiene
				 * l'id relativo il record inserito.
				 */
				this.categorieOrm.add(categoriaORM);
			} else
				throw new SQLException();
		} catch(SQLException e) {
			throw new AggiuntaCategoriaException(e);
		} catch (IOException e) {
			throw new AggiuntaCategoriaException(e);
		} catch (CategoriaOrmNonValidaException e) {
			throw new AggiuntaCategoriaException(e);
		}
	}
	
	/**
	 * Viene rimossa una categoria dal db
	 * tutte le eccezioni vengono reindirizzate a 
	 * "RimozioneCategoriaException" successivamente
	 * gestita
	 * Vi possono essere problemi riguardo 
	 * l'eliminiazione dal db e quindi problemi
	 * sql
	 * oppure problemi riguardo la ricerca della categoria
	 * nel db 
	 * @param daEliminare
	 * @throws RimozioneCategoriaException
	 */
	
	public CategoriaORM rimuoviCategoriaORM(CategoriaORM daEliminare) throws RimozioneCategoriaException {
		
		try {
			if (memory.getCategorieDAO().queryForId(daEliminare.getIdCategoria()) == null)
				throw new CategoriaOrmNonValidaException("La categoria non e' contenuta nel DB.");
			
			if (memory.cancellaCategoriaORM(daEliminare))
				return daEliminare;
			
		} catch (SQLException e) {
			throw new RimozioneCategoriaException(e);
		} catch (CategoriaOrmNonValidaException e) {
			throw new RimozioneCategoriaException(e);
		}
		return null;
	}
	
	public List<CategoriaORM> getCategorieOrm() {
		return categorieOrm;
	}

	/**
	 * Modifica di una categoria
	 * tutte le eccezioni vengono reindirizzate
	 * a "ModificaCategoriaException" successivamente 
	 * gestita
	 * vi possono essere eccezioni dovute dal db 
	 * oppure dalla modifica della categoria non possibile
	 * @param vecchiaCategoriaORM
	 * @param nuovaCategoriaORM
	 * @throws ModificaCategoriaException
	 */
	
	public boolean modificaCategoriaORM (CategoriaORM vecchiaCategoriaORM, CategoriaORM nuovaCategoriaORM) throws ModificaCategoriaException {
		
		try {
			if (memory.modificaCategoriaORM(vecchiaCategoriaORM, nuovaCategoriaORM)) {
				return true;
			}
		} catch (SQLException e) {
			throw new ModificaCategoriaException(e);
		}
		return false;
	}
	
	public List<Categoria> getCategorie() throws CategoriaOrmNonValidaException {
		List<Categoria> categorie = new ArrayList<Categoria>();
		for (CategoriaORM categoriaOrm : categorieOrm) {
			categorie.add(categoriaOrm.restituisciCategoria());
		}
		return categorie;
	}
	
	public void aggiornaCategorie() throws SQLException {
		this.categorieOrm = new ArrayList<CategoriaORM>(memory.restituisciCategorieORM());
	}
	
}
