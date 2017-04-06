package com.soflod.agenda.core;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.print.attribute.Size2DSyntax;

import java.awt.Color;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.junit.Assert;
import org.junit.Test;

import com.soflod.agenda.core.exception.CategorieNonValideException;
import com.soflod.agenda.core.exception.DataFineNonValidaException;
import com.soflod.agenda.core.exception.DataInizioNonValidaException;
import com.soflod.agenda.core.exception.DataScadenzaNonValidaException;
import com.soflod.agenda.core.exception.DateNonValideException;
import com.soflod.agenda.core.exception.EmailNonValidaException;
import com.soflod.agenda.core.exception.LimiteEtichettaSuperatoException;
import com.soflod.agenda.core.exception.LimiteNoteSuperatoException;
import com.soflod.agenda.core.exception.LimiteTitoloSuperatoException;
import com.soflod.agenda.core.exception.NumeroTelefonoNonValidoException;
import com.soflod.agenda.core.exception.PrioritaNonValidaException;
import com.soflod.agenda.core.exception.TelefoniNonValidiException;

import junit.framework.AssertionFailedError;

public class CoreTest {

	/*
	 * Test della classe Priorita.
	 */
	@Test
	public void testDefaultPriorita() {
    	
    	Priorita p = new Priorita();
    	Assert.assertTrue(p.getValorePriorita() == 1);
	}
	
	@Test
	public void testSetPriorita() {
		
		try{
			Priorita p = new Priorita((byte) 5);
			
			p = new Priorita((byte) 5);
			Assert.assertTrue(p.getValorePriorita() == 5);
			
    		p.setValorePriorita((byte) 1);
    		Assert.assertTrue(p.getValorePriorita() == 1);
    	} catch(Exception e) {
    		// Non fare nulla
    	}
	}
	
	@Test (expected = PrioritaNonValidaException.class)
	public void testPrioritaException() throws PrioritaNonValidaException {
		Priorita p = new Priorita((byte) 11);
	}
	
	/*
	 *  Test della classe Categoria.
	 */
	/**
	 * Verifica la correttezza dei creatori di Categoria.
	 * 
	 * @throws LimiteEtichettaSuperatoException
	 */
	@Test
	public void testCategoriaCreator() throws PrioritaNonValidaException, LimiteEtichettaSuperatoException {
		
		Categoria c = new Categoria("Lavoro", Color.BLACK);
		Assert.assertTrue(c.getEtichetta().equals("Lavoro"));
		Assert.assertTrue(c.getColore() == Color.BLACK);
		Assert.assertTrue(c.getPriorita().getValorePriorita() == (byte) 1); 
	
		c = new Categoria("Lavoro", Color.BLACK, new Priorita((byte) 4));
		Assert.assertTrue(c.getEtichetta().equals("Lavoro"));
		Assert.assertTrue(c.getColore() == Color.BLACK);
		Assert.assertTrue(c.getPriorita().getValorePriorita() == (byte) 4); 
	
		c = new Categoria("Lavoro", new Priorita((byte) 4));
		Assert.assertTrue(c.getEtichetta().equals("Lavoro"));
		Assert.assertTrue(c.getColore() == Categoria.getColoreDefault());
		Assert.assertTrue(c.getPriorita().getValorePriorita() == (byte) 4); 
	}
	
	/* Test della classe Attivita
	 * 
	 * Nel confronto con le date si usa il confronto con la stringa
	 * restituita dal formattatore.
	 * 
	 * Cio' perche' la data ha una precisione al millisecondo, mentre
	 * per noi e' rilevante solo il giorno, il mese e l'anno.
	 */
	
	/**
	 * Test del creatore della classe Attivita.
	 * Viene impostata un' etichetta ed una data di scadenza
	 * di 20 giorni a partire dal giorno odierno.
	 * 
	 * La categoria, sarà quella di default e la
	 * data di inizio dell'attività sarà la data odierna.
	 * 
	 */
	@Test
	public void testAttivitaDefaultCreator() {
		
		SimpleDateFormat sdf = new SimpleDateFormat(Attivita.FORMATO_DATA_ATTVITA);
		Date today = new Date();
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(today);
		
		calendar.add(Calendar.DATE, 20);
		String future20 = sdf.format(calendar.getTime());
		
		try {
		
			Attivita a = new Attivita.Builder("Mangiare", sdf.parse(future20)).build();
			
			//Sezione assert
			Assert.assertTrue(a.getTitolo().equals("Mangiare"));
			Assert.assertTrue(sdf.format(a.getDataInizio()).equals(sdf.format(today)));
			Assert.assertTrue(a.getCategoria().equals(new Categoria()));
			Assert.assertTrue(sdf.format(a.getDataScadenza()).equals(future20));
			
		} catch(Exception e) {
			// Non fare nulla, non ci sono eccezioni
		}
	}
	
	/**
	 * Test di un creatore complesso della classe Attivita,
	 * implementata mediante il pattern Builder.
	 * 
	 * Viene definita una nuova categoria, una data di
	 * inizio diversa da quella odierna di default e una
	 * data di scadenza che non generi una eccezione (3 mesi
	 * e 10 giorni dopo la data di inizio).
	 */
	@Test
	public void testAttivitaConstructorComplesso() {
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date today = new Date();
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(today);
		
		calendar.add(Calendar.DATE, 20);
		String future20 = sdf.format(calendar.getTime());
		
		/* Sposta il calendario di 3 mesi e 10 giorni
		 * in avanti rispetto il precedente avanzamento
		 * di 20 giorni.
		 */
		calendar.add(Calendar.MONTH, 3);
		calendar.add(Calendar.DATE, 10);
		String future_3months = sdf.format(calendar.getTime());
		
		try {
			
			Categoria fondamentale = new Categoria("Fondamentale",
					Color.GRAY,
					new Priorita((byte) 10));
			
			Categoria fondamentale2 = new Categoria("Fondamentale",
					Color.GRAY,
					new Priorita((byte) 10));
			
			Attivita a = new Attivita.Builder("Dormire", sdf.parse(future_3months))
					.categoria(fondamentale)
					.dataInizio(sdf.parse(future20))
					.build();
			
			//Sezione assert
			Assert.assertTrue(a.getTitolo().equals("Dormire"));
			Assert.assertTrue(sdf.format(a.getDataInizio()).equals(future20));
			Assert.assertTrue(a.getCategoria().equals(fondamentale));
			Assert.assertTrue(sdf.format(a.getDataScadenza()).equals(future_3months));
			
		} catch(Exception e) {
			// Non fare nulla, non ci sono eccezioni
		}
	}
	
