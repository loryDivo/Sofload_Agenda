package com.soflod.agenda.swingui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.j256.ormlite.logger.Logger;
import com.j256.ormlite.logger.LoggerFactory;
import com.soflod.agenda.persistence.Agenda;
import com.soflod.agenda.persistence.ImpegnoORM;
import com.soflod.agenda.persistence.exception.ImpegnoOrmNonValidoException;

public class PannelloImpegniCompatibili extends JPanel{

	private JLabel labelImpegniCompatibili;
	
	private JList<ImpegnoORM> elencoImpegniCompatibili;
	private DefaultListModel<ImpegnoORM> modelloImpegniCompatibili;
	private JScrollPane scrollImpegniCompatibili;
	
	private transient Agenda agenda;
	private transient Logger logger = LoggerFactory.getLogger(PannelloImpegniCompatibili.class);
	
	public PannelloImpegniCompatibili(FramePrincipale framePrincipale) {
		
		this.agenda = framePrincipale.getAgenda();
		inizializzazione();
		
		try {
			aggiornaListaImpegniCompatibili();
		} catch (ImpegnoOrmNonValidoException e) {
			logger.info(e, e.getMessage());
			JOptionPane.showMessageDialog(getParent(), "Errore nel visualizzare gli impegni compatibili.", "Errore impegni compatibili",
					JOptionPane.ERROR_MESSAGE);
		}
		
		setLayout(new GridBagLayout());

		GridBagConstraints gc = new GridBagConstraints(); 
		
		gc.gridx = 1;
		gc.gridy = 1;
		gc.anchor = GridBagConstraints.WEST;
		gc.fill = GridBagConstraints.BOTH;
		add(labelImpegniCompatibili, gc);
		
		gc.gridx = 1;
		gc.gridy = 2;
		gc.weightx = 1.0;
		gc.weighty = 0.7;
		gc.fill = GridBagConstraints.BOTH;
		gc.anchor = GridBagConstraints.CENTER;
		add(scrollImpegniCompatibili, gc);
		
		
	}

	private void inizializzazione(){
		
		labelImpegniCompatibili = new JLabel("Impegni compatibili");
		
		modelloImpegniCompatibili = new DefaultListModel<ImpegnoORM>();
		
		elencoImpegniCompatibili = new JList<ImpegnoORM>(modelloImpegniCompatibili);
		
		scrollImpegniCompatibili = new JScrollPane(elencoImpegniCompatibili);
	}
	
	private void aggiornaListaImpegniCompatibili() throws ImpegnoOrmNonValidoException{
		List<ImpegnoORM> impegniCompatibili = agenda.visualizzaImpegniCompatibili();
		elencoImpegniCompatibili.removeAll();
		modelloImpegniCompatibili = new DefaultListModel<ImpegnoORM>();
		
		for (ImpegnoORM impegniOrm : impegniCompatibili) {
			modelloImpegniCompatibili.addElement(impegniOrm);
		}
		
		elencoImpegniCompatibili.setModel(modelloImpegniCompatibili);
		
		
	}
	
	
}
