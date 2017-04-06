package com.soflod.agenda.swingui;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;


import com.j256.ormlite.logger.Logger;
import com.j256.ormlite.logger.LoggerFactory;
import com.soflod.agenda.core.Categoria;
import com.soflod.agenda.core.Contatto;
import com.soflod.agenda.core.Email;
import com.soflod.agenda.core.Telefono;
import com.soflod.agenda.core.exception.CategorieNonValideException;
import com.soflod.agenda.core.exception.EmailNonValidaException;
import com.soflod.agenda.core.exception.EmailsNonValideException;
import com.soflod.agenda.core.exception.NumeroTelefonoNonValidoException;
import com.soflod.agenda.core.exception.TelefoniNonValidiException;
import com.soflod.agenda.persistence.Agenda;
import com.soflod.agenda.persistence.CategoriaORM;
import com.soflod.agenda.persistence.ContattoORM;
import com.soflod.agenda.persistence.exception.AggiuntaContattoException;
import com.soflod.agenda.persistence.exception.CategoriaOrmNonValidaException;
import com.soflod.agenda.persistence.exception.ContattoOrmNonValidoException;
import com.soflod.agenda.persistence.exception.ModificaContattoException;

public class ContattoDialogPanel extends JPanel{

	private PannelloRubrica pannelloRubrica;
	
	
	private JLabel labelNome;
	private JLabel labelCognome;
	private JLabel labelEmail;
	private JLabel labelTelefoni;
	private JLabel labelCategorie;
	
	
	private JTextField campoNome;
	private JTextField campoCognome;
	private JTextField campoEmail;
	private JTextField campoTelefono;
	
	private JList<Telefono> elencoTelefoni;
	private JList<Email> elencoEmail;
	private JList<CategoriaORM> elencoCategorieSelezionate;
	
	private JComboBox<CategoriaORM> elencoCategorie;
	
	private DefaultListModel<Telefono> modelloTelefoni;
	private DefaultListModel<Email> modelloEmail;
	private DefaultListModel<CategoriaORM> modelloCategoriaORM;
	
	private JScrollPane pannelloTelefoni;
	private JScrollPane pannelloEmail;
	private JScrollPane pannelloCategorieORM;
	
	private FramePrincipale framePrincipale;

	private JButton btnConferma;
	private JButton btnAnnulla;
	private JButton btnAggiungiEmail;
	private JButton btnAggiungiTelefono;
	private JButton btnAggiungiCategoria;
	
