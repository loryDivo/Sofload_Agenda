package com.soflod.agenda.swingui;

import javax.swing.JDialog;

import com.soflod.agenda.persistence.AttivitaORM;
import com.soflod.agenda.persistence.exception.AttivitaNonValidaException;

public class AttivitaDialog extends JDialog {

	private AttivitaDialogPanel attivitaDialogPanel;
	
	public AttivitaDialog(FramePrincipale framePrincipale, AttivitaORM attivitaORM) throws AttivitaNonValidaException {
		super(framePrincipale, true);
		
		if (attivitaORM == null) {
			this.setTitle("Inserisci attivita");
			attivitaDialogPanel = new AttivitaDialogPanel(framePrincipale, null);
		} else {
			this.setTitle("Modifica attivita");
			attivitaDialogPanel = new AttivitaDialogPanel(framePrincipale, attivitaORM);
		}
		
				
		add(attivitaDialogPanel);
		
		this.pack();
	}

}
