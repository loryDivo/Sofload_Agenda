package com.soflod.agenda.core;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.soflod.agenda.core.exception.DataFineNonValidaException;
import com.soflod.agenda.core.exception.DataInizioNonValidaException;
import com.soflod.agenda.core.exception.DateNonValideException;
import com.soflod.agenda.core.exception.LimiteNoteSuperatoException;
import com.soflod.agenda.core.exception.LimiteTitoloSuperatoException;

public class Impegno {

	private Logger logger = Logger.getLogger(Impegno.class.getName());
	public static  final String FORMATO_DATA_IMPEGNO = "yyyy:MM:dd HH:mm";
	private SimpleDateFormat sdfImpegno = new SimpleDateFormat(FORMATO_DATA_IMPEGNO);

	private String titolo;
	private Categoria categoria;
	private String dataInizio;
	private String dataFine;
	private boolean allarme;
	private String note;
	private Ripetizione ripetizione;
	private static byte lunghezzaMaxTitolo = 40;
	private static int lunghezzaMaxNote = 1024;

	public static class Builder {

		private SimpleDateFormat sdfImpegno = new SimpleDateFormat(FORMATO_DATA_IMPEGNO);
		// Parametri richiesti

		private String titolo;
		private Categoria categoria;
		private String dataInizio;
		private String dataFine;

		// Parametri opzionali - inizializzati di default

		private Ripetizione ripetizione = Ripetizione.NESSUNA;
		private boolean allarme = false;
		private String note = "";

		/**
		 * Viene impostato il titolo, la categoria, di 
		 * default la data di inizio è il timestamp attuale e la
		 * data di fine è da 1 ora a partire da quella attuale.
		 * Controllo se data inizio è minore della data
		 * fine e se data di fine è superiore per un massimo di 24 ore 
		 * rispetto a quella di inizio altrimenti eccezione 
		 * Controllo se titolo minore di 40 caratteri
		 * 
		 * @param titolo
		 * @param categoria
		 * @param dataFine
		 * @throws LimiteTitoloSuperatoException
		 * @throws DataFineNonValidaException
		 * @throws ParseException
		 */
		public Builder(String titolo, Categoria categoria)
				throws LimiteTitoloSuperatoException {

			if (verificaLunghezzaTitolo(titolo)) {
				this.titolo = titolo;
			} else {
				throw new LimiteTitoloSuperatoException();
			}
			this.categoria = categoria;
			Calendar calendario = Calendar.getInstance();
			
			this.dataInizio = sdfImpegno.format(calendario.getTime());
			calendario.add(Calendar.HOUR, 1);
			this.dataFine = sdfImpegno.format(calendario.getTime());
		}

		public Builder ripetizione(Ripetizione ripetizione) {
			this.ripetizione = ripetizione;
			return this;
		}

		public Builder allarme(boolean allarme) {
			this.allarme = allarme;
			return this;
		}

		/**
		 * Verifica se le note superato i 1024 caratteri altrimenti eccezione
		 * 
		 * @param note
		 * @return
		 * @throws LimiteNoteSuperatoException
		 */
		public Builder note(String note) throws LimiteNoteSuperatoException {
			if (verificaLunghezzaNote(note)) {
				this.note = note;
				return this;
			} else {
				throw new LimiteNoteSuperatoException();
			}
		}

		/**
		 * Verficia se la data di inizio è minore della data di fine altrimenti
		 * eccezione. In aggiunta la data di fine deve essere superiore per un
		 * massimo di 24 ore rispetto a quella di inizio
		 * 
		 * @param dataInizio
		 * @return
		 * @throws DataInizioNonValidaException
		 * @throws ParseException
		 */
		public Builder date(Date dataInizio, Date dataFine) throws DateNonValideException {
			
			if (verificaDate(dataInizio, dataFine)){
				this.dataInizio = sdfImpegno.format(dataInizio);
				this.dataFine = sdfImpegno.format(dataFine);
				return this;
			}else
				throw new DateNonValideException();
		}

		public Impegno build() {
			return new Impegno(this);
		}

	}

	private Impegno(Builder builder) {
		titolo = builder.titolo;
		categoria = builder.categoria;
		dataInizio = builder.dataInizio;
		dataFine = builder.dataFine;
		allarme = builder.allarme;
		ripetizione = builder.ripetizione;
		note = builder.note;
	}

