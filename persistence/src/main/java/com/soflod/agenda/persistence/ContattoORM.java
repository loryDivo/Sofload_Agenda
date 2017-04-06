package com.soflod.agenda.persistence;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.logger.Logger;
import com.j256.ormlite.logger.LoggerFactory;
import com.j256.ormlite.table.DatabaseTable;
import com.soflod.agenda.core.Categoria;
import com.soflod.agenda.core.Contatto;
import com.soflod.agenda.core.Email;
import com.soflod.agenda.core.Telefono;
import com.soflod.agenda.core.exception.CategorieNonValideException;
import com.soflod.agenda.core.exception.EmailNonValidaException;
import com.soflod.agenda.core.exception.EmailsNonValideException;
import com.soflod.agenda.core.exception.NumeroTelefonoNonValidoException;
import com.soflod.agenda.core.exception.TelefoniNonValidiException;
import com.soflod.agenda.persistence.exception.CategoriaOrmNonValidaException;
import com.soflod.agenda.persistence.exception.ContattoOrmNonValidoException;

@DatabaseTable(tableName = "Contatti")
public class ContattoORM {

	private Logger logger  = LoggerFactory.getLogger(ContattoORM.class);
	
	public static final String NOME = "nome";
	public static final String COGNOME = "cognome";
	public static final String TELEFONI = "telefoni";
	public static final String EMAILS = "emails";


	@DatabaseField(generatedId = true)
	private Integer idContatto;

	@DatabaseField(canBeNull = false, columnName = NOME)
	private String nomeContatto;

	@DatabaseField(canBeNull = false, columnName = COGNOME)
	private String cognomeContatto;

	@DatabaseField(canBeNull = false, columnName = TELEFONI)
	private String telefoniContatto;

	@DatabaseField(canBeNull = false, columnName = EMAILS)
	private String emailsContatto;

	List<CategoriaORM> categorieORM;

	public ContattoORM() {
		super();
		this.idContatto = null;
		this.nomeContatto = null;
		this.cognomeContatto = null;
		this.telefoniContatto = null;
		this.emailsContatto = null;
		this.categorieORM = null;
	}

	public ContattoORM(Contatto contatto, Dao<CategoriaORM, Integer> categoriaDAO) throws SQLException, IOException {
		super();
		this.nomeContatto = contatto.getNome();
		this.cognomeContatto = contatto.getCognome();

		this.telefoniContatto = convertiTelefoni(contatto.getTelefoni());
		this.emailsContatto = convertiEmails(contatto.getEmails());
		this.categorieORM = estraiCategorie(categoriaDAO, contatto.getCategorie());

	}
	
	public Integer getIdContatto() {
		return idContatto;
	}

	public void setIdContatto(Integer idContatto) {
		this.idContatto = idContatto;
	}

	public String getNome() {
		return nomeContatto;
	}

	public void setNome(String nome) {
		this.nomeContatto = nome;
	}

	public String getCognome() {
		return cognomeContatto;
	}

	public void setCognome(String cognome) {
		this.cognomeContatto = cognome;
	}

	public String getTelefoni() {
		return telefoniContatto;
	}

	public void setTelefoni(List<Telefono> telefoni) {
		this.telefoniContatto = convertiTelefoni(telefoni);
	}

	public String getEmails() {
		return emailsContatto;
	}

	public void setEmails(List<Email> emails) {
		this.emailsContatto = convertiEmails(emails);
	}

	public List<CategoriaORM> getCategorieORM() {
		return categorieORM;
	}

	public void setCategorieORM(List<CategoriaORM> categorieORM) {
		this.categorieORM = categorieORM;
	}

	public String convertiTelefoni(List<Telefono> telefoni) {
		StringBuilder str = new StringBuilder();

		for (Telefono telefono : telefoni) {
			str.append(telefono.getNumeroTelefono() + ";");
		}

		return str.toString();
	}

	public List<Telefono> convertiTelefoni(String telefoni) throws ContattoOrmNonValidoException{

		List<Telefono> convertiti = new ArrayList<Telefono>();

		if (telefoni.length() == 0)
			return convertiti;

		String[] telefoniSingoli = telefoni.split(";");

		try {
			for (int tel = 0; tel < telefoniSingoli.length; tel++) {
				convertiti.add(new Telefono(telefoniSingoli[tel]));
			}
		} catch (NumeroTelefonoNonValidoException ntnv) {
			logger.info(ntnv, ntnv.getMessage());
			throw new ContattoOrmNonValidoException(ntnv);
		}

		return convertiti;
	}

