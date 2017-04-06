package com.soflod.agenda.swingui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.soflod.agenda.core.Categoria;
import com.soflod.agenda.persistence.CategoriaORM;
import com.soflod.agenda.persistence.exception.CategoriaOrmNonValidaException;

public class PannelloCategoria extends JPanel{
private FramePrincipale framePrincipale;
	
	
	private JLabel etichetta;
	private JLabel labelEletichetta;
	
	private JLabel colore;
	private JLabel labelColore;
	
	private JLabel priorita;
	private JLabel labelPriorita;
	
	
	private JButton btnModifica;
	
	private JButton btnTornaIndietro;

	private transient CategoriaORM categoriaOrm;

	public PannelloCategoria(FramePrincipale framePrincipale, CategoriaORM categoriaOrm) throws CategoriaOrmNonValidaException {

		this.framePrincipale = framePrincipale;
		this.categoriaOrm = categoriaOrm;

		Categoria categoria = categoriaOrm.restituisciCategoria();

		inizializzazione(categoria);

		setLayout(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();

		gc.gridy = 0;
		gc.gridx = 0;
		gc.anchor = GridBagConstraints.WEST;
		gc.fill = GridBagConstraints.BOTH;
		gc.weightx = 0.5;
		gc.weighty = 0.5;
		add(labelEletichetta, gc);

		gc.gridy = 0;
		gc.gridx = 1;
		gc.weightx = 0.5;
		gc.weighty = 0.5;
		gc.fill = GridBagConstraints.BOTH;
		gc.anchor = GridBagConstraints.CENTER;
		add(etichetta, gc);

		gc.gridy = 1;
		gc.gridx = 0;
		gc.anchor = GridBagConstraints.WEST;
		gc.fill = GridBagConstraints.BOTH;
		add(labelColore, gc);

		gc.gridy = 1;
		gc.gridx = 1;
		gc.anchor = GridBagConstraints.CENTER;
		gc.fill = GridBagConstraints.BOTH;
		add(colore, gc);

		gc.gridy = 2;
		gc.gridx = 0;
		gc.anchor = GridBagConstraints.WEST;
		gc.fill = GridBagConstraints.BOTH;
		add(priorita, gc);

		gc.gridy = 2;
		gc.gridx = 1;
		gc.fill = GridBagConstraints.BOTH;
		gc.anchor = GridBagConstraints.CENTER;
		add(labelPriorita, gc);

		gc.gridy = 3;
		gc.gridx = 0;
		gc.anchor = GridBagConstraints.WEST;
		gc.fill = GridBagConstraints.NONE;
		add(btnModifica, gc);

		gc.gridy = 3;
		gc.gridx = 1;
		gc.fill = GridBagConstraints.NONE;
		gc.anchor = GridBagConstraints.CENTER;
		add(btnTornaIndietro, gc);

		btnModifica.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				richiamaModificaCategoria();
				
			}
		});
		
		btnTornaIndietro.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tornaIndietro();
			}
		});
		
	}
	
	private void richiamaModificaCategoria(){
		framePrincipale.getPannelloCategorie().richiamaModificaCategoriaOrm(categoriaOrm);
	}
	
	private void tornaIndietro(){
		framePrincipale.visualizzaCategorie();
	}
	
	private void inizializzazione(Categoria categoria){
		labelEletichetta = new JLabel("Etichetta: ");
		etichetta = new JLabel(categoria.getEtichetta());
		
		labelColore = new JLabel("Colore: ");
		colore = new JLabel(categoria.getColore().toString());
		
		labelPriorita = new JLabel("Priorita: ");
		Byte tmp = categoria.getPriorita().getValorePriorita();
		priorita = new JLabel(tmp.toString());
		
		btnModifica = new JButton("Modifica");
		btnTornaIndietro = new JButton("Indietro");
	}
	

}
