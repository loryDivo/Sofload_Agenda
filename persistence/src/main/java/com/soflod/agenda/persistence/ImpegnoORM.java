package com.soflod.agenda.persistence;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.*;
import com.j256.ormlite.logger.Logger;
import com.j256.ormlite.logger.LoggerFactory;
import com.j256.ormlite.table.*;
import com.soflod.agenda.core.*;
import com.soflod.agenda.core.exception.DateNonValideException;
import com.soflod.agenda.core.exception.LimiteNoteSuperatoException;
import com.soflod.agenda.core.exception.LimiteTitoloSuperatoException;
import com.soflod.agenda.persistence.exception.CategoriaOrmNonValidaException;
import com.soflod.agenda.persistence.exception.ImpegnoOrmNonValidoException;

@DatabaseTable (tableName = "Impegni")
public class ImpegnoORM {
	
	public static  final String FORMATO_DATA_IMPEGNO = "yyyy:MM:dd HH:mm";
	
	public static final String TITOLO = "titolo";
	public static final String CATEGORIA = "categoria";
	public static final String DATA_INIZIO = "data_inizio";
	public static final String DATA_FINE = "data_fine";
	public static final String ALLARME = "allarme";
	public static final String NOTE = "note";
	public static final String RIPETIZIONE = "ripetizione";
	
	private Logger logger  = LoggerFactory.getLogger(ImpegnoORM.class);
	
	@DatabaseField (generatedId = true)
	private Integer idImpegno;
	
	@DatabaseField(columnName = TITOLO, canBeNull = false)
	private String titoloImpegno;
	
	@DatabaseField(columnName = CATEGORIA, foreign = true, canBeNull = false)
	private CategoriaORM categoriaORM;
	
	@DatabaseField(dataType = DataType.DATE_STRING, format = "yyyy:MM:dd HH:mm", columnName = DATA_INIZIO, canBeNull = false)
	private Date dataInizio;
	
	@DatabaseField(dataType = DataType.DATE_STRING, format = "yyyy:MM:dd HH:mm", columnName = DATA_FINE, canBeNull = false)
	private Date dataFine;
	
	@DatabaseField(columnName = ALLARME, canBeNull = false)
	private Boolean allarmeImpegno;
	
	@DatabaseField(columnName = NOTE, canBeNull = false)
	private String noteImpegno;
	
	@DatabaseField(columnName = RIPETIZIONE, canBeNull = false)
	private Ripetizione ripetizioneImpegno;
	
	public ImpegnoORM() {
		super();
		this.idImpegno = null;
		this.allarmeImpegno = null;
		this.categoriaORM = null;
		this.titoloImpegno = null;
		this.dataFine = null;
		this.dataInizio = null;
		this.noteImpegno = null;
		this.ripetizioneImpegno = null;
	}
	
	public ImpegnoORM(Impegno impegno, Dao<CategoriaORM, Integer> categoriaDAO) throws IOException, SQLException{
		super();
		this.idImpegno = null;
		this.titoloImpegno = impegno.getTitolo();
		this.dataInizio = impegno.getDataInizio();
		this.dataFine = impegno.getDataFine();
		
		CategoriaORM tmp = new CategoriaORM(impegno.getCategoria(), categoriaDAO);
		if(tmp.getIdCategoria() == null){
			categoriaDAO.create(tmp);
		}
		
		this.categoriaORM = tmp;
		
		this.allarmeImpegno = impegno.getAllarme();
		this.noteImpegno = impegno.getNote();
		this.ripetizioneImpegno = impegno.getRipetizione();
	}
	
	public Integer getIdImpegno() {
		return idImpegno;
	}

	public void setIdImpegno(Integer idImpegno) {
		this.idImpegno = idImpegno;
	}
	
	public String getTitolo() {
		return titoloImpegno;
	}

	public void setTitolo(String titolo) {
		this.titoloImpegno = titolo;
	}

	public CategoriaORM getCategoriaORM() {
		return categoriaORM;
	}

