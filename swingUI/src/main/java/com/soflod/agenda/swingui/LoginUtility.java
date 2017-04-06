package com.soflod.agenda.swingui;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.j256.ormlite.logger.Logger;
import com.j256.ormlite.logger.LoggerFactory;
import com.soflod.agenda.swingui.exception.PasswordException;
import com.soflod.agenda.swingui.exception.UtenteNonValidoException;

public class LoginUtility {

	private String percorsoHome;
	private static final String NOME_FILE_XML = "utenti.xml";
	private final String percorsoFileXML;
	
	private static final String TAG_UTENTE = "utente";
	private static final String TAG_NOME_UTENTE = "nomeUtente";
	
	private static final String TAG_CHIAVE = "password";
	private static final String TAG_PERCORSO = "percorso";
	
	private Document xml;

	private Logger logger = LoggerFactory.getLogger(LoginUtility.class);
	
	public LoginUtility(String percorsoHome) {

		/*
		 * controllo che l'xml esista
		 */
		this.percorsoHome = percorsoHome;
		
		
		//Path info utenti
		String percorsoXML = percorsoHome + File.separator + NOME_FILE_XML;
		this.percorsoFileXML = percorsoXML;		
		
		File xmlFile = new File(percorsoFileXML);
		if (!xmlFile.exists()) {
			try {
				boolean created = xmlFile.createNewFile();
				
				if (created)
					logger.info("File creato in:" + this.percorsoFileXML);
				else
					logger.info("Non è stato possibile creare il file");
				
			} catch (IOException e) {
				logger.error(e, e.getMessage());
			}
			//Definizione XML file
			this.xml = definisciXmlUtenti();
			scriviDocumentoXML(this.xml);
		}
		
		this.xml = leggiDocumentoXML(xmlFile);

		
	}
	
	public Document leggiDocumentoXML(File xmlFile) {
		try {
			
			DocumentBuilderFactory utentiFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = utentiFactory.newDocumentBuilder();
			
			return dBuilder.parse(xmlFile);
		} catch(ParserConfigurationException pe) {
			logger.error(pe, pe.getMessage());
		} catch (SAXException e) {
			logger.error(e, e.getMessage());
		} catch (IOException e) {
			logger.error(e, e.getMessage());
		}
		return null;
	}
	
	public void scriviDocumentoXML(Document xmlDocument) {
		try {
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();

			DOMSource sorgente= new DOMSource(xmlDocument);

			StreamResult risultato = new StreamResult(new File(this.percorsoFileXML));
			transformer.transform(sorgente, risultato);
			
		} catch (TransformerException te) {
			logger.error(te, te.getMessage());
		}
	}

	public Document definisciXmlUtenti() {

		try {
			DocumentBuilderFactory utentiFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder utentiBuilder = utentiFactory.newDocumentBuilder();

			Document tmpXml = utentiBuilder.newDocument();

			Element utenti = tmpXml.createElement("utenti");
			tmpXml.appendChild(utenti);
			
			
			return tmpXml;

		} catch (ParserConfigurationException e) {
			logger.error(e, e.getMessage());
		}
		
		return null;
	}

	/**
	 * Controlla che l'utente che si vuole registrare non utilizzi
	 * un nome utente gia esistente.
	 * 
	 * Ritorna true se il nome utente non è già in uso.
	 * 
	 * @param nomeNuovoUtente
	 * @return
	 */
	public boolean verificaNomeUtenteUsato(String nomeNuovoUtente) {
		return cercaNomeUtente(nomeNuovoUtente) != null;
	}
	
	public Element cercaNomeUtente(String nomeUtente) {
		
		NodeList nodiUtenti = this.xml.getElementsByTagName(TAG_UTENTE);
		
		if (nodiUtenti == null)
			return null;

		int length = nodiUtenti.getLength();

		for (int i = 0; i < length; i++) {

			if (nodiUtenti.item(i).getNodeType() == Node.ELEMENT_NODE) {
				Element elemento = (Element) nodiUtenti.item(i);

				if (elemento.getNodeName().contains(TAG_UTENTE)) {
					String tmpUtente = elemento.getElementsByTagName(TAG_NOME_UTENTE).item(0).getTextContent();

					if (tmpUtente.equals(nomeUtente))
						return elemento;
				}
			}
		}
		
		return null;
	}

	public void registrazioneNuovoUtente(String nomeNuovoUtente, char[] passwordUtente)
			throws
			UtenteNonValidoException {

		Element rootUtenti = xml.getDocumentElement();

		if (cercaNomeUtente(nomeNuovoUtente) != null)
			throw new UtenteNonValidoException("Il nome utente inserito e' gia' usato.");
		
		Element utenteTag = xml.createElement(TAG_UTENTE);
		rootUtenti.appendChild(utenteTag);

		Element nomeUtenteTag = xml.createElement(TAG_NOME_UTENTE);
		nomeUtenteTag.setTextContent(nomeNuovoUtente);
		utenteTag.appendChild(nomeUtenteTag);

		Element passwordTag = xml.createElement(TAG_CHIAVE);
		
		String pwdUtente = new String(passwordUtente);
		
		passwordTag.setTextContent(pwdUtente);
		utenteTag.appendChild(passwordTag);

		Element percorsoTag = xml.createElement(TAG_PERCORSO);

		String percorso = this.percorsoHome + File.separator + nomeNuovoUtente.toLowerCase() + ".db";
		percorsoTag.setTextContent(percorso);

		utenteTag.appendChild(percorsoTag);
		
		scriviDocumentoXML(xml);
		
	}
	
	public boolean verificaCoincidenzaPassword(char[] password1, char[] password2){

		if (password1.length != password2.length)
			return false;
		
		for (int i = 0; i < password1.length; i++) {
			if (password1[i] != password2[i])
				return false;
		}

		return true;
	}
	
	public String verificaCorrispondenzaLogin(String nomeUtenteLogin, char[] passwordUtenteLogin)
			throws
			UtenteNonValidoException {
		
		Element nodoUtente = cercaNomeUtente(nomeUtenteLogin);
		
		if (nodoUtente == null)
			throw new UtenteNonValidoException("Il nome utente inserito non e' presente.");
		
		String nodoNomeUtente = nodoUtente.getElementsByTagName(TAG_NOME_UTENTE).item(0).getTextContent();
		String nodoPassword = nodoUtente.getElementsByTagName(TAG_CHIAVE).item(0).getTextContent();
		
		char[] pwdChar = nodoPassword.toCharArray();
		
		String nodoPercorso = nodoUtente.getElementsByTagName(TAG_PERCORSO).item(0).getTextContent(); 
			
		// non dovrebbe servire, se il nome non è uguale l'eccezione sopra viene lanciata
		if (! nodoNomeUtente.equals(nomeUtenteLogin))
			throw new UtenteNonValidoException("Il nome utente inserito non e' presente.");
		
		if (! verificaCoincidenzaPassword(passwordUtenteLogin, pwdChar))
			throw new UtenteNonValidoException(new PasswordException());
		
		
		return nodoPercorso;
	}
	
}
