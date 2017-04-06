package com.soflod.agenda.persistence;

import java.sql.SQLException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import com.j256.ormlite.logger.Logger;
import com.j256.ormlite.logger.LoggerFactory;
import com.soflod.agenda.core.Attivita;
import com.soflod.agenda.core.Categoria;
import com.soflod.agenda.core.Contatto;
import com.soflod.agenda.core.Impegno;
import com.soflod.agenda.persistence.exception.AggiuntaAttivitaException;
import com.soflod.agenda.persistence.exception.AggiuntaCategoriaException;
import com.soflod.agenda.persistence.exception.AggiuntaContattoException;
import com.soflod.agenda.persistence.exception.AggiuntaImpegnoException;
import com.soflod.agenda.persistence.exception.AttivitaNonValidaException;
import com.soflod.agenda.persistence.exception.ConnessioneException;
import com.soflod.agenda.persistence.exception.ImpegnoOrmNonValidoException;
import com.soflod.agenda.persistence.exception.InizializzazioneAgendaException;
import com.soflod.agenda.persistence.exception.ModificaAttivitaException;
import com.soflod.agenda.persistence.exception.ModificaCategoriaException;
import com.soflod.agenda.persistence.exception.ModificaContattoException;
import com.soflod.agenda.persistence.exception.ModificaImpegnoException;
import com.soflod.agenda.persistence.exception.RimozioneAttivitaException;
import com.soflod.agenda.persistence.exception.RimozioneCategoriaException;
import com.soflod.agenda.persistence.exception.RimozioneContattoException;
import com.soflod.agenda.persistence.exception.RimozioneImpegnoException;
public class Agenda {

	
	private Memory memory;
	private Connessione connessione;
	
	private Logger logger = LoggerFactory.getLogger(Agenda.class);
	private GestoreCategoriaORM gestoreCategorieOrm;	
	private GestoreImpegnoORM gestoreImpegniOrm;
	private GestoreAttivitaORM gestoreAttivitaOrm;
	private Rubrica rubrica;
	private Date dataCorrente;
	
	public static final String PRECISIONE_MESE = "yyyy:MM";
	public static final String PRECISIONE_GIORNO = "yyyy:MM:dd";
	public static final String PRECISIONE_MINUTO = "yyyy:MM:dd HH:mm";
	/**
	 * Viene inizializzata l'agenda 
	 * eccezioni reindirizzate a
	 * "InizializzazioneAgendaException"
	 * @param percorsoDelDB
	 * @throws InizializzazioneAgendaException
	 */
	
	public Agenda(String percorsoDelDB) throws InizializzazioneAgendaException {
		try{
			this.connessione = new Connessione(percorsoDelDB);
			this.memory = new Memory(this.connessione.getIstanzaConnessione());
			this.dataCorrente = new Date();
			gestoreCategorieOrm = new GestoreCategoriaORM(memory);
			gestoreImpegniOrm = new GestoreImpegnoORM(memory);
			gestoreAttivitaOrm = new GestoreAttivitaORM(memory);
			rubrica = new Rubrica(memory);
		}
		catch (SQLException sqle){
			logger.error(sqle.getMessage());
			throw new InizializzazioneAgendaException(sqle);
		}
		catch(ConnessioneException ce){
			logger.error(ce.getMessage());
			throw new InizializzazioneAgendaException(ce);
		}
	}
	
	public Memory getMemory() {
		return this.memory;
	}
	
	public Connessione getConnessione() {
		return this.connessione;
	}
	
	public GestoreCategoriaORM getGestoreCategorieOrm() {
		return gestoreCategorieOrm;
	}

	public GestoreImpegnoORM getGestoreImpegniOrm() {
		return gestoreImpegniOrm;
	}

	public GestoreAttivitaORM getGestoreAttivitaOrm() {
		return gestoreAttivitaOrm;
	}
	
