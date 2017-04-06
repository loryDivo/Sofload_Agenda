package com.soflod.agenda.persistence;

import java.awt.Color;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


import org.junit.Assert;
import org.junit.Test;

import com.j256.ormlite.logger.Logger;
import com.soflod.agenda.core.Attivita;
import com.soflod.agenda.core.Categoria;
import com.soflod.agenda.core.Contatto;
import com.soflod.agenda.core.Impegno;
import com.soflod.agenda.core.Priorita;
import com.soflod.agenda.core.Ripetizione;
import com.soflod.agenda.core.Telefono;
import com.soflod.agenda.core.exception.CategorieNonValideException;
import com.soflod.agenda.core.exception.DataFineNonValidaException;
import com.soflod.agenda.core.exception.DataInizioNonValidaException;
import com.soflod.agenda.core.exception.DataScadenzaNonValidaException;
import com.soflod.agenda.core.exception.DateNonValideException;
import com.soflod.agenda.core.exception.LimiteEtichettaSuperatoException;
import com.soflod.agenda.core.exception.LimiteTitoloSuperatoException;
import com.soflod.agenda.core.exception.NumeroTelefonoNonValidoException;
import com.soflod.agenda.core.exception.PrioritaNonValidaException;
import com.soflod.agenda.persistence.Agenda;
import com.soflod.agenda.persistence.AttivitaORM;
import com.soflod.agenda.persistence.CategoriaORM;
import com.soflod.agenda.persistence.ContattoCategoriaORM;
import com.soflod.agenda.persistence.ContattoORM;
import com.soflod.agenda.persistence.ImpegnoComparator;
import com.soflod.agenda.persistence.ImpegnoORM;
import com.soflod.agenda.persistence.exception.AggiuntaAttivitaException;
import com.soflod.agenda.persistence.exception.AggiuntaCategoriaException;
import com.soflod.agenda.persistence.exception.AggiuntaContattoException;
import com.soflod.agenda.persistence.exception.AggiuntaImpegnoException;
import com.soflod.agenda.persistence.exception.AttivitaOrmNonValidaException;
import com.soflod.agenda.persistence.exception.CategoriaOrmNonValidaException;
import com.soflod.agenda.persistence.exception.ConnessioneException;
import com.soflod.agenda.persistence.exception.ContattoOrmNonValidoException;
import com.soflod.agenda.persistence.exception.ImpegnoOrmNonValidoException;
import com.soflod.agenda.persistence.exception.InizializzazioneAgendaException;
import com.soflod.agenda.persistence.exception.InizializzazioneNonValidaException;
import com.soflod.agenda.persistence.exception.ModificaAttivitaException;
import com.soflod.agenda.persistence.exception.ModificaCategoriaException;
import com.soflod.agenda.persistence.exception.ModificaContattoException;
import com.soflod.agenda.persistence.exception.ModificaImpegnoException;
import com.soflod.agenda.persistence.exception.RimozioneAttivitaException;
import com.soflod.agenda.persistence.exception.RimozioneCategoriaException;
import com.soflod.agenda.persistence.exception.RimozioneContattoException;
import com.soflod.agenda.persistence.exception.RimozioneImpegnoException;

public class TestPersistence {

	private Logger logger;
	/*
	 * public static void main( String[] args ) throws SQLException,
	 * DataInizioNonValidaException, ParseException,
	 * LimiteTitoloSuperatoException, DataFineNonValidaException, IOException,
	 * LimiteEtichettaSuperatoException, PrioritaNonValidaException,
	 * LimiteNoteSuperatoException, DataScadenzaNonValidaException,
	 * NumeroTelefonoNonValidoException { // path database String databaseUrl =
	 * "jdbc:sqlite:test.db"; // create a connection source to our database
	 * ConnectionSource connectionSource = new
	 * JdbcConnectionSource(databaseUrl);
	 * 
	 * Memory mem = new Memory(connectionSource); mem.svuotaTabelle();
	 * 
	 * 
	 * Categoria c = new Categoria("Lavoro", Color.BLACK, new Priorita((byte)
	 * 5)); Categoria c1 = new Categoria("Altro", Color.BLUE, new Priorita());
	 * 
	 * SimpleDateFormat sdf = new
	 * SimpleDateFormat(Impegno.FORMATO_DATA_IMPEGNO); Date today = new Date();
	 * 
	 * Calendar calendar = Calendar.getInstance(); calendar.setTime(today);
	 * 
	 * calendar.add(Calendar.HOUR, 1); String hours1 =
	 * sdf.format(calendar.getTime()); Impegno i = new
	 * Impegno.Builder("Impegno", c, sdf.parse(hours1)) .dataInizio(today)
	 * .allarme(true) .ripetizione(Ripetizione.GIORNO) .build();
	 * 
	 * Impegno i1 = new Impegno.Builder("Impegno", new Categoria(),
	 * sdf.parse(hours1)) .dataInizio(today) .allarme(true)
	 * .ripetizione(Ripetizione.GIORNO) .note("Avada kedavra ààà") .build();
	 * 
	 * mem.inserisciCategoriaORM(new CategoriaORM(c)); CategoriaORM c1ORM = new
	 * CategoriaORM(c1); mem.inserisciCategoriaORM(c1ORM);
	 * mem.inserisciImpegnoORM(new ImpegnoORM(i, mem.getCategorieDAO()));
	 * 
	 * ImpegnoORM i1ORM = new ImpegnoORM(i1, mem.getCategorieDAO());
	 * mem.inserisciImpegnoORM(i1ORM);
	 * 
	 * Contatto contatto = new Contatto.Builder("Di Vito") .categoria(c)
	 * .telefono(new Telefono("456012932")) .telefono(new Telefono("4560132"))
	 * .categoria(new Categoria()) .categoria(c1) .build();
	 * 
	 * 
	 * ContattoORM cORM = new ContattoORM(contatto, mem.getCategorieDAO());
	 * mem.inserisciContattoORM(cORM); mem.cancellaImpegnoORM(i1ORM);
	 * 
	 * //System.out.println(cORM.restituisciContatto(mem.getContattiCategorieDAO
	 * (), mem.getCategorieDAO()));
	 * 
	 * 
	 * Attivita a = new Attivita.Builder("Attivita", today).build(); AttivitaORM
	 * aORM = new AttivitaORM(a, mem.getCategorieDAO());
	 * mem.inserisciAttivitaORM(aORM); mem.cancellaAttivitaORM(aORM);
	 * 
	 * boolean x = mem.cancellaCategoriaORM(c1ORM); System.out.print(x);
	 * 
	 * 
	 * 
	 * connectionSource.close();
	 * 
	 * }
	 */
	/**
	 * Test istanziazione singleton con eventuale eccezione doppio singleton
	 * 
	 * @throws InizializzazioneNonValidaException
	 */
	/*
	 * @Test(expected = InizializzazioneNonValidaException.class) public void
	 * testApplicazione() throws InizializzazioneNonValidaException{
	 * Applicazione singleton = Applicazione.getInstance();
	 * singleton.inizializza("jdbc:sqlite:test.db");
	 * singleton.inizializza("jdbc:sqlite:test.db");
	 * singleton.chiudiConnessione(); }
	 */

