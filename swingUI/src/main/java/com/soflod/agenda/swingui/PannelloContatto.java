package com.soflod.agenda.swingui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.j256.ormlite.logger.Logger;
import com.j256.ormlite.logger.LoggerFactory;
import com.soflod.agenda.core.Categoria;
import com.soflod.agenda.core.Contatto;
import com.soflod.agenda.core.Email;
import com.soflod.agenda.core.Telefono;
import com.soflod.agenda.persistence.ContattoORM;
import com.soflod.agenda.persistence.exception.ContattoOrmNonValidoException;

public class PannelloContatto extends JPanel{

	private FramePrincipale framePrincipale;
	
	private JLabel nome;
	private JLabel labelNome;
	
	private JLabel cognome;
	private JLabel labelCognome;
	
	private JLabel labelEmails;
	private JLabel labelTelefoni;
	private JLabel labelcategorie;
	
	private DefaultListModel<Email> modelloEmail;
	private DefaultListModel<Telefono> modelloTelefono;
	private DefaultListModel<Categoria> modelloCategoria;
	
	private JScrollPane scrollEmail;
	private JScrollPane scrollTelefono;
	private JScrollPane scrollCategoriaORM;
	
	
	private JButton btnModifica;
	
	private JButton btnTornaIndietro;
	
	private transient ContattoORM contattoOrm;
	private transient Logger logger = LoggerFactory.getLogger(PannelloContatto.class);
	
	public PannelloContatto(FramePrincipale framePrincipale, ContattoORM contattoOrm) {
		this.framePrincipale = framePrincipale;
		this.contattoOrm = contattoOrm;
		
		try {
			
			Contatto contatto = contattoOrm.restituisciContatto(framePrincipale.getAgenda().getMemory().getContattiCategorieDAO(), framePrincipale.getAgenda().getMemory().getCategorieDAO());
			
			inizializzazioneEtichette(contatto);
			
			setLayout(new GridBagLayout());
			GridBagConstraints gc = new GridBagConstraints();

			
			gc.gridy = 0;
			gc.gridx = 0;
			gc.anchor = GridBagConstraints.WEST;
			gc.fill = GridBagConstraints.BOTH;
			gc.weightx = 0.5;
			gc.weighty = 0.5;
			add(nome, gc);
			
			gc.gridy = 0;
			gc.gridx = 1;
			gc.weightx = 0.5;
			gc.weighty = 0.5;
			gc.fill = GridBagConstraints.BOTH;
			gc.anchor = GridBagConstraints.CENTER;
			add(labelNome, gc);
			
			gc.gridy = 1;
			gc.gridx = 0;
			gc.anchor = GridBagConstraints.WEST;
			gc.fill = GridBagConstraints.BOTH;
			add(cognome, gc);
			
			gc.gridy = 1;
			gc.gridx = 1;
			gc.anchor = GridBagConstraints.CENTER;
			gc.fill = GridBagConstraints.BOTH;
			add(labelCognome, gc);
			
			gc.gridy = 2;
			gc.gridx = 0;
			gc.anchor = GridBagConstraints.WEST;
			gc.fill = GridBagConstraints.BOTH;
			add(labelEmails, gc);
			
			gc.gridy = 2;
			gc.gridx = 1;
			gc.fill = GridBagConstraints.BOTH;
			gc.anchor = GridBagConstraints.CENTER;
			add(scrollEmail, gc);
			
			
			gc.gridy = 3;
			gc.gridx = 0;
			gc.anchor = GridBagConstraints.WEST;
			gc.fill = GridBagConstraints.BOTH;
			add(labelTelefoni, gc);
			
			gc.gridy = 3;
			gc.gridx = 1;
			gc.fill = GridBagConstraints.BOTH;
			gc.anchor = GridBagConstraints.CENTER;
			add(scrollTelefono, gc);
			
			gc.gridy = 4;
			gc.gridx = 0;
			gc.anchor = GridBagConstraints.WEST;
			gc.fill = GridBagConstraints.BOTH;
			add(labelcategorie, gc);
			
			gc.gridy = 4;
			gc.gridx = 1;
			gc.fill = GridBagConstraints.BOTH;
			gc.anchor = GridBagConstraints.CENTER;
			add(scrollCategoriaORM, gc);
			
			gc.gridy = 5;
			gc.gridx = 0;
			gc.weighty = 0.1;
			gc.weightx = 0.1;
			gc.anchor = GridBagConstraints.CENTER;
			gc.fill = GridBagConstraints.NONE;
			add(btnTornaIndietro, gc);
			
			gc.gridy = 5;
			gc.gridx = 1;
			gc.anchor = GridBagConstraints.CENTER;
			gc.fill = GridBagConstraints.NONE;
			add(btnModifica, gc);
			
		} catch(ContattoOrmNonValidoException conve){
			logger.info(conve, conve.getMessage());
		}
		
		btnModifica.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				richiamaModificaContattoOrm();
			}
		});
		
		
	}
	
	private void inizializzazioneEtichette(Contatto contatto){
		nome = new JLabel("Nome");
		labelNome = new JLabel(contatto.getNome());
		
		cognome = new JLabel("Cognome");
		labelCognome = new JLabel(contatto.getCognome());
		
		labelEmails = new JLabel("Email");
		labelcategorie = new JLabel("Categorie");
		labelTelefoni = new JLabel("Telefoni");
		
		btnModifica = new JButton("Modifica");
		btnTornaIndietro = new JButton("Indietro");
	
		inserisciEmail(contatto);
		inserisciTelefoni(contatto);
		inserisciCategorie(contatto);
		
		JList<Email> listaEmail;
		JList<Telefono> listaTelefoni;
		JList<Categoria> listaCategorie;
		
		listaEmail = new JList<Email>(modelloEmail);
		listaTelefoni = new JList<Telefono>(modelloTelefono);
		listaCategorie = new JList<Categoria>(modelloCategoria);
		
		scrollEmail = new JScrollPane(listaEmail);
		scrollTelefono = new JScrollPane(listaTelefoni);
		scrollCategoriaORM = new JScrollPane(listaCategorie);
	
		
	}

	private void inserisciEmail(Contatto contatto){
		modelloEmail = new DefaultListModel<Email>();
		for (Email email : contatto.getEmails()) {
			modelloEmail.addElement(email);
		}
	}
	
	private void inserisciTelefoni(Contatto contatto) {
		modelloTelefono = new DefaultListModel<Telefono>();
		for (Telefono telefono : contatto.getTelefoni()) {
			modelloTelefono.addElement(telefono);
		}

	}
	
	private void inserisciCategorie(Contatto contatto){
		modelloCategoria = new DefaultListModel<Categoria>();
		for (Categoria categoria : contatto.getCategorie()) {
			modelloCategoria.addElement(categoria);
		}
	}
	
	private void richiamaModificaContattoOrm(){
		framePrincipale.getPannelloRubrica().richiamaModificaContattoOrm(contattoOrm);
	}
}