	private boolean verificaDataImpegno(Date dataInizioImpegno) {
		
		SimpleDateFormat sdfMese = new SimpleDateFormat(PRECISIONE_MESE);
		
		/*
		 * converto le date in modo da averle ad una precisione
		 * al mese per entrambe
		 */
		String dataCorrenteAlMese = sdfMese.format(dataCorrente);
		String dataInizioAlMese = sdfMese.format(dataInizioImpegno);
		
		/*
		 * Riconverto le date per usare i metodi di confronto.
		 * si potrebbe andare ad selezionare da stringa direttamente
		 * il mese, ma penso che sia una pratica poco pulita
		 */
		try {
			Date meseCorrente = sdfMese.parse(dataCorrenteAlMese);
			Date meseImpegno = sdfMese.parse(dataInizioAlMese);
			
			// se le due date sono uguali
			if (meseImpegno.compareTo(meseCorrente) == 0)
				return true;
			
		} catch (ParseException e) {
			// non serve gestire l'eccezione.
		}
		
		return false;
	}
	
	private boolean verificaDataAttivita(Date dataInizio, Date dataScadenza) {
		
SimpleDateFormat sdfMese = new SimpleDateFormat(PRECISIONE_MESE);
		
		/*
		 * converto le date in modo da averle ad una precisione
		 * al mese per entrambe
		 */
		String dataCorrenteAlMese = sdfMese.format(dataCorrente);
		String dataInizioAlMese = sdfMese.format(dataInizio);
		String dataScadenzaAlMese = sdfMese.format(dataScadenza);
		
		/*
		 * Riconverto le date per usare i metodi di confronto.
		 * si potrebbe andare ad selezionare da stringa direttamente
		 * il mese, ma penso che sia una pratica poco pulita
		 */
		try {
			Date meseCorrente = sdfMese.parse(dataCorrenteAlMese);
			Date inizioMeseAttivita = sdfMese.parse(dataInizioAlMese);
			Date scadenzaMeseAttivita = sdfMese.parse(dataScadenzaAlMese);
			
			/*
			 * se il mese di inizio della attivita e' uguale o precede il mese corrente,
			 * e il mese di scadenza della attivita e' uguale o succede il mese corrente,
			 * allora la attivita e' attiva nel mese corrente
			 */
			if (inizioMeseAttivita.compareTo(meseCorrente) <= 0
					&& scadenzaMeseAttivita.compareTo(meseCorrente) >= 0)
				return true;
			
		} catch (ParseException e) {
			// non serve gestire l'eccezione.
		}
		
		return false;
	}

	/**
	 *
	 * Viene aggiunto un impegno all'agenda
	 * se è gia presente viene lanciata
	 * un eccezione altrimenti viene aggiunto 
	 * impegno
	 * @param impegno
	 * @throws AggiuntaImpegnoException 
	 */
	public void aggiungiImpegno(Impegno impegno) throws AggiuntaImpegnoException {
		ImpegnoORM impegnoAggiunto = gestoreImpegniOrm.aggiungiImpegno(impegno);
		if(impegnoAggiunto!=null){
			
			if(verificaDataImpegno(impegnoAggiunto.getDataInizio())){
				gestoreImpegniOrm.getImpegniORM().add(impegnoAggiunto);
			}
		}
		else{
			throw new AggiuntaImpegnoException();
		}
		
	}
	
	/**
	 * Viene aggiunta un attività all'agenda
	 * se è gia presente viene lanciata
	 * un eccezione 
	 * @param attivitaDaAggiungere
	 * @throws AggiuntaAttivitaException 
	 */
	public void aggiungiAttivita(Attivita attivitaDaAggiungere) throws AggiuntaAttivitaException {
		AttivitaORM attivitaAggiunta = gestoreAttivitaOrm.aggiungiAttivita(attivitaDaAggiungere);
		if(attivitaAggiunta!=null){
			
			if(verificaDataAttivita(attivitaAggiunta.getDataInizio(), attivitaAggiunta.getDataScadenza())){
				gestoreAttivitaOrm.getAttivitaOrm().add(attivitaAggiunta);
			}
		}
		else{
			throw new AggiuntaAttivitaException();
		}
	}
	
