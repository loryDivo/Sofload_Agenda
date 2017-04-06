package com.soflod.agenda.core;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.soflod.agenda.core.exception.DataInizioNonValidaException;
import com.soflod.agenda.core.exception.DataScadenzaNonValidaException;
import com.soflod.agenda.core.exception.LimiteTitoloSuperatoException;

public class Attivita {

	private static Logger logger = Logger.getLogger(Attivita.class.getName());
	private static byte lunghezzaTitoloMax = 40;
	public static final String FORMATO_DATA_ATTVITA = "yyyy:MM:dd";
	private SimpleDateFormat sdfAttivita = new SimpleDateFormat(FORMATO_DATA_ATTVITA);

	private String titolo;
	private String dataInizio;
	private String dataScadenza;
	private Categoria categoria;

	public static class Builder {

		private SimpleDateFormat sdfAttivita = new SimpleDateFormat(FORMATO_DATA_ATTVITA);
		private String titolo;
		private String dataInizio = sdfAttivita.format(new Date());
		private String dataScadenza;
		private Categoria categoria = new Categoria();

		/**
		 * Viene impostato il titolo e la data di scadenza unici parametri
		 * obligatori Verifica se titolo minore di 40 caratteri e di data
		 * scadenza maggiore di data inizio in caso negativo lancio eccezione
		 * 
		 * @param titolo
		 * @param dataScadenza
		 * @throws LimiteTitoloSuperatoException
		 * @throws DataScadenzaNonValidaException
		 * @throws ParseException
		 */
		public Builder(String titolo, Date dataScadenza)
				throws LimiteTitoloSuperatoException, DataScadenzaNonValidaException, ParseException {

			if (verificaLunghezzaTitolo(titolo))
				this.titolo = titolo;
			else
				throw new LimiteTitoloSuperatoException();

			Date formatoDataInizio = sdfAttivita.parse(this.dataInizio);
			if (formatoDataInizio.before(dataScadenza))
				this.dataScadenza = sdfAttivita.format(dataScadenza);
			else
				throw new DataScadenzaNonValidaException();

		}

		/**
		 * inserimento data di inizio con verifica che sia minore della data di
		 * scadenza in caso negativo lancio eccezione
		 * 
		 * @param dataInizio
		 * @return
		 * @throws DataInizioNonValidaException
		 * @throws ParseException
		 */
		public Builder dataInizio(Date dataInizio) throws DataInizioNonValidaException {

			try {

				Date formatoDataScadenza = sdfAttivita.parse(this.dataScadenza);

				if (dataInizio.before(formatoDataScadenza))
					this.dataInizio = sdfAttivita.format(dataInizio);
				else
					throw new DataInizioNonValidaException();
			} catch (ParseException pe) {
				logger.log(Level.SEVERE, pe.getMessage());
			}

			return this;
		}

		public Builder categoria(Categoria categoria) {
			this.categoria = categoria;
			return this;
		}

		public Attivita build() {
			return new Attivita(this);
		}

	}

	// Metodi della classe Attivita

	private Attivita(Builder builder) {
		this.titolo = builder.titolo;
		this.categoria = builder.categoria;
		this.dataInizio = builder.dataInizio;
		this.dataScadenza = builder.dataScadenza;
	}

	public SimpleDateFormat getSdfAttivita() {
		return sdfAttivita;
	}

	public String getTitolo() {
		return this.titolo;
	}

	/**
	 * Settaggio titolo con lancio eccezione in caso di superamento di 40
	 * caratteri
	 * 
	 * @param titolo
	 * @throws LimiteTitoloSuperatoException
	 */
	public void setTitolo(String titolo) throws LimiteTitoloSuperatoException {
		if (verificaLunghezzaTitolo(titolo))
			this.titolo = titolo;
		else
			throw new LimiteTitoloSuperatoException();
	}

	public Date getDataInizio() {

		Date formatoDataInizio = null;

		try {
			formatoDataInizio = sdfAttivita.parse(this.dataInizio);
		} catch (ParseException pe) {
			logger.log(Level.SEVERE, pe.getMessage());
		}

		return formatoDataInizio;
	}

	/**
	 * Settaggio data inizio con lancio eccezione in caso di data inizio
	 * maggiore di data scadenza
	 * 
	 * @param dataInizio
	 * @throws DataInizioNonValidaException
	 * @throws ParseException
	 */
	public void setDataInizio(Date dataInizio) throws DataInizioNonValidaException {

		try {
			Date formatoDataScadenza = sdfAttivita.parse(this.dataScadenza);

			if (dataInizio.before(formatoDataScadenza))
				this.dataInizio = sdfAttivita.format(dataInizio);
			else
				throw new DataInizioNonValidaException();
		} catch(ParseException pe) {
			logger.log(Level.SEVERE, pe.getMessage());
		}

	}

	public Date getDataScadenza() {
		Date formatoDataScadenza = null;
		try {
			formatoDataScadenza = sdfAttivita.parse(this.dataScadenza);
		} catch(ParseException pe) {
			logger.log(Level.SEVERE, pe.getMessage());
		}
		return formatoDataScadenza;
	}

	/**
	 * Settaggio data scadenza con eventuale eccezione in caso di data scadenza
	 * minore di data inizio
	 * 
	 * @param dataScadenza
	 * @throws DataScadenzaNonValidaException
	 * @throws ParseException
	 */
	public void setDataScadenza(Date dataScadenza) throws DataScadenzaNonValidaException {

		try {
			Date formatoDataInizio = sdfAttivita.parse(this.dataInizio);

			if (formatoDataInizio.before(dataScadenza))
				this.dataScadenza = sdfAttivita.format(dataScadenza);
			else
				throw new DataScadenzaNonValidaException();
		} catch (ParseException pe) {
			logger.log(Level.SEVERE, pe.getMessage());
		}

	}

	public Categoria getCategoria() {
		return categoria;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((categoria == null) ? 0 : categoria.hashCode());
		result = prime * result + ((dataInizio == null) ? 0 : dataInizio.hashCode());
		result = prime * result + ((dataScadenza == null) ? 0 : dataScadenza.hashCode());
		result = prime * result + ((titolo == null) ? 0 : titolo.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;

		if (obj instanceof Attivita) {

			Attivita other = (Attivita) obj;

			if (!this.titolo.equals(other.getTitolo()))
				return false;

			if (!this.dataInizio.equals(sdfAttivita.format(other.getDataInizio())))
				return false;

			if (!this.dataScadenza.equals(sdfAttivita.format(other.getDataScadenza())))
				return false;

			if (!this.categoria.equals(other.getCategoria()))
				return false;

			return true;
		}

		return false;

	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

	/*
	 * Stampa stringa di attività in formato JSON(non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "{\"attivita\" : {\"titolo\" : \""
				+ titolo + "\", \"categorie\" : "
				+ categoria
				+ ", \"dataInizio\" : \""
				+ dataInizio + "\", \"dataScadenza\" : \""
				+ dataScadenza + "\""
				+ "}}";
	}

	/*
	 * Verifico che il titolo non superi il massimo numero di caratteri
	 * consentito.
	 * 
	 * @param titolo Il titolo da verificare
	 */
	private static boolean verificaLunghezzaTitolo(String titolo) {
		return titolo.length() <= lunghezzaTitoloMax;
	}

}
