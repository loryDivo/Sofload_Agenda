package com.soflod.agenda.core;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.soflod.agenda.core.exception.EmailNonValidaException;

public class Email {

	/*
	 * Grazie a: http://stackoverflow.com/questions/8204680/java-regex-email
	 * 
	 * Non importa la regex quanto sia complicata, non soddisfera' mai
	 * RFC822, se si riesce a trovare la regex usata da google, si provvedera'
	 * a sostituirla con quella, fino ad allora si usera' questa.
	 */
	private static final Pattern EMAIL_PATTERN =
			Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$",
					Pattern.CASE_INSENSITIVE);
			
	private String indirizzoEmail;

	public Email(String indirizzoEmail) throws EmailNonValidaException {
		super();
		if (verificaIndirizzoEmail(indirizzoEmail))
			this.indirizzoEmail = indirizzoEmail;
		else
			throw new EmailNonValidaException();
	}
	
	public String getIndirizzoEmail() {
		return indirizzoEmail;
	}

	public void setIndirizzoEmail(String indirizzoEmail) throws EmailNonValidaException {
		if (verificaIndirizzoEmail(indirizzoEmail))
			this.indirizzoEmail = indirizzoEmail;
		else
			throw new EmailNonValidaException();
	}
	
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		
		if (obj instanceof Email)
			return this.indirizzoEmail.equals(((Email) obj).getIndirizzoEmail());
		
		return false;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((indirizzoEmail == null) ? 0 : indirizzoEmail.hashCode());
		return result;
	}


	@Override
	public String toString() {
		return "\"indirizzo-email\" : \"" + this.indirizzoEmail + "\"";
	}

	private boolean verificaIndirizzoEmail(String indirizzoEmail) {
        Matcher matcher = EMAIL_PATTERN.matcher(indirizzoEmail);
        return matcher.matches();
	}	
}