	/**
	 * Settaggio data per metodi di aggiunta
	 * impegno e attivita
	 * @param dataCorrente
	 */
	
	public void setDataCorrente(Date dataCorrente) {
		this.dataCorrente = dataCorrente;
	}
	
	public Date getDataCorrente() {
		return this.dataCorrente;
	}

	/**
	 * Viene aggiunta una categoria, in caso negativo 
	 * viene lanciata un eccezione
	 * @param categoria
	 * @throws AggiuntaCategoriaException 
	 */
	
	public void aggiungiCategoria(Categoria categoria) throws AggiuntaCategoriaException{
			gestoreCategorieOrm.aggiungiCategoria(categoria);
	}
	
	/**
	 * Viene aggiunto un contatto, in caso negativo
	 * viene lanciata un eccezione
	 * @param contatto
	 * @throws AggiuntaContattoException 
	 */
	public void aggiungiContatto(Contatto contatto) throws AggiuntaContattoException {
		rubrica.aggiungiContatto(contatto);
	}
	
	
	/**
	 * Viene rimosso un impegno dall'agenda
	 * se non esiste tale rimozione lancia eccezione
	 * alla rimozione dal db verrà controllato l'appartenenza
	 * all'arraylist e in caso positivo rimossa.
	 * @param impegnoOrm
	 * @throws RimozioneImpegnoException 
	 * @throws ImpegnoOrmNonValidoException 
	 * @throws SQLException 
	 */
	public void rimuoviImpegno(ImpegnoORM impegnoOrm) throws RimozioneImpegnoException {
		ImpegnoORM impegnoRimosso = gestoreImpegniOrm.rimuoviImpegnoORM(impegnoOrm);
		if(impegnoRimosso != null){
			if(verificaAppartenenza(impegnoRimosso)){
				gestoreImpegniOrm.getImpegniORM().remove(impegnoRimosso);
			}
		}
		else{
			throw new RimozioneImpegnoException();
		}
	}
	
	/**
	 * Viene rimossa un attivita dall'agenda
	 * se non esiste tale rimozione lancia eccezione
	 * alla rimozione dal db verrà controllato l'appartenenza
	 * all'arraylist e in caso positivo rimossa.
	* @param attivitaDaRimuovere
	 * @throws RimozioneAttivitaException 
	*/
	public void rimuoviAttivita(AttivitaORM attivitaDaRimuovere) throws RimozioneAttivitaException {
		AttivitaORM attivitaEliminata = gestoreAttivitaOrm.rimuoviAttivitaORM(attivitaDaRimuovere);
		if (attivitaEliminata != null) {
			if (verificaAppartenenza(attivitaEliminata)) {
				gestoreAttivitaOrm.getAttivitaOrm().remove(attivitaEliminata);
			}
		} else {
			throw new RimozioneAttivitaException();
		}
	}
	
	
	/**
	 * Viene rimossa una categoria dall'agenda
	 * se non esiste tale rimozione lancia eccezione
	 * alla rimozione dal db verrà controllato l'appartenenza
	 * all'arraylist e in caso positivo rimossa.
	 * @param categoriaOrm
	 * @throws RimozioneCategoriaException 
	 */
	public void rimuoviCategoria(CategoriaORM categoriaOrm) throws RimozioneCategoriaException{
		CategoriaORM categoriaEliminata = gestoreCategorieOrm.rimuoviCategoriaORM(categoriaOrm);
		if (categoriaEliminata != null) {
			if (verificaAppartenenza(categoriaEliminata)) {
				gestoreCategorieOrm.getCategorieOrm().remove(categoriaEliminata);
			}
		} else {
			throw new RimozioneCategoriaException();
		}
	}
	
	
	public Rubrica getRubrica() {
		return rubrica;
	}

