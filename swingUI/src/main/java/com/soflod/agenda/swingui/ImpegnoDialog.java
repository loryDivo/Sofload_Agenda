package com.soflod.agenda.swingui;

import javax.swing.JDialog;

import com.soflod.agenda.persistence.ImpegnoORM;
import com.soflod.agenda.persistence.exception.ImpegnoOrmNonValidoException;

public class ImpegnoDialog extends JDialog {

	private ImpegnoDialogPanel impegnoDialogPanel;
	
	public ImpegnoDialog(FramePrincipale framePrincipale, ImpegnoORM impegnoORM) throws ImpegnoOrmNonValidoException {
		super(framePrincipale, true);
		
		if (impegnoORM == null) {
			this.setTitle("Inserisci impegno");
			impegnoDialogPanel = new ImpegnoDialogPanel(framePrincipale, null);
		} else {
			this.setTitle("Modifica impegno");
			impegnoDialogPanel = new ImpegnoDialogPanel(framePrincipale, impegnoORM);
		}
		
				
		add(impegnoDialogPanel);
		
		this.pack();
	}

}