	public String convertiEmails(List<Email> emails) {
		StringBuilder str = new StringBuilder();

		for (Email email : emails) {
			str.append(email.getIndirizzoEmail() + ";");
		}

		return str.toString();
	}

	public List<Email> convertiEmails(String emails) throws ContattoOrmNonValidoException {

		List<Email> convertiti = new ArrayList<Email>();

		if (emails.length() == 0)
			return convertiti;

		String[] emailsSingole = emails.split(";");

		try {
			for (int tel = 0; tel < emailsSingole.length; tel++) {
				convertiti.add(new Email(emailsSingole[tel]));
			}
		} catch (EmailNonValidaException env) {
			logger.info(env, env.getMessage());
			throw new ContattoOrmNonValidoException(env);
		}

		return convertiti;
	}

	private List<CategoriaORM> estraiCategorie(Dao<CategoriaORM, Integer> categoriaDAO, List<Categoria> categorie)
			throws SQLException, IOException {

		List<CategoriaORM> categorieOrm = new ArrayList<CategoriaORM>();

		CategoriaORM tmp;

		for (Categoria categoria : categorie) {

			tmp = new CategoriaORM(categoria, categoriaDAO);

			if (tmp.getIdCategoria() == null)
				categoriaDAO.create(tmp);

			categorieOrm.add(tmp);
		}

		return categorieOrm;
	}

	public Contatto restituisciContatto(Dao<ContattoCategoriaORM, Integer> contattiCategorieDAO,
			Dao<CategoriaORM, Integer> categorieDAO) throws ContattoOrmNonValidoException {
		try {
			List<Categoria> categorie = new ArrayList<Categoria>();

			List<ContattoCategoriaORM> associazioni = contattiCategorieDAO.queryForEq(ContattoCategoriaORM.ID_CONTATTO,
					this.getIdContatto());

			for (ContattoCategoriaORM contattoCategoriaAssociato : associazioni) {
				CategoriaORM categoriaORM = categorieDAO
						.queryForId(contattoCategoriaAssociato.getCategoria().getIdCategoria());

				categorie.add(categoriaORM.restituisciCategoria());
			}
			Contatto tmp;
			tmp = new Contatto.Builder(this.nomeContatto).cognome(this.cognomeContatto).telefoni(convertiTelefoni(this.telefoniContatto))
					.emails(convertiEmails(this.emailsContatto)).categorie(categorie).build();
			return tmp;
		} catch (SQLException sqle) {
			throw new ContattoOrmNonValidoException(sqle);
		} catch (CategorieNonValideException cnv) {
			throw new ContattoOrmNonValidoException(cnv);
		} catch (EmailsNonValideException env) {
			throw new ContattoOrmNonValidoException(env);
		} catch (TelefoniNonValidiException tnv) {
			throw new ContattoOrmNonValidoException(tnv);
		} catch (CategoriaOrmNonValidaException e) {
			throw new ContattoOrmNonValidoException(e);
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((categorieORM == null) ? 0 : categorieORM.hashCode());
		result = prime * result + ((cognomeContatto == null) ? 0 : cognomeContatto.hashCode());
		result = prime * result + ((emailsContatto == null) ? 0 : emailsContatto.hashCode());
		result = prime * result + ((idContatto == null) ? 0 : idContatto.hashCode());
		result = prime * result + ((logger == null) ? 0 : logger.hashCode());
		result = prime * result + ((nomeContatto == null) ? 0 : nomeContatto.hashCode());
		result = prime * result + ((telefoniContatto == null) ? 0 : telefoniContatto.hashCode());
		return result;
	}
	
	/**
	 * Equals tra contattiORM
	 * Nel nostro utilizzo non servirà
	 * effettuare l'equals anche delle categorie
	 * (usato attualemente per rimozione di un contatto
	 * da arraylist provvisorio)
	 */
	@Override
	public boolean equals(Object obj){
		
		if(this==obj){
			return true;
		}
		if(obj == null){
			return false;
		}
		
		if(obj instanceof ContattoORM){
			ContattoORM other = (ContattoORM) obj;
			
			if(! this.idContatto.equals(other.idContatto)){
				return false;
			}
			if(! this.nomeContatto.equals(other.getNome())){
				return false;
			}
			if(! this.cognomeContatto.equals(other.getCognome())){
				return false;
			}
			if(! this.emailsContatto.equals(other.getEmails())){
				return false;
			}
			if(! this.telefoniContatto.equals(other.getTelefoni())){
				return false;
			}
			return true;
		}
		return false;
	}

	@Override
	public String toString() {
		return nomeContatto + " " + cognomeContatto;
	}

	
	

}
