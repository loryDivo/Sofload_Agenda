package com.soflod.agenda.swingui;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.j256.ormlite.logger.Logger;
import com.j256.ormlite.logger.LoggerFactory;
import com.soflod.agenda.core.Categoria;
import com.soflod.agenda.persistence.Agenda;
import com.soflod.agenda.persistence.AttivitaORM;
import com.soflod.agenda.persistence.CategoriaORM;
import com.soflod.agenda.persistence.ContattoORM;
import com.soflod.agenda.persistence.ImpegnoORM;
import com.soflod.agenda.persistence.exception.AggiuntaCategoriaException;
import com.soflod.agenda.persistence.exception.AttivitaNonValidaException;
import com.soflod.agenda.persistence.exception.CategoriaOrmNonValidaException;
import com.soflod.agenda.persistence.exception.ConnessioneException;
import com.soflod.agenda.persistence.exception.ImpegnoOrmNonValidoException;
import com.soflod.agenda.persistence.exception.InizializzazioneAgendaException;

public class FramePrincipale extends JFrame {
	
	private PannelloCalendario pannelloCalendario;

	private JPanel pannelloContenitore;
	private PannelloGiorno pannelloGiorno;
	private PannelloImpegno pannelloImpegno;
	private PannelloAttivita pannelloAttivita;
	private PannelloRubrica pannelloRubrica;
	private PannelloCategorie pannelloCategorie;
	private PannelloCategoria pannelloCategoria;
	
	private Date vecchiaData;
	
	private CardLayout layoutCarte;
	
	public static final String VISTA_GIORNO = "Vista giorno";
	public static final String VISTA_ATTIVITA	= "Vista attivita";
	public static final String VISTA_IMPEGNO = "Vista impegno";
	public static final String VISTA_RUBRICA = "Vista rubrica";
	public static final String VISTA_CONTATTO = "Vista contatto";
	public static final String VISTA_CATEGORIE = "Vista categorie";
	public static final String VISTA_CATEGORIA = "Vista categoria";
	public static final String VISTA_IMPEGNI_COMPATIBILI = "Vista impegni compatibili";

	private transient AgendaFrame agendaFrame;
	private transient Logger logger = LoggerFactory.getLogger(FramePrincipale.class);
	private transient Agenda agenda;

