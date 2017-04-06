package com.soflod.agenda.persistence;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.j256.ormlite.logger.Logger;
import com.j256.ormlite.logger.LoggerFactory;
import com.soflod.agenda.core.Attivita;
import com.soflod.agenda.persistence.exception.AggiuntaAttivitaException;
import com.soflod.agenda.persistence.exception.AttivitaNonValidaException;
import com.soflod.agenda.persistence.exception.AttivitaOrmNonValidaException;
import com.soflod.agenda.persistence.exception.ModificaAttivitaException;
import com.soflod.agenda.persistence.exception.RimozioneAttivitaException;

public class GestoreAttivitaORM {
	
	private List<AttivitaORM> attivitaORM;
	private Memory memory;
	private Logger logger  = LoggerFactory.getLogger(GestoreAttivitaORM.class);
	
	
	public GestoreAttivitaORM(Memory memory) {
		this.attivitaORM = new ArrayList<AttivitaORM>();
		this.memory = memory;
	}
	
	public List<Attivita> getAttivita() throws AttivitaNonValidaException {
		
		List<Attivita> attivita = new ArrayList<Attivita>();
		
		for (AttivitaORM attORM : this.attivitaORM) {
			attivita.add(attORM.restituisciAttivita());
		}
		
		return attivita;
	}
	
	public List<AttivitaORM> getAttivitaOrm() {
		return attivitaORM;
	}
	
	/**
	 * Aggiunge un attività
	 * Vengono gestite le possibili eccezioni
	 * e reindirizzate tutte ad "AggiuntaAttivitaException"
	 * successivamente gestita dall'agenda
	 * Viene verificata l'eventuale esistenza dell'attività
	 * e in caso positivo verrà lanciata l'eventuale eccezione
	 * In caso contrario viene aggiunta al db
	 * @param daAggiungere
	 * @throws AggiuntaAttivitaException
	 */
	
	public AttivitaORM aggiungiAttivita(Attivita daAggiungere) throws AggiuntaAttivitaException {

		try {
			if (memory.verificaEsistenzaAttivita(daAggiungere))
				throw new AttivitaOrmNonValidaException("L'attivita' e' gia' presente nel DB.");
		} catch (AttivitaOrmNonValidaException e) {
			throw new AggiuntaAttivitaException(e);
		} catch (SQLException e) {
			throw new AggiuntaAttivitaException(e);
		}
		
		try {
			// qui attivitaORM ha ancora id nullo
			AttivitaORM nuovaAttivitaORM = new AttivitaORM(daAggiungere, memory.getCategorieDAO());

			if (memory.inserisciAttivitaORM(nuovaAttivitaORM)) {
				// se l'inserimento è andato a buon fine, attivitaORM contiene
				// l'id
				// relativo
				// il record inserito.
				return nuovaAttivitaORM;
			}
			return null;
		} catch (SQLException e) {
			throw new AggiuntaAttivitaException(e);
		} catch (IOException e) {
			throw new AggiuntaAttivitaException(e);
		}

	}
	
	/**
	 * Viene rimossa una attivita
	 * Tutte le eccezioni vengono reindirizzate a
	 * "RimuoviAttivitaException" che verrà successivamente
	 * gestito
	 * La rimozione può generare eccezioni dobute
	 * al db oppure all'attività non presente 
	 * oppure alla cancellazione (problemi db)
	 * @param daEliminare
	 * @throws RimozioneAttivitaException
	 */
	
	public AttivitaORM rimuoviAttivitaORM(AttivitaORM daEliminare) throws RimozioneAttivitaException {
		
		try {
			if (memory.getAttivitaDAO().queryForId(daEliminare.getIdAttivita()) == null)
				throw new AttivitaOrmNonValidaException("L'attivita da eliminare"
						+ " non è presente");
		} catch (SQLException e) {
			throw new RimozioneAttivitaException(e);
		} catch (AttivitaOrmNonValidaException e) {
			throw new RimozioneAttivitaException(e);
		}

		try {
			if (memory.cancellaAttivitaORM(daEliminare))
				return daEliminare;
		} catch (SQLException e) {
			throw new RimozioneAttivitaException(e);
		}
		return null;
	}
	
	/**
	 * Viene modifica un attività da una nuova
	 * Tutte le eccezioni vengono reindirizzate 
	 * a "ModificaAttivitaException" successivamente gestita
	 * errori possono essere dovuti dal db 
	 * @param vecchiaAttivita
	 * @param nuovaAttivita
	 * @throws SQLException
	 */
	
	public boolean modificaAttivitaORM(AttivitaORM vecchiaAttivita, AttivitaORM nuovaAttivita) throws ModificaAttivitaException {
		try {
			if (memory.modificaAttivitaORM(vecchiaAttivita, nuovaAttivita)) {
				return true;
			}
		} catch (SQLException sqle) {
			logger.error(sqle, sqle.getMessage());
			throw new ModificaAttivitaException(sqle);
		}
		return false;
	}
	
	public void richiediAttivita(Date mese) throws SQLException {
		this.attivitaORM = new ArrayList<AttivitaORM>(memory.restituisciAttivitaNelMese(mese));
	}

}
