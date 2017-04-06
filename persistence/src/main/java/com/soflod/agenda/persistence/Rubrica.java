package com.soflod.agenda.persistence;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.soflod.agenda.core.Contatto;
import com.soflod.agenda.persistence.exception.AggiuntaContattoException;
import com.soflod.agenda.persistence.exception.ContattoOrmNonValidoException;
import com.soflod.agenda.persistence.exception.ModificaContattoException;
import com.soflod.agenda.persistence.exception.RimozioneContattoException;


public class Rubrica {
	
	private List<ContattoORM> contattiORM;
	private Memory memory;
	
	public Rubrica(Memory memory) {
		this.contattiORM = new ArrayList<ContattoORM>();
		this.memory = memory;
	}
	
	public List<Contatto> getContatti() throws ContattoOrmNonValidoException {
		
		List<Contatto> contatti = new ArrayList<Contatto>();
		
		for (ContattoORM contattoORM : this.contattiORM) {
				contatti.add(contattoORM
						.restituisciContatto(memory.getContattiCategorieDAO(),
								memory.getCategorieDAO()));
		}
		
		return contatti;
	}
	
	/**
	 * Viene aggiunto un contatto
	 * tutte le eccezioni vengono reindirizzate
	 * ad "AggiuntaContattoException" che verrà gestito 
	 * successivamente dagli eventuali gestori
	 * Vi possono essere eccezioni dovute all'esistenza
	 * del contatto in memoria
	 * oppure all'inserimento errato del contatto in memoria
	 * @param contatto
	 * @throws AggiuntaContattoException
	 */
	
	public void aggiungiContatto(Contatto contatto) throws AggiuntaContattoException {
		
		try {
			if (memory.verificaEsistenzaContatto(contatto))
				throw new ContattoOrmNonValidoException("Il contatto e' gia' presente nel DB.");
		} catch (ContattoOrmNonValidoException e) {
			throw new AggiuntaContattoException(e);
		} catch (SQLException e) {
			throw new AggiuntaContattoException(e);
		}
		
		try {
			ContattoORM contattoORM = new ContattoORM(contatto, memory.getCategorieDAO());

			if (memory.inserisciContattoORM(contattoORM))
				this.contattiORM.add(contattoORM);

		} catch(IOException e) {
			throw new AggiuntaContattoException(e);
		} catch (SQLException e) {
			throw new AggiuntaContattoException(e);
		}
	}
	
	/**
	 * Viene rimosso un contatto
	 * Tutte le eccezioni vengono reindirizzate
	 * a "RimozioneContattoException"
	 * Vi possono essere eccezion dobute dalla
	 * richiesta del contatto non esistente
	 * oppure errori dovuti dal db
	 * @param daRimuovere
	 * @throws RimozioneContattoException
	 */
	
	public ContattoORM rimuoviContattoORM(ContattoORM daRimuovere) throws RimozioneContattoException {
		try {
			if (memory.getContattiDAO().queryForId(daRimuovere.getIdContatto()) == null)
				throw new ContattoOrmNonValidoException("Il contatto " + "da eliminare non esiste.");

			if (memory.cancellaContattoORM(daRimuovere))
				return daRimuovere;
		} catch(SQLException e) {
			throw new RimozioneContattoException(e);
		} catch (ContattoOrmNonValidoException e) {
			throw new RimozioneContattoException(e);
		}
		return null;
	}
	
	public List<ContattoORM> getContattiORM() {
		return contattiORM;
	}

	/**
	 * Viene modificato il contatto 
	 * rimuovendolo e poi riaggiungendolo
	 * tutte le eccezioni vengono reindirizzate a 
	 * "ModificaContattoException" che verrà gestito
	 * successivamente
	 * Vi possono essere errori dovuti dal db
	 * @param vecchioContatto
	 * @param nuovoContatto
	 * @throws ModificaContattoException
	 */
	
	public boolean modificaContattoORM(ContattoORM vecchioContatto, ContattoORM nuovoContatto) throws ModificaContattoException {
		
		try {
			ContattoORM contattoOrmModificato;
			contattoOrmModificato = memory.modificaContattoORM(vecchioContatto, nuovoContatto);
			if (contattoOrmModificato != null) {
				if(contattiORM.contains(vecchioContatto)){
					contattiORM.remove(vecchioContatto);
					contattiORM.add(nuovoContatto);
				}
				return true;
			}
		} catch (SQLException e) {
			throw new ModificaContattoException(e);
		}
		return false;
	}

	public void aggiornaContatti() throws SQLException {
		this.contattiORM = new ArrayList<ContattoORM>(memory.restituisciContattiORM());
	}
	
}
