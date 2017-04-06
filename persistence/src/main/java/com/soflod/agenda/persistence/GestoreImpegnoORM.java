package com.soflod.agenda.persistence;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.j256.ormlite.logger.Logger;
import com.j256.ormlite.logger.LoggerFactory;
import com.soflod.agenda.core.Impegno;
import com.soflod.agenda.persistence.exception.AggiuntaImpegnoException;
import com.soflod.agenda.persistence.exception.ImpegnoOrmNonValidoException;
import com.soflod.agenda.persistence.exception.ModificaImpegnoException;
import com.soflod.agenda.persistence.exception.RimozioneImpegnoException;

public class GestoreImpegnoORM {

	private List<ImpegnoORM> impegniORM;

	private Memory memory;

	private Logger logger  = LoggerFactory.getLogger(GestoreImpegnoORM.class);
	
	public GestoreImpegnoORM(Memory memory) {
		this.impegniORM = new ArrayList<ImpegnoORM>();
		this.memory = memory;
	}

	public List<Impegno> getImpegni() throws ImpegnoOrmNonValidoException {

		List<Impegno> impegni = new ArrayList<Impegno>();

		for (ImpegnoORM impegnoORM : this.impegniORM) {
			impegni.add(impegnoORM.restituisciImpegno());
		}

		return impegni;
	}

	/**
	 * Viene aggiunto un impegno all'interno del db
	 * Vengono gestite le eccezioni con lancio di altra 
	 * eccezione ad agenda (AggiuntaImpegnoException)
	 * L'eccezione viene lanciata se l'impegno è già
	 * presente oppure se vi sono errori a livello di sql 
	 * @param impegno
	 * @throws AggiuntaImpegnoException
	 */
	
	public ImpegnoORM aggiungiImpegno(Impegno impegno) throws AggiuntaImpegnoException {

		try {
			if (memory.verificaEsistenzaImpegno(impegno)) {
				throw new ImpegnoOrmNonValidoException("L'impegno e' gia' presente nel DB.");
			}
		} catch (ImpegnoOrmNonValidoException e) {
			throw new AggiuntaImpegnoException(e);
		} catch (SQLException e) {
			throw new AggiuntaImpegnoException(e);
		}
		
		try {
			// qui impegnoORM ha ancora id nullo
			ImpegnoORM impegnoORM = new ImpegnoORM(impegno, memory.getCategorieDAO());
			if (memory.inserisciImpegnoORM(impegnoORM)) {
				// se l'inserimento è andato a buon fine, impegnoORM contiene
				// l'id relativo
				// il record inserito.
				return impegnoORM;

			} 
			return null;
		}catch(SQLException e) {
			throw new AggiuntaImpegnoException(e);
		} catch (IOException e) {
			throw new AggiuntaImpegnoException(e);
		}
	}

	/**
	 * Viene rimosso l'impegno dall'agenda
	 * Tutte le eccezioni vengono reindirizzate a 
	 * "RimozioneImpegnoException" che verrà successivamente
	 * gestito
	 * Vi possono essere errori dovuti a sql
	 * oppure riguardo il cancellamento effettivo dell'
	 * impegno dal db
	 * @param impegnoDaEliminare
	 * @throws RimozioneImpegnoException
	 */
	public ImpegnoORM rimuoviImpegnoORM(ImpegnoORM impegnoDaEliminare) throws RimozioneImpegnoException {
		
		try {
			if (memory.getImpegniDAO().queryForId(impegnoDaEliminare.getIdImpegno()) == null)
				throw new ImpegnoOrmNonValidoException("L'impegno non puo' essere eliminato. Non "
						+ "e' presente nel DB.");
		} catch (SQLException e) {
			throw new RimozioneImpegnoException(e);
		} catch (ImpegnoOrmNonValidoException e) {
			throw new RimozioneImpegnoException(e);
		}
		
		try {
			if (memory.cancellaImpegnoORM(impegnoDaEliminare))
				return impegnoDaEliminare;
		} catch (SQLException e) {
			throw new RimozioneImpegnoException(e);
		}
		return null;
	}

	/**
	 * Viene modifica un impegno
	 * Tutte le eccezioni vengono reindirizzate a 
	 * "ModificaContattoException" e gestite successivamente
	 * Vi possono essere problemi riguardo la modifica 
	 * @param vecchioImpegno
	 * @param nuovoImpegno
	 * @throws SQLException
	 * @throws ModificaImpegnoException
	 */
	
	public boolean modificaImpegnoORM(ImpegnoORM vecchioImpegno, ImpegnoORM nuovoImpegno) throws ModificaImpegnoException{
		try {
			if (memory.modificaImpegnoORM(vecchioImpegno, nuovoImpegno)) {
				return true;
			}
		} catch (SQLException sqle) {
			logger.error(sqle, sqle.getMessage());
			throw new ModificaImpegnoException(sqle);
		}
		return false;
	}
	
	public void richiediImpegni(Date da, Date a) throws SQLException{
		this.impegniORM = new ArrayList<ImpegnoORM>(memory.restituisciImpegniIn(da, a));
	}
	
	public List<ImpegnoORM> getImpegniORM() {
		return impegniORM;
	}
}