	private transient ContattoORM contattoOrm;
	private transient Agenda agenda;
	private transient Logger logger = LoggerFactory.getLogger(ContattoDialogPanel.class);
	
	
	public ContattoDialogPanel(FramePrincipale framePrincipale, ContattoORM contattoORM) {
		
		this.framePrincipale = framePrincipale;
		this.agenda = framePrincipale.getAgenda();
		this.contattoOrm = contattoORM;
		this.pannelloRubrica = framePrincipale.getPannelloRubrica();
		inizializzaEtichette();
		if (contattoORM == null) {
			inizializza();
		} else {
			inizializza(contattoORM);
		}
		
		
		setLayout(new GridBagLayout());

		GridBagConstraints gc = new GridBagConstraints(); 
		
		gc.gridx = 0;
		gc.gridy = 0;
		gc.anchor = GridBagConstraints.WEST;
		gc.fill = GridBagConstraints.BOTH;
		add(labelNome, gc);
		
		gc.gridx = 1;
		gc.gridy = 0;
		gc.fill = GridBagConstraints.BOTH;
		add(campoNome, gc);
		
		gc.gridx = 0;
		gc.gridy = 1;
		gc.fill = GridBagConstraints.BOTH;
		add(labelCognome, gc);
		
		gc.gridx = 1;
		gc.gridy = 1;
		gc.anchor = GridBagConstraints.WEST;
		gc.fill = GridBagConstraints.BOTH;
		add(campoCognome, gc);
		
		gc.gridx = 0;
		gc.gridy = 2;
		gc.fill = GridBagConstraints.BOTH;
		add(labelEmail, gc);
		
		gc.gridx = 1;
		gc.gridy = 2;
		gc.anchor = GridBagConstraints.WEST;
		gc.fill = GridBagConstraints.BOTH;
		add(campoEmail, gc);
		
		gc.gridx = 1;
		gc.gridy = 3;
		gc.anchor = GridBagConstraints.WEST;
		gc.fill = GridBagConstraints.BOTH;
		add(pannelloEmail, gc);
		
		gc.gridx = 2;
		gc.gridy = 2;
		gc.anchor = GridBagConstraints.WEST;
		gc.fill = GridBagConstraints.BOTH;
		add(btnAggiungiEmail, gc);
		
		gc.gridx = 0;
		gc.gridy = 4;
		gc.fill = GridBagConstraints.BOTH;
		add(labelTelefoni, gc);
		
		gc.gridx = 1;
		gc.gridy = 4;
		gc.fill = GridBagConstraints.BOTH;
		add(campoTelefono, gc);
		
		gc.gridx = 1;
		gc.gridy = 5;
		gc.fill = GridBagConstraints.BOTH;
		add(pannelloTelefoni, gc);
		
		gc.gridx = 2;
		gc.gridy = 4;
		gc.anchor = GridBagConstraints.WEST;
		gc.fill = GridBagConstraints.BOTH;
		add(btnAggiungiTelefono, gc);
		
		
		gc.gridx = 0;
		gc.gridy = 6;
		gc.fill = GridBagConstraints.BOTH;
		add(labelCategorie, gc);
		
		gc.gridx = 1;
		gc.gridy = 6;
		gc.fill = GridBagConstraints.BOTH;
		add(elencoCategorie, gc);
	
		gc.gridx = 1;
		gc.gridy = 7;
		gc.fill = GridBagConstraints.BOTH;
		add(pannelloCategorieORM, gc);
		
		gc.gridx = 2;
		gc.gridy = 6;
		gc.anchor = GridBagConstraints.WEST;
		gc.fill = GridBagConstraints.BOTH;
		add(btnAggiungiCategoria, gc);
		
		gc.gridx = 0;
		gc.gridy = 9;
		gc.fill = GridBagConstraints.NONE;
		add(btnConferma, gc);
		
		gc.gridx = 1;
		gc.gridy = 9;
		gc.fill = GridBagConstraints.NONE;
		add(btnAnnulla, gc);
		
		btnAggiungiEmail.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				try {
					Email emailCampo = new Email(campoEmail.getText());
					if (verificaDuplicatiComboBox(emailCampo)) {
						modelloEmail.addElement(emailCampo);
						aggiornaListe(emailCampo);
					}
					else{
						JOptionPane.showMessageDialog(getParent(), "Email duplicata inserita", "Email duplicata",
								JOptionPane.ERROR_MESSAGE);
					}
				} catch (EmailNonValidaException e1) {
					logger.info(e1, e1.getMessage());
					JOptionPane.showMessageDialog(getParent(), "Email inserita in modo errato", "Email errata",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		
		btnAggiungiTelefono.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				aggiungiTelefono();
			}
		});
		
		btnAggiungiCategoria.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				CategoriaORM categoriaSelezionata = (CategoriaORM)elencoCategorie.getSelectedItem();
				if(verificaDuplicatiComboBox(categoriaSelezionata)){
					modelloCategoriaORM.addElement(categoriaSelezionata);
					aggiornaListe(categoriaSelezionata);
				}
				else{
					JOptionPane.showMessageDialog(getParent(), "Categoria duplicata selezionata", "Categoria duplicata ",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		
		if(contattoOrm == null){
			btnConferma.addActionListener(new ActionListener() {
				
				public void actionPerformed(ActionEvent e) {
					aggiungiContattoClick();
				}
			});
		}
		else{
			btnConferma.addActionListener(new ActionListener() {
				
				public void actionPerformed(ActionEvent e) {
					modificaContattoClick();
				}
			});
		}
	
		campoNome.addMouseListener(new MouseListener() {
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
				campoNome.setText("");
			}
		});
		
		campoCognome.addMouseListener(new MouseListener() {
			
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
				campoCognome.setText("");
			}
		});
		
