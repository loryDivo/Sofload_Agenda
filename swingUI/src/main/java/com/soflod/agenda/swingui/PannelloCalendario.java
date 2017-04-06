package com.soflod.agenda.swingui;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.j256.ormlite.logger.Logger;
import com.j256.ormlite.logger.LoggerFactory;
import com.soflod.agenda.core.Attivita;
import com.soflod.agenda.persistence.Agenda;
import com.soflod.agenda.persistence.ImpegnoORM;
import com.soflod.agenda.persistence.exception.AttivitaNonValidaException;
import com.toedter.calendar.JCalendar;

public class PannelloCalendario extends JPanel {

	private static final long serialVersionUID = 1L;

	private JCalendar calendario;
	
	private JButton btnRubrica;
	private JButton btnGiorno;
	private JButton btnCategoria;
	private JButton btnVisualizzaImpegniCompatibili;
	private JButton btnAttivitaPrioritaMassima;
	
	private FramePrincipale framePrincipale;
	
	private transient Agenda agenda;
	private transient Logger logger = LoggerFactory.getLogger(PannelloCalendario.class);
	
	private static final int OFFSET_CALENDARIO = 8;
	
	
	public PannelloCalendario(FramePrincipale framePrincipale) {

		super();
		this.framePrincipale = framePrincipale;
		// metodo che colore le celle del calendario
		this.agenda = framePrincipale.getAgenda();
		inizializzazione();
		
		setBorder(BorderFactory.createTitledBorder("Seleziona data:"));

		setLayout(new GridBagLayout());

		GridBagConstraints gc = new GridBagConstraints(); 
		
		gc.weightx = 0.5;
		gc.weighty = 0.5;
		gc.gridx = 0;
		gc.gridy = 0;
		gc.fill = GridBagConstraints.BOTH;
		gc.anchor = GridBagConstraints.NORTH;
		add(calendario, gc);
		
		gc.gridx = 0;
		gc.gridy = 1;
		gc.fill = GridBagConstraints.NONE;
		gc.anchor = GridBagConstraints.SOUTH;
		add(btnGiorno, gc);
		
		gc.gridx = 0;
		gc.gridy = 2;
		gc.fill = GridBagConstraints.NONE;
		gc.anchor = GridBagConstraints.SOUTH;
		add(btnCategoria, gc);
		
		gc.gridx = 0;
		gc.gridy = 3;
		gc.fill = GridBagConstraints.NONE;
		gc.anchor = GridBagConstraints.SOUTH;
		add(btnRubrica, gc);
		
		gc.gridx = 0;
		gc.gridy = 4;
		gc.fill = GridBagConstraints.NONE;
		gc.anchor = GridBagConstraints.SOUTH;
		add(btnVisualizzaImpegniCompatibili, gc);
		
		gc.gridx = 0;
		gc.gridy = 5;
		gc.fill = GridBagConstraints.NONE;
		gc.anchor = GridBagConstraints.SOUTH;
		add(btnAttivitaPrioritaMassima, gc);
		
		calendario.addPropertyChangeListener("calendar", new PropertyChangeListener() {
			
			public void propertyChange(PropertyChangeEvent evt) {
				Calendar dataToccata = (Calendar) evt.getNewValue();
				trasmettiData(dataToccata.getTime());
			}
		});

		btnRubrica.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				visualizzaRubrica();
			}
		});
		
		btnCategoria.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				visualizzaCategorie();
			}
		});
		
		btnGiorno.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				visualizzaGiorno();
			}
		});
		
		btnVisualizzaImpegniCompatibili.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				visualizzaElencoImpegniCompatibili();
			}
		});
		
		btnAttivitaPrioritaMassima.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				visualizzaAttivitaPrioritaMassima();
			}
		});
	}
	
	public void coloraImpegniCalendario() {
		Component[] pannelli = calendario.getDayChooser().getDayPanel().getComponents();
		List<ImpegnoORM> tmpImpegni = agenda.getGestoreImpegniOrm().getImpegniORM();
		List<Integer> listaGiorni = ritornaGiorniImpegni(tmpImpegni);
		
		Integer tmpGiorno;
		for (int giorno = OFFSET_CALENDARIO + 1; giorno < pannelli.length; giorno++) {
			tmpGiorno = giorno - OFFSET_CALENDARIO;
			if (listaGiorni.contains(tmpGiorno))
				pannelli[giorno].setBackground(Color.GREEN);
			
		}
	}
	
	
	/**
	 * Ritorna una lista di interi rappresentanti il giorno dell'inizio
	 * di un impegno.
	 * 
	 * @param impegni
	 * @return
	 */
	private List<Integer> ritornaGiorniImpegni(List<ImpegnoORM> impegni) {
		List<Integer> listaGiorni = new ArrayList<Integer>();
		SimpleDateFormat sdfGiornoMese = new SimpleDateFormat("dd");
		String tmpStringaGiorno;
		Integer tmpGiorno;
		for (ImpegnoORM impegnoORM : impegni) {
			
			tmpStringaGiorno = sdfGiornoMese.format(impegnoORM.getDataInizio());
			try {
				tmpGiorno = Integer.parseInt(tmpStringaGiorno);
				listaGiorni.add(tmpGiorno);
			} catch(NumberFormatException pe) {
				logger.info(pe, pe.getMessage());
			}
		}
		
		return listaGiorni;
	}

	private void inizializzaColoreCalendario() {
		Component[] pannelli = calendario.getDayChooser().getDayPanel().getComponents();
		for (Component component : pannelli) {
			component.setForeground(Color.BLUE);
			
		}
	}
	
	private void inizializzazione() {
		calendario = new JCalendar(this.agenda.getDataCorrente());
		inizializzaColoreCalendario();
		coloraImpegniCalendario();
		btnRubrica = new JButton("Rubrica");
		btnGiorno = new JButton("Impegni e Attivita del Giorno");
		btnCategoria = new JButton("Categorie");
		btnVisualizzaImpegniCompatibili = new JButton("Visualizza impegni compatibili");
		btnAttivitaPrioritaMassima = new JButton("Visualizza attivita con priorità massima");
	}
	
	private void visualizzaAttivitaPrioritaMassima(){
		try {
			List<Attivita> attivitaDaSvolgere;
			attivitaDaSvolgere = agenda.restituisciAttivitaDaSvolgere();
			
			StringBuilder messaggio = new StringBuilder();
			for (Attivita attivita : attivitaDaSvolgere) {
				messaggio.append("Nome attivita': " + attivita.getTitolo() + "\n");
			}
			JOptionPane.showMessageDialog(this, messaggio);
			
		} catch (AttivitaNonValidaException e) {
			logger.info(e, e.getMessage());
		}
		
	}
	
	private void trasmettiData(Date data) {
		this.framePrincipale.aggiornaDataAgenda(data);
	}

	private void visualizzaGiorno() {
		framePrincipale.visualizzaGiorno();
	}
	
	private void visualizzaCategorie() {
		framePrincipale.visualizzaCategorie();
	}
	
	private void visualizzaRubrica(){
		framePrincipale.visualizzaRubrica();
	}
	
	private void visualizzaElencoImpegniCompatibili(){
		framePrincipale.visualizzaElencoImpegniCompatibili();
	}
	
}
