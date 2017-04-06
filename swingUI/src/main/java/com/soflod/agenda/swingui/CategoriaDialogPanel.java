package com.soflod.agenda.swingui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.j256.ormlite.logger.Logger;
import com.j256.ormlite.logger.LoggerFactory;
import com.soflod.agenda.core.Categoria;
import com.soflod.agenda.core.Priorita;
import com.soflod.agenda.core.exception.LimiteEtichettaSuperatoException;
import com.soflod.agenda.core.exception.PrioritaNonValidaException;
import com.soflod.agenda.persistence.Agenda;
import com.soflod.agenda.persistence.CategoriaORM;
import com.soflod.agenda.persistence.exception.AggiuntaCategoriaException;
import com.soflod.agenda.persistence.exception.CategoriaOrmNonValidaException;
import com.soflod.agenda.persistence.exception.ModificaCategoriaException;

public class CategoriaDialogPanel extends JPanel {

	
	private JLabel labelEtichetta;
	private JLabel labelColore;
	private JLabel labelPriorita;
	
	private JTextField campoEtichetta;
	private JColorChooser selettoreColore;
	private JComboBox<Byte> elencoPriorita;
	
	private JButton btnConferma;
	private JButton btnAnnulla;
	
	private FramePrincipale framePrincipale;
	
	private transient  CategoriaORM categoriaOrm;
	private transient Logger logger = LoggerFactory.getLogger(CategoriaDialogPanel.class);
	private transient Agenda agenda;
	
