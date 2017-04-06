package com.soflod.agenda.swingui;

import java.awt.Frame;

import javax.swing.JDialog;

public class RegistrazioneDialog extends JDialog {
	
	private PannelloRegistrazione pannello;
	
	private transient LoginUtility loginUtility;
	
	public RegistrazioneDialog(Frame parent, LoginUtility loginUtility) {
		
		super(parent, true);
		this.setLocationRelativeTo(parent);
		
		this.setTitle("Registrazione");
		
		this.loginUtility = loginUtility;
		
		pannello = new PannelloRegistrazione(this.loginUtility);
		
		add(pannello);
		
		this.pack();
		
	}


	public PannelloRegistrazione getPannello() {
		return pannello;
	}
}