	/**
	 * Viene rimossa un contatto dall'agenda
	 * se non esiste tale rimozione lancia eccezione
	 * alla rimozione dal db verrà controllato l'appartenenza
	 * all'arraylist e in caso positivo rimossa.
	 * @throws RimozioneContattoException 
	 */
	public void rimuoviContatto(ContattoORM contattoOrm) throws RimozioneContattoException {
		ContattoORM contattoRimosso = rubrica.rimuoviContattoORM(contattoOrm);
		if (contattoRimosso != null) {
			if (verificaAppartenenza(contattoRimosso)) {
				rubrica.getContattiORM().remove(contattoRimosso);
			}
		} else {
			throw new RimozioneContattoException();
		}
	}
	
	/**
	 * Viene aggiornato un impegno dell'agenda
	 * se non avviene tale modifica lancia eccezione
	 * alla rimozione dal db verrà controllato l'appartenenza
	 * all'arraylist e in caso positivo modificata.
	 * @param vecchioImpegnoOrm
	 * @param nuovoImpegnoOrm
	 * @throws ModificaImpegnoException
	 */
	
	public void aggiornaImpegno(ImpegnoORM vecchioImpegnoOrm, ImpegnoORM nuovoImpegnoOrm) throws ModificaImpegnoException{
		if (gestoreImpegniOrm.modificaImpegnoORM(vecchioImpegnoOrm, nuovoImpegnoOrm)) {
			if (verificaAppartenenza(vecchioImpegnoOrm)) {
				gestoreImpegniOrm.getImpegniORM().remove(vecchioImpegnoOrm);
				gestoreImpegniOrm.getImpegniORM().add(nuovoImpegnoOrm);
			}
		} else {
			throw new ModificaImpegnoException();
		}
	}
	
	/**
	 * Viene aggiornata un attivita dell'agenda
	 * se non avviene tale modifica lancia eccezione
	 * alla rimozione dal db verrà controllato l'appartenenza
	 * all'arraylist e in caso positivo modificata.
	 * @param vecchiaAttivita
	 * @param nuovaAttivita
	 * @throws ModificaAttivitaException
	 */
	
	public void aggiornaAttivita(AttivitaORM vecchiaAttivita, AttivitaORM nuovaAttivita) throws ModificaAttivitaException{
		if (gestoreAttivitaOrm.modificaAttivitaORM(vecchiaAttivita, nuovaAttivita)) {
			if (verificaAppartenenza(vecchiaAttivita)) {
				gestoreAttivitaOrm.getAttivitaOrm().remove(vecchiaAttivita);
				gestoreAttivitaOrm.getAttivitaOrm().add(nuovaAttivita);
			}
		} else {
			throw new ModificaAttivitaException();
		}
	}
	
	/**
	 * Viene aggiornata una categoria
	 * se non avviene tale modifica lancia 
	 * eccezione
	 * alla rimozione dal db verrà controllato l'appartenenza
	 * all'arraylist e in caso positivo modificata.
	 * @param vecchiaCategoria
	 * @param nuovaCategoria
	 * @throws ModificaCategoriaException
	 */
	
	public void aggiornaCategoria(CategoriaORM vecchiaCategoria, CategoriaORM nuovaCategoria) throws ModificaCategoriaException{
		if (gestoreCategorieOrm.modificaCategoriaORM(vecchiaCategoria, nuovaCategoria)) {
			if (verificaAppartenenza(vecchiaCategoria)) {
				gestoreCategorieOrm.getCategorieOrm().remove(vecchiaCategoria);
				gestoreCategorieOrm.getCategorieOrm().add(nuovaCategoria);
			}
		} else {
			throw new ModificaCategoriaException();
		}
	}
	
	/**
	 * Viene aggiornato un contatto
	 * se non avviene tale modifica lancia 
	 * eccezione
	 * @param vecchioContatto
	 * @param nuovoContatto
	 * @throws ModificaContattoException
	 */
	
	public void aggiornaContatto(ContattoORM vecchioContatto, ContattoORM nuovoContatto)throws ModificaContattoException{
		if (rubrica.modificaContattoORM(vecchioContatto, nuovoContatto)) {
			if (verificaAppartenenza(vecchioContatto)) {
				rubrica.getContattiORM().remove(vecchioContatto);
				rubrica.getContattiORM().add(nuovoContatto);
			}
		} else {
			throw new ModificaContattoException();
		}
	}
	