	public void setCategoriaORM(CategoriaORM categoriaOrm) {
		this.categoriaORM = categoriaOrm;
	}

	public Date getDataInizio() {
		return dataInizio;
	}

	public void setDataInizio(Date dataInizio) {
		this.dataInizio = dataInizio;
	}

	public Date getDataFine() {
		return dataFine;
	}

	public void setDataFine(Date dataFine) {
		this.dataFine = dataFine;
	}

	public boolean getAllarme() {
		return allarmeImpegno;
	}

	public void setAllarme(boolean allarme) {
		this.allarmeImpegno = allarme;
	}

	public String getNote() {
		return noteImpegno;
	}

	public void setNote(String note) {
		this.noteImpegno = note;
	}

	public Ripetizione getRipetizione() {
		return ripetizioneImpegno;
	}

	public void setRipetizione(Ripetizione ripetizione) {
		this.ripetizioneImpegno = ripetizione;
	}
	
	public Impegno restituisciImpegno() throws ImpegnoOrmNonValidoException{
		
		Impegno tmp = null;
		
		try {
			Categoria categoria = this.categoriaORM.restituisciCategoria();
			tmp = new Impegno.Builder(this.titoloImpegno, categoria)
					.date(this.dataInizio, this.dataFine)
					.allarme(this.allarmeImpegno)
					.ripetizione(this.ripetizioneImpegno)
					.note(this.noteImpegno)
					.build();
			
		} catch (LimiteNoteSuperatoException lne) {
			throw new ImpegnoOrmNonValidoException(lne);
		} catch (LimiteTitoloSuperatoException lte) {
			throw new ImpegnoOrmNonValidoException(lte);
		} catch (DateNonValideException e) {
			throw new ImpegnoOrmNonValidoException(e);
		} catch (CategoriaOrmNonValidaException e) {
			throw new ImpegnoOrmNonValidoException(e);
		}
		
		return tmp;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((allarmeImpegno == null) ? 0 : allarmeImpegno.hashCode());
		result = prime * result + ((categoriaORM == null) ? 0 : categoriaORM.hashCode());
		result = prime * result + ((dataFine == null) ? 0 : dataFine.hashCode());
		result = prime * result + ((dataInizio == null) ? 0 : dataInizio.hashCode());
		result = prime * result + ((idImpegno == null) ? 0 : idImpegno.hashCode());
		result = prime * result + ((logger == null) ? 0 : logger.hashCode());
		result = prime * result + ((noteImpegno == null) ? 0 : noteImpegno.hashCode());
		result = prime * result + ((ripetizioneImpegno == null) ? 0 : ripetizioneImpegno.hashCode());
		result = prime * result + ((titoloImpegno == null) ? 0 : titoloImpegno.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj){
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (obj instanceof ImpegnoORM) {
			ImpegnoORM other = (ImpegnoORM) obj;

			if(! this.idImpegno.equals(other.getIdImpegno())){
				return false;
			}
			
			if (! this.titoloImpegno.equals(other.getTitolo())) {
				return false;
			}
			if (! this.noteImpegno.equals(other.getNote())) {
				return false;
			}
			if (! this.ripetizioneImpegno.equals(other.getRipetizione())) {
				return false;
			}
			if (! this.dataInizio.equals(other.getDataInizio())) {
				return false;
			}
			if (! this.dataFine.equals(other.getDataFine())) {
				return false;
			}
			return true;
		}
		return false;
	}

	@Override
	public String toString() {
		
		StringBuilder str = new StringBuilder();
		SimpleDateFormat sdfGiorno = new SimpleDateFormat(Impegno.FORMATO_DATA_IMPEGNO);
		str.append(titoloImpegno + ": " + sdfGiorno.format(dataInizio) + " - " + sdfGiorno.format(dataFine) + " ");
		
		if (allarmeImpegno)
			str.append("ALLARME ATTIVO");
		else
			str.append("ALLARME DISATTIVO");
		
		return str.toString();
		
	}

	
	
}