	/**
	 * Viene instanziata un'Attivita alla quale
	 * vengono modificati la categoria, il titolo e
	 * la data di Scadenza.
	 */
	@Test
	public void testSetAttivita() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date today = new Date();
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(today);
		
		calendar.add(Calendar.DATE, 1);
		String tomorrow = sdf.format(calendar.getTime());
		
		calendar.add(Calendar.DATE, 20);
		String future20 = sdf.format(calendar.getTime());
		
		try {
		
			Attivita a = new Attivita.Builder("Codificare", sdf.parse(tomorrow))
					.build();
			
			Categoria modifica = a.getCategoria();
			String nuova_etichetta = "Etichetta Modificata";
			String nuovo_titolo = "Nuovo titolo";
			modifica.setEtichetta(nuova_etichetta);
			a.setCategoria(modifica);
			
			a.setTitolo(nuovo_titolo);
			a.setDataScadenza(sdf.parse(future20));
			
			//Sezione assert
			Assert.assertTrue(a.getCategoria().getEtichetta().equals(nuova_etichetta));
			Assert.assertTrue(a.getTitolo().equals(nuovo_titolo));
			Assert.assertTrue(sdf.format(a.getDataScadenza()).equals(future20));
			
		} catch(Exception e) {
			// Non ci saranno eccezioni
		}
	}
	
	/**
	 * Viene creata un'istanza di Attivita con una data di
	 * scadenza inferiore a quella di inizio (20 giorni precedenti
	 * la data di inizio).
	 * 
	 * Verrà lanciata una eccezione sulla validità della data di
	 * scadenza.
	 * 
	 * @throws DataScadenzaNonValidaException
	 */
	@Test (expected = DataScadenzaNonValidaException.class)
	public void testDataScadenzaAttivita() throws DataScadenzaNonValidaException {
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date today = new Date();
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(today);
		
		calendar.add(Calendar.DATE, -20);
		String future20 = sdf.format(calendar.getTime());
		try {
			Attivita a = new Attivita.Builder("Codificare", sdf.parse(future20)).build();
		} catch(ParseException pe) {
			// Non ci saranno eccezioni sul parsing.
		} catch(LimiteTitoloSuperatoException le) {
			// Non ci saranno eccezioni sulla lunghezza dell'etichetta
		}
	}
	
	/**
	 * 
	 * Viene creata un'istanza di Attivita con una data di
	 * scadenza maggiore di quella di inizio (20 giorni in avanti
	 * rispetto la data di inizio).
	 * 
	 * Verrà lanciata una eccezione sulla validità della data di
	 * scadenza.
	 * 
	 * @throws DataInizioNonValidaException
	 */
	@Test (expected = DataInizioNonValidaException.class)
	public void testDataInizioAttivita() throws DataInizioNonValidaException {
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date today = new Date();
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(today);
		
		calendar.add(Calendar.DATE, 1);
		String tomorrow = sdf.format(calendar.getTime());
		
		calendar.add(Calendar.DATE, 20);
		String future20 = sdf.format(calendar.getTime());
		
		try {
		
			Attivita a = new Attivita.Builder("Codificare", sdf.parse(tomorrow))
					.dataInizio(sdf.parse(future20))
					.build();
			
		} catch(ParseException pe) {
			// Non ci saranno eccezioni sul parsing.
		} catch(LimiteTitoloSuperatoException le) {
			// Non ci saranno eccezioni sulla lunghezza dell'etichetta
		} catch(DataScadenzaNonValidaException ds) {
			// Non ci saranno eccezioni sulla data di scadenza
		}
	}
	
	/**
	 * Viene instanziata una Attivita con un titolo
	 * la cui dimensione viola la lunghezza massima consentita.
	 * 
	 * @throws LimiteTitoloSuperatoException
	 */
	@Test (expected = LimiteTitoloSuperatoException.class)
	public void testLimiteTitoloAttivita() throws LimiteTitoloSuperatoException {
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date today = new Date();
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(today);
		
		calendar.add(Calendar.DATE, 1);
		String tomorrow = sdf.format(calendar.getTime());
		
		try {
			
			Attivita a = new Attivita.Builder("Dormire Mangiare Codificare e piu' " +
					"di tot caratteri non si puo' andare",
					sdf.parse(tomorrow))
					.build();
			
		} catch(ParseException pe) {
			// Non ci saranno eccezioni sul parsing.
		} catch(DataScadenzaNonValidaException ds) {
			// Non ci saranno eccezioni sulla data di scadenza
		}
	}
	
	/*
	 * Test uguaglianza attività
	 */
	/**
	 * Vengono istanziate due attivita complesse completamente uguali
	 *  e viene verificata l'uguaglianza tra queste.
	 */
	@Test
	public void testUguaglianzaAttivita(){
		// Si accettano suggerimenti per il nome del metodo di test.
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date today = new Date();
			
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(today);
			
		calendar.add(Calendar.DATE, 20);
		String future20 = sdf.format(calendar.getTime());
				
		/* Sposta il calendario di 3 mesi e 10 giorni
		 * in avanti rispetto il precedente avanzamento
		 * di 20 giorni.
		 */
		calendar.add(Calendar.MONTH, 3);
		calendar.add(Calendar.DATE, 10);
		String future_3months = sdf.format(calendar.getTime());
				
		try {
					
			Categoria fondamentale = new Categoria("Fondamentale",
					Color.GRAY,
					new Priorita((byte) 10));
					
			Categoria fondamentale2 = new Categoria("Fondamentale",
					Color.GRAY,
					new Priorita((byte) 10));
					
			Attivita a = new Attivita.Builder("Dormire", sdf.parse(future_3months))
					.categoria(fondamentale)
					.dataInizio(sdf.parse(future20))
					.build();
					
			Attivita a1 = new Attivita.Builder("Dormire", sdf.parse(future_3months))
					.categoria(fondamentale)
					.dataInizio(sdf.parse(future20))
					.build();
			
			//Sezione assert
			Assert.assertTrue(a.equals(a1));
					
		} catch(Exception e) {
			// Non fare nulla, non ci sono eccezioni
		}
	}
	
	/*
	 * Test della classe Impegno.
	 */
	/**
	 * Verifica corretta istanziazione della classe impegno (con ripetizione di default)
	 * Verrà creata un istanza di impegno con costruttore default-basic.
	 * Test di corretta inizializzazione parametri
	 */
	@Test 
	public void testCostruttoreImpegno() {
		try{	
			Categoria categoria = new Categoria("Lavoro", Color.BLACK);
		
			SimpleDateFormat sdf = new SimpleDateFormat(Impegno.FORMATO_DATA_IMPEGNO);
			Date today = new Date();
		
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(today);
		
			calendar.add(Calendar.HOUR, 1);
			String hours1 = sdf.format(calendar.getTime());
			Impegno i = new Impegno.Builder("Impegno", categoria)
					.date(today, sdf.parse(hours1))
					.allarme(true)
					.ripetizione(Ripetizione.GIORNO)
					.build();
		
			//Sezione assert
			Assert.assertTrue(i.getTitolo().equals("Impegno"));
			Assert.assertTrue(sdf.format(i.getDataInizio()).equals(sdf.format(today)));
			Assert.assertTrue(i.getDataFine().equals(sdf.parse(hours1)));
			Assert.assertTrue(i.getAllarme() == true);
			Assert.assertTrue(i.getNote().equals(""));
			Assert.assertTrue(i.getRipetizione() .equals(Ripetizione.GIORNO));
			
		}catch(DateNonValideException di){
			//Non ci saranno eccezioni a riguardo
		}catch(LimiteTitoloSuperatoException le){
			//Non ci saranno eccezioni sul tiolo
		}catch(ParseException pe) {
			// Non ci saranno eccezioni sul parsing.
		}catch(LimiteEtichettaSuperatoException le){
			// Non ci saranno eccezioni dell'etichetta
		}
	}
	
	
	/* 
	 * Test settaggio parametri attività classe Impegno
	 */
	/**
	 * Verifica settaggio parametri attività
	 * Verrà creata una istanza di impegno con 
	 * costruttore default-basic.
	 * Successivamente verranno settati tutti gli 
	 * attributi di default.
	 * Verrà controllato il corretto funzionamento
	 * del settaggio degli attributi 
	 */
	@Test
	public void testSetImpegno(){
		try{	
			Categoria categoria = new Categoria("Lavoro", Color.BLACK);
		
			SimpleDateFormat sdf = new SimpleDateFormat(Impegno.FORMATO_DATA_IMPEGNO);;
			Date today = new Date();
		
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(today);
			
			/* Sposta il calendario di 2 ore
			 * in avanti rispetto il precedente avanzamento
			 * 
			 */
			calendar.add(Calendar.HOUR, 2);
			String future2hours = sdf.format(calendar.getTime());
			
			Impegno i = new Impegno.Builder("Impegno", categoria)
					.date(new Date(), sdf.parse(future2hours)).build();
			
			//i.setDataInizio(today);
			i.setAllarme(true);
			i.setNote("Prima nota");
			i.setRipetizione(Ripetizione.GIORNO);
			
			//Sezione assert
			Assert.assertTrue(i.getTitolo().equals("Impegno"));
			Assert.assertTrue(sdf.format(i.getDataInizio()).equals(sdf.format(today)));
			Assert.assertTrue(sdf.format(i.getDataFine()).equals(future2hours));
			Assert.assertTrue(i.getAllarme() == true);
			Assert.assertTrue(i.getNote().equals("Prima nota"));
			Assert.assertTrue(i.getRipetizione().equals(Ripetizione.GIORNO));
		}
		catch(DateNonValideException di){
			//Non ci saranno eccezioni a riguardo
		}catch(LimiteTitoloSuperatoException le){
			//Non ci saranno eccezioni sul tiolo
		}catch(ParseException pe) {
			// Non ci saranno eccezioni sul parsing.
		}catch(LimiteEtichettaSuperatoException le){
			// Non ci saranno eccezioni dell'etichetta
		}catch(LimiteNoteSuperatoException ln){
			// Non ci saranno eccezioni sulle note
		}
	}
	
	
	/*
	 * Test superamento limite titolo attributo classe Impegno
	 */
	/**
	 * Viene istanziato un impegno con titolo che supera 
	 * il limite di caratteri per il titolo.
	 * Pertanto avremo un eccezione di titolo
	 * correttamente gestita
	 * @throws LimiteNoteSuperatoException
	 * @throws DataFineNonValidaException
	 * @throws LimiteTitoloSuperatoException
	 * @throws LimiteEtichettaSuperatoException
	 * @throws ParseException
	 */
	@Test (expected = LimiteTitoloSuperatoException.class)
	public void testLimiteTitoloImpegno() throws LimiteTitoloSuperatoException{
		try{

			Categoria categoria = new Categoria("Lavoro", Color.BLACK);

			SimpleDateFormat sdf = new SimpleDateFormat(Impegno.FORMATO_DATA_IMPEGNO);;
			Date today = new Date();

			Calendar calendar = Calendar.getInstance();
			calendar.setTime(today);

			calendar.add(Calendar.DATE, 1);
			String tomorrow = sdf.format(calendar.getTime());
			Impegno i = new Impegno.Builder("Impegno per un lavoro duro e difficile ma che ci porterà proficui guadagni "
					+ "nella nostra carriera lavorativa da universitari", 
					categoria)
					.date(new Date(), sdf.parse(tomorrow)).allarme(true).note("Prima nota").build();	


		}catch(ParseException pe) {
			// Non ci saranno eccezioni sul parsing.
		}catch(LimiteEtichettaSuperatoException le){
			// Non ci saranno eccezioni dell'etichetta
		}catch(LimiteNoteSuperatoException ln){
			// Non ci saranno eccezioni sulle note
		} catch (DateNonValideException e) {
			// Non ci saranno eccezioni sulla data di inizio
		}
	}
	
	// Test superamento lunghezza note attributo classe Impegno
	/**
	 * Test per verifica lunghezza massima di note
	 * Verrà creata un istanza di impegno e assegnata
	 * una lunghezza a "note" superiore a quella prevista
	 * (1024 dati), successivamente verrà gestita l'eccezione
	 * di note
	 * @throws LimiteNoteSuperatoException
	 * @throws DataFineNonValidaException
	 * @throws LimiteTitoloSuperatoException
	 * @throws LimiteEtichettaSuperatoException
	 * @throws ParseException
	 */
	@Test (expected = LimiteNoteSuperatoException.class)
	public void testLimiteNoteImpegno()throws LimiteNoteSuperatoException{
		try{
			
			Categoria categoria = new Categoria("Lavoro", Color.BLACK);
			
			SimpleDateFormat sdf = new SimpleDateFormat(Impegno.FORMATO_DATA_IMPEGNO);;
			Date today = new Date();
			
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(today);
			
			calendar.add(Calendar.HOUR, 1);
			String hours1 = sdf.format(calendar.getTime());
			Impegno i = new Impegno.Builder("Impegno", 
					categoria).date(new Date(), sdf.parse(hours1)).allarme(true).note("Prima nota di impegno per un lavoro duro"
							+ " e difficile ma che ci porterà proficui guadagni "
							+ "nella nostra carriera lavorativa da universitari"
							+ "inoltre per il lavoro vi sarà più esperienza"
							+ "alla fine è un bagaglio culturale enorme"
							+ "poter fare un progetto in gruppo"
							+ "si impara a lavorare in gruppo ed è utile in"
							+ "futuro dal punto di vista lavorativo"
							+ "e non solo"
							+ "spero che questo mi aiuterà veramente come penso"
							+ " e difficile ma che ci porterà proficui guadagni "
							+ "nella nostra carriera lavorativa da universitari"
							+ "inoltre per il lavoro vi sarà più esperienza"
							+ "alla fine è un bagaglio culturale enorme"
							+ "poter fare un progetto in gruppo"
							+ "si impara a lavorare in gruppo ed è utile in"
							+ "futuro dal punto di vista lavorativo"
							+ "e non solo"
							+ "spero che questo mi aiuterà veramente come penso"
							+ " e difficile ma che ci porterà proficui guadagni "
							+ "nella nostra carriera lavorativa da universitari"
							+ "inoltre per il lavoro vi sarà più esperienza"
							+ "alla fine è un bagaglio culturale enorme"
							+ "poter fare un progetto in gruppo"
							+ "si impara a lavorare in gruppo ed è utile in"
							+ "futuro dal punto di vista lavorativo"
							+ "e non solo"
							+ "spero che questo mi aiuterà veramente come penso"
							+ " e difficile ma che ci porterà proficui guadagni "
							+ "nella nostra carriera lavorativa da universitari"
							+ "inoltre per il lavoro vi sarà più esperienza"
							+ "alla fine è un bagaglio culturale enorme"
							+ "poter fare un progetto in gruppo"
							+ "si impara a lavorare in gruppo ed è utile in"
							+ "futuro dal punto di vista lavorativo"
							+ "e non solo"
							+ "spero che questo mi aiuterà veramente come penso").build();	
			
			
		}catch(ParseException pe) {
			// Non ci saranno eccezioni sul parsing.
		}catch(LimiteEtichettaSuperatoException le){
			// Non ci saranno eccezioni dell'etichetta
		}catch(LimiteTitoloSuperatoException ln){
			// Non ci saranno eccezioni sul titolo
		} catch (DateNonValideException e) {
			// Non ci saranno problemi sulla data di inizio
		}
	}
	
	
	/**
	 * Test data inizio prima di data fine
	 * Verrà creata un istanza di impegno con
	 * data di inizio superiore a quella di 
	 * fine e pertanto verrà gestita l'eccezione
	 * @throws DataInizioNonValidaException
	 */
	@Test (expected = DateNonValideException.class)
	public void testDataInizioImpegno() throws DateNonValideException {
		try{
			
			Categoria categoria = new Categoria("Lavoro", Color.BLACK);
			
			SimpleDateFormat sdf = new SimpleDateFormat(Impegno.FORMATO_DATA_IMPEGNO);;
			Date today = new Date();
			
			Calendar calendar = Calendar.getInstance();
			
			/*
			 * Setto calendario 2 ore in avanti rispetto a oggi
			 */
			calendar.setTime(today);
			calendar.add(Calendar.HOUR, 2);
			String future_2hours = sdf.format(calendar.getTime());
			
			/*
			 * Setto calendario 20 giorni in avanti rispetto a today
			 */
			
			calendar.add(Calendar.DATE, 20);
			String future20 = sdf.format(calendar.getTime());
			
			Impegno i = new Impegno.Builder("Impegno", categoria)
					.date(sdf.parse(future20), sdf.parse(future_2hours))
					.allarme(true).note("Prima nota").build();	
			
		}catch(ParseException pe) {
			// Non ci saranno eccezioni sul parsing.
		}catch(LimiteEtichettaSuperatoException le){
			// Non ci saranno eccezioni dell'etichetta
		}catch(LimiteNoteSuperatoException ln){
			// Non ci saranno eccezioni sulle note
		}catch(LimiteTitoloSuperatoException lt){
			// Non ci saranno eccezioni sul titolo
		}
	}
	
	//TODO: Questo test non serve più a molto, bisognerebbe sostiuire le eccezione delle due date con una sola
	/**
	 * test data fine dopo data inizio
	 * Verrà creata un istanza di impegno con
	 * data di fine superiore più di un giorno 
	 * a quella di inizio e pertanto verrà gestita l'eccezione
	 * @throw DataFineNonValidaException
	 */
	@Test (expected = DateNonValideException.class)
	public void testDataFineImpegno () throws DateNonValideException {
		try{
			
			Categoria categoria = new Categoria("Lavoro", Color.BLACK);
			
			SimpleDateFormat sdf = new SimpleDateFormat(Impegno.FORMATO_DATA_IMPEGNO);;
			Date today = new Date();
			
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(today);
			
			/*
			 * Sposto calendario di due ore in avanti
			 */
			
			calendar.add(Calendar.HOUR, 2);
			String future2hours = sdf.format(calendar.getTime());
			
			/* Sposta il calendario di 3 mesi e 10 giorni
			 * in avanti rispetto il precedente avanzamento
			 * di 20 giorni.
			 */
			calendar.add(Calendar.MONTH, 3);
			calendar.add(Calendar.DATE, 10);
			String future_3months = sdf.format(calendar.getTime());
			
			Impegno i = new Impegno.Builder("Impegno", categoria)
					.allarme(true).note("Prima nota").build();	
			i.setDate(sdf.parse(future2hours), sdf.parse(future_3months));
			
			
		}catch(ParseException pe) {
			// Non ci saranno eccezioni sul parsing.
		}catch(LimiteEtichettaSuperatoException le){
			// Non ci saranno eccezioni dell'etichetta
		}catch(LimiteNoteSuperatoException ln){
			// Non ci saranno eccezioni sulle note
		}catch(LimiteTitoloSuperatoException lt){
			// Non ci saranno eccezioni sul titolo
		}
	}
	
	
	/*
	 * Verifica uguaglianza tra due impegni
	 */
	/**
	 * Verifica uguaglianza tra due impegni
	 * istanziazione di due impegni con relativa
	 * verifica
	 */
	public void testUguaglianzaImpegni(){
		try{	
			
			Categoria categoria = new Categoria("Lavoro", Color.BLACK);
		
			SimpleDateFormat sdf = new SimpleDateFormat(Impegno.FORMATO_DATA_IMPEGNO);;
			Date today = new Date();
		
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(today);
		
			calendar.add(Calendar.DATE, 1);
			String tomorrow = sdf.format(calendar.getTime());
			// impegno 2
			Impegno i = new Impegno.Builder("Impegno", categoria).date(new Date(), sdf.parse(tomorrow)).build();
			
			// impegno 2
			Impegno i1 = new Impegno.Builder("Impegno", categoria).date(new Date(), sdf.parse(tomorrow)).build();
			
			Assert.assertTrue(i.equals(i1));
		}catch (Exception e){
			//tutte eccezioni possibili non verranno lanciate
		}
	}
	
	/*
	 * Test della classe Telefono
	 */
	/**
	 * Vengono create due istanze di Telefono in modo
	 * da verificare il costruttore di default e quello che accetta
	 * un numero.
	 */
	@Test (expected = NumeroTelefonoNonValidoException.class)
	public void testTelefonoNonInserito() throws NumeroTelefonoNonValidoException {
		// Test con telefono a vuoto
		Telefono t = new Telefono("");
	}
	
	@Test (expected = NumeroTelefonoNonValidoException.class)
	public void testTelefonoNonValido() throws NumeroTelefonoNonValidoException {
		// Test con telefono a vuoto
		Telefono t = new Telefono("+222444777982+");
	}
	
	@Test (expected = NumeroTelefonoNonValidoException.class)
	public void testModificaTelefonoNonValido() throws NumeroTelefonoNonValidoException {
		// Test con telefono a vuoto
		Telefono t = new Telefono("+222444777982");
		t.setNumeroTelefono(t.getNumeroTelefono() + "+");
	}
	
	/**
	 * Istanzio un numero di telefono valido,
	 * verfico che il confronto con una stringa(!)
	 * rappresentante lo stesso numero di telefono
	 * dia esito negativo.
	 * 
	 * Modifico il numero di telefono inserendo un numero valido.
	 * Verifico l'effettiva modifica del numero inserito.
	 * 
	 * Verifico la rappresentazione simbolica dell'oggetto
	 * telefono.
	 * 
	 * @throws NumeroTelefonoNonValidoException
	 */
	@Test
	public void testTelefono() throws NumeroTelefonoNonValidoException {
		
		Telefono t = new Telefono("+391234567891");
		
		String phone = "+391234567891";
		
		Assert.assertFalse(t.equals(phone));
		Assert.assertTrue(t.equals(new Telefono(phone)));
		
		t.setNumeroTelefono("+39112");
		Assert.assertTrue(t.getNumeroTelefono().equals("+39112"));
		
		Assert.assertTrue(t.toString().equals("{\"telefono\" : \"+39112\"}"));
		
	}
	
	/*
	 * Test della classe Email
	 */
	/**
	 * Verifica del costruttore della classe Email.
	 * 
	 * Verifica dell'operazione di modifica della email
	 * e della rappresentazione simbolica dell'oggetto di
	 * tipo Email.
	 * 
	 * @throws EmailNonValidaException
	 */
	@Test
	public void testEmail() throws EmailNonValidaException {
		
		String mail = "nome.congome@campus.unimib.it";
		
		Email e = new Email(mail);
		Assert.assertTrue(e.getIndirizzoEmail().equals(mail));
		
		e.setIndirizzoEmail(mail.toUpperCase());
		Assert.assertFalse(e.getIndirizzoEmail().equals(mail));
		Assert.assertTrue(e.getIndirizzoEmail().equals(mail.toUpperCase()));
		
		e.setIndirizzoEmail("m.a.i.l@dominio.prov");
		Assert.assertTrue(e.toString().equals("\"indirizzo-email\" : \"m.a.i.l@dominio.prov\""));	
	}
	
	@Test (expected = EmailNonValidaException.class)
	public void testCostruttoreEmailNonValida() 
			throws EmailNonValidaException {
		
		Email e = new Email("harry.avadakedavra.potter");
	}
	
	@Test (expected = EmailNonValidaException.class)
	public void testModificaEmailNonValida() 
			throws EmailNonValidaException {
		
		Email e = new Email("harry@avadakedavra.potter");
		
		e.setIndirizzoEmail("h@potter");
	}
	
	/*
	 * Test della classe Contatto
	 */
	
	/** 
	 * Verifica di correttezza dell'
	 *  istanziazione di un contatto
	 * con diversi attributi e diverse 
	 * categorie
	 * @throws LimiteEtichettaSuperatoException
	 * @throws CategorieNonValideException
	 * @throws PrioritaNonValidaException
	 */
	
	@Test
	public void testContattoCategorie()
			throws LimiteEtichettaSuperatoException,
				CategorieNonValideException,
				PrioritaNonValidaException {
		
		Contatto c = new Contatto.Builder("Studente").build();
		
		Assert.assertTrue(c.getNome().equals("Studente"));
		Assert.assertTrue(c.getCognome().equals(""));
		Assert.assertTrue(c.getCategorie().contains(new Categoria()));
		Assert.assertTrue(c.getEmails().isEmpty());
		Assert.assertTrue(c.getTelefoni().isEmpty());
		
		/*
		 * Se aggiungo una categoria già presente nell'ArrayList, questa non viene inserita
		 */
		Contatto d = new Contatto.Builder("Studente").categoria(new Categoria()).build();
		Assert.assertTrue(d.getCategorie().size() == 1);
		
		/*
		 * Posso aggiungere più di una categoria individualmente grazie al pattern Builder
		 */
		Categoria cat2 = new Categoria("Famiglia", Color.BLUE);
		Categoria copia_cat2 = new Categoria("Famiglia", Color.BLUE);
		d = new Contatto.Builder("Studente").categoria(new Categoria()).
				categoria(cat2).
				build();
		Assert.assertTrue(d.getCategorie().size() == 2);
		Assert.assertTrue(d.getCategorie().contains(cat2));
		
		/*
		 *  nonostante copia_cat2 sia un nuovo oggetto, esso e'
		 *  comunque presente tra le categorie del contatto,
		 *  in quanto i parametri di copia_cat2 sono gli stessi
		 *  di cat2, questo grazie all'override del metodo equals
		 *  nella classe Categoria.
		 */
		Assert.assertTrue(d.getCategorie().contains(copia_cat2));
		
		/*
		 * Test del builder specificando le categorie.
		 */
		List<Categoria> nuoveCategorie = new ArrayList<Categoria>();
		nuoveCategorie.add(cat2);
		nuoveCategorie.add(new Categoria());
		int sizeNuoveCategorie = nuoveCategorie.size();
		
		
		d = new Contatto.Builder("Famiglia").categoria(new Categoria("Lavoro", Color.BLACK))
				.categorie(nuoveCategorie)
				.categoria(new Categoria("Sport", Color.GREEN))
				.build();
		
		Assert.assertTrue(nuoveCategorie.size() == sizeNuoveCategorie);
		Assert.assertTrue(d.getCategorie().size() == 3);
		
		// aggiungo una categoria gia' presente nel contatto, la categoria non viene aggiunta
		d.getGestoreCategorie().aggiungiCategoria(new Categoria());
		Assert.assertTrue(d.getCategorie().size() == 3);
		
		d.getGestoreCategorie().aggiungiCategoria(new Categoria("Studio", new Priorita((byte) 8)));
		Assert.assertTrue(d.getCategorie().size() == 4);
		
		d.getGestoreCategorie().rimuoviCategoria(new Categoria("Studio", new Priorita((byte) 8)));
		Assert.assertTrue(d.getCategorie().size() == 3);
		
		d.getGestoreCategorie().rimuoviCategoria(new Categoria("Lavoro", Color.BLACK));
		d.getGestoreCategorie().rimuoviCategoria(copia_cat2);
		d.getGestoreCategorie().rimuoviCategoria(new Categoria());
		
		Assert.assertTrue(d.getCategorie().size() == 1);
		
		
	}
	
	/*
	 * Verifica che vi sia sempre una categoria 
	 */
	
	/**
	 * Istanziazione di un contatto 
	 * con diverse categorie.
	 * Eccezione aspettata dovuta al fatto
	 * che ci deve essere sempre una categoria
	 * inserita nel contatto altrimenti non 
	 * risulta più valido.
	 * Se viene rimossa una categoria non presente
	 * non vi è alcun problema.
	 * @throws CategorieNonValideException
	 * @throws LimiteEtichettaSuperatoException
	 */
	
	@Test (expected = CategorieNonValideException.class)
	public void testRimozioneCategoria()
			throws
				CategorieNonValideException,
				LimiteEtichettaSuperatoException {
		
		Categoria cat2 = new Categoria("Famiglia", Color.BLUE);
		Categoria copia_cat2 = new Categoria("Famiglia", Color.BLUE);
		
		List<Categoria> nuoveCategorie = new ArrayList<Categoria>();
		nuoveCategorie.add(cat2);
		nuoveCategorie.add(new Categoria());
		
		Contatto d = new Contatto.Builder("Famiglia").categoria(new Categoria("Lavoro", Color.BLACK))
				.categorie(nuoveCategorie)
				.categoria(new Categoria("Sport", Color.GREEN))
				.build();
		
		d.getGestoreCategorie().aggiungiCategoria(new Categoria());
		Assert.assertTrue(d.getCategorie().size() == 3);
		
		d.getGestoreCategorie().rimuoviCategoria(new Categoria("Lavoro", Color.BLACK));
		d.getGestoreCategorie().rimuoviCategoria(copia_cat2);
		d.getGestoreCategorie().rimuoviCategoria(new Categoria());
		Assert.assertTrue(d.getCategorie().size() == 1);
		
		
		/*
		 * rimuovo ancora una categoria, il contatto non possiede
		 * piu' categorie, si genera una eccezione.
		 */
		
		// rimuovo una categoria non presente, nessun problema
		d.getGestoreCategorie().rimuoviCategoria(new Categoria());
		Assert.assertTrue(d.getCategorie().size() == 1);
		
		// rimuovo l'ultima categoria
		d.getGestoreCategorie().rimuoviCategoria(new Categoria("Sport", Color.GREEN));
			
	}
	
	// TODO: aggiungere caso di test per eccezioni sulle EMails non valide
	
	@Test
	public void testContattoEmail()
			throws
			LimiteEtichettaSuperatoException,
			EmailNonValidaException {
		
		/*
		 * Un piccolo test per ricordarmi come Java gestisce i reference
		 * per i tipi String.
		 */
		
		String nome = "Nome";
		
		Contatto c = new Contatto.Builder(nome).build();
		c.setNome("NomeModificato");
		
		Assert.assertFalse(c.getNome().equals(nome));
		
		c = new Contatto.Builder("Nome1")
				.cognome("Cognome1")
				.categoria(new Categoria("Lavoro", Color.BLUE))
				.email(new Email("nome1.cognome1@lavoro.it"))
				.build();
		
		Assert.assertTrue(c.getEmails().contains(new Email("nome1.cognome1@lavoro.it")));
		
		// aggiungo email uguale, la mail non viene aggiunta
		c.getGestoreEmails().aggiungiEmail(new Email("nome1.cognome1@lavoro.it"));
		Assert.assertTrue(c.getEmails().size() == 1);
		
		// aggiungo mail diversa
		c.getGestoreEmails().aggiungiEmail(new Email("nome1.cognome1@altroprovider.it"));
		Assert.assertTrue(c.getEmails().size() == 2);
		
		// rimuovo tutte le mail
		c.getGestoreEmails().rimuoviEmail(new Email("nome1.cognome1@altroprovider.it"));
		c.getGestoreEmails().rimuoviEmail(new Email("nome1.cognome1@lavoro.it"));
		Assert.assertTrue(c.getEmails().isEmpty());
		
		// rimuovo una mail non presente, ritorna null, ma non da errore
		Email response = c.getGestoreEmails().rimuoviEmail(new Email("mail.fasulla@nonerrore.nonono"));
		Assert.assertTrue(response == null);
		
		// TODO: aggiungere test sul builder con più email che vengono
		// sovrascritte da una successiva emails nel builder (vedi categoria test builder sopra)
		
	}
	
	/*
	 * Istanziazione contatto con più telefoni
	 */
	
	/**
	 * Istanziazione di un contatto con 
	 * all'interno più telefoni e verifica 
	 * corretta istanziazione di questo.
	 * @throws LimiteEtichettaSuperatoException
	 * @throws NumeroTelefonoNonValidoException
	 * @throws TelefoniNonValidiException
	 */
	
	@Test
	public void testContattoTelefoni()
			throws
			LimiteEtichettaSuperatoException,
			NumeroTelefonoNonValidoException,
			TelefoniNonValidiException {
		
		Contatto c = new Contatto.Builder("Nome1").cognome("Cognome1")
				.categoria(new Categoria("Tempo Libero", Color.YELLOW))
				.telefono(new Telefono("031123456"))
				.telefono(new Telefono("+391234567890"))
				.build();
		
		Assert.assertTrue(c.getTelefoni().contains(new Telefono("031123456")));
		Assert.assertTrue(c.getTelefoni().size() == 2);
		
		List<Telefono> telefoni = new ArrayList<Telefono>();
		telefoni.add(new Telefono("+39112"));
		
		/*
		 *  Sovrascrittura dei telefoni singoli con una nuova lista di telefoni.
		 *  Inserisco poi un telefono già presente nella lista precedentemente
		 *  inserita dal builder, quest'ultimo non viene aggiunto.
		 */
		c = new Contatto.Builder("Nome1").cognome("Cognome1")
				.categoria(new Categoria("Tempo Libero", Color.YELLOW))
				.telefono(new Telefono("031123456"))
				.telefono(new Telefono("+391234567890"))
				.telefoni(telefoni)
				.telefono(new Telefono("+39112"))
				.build();
		
		Assert.assertTrue(c.getTelefoni().size() == 1);
		
		c.getGestoreTelefoni().aggiungiTelefono(new Telefono("+39112"));
		Assert.assertTrue(c.getTelefoni().size() == 1);
		
		c.getGestoreTelefoni().rimuoviTelefono(new Telefono("+39112"));
		Assert.assertTrue(c.getTelefoni().isEmpty());
		
		telefoni = new ArrayList<Telefono>();
		Telefono t1 = new Telefono("+39333555467");
		Telefono t2 = new Telefono("+39333555468");
		Telefono t3 = new Telefono("333555467");
		
		telefoni.add(t1);
		telefoni.add(t2);
		telefoni.add(t3);
		
		c.setTelefoni(telefoni);
		Assert.assertTrue(c.getTelefoni().size() == 3);
			
	}
	
	/*
	 * Test verifica telefono non valido
	 */
	/**
	 * Istanziazione di un contatto e di vari telefoni
	 * e verifica se non vi sono duplicati all'interno
	 * (Aspettasi eccezione in quanto vi sono duplicati)
	 */
	
	@Test (expected = TelefoniNonValidiException.class)
	public void testTelefoniContattoNonValidi()
			throws
			LimiteEtichettaSuperatoException,
			NumeroTelefonoNonValidoException,
			TelefoniNonValidiException {
		
		Contatto c = new Contatto.Builder("Nome1").cognome("Cognome1")
				.categoria(new Categoria("Tempo Libero", Color.YELLOW))
				.telefono(new Telefono("031123456"))
				.telefono(new Telefono("+391234567890"))
				.telefono(new Telefono("+39112"))
				.build();
		
		Assert.assertTrue(c.getTelefoni().size() == 3);
		
		List<Telefono> telefoni = new ArrayList<Telefono>();
		Telefono t1 = new Telefono("+39333555467");
		Telefono t2 = new Telefono("+39333555468");
		Telefono t3 = new Telefono("+3933355546");
		
		telefoni.add(t1);
		telefoni.add(t2);
		telefoni.add(t3);
		
		// sostiuisco i telefoni del contatto con nuovi numeri.
		c.setTelefoni(telefoni);
		Assert.assertTrue(c.getTelefoni().size() == 3);
			
		telefoni = new ArrayList<Telefono>();
		t1 = new Telefono("+39333555467");
		// notare che t2 e' uguale a t1.
		t2 = new Telefono("+39333555467");
		t3 = new Telefono("333555467");
		
		telefoni.add(t1);
		telefoni.add(t2);
		telefoni.add(t3);
		
		// scatta l'eccezione, ci sono duplicati nella lista telefoni.
		c.setTelefoni(telefoni);
	}
	
	/*
	 * Verifica costruttore contatto completo
	 */
	
	/**
	 * Istanziazione di un contatto completo e verfica
	 * di corretta assegnazione degli attributi 
	 * (corretta istanziazione)
	 * @throws NumeroTelefonoNonValidoException
	 * @throws LimiteEtichettaSuperatoException
	 * @throws EmailNonValidaException
	 */
	@Test
	public void testContattoCompleto()
			throws NumeroTelefonoNonValidoException,
			LimiteEtichettaSuperatoException,
			EmailNonValidaException {
		
		Contatto c = new Contatto.Builder("Pinco").cognome("Pallo")
				.telefono(new Telefono("+390324412579"))
				.telefono(new Telefono("55544466792"))
				.telefono(new Telefono("55544466792"))
				.categoria(new Categoria())
				.categoria(new Categoria())
				.categoria(new Categoria("Lavoro", Color.GRAY))
				.email(new Email("pinco.pallo@campus.unimib.it"))
				.email(new Email("pinco.pallo@campus.unimib.it"))
				.build();
		
		//Sezione assert
		Assert.assertTrue(c.getTelefoni().size() == 2);
		Assert.assertTrue(c.getEmails().size() == 1);
		Assert.assertTrue(c.getCategorie().size() == 2);
		
	}
	
	/*
	 * Verifica uguaglianza contatti
	 */
	/**
	 * Istanziazione di due contatti complessi e verifica
	 * uguaglianza tra questi
	 * @throws NumeroTelefonoNonValidoException
	 * @throws LimiteEtichettaSuperatoException
	 * @throws EmailNonValidaException
	 */
	
	@Test
	public void testContattoEquals()
			throws
			NumeroTelefonoNonValidoException,
			LimiteEtichettaSuperatoException,
			EmailNonValidaException {
		
		Contatto c1 = new Contatto.Builder("Pinco").cognome("Pallo")
				.telefono(new Telefono("+390324412579"))
				.telefono(new Telefono("55544466792"))
				.telefono(new Telefono("55544466792"))
				.categoria(new Categoria())
				.categoria(new Categoria())
				.categoria(new Categoria("Lavoro", Color.GRAY))
				.email(new Email("pinco.pallo@campus.unimib.it"))
				.email(new Email("pinco.pallo@campus.unimib.it"))
				.build();
		
		Contatto c2 = new Contatto.Builder("Pinco").cognome("Pallo")
				.telefono(new Telefono("+390324412579"))
				.telefono(new Telefono("55544466792"))
				.telefono(new Telefono("55544466792"))
				.categoria(new Categoria())
				.categoria(new Categoria())
				.categoria(new Categoria("Lavoro", Color.GRAY))
				.email(new Email("pinco.pallo@campus.unimib.it"))
				.email(new Email("pinco.pallo@campus.unimib.it"))
				.build();
		
		Assert.assertTrue(c1.equals(c2));
		
		c2 = new Contatto.Builder("Pinco").cognome("Pallo")
				.telefono(new Telefono("+390324412578"))
				.telefono(new Telefono("55544466792"))
				.telefono(new Telefono("55544466792"))
				.categoria(new Categoria())
				.categoria(new Categoria())
				.categoria(new Categoria("Lavoro", Color.GRAY))
				.email(new Email("pinco.pallo@campus.unimib.it"))
				.email(new Email("pinco.pallo@campus.unimib.it"))
				.build();
		
		Assert.assertFalse(c1.equals(c2));
		
		c2 = new Contatto.Builder("Pinco").cognome("Pallo").build();
		c1 = new Contatto.Builder("Pinco").cognome("Pallo").build();
		Assert.assertTrue(c1.equals(c2));
		
		c2.getGestoreEmails().aggiungiEmail(new Email("pinco.pallo@campus.unimib.it"));
		Assert.assertFalse(c1.equals(c2));
		
	}
	
	/*
	 * Test costruttore rubrica con aggiunta di vari contatti
	 */
	/**
	 * Verifica la corretta istanziazione di un oggetto rubrica
	 * Istanziazione di una rubrica con all'interno 3 contatti con
	 * verifica correttezza.
	 * @throws EmailNonValidaException
	 */
	@Test
	public void testCostruttoreRubrica() throws EmailNonValidaException{
		
		//Sezione rubrica
		Rubrica rubrica = new Rubrica();
		Contatto contatto1 = new Contatto.Builder("Lorenzo").cognome("Di Vito").email(new Email("lorenzo.divito@hotmail.com")).build();
		Contatto contatto2 = new Contatto.Builder("Diego").cognome("Lobba").email(new Email("diego.lobba@hotmail.it")).build();
		Contatto contatto3 = new Contatto.Builder("Sofia").cognome("Baggi").email(new Email("sofia.baggi@hotmail.it")).build();
		rubrica.aggiungiContatto(contatto1);
		rubrica.aggiungiContatto(contatto2);
		rubrica.aggiungiContatto(contatto3);
		
		//Sezione assert
		Assert.assertTrue(rubrica.getContatti().contains(contatto1));
		Assert.assertTrue(rubrica.getContatti().contains(contatto2));
		Assert.assertTrue(rubrica.getContatti().contains(contatto3));
	}
	
	/*
	 * Test eliminazione contatto da rubrica
	 */
	
	/**
	 * Verifica la rimozione di un determinato contatto all'interno
	 * di rubrica
	 * Istanziazione di più contatti e rimozione di uno con successivo
	 * controllo di rimozione di questo.
	 * @throws EmailNonValidaException
	 */
	@Test
	public void testRimuoviContattoRubrica()throws EmailNonValidaException{
		
		//Sezione rubrica
		Rubrica rubrica = new Rubrica();
		Contatto contatto1 = new Contatto.Builder("Lorenzo").cognome("Di Vito").email(new Email("lorenzo.divito@hotmail.com")).build();
		Contatto contatto2 = new Contatto.Builder("Diego").cognome("Lobba").email(new Email("diego.lobba@hotmail.it")).build();
		Contatto contatto3 = new Contatto.Builder("Sofia").cognome("Baggi").email(new Email("sofia.baggi@hotmail.it")).build();
		rubrica.aggiungiContatto(contatto1);
		rubrica.aggiungiContatto(contatto2);
		rubrica.aggiungiContatto(contatto3);
		rubrica.rimuoviContatto(contatto2);
		
		//Sezione assert
		Assert.assertFalse(rubrica.getContatti().contains(contatto2));
	}
	
	/*
	 * Test ricerca contatto da rubrica
	 */
	/**
	 * Verifica la presenza di un determinato contatto all'interno di rubrica
	 * istanziazione di diversi contatti con ricerca di uno in specifico.
	 * @throws EmailNonValidaException
	 */
	@Test
	public void testRicercaContattoRubrica() throws EmailNonValidaException{
		
		//Sezione rubrica
		Rubrica rubrica = new Rubrica();
		Contatto contatto1 = new Contatto.Builder("Lorenzo").cognome("Di Vito").email(new Email("lorenzo.divito@hotmail.com")).build();
		Contatto contatto2 = new Contatto.Builder("Diego").cognome("Lobba").email(new Email("diego.lobba@hotmail.it")).build();
		Contatto contatto3 = new Contatto.Builder("Sofia").cognome("Baggi").email(new Email("sofia.baggi@hotmail.it")).build();
		rubrica.aggiungiContatto(contatto1);
		rubrica.aggiungiContatto(contatto2);
		rubrica.aggiungiContatto(contatto3);
		List<Contatto> contattiRicercati = new ArrayList<Contatto>();
		contattiRicercati = rubrica.ricercaNomeContatto("Lorenzo");
		
		//Sezione assert
		Assert.assertTrue(contattiRicercati.contains(contatto1));
	}
	
	/*
	 * Test inserimento contatti uguali
	 */
	/**
	 * Verifica se inserendo due contatti uguali ne viene aggiunto
	 * solo 1.
	 * Istanziazione di diversi contatti di cui 1 dupplicato e verifica
	 * di non aggiunta di questo
	 * @throws EmailNonValidaException
	 */
	@Test
	public void testVerificaContattoUguale() throws EmailNonValidaException{
		
		//Sezione rubrica
		Rubrica rubrica = new Rubrica();
		Contatto contatto1 = new Contatto.Builder("Lorenzo").cognome("Di Vito").email(new Email("lorenzo.divito@hotmail.com")).build();
		Contatto contattodup = new Contatto.Builder("Lorenzo").cognome("Di Vito").email(new Email("lorenzo.divito@hotmail.com")).build();
		Contatto contatto2 = new Contatto.Builder("Diego").cognome("Lobba").email(new Email("diego.lobba@hotmail.it")).build();
		Contatto contatto3 = new Contatto.Builder("Sofia").cognome("Baggi").email(new Email("sofia.baggi@hotmail.it")).build();
		rubrica.aggiungiContatto(contatto1);
		rubrica.aggiungiContatto(contattodup);
		rubrica.aggiungiContatto(contatto2);
		rubrica.aggiungiContatto(contatto3);
		
		//Sezione assert
		Assert.assertTrue(rubrica.getNumeroContatti()==3);
	}
	
}
