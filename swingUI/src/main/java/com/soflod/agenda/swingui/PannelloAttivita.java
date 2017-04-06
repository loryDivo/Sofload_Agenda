package com.soflod.agenda.swingui;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.soflod.agenda.core.Attivita;
import com.soflod.agenda.core.Impegno;
import com.soflod.agenda.persistence.AttivitaORM;
import com.soflod.agenda.persistence.exception.AttivitaNonValidaException;

public class PannelloAttivita extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private FramePrincipale framePrincipale;


	private JLabel titolo;
	private JLabel titoloAttivita;

	private JLabel dataInizio;
	private JLabel dataInizioAttivita;

	private JLabel dataScadenza;
	private JLabel dataScadenzaAttivita;

	private JLabel categoria;
	private JLabel categoriaAttivita;

	private JButton btnModifica;

	private JButton btnTornaIndietro;

	private transient AttivitaORM attivitaOrm;
	
	public PannelloAttivita(FramePrincipale framePrincipale, AttivitaORM attivitaOrm) throws AttivitaNonValidaException {
		this.attivitaOrm = attivitaOrm;
		this.framePrincipale = framePrincipale;

		Attivita attivita = attivitaOrm.restituisciAttivita();

		inizializzaEtichette(attivita);

		setLayout(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();

		gc.gridy = 0;
		gc.gridx = 0;
		gc.anchor = GridBagConstraints.WEST;
		gc.fill = GridBagConstraints.BOTH;
		gc.weightx = 0.5;
		gc.weighty = 0.5;
		add(titolo, gc);

		gc.gridy = 0;
		gc.gridx = 1;
		gc.weightx = 0.5;
		gc.weighty = 0.5;
		gc.fill = GridBagConstraints.BOTH;
		gc.anchor = GridBagConstraints.CENTER;
		add(titoloAttivita, gc);

		gc.gridy = 1;
		gc.gridx = 0;
		gc.anchor = GridBagConstraints.WEST;
		gc.fill = GridBagConstraints.BOTH;
		add(dataInizio, gc);

		gc.gridy = 1;
		gc.gridx = 1;
		gc.anchor = GridBagConstraints.CENTER;
		gc.fill = GridBagConstraints.BOTH;
		add(dataInizioAttivita, gc);

		gc.gridy = 2;
		gc.gridx = 0;
		gc.anchor = GridBagConstraints.WEST;
		gc.fill = GridBagConstraints.BOTH;
		add(dataScadenza, gc);

		gc.gridy = 2;
		gc.gridx = 1;
		gc.fill = GridBagConstraints.BOTH;
		gc.anchor = GridBagConstraints.CENTER;
		add(dataScadenzaAttivita, gc);

		gc.gridy = 5;
		gc.gridx = 0;
		gc.anchor = GridBagConstraints.WEST;
		gc.fill = GridBagConstraints.BOTH;
		add(categoria, gc);

		gc.gridy = 5;
		gc.gridx = 1;
		gc.fill = GridBagConstraints.BOTH;
		gc.anchor = GridBagConstraints.CENTER;
		add(categoriaAttivita, gc);

		gc.gridy = 6;
		gc.gridx = 0;
		gc.weighty = 0.1;
		gc.weightx = 0.1;
		gc.anchor = GridBagConstraints.CENTER;
		gc.fill = GridBagConstraints.NONE;
		add(btnTornaIndietro, gc);

		gc.gridy = 6;
		gc.gridx = 1;
		gc.anchor = GridBagConstraints.CENTER;
		gc.fill = GridBagConstraints.NONE;
		add(btnModifica, gc);

		btnModifica.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				richiamaModificaAttivitaOrm();
			}
		});
		
		btnTornaIndietro.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tornaIndietro();
			}
		});

	}

	public void inizializzaEtichette(Attivita attivita) {
		titolo = new JLabel("Titolo:");
		dataInizio = new JLabel("Data inizio:");
		dataScadenza = new JLabel("Data fine:");
		categoria = new JLabel("Categoria associata:");

		titolo.setFont(new Font(null, Font.BOLD, 20));
		dataInizio.setFont(new Font(null, Font.BOLD, 20));
		dataScadenza.setFont(new Font(null, Font.BOLD, 20));
		categoria.setFont(new Font(null, Font.BOLD, 20));

		SimpleDateFormat sdf = new SimpleDateFormat(Impegno.FORMATO_DATA_IMPEGNO);

		this.titoloAttivita = new JLabel(attivita.getTitolo());
		this.dataInizioAttivita = new JLabel(sdf.format(attivita.getDataInizio()));
		this.dataScadenzaAttivita = new JLabel(sdf.format(attivita.getDataScadenza()));
		this.categoriaAttivita = new JLabel(attivita.getCategoria().getEtichetta());

		this.btnTornaIndietro = new JButton("Indietro");

		this.btnModifica = new JButton("Modifica");

	}

	private void richiamaModificaAttivitaOrm() {
		framePrincipale.richiamaModificaAttivitaOrm(attivitaOrm);
	}
	
	private void tornaIndietro() {
		framePrincipale.visualizzaCarta(FramePrincipale.VISTA_GIORNO);
	}
}