	/**
	 * Metodo supporto per verifica di
	 * appartenenza di un attivita/
	 * impegno/categoria/contatto ad
	 * arraylist
	 */
	private boolean verificaAppartenenza(Object obj){
		
		if(obj == null){
			return false;
		}
		
		if(obj instanceof ImpegnoORM){
			ImpegnoORM impegno = (ImpegnoORM) obj;
			if(gestoreImpegniOrm.getImpegniORM().contains(impegno)){
				return true;
			}
			return false;
		}
		if(obj instanceof AttivitaORM){
			AttivitaORM attivita = (AttivitaORM) obj;
			if(gestoreAttivitaOrm.getAttivitaOrm().contains(attivita)){
				return true;
			}
			return false;
		}
		if(obj instanceof CategoriaORM){
			CategoriaORM categoria = (CategoriaORM) obj;
			if(gestoreCategorieOrm.getCategorieOrm().contains(categoria)){
				return true;
			}
			return false;
		}
		if(obj instanceof ContattoORM){
			ContattoORM contatto = (ContattoORM) obj;
			if(rubrica.getContattiORM().contains(contatto)){
				return true;
			}
			return false;
		}
		return false;
	}
	
	
	/**
	 * Visualizza sovrapposizioni
	 * vengono visualizzate sovrapposizioni
	 * del giorno selezionato
	 * verrà riempito un arraylist con il giorno
	 * corrente basandosi su arraylist del mese 
	 * corrente
	 * @throws ImpegnoOrmNonValidoException 
	 */
	
	public List<ImpegnoORM> visualizzaImpegniCompatibili() throws ImpegnoOrmNonValidoException{
		//Impegni sovrapposti
		List<ImpegnoORM> impegniSovrapposti = new ArrayList<ImpegnoORM>();
		//Impegni del giorno
		List<Impegno> impegniDelGiorno;
		//Impegni del giorno ordinati secondo data di fine
		impegniDelGiorno = restituisciImpegniDelGiorno();
		if(!impegniDelGiorno.isEmpty()){
			impegniSovrapposti = restituisciImpegniOrdinati(impegniDelGiorno);
			return impegniSovrapposti;
		}
		else{
			return impegniSovrapposti;
		}
	}
	
	
	/**
	 * Viene esaminato l'arraylist del gestore
	 * ed estrapolati gli impegni che hanno data che 
	 * equivale al giorno come parametro e restituiti
	 * in caso non vi siano viene restituito arraylist vuoto 
	 */
	private List<Impegno> restituisciImpegniDelGiorno() throws ImpegnoOrmNonValidoException{
		List<Impegno> tmp = new ArrayList<Impegno>();
		
		try {
			SimpleDateFormat sdfGiorno = new SimpleDateFormat(PRECISIONE_GIORNO);
			
			//Impegni del mese presi tramite l'arraylist
			List<Impegno> impegniMese = gestoreImpegniOrm.getImpegni();
			
			//Calcolo data odierna in formato yyyy:mm:dd
			String dataOdierna = sdfGiorno.format(dataCorrente);
			Date dataConfrontoOdierna = sdfGiorno.parse(dataOdierna);
			
			//Data singola di un impegno da confrontarsi con data odierna
			String dataImpegnoAlGiorno;
			Date dataConfrontoImpegnoAlGiorno;
			
			//Confronto i vari impegni e verifico quali sono nel giorno corrente
			for (Impegno impegno : impegniMese) {
				dataImpegnoAlGiorno = sdfGiorno.format(impegno.getDataInizio());
				dataConfrontoImpegnoAlGiorno = sdfGiorno.parse(dataImpegnoAlGiorno);
				if(dataConfrontoImpegnoAlGiorno.compareTo(dataConfrontoOdierna) == 0){
					tmp.add(impegno);
				}
			}
		} catch (ParseException pe) {
			//Non serve gestire
		}
		return tmp;
	}
	
	
	/**
	 * Metodo per ricerca primo compatibile
	 * ricorsivamente, caso base equivale a indice
	 * uguale a 0 quando è stata controllata tutta la
	 * lista e vi sono compatibilità
	 * @param impegni
	 * @param indice
	 * @return
	 */
	
