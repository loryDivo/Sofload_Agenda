package com.soflod.agenda.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.soflod.agenda.core.exception.EmailsNonValideException;

public class GestoreEmails {
	
	private List<Email> emails;
	
	public GestoreEmails() {
		super();
		this.emails = new ArrayList<Email>();
	}
	
	public GestoreEmails(List<Email> emails) throws EmailsNonValideException { 
		super();
		if (! verificaNoDoppioniEmails(emails)) 
			throw new EmailsNonValideException("Le emails da aggiungere al contatto" +
				"non devono contenere doppioni.");
		
		this.emails = new ArrayList<Email>(emails);
	}
	
	public List<Email> getEmails() {
		return emails;
	}

	public boolean aggiungiEmail(Email email) {
		if (this.emails.contains(email))
			return true;
		return this.emails.add(email);
	}
	
	public Email rimuoviEmail(Email email) {
		if (this.emails.contains(email)) {
			this.emails.remove(email);
			
			return email;
		}
		
		return null;
	}
	
	private static boolean verificaNoDoppioniEmails(final List<Email> emails) {

		List<Email> tmpEmails = new ArrayList<Email>(emails);
		List<Email> noDoppi = new ArrayList<Email>();
		Iterator<Email> emailIterator = tmpEmails.iterator();
		Email tmp;
		while (emailIterator.hasNext()) {

			tmp = emailIterator.next();
			emailIterator.remove();

			// se contiene un doppione
			if (tmpEmails.contains(tmp))
				return false;

			noDoppi.add(tmp);
		}
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((emails == null) ? 0 : emails.hashCode());
		return result;
	}

	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;

		if (obj == null)
			return false;

		if (obj instanceof GestoreEmails) {

			GestoreEmails other = (GestoreEmails) obj;
		
		
		// equals sulle Email, da ridistribuire nella extract class
		
		if (this.emails.size() != other.getEmails().size())
			return false;

		for (Email email: other.getEmails()) {
			if (! this.emails.contains(email))
				return false;
		}

			return true;
		}
		
		return false;
	}
	
	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		
		Iterator<Email> emailIt = this.getEmails().iterator();
		Email temp;
		
		while (emailIt.hasNext()) {
			temp = emailIt.next();
			
			if (emailIt.hasNext())
				str.append(temp.toString() + ", ");
			else
				str.append(temp.toString());
		}
		return str.toString();
	}


}
