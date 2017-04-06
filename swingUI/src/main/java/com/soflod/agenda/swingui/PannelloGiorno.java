package com.soflod.agenda.swingui;

import java.awt.Color;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.j256.ormlite.logger.Logger;
import com.j256.ormlite.logger.LoggerFactory;
import com.soflod.agenda.persistence.Agenda;
import com.soflod.agenda.persistence.AttivitaORM;
import com.soflod.agenda.persistence.ImpegnoORM;
import com.soflod.agenda.persistence.exception.AttivitaNonValidaException;
import com.soflod.agenda.persistence.exception.ImpegnoOrmNonValidoException;
import com.soflod.agenda.persistence.exception.RimozioneAttivitaException;
import com.soflod.agenda.persistence.exception.RimozioneImpegnoException;

public class PannelloGiorno extends JPanel {

	private FramePrincipale framePrincipale;
	
	private JLabel labelImpegni;
	private JLabel labelAttivita;
	private JLabel labelGiorno;
	
	private JButton btnAggiungiImpegno;
	private JButton btnAggiungiAttivita;
	
	private JButton btnEliminaImpegno;
	private JButton btnEliminaAttivita;
	
	private JList<ImpegnoORM> elencoImpegni;
	private JList<AttivitaORM> elencoAttivita;
	
	private DefaultListModel<ImpegnoORM> modelloImpegni;
	private DefaultListModel<AttivitaORM> modelloAttivita;
	
	private JScrollPane pannelloImpegni;
	private JScrollPane pannelloAttivita;
	
	private transient Agenda agenda;
	private transient Logger logger = LoggerFactory.getLogger(PannelloGiorno.class);

