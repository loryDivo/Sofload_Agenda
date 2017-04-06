package com.soflod.agenda.swingui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.SpinnerDateModel;

import org.jdesktop.swingx.JXDatePicker;

import com.j256.ormlite.logger.Logger;
import com.j256.ormlite.logger.LoggerFactory;
import com.soflod.agenda.core.Impegno;
import com.soflod.agenda.core.Ripetizione;
import com.soflod.agenda.core.exception.DataInizioNonValidaException;
import com.soflod.agenda.core.exception.DataScadenzaNonValidaException;
import com.soflod.agenda.core.exception.DateNonValideException;
import com.soflod.agenda.core.exception.LimiteNoteSuperatoException;
import com.soflod.agenda.core.exception.LimiteTitoloSuperatoException;
import com.soflod.agenda.persistence.Agenda;
import com.soflod.agenda.persistence.CategoriaORM;
import com.soflod.agenda.persistence.ImpegnoORM;
import com.soflod.agenda.persistence.exception.AggiuntaImpegnoException;
import com.soflod.agenda.persistence.exception.CategoriaOrmNonValidaException;
import com.soflod.agenda.persistence.exception.ImpegnoOrmNonValidoException;
import com.soflod.agenda.persistence.exception.ModificaImpegnoException;

public class ImpegnoDialogPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	
	
	private JXDatePicker selettoreDataInizio;
	private JSpinner selettoreOraInizio;
	private JXDatePicker selettoreDataFine;
	private JSpinner selettoreOraFine;

	private JTextField campoTitolo;
	private JLabel labelSelezioneDataInizio;
	private JLabel labelSelezioneDataFine;
	private JComboBox<CategoriaORM> elencoCategorie;
	private JComboBox<Ripetizione> elencoRipetizioni;

	private JToggleButton toggleAllarme;
	private static final String ALLARME_ATTIVO = "Allarme attivato";
	private static final String ALLARME_DISATTIVO = "Allarme disattivato";
	private static final String FORMATO_ORA = "HH:mm";

	private JTextArea campoNote;

	private JLabel labelTitolo;
	private JLabel labelCategoria;
	private JLabel labelRipetizione;
	private JLabel labelAllarme;
	private JLabel labelNote;

	private FramePrincipale framePrincipale;

	private JButton btnConferma;
	private JButton btnAnnulla;
	

	private transient ImpegnoORM impegnoORM;
	private transient Agenda agenda;
	private transient Logger logger = LoggerFactory.getLogger(ImpegnoDialogPanel.class);
	
	public ImpegnoDialogPanel(FramePrincipale framePrincipale, ImpegnoORM impegnoORM) throws ImpegnoOrmNonValidoException {

		this.impegnoORM = impegnoORM;
		this.framePrincipale = framePrincipale;
		this.agenda = framePrincipale.getAgenda();

		if (impegnoORM == null) {
			inizializzazione();
		} else {
			inizializzazione(impegnoORM);
		}
		setLayout(new GridBagLayout());

		GridBagConstraints gc = new GridBagConstraints();

		gc.gridx = 0;
		gc.gridy = 0;
		gc.anchor = GridBagConstraints.WEST;
		gc.fill = GridBagConstraints.BOTH;
		add(labelTitolo, gc);

		gc.gridx = 1;
		gc.gridy = 0;
		gc.fill = GridBagConstraints.BOTH;
		add(campoTitolo, gc);

		gc.gridx = 0;
		gc.gridy = 1;
		gc.fill = GridBagConstraints.BOTH;
		add(labelSelezioneDataInizio, gc);

		gc.gridx = 1;
		gc.gridy = 1;
		gc.anchor = GridBagConstraints.WEST;
		gc.fill = GridBagConstraints.BOTH;
		add(selettoreDataInizio, gc);
		
		gc.gridx = 2;
		gc.gridy = 1;
		gc.anchor = GridBagConstraints.WEST;
		gc.fill = GridBagConstraints.BOTH;
		add(selettoreOraInizio, gc);
		

		gc.gridx = 0;
		gc.gridy = 2;
		gc.fill = GridBagConstraints.BOTH;
		add(labelSelezioneDataFine, gc);

		gc.gridx = 1;
		gc.gridy = 2;
		gc.anchor = GridBagConstraints.WEST;
		gc.fill = GridBagConstraints.BOTH;
		add(selettoreDataFine, gc);
		
		gc.gridx = 2;
		gc.gridy = 2;
		gc.anchor = GridBagConstraints.WEST;
		gc.fill = GridBagConstraints.BOTH;
		add(selettoreOraFine, gc);
		
		gc.gridx = 0;
		gc.gridy = 3;
		gc.fill = GridBagConstraints.BOTH;
		add(labelAllarme, gc);

		gc.gridx = 1;
		gc.gridy = 3;
		gc.anchor = GridBagConstraints.WEST;
		gc.fill = GridBagConstraints.BOTH;
		add(toggleAllarme, gc);
		
		
		gc.gridx = 0;
		gc.gridy = 4;
		gc.fill = GridBagConstraints.BOTH;
		add(labelRipetizione, gc);

		gc.gridx = 1;
		gc.gridy = 4;
		gc.anchor = GridBagConstraints.WEST;
		gc.fill = GridBagConstraints.BOTH;
		add(elencoRipetizioni, gc);
		
		gc.gridx = 0;
		gc.gridy = 5;
		gc.fill = GridBagConstraints.BOTH;
		add(labelCategoria, gc);

		gc.gridx = 1;
		gc.gridy = 5;
		gc.fill = GridBagConstraints.BOTH;
		add(elencoCategorie, gc);

		gc.gridx = 0;
		gc.gridy = 6;
		gc.fill = GridBagConstraints.BOTH;
		add(labelNote, gc);

		gc.gridx = 1;
		gc.gridy = 6;
		gc.anchor = GridBagConstraints.WEST;
		gc.fill = GridBagConstraints.BOTH;
		add(campoNote, gc);
		
		gc.gridx = 0;
		gc.gridy = 7;
		gc.fill = GridBagConstraints.NONE;
		add(btnConferma, gc);

		gc.gridx = 1;
		gc.gridy = 7;
		gc.fill = GridBagConstraints.NONE;
		add(btnAnnulla, gc);
		
		if (impegnoORM == null) {
			btnConferma.addActionListener(new ActionListener() {
				
				public void actionPerformed(ActionEvent e) {
					aggiungiImpegnoClick();
				}
			});
		} else {
			btnConferma.addActionListener(new ActionListener() {
				
				public void actionPerformed(ActionEvent e) {
					modificaImpegnoClick();
				}
			});
		}
		
		btnAnnulla.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				distruggiDialogImpegno();
			}
		});

		toggleAllarme.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				cliccaToggleAllarme();
			}
		});
	}

	public void inizializzazione() {

		labelTitolo = new JLabel("Inserisci titolo impegno: ");
		campoTitolo = new JTextField("Inserire titolo impegno");
		labelSelezioneDataInizio = new JLabel("Selezionare la data e l'ora dell'inizio dell'impegno:");

		Date tmpData = new Date();
		selettoreDataInizio = new JXDatePicker(tmpData);

		selettoreOraInizio = new JSpinner(new SpinnerDateModel());
		JSpinner.DateEditor editorTempo = new JSpinner.DateEditor(selettoreOraInizio, FORMATO_ORA);
		selettoreOraInizio.setEditor(editorTempo);
		selettoreOraInizio.setValue(tmpData);

		labelSelezioneDataFine = new JLabel("Selezionare la data e l'ora della fine dell'impegno:");

		selettoreDataFine = new JXDatePicker(tmpData);
		selettoreOraFine = new JSpinner(new SpinnerDateModel());
		editorTempo = new JSpinner.DateEditor(selettoreOraFine, FORMATO_ORA);
		selettoreOraFine.setEditor(editorTempo);
		selettoreOraFine.setValue(tmpData);

		labelAllarme = new JLabel("Scegliere se l'allarme sarà attivo: ");
		toggleAllarme = new JToggleButton(ALLARME_DISATTIVO);
		toggleAllarme.setSelected(false);

		labelRipetizione = new JLabel("Seleziona la ripetizione: ");
		elencoRipetizioni = preparaComboBoxRipetizione();
		elencoRipetizioni.setSelectedIndex(0); // seleziona di default nessuna
												// ripetizione

		labelCategoria = new JLabel("Seleziona una categoria da associare all'impegno: ");
		elencoCategorie = preparaComboBoxCategorie();

		labelNote = new JLabel("Inserisci delle note: ");
		campoNote = new JTextArea(20, 20);
		
		btnConferma = new JButton("Conferma");
		btnAnnulla = new JButton("Annulla");
		
	}
	
	public void inizializzazione(ImpegnoORM impegnoORM) throws ImpegnoOrmNonValidoException {

		Impegno tmpImpegno = impegnoORM.restituisciImpegno();
		
		labelTitolo = new JLabel("Inserisci titolo impegno: ");
		campoTitolo = new JTextField(tmpImpegno.getTitolo());
		labelSelezioneDataInizio = new JLabel("Selezionare la data e l'ora dell'inizio dell'impegno:");

		selettoreDataInizio = new JXDatePicker(tmpImpegno.getDataInizio());

		selettoreOraInizio = new JSpinner(new SpinnerDateModel());
		JSpinner.DateEditor editorTempo = new JSpinner.DateEditor(selettoreOraInizio, FORMATO_ORA);
		selettoreOraInizio.setEditor(editorTempo);
		selettoreOraInizio.setValue(tmpImpegno.getDataInizio());

		labelSelezioneDataFine = new JLabel("Selezionare la data e l'ora della fine dell'impegno:");

		selettoreDataFine = new JXDatePicker(tmpImpegno.getDataFine());
		selettoreOraFine = new JSpinner(new SpinnerDateModel());
		editorTempo = new JSpinner.DateEditor(selettoreOraFine, FORMATO_ORA);
		selettoreOraFine.setEditor(editorTempo);
		selettoreOraFine.setValue(tmpImpegno.getDataFine());

		labelAllarme = new JLabel("Scegliere se l'allarme sarà attivo: ");
		toggleAllarme = new JToggleButton(ALLARME_DISATTIVO);
		toggleAllarme.setSelected(false);
		
		if (tmpImpegno.getAllarme()) {
			cliccaToggleAllarme();
		}
		
		labelRipetizione = new JLabel("Seleziona la ripetizione: ");
		elencoRipetizioni = preparaComboBoxRipetizione();
		elencoRipetizioni.setSelectedItem(tmpImpegno.getRipetizione());

		labelCategoria = new JLabel("Seleziona una categoria da associare all'impegno: ");
		elencoCategorie = preparaComboBoxCategorie();
		elencoCategorie.setSelectedItem(impegnoORM.getCategoriaORM());

		labelNote = new JLabel("Inserisci delle note: ");
		campoNote = new JTextArea(20, 20);
		campoNote.setText(tmpImpegno.getNote());
		
		btnConferma = new JButton("Conferma");
		btnAnnulla = new JButton("Annulla");
		
	}

	private JComboBox<CategoriaORM> preparaComboBoxCategorie() {
		List<CategoriaORM> tmpList = agenda.getGestoreCategorieOrm().getCategorieOrm();
		CategoriaORM[] arrayCategorie = new CategoriaORM[tmpList.size()];
		tmpList.toArray(arrayCategorie);

		return new JComboBox<CategoriaORM>(arrayCategorie);
	}

	private JComboBox<Ripetizione> preparaComboBoxRipetizione() {
		JComboBox<Ripetizione> tmp = new JComboBox<Ripetizione>();
		tmp.addItem(Ripetizione.NESSUNA);
		tmp.addItem(Ripetizione.GIORNO);
		tmp.addItem(Ripetizione.SETTIMANA);
		tmp.addItem(Ripetizione.MESE);
		tmp.addItem(Ripetizione.ANNO);

		return tmp;
	}
	
	
	private void distruggiDialogImpegno(){
		JDialog dialogParent = (JDialog) getRootPane().getParent();
		dialogParent.dispose();
	}
	
	private void aggiornaListaImpegni() {
		framePrincipale.getPannelloGiorno().aggiornaListaImpegni();
	}
	
	private void aggiungiImpegnoClick() {
		
		Impegno tmp = preparaImpegno();
		
		if (tmp != null) {
			
			try{	
				agenda.aggiungiImpegno(tmp);
				aggiornaListaImpegni();
				distruggiDialogImpegno();
			} catch (AggiuntaImpegnoException e) {
				logger.info(e, e.getMessage());
				JOptionPane.showMessageDialog(getParent(), "Errore nell'inserimento dell'impegno: "
						+ e.getMessage(), "Errore inserimento impegno", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	private void modificaImpegnoClick(){
		
		Impegno tmp = preparaImpegno();
		
		if (tmp != null) {
		
			
			ImpegnoORM impegnoDiModifica = new ImpegnoORM();
			impegnoDiModifica.setTitolo(tmp.getTitolo());
			impegnoDiModifica.setDataInizio(tmp.getDataInizio());
			impegnoDiModifica.setDataFine(tmp.getDataFine());
			impegnoDiModifica.setAllarme(tmp.getAllarme());
			impegnoDiModifica.setRipetizione(tmp.getRipetizione());
			impegnoDiModifica.setNote(tmp.getNote());
			impegnoDiModifica.setCategoriaORM((CategoriaORM) elencoCategorie.getSelectedItem());
			
			try {
				agenda.aggiornaImpegno(impegnoORM, impegnoDiModifica);
				aggiornaListaImpegni();
				framePrincipale.visualizzaCarta(FramePrincipale.VISTA_GIORNO);
				distruggiDialogImpegno();
			} catch(ModificaImpegnoException me) {
				logger.info(me, me.getMessage());
				JOptionPane.showMessageDialog(getParent(), "Errore nella modifica dell'impegno: "
						+ me.getMessage(), "Errore modifica impegno", JOptionPane.ERROR_MESSAGE);
			}
		
		}
	}
	
	/**
	 * In input ho due date di cui voglio ricavare il giorno dalla prima e solo
	 * ora e minuti dalla seconda.
	 * 
	 * Restituisco in output la data risultante dalla convergenze del giorno
	 * della prima e l'ora della seconda.
	 * 
	 * @param data
	 * @param ora
	 * @return
	 * @throws ParseException 
	 */
	private Date convergiDataOra(Date data, Date ora) throws ParseException {
		
		final String precisioneOra = FORMATO_ORA;
		SimpleDateFormat sdfGiorno = new SimpleDateFormat(Agenda.PRECISIONE_GIORNO);
		SimpleDateFormat sdfOra = new SimpleDateFormat(precisioneOra);
		
		String formatoGiorno = sdfGiorno.format(data);
		String formatoOra = sdfOra.format(ora);
		
		String formatoDataOra = formatoGiorno + " " + formatoOra;
		
		SimpleDateFormat sdfGiornoOra = new SimpleDateFormat(Agenda.PRECISIONE_GIORNO + " " + precisioneOra);
		
		Date tmp;
		
		tmp = sdfGiornoOra.parse(formatoDataOra);
		
		return tmp;
		
	}
	
	private Impegno preparaImpegno() {
		String titolo = campoTitolo.getText();
		CategoriaORM categoriaORM = (CategoriaORM) elencoCategorie.getSelectedItem();

		boolean allarme = toggleAllarme.isSelected();
		String note = campoNote.getText().trim();
		Ripetizione ripetizione = (Ripetizione) elencoRipetizioni.getSelectedItem();

		Date dataInizio = selettoreDataInizio.getDate();
		Date oraInizio = (Date) selettoreOraInizio.getValue();

		Date dataFine = selettoreDataFine.getDate();
		Date oraFine = (Date) selettoreOraFine.getValue();

		if (categoriaORM != null) {

			Impegno tmp = null;
			try {

				if (dataInizio == null)
					throw new DataInizioNonValidaException();

				if (dataFine == null)
					throw new DataScadenzaNonValidaException();

				Date dataOraInizio = convergiDataOra(dataInizio, oraInizio);

				Date dataOraFine = convergiDataOra(dataFine, oraFine);

				tmp = new Impegno.Builder(titolo, categoriaORM.restituisciCategoria()).date(dataOraInizio, dataOraFine)
						.allarme(allarme).ripetizione(ripetizione).note(note).build();

				return tmp;

			} catch (LimiteTitoloSuperatoException e1) {
				logger.info(e1, e1.getMessage());
				JOptionPane.showMessageDialog(getParent(), "Dimensione massima per il titolo superata.",
						"Superamento titolo", JOptionPane.ERROR_MESSAGE);
			} catch (DataScadenzaNonValidaException e1) {
				logger.info(e1, e1.getMessage());
				JOptionPane.showMessageDialog(getParent(), "Data scadenza selezionata non valida.",
						"Data scadenza non valida", JOptionPane.ERROR_MESSAGE);
			} catch (ParseException e1) {
				logger.info(e1, e1.getMessage());
				JOptionPane.showMessageDialog(getParent(), "ERRORE PARSING", "Parsing error.",
						JOptionPane.ERROR_MESSAGE);

			} catch (DataInizioNonValidaException e1) {
				logger.info(e1, e1.getMessage());
				JOptionPane.showMessageDialog(getParent(), "Data inizio selezionata non valida.",
						"Data inizio non valida", JOptionPane.ERROR_MESSAGE);
			} catch (LimiteNoteSuperatoException e) {
				logger.info(e, e.getMessage());
				JOptionPane.showMessageDialog(getParent(), "Massima lunghezza delle note superata.",
						"Lunghezza note superata", JOptionPane.ERROR_MESSAGE);
			} catch (DateNonValideException e) {
				logger.info(e, e.getMessage());
				JOptionPane.showMessageDialog(getParent(), "Le date inserite non sono valide.", "Date non valide",
						JOptionPane.ERROR_MESSAGE);
			} catch (CategoriaOrmNonValidaException e) {
				logger.info(e, e.getMessage());
				JOptionPane.showMessageDialog(getParent(), "Errore reperimento categoriaORM.",
						"Errore reperimento categorie", JOptionPane.ERROR_MESSAGE);
			}
		} else
			JOptionPane.showMessageDialog(getParent(), "Nessuna categoria selezionata.",
					"Errore selezione categoria", JOptionPane.ERROR_MESSAGE);
		
		
		return null;
	}
	
	
	private void cliccaToggleAllarme() {
		
		if (toggleAllarme.getText().equals(ALLARME_ATTIVO)) {
			toggleAllarme.setText(ALLARME_DISATTIVO);
			toggleAllarme.setSelected(false);
		} else {
			toggleAllarme.setText(ALLARME_ATTIVO);
			toggleAllarme.setSelected(true);
		}
	}
	
}
