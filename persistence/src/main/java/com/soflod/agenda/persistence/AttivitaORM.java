package com.soflod.agenda.persistence;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.soflod.agenda.core.Attivita;
import com.soflod.agenda.core.Categoria;
import com.soflod.agenda.core.exception.DataInizioNonValidaException;
import com.soflod.agenda.core.exception.DataScadenzaNonValidaException;
import com.soflod.agenda.core.exception.LimiteTitoloSuperatoException;
import com.soflod.agenda.persistence.exception.AttivitaNonValidaException;
import com.soflod.agenda.persistence.exception.CategoriaOrmNonValidaException;

@DatabaseTable (tableName = "Attivita")
public class AttivitaORM {
	
	
	public static final String FORMATO_DATA_ATTVITA = "yyyy:MM:dd";
	
	public static final String TITOLO = "titoloAttivita";
	public static final String CATEGORIA = "categoria";
	public static final String DATA_INIZIO = "dataInizio";
	public static final String DATA_SCADENZA = "dataScadenza";
	
	@DatabaseField (generatedId = true)
	private Integer idAttivita;
	
	@DatabaseField (columnName = TITOLO, canBeNull = false)
	private String titoloAttivita;
	
	@DatabaseField (columnName = CATEGORIA, foreign = true, canBeNull = false)
	private CategoriaORM categoriaORM;
	
	@DatabaseField (columnName = DATA_INIZIO, dataType = DataType.DATE_STRING, format = "yyyy:MM:dd", canBeNull = false)
	private Date dataInizio;
	
	@DatabaseField (columnName = DATA_SCADENZA, dataType = DataType.DATE_STRING, format = "yyyy:MM:dd", canBeNull = false)
	private Date dataScadenza;
	
	public AttivitaORM() {
		super();
		this.idAttivita = null;
		this.titoloAttivita = null;
		this.dataInizio = null;
		this.dataScadenza = null;
		this.categoriaORM = null;
	}
	
	public AttivitaORM(Attivita attivita, Dao<CategoriaORM, Integer> categoriaDAO) throws IOException, SQLException {
		
		this.idAttivita = null;
		this.titoloAttivita = attivita.getTitolo();
		this.dataInizio = attivita.getDataInizio();
		this.dataScadenza = attivita.getDataScadenza();
		
		CategoriaORM tmp = new CategoriaORM(attivita.getCategoria(), categoriaDAO);
		
		if (tmp.getIdCategoria() == null)
			categoriaDAO.create(tmp);
		
		this.categoriaORM = tmp;
	}

	public Integer getIdAttivita() {
		return idAttivita;
	}
	
	public void setIdAttivita(Integer idAttivita) {
		this.idAttivita = idAttivita;
	}
	
	public String getTitolo() {
		return titoloAttivita;
	}
	
	public void setTitolo(String titoloAttivita) {
		this.titoloAttivita = titoloAttivita;
	}

	public Date getDataInizio() {
		return dataInizio;
	}
	
	public void setDataInizio(Date dataInizio) {
		this.dataInizio = dataInizio;
	}

	public Date getDataScadenza() {
		return dataScadenza;
	}
	
	public void setDataScadenza(Date dataScadenza) {
		this.dataScadenza = dataScadenza;
	}
	
	public void setCategoriaORM(CategoriaORM categoriaORM) {
		this.categoriaORM = categoriaORM;
	}
	
	public CategoriaORM getCategoriaORM() {
		return this.categoriaORM;
	}

	public Attivita restituisciAttivita() throws AttivitaNonValidaException{
		
		Attivita tmp = null;
		try {
		Categoria categoria = this.categoriaORM.restituisciCategoria();
		
			tmp =  new Attivita.Builder(this.titoloAttivita, this.dataScadenza)
					.categoria(categoria)
					.dataInizio(this.dataInizio)
					.build();
		} catch (DataInizioNonValidaException dnv) {
			throw new AttivitaNonValidaException(dnv);
		} catch (LimiteTitoloSuperatoException tnv) {
			throw new AttivitaNonValidaException(tnv);
		} catch (DataScadenzaNonValidaException dnv) {
			throw new AttivitaNonValidaException(dnv);
		} catch (ParseException pe) {
			throw new AttivitaNonValidaException(pe);
		} catch (CategoriaOrmNonValidaException e) {
			throw new AttivitaNonValidaException(e);
		}
		
		return tmp;
	}

	
	@Override
	public String toString() {

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

		return titoloAttivita + ": " + sdf.format(dataInizio) + " - " + sdf.format(dataScadenza);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((categoriaORM == null) ? 0 : categoriaORM.hashCode());
		result = prime * result + ((dataInizio == null) ? 0 : dataInizio.hashCode());
		result = prime * result + ((dataScadenza == null) ? 0 : dataScadenza.hashCode());
		result = prime * result + ((idAttivita == null) ? 0 : idAttivita.hashCode());
		result = prime * result + ((titoloAttivita == null) ? 0 : titoloAttivita.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;

		if (obj instanceof AttivitaORM) {

			AttivitaORM other = (AttivitaORM) obj;

			if(! this.idAttivita.equals(other.getIdAttivita())){
				return false;
			}
			
			if (! this.titoloAttivita.equals(other.getTitolo()))
				return false;

			if (! this.dataInizio.equals(other.getDataInizio()))
				return false;

			if (! this.dataScadenza.equals(other.getDataScadenza()))
				return false;

			return true;
		}

		return false;

	}


	
	

}
