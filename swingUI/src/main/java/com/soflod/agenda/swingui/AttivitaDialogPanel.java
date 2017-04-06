package com.soflod.agenda.swingui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.jdesktop.swingx.JXDatePicker;

import com.j256.ormlite.logger.Logger;
import com.j256.ormlite.logger.LoggerFactory;
import com.soflod.agenda.core.Attivita;
import com.soflod.agenda.core.exception.DataInizioNonValidaException;
import com.soflod.agenda.core.exception.DataScadenzaNonValidaException;
import com.soflod.agenda.core.exception.LimiteTitoloSuperatoException;
import com.soflod.agenda.persistence.Agenda;
import com.soflod.agenda.persistence.AttivitaORM;
import com.soflod.agenda.persistence.CategoriaORM;
import com.soflod.agenda.persistence.exception.AggiuntaAttivitaException;
import com.soflod.agenda.persistence.exception.AttivitaNonValidaException;
import com.soflod.agenda.persistence.exception.CategoriaOrmNonValidaException;
import com.soflod.agenda.persistence.exception.ModificaAttivitaException;


public class AttivitaDialogPanel extends JPanel implements Serializable{

	private JXDatePicker selettoreDataInizio;
	private JXDatePicker selettoreDataScadenza;
	
	private JLabel labelSelezioneDataInizio;
	private JLabel labelSelezioneDataScadenza;
	
	private JLabel labelTitolo;
	private JLabel labelCategoria;
	
	private JTextField campoTitolo;
	
	private JComboBox<CategoriaORM> elencoCategorie;
	
	private FramePrincipale framePrincipale;

	private JButton btnConferma;
	private JButton btnAnnulla;
	
