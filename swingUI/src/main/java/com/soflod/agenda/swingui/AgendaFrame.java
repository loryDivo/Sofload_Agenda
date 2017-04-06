package com.soflod.agenda.swingui;



import javax.swing.JOptionPane;

import com.j256.ormlite.logger.Logger;
import com.j256.ormlite.logger.LoggerFactory;
import com.soflod.agenda.persistence.exception.InizializzazioneAgendaException;

public class AgendaFrame {

	private FrameLogin frameLogin;
	private FramePrincipale framePrincipale;
	private Logger logger = LoggerFactory.getLogger(AgendaFrame.class);
	
	
	public AgendaFrame() {
		
		
		this.frameLogin = new FrameLogin(this);
		frameLogin.setVisible(true);
		
		this.framePrincipale = null;
		
	}	
	
	public void mostraFramePrincipale (String percorsoDB) {
		try {
			framePrincipale = new FramePrincipale(this, "Agenda", percorsoDB);
			framePrincipale.setSize(700, 600);
			this.frameLogin.setVisible(false);
			framePrincipale.setVisible(true);
		} catch (InizializzazioneAgendaException e) {
			logger.error(e, e.getMessage());
			
			JOptionPane.showMessageDialog(frameLogin, "Errore nell'istanziazione dell'agenda.", "Errore istanziazione", JOptionPane.ERROR_MESSAGE);
			
		}
	}
	
	public void mostraFrameLogin() {
		frameLogin.setVisible(true);
		framePrincipale.dispose();
	}
	
}
		