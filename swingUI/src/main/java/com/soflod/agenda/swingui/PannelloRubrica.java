package com.soflod.agenda.swingui;


import java.awt.Font;
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
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.soflod.agenda.persistence.Agenda;
import com.soflod.agenda.persistence.ContattoORM;

public class PannelloRubrica extends JPanel{

	private FramePrincipale framePrincipale;
	
	private JLabel labelTitoloContatti;
	
	private JButton btnAggiungiContatto;
	private JButton btnIndietro;
	
	private JList<ContattoORM> elencoContatti;
	private DefaultListModel<ContattoORM> modelloContatti;
	private JScrollPane pannelloContatti;
	
	private transient Agenda agenda;
	
	public PannelloRubrica(FramePrincipale framePrincipale) {
		this.framePrincipale = framePrincipale;
		this.agenda = framePrincipale.getAgenda();
		inizializzazione();
		
		aggiornaListaContatti();
		
		setLayout(new GridBagLayout());

		GridBagConstraints gc = new GridBagConstraints(); 
		
		gc.gridx = 1;
		gc.gridy = 1;
		gc.anchor = GridBagConstraints.WEST;
		gc.fill = GridBagConstraints.BOTH;
		add(labelTitoloContatti, gc);
		
		gc.gridx = 1;
		gc.gridy = 2;
		gc.weightx = 1.0;
		gc.weighty = 0.7;
		gc.fill = GridBagConstraints.BOTH;
		gc.anchor = GridBagConstraints.CENTER;
		add(pannelloContatti, gc);
		
		gc.gridx = 1;
		gc.gridy = 3;
		gc.weightx = 0.1;
		gc.weighty = 0.1;
		gc.fill = GridBagConstraints.NONE;
		gc.anchor = GridBagConstraints.WEST;
		add(btnAggiungiContatto, gc);
		
		gc.gridx = 1;
		gc.gridy = 3;
		gc.weightx = 0.1;
		gc.weighty = 0.1;
		gc.fill = GridBagConstraints.NONE;
		gc.anchor = GridBagConstraints.EAST;
		add(btnIndietro, gc);
		
		btnAggiungiContatto.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				richiamaInserisciContattoDialog();
			}
		});
		
		elencoContatti.addMouseListener(new MouseListener() {
			
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
			        	ContattoORM contattoSelezionato = elencoContatti.getSelectedValue();
			        	visualizzaContattoSelezionato(contattoSelezionato);
			        }
			}
		});
		
	}

	private void inizializzazione(){
		labelTitoloContatti = new JLabel("Visualizzazione contatti");
		labelTitoloContatti.setFont(new Font(null, Font.BOLD, 20));
		btnAggiungiContatto = new JButton("Aggiungi");
		btnIndietro = new JButton("Indietro");
		modelloContatti = new DefaultListModel<ContattoORM>();
		elencoContatti = new JList<ContattoORM>(modelloContatti);
		elencoContatti.setCellRenderer(new RenderCella());
		pannelloContatti = new JScrollPane(elencoContatti);
	}
	
	public void aggiornaListaContatti(){
		elencoContatti.removeAll();
		List<ContattoORM> tmpList = agenda.getRubrica().getContattiORM();
		modelloContatti = new DefaultListModel<ContattoORM>();
		
		for (ContattoORM contattoORM : tmpList) {
			modelloContatti.addElement(contattoORM);
		}
		
		elencoContatti.setModel(modelloContatti);
	}
	
	
	private void richiamaInserisciContattoDialog() {
		ContattoDialog contattoDialog;
		contattoDialog = new ContattoDialog(framePrincipale, null);
		contattoDialog.setVisible(true);
	}

	private void visualizzaContattoSelezionato(ContattoORM contattoSelezionato){
		framePrincipale.visualizzaContattoOrmSelezionato(contattoSelezionato);
	}
	
	public void richiamaModificaContattoOrm(ContattoORM nuovoContattoORM){
			ContattoDialog contattoDialog;
			contattoDialog = new ContattoDialog(framePrincipale, nuovoContattoORM);
			contattoDialog.setVisible(true);
	}
}