	public int trovaCompatibile(Impegno[] impegni, int indice) {
		
		if (indice == 0){
			return 0;
		}
		
		Date inizioDiRiferimento = impegni[indice].getDataInizio();
		Date dataFine;
		for (int i = indice - 1; i >= 0; i--) {
			
			dataFine = impegni[i].getDataFine();
			
			if (dataFine.before(inizioDiRiferimento))
				return i;
		}
		
		return 0;
	}

	/**
	 * Metodo per la restituzione della
	 * soluzione relativa alle sovrapposizioni
	 * Ritorna una lista contenente i vari impegni
	 * non sovrapposti
	 * @param listaOttima
	 * @param indice
	 * @param ottimo
	 * @param arrayAppoggio
	 * @return
	 */
	
	private List<Impegno> ricercaSoluzione(List<Impegno> listaOttima, int indice, int [] ottimo, Impegno[] arrayAppoggio){
		if(indice == 0){
			return listaOttima;
		}
		else if(ottimo[indice] > ottimo[trovaCompatibile(arrayAppoggio, indice)]){
			listaOttima.add(arrayAppoggio[indice]);
			ricercaSoluzione(listaOttima, trovaCompatibile(arrayAppoggio, indice), ottimo, arrayAppoggio);
		}
		else{
			ricercaSoluzione(listaOttima, indice - 1, ottimo ,arrayAppoggio);
		}
		return listaOttima;
	}
	
	/**
	 * Metodo di utilità generale per il confronto
	 * fra due date, con precisione al giorno.
	 * 
	 * @param data1
	 * @param data2
	 * @return
	 */
	public static int confrontaDate(Date data1, Date data2, String formato) {
		
		SimpleDateFormat sdfGiorno = new SimpleDateFormat(formato);
		
		String formatoData1 = sdfGiorno.format(data1);
		String formatoData2 = sdfGiorno.format(data2);
		
		/*
		 *  diamogli un valore non nullo, comunque verranno sovrascritte.
		 *  la parse exception NON può capitare
		 */
		Date dataGiorno1 = data1;
		Date dataGiorno2 = data2;
		try {
			dataGiorno1 = sdfGiorno.parse(formatoData1);
			dataGiorno2 = sdfGiorno.parse(formatoData2);
		} catch (ParseException e) {
			// Impossibile venga generata una eccezione
		}
		
		return dataGiorno1.compareTo(dataGiorno2);
	}
	
	/**
	 * Restituisce gli impegni ordinati secondo data di fine.
	 * ritorna un array ordinato
	 * @param impegniDaOrdinare 
	 */
	private List<ImpegnoORM> restituisciImpegniOrdinati(List<Impegno> impegniDaOrdinare){
		impegniDaOrdinare.sort(new ImpegnoComparator());
		
		Impegno [] arrayAppoggio = new Impegno[impegniDaOrdinare.size()];
		for(int i = 0; i<arrayAppoggio.length; i++){
			arrayAppoggio[i] = impegniDaOrdinare.get(i);
		}
		
		int[] ottimo = new int[arrayAppoggio.length];
		
		ottimo[0] = 0;
		
		for (int i = 1; i < arrayAppoggio.length; i++) {
			ottimo[i] = Math.max(1 + ottimo[trovaCompatibile(arrayAppoggio, i)], ottimo[i - 1]);
		}
		
		
		List<Impegno> listaOttima = new ArrayList<Impegno>();
		listaOttima = ricercaSoluzione(listaOttima, ottimo.length - 1, ottimo, arrayAppoggio);
		List<ImpegnoORM> listaOttimaORM = new ArrayList<ImpegnoORM>(listaOttima.size());
		for (Impegno impegno : listaOttima) {
			ImpegnoORM impegnoOrm = new ImpegnoORM();
			impegnoOrm.setTitolo(impegno.getTitolo());
			impegnoOrm.setDataInizio(impegno.getDataInizio());
			impegnoOrm.setDataFine(impegno.getDataFine());
			impegnoOrm.setNote(impegno.getNote());
			impegnoOrm.setRipetizione(impegnoOrm.getRipetizione());
			impegnoOrm.setAllarme(impegno.getAllarme());
			listaOttimaORM.add(impegnoOrm);
		}
		return listaOttimaORM;
	}
	