	public CategoriaDialogPanel(FramePrincipale framePrincipale, CategoriaORM categoriaORM) throws CategoriaOrmNonValidaException {

		this.framePrincipale = framePrincipale;
		this.agenda = this.framePrincipale.getAgenda();
		this.categoriaOrm = categoriaORM;
		if (this.categoriaOrm == null)
			inizializzazione();
		else
			inizializzazione(this.categoriaOrm);
		
		setLayout(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints(); 
		
		gc.gridx = 0;
		gc.gridy = 0;
		gc.anchor = GridBagConstraints.WEST;
		gc.fill = GridBagConstraints.BOTH;
		add(labelEtichetta, gc);
		
		gc.gridx = 1;
		gc.gridy = 0;
		gc.fill = GridBagConstraints.BOTH;
		add(campoEtichetta, gc);
		
		gc.gridx = 0;
		gc.gridy = 1;
		gc.fill = GridBagConstraints.BOTH;
		add(labelColore, gc);
		
		gc.gridx = 1;
		gc.gridy = 1;
		gc.anchor = GridBagConstraints.WEST;
		gc.fill = GridBagConstraints.BOTH;
		add(selettoreColore, gc);
		
		gc.gridx = 0;
		gc.gridy = 2;
		gc.fill = GridBagConstraints.BOTH;
		add(labelPriorita, gc);
		
		gc.gridx = 1;
		gc.gridy = 2;
		gc.anchor = GridBagConstraints.WEST;
		gc.fill = GridBagConstraints.BOTH;
		add(elencoPriorita, gc);
		
		gc.gridx = 0;
		gc.gridy = 3;
		gc.fill = GridBagConstraints.BOTH;
		add(btnConferma, gc);
		
		gc.gridx = 1;
		gc.gridy = 3;
		gc.anchor = GridBagConstraints.WEST;
		gc.fill = GridBagConstraints.BOTH;
		add(btnAnnulla, gc);
		
		btnAnnulla.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				distruggiCategoriaDialog();
			}
		});
		if (categoriaOrm == null)
			btnConferma.addActionListener(new ActionListener() {
				
				public void actionPerformed(ActionEvent e) {
					aggiungiCategoriaClick();
				}
			});
		else
			btnConferma.addActionListener(new ActionListener() {
				
				public void actionPerformed(ActionEvent e) {
					modificaCategoriaClick();
				}
			});
	}
	
	private void inizializzazione() {
		
		labelEtichetta = new JLabel("Inserisci etichetta categoria: ");
		labelColore = new JLabel("Seleziona colore: ");
		labelPriorita = new JLabel("Seleziona priorita associata: ");
		
		campoEtichetta = new JTextField("Inserisci etichetta categoria");
		selettoreColore = new JColorChooser(Categoria.getColoreDefault());
		elencoPriorita = preparaComboBoxPriorita();
		
		btnConferma = new JButton("Conferma");
		btnAnnulla = new JButton("Annulla");
	}
	
	private void inizializzazione(CategoriaORM categoriaORM) throws CategoriaOrmNonValidaException {
		
		Categoria categoria = categoriaORM.restituisciCategoria();
		
		labelEtichetta = new JLabel("Inserisci etichetta categoria: ");
		labelColore = new JLabel("Seleziona colore: ");
		labelPriorita = new JLabel("Seleziona priorita associata: ");
		
		campoEtichetta = new JTextField(categoria.getEtichetta());
		selettoreColore = new JColorChooser(categoria.getColore());
		elencoPriorita = preparaComboBoxPriorita();
		elencoPriorita.setSelectedItem(categoria.getPriorita().getValorePriorita());
		
		btnConferma = new JButton("Conferma");
		btnAnnulla = new JButton("Annulla");
	}
	
	private JComboBox<Byte> preparaComboBoxPriorita() {
		JComboBox<Byte> tmp = new JComboBox<Byte>();
		for (Byte i = 1; i <= 10; i++) {
			tmp.addItem(i);
		}
		return tmp;
	}
	
	private void distruggiCategoriaDialog(){
		JDialog dialogParent = (JDialog) getRootPane().getParent();
		dialogParent.dispose();
	}
	
	
	private void aggiungiCategoriaClick() {
		
		Categoria tmp = preparaCategoria();
		
		if (tmp != null) {
			
			try {
				agenda.aggiungiCategoria(tmp);
				distruggiCategoriaDialog();
				aggiornaListaCategorie();
			} catch (AggiuntaCategoriaException e) {
				logger.info(e, e.getMessage());
				JOptionPane.showMessageDialog(this, "Errore nell'inserimento della categoria nel db.", 
						"Errore inserimento categoria", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	private void aggiornaListaCategorie(){
		framePrincipale.getPannelloCategorie().aggiornaListaCategorie();
	}
	
	private void modificaCategoriaClick() {
		Categoria tmp = preparaCategoria();
		
		if (tmp != null) {
			
			CategoriaORM tmpCategoria = new CategoriaORM();
			tmpCategoria.setEtichetta(tmp.getEtichetta());
			tmpCategoria.setColore(tmp.getColore());
			tmpCategoria.setPriorita(tmp.getPriorita());
			
			try {
				agenda.aggiornaCategoria(categoriaOrm, tmpCategoria);
				aggiornaListaCategorieModificate();
			} catch (ModificaCategoriaException e) {
				logger.info(e, e.getMessage());
				JOptionPane.showMessageDialog(this, "Errore nella modifica della categoria nel db.", 
						"Errore modifica categoria", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	private void aggiornaListaCategorieModificate(){
		aggiornaListaCategorie();
		framePrincipale.visualizzaCarta(FramePrincipale.VISTA_CATEGORIE);
		distruggiCategoriaDialog();
	}
	
	private Categoria preparaCategoria() {
		
		String etichetta = campoEtichetta.getText();
		Color colore = selettoreColore.getColor();
		try {
			Priorita priorita = new Priorita((Byte)elencoPriorita.getSelectedItem());
			return new Categoria(etichetta, colore, priorita);
		} catch (PrioritaNonValidaException e) {
			logger.info(e, e.getMessage());
			JOptionPane.showMessageDialog(this, "Errore nell'instanziazione della priorità.", 
					"Errore priorita", JOptionPane.ERROR_MESSAGE);
		} catch (LimiteEtichettaSuperatoException e) {
			logger.info(e, e.getMessage());
			JOptionPane.showMessageDialog(this, "Si è superato il massimo numero di caratteri per l'etichetta.", 
					"Errore etichetta", JOptionPane.ERROR_MESSAGE);
		}
		return null;
	}
}