	public PannelloGiorno(FramePrincipale framePrincipale) {

		this.agenda = framePrincipale.getAgenda();
		this.framePrincipale = framePrincipale;
		
		inizializzazione();
		
		setLayout(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();
		
		gc.weightx = 0.2;
		gc.weighty = 0.2;
		gc.gridx = 1;
		gc.gridy = 0;
		gc.fill = GridBagConstraints.BOTH;
		gc.anchor = GridBagConstraints.CENTER;
		add(labelGiorno, gc);
		
		gc.weightx = 0.4;
		gc.weighty = 0.4;
		gc.gridx = 0;
		gc.gridy = 1;
		gc.anchor = GridBagConstraints.CENTER;
		gc.fill = GridBagConstraints.BOTH;
		add(labelImpegni, gc);
		
		gc.gridx = 2;
		gc.gridy = 1;
		gc.weightx = 0.4;
		gc.weighty = 0.4;
		gc.anchor = GridBagConstraints.CENTER;
		gc.fill = GridBagConstraints.BOTH;
		add(labelAttivita, gc);
		
		
		gc.gridx = 0;
		gc.gridy = 2;
		gc.weightx = 1.0;
		gc.weighty = 0.7;
		gc.fill = GridBagConstraints.BOTH;
		gc.anchor = GridBagConstraints.CENTER;
		add(pannelloImpegni, gc);
		
		gc.gridx = 2;
		gc.gridy = 2;
		gc.weightx = 1.0;
		gc.weighty = 0.7;
		gc.fill = GridBagConstraints.BOTH;
		gc.anchor = GridBagConstraints.CENTER;
		add(pannelloAttivita, gc);
		
		
		gc.gridx = 0;
		gc.gridy = 3;
		gc.weightx = 0.1;
		gc.weighty = 0.1;
		gc.anchor = GridBagConstraints.SOUTH;
		gc.fill = GridBagConstraints.NONE;
		add(btnAggiungiImpegno, gc);
		
		gc.gridx = 2;
		gc.gridy = 3;
		gc.weightx = 0.1;
		gc.weighty = 0.1;
		gc.anchor = GridBagConstraints.SOUTH;
		gc.fill = GridBagConstraints.NONE;
		add(btnAggiungiAttivita, gc);
		
		gc.gridx = 0;
		gc.gridy = 4;
		gc.weightx = 0.1;
		gc.weighty = 0.1;
		add(btnEliminaImpegno, gc);
		
		gc.gridx = 2;
		gc.gridy = 4;
		gc.weightx = 0.1;
		gc.weighty = 0.1;
		add(btnEliminaAttivita, gc);

		// Gestione eventi
		
		btnAggiungiImpegno.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				richiamaInserisciImpegnoDialog();
			}
		});

		btnEliminaImpegno.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ImpegnoORM impegnoSelezionato = elencoImpegni.getSelectedValue();
				if (impegnoSelezionato != null) {

					try {
						rimuoviImpegnoSelezionato(impegnoSelezionato);
						aggiornaListaImpegni();
					} catch (RimozioneImpegnoException rie) {
						logger.info(rie, rie.getMessage());
						JOptionPane.showMessageDialog(getParent(),
								"Errore nell'eliminazione dell'attivita selezionata: " + rie.getMessage(),
								" Errore eliminazione attivita", JOptionPane.ERROR_MESSAGE);
					}
				} else {
					JOptionPane.showMessageDialog(getParent(), "Nessun impegno selezionato.",
							"Selezione impegno", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		btnAggiungiAttivita.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				richiamaInserisciAttivitaDialog();
			}
		});

		btnEliminaAttivita.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AttivitaORM attivitaSelezionata = elencoAttivita.getSelectedValue();
				if(attivitaSelezionata != null){
					try {
						rimuoviAttivitaSelezionata(attivitaSelezionata);
						aggiornaListaAttivita();
					} catch (RimozioneAttivitaException rae) {
						logger.info(rae, rae.getMessage());
						JOptionPane.showMessageDialog(getParent(), "Errore nell'eliminazione dell'attivita selezionata: "
						+ rae.getMessage(),
								" Errore eliminazione attivita", JOptionPane.ERROR_MESSAGE);
					}
				} else {
					JOptionPane.showMessageDialog(getParent(), "Nessuna attivita selezionata.",
							"Selezione attivita", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		
		elencoAttivita.addMouseListener(new MouseListener() {
			public void mouseReleased(MouseEvent e) {
				// evento non usato
			}
			public void mousePressed(MouseEvent e) {
				// evento non usato
			}
			public void mouseExited(MouseEvent e) {
				// evento non usato
			}
			public void mouseEntered(MouseEvent e) {
				// evento non usato
			}
			public void mouseClicked(MouseEvent e) {
				 if (e.getClickCount() == 2) {
			        	AttivitaORM attivitaSelezionata = elencoAttivita.getSelectedValue();
			        	visualizzaSelezioneAttivita(attivitaSelezionata);
				 }
			}
		});
		
		elencoImpegni.addMouseListener(new MouseListener() {
			public void mouseReleased(MouseEvent e) {
				// evento non usato
			}
			public void mousePressed(MouseEvent e) {
				// evento non usato
			}
			public void mouseExited(MouseEvent e) {
				// evento non usato
			}
			public void mouseEntered(MouseEvent e) {
				// evento non usato
			}
			public void mouseClicked(MouseEvent e) {
		        if (e.getClickCount() == 2) {
		        	ImpegnoORM impegnoSelezionato = elencoImpegni.getSelectedValue();
		        	visualizzaSelezioneImpegno(impegnoSelezionato);
		        }
			}
		});
		
		
	}
	
	private void inizializzazione() {
		SimpleDateFormat sdf = new SimpleDateFormat(Agenda.PRECISIONE_GIORNO);
		
		labelGiorno = new JLabel(sdf.format(agenda.getDataCorrente()));
		labelGiorno.setFont(new Font(null, Font.BOLD, 20));
		labelGiorno.setForeground(Color.BLACK);

		labelImpegni = new JLabel("Impegni");
		labelImpegni.setFont(new Font(null, Font.BOLD, 15));
		labelImpegni.setForeground(Color.BLUE);

		labelAttivita = new JLabel("Attivita'");
		labelAttivita.setFont(new Font(null, Font.BOLD, 15));
		labelAttivita.setForeground(Color.BLUE);
		
		modelloImpegni = new DefaultListModel<ImpegnoORM>();
		modelloAttivita = new DefaultListModel<AttivitaORM>();
		
		elencoImpegni = new JList<ImpegnoORM>(modelloImpegni);
		elencoImpegni.setCellRenderer(new RenderCella());
		
		elencoAttivita = new JList<AttivitaORM>(modelloAttivita);
		elencoAttivita.setCellRenderer(new RenderCella());
		
		pannelloImpegni = new JScrollPane(elencoImpegni);
		pannelloAttivita = new JScrollPane(elencoAttivita);

		aggiornaListaAttivita(this.agenda.getDataCorrente());
		
		btnAggiungiImpegno = new JButton("Aggiungi impegno");
		btnEliminaImpegno = new JButton("Rimuovi impegno");

		btnAggiungiAttivita = new JButton("Aggiungi attivita");
		btnEliminaAttivita = new JButton("Rimuovi attivita");
	}
	
	public void aggiornaListaImpegni(Date data) {
		
		SimpleDateFormat sdfGiorno = new SimpleDateFormat(Agenda.PRECISIONE_GIORNO);
		String inizioGiorno = sdfGiorno.format(data);
		
		Date primaOraGiorno = new Date();
		try {
			primaOraGiorno = sdfGiorno.parse(inizioGiorno);
		} catch (ParseException e) {
			logger.info(e, e.getMessage());
		}
		
		Calendar tmpCalendario = Calendar.getInstance();
		tmpCalendario.setTime(primaOraGiorno);
		tmpCalendario.add(Calendar.HOUR, 23);
		tmpCalendario.add(Calendar.MINUTE, 59);
		Date ultimaOraGiorno = tmpCalendario.getTime();
		try {
			elencoImpegni.removeAll();
			List<ImpegnoORM> tmpList = agenda.getMemory().restituisciImpegniIn(primaOraGiorno, ultimaOraGiorno);
			
			modelloImpegni = new DefaultListModel<ImpegnoORM>();
			
			for (ImpegnoORM impegnoORM : tmpList) {
				modelloImpegni.addElement(impegnoORM);
			}
			
			elencoImpegni.setModel(modelloImpegni);
		} catch(SQLException e) {
			logger.info(e, e.getMessage());
			JOptionPane.showMessageDialog(this, "Errore nel reperimento delle informazioni.",
					"Errore SQL", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public void aggiornaListaImpegni() {
		
		elencoImpegni.removeAll();
		List<ImpegnoORM> tmpList = agenda.getGestoreImpegniOrm().getImpegniORM();
		modelloImpegni = new DefaultListModel<ImpegnoORM>();
		for (ImpegnoORM impegnoORM : tmpList) {
			modelloImpegni.addElement(impegnoORM);
		}
		
		elencoImpegni.setModel(modelloImpegni);
	}

	public void aggiornaListaAttivita(Date data) {
		try {
			elencoAttivita.removeAll();
			List<AttivitaORM> tmpList = agenda.getMemory().restituisciAttivitaNelMese(data);
			modelloAttivita = new DefaultListModel<AttivitaORM>();

			for (AttivitaORM attivitaORM : tmpList) {
				modelloAttivita.addElement(attivitaORM);
			}
			elencoAttivita.setModel(modelloAttivita);
		} catch (SQLException se) {
			logger.info(se, se.getMessage());
			JOptionPane.showMessageDialog(this, "Errore nel reperimento delle informazioni.",
					"Errore SQL", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public void aggiornaListaAttivita() {
		elencoAttivita.removeAll();
		List<AttivitaORM> tmpList = agenda.getGestoreAttivitaOrm().getAttivitaOrm();
		modelloAttivita = new DefaultListModel<AttivitaORM>();
		
		for (AttivitaORM attivitaORM : tmpList) {
			modelloAttivita.addElement(attivitaORM);
		}
		
		elencoAttivita.setModel(modelloAttivita);
	}
	
	public void aggiornaData() {
		
		SimpleDateFormat sdf = new SimpleDateFormat(Agenda.PRECISIONE_GIORNO);
		this.labelGiorno.setText(sdf.format(this.agenda.getDataCorrente()));
	}
	
	private void richiamaInserisciImpegnoDialog() {
		try {	
			ImpegnoDialog impegnoDialog;
			impegnoDialog = new ImpegnoDialog(framePrincipale, null);
			impegnoDialog.setVisible(true);
		} catch (ImpegnoOrmNonValidoException ioe) {
			logger.info(ioe, ioe.getMessage());
			JOptionPane.showMessageDialog(this, "Si è verificato un problema nel ricavare le categorie: "
					+ ioe.getMessage(), "Errore impegno", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	
	private void richiamaInserisciAttivitaDialog() {
		try {
			AttivitaDialog attivitaDialog;
			attivitaDialog = new AttivitaDialog(framePrincipale, null);
			attivitaDialog.setVisible(true);
		} catch (AttivitaNonValidaException e) {
			logger.info(e, e.getMessage());
			JOptionPane.showMessageDialog(this, "Si è verificato un problema nel ricavare le categorie: "
					+ e.getMessage(), "Errore attivita", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	
	private void rimuoviAttivitaSelezionata(AttivitaORM attivitaSelezionata) throws RimozioneAttivitaException{
		this.agenda.rimuoviAttivita(attivitaSelezionata);
	}
	
	private void rimuoviImpegnoSelezionato(ImpegnoORM impegnoOrm) throws RimozioneImpegnoException {
		this.agenda.rimuoviImpegno(impegnoOrm);
	}
	
	private void visualizzaSelezioneImpegno(ImpegnoORM impegnoSelezionato){
		framePrincipale.visualizzaImpegnoOrmSelezionato(impegnoSelezionato);
	}

	private void visualizzaSelezioneAttivita(AttivitaORM attivitaSelezionata){
		framePrincipale.visualizzaAttivitaOrmSelezionata(attivitaSelezionata);
	}
	
}
