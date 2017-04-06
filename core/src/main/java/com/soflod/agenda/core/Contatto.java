package com.soflod.agenda.core;

import java.util.List;

import com.soflod.agenda.core.exception.CategorieNonValideException;
import com.soflod.agenda.core.exception.EmailsNonValideException;
import com.soflod.agenda.core.exception.TelefoniNonValidiException;

public class Contatto {
	
	/*
	 * 
	 * Contatto sta diventando una fat class
	 * 
	 * forse sarebbe meglio andare a specificare delle classi
	 * di gestione delle mail, telefoni e categorie.
	 * 
	 * Questo lasciamolo nel refactoring. Non affrontiamolo ora.
	 */
	
	/*
	 * Non mettiamo vincoli ai nomi di cui possiamo pentirci.
	 */
	private String nome;
	private String cognome;
	private GestoreCategorie gestoreCategorie;
	private GestoreTelefoni gestoreTelefoni;
	
	/*
	 *  per non generare ambiguita' ed incomprensioni usiamo
	 *  il nome inglese, emails identifica più email.
	 *  
	 *  email è riferita ad una singola istanza di tipo Email.
	 *  
	 */
	private GestoreEmails gestoreEmails;
	
	public static class Builder {
		
		/*
		 * Potrei voler salvare un contatto senza inserire
		 * ne' numeri di telefono ne' email.
		 * 
		 * Quindi l'unico attributo obbligatorio per la classe
		 * e' il nome.
		 */
		
		private String nome;
		private String cognome;
		private GestoreCategorie gestoreCategorie;
		private GestoreTelefoni gestoreTelefoni;
		private GestoreEmails gestoreEmails;
		
		/**
		 * Viene impostato il nome del contatto. Questo
		 * è l'unico parametro obbligatorio per poter creare
		 * il contatto.
		 * 
		 * Vengono instanziate le ArrayList dei telefoni,
		 * delle categorie e delle email.
		 * Viene inserita la categoria di default.
		 * 
		 * Come cognome viene impostata la stringa vuota.
		 * 
		 * @param nome
		 */
		public Builder (String nome) {
			this.nome = nome;
			this.cognome = "";
			this.gestoreTelefoni = new GestoreTelefoni();
			this.gestoreCategorie = new GestoreCategorie();
			this.gestoreEmails = new GestoreEmails();
			
			this.gestoreCategorie.aggiungiCategoria(new Categoria());
		}
		
		/**
		 * Aggiunge una categoria all'ArrayList contenente
		 * le categorie del contatto.
		 * 
		 * Se la categoria è già presente nella lista,
		 * la categoria non viene aggiunta, non viene comunque
		 * generata alcuna eccezione od errore.
		 */
		public Builder categoria(Categoria categoria) {
			if (! this.gestoreCategorie.getCategorie().contains(categoria))
				this.gestoreCategorie.aggiungiCategoria(categoria);
			
			return this;
		}
		
		/**
		 * Si vanno ad impostare le categorie del contatto
		 * sovrascrivendo eventuali categorie già associate al
		 * contatto.
		 * 
		 * @param categorie le nuove categorie associate al contatto
		 * @throws CategorieNonValideException 
		 */
		public Builder categorie(List<Categoria> categorie) throws CategorieNonValideException {
			this.gestoreCategorie = new GestoreCategorie(categorie);
			return this;
		}
		
		public Builder cognome(String cognome) {
			this.cognome = cognome;
			return this;
		}
		
		public Builder emails(List<Email> emails) throws EmailsNonValideException {
			this.gestoreEmails = new GestoreEmails(emails);
			return this;
		}
		
		public Builder email(Email email) {
			if (! this.gestoreEmails.getEmails().contains(email))
				this.gestoreEmails.aggiungiEmail(email);
			
			return this;
		}
		
		public Contatto build() {
			return new Contatto(this);
		}
		
		public Builder telefono(Telefono telefono) {
			if (! this.gestoreTelefoni.getTelefoni().contains(telefono))
				this.gestoreTelefoni.aggiungiTelefono(telefono);

			return this;
		}
		
		public Builder telefoni(List<Telefono> telefoni) throws TelefoniNonValidiException {
			this.gestoreTelefoni = new GestoreTelefoni(telefoni);
			return this;
		}
		
	}
	
	private Contatto(Builder builder) {
		this.nome = builder.nome;
		this.cognome = builder.cognome;
		this.gestoreTelefoni = builder.gestoreTelefoni;
		this.gestoreEmails = builder.gestoreEmails;
		this.gestoreCategorie = builder.gestoreCategorie;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public List<Categoria> getCategorie() {
		return gestoreCategorie.getCategorie();
	}

	public void setCategorie(List<Categoria> categorie) throws CategorieNonValideException {
		this.gestoreCategorie = new GestoreCategorie(categorie);
	}
	
	public GestoreCategorie getGestoreCategorie() {
		return gestoreCategorie;
	}
	
	public List<Email> getEmails() {
		return gestoreEmails.getEmails();
	}

	/**
	 * 
	 * Sostituisce tutte le Email del contatto con
	 * quelle in emails.
	 * 
	 * @param emails le nuove email del contatto.
	 * @throws EmailsNonValideException 
	 */
	public void setEmails(List<Email> emails) throws EmailsNonValideException {
		this.gestoreEmails = new GestoreEmails(emails);
	}

	
	public GestoreEmails getGestoreEmails() {
		return this.gestoreEmails;
	}
	
	public List<Telefono> getTelefoni() {
		return gestoreTelefoni.getTelefoni();
	}

	public void setTelefoni(List<Telefono> telefoni) throws TelefoniNonValidiException {
		this.gestoreTelefoni = new GestoreTelefoni(telefoni);
	}
	
	public GestoreTelefoni getGestoreTelefoni() {
		return this.gestoreTelefoni;
	}
	

	
	/*
	 * Stampa stringa contatto in formato JSON(non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	
	@Override
	public String toString() {
		
		StringBuilder str = new StringBuilder();
		str.append("{\"contatto\" : {");
		str.append("\"nome\" : \"" + this.nome + "\", ");
		str.append("\"cognome\" : \"" + this.cognome + "\", ");
		
		// Telefoni
		str.append("\"telefoni\" : [");
		str.append(getGestoreTelefoni().toString());
		str.append("], ");
		
		// Emails
		str.append("\"emails\" : [");
		str.append(getGestoreEmails().toString());
		str.append("], ");
		
		// Categorie
		str.append("\"categorie\" : [");
		str.append(getGestoreCategorie().toString());
		str.append("]");		
		
		str.append("}}");		
		
		return str.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cognome == null) ? 0 : cognome.hashCode());
		result = prime * result + ((gestoreCategorie == null) ? 0 : gestoreCategorie.hashCode());
		result = prime * result + ((gestoreEmails == null) ? 0 : gestoreEmails.hashCode());
		result = prime * result + ((gestoreTelefoni == null) ? 0 : gestoreTelefoni.hashCode());
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		
		
		if (obj instanceof Contatto) {

			Contatto other = (Contatto) obj;
			
			if (! this.nome.equals(other.getNome()))
				return false;
			
			if (! this.cognome.equals(other.getCognome()))
				return false;
			
			if (! this.gestoreCategorie.equals(other.getGestoreCategorie()))
				return false;
			
			if (! this.gestoreEmails.equals(other.getGestoreEmails()))
				return false;
			
			if (! this.gestoreTelefoni.equals(other.getGestoreTelefoni()))
				return false;
			
			return true;
		}

		return false;
	}
	
}