		campoEmail.addMouseListener(new MouseListener() {
			
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
				campoEmail.setText("");
			}
		});
		
		campoTelefono.addMouseListener(new MouseListener() {
			
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
				campoTelefono.setText("");
			}
		});
	}

	private void inizializza(){
		
		campoNome = new JTextField("Inserisci nome", 40);
		campoCognome = new JTextField("Inserisci cognome", 40);
		campoEmail = new JTextField("Inserisci email", 40);
		campoTelefono = new JTextField("Inserisci telefono", 40);
		
		btnConferma = new JButton("Conferma");
		btnAnnulla = new JButton("Annulla");
		btnAggiungiEmail = new JButton("Aggiungi email");
		btnAggiungiCategoria = new JButton("Aggiungi categoria");
		btnAggiungiTelefono = new JButton("Aggiungi telefono");
		
		elencoCategorie = new JComboBox<CategoriaORM>();
		
		modelloEmail = new DefaultListModel<Email>();
		modelloTelefoni = new DefaultListModel<Telefono>();
		modelloCategoriaORM = new DefaultListModel<CategoriaORM>();
		
		elencoEmail  = new JList<Email>(modelloEmail);
		elencoTelefoni = new JList<Telefono>(modelloTelefoni);
		elencoCategorieSelezionate = new JList<CategoriaORM>(modelloCategoriaORM);
		
		pannelloEmail = new JScrollPane(elencoEmail);
		pannelloTelefoni = new JScrollPane(elencoTelefoni);
		pannelloCategorieORM = new JScrollPane(elencoCategorieSelezionate);
		
		aggiornaElencoCategorie();
		
	}
	
	private void inizializza(ContattoORM contatto){

		campoNome = new JTextField(contatto.getNome(), 40);
		campoCognome = new JTextField(contatto.getCognome(), 40);
		campoEmail = new JTextField("Inserisci email", 40);
		campoTelefono = new JTextField("Inserisci telefono", 40);

		btnConferma = new JButton("Conferma");
		btnAnnulla = new JButton("Annulla");
		btnAggiungiEmail = new JButton("Aggiungi email");
		btnAggiungiCategoria = new JButton("Aggiungi categoria");
		btnAggiungiTelefono = new JButton("Aggiungi telefono");

		elencoCategorie = new JComboBox<CategoriaORM>();

		modelloEmail = new DefaultListModel<Email>();
		modelloTelefoni = new DefaultListModel<Telefono>();
		modelloCategoriaORM = new DefaultListModel<CategoriaORM>();

		inserisciEmail(contatto);
		inserisciTelefoni(contatto);
		inserisciCategorie(contatto);
		
		elencoEmail = new JList<Email>(modelloEmail);
		elencoTelefoni = new JList<Telefono>(modelloTelefoni);
		elencoCategorieSelezionate = new JList<CategoriaORM>(modelloCategoriaORM);

		pannelloEmail = new JScrollPane(elencoEmail);
		pannelloTelefoni = new JScrollPane(elencoTelefoni);
		pannelloCategorieORM = new JScrollPane(elencoCategorieSelezionate);
		
		aggiornaElencoCategorie();
		
	}
	
	private void inizializzaEtichette(){
		labelNome = new JLabel("Nome");
		labelCognome = new JLabel("Cognome");
		labelEmail = new JLabel("Email");
		labelTelefoni = new JLabel("Telefono");
		labelCategorie = new JLabel("Categorie");
		labelNome.setFont(new Font(null, Font.BOLD, 15));
		labelCognome.setFont(new Font(null, Font.BOLD, 15));
		labelEmail.setFont(new Font(null, Font.BOLD, 15));
		labelTelefoni.setFont(new Font(null, Font.BOLD, 15));
		labelCategorie.setFont(new Font(null, Font.BOLD, 15));
	}
	
	private void aggiungiTelefono() {
		try {
			Telefono telefonoCampo = new Telefono(campoTelefono.getText());
			if (verificaDuplicatiComboBox(telefonoCampo)) {
				modelloTelefoni.addElement(telefonoCampo);
				aggiornaListe(telefonoCampo);
			}
			else{
				
				JOptionPane.showMessageDialog(getParent(), "Telefono duplicato inserito", "Telefono duplicato",
						JOptionPane.ERROR_MESSAGE);
			}
		} catch (NumeroTelefonoNonValidoException e1) {
			logger.info(e1, e1.getMessage());
			JOptionPane.showMessageDialog(getParent(), "Telefono inserito in modo errato", "Telefono errato",
					JOptionPane.ERROR_MESSAGE);
		}
	}
	
	
	private void inserisciEmail(ContattoORM contatto){
		try {
			modelloEmail = new DefaultListModel<Email>();
			String emails = contatto.getEmails();
			List<Email> listaEmail;
			listaEmail = contatto.convertiEmails(emails);
			for (Email email : listaEmail) {
				modelloEmail.addElement(email);
			}
		} catch (ContattoOrmNonValidoException e) {
			logger.info(e, e.getMessage());
		}
	}
	
	private void inserisciTelefoni(ContattoORM contatto) {
		try {
			modelloTelefoni = new DefaultListModel<Telefono>();
			String telefoni = contatto.getTelefoni();
			List<Telefono> listaTelefoni;
			listaTelefoni = contatto.convertiTelefoni(telefoni);
			for (Telefono telefono : listaTelefoni) {
				modelloTelefoni.addElement(telefono);
			}
		} catch (ContattoOrmNonValidoException e) {
			logger.info(e, e.getMessage());
		}
	}
	
	private void inserisciCategorie(ContattoORM contatto){
		try {
			modelloCategoriaORM = new DefaultListModel<CategoriaORM>();
			List<CategoriaORM> listaCategorieOrm;
			listaCategorieOrm = agenda.getMemory().restituisciCategorieORMContatto(contatto);
			for (CategoriaORM categoriaORM : listaCategorieOrm) {
				modelloCategoriaORM.addElement(categoriaORM);
			}
		} catch (SQLException e) {
			logger.info(e, e.getMessage());
		}
	}
	

	private void aggiornaElencoCategorie(){
		elencoCategorie.removeAll();
		List<CategoriaORM> tmpList = agenda.getGestoreCategorieOrm().getCategorieOrm();
		
		for (CategoriaORM categoriaORM : tmpList) {
			elencoCategorie.addItem(categoriaORM);
		}
	}
	
	private void aggiungiContattoClick(){
		Contatto contatto = preparaContatto();
		if (contatto != null) {
			try {
				agenda.aggiungiContatto(contatto);
				aggiornaListaContatti();
			} catch (AggiuntaContattoException e) {
				logger.info(e, e.getMessage());
				JOptionPane.showMessageDialog(getParent(), e.getMessage(), "Errore inserimento contatto",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	private void modificaContattoClick(){
		try {
			Contatto contattoDiModifica = preparaContatto();
			ContattoORM contattoDiModificaORM = new ContattoORM();
			contattoDiModificaORM.setNome(contattoDiModifica.getNome());
			contattoDiModificaORM.setCognome(contattoDiModifica.getCognome());
			contattoDiModificaORM.setEmails(contattoDiModifica.getEmails());
			contattoDiModificaORM.setTelefoni(contattoDiModifica.getTelefoni());
			List<CategoriaORM> categorieORM = new ArrayList<CategoriaORM>(contattoDiModifica.getCategorie().size());
			
			for (Categoria categoria : contattoDiModifica.getCategorie()) {
				CategoriaORM categoriaORM;
				categoriaORM = new CategoriaORM(categoria, agenda.getMemory().getCategorieDAO());
				categorieORM.add(categoriaORM);
			}
			contattoDiModificaORM.setCategorieORM(categorieORM);
			
			agenda.getRubrica().modificaContattoORM(contattoOrm, contattoDiModificaORM);
			aggiornaListaContattiModificati();
			
		} catch (IOException e) {
			logger.info(e, e.getMessage());
		} catch (ModificaContattoException e) {
			logger.info(e, e.getMessage());
		}
	}
	
	private void aggiornaListaContattiModificati(){
		pannelloRubrica.aggiornaListaContatti();
		framePrincipale.visualizzaCarta(FramePrincipale.VISTA_RUBRICA);
		distruggiContattoDialog();
	}
	
	private Contatto preparaContatto() {
		String nome = campoNome.getText();
		String cognome = campoCognome.getText();

		if (modelloCategoriaORM.size() != 0) {
			Contatto tmp;
			List<CategoriaORM> categorieOrmDaInserire;
			if ("".equals(nome)) {
				JOptionPane.showMessageDialog(getParent(), "Nome non inserito", "Nome errato",
						JOptionPane.ERROR_MESSAGE);
			}
			tmp = new Contatto.Builder(nome).build();
			if (!"".equals(cognome)) {
				tmp.setCognome(cognome);
			}
			if (elencoEmail.getModel().getSize() != 0) {
				List<Email> emails = new ArrayList<Email>();
				for (int i = 0; i < elencoEmail.getModel().getSize(); i++) {
					Email emailSelezionata = elencoEmail.getModel().getElementAt(i);
					emails.add(emailSelezionata);
				}
				try {
					tmp.setEmails(emails);
				} catch (EmailsNonValideException e) {
					logger.info(e, e.getMessage());
					JOptionPane.showMessageDialog(getParent(), "Email all'interno non valide", "Errore email",
							JOptionPane.ERROR_MESSAGE);
				}
			}
			if (elencoTelefoni.getModel().getSize() != 0) {
				List<Telefono> telefoni = new ArrayList<Telefono>();
				for (int i = 0; i < elencoTelefoni.getModel().getSize(); i++) {
					Telefono telefonoSelezionato = elencoTelefoni.getModel().getElementAt(i);
					telefoni.add(telefonoSelezionato);
				}
				try {
					tmp.setTelefoni(telefoni);
				} catch (TelefoniNonValidiException e) {
					logger.info(e, e.getMessage());
					JOptionPane.showMessageDialog(getParent(), "Telefoni inseriti non validi", "Errore telefoni",
							JOptionPane.ERROR_MESSAGE);
				}
			}
			try {
				categorieOrmDaInserire = restituisciCategorieOrmCorrenti();
				List<Categoria> categorieDaInserire = new ArrayList<Categoria>(categorieOrmDaInserire.size());
				for (CategoriaORM categoriaOrm : categorieOrmDaInserire) {
					categorieDaInserire.add(categoriaOrm.restituisciCategoria());
				}
				tmp.setCategorie(categorieDaInserire);
			} catch (CategorieNonValideException e) {
				logger.info(e, e.getMessage());
				JOptionPane.showMessageDialog(getParent(), "Errore inserimento categorie.",
						"Errore inserimento categorie", JOptionPane.ERROR_MESSAGE);
			} catch (CategoriaOrmNonValidaException e) {
				logger.info(e, e.getMessage());
				JOptionPane.showMessageDialog(getParent(), "Errore reperimento categoriaORM.",
						"Errore reperimento categorie", JOptionPane.ERROR_MESSAGE);
			}

			return tmp;
		} else {
			JOptionPane.showMessageDialog(getParent(), "Non vi sono categorie selezionate.", "Selezione categoria",
					JOptionPane.ERROR_MESSAGE);
		}
		return null;
	}
	
	private boolean verificaDuplicatiComboBox(Object object){
		
		if(object == null){
			return false;
		}
		
		if(object instanceof Email){
			Email email = (Email)object;
			for(int i = 0; i < modelloEmail.getSize(); i++){
				if(email.equals(modelloEmail.getElementAt(i))){
					return false;
				}
			}
			return true;
		}
		
		if(object instanceof Telefono){
			Telefono telefono = (Telefono)object;
			for(int i = 0; i < modelloTelefoni.getSize(); i++){
				if(telefono.equals(modelloTelefoni.getElementAt(i))){
					return false;
				}
			}
			return true;
		}
		
		if(object instanceof CategoriaORM){
			CategoriaORM categoriaOrm = (CategoriaORM)object;
			for(int i = 0; i < modelloCategoriaORM.getSize(); i++){
				if(categoriaOrm.equals(modelloCategoriaORM.getElementAt(i))){
					return false;
				}
			}
			return true;
		}
		return false;
	}
	
	private void aggiornaListe(Object object){
		
		if(object instanceof Email){
			List<Email> tmp = restituisciEmailCorrenti();
			elencoEmail.removeAll();
			modelloEmail = new DefaultListModel<Email>();
			
			for (Email emailSingola : tmp) {
				modelloEmail.addElement(emailSingola);
			}
			elencoEmail.setModel(modelloEmail);
		}
		
		if(object instanceof Telefono){
			List<Telefono> tmp = restituisciTelefoniCorrenti();
			elencoTelefoni.removeAll();
			modelloTelefoni = new DefaultListModel<Telefono>();
			
			for (Telefono telefonoSingolo : tmp) {
				modelloTelefoni.addElement(telefonoSingolo);
			}
			elencoTelefoni.setModel(modelloTelefoni);
		}
		
		if(object instanceof CategoriaORM){
			List<CategoriaORM> tmp = restituisciCategorieOrmCorrenti();
			elencoCategorieSelezionate.removeAll();
			modelloCategoriaORM = new DefaultListModel<CategoriaORM>();
			
			for (CategoriaORM categoriaSingola : tmp) {
				modelloCategoriaORM.addElement(categoriaSingola);
			}
			elencoCategorieSelezionate.setModel(modelloCategoriaORM);
		}
	}
	
	private List<Email> restituisciEmailCorrenti(){
		List<Email> tmp = new ArrayList<Email>();
		for(int i = 0; i<modelloEmail.size(); i++){
			tmp.add(modelloEmail.getElementAt(i));
		}
		return tmp;
	}
	
	private List<Telefono> restituisciTelefoniCorrenti(){
		List<Telefono> tmp = new ArrayList<Telefono>();
		for(int i = 0; i<modelloTelefoni.size(); i++){
			tmp.add(modelloTelefoni.getElementAt(i));
		}
		return tmp;
	}
	
	private List<CategoriaORM> restituisciCategorieOrmCorrenti(){
		List<CategoriaORM> tmp = new ArrayList<CategoriaORM>();
		for(int i = 0; i<modelloCategoriaORM.size(); i++){
			tmp.add(modelloCategoriaORM.getElementAt(i));
		}
		return tmp;
	}
	
	private void aggiornaListaContatti(){
		pannelloRubrica.aggiornaListaContatti();
		distruggiContattoDialog();
	}
	
	private void distruggiContattoDialog(){
		JDialog dialogParent = (JDialog) getRootPane().getParent();
		dialogParent.dispose();
	}
}