	private transient Agenda agenda;
	private transient AttivitaORM attivitaORM;
	private transient Logger logger = LoggerFactory.getLogger(AttivitaDialogPanel.class);
	/**
	 * Dialog per l'inserimento o la modifica di una attivita,
	 * se l'attivita passata con il costruttore è nulla
	 * allora la procedura è relativa l'inserimento.
	 * 
	 * @param framePrincipale
	 * @param attivitaORM
	 * @throws AttivitaNonValidaException 
	 */
	public AttivitaDialogPanel(FramePrincipale framePrincipale, AttivitaORM attivitaORM) throws AttivitaNonValidaException {
		
		this.attivitaORM = attivitaORM;
		
		this.framePrincipale = framePrincipale;
		this.agenda = framePrincipale.getAgenda();
		
		if (attivitaORM == null) {
			inizializzazione();
		} else {
			inizializzazione(attivitaORM);
		}
		setLayout(new GridBagLayout());

		GridBagConstraints gc = new GridBagConstraints(); 
		
		
		gc.anchor = GridBagConstraints.WEST;
		gc.fill = GridBagConstraints.BOTH;
		collocaComponente(gc, labelTitolo, 0, 0);
		
		gc.fill = GridBagConstraints.BOTH;
		collocaComponente(gc, campoTitolo, 1, 0);	
		
		gc.fill = GridBagConstraints.BOTH;
		collocaComponente(gc, labelSelezioneDataInizio, 0, 1);	
		
		gc.anchor = GridBagConstraints.WEST;
		gc.fill = GridBagConstraints.BOTH;
		collocaComponente(gc, selettoreDataInizio, 1, 1);
		
		gc.fill = GridBagConstraints.BOTH;
		collocaComponente(gc, labelSelezioneDataScadenza, 0, 2);
		
		gc.anchor = GridBagConstraints.WEST;
		gc.fill = GridBagConstraints.BOTH;
		collocaComponente(gc, selettoreDataScadenza, 1, 2);
		
		gc.fill = GridBagConstraints.BOTH;
		collocaComponente(gc, labelCategoria, 0, 3);
		
		gc.fill = GridBagConstraints.BOTH;
		collocaComponente(gc, elencoCategorie, 1, 3);
		
		gc.fill = GridBagConstraints.NONE;
		collocaComponente(gc, btnConferma, 0, 4);
		
		gc.fill = GridBagConstraints.NONE;
		collocaComponente(gc, btnAnnulla, 1, 4);
		
		
		if (attivitaORM == null) {
			btnConferma.addActionListener(new ActionListener() {
				
				public void actionPerformed(ActionEvent e) {
					
					aggiungiAttivitaClick();
				}
			});
		} else {
			btnConferma.addActionListener(new ActionListener() {
				
				public void actionPerformed(ActionEvent e) {
					modificaAttivitaClick();
				}
			});
		}
		
		btnAnnulla.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				distruggiDialogAttivita();
			}
		});
	}

	
	
	private void distruggiDialogAttivita(){
		JDialog dialogParent = (JDialog) getRootPane().getParent();
		dialogParent.dispose();
	}
	
	private void aggiornaListaAttivita() {
		framePrincipale.getPannelloGiorno().aggiornaListaAttivita();
	}
	
	private void inizializzazione(){
		labelTitolo = new JLabel("Inserisci titolo: ");
		labelCategoria = new JLabel("Seleziona categoria: ");
		labelSelezioneDataInizio = new JLabel("Seleziona data inizio: ");
		labelSelezioneDataScadenza = new JLabel("Seleziona data fine: ");
		selettoreDataInizio = new JXDatePicker();
		selettoreDataScadenza = new JXDatePicker();
		campoTitolo = new JTextField("Inserisci titolo", 40);
		elencoCategorie = preparaComboBoxCategorie();
		btnConferma = new JButton("Conferma");
		btnAnnulla = new JButton("Annulla");
	}
	
	private void inizializzazione(AttivitaORM attivitaORM) throws AttivitaNonValidaException {
		Attivita attivita = attivitaORM.restituisciAttivita();
		
		labelTitolo = new JLabel("Inserisci titolo: ");
		labelCategoria = new JLabel("Seleziona categoria: ");
		labelSelezioneDataInizio = new JLabel("Seleziona data inizio: ");
		labelSelezioneDataScadenza = new JLabel("Seleziona data fine: ");
		selettoreDataInizio = new JXDatePicker(attivita.getDataInizio());
		selettoreDataScadenza = new JXDatePicker(attivita.getDataScadenza());
		campoTitolo = new JTextField(attivita.getTitolo(), 40);
		elencoCategorie = preparaComboBoxCategorie();
		elencoCategorie.setSelectedItem(attivitaORM.getCategoriaORM());
		btnConferma = new JButton("Conferma");
		btnAnnulla = new JButton("Annulla");
	}
	
	private void collocaComponente(GridBagConstraints gc, JComponent component, int colonna, int riga) {
		gc.gridx = colonna;
		gc.gridy = riga;
		add(component, gc);
		
	}
		
	private JComboBox<CategoriaORM> preparaComboBoxCategorie(){
		List<CategoriaORM> tmpList = agenda.getGestoreCategorieOrm().getCategorieOrm();
		
		CategoriaORM[] arrayCategorie = new CategoriaORM[tmpList.size()];
		tmpList.toArray(arrayCategorie);
		
		return new JComboBox<CategoriaORM>(arrayCategorie);
	}
	
	private void aggiungiAttivitaClick() {
		
		Attivita tmp = preparaAttivita();
		if (tmp != null) {
			try {

				agenda.aggiungiAttivita(tmp);
				aggiornaListaAttivita();
				distruggiDialogAttivita();

			} catch (AggiuntaAttivitaException e1) {
				logger.info(e1, e1.getMessage());
				JOptionPane.showMessageDialog(getParent(), e1.getMessage(), "Errore inserimento attivita'",
						JOptionPane.ERROR_MESSAGE);
			}
		}
		
	}
	
	private void modificaAttivitaClick(){
		
		
		Attivita tmp = preparaAttivita();
		
		if (tmp != null) {
		
			CategoriaORM categoriaORM = (CategoriaORM)elencoCategorie.getSelectedItem();
			AttivitaORM attivitaDiModifica = new AttivitaORM();
		
			attivitaDiModifica.setTitolo(tmp.getTitolo());
			attivitaDiModifica.setDataInizio(tmp.getDataInizio());
			attivitaDiModifica.setDataScadenza(tmp.getDataScadenza());
			attivitaDiModifica.setCategoriaORM(categoriaORM);
			
			try {
				agenda.aggiornaAttivita(attivitaORM, attivitaDiModifica);
				aggiornaListaAttivita();
				framePrincipale.visualizzaCarta(FramePrincipale.VISTA_GIORNO);
				distruggiDialogAttivita();
			} catch(ModificaAttivitaException me) {
				logger.info(me, me.getMessage());
				JOptionPane.showMessageDialog(getParent(), me.getMessage(), "Errore modifica attivita'",
						JOptionPane.ERROR_MESSAGE);
			}
		
		}
		
	}
	
	private Attivita preparaAttivita() {
		
		CategoriaORM categoriaORM = (CategoriaORM) elencoCategorie.getSelectedItem();

		if (categoriaORM != null) {
			
			return attivitaVerificata(categoriaORM);
			
		} else
			creaMessaggioErrore("Non vi sono categorie selezionate.", "Selezione categoria");
		
		return null;
	}
	
	private void creaMessaggioErrore(String messaggio, String titolo) {
		JOptionPane.showMessageDialog(getParent(), messaggio,
				titolo, JOptionPane.ERROR_MESSAGE);
	}

	private Attivita attivitaVerificata(CategoriaORM categoriaORM) {

		String titolo = campoTitolo.getText();
		Date dataInizio = selettoreDataInizio.getDate();
		Date dataScadenza = selettoreDataScadenza.getDate();
		
		Calendar calendario = Calendar.getInstance();
		calendario.add(Calendar.DATE, 1);
		
		Attivita tmp = null;
		try {

			if (dataInizio == null)
				throw new DataInizioNonValidaException();

			if (dataScadenza == null)
				throw new DataScadenzaNonValidaException();

			tmp = new Attivita.Builder(titolo, calendario.getTime()).build();
			tmp.setCategoria(categoriaORM.restituisciCategoria());

			tmp.setDataScadenza(dataScadenza);
			tmp.setDataInizio(dataInizio);

			return tmp;
			
		} catch (LimiteTitoloSuperatoException e1) {
			logger.info(e1, e1.getMessage());
			creaMessaggioErrore("Dimensione massima per il titolo superata.", "Superamento titolo");
		} catch (DataScadenzaNonValidaException e1) {
			logger.info(e1, e1.getMessage());
			creaMessaggioErrore("Data scadenza selezionata non valida.", "Data scadenza non valida");
		} catch (ParseException e1) {
			logger.info(e1, e1.getMessage());
			creaMessaggioErrore("ERRORE PARSING", "Parsing error.");
		} catch (DataInizioNonValidaException e1) {
			logger.info(e1, e1.getMessage());
			creaMessaggioErrore("Data inizio selezionata non valida.", "Data inizio non valida");
		} catch (CategoriaOrmNonValidaException e) {
			logger.info(e, e.getMessage());
			creaMessaggioErrore("La categoria non è stata settata correttamente.", "Categoria non settata correttamente");
		}
		return null;
	}
	
}

