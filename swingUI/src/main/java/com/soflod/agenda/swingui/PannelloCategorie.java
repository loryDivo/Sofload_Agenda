package com.soflod.agenda.swingui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
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
import com.soflod.agenda.persistence.CategoriaORM;
import com.soflod.agenda.persistence.exception.CategoriaOrmNonValidaException;
import com.soflod.agenda.persistence.exception.RimozioneCategoriaException;

public class PannelloCategorie extends JPanel {

	
	private FramePrincipale framePrincipale;
	
	private JLabel labelCategorie;
	private JList<CategoriaORM> listaCategorie;
	private DefaultListModel<CategoriaORM> modelloCategorie;
	private JScrollPane scrollCategorie;
	
	private JButton btnAggiungiCategoria;
	private JButton btnRimuoviCategoria;
	
	private transient Logger logger = LoggerFactory.getLogger(PannelloCategorie.class);
	private transient Agenda agenda;
	
	public PannelloCategorie(FramePrincipale framePrincipale) {
		this.framePrincipale = framePrincipale;
		this.agenda = this.framePrincipale.getAgenda();
		
		inizializzazione();
		
		setLayout(new GridBagLayout());

		GridBagConstraints gc = new GridBagConstraints(); 
		
		gc.gridx = 1;
		gc.gridy = 1;
		gc.anchor = GridBagConstraints.WEST;
		gc.fill = GridBagConstraints.BOTH;
		add(labelCategorie, gc);
		
		gc.gridx = 1;
		gc.gridy = 2;
		gc.weightx = 1.0;
		gc.weighty = 0.7;
		gc.fill = GridBagConstraints.BOTH;
		gc.anchor = GridBagConstraints.CENTER;
		add(scrollCategorie, gc);
		
		gc.gridx = 1;
		gc.gridy = 3;
		gc.weightx = 0.1;
		gc.weighty = 0.1;
		gc.fill = GridBagConstraints.NONE;
		gc.anchor = GridBagConstraints.WEST;
		add(btnAggiungiCategoria, gc);
		
		gc.gridx = 1;
		gc.gridy = 3;
		gc.weightx = 0.1;
		gc.weighty = 0.1;
		gc.fill = GridBagConstraints.NONE;
		gc.anchor = GridBagConstraints.EAST;
		add(btnRimuoviCategoria, gc);
	
		btnAggiungiCategoria.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				richiamaAggiuntaCategoria();
			}
		});
		
		btnRimuoviCategoria.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				CategoriaORM categoriaSelezionata = listaCategorie.getSelectedValue();
				if(categoriaSelezionata != null){
					rimuoviCategoria(categoriaSelezionata);
					aggiornaListaCategorie();
				} else {
					JOptionPane.showMessageDialog(getParent(), "Nessuna attivita selezionata.",
							"Selezione attivita", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		
		listaCategorie.addMouseListener(new MouseListener() {
			
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
					CategoriaORM categoriaSelezionata = listaCategorie.getSelectedValue();
					visualizzaCategoriaSelezionata(categoriaSelezionata);
				}
			}
		});
	}
	
	private void rimuoviCategoria(CategoriaORM categoriaOrm) {
		try {
			this.agenda.rimuoviCategoria(categoriaOrm);
		} catch (RimozioneCategoriaException e) {
			logger.info(e, e.getMessage());
			JOptionPane.showMessageDialog(getParent(),
					"Errore nell'eliminazione della categoria selezionata: " + e.getMessage(),
					" Errore eliminazione categoria", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private void visualizzaCategoriaSelezionata(CategoriaORM categoriaSelezionata){
		framePrincipale.visualizzaCategoriaOrmSelezionata(categoriaSelezionata);
	}
	
	private void inizializzazione() {
		labelCategorie = new JLabel("Lista categorie disponibili:");
		modelloCategorie = preparaModelloCategorie();
		
		listaCategorie = new JList<CategoriaORM>(modelloCategorie);
		scrollCategorie = new JScrollPane(listaCategorie);
		
		btnAggiungiCategoria = new JButton("Aggiungi categoria");
		btnRimuoviCategoria = new JButton("Rimuovi categoria");
	}
	
	private DefaultListModel<CategoriaORM> preparaModelloCategorie() {
		DefaultListModel<CategoriaORM> tmpModello = new DefaultListModel<CategoriaORM>();
		
		List<CategoriaORM> tmpList = agenda.getGestoreCategorieOrm().getCategorieOrm();
		
		
		for (CategoriaORM categoriaORM : tmpList) {
			tmpModello.addElement(categoriaORM);
		}
		
		return tmpModello;
	}

	private void richiamaAggiuntaCategoria() {
		try {
			CategoriaDialog categoriaDialog = new CategoriaDialog(framePrincipale, null);
			categoriaDialog.setVisible(true);
		} catch (CategoriaOrmNonValidaException e) {
			logger.info(e, e.getMessage());
			JOptionPane.showMessageDialog(this, "Errore nell'instaziare la categoria.",
					"Errore inserimento categoria", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public void aggiornaListaCategorie(){
		listaCategorie.removeAll();
		List<CategoriaORM> tmpList = agenda.getGestoreCategorieOrm().getCategorieOrm();
		modelloCategorie = new DefaultListModel<CategoriaORM>();
		
		for (CategoriaORM categoriaORM : tmpList) {
			modelloCategorie.addElement(categoriaORM);
		}
		
		listaCategorie.setModel(modelloCategorie);
	}
	
	public void richiamaModificaCategoriaOrm(CategoriaORM nuovaCategoriaOrm){
		try {
			CategoriaDialog categoriaDialog;
			categoriaDialog = new CategoriaDialog(framePrincipale, nuovaCategoriaOrm);
			categoriaDialog.setVisible(true);
		} catch (CategoriaOrmNonValidaException e) {
			logger.info(e, e.getMessage());
			JOptionPane.showMessageDialog(this, "Errore nella modifica della categoria.",
					"Errore modifica categoria", JOptionPane.ERROR_MESSAGE);
		}
}
	
}
