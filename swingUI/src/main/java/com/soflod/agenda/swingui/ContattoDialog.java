package com.soflod.agenda.swingui;

import javax.swing.JDialog;

import com.soflod.agenda.persistence.ContattoORM;

public class ContattoDialog extends JDialog {

	private ContattoDialogPanel contattoDialogPanel;

	public ContattoDialog(FramePrincipale framePrincipale, ContattoORM contattoORM) {
		super(framePrincipale, true);

		if (contattoORM == null) {
			this.setTitle("Inserisci contatto");
			contattoDialogPanel = new ContattoDialogPanel(framePrincipale, null);
		} else {
			this.setTitle("Modifica contatto");
			contattoDialogPanel = new ContattoDialogPanel(framePrincipale, contattoORM);
		}

		add(contattoDialogPanel);

		this.pack();

	}

}
