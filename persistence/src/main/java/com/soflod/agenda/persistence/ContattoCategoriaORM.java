package com.soflod.agenda.persistence;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable (tableName = "ContattiCategorie")
public class ContattoCategoriaORM {
	
	public static final String ID_CONTATTO = "IdContatto";
	public static final String ID_CATEGORIA = "IdCategoria";
	
	
	@DatabaseField (generatedId = true)
	private Integer idContattoCategoria;
	
	@DatabaseField (columnName = ID_CONTATTO, foreign = true)
	private ContattoORM contatto;
	
	@DatabaseField (columnName = ID_CATEGORIA, foreign= true)
    private CategoriaORM categoria;
	
	
	public ContattoCategoriaORM() {
		super();
	}
	
	public ContattoCategoriaORM(ContattoORM contatto, CategoriaORM categoria) {
		super();
		this.idContattoCategoria = null;
		this.contatto = contatto;
		this.categoria = categoria;
	}

	public ContattoORM getContatto() {
		return contatto;
	}

	public CategoriaORM getCategoria() {
		return categoria;
	}


}
