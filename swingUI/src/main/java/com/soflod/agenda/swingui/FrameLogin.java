package com.soflod.agenda.swingui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;


import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.j256.ormlite.logger.Logger;
import com.j256.ormlite.logger.LoggerFactory;
import com.soflod.agenda.swingui.exception.PasswordException;
import com.soflod.agenda.swingui.exception.UtenteNonValidoException;

public class FrameLogin extends JFrame {

	private static final long serialVersionUID = 1L;

	
	private String infoPercorsoDB;
	
	private final String home;
	private RegistrazioneDialog registrazioneDialog;
	
	private JButton bottoneRegistrazione;
	private JButton bottoneLogin;
	
	
	private JTextField campoNome;
	private JPasswordField campoPassword;
	private JLabel labelNome;
	private JLabel labelPassword;
	
	private JLabel labelPasswordInvalida;
	private JLabel labelNomeNonValido;
	
	
	private transient AgendaFrame agendaFrame;
	private transient LoginUtility loginUtility;
	private transient Logger logger = LoggerFactory.getLogger(FrameLogin.class);
	/**
	 * Istanziazione login con inizializzazione userName e password e creazione
	 * file o accesso se questo fosse già stato creato
	 */
	public FrameLogin(AgendaFrame agendaFrame) {

		super();
		
		this.agendaFrame = agendaFrame;
		this.registrazioneDialog = null;
		this.bottoneLogin = null;
		
		this.home = System.getProperty("user.home") + File.separator + "agenda";
		File cartellaHome = new File(this.home);

		if (!cartellaHome.exists()) {
			cartellaHome.mkdirs();
		}
		
		// Sfondo grigio
		setBackground(Color.LIGHT_GRAY);

		this.labelNome = new JLabel("Nome utente:");
		this.campoNome = new JTextField("Inserisci nome utente");
		this.campoNome.setColumns(40);
		
		
		this.campoNome.addMouseListener(new MouseListener() {
			boolean primaVolta = true;
			
			public void mouseExited(MouseEvent e) {
				// evento non usato
			}

			public void mouseClicked(MouseEvent e) {
				
				if (primaVolta) {
					primaVolta = false;
					campoNome.setText("");
				}
				
				if (labelNomeNonValido.isVisible()) {
					labelNomeNonValido.setVisible(false);
					adattaDimensioniFrame();
				}
			}

			public void mouseReleased(MouseEvent e) {
				// evento non usato
			}
			public void mouseEntered(MouseEvent e) {
				// evento non usato
			}
			public void mousePressed(MouseEvent e) {
				// evento non usato
			}
		});
			
		
		this.labelNomeNonValido = new JLabel("Nome utente inesistente");
		this.labelNomeNonValido.setForeground(Color.RED);
		this.labelNomeNonValido.setVisible(false);
		
		this.labelPassword = new JLabel("Password:");
		this.campoPassword = new JPasswordField();
		this.campoPassword.setColumns(40);
		
		this.campoPassword.addMouseListener(new MouseListener() {
			
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
				if (labelPasswordInvalida.isVisible()) {
					labelPasswordInvalida.setVisible(false);
					adattaDimensioniFrame();
				}
			}
		});
		
		
		this.labelPasswordInvalida = new JLabel("Password non valida");
		this.labelPasswordInvalida.setForeground(Color.RED);
		this.labelPasswordInvalida.setVisible(false);
		
		this.bottoneRegistrazione = new JButton("Registrati");
		this.bottoneRegistrazione.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				richiamaRegistrazioneDialog();
			}
		});
		
		this.bottoneLogin = new JButton("Login");
		this.bottoneLogin.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				eseguiAutenticazione();
			}
		});
		
		setLayout(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();

		gc.gridy = 0;
		gc.gridx = 0;
		gc.anchor = GridBagConstraints.WEST;
		add(labelNome, gc);
		
		gc.gridy = 1;
		gc.gridx = 0;
		add(campoNome, gc);
		
		gc.gridy = 1;
		gc.gridx = 1;
		add(labelNomeNonValido, gc);
		
		gc.gridy = 2;
		gc.gridx = 0;
		gc.anchor = GridBagConstraints.WEST;
		add(labelPassword, gc);
		
		gc.gridy = 3;
		gc.gridx = 0;
		add(campoPassword, gc);
		
		gc.gridy = 3;
		gc.gridx = 1;
		add(labelPasswordInvalida, gc);
		
		gc.gridy = 4;
		gc.gridx = 0;
		gc.anchor = GridBagConstraints.WEST;
		add(bottoneRegistrazione, gc);
		
		gc.gridy = 4;
		gc.gridx = 1;
		gc.anchor = GridBagConstraints.EAST;
		add(bottoneLogin, gc);
		
		this.pack();
		
		loginUtility = new LoginUtility(home);
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	private void adattaDimensioniFrame() {
		this.pack();
	}
	
	private void richiamaRegistrazioneDialog() {
		
		this.registrazioneDialog = new RegistrazioneDialog(this, this.loginUtility);
		this.registrazioneDialog.setVisible(true);
	}
	
	private void eseguiAutenticazione() {
		try {
			autenticazioneLogin(campoNome.getText(), campoPassword.getPassword());
		} catch (UtenteNonValidoException ue) {
			logger.info(ue, ue.getMessage());
			if (ue.getCause() != null) {
				
				if (ue.getCause() instanceof PasswordException && (! labelPasswordInvalida.isVisible())) {
						labelPasswordInvalida.setVisible(true);
						adattaDimensioniFrame();
				}
			} else {
				if (! labelNomeNonValido.isVisible()) {
					labelNomeNonValido.setVisible(true);
					adattaDimensioniFrame();
				}
			
			}
		}
	}

	private void autenticazioneLogin(String user, char[] password) throws UtenteNonValidoException {
		String infoAccesso = loginUtility.verificaCorrispondenzaLogin(user, password);
		if(infoAccesso != null){
			// restituisci il path al parent, poi nascondi il frame e attiva il frame principale
			agendaFrame.mostraFramePrincipale(infoAccesso);
		}
		else{
			JOptionPane.showMessageDialog(getParent(), "Nome utente o password non valida");
		}
	}
	
	public String getPercorsoDB(){
		return infoPercorsoDB;
	}
	
}