	public String getTitolo() {
		return titolo;
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public Date getDataInizio() {
		Date formatoDataInizio = null;
		try {
			formatoDataInizio = sdfImpegno.parse(dataInizio);
		} catch (ParseException pe) {
			logger.log(Level.SEVERE, pe.getMessage());
		}
		return formatoDataInizio;
	}

	public Date getDataFine() {
		Date formatoDataFine = null;
		try {
			formatoDataFine = sdfImpegno.parse(dataFine);
		} catch (ParseException pe) {
			logger.log(Level.SEVERE, pe.getMessage());
		}
		return formatoDataFine;
	}

	public boolean getAllarme() {
		return allarme;
	}

	public String getNote() {
		return note;
	}

	public Ripetizione getRipetizione() {
		return ripetizione;
	}

	public SimpleDateFormat getSimpleDateFormat(){
		return sdfImpegno;
	}
	/**
	 * Settaggio titolo con eventuale verifica di lunghezza massima di 40
	 * caratteri con lancio eccezione in caso negativo
	 * 
	 * @param titolo
	 * @throws LimiteTitoloSuperatoException
	 */
	public void setTitolo(String titolo) throws LimiteTitoloSuperatoException {
		if (verificaLunghezzaTitolo(titolo)) {
			this.titolo = titolo;
		} else {
			throw new LimiteTitoloSuperatoException();
		}
	}

	/**
	 * Settaggio delle date di inizio  e di fine con eventuale verifica che data inizio sia prima di
	 * fine e quest'ultima non superiore a 24 ore con lancio eccezione in caso negativo.
	 * 
	 * @param dataInizio
	 * @throws DataInizioNonValidaException
	 */
	public void setDate(Date dataInizio, Date dataFine) throws DateNonValideException {
		if (verificaDate(dataInizio, dataFine)) {
			this.dataInizio = sdfImpegno.format(dataInizio);
			this.dataFine = sdfImpegno.format(dataFine);
		} else {
			throw new DateNonValideException();
		}
	}


	/**
	 * Settaggio note con eventuale verifica che note non superino i 1024
	 * caratteri con lancio eccezione in caso negativo
	 * 
	 * @param note
	 * @throws LimiteNoteSuperatoException
	 */
	public void setNote(String note) throws LimiteNoteSuperatoException {
		if (verificaLunghezzaNote(note)) {
			this.note = note;
		} else {
			throw new LimiteNoteSuperatoException();
		}
	}

	public void setAllarme(boolean allarme) {
		this.allarme = allarme;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

	public void setRipetizione(Ripetizione ripetizione) {
		this.ripetizione = ripetizione;
	}
	
	private static boolean verificaDate(Date dataInizio, Date dataFine) {
		if (dataInizio.before(dataFine)) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(dataInizio);
			calendar.add(Calendar.HOUR, 24);

			Date limiteImpegno = calendar.getTime();

			return dataFine.before(limiteImpegno);
		}
		
		return false;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (allarme ? 1231 : 1237);
		result = prime * result + ((categoria == null) ? 0 : categoria.hashCode());
		result = prime * result + ((dataFine == null) ? 0 : dataFine.hashCode());
		result = prime * result + ((dataInizio == null) ? 0 : dataInizio.hashCode());
		result = prime * result + ((note == null) ? 0 : note.hashCode());
		result = prime * result + ((ripetizione == null) ? 0 : ripetizione.hashCode());
		result = prime * result + ((titolo == null) ? 0 : titolo.hashCode());
		return result;
	}

	/*
	 * Metodo equals per verificare se due impegni sono uguali(non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		SimpleDateFormat sdf = new SimpleDateFormat(FORMATO_DATA_IMPEGNO);
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (obj instanceof Impegno) {
			Impegno other = (Impegno) obj;

			if (!this.titolo.equals(other.getTitolo())) {
				return false;
			}
			if (!this.categoria.equals(getCategoria())) {
				return false;
			}
			if (!this.note.equals(other.getNote())) {
				return false;
			}
			if (!this.ripetizione.equals(other.getRipetizione())) {
				return false;
			}
			if (!this.dataInizio.equals(sdf.format(other.getDataInizio()))) {
				return false;
			}
			if (!this.dataFine.equals(sdf.format(other.getDataFine()))) {
				return false;
			}
			return true;
		}
		return false;
	}

	/*
	 * Stampa string in formato JSON(non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {

		return "{\"impegno\" : {\"titolo\" : \"" + titolo + "\", \"categorie\" : " + categoria + ", \"dataInizio\" : \""
				+ dataInizio + "\", \"dataFine\" : \"" + dataFine + "\", \"allarme\" : \"" + allarme
				+ "\", \"note\" : \"" + note + "\", \"ripetizione\" : \"" + ripetizione + "\"" + "}}";
	}

	/**
	 * Verifico che il titolo abbia una lunghezza minore di 10 carazzeri
	 * 
	 * @param titolo titolo da inserire
	 */
	private static boolean verificaLunghezzaTitolo(String titolo) {
		return titolo.length() <= lunghezzaMaxTitolo;
	}

	/**
	 * Verifico che le note abbiano una lunghezza minore di 1024 caratteri
	 * 
	 * @param note note da inserire
	 */
	private static boolean verificaLunghezzaNote(String note) {
		return note.length() <= lunghezzaMaxNote;
	}
}
