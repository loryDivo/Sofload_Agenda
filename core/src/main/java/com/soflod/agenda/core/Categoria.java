package com.soflod.agenda.core;

import java.awt.Color;

import com.soflod.agenda.core.exception.LimiteEtichettaSuperatoException;

public class Categoria {
	
	private static final Color COLORE_CATEGORIA_DEFAULT = Color.CYAN;
	private static final String ETICHETTA_CATEGORIA_DEFAULT = "Attività Libera";
	private static final byte LUNGHEZZA_MAX = 40;
	
	private String etichetta;
	private Color colore;
	private Priorita priorita;
	
	/**
	 * Viene impostata etichetta, colore e priorita
	 * con verifica che l'etichetta rispetti la lunghezza 
	 * massima di 40 caratteri
	 * in caso contrario lancio eccezione
	 * @param etichetta
	 * @param colore
	 * @param priorita
	 * @throws LimiteEtichettaSuperatoException
	 */
	public Categoria(String etichetta, Color colore, Priorita priorita) throws LimiteEtichettaSuperatoException {
	
		if (verificaLunghezzaEtichetta(etichetta))
			this.etichetta = etichetta;
		else
			throw new LimiteEtichettaSuperatoException();
		
		this.colore = colore;
		this.priorita = priorita;
	}
	
	public Categoria(String etichetta, Color colore) throws LimiteEtichettaSuperatoException {
		this(etichetta, colore, new Priorita());
	}
	
	public Categoria(String etichetta, Priorita priorita) throws LimiteEtichettaSuperatoException {
		this(etichetta, COLORE_CATEGORIA_DEFAULT, priorita);
	}
	
	public Categoria() {
		this.etichetta = ETICHETTA_CATEGORIA_DEFAULT;
		this.colore = COLORE_CATEGORIA_DEFAULT;
		this.priorita = new Priorita();
	}

	public String getEtichetta() {
		return etichetta;
	}

	/**
	 * Settaggio etichetta con verifica che non superi
	 * i 40 caratteri. in caso negativo lancio eccezione
	 * @param etichetta
	 * @throws LimiteEtichettaSuperatoException
	 */
	public void setEtichetta(String etichetta) throws LimiteEtichettaSuperatoException {
		if (verificaLunghezzaEtichetta(etichetta))
			this.etichetta = etichetta;
		else
			throw new LimiteEtichettaSuperatoException();
	}

	public Color getColore() {
		return colore;
	}

	public void setColore(Color colore) {
		this.colore = colore;
	}

	public Priorita getPriorita() {
		return priorita;
	}

	public void setPriorita(Priorita priorita) {
		this.priorita = priorita;
	}
	
	public static Color getColoreDefault() {
		return COLORE_CATEGORIA_DEFAULT;
	}
	
	private boolean verificaLunghezzaEtichetta(String etichetta) {
		int lun = etichetta.length();
		return lun <= LUNGHEZZA_MAX;
	}
		
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		
		if (obj instanceof Categoria) {
			Categoria categoria = (Categoria) obj;
			
			if (! this.etichetta.equals(categoria.getEtichetta()))
				return false;
			
			if (! this.colore.equals(categoria.getColore()))
				return false;
			
			if (! this.priorita.equals(categoria.getPriorita()))
				return false;
					
			return true;
		}
		
		return false;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((colore == null) ? 0 : colore.hashCode());
		result = prime * result + ((etichetta == null) ? 0 : etichetta.hashCode());
		result = prime * result + ((priorita == null) ? 0 : priorita.hashCode());
		return result;
	}

	/*
	 * Stampa stringa categoria in forma JSON(non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	
	@Override
	public String toString() {
		return "{\"categoria\" : {\"etichetta\" : \"" + etichetta +
				"\", \"colore\" : {" +
				"\"r\" : " + colore.getRed() + " ," +
				"\"g\" : " + colore.getGreen() + " ," +
				"\"b\" : " + colore.getBlue() + " ," +
				"\"a\" : " + colore.getAlpha() +
				"}, " +
				priorita.toString() + "}}";
	}	
}
