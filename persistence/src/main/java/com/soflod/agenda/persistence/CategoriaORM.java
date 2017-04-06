package com.soflod.agenda.persistence;

import java.awt.Color;
import java.io.IOException;

import com.j256.ormlite.dao.CloseableWrappedIterable;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.*;
import com.soflod.agenda.core.Categoria;
import com.soflod.agenda.core.Priorita;
import com.soflod.agenda.core.exception.LimiteEtichettaSuperatoException;
import com.soflod.agenda.core.exception.PrioritaNonValidaException;
import com.soflod.agenda.persistence.exception.CategoriaOrmNonValidaException;

/**
 * 
 * CateogoriaORM astrae la classe categora, mappandola
 * ad oggetti primitivi di un classico RDBMS (il target
 * sarà SQLite).
 * 
 * @author soflod
 *
 */
@DatabaseTable (tableName = "Categorie")
public class CategoriaORM {
	
	/*
	 *  Usiamo Integer in quanto l'id potrebbe non essere stato impostato, quindi
	 *  potrebbe avere valore nullo.
	 */
	
	
	public static final String ETICHETTA = "etichetta";
	public static final String PRIORITA = "priorita";
	public static final String COLORE = "colore";
	
	@DatabaseField (generatedId = true)
	private Integer idCategoria;
	
	@DatabaseField (columnName = ETICHETTA, canBeNull = false)
	private String etichettaCategoria;
	
	@DatabaseField (columnName = PRIORITA, canBeNull = false)
	private Byte prioritaCategoria;
	
	@DatabaseField (columnName = COLORE, canBeNull = false)
	private String coloreCategoria;
	
	public CategoriaORM(Categoria categoria) {
	
		super();
		this.idCategoria = null;
		this.etichettaCategoria = categoria.getEtichetta();
		this.prioritaCategoria = categoria.getPriorita().getValorePriorita();
		this.coloreCategoria = convertiColore(categoria.getColore());
		
	}

	public CategoriaORM(Categoria categoria, Dao<CategoriaORM, Integer> categoriaDAO) throws IOException {
		this(categoria);
		this.idCategoria = verificaEsistenza(categoriaDAO);
		
	}
	
	public CategoriaORM() {
		super();
		this.idCategoria = null;
		this.etichettaCategoria = null;
		this.prioritaCategoria = null;
		this.coloreCategoria = null;
	}
	
	public Categoria restituisciCategoria() throws CategoriaOrmNonValidaException {
		
		Categoria mappa = new Categoria();		
		try {
			mappa.setColore(convertiColore(this.coloreCategoria));
			
			mappa.setEtichetta(etichettaCategoria);
			mappa.setPriorita(new Priorita((byte) this.prioritaCategoria));
		} catch(LimiteEtichettaSuperatoException lese) {
			throw new CategoriaOrmNonValidaException(lese);
		} catch (PrioritaNonValidaException e) {
			throw new CategoriaOrmNonValidaException(e);
		}
		return mappa;
	}
	
	private int[] restituisciCanaliColore(String colore) {
		
		int[] canaliColore = new int[]{0, 0, 0, 0};
		
		if ("".equals(colore))
			return canaliColore;
		
		String[] canali = colore.split("[:;]");
		
		try {
			canaliColore = new int[] {
					Integer.parseInt(canali[1]),
					Integer.parseInt(canali[3]),
					Integer.parseInt(canali[5]),
					Integer.parseInt(canali[7])
			};
		} catch(NumberFormatException nfe) {	
			canaliColore = new int[]{0, 0, 0, 0};
		}
		
		return canaliColore;
	}
	
	private String convertiColore(Color colore) {
		
		StringBuilder str = new StringBuilder();
		str.append("r:" + colore.getRed() + ";");
		str.append("g:" + colore.getGreen() + ";");
		str.append("b:" + colore.getBlue() + ";");
		str.append("a:" + colore.getAlpha());
		
		return str.toString();
	}
	
	private Color convertiColore(String stringaColore) {
		int[] canaliColore = restituisciCanaliColore(stringaColore);
		return new Color(
				canaliColore[0],
				canaliColore[1],
				canaliColore[2],
				canaliColore[3]
				);
	}

	public Integer getIdCategoria() {
		return idCategoria;
	}

	public void setIdCategoria(Integer idCategoria) {
		this.idCategoria = idCategoria;
	}

	public String getEtichetta() {
		return etichettaCategoria;
	}

	public void setEtichetta(String etichetta) {
		this.etichettaCategoria = etichetta;
	}

	public byte getPriorita() {
		return prioritaCategoria;
	}

	public void setPriorita(Priorita priorita) {
		this.prioritaCategoria = priorita.getValorePriorita();
	}

	public String getColore() {
		return coloreCategoria;
	}

	public void setColore(Color colore) {
		this.coloreCategoria = convertiColore(colore);
	}
	
	
	/**
	 * Verifica la presenza della categoria associata all'oggetto
	 * di tipo CategoriaORM nella tabella.
	 * 
	 * Se presente ritorna l'id associato alla CategoriaORM cui l'oggetto fa
	 * parte, altrimenti ritorna null e l'id verrà automaticamente
	 * generato dal DAO in fase di inserimento.
	 * @throws IOException 
	 */
	public Integer verificaEsistenza(Dao<CategoriaORM, Integer> categoriaDAO) throws IOException {
		
		CloseableWrappedIterable<CategoriaORM> iterator = categoriaDAO.getWrappedIterable();
		try {
			Categoria categoria = this.restituisciCategoria();
			
			Categoria tmp;
			for (CategoriaORM categoriaORM : categoriaDAO) {
				tmp = categoriaORM.restituisciCategoria();
				
				if (tmp.equals(categoria))
					return categoriaORM.getIdCategoria();
			}
		} catch (CategoriaOrmNonValidaException e) {
			throw new IOException(e);
		} finally {
			iterator.close();
		}
		
		return null;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((coloreCategoria == null) ? 0 : coloreCategoria.hashCode());
		result = prime * result + ((etichettaCategoria == null) ? 0 : etichettaCategoria.hashCode());
		result = prime * result + ((idCategoria == null) ? 0 : idCategoria.hashCode());
		result = prime * result + ((prioritaCategoria == null) ? 0 : prioritaCategoria.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		
		if (obj instanceof CategoriaORM) {
			CategoriaORM other = (CategoriaORM) obj;
			
			if(! this.idCategoria.equals(other.getIdCategoria())){
				return false;
			}
			
			if (! this.etichettaCategoria.equals(other.getEtichetta()))
				return false;
			
			if (! this.coloreCategoria.equals(other.getColore()))
				return false;
			
			if (! this.prioritaCategoria.equals(other.getPriorita()))
				return false;
					
			return true;
		}
		
		return false;
	}

	@Override
	public String toString() {
		return this.etichettaCategoria;
	}

}
