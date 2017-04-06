package com.soflod.agenda.swingui;


import javax.swing.JDialog;

import com.soflod.agenda.persistence.CategoriaORM;
import com.soflod.agenda.persistence.exception.CategoriaOrmNonValidaException;

public class CategoriaDialog extends JDialog {


	private CategoriaDialogPanel categoriaDialogPanel;

	public CategoriaDialog(FramePrincipale framePrincipale, CategoriaORM categoriaORM) throws CategoriaOrmNonValidaException {
		super(framePrincipale, true);

		if (categoriaORM == null) {
			this.setTitle("Inserisci contatto");
			categoriaDialogPanel = new CategoriaDialogPanel(framePrincipale, null);
		} else {
			this.setTitle("Modifica contatto");
			categoriaDialogPanel = new CategoriaDialogPanel(framePrincipale, categoriaORM);
		}

		add(categoriaDialogPanel);

		this.pack();

	}


}