	/**
	 * Test Funzionamento aggiunta Impegno, Attivita, categoria, contatto
	 * 
	 * @throws ConnessioneException
	 * @throws InizializzazioneNonValidaException
	 * @throws DataFineNonValidaException
	 * @throws LimiteTitoloSuperatoException
	 * @throws ParseException
	 * @throws DataInizioNonValidaException
	 * @throws PrioritaNonValidaException
	 * @throws LimiteEtichettaSuperatoException
	 * @throws IOException
	 * @throws SQLException
	 * @throws ImpegnoOrmNonValidoException
	 * @throws NumeroTelefonoNonValidoException
	 * @throws DataScadenzaNonValidaException
	 * @throws CategoriaOrmNonValidaException
	 * @throws ContattoOrmNonValidoException
	 * @throws AttivitaOrmNonValidaException
	 * @throws CategorieNonValideException
	 */
	@Test
	public void testCostruzioneAgenda() throws ConnessioneException {

		// Instanziazione categoria
		Agenda agenda = null;
		try {
			agenda = new Agenda("test.db");
			agenda.getMemory().svuotaTabelle(agenda.getConnessione().getIstanzaConnessione());
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (InizializzazioneAgendaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {

			Categoria c = new Categoria("Lavoro", Color.BLACK, new Priorita((byte) 5));
			// Sezione istanziazione impegno
			SimpleDateFormat sdf = new SimpleDateFormat(Impegno.FORMATO_DATA_IMPEGNO);
			Date today = new Date();

			Calendar calendar = Calendar.getInstance();
			calendar.setTime(today);

			calendar.add(Calendar.HOUR, 1);
			String hours1 = sdf.format(calendar.getTime());
			Impegno i = new Impegno.Builder("Impegno", c).date(today, sdf.parse(hours1)).allarme(true)
					.ripetizione(Ripetizione.GIORNO).build();
			// Sezione istanziazione attivita
			Attivita a = new Attivita.Builder("Attivita", today).categoria(c).build();
			// Sezione istanziazione contatto
			List<Categoria> cat = new ArrayList<Categoria>();
			cat.add(c);
			Contatto contatto = new Contatto.Builder("Di Vito").categoria(c).telefono(new Telefono("456012932"))
					.telefono(new Telefono("4560132")).categoria(c).categorie(cat).build();

			agenda.aggiungiCategoria(c);
			agenda.aggiungiImpegno(i);
			agenda.aggiungiAttivita(a);
			agenda.aggiungiContatto(contatto);

			agenda.getMemory().svuotaTabelle(agenda.getConnessione().getIstanzaConnessione());

		} catch (CategorieNonValideException cnv) {
			logger.error(cnv.getMessage());
		} catch (LimiteEtichettaSuperatoException etSup) {
			logger.error(etSup.getMessage());
		} catch (PrioritaNonValidaException pnv) {
			logger.error(pnv.getMessage());
		} catch (ParseException pe) {
			logger.error(pe.getMessage());
		} catch (LimiteTitoloSuperatoException lts) {
			logger.error(lts.getMessage());
		} catch (DataScadenzaNonValidaException dsnv) {
			logger.error(dsnv.getMessage());
		} catch (NumeroTelefonoNonValidoException tnv) {
			logger.error(tnv.getMessage());
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		} catch (DateNonValideException e) {
			e.printStackTrace();
		} catch (AggiuntaImpegnoException e) {
			e.printStackTrace();
		} catch (AggiuntaAttivitaException e) {
			e.printStackTrace();
		} catch (AggiuntaCategoriaException e) {
			e.printStackTrace();
		} catch (AggiuntaContattoException e) {
			e.printStackTrace();
		} finally {
			agenda.getConnessione().chiudiConnessione();
		}

	}

	/**
	 * Test di verifica costruttore con inserimento di due impegni uguali
	 * 
	 * @throws AggiuntaImpegnoException
	 * @throws ConnessioneException
	 */
	@Test(expected = AggiuntaImpegnoException.class)
	public void testCostruttoreAgendaErrore() throws AggiuntaImpegnoException, ConnessioneException {

		Agenda agenda = null;
		try {
			agenda = new Agenda("test.db");
			agenda.getMemory().svuotaTabelle(agenda.getConnessione().getIstanzaConnessione());
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (InizializzazioneAgendaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {

			// Instanziazione categoria
			Categoria c = new Categoria("Lavoro", Color.BLACK, new Priorita((byte) 5));

			// Sezione istanziazione impegno
			SimpleDateFormat sdf = new SimpleDateFormat(Impegno.FORMATO_DATA_IMPEGNO);
			Date today = new Date();

			Calendar calendar = Calendar.getInstance();
			calendar.setTime(today);

			calendar.add(Calendar.HOUR, 1);
			String hours1 = sdf.format(calendar.getTime());
			Impegno i = new Impegno.Builder("Impegno", c).date(today, sdf.parse(hours1)).allarme(true)
					.ripetizione(Ripetizione.GIORNO).build();

			Impegno i1 = new Impegno.Builder("Impegno", c).date(today, sdf.parse(hours1)).allarme(true)
					.ripetizione(Ripetizione.GIORNO).build();

			agenda.aggiungiCategoria(c);
			agenda.aggiungiImpegno(i);
			agenda.aggiungiImpegno(i1);
			agenda.getConnessione().chiudiConnessione();

			agenda.getMemory().svuotaTabelle(agenda.getConnessione().getIstanzaConnessione());

		} catch (LimiteEtichettaSuperatoException etSup) {
			logger.error(etSup.getMessage());
		} catch (PrioritaNonValidaException pnv) {
			logger.error(pnv.getMessage());
		} catch (ParseException pe) {
			logger.error(pe.getMessage());
		} catch (LimiteTitoloSuperatoException lts) {
			logger.error(lts.getMessage());
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		} catch (DateNonValideException e) {
			e.printStackTrace();
		} catch (AggiuntaCategoriaException e) {
			e.printStackTrace();
		} finally {
			agenda.getConnessione().chiudiConnessione();
		}
	}

	@Test
	public void testGestionePersistenza() throws ContattoOrmNonValidoException, ConnessioneException {

		Agenda agenda = null;
		try {
			agenda = new Agenda("test.db");
			agenda.getMemory().svuotaTabelle(agenda.getConnessione().getIstanzaConnessione());
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (InizializzazioneAgendaException e) {
			e.printStackTrace();
		}

		try {

			Categoria cat1 = new Categoria();
			Categoria cat2 = new Categoria("Lavoro", Color.CYAN);

			List<Categoria> listaCat = new ArrayList<Categoria>();
			listaCat.add(cat1);

			Contatto con1 = new Contatto.Builder("Diego").cognome("MioCognome").telefono(new Telefono("032994422"))
					.categorie(listaCat).build();

			Contatto con2 = new Contatto.Builder("Lorenzo").cognome("MioCognome").telefono(new Telefono("9992221182"))
					.categorie(listaCat).build();

			listaCat.clear();
			listaCat.add(cat2);
			Contatto con3 = new Contatto.Builder("Sofia").cognome("MioCognome").telefono(new Telefono("118"))
					.categorie(listaCat).build();

			Calendar calendario = Calendar.getInstance();
			calendario.add(Calendar.HOUR, 1);
			calendario.add(Calendar.DATE, 7);

			Date dataInizio = calendario.getTime();
			calendario.add(Calendar.HOUR, 23);
			Date dataFine = calendario.getTime();

			Impegno impegno1 = new Impegno.Builder("Finire il progetto", cat2).date(dataInizio, dataFine).build();

			calendario = Calendar.getInstance();
			calendario.add(Calendar.HOUR, 3);
			Impegno impegno2 = new Impegno.Builder("F", cat2).date(new Date(), calendario.getTime()).build();

			calendario.add(Calendar.DATE, 80);
			Attivita attivita1 = new Attivita.Builder("Correre", calendario.getTime()).build();

			calendario = Calendar.getInstance();
			calendario.add(Calendar.DATE, -80);

			Attivita attivita2 = new Attivita.Builder("Pescare", new Date()).build();
			attivita2.setDataInizio(calendario.getTime());
			calendario.add(Calendar.DATE, 80);
			attivita2.setDataScadenza(calendario.getTime());

			// AGGIUNTA DELLE CATEGORIE

			agenda.aggiungiCategoria(cat1);
			agenda.aggiungiCategoria(cat2);

			List<Categoria> categorie = agenda.getGestoreCategorieOrm().getCategorie();

			Assert.assertTrue(categorie.contains(cat1));
			Assert.assertTrue(categorie.contains(cat2));
			agenda.getGestoreCategorieOrm().aggiornaCategorie();
			Assert.assertTrue(agenda.getGestoreCategorieOrm().getCategorie().size() == 2);

			agenda.aggiungiAttivita(attivita1);
			agenda.aggiungiAttivita(attivita2);

			agenda.aggiungiContatto(con1);
			agenda.aggiungiContatto(con2);
			agenda.aggiungiContatto(con3);

			ContattoORM conORM1 = agenda.getMemory().cercaContatto(con1);

			/*
			 * verifico che il contatto del db sia uguale a quello inserito in
			 * origine.
			 */
			Assert.assertTrue(conORM1.restituisciContatto(agenda.getMemory().getContattiCategorieDAO(),
					agenda.getMemory().getCategorieDAO()).equals(con1));

			agenda.aggiungiImpegno(impegno1);
			agenda.aggiungiImpegno(impegno2);

			// numero impegni
			Assert.assertTrue(agenda.getMemory().getImpegniDAO().queryForAll().size() == 2);

			// numero categorie
			Assert.assertTrue(agenda.getMemory().getCategorieDAO().queryForAll().size() == 2);

			// numero contatti
			Assert.assertTrue(agenda.getMemory().getContattiDAO().queryForAll().size() == 3);

			// numero attivita
			Assert.assertTrue(agenda.getMemory().getAttivitaDAO().queryForAll().size() == 2);

			/*
			 * non svuto le tabelle, voglio verificare che ricreando la agenda
			 * (svuotando quindi le arraylist locali) tutto funzioni comunque
			 */
			// agenda.getMemory().svuotaTabelle();
		} catch (LimiteEtichettaSuperatoException e) {
			e.printStackTrace();
		} catch (CategorieNonValideException e) {
			e.printStackTrace();
		} catch (NumeroTelefonoNonValidoException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (LimiteTitoloSuperatoException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (DateNonValideException e) {
			e.printStackTrace();
		} catch (DataScadenzaNonValidaException e) {
			e.printStackTrace();
		} catch (DataInizioNonValidaException e) {
			e.printStackTrace();
		} catch (AggiuntaImpegnoException e) {
			e.printStackTrace();
		} catch (AggiuntaAttivitaException e) {
			e.printStackTrace();
		} catch (AggiuntaCategoriaException e) {
			e.printStackTrace();
		} catch (AggiuntaContattoException e) {
			e.printStackTrace();
		} catch (CategoriaOrmNonValidaException e) {
			e.printStackTrace();
		} finally {
			agenda.getConnessione().chiudiConnessione();
			agenda = null;
		}

		agenda = null;
		try {
			agenda = new Agenda("test.db");
			// agenda.getMemory().svuotaTabelle(agenda.getConnessione().getIstanzaConnessione());
		} catch (InizializzazioneAgendaException e) {
			e.printStackTrace();
		}
		try {
			Categoria cat1 = new Categoria();
			Categoria cat2 = new Categoria("Sport", Color.CYAN);

			Categoria cat3 = new Categoria("Relazioni", Color.DARK_GRAY);

			Categoria cat4 = new Categoria("Test rimozione: eccezione da contatto", Color.DARK_GRAY);

			Categoria cat5 = new Categoria("Test rimozione: dovrebbe eliminarlo", Color.DARK_GRAY);

			List<Categoria> listaCat = new ArrayList<Categoria>();
			listaCat.add(cat1);

			List<Categoria> listaCat2 = new ArrayList<Categoria>();
			listaCat2.add(cat2);
			listaCat2.add(cat1);

			Contatto con1 = new Contatto.Builder("Diego").cognome("MioCognome").telefono(new Telefono("032994422"))
					.categorie(listaCat).build();

			Contatto con2 = new Contatto.Builder("Lorenzo 2").cognome("MioCognome").telefono(new Telefono("9992221182"))
					.categorie(listaCat).build();

			Contatto con2DiverseCategorie = new Contatto.Builder("Lorenzo 2").cognome("MioCognome")
					.telefono(new Telefono("9992221182")).categorie(listaCat2).build();

			listaCat.clear();
			listaCat.add(cat2);

			Contatto con3 = new Contatto.Builder("Sofia 2").cognome("MioCognome").telefono(new Telefono("118"))
					.categorie(listaCat).build();

			listaCat.clear();
			/*
			 * aggiungo cat4, il contatto avra' questa categoria associata.
			 * inoltre tale categoria non ha associati nessun altro impegno o
			 * attivita.
			 */
			listaCat.add(cat4);
			Contatto con4 = new Contatto.Builder("Sofia 3").cognome("MioCognome").telefono(new Telefono("118"))
					.categorie(listaCat).build();

			Calendar calendario = Calendar.getInstance();
			calendario.add(Calendar.HOUR, 1);
			calendario.add(Calendar.DATE, 7);

			Date dataInizio = calendario.getTime();
			calendario.add(Calendar.HOUR, 23);
			Date dataFine = calendario.getTime();

			Impegno impegno1 = new Impegno.Builder("Finire il progetto", cat2).date(dataInizio, dataFine).build();

			calendario = Calendar.getInstance();
			calendario.add(Calendar.HOUR, 3);
			Impegno impegno2 = new Impegno.Builder("F", cat2).date(new Date(), calendario.getTime()).build();

			// L'impegno ha con se una categoria che non verra' inserita.
			Impegno impegno3 = new Impegno.Builder("F2", cat3).date(new Date(), calendario.getTime()).build();

			Impegno impegno4 = new Impegno.Builder("Non verrò inserito nel DB", cat2)
					.date(new Date(), calendario.getTime()).build();

			calendario.add(Calendar.DATE, 80);
			Attivita attivita1 = new Attivita.Builder("Correre", calendario.getTime()).build();

			calendario = Calendar.getInstance();
			calendario.add(Calendar.DATE, -80);

			Attivita attivita2 = new Attivita.Builder("Pescare di nuovo", new Date()).build();
			attivita2.setDataInizio(calendario.getTime());
			calendario.add(Calendar.DATE, 80);
			attivita2.setDataScadenza(calendario.getTime());

			// Da eccezione visto che la categoria e' gia' presente
			// agenda.aggiungiCategoria(cat1);
			agenda.aggiungiCategoria(cat2);

			agenda.aggiungiCategoria(cat4);
			agenda.aggiungiCategoria(cat5);

			// Da eccezione visto che il contatto e' gia' presente
			// agenda.aggiungiContatto(con1);
			agenda.aggiungiContatto(con2);
			agenda.aggiungiContatto(con3);

			/*
			 * Visto che questo contatto ha come unica categoria la cat4, quando
			 * cerchero' di eliminare la categoria cat4 otterro' una eccezione.
			 */
			agenda.aggiungiContatto(con4);

			Assert.assertTrue(agenda.getMemory().getContattiDAO().queryForAll().size() == 6);

			/*
			 * Da eccezione visto che il contatto e' gia' presente nel db. Anche
			 * se le categorie sono diverse, vengono considerati i dati
			 * principali per testare l'uguaglianza (nome, congome, email,
			 * telefoni).
			 * 
			 * Si puo' pensare di testare l'uguaglianza su nome e cognome, ma
			 * avremmo problemi con casi di omonimia.
			 * 
			 * In tal caso invece si hanno problemi con l'omonimia solo nel caso
			 * non vi siano ne telefoni, ne email inserite.
			 */
			// agenda.aggiungiContatto(con2DiverseCategorie);

			agenda.aggiungiImpegno(impegno1);

			// Da eccezione visto che l'impegno è già presente
			// agenda.aggiungiImpegno(impegno1);

			agenda.aggiungiImpegno(impegno2);

			// ricordare che l'impegno3 non è stato inserito

			Assert.assertTrue(agenda.getMemory().getImpegniDAO().queryForAll().size() == 4);

			// cerco un impegno che non esiste.
			ImpegnoORM impegnoORM4 = agenda.getMemory().cercaImpegno(impegno4);

			// l'impegno non viene trovato nel db.

			Assert.assertTrue(impegnoORM4 == null);

			// da eccezione visto che la categoria associata all'impegno
			// non è presente.
			// agenda.aggiungiImpegno(impegno3);

			// Da eccezione visto che l'attivita e' gia' presente
			// agenda.aggiungiAttivita(attivita1);
			agenda.aggiungiAttivita(attivita2);

			CategoriaORM catORM1 = agenda.getMemory().cercaCategoria(cat1);

			// Da eccezione visto che ci sono impegni e attivita associati.
			// agenda.rimuoviCategoria(catORM1);

			CategoriaORM catORM4 = agenda.getMemory().cercaCategoria(cat4);
			CategoriaORM catORM5 = agenda.getMemory().cercaCategoria(cat5);

			// Da eccezione visto che cat4 è l'unica categoria di con4.
			// agenda.rimuoviCategoria(catORM4);

			/*
			 * La categoria viene correttamente eliminata dal DB visto che non
			 * sono presenti ne impegni, ne attivita associati alla categoria ed
			 * inoltre non vi sono contatti che rimarrebbero senza categoria
			 * (cioe' che hanno cat5 come unica categoria).
			 */
			agenda.rimuoviCategoria(catORM5);

			ImpegnoORM impegnoORM1 = agenda.getMemory().cercaImpegno(impegno1);
			agenda.rimuoviImpegno(impegnoORM1);

			// Verifico che siano rimasti 3 impegni.
			Assert.assertTrue(agenda.getMemory().getImpegniDAO().queryForAll().size() == 3);

			AttivitaORM attivitaORM2 = agenda.getMemory().cercaAttivita(attivita2);

			Assert.assertTrue(attivitaORM2 != null);
			agenda.rimuoviAttivita(attivitaORM2);

			Assert.assertTrue(agenda.getMemory().getAttivitaDAO().queryForAll().size() == 2);

			ContattoORM con2ORM = agenda.getMemory().cercaContatto(con2);

			Assert.assertTrue(con2ORM != null);
			agenda.rimuoviContatto(con2ORM);

			// il contatto viene eliminato correttamente.
			Assert.assertTrue(agenda.getMemory().getContattiDAO().queryForAll().size() == 5);

			/*
			 * verifico che tutte le associazioni relative il contatto nella
			 * tabella categoria contatti sono state eliminate.
			 * 
			 * Cioe' eseguendo una query sul dao che gestisce la tabella non
			 * trovo record associati al contatto (idContatto =
			 * idContattoEliminato)
			 */
			Assert.assertTrue(agenda.getMemory().getContattiCategorieDAO()
					.queryForEq(ContattoCategoriaORM.ID_CONTATTO, con2ORM.getIdContatto()).size() == 0);

			/*
			 * Verifica funzionamento vari update del db
			 */

			/*
			 * Verifica funzionamento update impegno
			 */
			// creazione e aggiunta categoria (ci baseremo su questa per update
			// 4 oggetti
			Categoria cat6 = new Categoria("Test update: dovrebbe fare update", Color.CYAN);
			agenda.aggiungiCategoria(cat6);

			/*
			 * Aggiorno "impegno2" ricercandolo, da eccezione visto che impegno
			 * 3 non è stato inserito, un suo inserimento avrebbe lanciato un
			 * eccezione. Impegno3 ha associata una categoria che non viene
			 * inserita.
			 */
			ImpegnoORM impegnoDaAggiornare = agenda.getMemory().cercaImpegno(impegno2);

			// Istanziazione impegno con categoria diversa
			Impegno impegnoUpdate = new Impegno.Builder("Impegno di update", cat6).date(dataInizio, dataFine).build();
			ImpegnoORM iUpdate = new ImpegnoORM(impegnoUpdate, agenda.getMemory().getCategorieDAO());

			// Update impegno
			agenda.aggiornaImpegno(impegnoDaAggiornare, iUpdate);

			// Assert impegno modificato
			Assert.assertTrue(agenda.getMemory().cercaImpegno(impegnoUpdate).getTitolo().equals("Impegno di update"));
			Assert.assertTrue(agenda.getMemory().cercaImpegno(impegno2) == null);

			// Assert categoria aggiunta
			Assert.assertTrue(
					agenda.getMemory().cercaCategoria(cat6).getEtichetta().equals("Test update: dovrebbe fare update"));

			// Verifica funzionamento update attivita

			// Aggiorno "attivita1" ricercandola, attivita2 è stata eliminata
			AttivitaORM attivitaDaAggiornare = agenda.getMemory().cercaAttivita(attivita1);
			// Istanziazione attivita di modifica con categoria diversa
			Attivita attivitaUpdate = new Attivita.Builder("Attivita update", new Date()).build();
			AttivitaORM aUpdate = new AttivitaORM(attivitaUpdate, agenda.getMemory().getCategorieDAO());

			// Update attivita
			agenda.aggiornaAttivita(attivitaDaAggiornare, aUpdate);

			// Assert attivita modifica
			Assert.assertTrue(agenda.getMemory().cercaAttivita(attivitaUpdate).getTitolo().equals("Attivita update"));
			Assert.assertTrue(agenda.getMemory().cercaAttivita(attivita1) == null);
			// Verifica funzionamente update categoria

			// Aggiorno "categoria2" ricercandola
			CategoriaORM categoriaDaAggiornare = agenda.getMemory().cercaCategoria(cat2);

			// Istanziazione categoria di modifica
			Categoria categoriaUpdate = new Categoria("Categoria update", Color.GREEN);
			CategoriaORM cUpdate = new CategoriaORM(categoriaUpdate, agenda.getMemory().getCategorieDAO());

			// Update categoria
			agenda.aggiornaCategoria(categoriaDaAggiornare, cUpdate);

			// Assert di verifica
			Assert.assertTrue(
					agenda.getMemory().cercaCategoria(categoriaUpdate).getEtichetta().equals("Categoria update"));
			Assert.assertFalse(agenda.getMemory().restituisciCategorie().contains(categoriaDaAggiornare));
			Assert.assertTrue(agenda.getMemory().restituisciCategorie().contains(categoriaUpdate));

			// Verifica funzionamento update contatto

			// Aggiorno "con3" con nome diverso"
			ContattoORM contattoDaAggiornare = agenda.getMemory().cercaContatto(con1);

			// Istanziazione contatto di modifica
			Contatto contattoUpdate = new Contatto.Builder("Sofia").cognome("TestingModifica")
					.telefono(new Telefono("9992221182")).categorie(listaCat).build();
			ContattoORM contUpdate = new ContattoORM(contattoUpdate, agenda.getMemory().getCategorieDAO());

			// Update contatto
			agenda.aggiornaContatto(contattoDaAggiornare, contUpdate);

			// Assert di verifica
			// Verifica nome
			Assert.assertTrue(agenda.getMemory().cercaContatto(contattoUpdate).getNome().equals("Sofia"));
			;
			Assert.assertFalse(agenda.getMemory().restituisciContattiORM().contains(contattoDaAggiornare));
			Assert.assertTrue(agenda.getMemory().cercaContatto(con1) == null);
		} catch (LimiteEtichettaSuperatoException e) {
			e.printStackTrace();
		} catch (CategorieNonValideException e) {
			e.printStackTrace();
		} catch (NumeroTelefonoNonValidoException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (LimiteTitoloSuperatoException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (DateNonValideException e) {
			e.printStackTrace();
		} catch (DataScadenzaNonValidaException e) {
			e.printStackTrace();
		} catch (DataInizioNonValidaException e) {
			e.printStackTrace();
		} catch (AggiuntaImpegnoException e) {
			e.printStackTrace();
		} catch (RimozioneImpegnoException e) {
			e.printStackTrace();
		} catch (ModificaContattoException e) {
			e.printStackTrace();
		} catch (ModificaCategoriaException e) {
			e.printStackTrace();
		} catch (AggiuntaAttivitaException e) {
			e.printStackTrace();
		} catch (RimozioneAttivitaException e) {
			e.printStackTrace();
		} catch (AggiuntaCategoriaException e) {
			e.printStackTrace();
		} catch (RimozioneCategoriaException e) {
			e.printStackTrace();
		} catch (AggiuntaContattoException e) {
			e.printStackTrace();
		} catch (RimozioneContattoException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ModificaImpegnoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ModificaAttivitaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			agenda.getConnessione().chiudiConnessione();
		}
	}

	/**
	 * Test di verifica corretto inserimento in arraylist di impegni e attività
	 */

	@Test
	public void testInserimentoLista() {
		Agenda agenda = null;
		try {
			agenda = new Agenda("test.db");
			agenda.getMemory().svuotaTabelle(agenda.getConnessione().getIstanzaConnessione());
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (InizializzazioneAgendaException e) {
			e.printStackTrace();
		}
		try {
			Categoria c = new Categoria("Lavoro", Color.BLACK, new Priorita((byte) 5));
			// Aggiunta categoria
			agenda.aggiungiCategoria(c);
			// Sezione istanziazione impegno
			SimpleDateFormat sdf = new SimpleDateFormat(Impegno.FORMATO_DATA_IMPEGNO);
			Date today = new Date();

			Calendar calendar = Calendar.getInstance();
			calendar.setTime(today);

			calendar.add(Calendar.HOUR, 1);
			String hours1 = sdf.format(calendar.getTime());
			Impegno i = new Impegno.Builder("Impegno", c).date(today, sdf.parse(hours1)).allarme(true)
					.ripetizione(Ripetizione.GIORNO).build();
			// Sezione istanziazione attivita
			Attivita a = new Attivita.Builder("Attivita", today).categoria(c).build();

			// Aggiunta ad agenda
			agenda.aggiungiImpegno(i);
			agenda.aggiungiAttivita(a);
			// Ci deve essere un impegno e un attivita
			Assert.assertTrue(agenda.getGestoreAttivitaOrm().getAttivitaOrm().size() == 1);
			Assert.assertTrue(agenda.getGestoreImpegniOrm().getImpegniORM().size() == 1);

			// Sposto calendario
			calendar.add(Calendar.MONTH, 3);
			Date threeMonthStart = sdf.parse(sdf.format(calendar.getTime()));
			calendar.add(Calendar.HOUR, 1);
			Date threeMonthOneHourEnd = sdf.parse(sdf.format(calendar.getTime()));

			Impegno i1 = new Impegno.Builder("Non inserito", c).date(threeMonthStart, threeMonthOneHourEnd).build();

			// Aggiunta ad agenda ma non deve essere presente
			agenda.aggiungiImpegno(i1);

			// Verifica 1 impegno
			Assert.assertTrue(agenda.getGestoreImpegniOrm().getImpegniORM().size() == 1);

			// Rimozione di un impegno
			ImpegnoORM impegnoRimuovere = agenda.getMemory().cercaImpegno(i);
			agenda.rimuoviImpegno(impegnoRimuovere);

			// Verifica 0 impegni in arrayList
			Assert.assertTrue(agenda.getGestoreImpegniOrm().getImpegniORM().size() == 0);

			// Aggiunta impegno precedente
			agenda.aggiungiImpegno(i);
			Assert.assertTrue(agenda.getGestoreImpegniOrm().getImpegniORM().size() == 1);

			// Rimozione impegno non inserito in arraylist
			ImpegnoORM impegno1Rimuovere = agenda.getMemory().cercaImpegno(i1);
			agenda.rimuoviImpegno(impegno1Rimuovere);
			// Controllo se vi è l'impegno i nell'array
			ImpegnoORM impegnoNonRimosso = agenda.getMemory().cercaImpegno(i);
			Assert.assertTrue(agenda.getGestoreImpegniOrm().getImpegniORM().contains(impegnoNonRimosso));
			Assert.assertTrue(agenda.getGestoreImpegniOrm().getImpegniORM().size() == 1);

			// Rimozione attivita a
			AttivitaORM attivitaDaRimuovere = agenda.getMemory().cercaAttivita(a);
			agenda.rimuoviAttivita(attivitaDaRimuovere);
			// Assert controllo arraylist 0
			Assert.assertTrue(agenda.getGestoreAttivitaOrm().getAttivitaOrm().size() == 0);
			// Instanza attivita con data non nel range

			// Sposto calendario
			calendar.add(Calendar.MONTH, 1);
			Date MonthStart1 = sdf.parse(sdf.format(calendar.getTime()));
			calendar.add(Calendar.MONTH, 2);
			Date threeMonthEnd = sdf.parse(sdf.format(calendar.getTime()));
			Attivita attivitaNonRange = new Attivita.Builder("Attivita non inserita", threeMonthEnd)
					.dataInizio(MonthStart1).categoria(c).build();

			// Aggiunta ad agenda
			agenda.aggiungiAttivita(attivitaNonRange);
			// Verifica non inserimento in arraylist
			Assert.assertTrue(agenda.getGestoreAttivitaOrm().getAttivitaOrm().size() == 0);

			// aggiunta contatti con una categoria e aggiunta ad agenda
			Categoria cat1 = new Categoria();
			Categoria cat2 = new Categoria("Lavoro", Color.CYAN);

			List<Categoria> listaCat = new ArrayList<Categoria>();
			listaCat.add(cat1);
			agenda.aggiungiCategoria(cat1);
			agenda.aggiungiCategoria(cat2);

			Contatto con1 = new Contatto.Builder("Diego").cognome("MioCognome").telefono(new Telefono("032994422"))
					.categorie(listaCat).build();

			Contatto con2 = new Contatto.Builder("Lorenzo 2").cognome("MioCognome").telefono(new Telefono("9992221182"))
					.categorie(listaCat).build();

			agenda.aggiungiContatto(con1);
			agenda.aggiungiContatto(con2);

			// Assert
			Assert.assertTrue(agenda.getRubrica().getContattiORM().size() == 2);
			// Rimozione contatto
			ContattoORM contattoDaRimuovere = agenda.getMemory().cercaContatto(con1);
			agenda.rimuoviContatto(contattoDaRimuovere);
			Assert.assertTrue(agenda.getRubrica().getContattiORM().size() == 1);

			// Rimozione ultimo contratto
			ContattoORM ultimoContatto = agenda.getMemory().cercaContatto(con2);
			agenda.rimuoviContatto(ultimoContatto);
			Assert.assertTrue(agenda.getRubrica().getContattiORM().size() == 0);

			// Rimozione categoria
			CategoriaORM categoriaDaRimuovere = agenda.getMemory().cercaCategoria(cat1);
			agenda.rimuoviCategoria(categoriaDaRimuovere);
			// Test
			Assert.assertFalse(agenda.getRubrica().getContattiORM().contains(categoriaDaRimuovere));

			// Modifica categoria
			CategoriaORM categoriaDaModificare = agenda.getMemory().cercaCategoria(cat2);
			Categoria modifica = new Categoria();
			CategoriaORM categoriaDiModifica = new CategoriaORM(modifica);
			agenda.aggiornaCategoria(categoriaDaModificare, categoriaDiModifica);

			// Assert corretta modifica
			Assert.assertTrue(agenda.getMemory().cercaCategoria(modifica) != null);
			Assert.assertFalse(agenda.getMemory().cercaCategoria(cat2) != null);
			Assert.assertFalse(agenda.getRubrica().getContattiORM().contains(categoriaDiModifica));
			// Modifica impegno
			Impegno iInserimento = new Impegno.Builder("Impegno", c).date(today, sdf.parse(hours1)).allarme(true)
					.ripetizione(Ripetizione.MESE).build();
			// aggiun impegno da modificare
			agenda.aggiungiImpegno(iInserimento);
			// Modifica impegno
			Impegno i2 = new Impegno.Builder("secondo impegno", c).date(threeMonthStart, threeMonthOneHourEnd).build();

			ImpegnoORM impegnoDiModifica = agenda.getMemory().cercaImpegno(iInserimento);

			ImpegnoORM impegnoModificato = new ImpegnoORM(i2, agenda.getMemory().getCategorieDAO());
			agenda.aggiornaImpegno(impegnoDiModifica, impegnoModificato);

			// Assert di verifica
			Assert.assertTrue(agenda.getMemory().cercaImpegno(iInserimento) == null);
			Assert.assertTrue(agenda.getMemory().cercaImpegno(i2) != null);
			// I2 non contenuto perchè fuori dal mese
			Assert.assertFalse(agenda.getGestoreImpegniOrm().getImpegniORM().contains(i2));
			Assert.assertFalse(agenda.getGestoreImpegniOrm().getImpegniORM().contains(iInserimento));
		} catch (LimiteEtichettaSuperatoException l) {

		} catch (DateNonValideException e) {
			e.printStackTrace();
		} catch (LimiteTitoloSuperatoException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (DataScadenzaNonValidaException e) {
			e.printStackTrace();
		} catch (PrioritaNonValidaException e) {
			e.printStackTrace();
		} catch (AggiuntaImpegnoException e) {
			e.printStackTrace();
		} catch (AggiuntaAttivitaException e) {
			e.printStackTrace();
		} catch (AggiuntaCategoriaException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (RimozioneImpegnoException e) {
			e.printStackTrace();
		} catch (RimozioneAttivitaException e) {
			e.printStackTrace();
		} catch (DataInizioNonValidaException e) {
			e.printStackTrace();
		} catch (CategorieNonValideException e) {
			e.printStackTrace();
		} catch (NumeroTelefonoNonValidoException e) {
			e.printStackTrace();
		} catch (AggiuntaContattoException e) {
			e.printStackTrace();
		} catch (RimozioneContattoException e) {
			e.printStackTrace();
		} catch (RimozioneCategoriaException e) {
			e.printStackTrace();
		} catch (ModificaCategoriaException e) {
			e.printStackTrace();
		} catch (ModificaImpegnoException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				agenda.getConnessione().chiudiConnessione();
			} catch (ConnessioneException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Test
	public void testSort()
			throws
			LimiteTitoloSuperatoException,
			DateNonValideException,
			LimiteEtichettaSuperatoException {
		
		Calendar calendar = Calendar.getInstance();
		
		List<Impegno> impegni = new ArrayList<Impegno>();
		
		Categoria cat1 = new Categoria();
		Categoria cat2 = new Categoria("Lavoro", Color.BLUE);
		
		Impegno i1 = new Impegno.Builder("Uscire", cat1).build();
		
		calendar.add(Calendar.HOUR, 7);
		Impegno i2 = new Impegno.Builder("Andare a casa", cat2).date(new Date(), calendar.getTime()).build();
		
		calendar.add(Calendar.HOUR, -9);
		calendar.add(Calendar.MONTH, 5);
		Date inizio = calendar.getTime();
		calendar.add(Calendar.HOUR, 15);
		Date fine = calendar.getTime();
		Impegno i3 = new Impegno.Builder("Andare a casa", cat2).date(inizio, fine).build();
		
		impegni.add(i3);
		impegni.add(i2);
		impegni.add(i1);
		
		for (Impegno impegno : impegni) {
			System.err.println("OK: " + impegno.getDataFine().toString());
		}
		
		impegni.sort(new ImpegnoComparator());
		
		for (Impegno impegno : impegni) {
			System.err.println("OK: " + impegno.getDataFine().toString());
		}
		
		
	}

	
	@Test
	public void testSovrapposizioni() throws DateNonValideException, 
										LimiteTitoloSuperatoException, 
										LimiteEtichettaSuperatoException {
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy:MM:dd");
		//String giornoCorrente = "2017:02:20 00:01";
		//DATA CORRENTE
		
		Date giornoCorrente = calendar.getTime();
		
		try {
			giornoCorrente = sdf.parse(sdf.format(giornoCorrente));
		} catch (ParseException e1) {
			// non gestire l'eccezione
		}
		
		calendar.setTime(giornoCorrente);
		
		/*
		Date giornoCorrenteData;
		try {
			giornoCorrenteData = sdf.parse(giornoCorrente);
			calendar.setTime(giornoCorrenteData);
		} catch (ParseException e1) {
			e1.printStackTrace();
		}*/
		
		Date giorno = calendar.getTime();
		calendar.add(Calendar.HOUR, 9);
		Date oneHour = calendar.getTime();
		calendar.add(Calendar.HOUR, 2);
		Date threeHour = calendar.getTime();
		
		Categoria cat1 = new Categoria();
		Categoria cat2 = new Categoria("Lavoro", Color.BLUE);
		
		Impegno i1 = new Impegno.Builder("Primo impegno", cat1).date(oneHour, threeHour).build();
		calendar.add(Calendar.HOUR, -1);
		Date sovOne = calendar.getTime();
		calendar.add(Calendar.HOUR, 2);
		Date fineSovOne = calendar.getTime();
		Impegno i2 = new Impegno.Builder("Secondo impegno", cat1).date(sovOne, fineSovOne).build();
		calendar.add(Calendar.MINUTE, -30);
		Date sovTwo = calendar.getTime();
		calendar.add(Calendar.MINUTE, 150);
		Date fineSovTwo = calendar.getTime();
		Impegno i3 = new Impegno.Builder("Terzo impegno", cat2).date(sovTwo, fineSovTwo).build();
		calendar.add(Calendar.MINUTE, -165);
		Date sovThree = calendar.getTime();
		calendar.add(Calendar.MINUTE, 165+120);
		Date fineSovThree = calendar.getTime();
		Impegno i4 = new Impegno.Builder("Quarto impegno", cat1).date(sovThree, fineSovThree).build();
		calendar.add(Calendar.MINUTE, -119);
		Date sovFour = calendar.getTime();
		calendar.add(Calendar.MINUTE, 119+180);
		Date fineSovFour = calendar.getTime();
		Impegno i5 = new Impegno.Builder("Quinto impegno", cat2).date(sovFour, fineSovFour).build();
		
		Agenda agenda = null;
		try {
			agenda = new Agenda("test.db");
			agenda.getMemory().svuotaTabelle(agenda.getConnessione().getIstanzaConnessione());
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (InizializzazioneAgendaException e) {
			e.printStackTrace();
		}
		try {
			agenda.aggiungiCategoria(cat1);
			agenda.aggiungiCategoria(cat2);
			agenda.aggiungiImpegno(i1);
			agenda.aggiungiImpegno(i2);
			agenda.aggiungiImpegno(i3);
			agenda.aggiungiImpegno(i4);
			agenda.aggiungiImpegno(i5);
			for (Impegno impegno : agenda.getGestoreImpegniOrm().getImpegni()) {
				System.out.println(impegno.toString());
			
			}
		} catch (AggiuntaImpegnoException e) {

			e.printStackTrace();
		} catch (AggiuntaCategoriaException e) {
			e.printStackTrace();
		} catch (ImpegnoOrmNonValidoException e) {
			e.printStackTrace();
		} finally {
			try {
				agenda.getConnessione().chiudiConnessione();
			} catch (ConnessioneException e) {
				e.printStackTrace();
			}
		}

		List<ImpegnoORM> impegni = new ArrayList<ImpegnoORM>();
		try {
			impegni = agenda.visualizzaImpegniCompatibili();
		} catch (ImpegnoOrmNonValidoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (ImpegnoORM impegno : impegni) {
			System.out.println(impegno.toString());

		}
	}

}