	/**
	 * Restituisce l'attivita con priorità massima 
	 * che non si sovrappone agli impegni attualmente
	 * in corso di circa una ora.
	 * @throws AttivitaNonValidaException 
	 * 
	 */
	public List<Attivita> restituisciAttivitaDaSvolgere() throws AttivitaNonValidaException {
		
		try {
			
			if (verificaImpegniAttivi())
				return new ArrayList<Attivita>();
			
			// Qui è possibile effettuare qualche attivita
			
			List<AttivitaORM> attivitaAttiveOrm = memory.restituisciAttivitaAttiveIn(this.dataCorrente, Agenda.PRECISIONE_MINUTO);
			List<Attivita> attivitaAttive = new ArrayList<Attivita>();
			List<Attivita> attivitaPriorita = new ArrayList<Attivita>();
			
			for (AttivitaORM attivitaOrm : attivitaAttiveOrm) {
				attivitaAttive.add(attivitaOrm.restituisciAttivita());
			}

			attivitaAttive.sort(new Comparator<Attivita>() {

				public int compare(Attivita o1, Attivita o2) {
					return o1.getDataScadenza().compareTo(o2.getDataScadenza());
				}
			});

			// prendiamo le prime 5 attivita in ordine di scadenza.
			int tmpNumeroAttivita = 5;

			if (tmpNumeroAttivita > attivitaAttive.size())
				tmpNumeroAttivita = attivitaAttive.size();

			for (int i = 0; i < tmpNumeroAttivita; i++) {
				attivitaPriorita.add(attivitaAttive.get(i));
			}

			// ordiniamo in base alla priorita le attivita
			attivitaPriorita.sort(new Comparator<Attivita>() {

				public int compare(Attivita o1, Attivita o2) {
					return o1.getCategoria().getPriorita().compareTo(o2.getCategoria().getPriorita());
				}
			});
			
			return attivitaPriorita;
			
		} catch(SQLException se) {
			logger.info(se, se.getMessage());
			throw new AttivitaNonValidaException(se);
		} catch (ImpegnoOrmNonValidoException se) {
			logger.info(se, se.getMessage());
			throw new AttivitaNonValidaException(se);
		}
		
	}
	/**
	 * Restitisce true se ci sono impegni attivit nella data corrente
	 * CON PRECISIONE AL MINUTO.
	 * @return
	 * @throws ImpegnoOrmNonValidoException
	 */
	private boolean verificaImpegniAttivi() throws ImpegnoOrmNonValidoException {
		try {
			List<Impegno> impegniNelGiorno = restituisciImpegniDelGiorno();
			SimpleDateFormat sdfMinuto = new SimpleDateFormat(PRECISIONE_MINUTO);
			String dataAttualeStringa = sdfMinuto.format(this.dataCorrente);
			Date dataCorrenteMinuto = sdfMinuto.parse(dataAttualeStringa);

			/*
			 * Se nella data corrente c'è anche solo un impegno. io non posso
			 * fare NULLA!
			 */
			for (Impegno impegno : impegniNelGiorno) {
				if (dataCorrenteMinuto.compareTo(impegno.getDataInizio()) >= 0
						&& dataCorrenteMinuto.compareTo(impegno.getDataFine()) <= 0)
					return true;

			}
		}  catch (ParseException e) {
			logger.info(e, e.getMessage());
		}
		
		return false;
	}
	
}