	public FramePrincipale(AgendaFrame agendaFrame, String title, String path) throws InizializzazioneAgendaException {
		
		super(title);
		this.agenda = null;
		this.agendaFrame = agendaFrame;
		
		WindowListener exitListener = new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				chiusuraFramePrincipale();
				
			}
		};
		this.addWindowListener(exitListener);
		
		try {
			
			this.agenda = new Agenda(path);
			
			/* 
			 * all'inizio la vecchia data coincide con quella della
			 * agenda
			 */
			this.vecchiaData = agenda.getDataCorrente();
			
			
			
		} catch (InizializzazioneAgendaException iae) {
			chiusuraConnessione();
			throw iae;
		}
		
		// il pannello calendario avra' gia' la data del giorno attuale
		this.pannelloCalendario = new PannelloCalendario(this);

		Container c = getContentPane();
		c.setLayout(new BorderLayout());
		c.add(this.pannelloCalendario, BorderLayout.WEST);
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int calendarWidth = (int) screenSize.getWidth() / 10 * 2;
		int calendarHeight = (int) screenSize.getHeight();
		Dimension dimensioniCalendario = new Dimension(calendarWidth, calendarHeight);
		this.pannelloCalendario.setPreferredSize(dimensioniCalendario);

		
		// Inizio Gestione cardLayout
		// creazione dei pannelli
		this.pannelloGiorno = new PannelloGiorno(this);
				
		layoutCarte = new CardLayout();
		this.pannelloContenitore = new JPanel(layoutCarte);
		this.pannelloContenitore.setLayout(layoutCarte);
		this.pannelloContenitore.add(pannelloGiorno, VISTA_GIORNO);
		layoutCarte.show(pannelloContenitore, VISTA_GIORNO);

		
		// aggiungo il pannello con card layout al layout del framePrincipale
		c.add(pannelloContenitore, BorderLayout.CENTER);
		
		// inizializzo le liste con gli impegni e le attivita del giorno
		
		iniziallizzaGestori();
		this.pannelloCalendario.coloraImpegniCalendario();
		this.pack();
	}

	public Agenda getAgenda() {
		return agenda;
	}
	
	private void iniziallizzaGestori() {
		caricaImpegniAttivitaGiorno(this.agenda.getDataCorrente());
		try {
			agenda.getGestoreCategorieOrm().aggiornaCategorie();
			agenda.getRubrica().aggiornaContatti();
		
			if (agenda.getGestoreCategorieOrm().getCategorieOrm().isEmpty())
				agenda.aggiungiCategoria(new Categoria());
		} catch (SQLException e) {
			logger.error(e, e.getMessage());
		} catch (AggiuntaCategoriaException e) {
			logger.error(e, e.getMessage());
		}
		
	}

	private void caricaImpegniAttivitaGiorno(Date data) {
		
		//Pulizia liste impegni e attivita'
		this.agenda.getGestoreImpegniOrm().getImpegniORM().clear();
		this.agenda.getGestoreAttivitaOrm().getAttivitaOrm().clear();
		
		// ricava le date dal primo giorno del mese, all'ultimo giorno del mese.
		
		//Data con precisione al mese
		SimpleDateFormat tmpSdf = new SimpleDateFormat(Agenda.PRECISIONE_MESE);
		String mese = tmpSdf.format(data);
		Date dataMese = null;
		try {
			dataMese = tmpSdf.parse(mese);
		} catch (ParseException e) {
			// Non serve gestire l'eccezione
		}
		
		//Data con precisione al giorno
		tmpSdf = new SimpleDateFormat(Agenda.PRECISIONE_GIORNO);
		mese = tmpSdf.format(dataMese);
		
		// in caso va male, ritorno alla vecchia data
		Date primoGiornoMese = vecchiaData;
		try {
			primoGiornoMese = tmpSdf.parse(mese);
		} catch (ParseException e) {
			//Non serve gestire
		}
		
		Calendar calendario = Calendar.getInstance();
		calendario.setTime(primoGiornoMese);
		int giorniNelMese = calendario.getActualMaximum(Calendar.DAY_OF_MONTH);
		calendario.add(Calendar.DATE, giorniNelMese);
		Date ultimoGiornoMese = calendario.getTime();
		
		try {
			this.agenda.getGestoreImpegniOrm().richiediImpegni(primoGiornoMese, ultimoGiornoMese);
			this.agenda.getGestoreAttivitaOrm().richiediAttivita(dataMese);
			
			this.pannelloGiorno.aggiornaListaAttivita(data);
			this.pannelloGiorno.aggiornaListaImpegni(data);
		} catch (SQLException e) {
			logger.info(e, e.getMessage());
			JOptionPane.showMessageDialog(this, "Errore nel reperimento delle informazioni.",
					"Errore SQL", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	
	/**
	 *	Se il mese della data corrente è cambiato,
	 * 	svuota da agenda le arraylist degli impegni e 
	 * 	delle attivita' e aggiornale rispetto la nuova
	 *  data.
	 * 
	 */
	public void aggiornaDataAgenda(Date data) {
		
		this.vecchiaData = this.agenda.getDataCorrente();
		this.agenda.setDataCorrente(data);
		
		this.pannelloGiorno.aggiornaData();
		if (Agenda.confrontaDate(data, vecchiaData, Agenda.PRECISIONE_MESE) != 0) {
			caricaImpegniAttivitaGiorno(data);
		} else if (Agenda.confrontaDate(data, vecchiaData, Agenda.PRECISIONE_GIORNO) != 0) {
			this.pannelloGiorno.aggiornaListaImpegni(data);
		}
		pannelloCalendario.coloraImpegniCalendario();
		this.pannelloCalendario.coloraImpegniCalendario();
		
	}

	private void chiusuraConnessione() {
		try {
			agenda.getConnessione().chiudiConnessione();
		} catch (ConnessioneException e1) {
			logger.info(e1, e1.getMessage());
			JOptionPane.showMessageDialog(this, "Il database non e' stato chiuso correttamente", "Chiusura db errata", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public PannelloCalendario getPannelloCalendario() {
		return this.pannelloCalendario;
	}


	public PannelloGiorno getPannelloGiorno() {
		return this.pannelloGiorno;
	}
	
	public void chiusuraFramePrincipale() {
		chiusuraConnessione();
		agendaFrame.mostraFrameLogin();
	}
	
	public void visualizzaAttivitaOrmSelezionata(AttivitaORM attivitaSelezionata){
		try {
			this.pannelloAttivita = new PannelloAttivita(this, attivitaSelezionata);
			this.pannelloContenitore.add(pannelloAttivita, VISTA_ATTIVITA);
			visualizzaCarta(VISTA_ATTIVITA);
		} catch(AttivitaNonValidaException ae) {
			logger.info(ae, ae.getMessage());
			JOptionPane.showMessageDialog(this, "Errore nel reperire l'attivita.", "Errore reperimento attivita"
					+ ae.getMessage(), JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public void visualizzaImpegnoOrmSelezionato(ImpegnoORM impegnoSelezionato){
		try {
			this.pannelloImpegno= new PannelloImpegno(this, impegnoSelezionato);
			this.pannelloContenitore.add(pannelloImpegno, VISTA_IMPEGNO);
			visualizzaCarta(VISTA_IMPEGNO);
		} catch(ImpegnoOrmNonValidoException ie) {
			logger.info(ie, ie.getMessage());
			JOptionPane.showMessageDialog(this, "Errore nel reperire l'impegno.", "Errore reperimento impegno"
					+ ie.getMessage(), JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public void visualizzaContattoOrmSelezionato(ContattoORM contattoSelezionato){
		PannelloContatto pannelloContatto;
		pannelloContatto = new PannelloContatto(this, contattoSelezionato);
		this.pannelloContenitore.add(pannelloContatto, VISTA_CONTATTO);
		visualizzaCarta(VISTA_CONTATTO);
	}
	
	public void visualizzaCategoriaOrmSelezionata(CategoriaORM categoriaOrm){
		try {
			this.pannelloCategoria = new PannelloCategoria(this, categoriaOrm);
			this.pannelloContenitore.add(pannelloCategoria, VISTA_CATEGORIA);
			visualizzaCarta(VISTA_CATEGORIA);
		} catch (CategoriaOrmNonValidaException e) {
			logger.info(e, e.getMessage());
			JOptionPane.showMessageDialog(this, "Errore nel reperire la categoria", "Errore reperimento categoria"
					+ e.getMessage(), JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public void visualizzaRubrica(){
		this.pannelloRubrica = new PannelloRubrica(this);
		this.pannelloContenitore.add(pannelloRubrica, VISTA_RUBRICA);
		visualizzaCarta(VISTA_RUBRICA);
	}
	
	public void visualizzaGiorno(){
		visualizzaCarta(VISTA_GIORNO);
	}
	
	public void visualizzaCategorie(){
		this.pannelloCategorie = new PannelloCategorie(this);
		this.pannelloContenitore.add(pannelloCategorie, VISTA_CATEGORIE);
		visualizzaCarta(VISTA_CATEGORIE);
	}
	
	public void visualizzaElencoImpegniCompatibili(){
		PannelloImpegniCompatibili pannelloImpegniCompatibili;
		pannelloImpegniCompatibili = new PannelloImpegniCompatibili(this);
		this.pannelloContenitore.add(pannelloImpegniCompatibili, VISTA_IMPEGNI_COMPATIBILI);
		visualizzaCarta(VISTA_IMPEGNI_COMPATIBILI);
	}
	
	public void richiamaModificaAttivitaOrm(AttivitaORM attivitaOrmDaModificare){
		try {
			AttivitaDialog attivitaDialog;
			attivitaDialog = new AttivitaDialog(this, attivitaOrmDaModificare);
			attivitaDialog.setVisible(true);
		} catch (AttivitaNonValidaException e) {
			logger.info(e, e.getMessage());
			JOptionPane.showMessageDialog(this, "Errore nel reperire l'attivita.", "Errore reperimento attivita"
					+ e.getMessage(), JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public void richiamaModificaImpegnoOrm(ImpegnoORM impegnoOrmDaModificare){
		try {
			ImpegnoDialog impegnoDialog;
			impegnoDialog = new ImpegnoDialog(this, impegnoOrmDaModificare);
			impegnoDialog.setVisible(true);
		} catch (ImpegnoOrmNonValidoException e) {
			logger.info(e, e.getMessage());
			JOptionPane.showMessageDialog(this, "Errore nel reperire l'impegno.", "Errore reperimento impegno"
					+ e.getMessage(), JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public PannelloRubrica getPannelloRubrica() {
		return pannelloRubrica;
	}

	public void visualizzaCarta(String cartaDaVisualizzare){
		layoutCarte.show(pannelloContenitore, cartaDaVisualizzare);
	}

	public PannelloCategorie getPannelloCategorie() {
		return pannelloCategorie;
	}
	
	
}
