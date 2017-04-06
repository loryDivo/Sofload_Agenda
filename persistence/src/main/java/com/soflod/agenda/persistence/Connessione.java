package com.soflod.agenda.persistence;

import java.io.IOException;
import java.sql.SQLException;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.logger.Logger;
import com.j256.ormlite.logger.LoggerFactory;
import com.j256.ormlite.support.ConnectionSource;
import com.soflod.agenda.persistence.exception.ConnessioneException;

public class Connessione {

	private ConnectionSource istanzaConnessione = null;
	private Logger logger  = LoggerFactory.getLogger(Connessione.class);
	
	/**
	 * Istanziazione di una connessione se esiste
	 * In caso negativo lancio eccezione
	 * @param path
	 * @throws ConnessioneException 
	 */
	
	public Connessione(String path) throws ConnessioneException {
		try {
			if (istanzaConnessione == null)
				this.istanzaConnessione = new JdbcConnectionSource("jdbc:sqlite:" + path);
		} catch (SQLException sqle) {
			logger.error(sqle.getMessage());
			throw new ConnessioneException(sqle);
		}
		
	}
	
	/**
	 * Richiesta istanza di connessione
	 * @return
	 */
	
	public ConnectionSource getIstanzaConnessione() {
		return this.istanzaConnessione;
	}
	
	/**
	 * Chiusura connessione se ve ne 
	 * è una
	 */
	
	public void chiudiConnessione() throws ConnessioneException{
		try {
			if (this.istanzaConnessione != null)
				this.istanzaConnessione.close();
			this.istanzaConnessione = null;
		} catch (IOException e) {
			logger.error(e.getMessage());
			throw new ConnessioneException(e);
		}
	}

}
