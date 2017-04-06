package com.soflod.agenda.swingui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.j256.ormlite.logger.Logger;
import com.j256.ormlite.logger.LoggerFactory;
import com.soflod.agenda.swingui.exception.UtenteNonValidoException;

public class PannelloRegistrazione extends JPanel {

	private JLabel labelNome;
	private JTextField campoTestoNome;
	
	private JPasswordField campoPassword;
	private JLabel labelPassword;
	
	private JPasswordField confermaPassword;
	private JLabel labelConfermaPassword;
	
	private JLabel labelPasswordNonCoincidente;
	private JLabel labelNomeUtenteNonDiposinibile;
	
	private JButton btnRegistrazione;
	
	private transient LoginUtility loginUtility;
	private transient Logger logger = LoggerFactory.getLogger(PannelloRegistrazione.class);

	public PannelloRegistrazione(LoginUtility loginUtility) {
		
		this.loginUtility = loginUtility;
		
		labelNome = new JLabel("Nome utente:");
		campoTestoNome = new JTextField();
		campoTestoNome.setText("Inserisci il tuo nome utente.");
		campoTestoNome.setColumns(40);
		labelNomeUtenteNonDiposinibile = new JLabel("Nome utente non disponibile");
		labelNomeUtenteNonDiposinibile.setForeground(Color.red);
		labelNomeUtenteNonDiposinibile.setVisible(false);
		
		labelPassword = new JLabel("Password:");
		campoPassword = new JPasswordField();
		campoPassword.setColumns(40);
		
		labelConfermaPassword = new JLabel("Conferma password:");
		confermaPassword = new JPasswordField();
		confermaPassword.setColumns(40);
		
		labelPasswordNonCoincidente = new JLabel("Le password non coincidono");
		labelPasswordNonCoincidente.setForeground(Color.RED);
		labelPasswordNonCoincidente.setVisible(false);
		
		btnRegistrazione = new JButton("Registrati");
		
		setLayout(new GridBagLayout());

		GridBagConstraints gc = new GridBagConstraints(); 
		
		gc.gridx = 0;
		gc.gridy = 0;
		gc.anchor = GridBagConstraints.WEST;
		add(labelNome, gc);
		
		gc.gridx = 0;
		gc.gridy = 1;
		add(campoTestoNome, gc);
		
		gc.gridx = 1;
		gc.gridy = 1;
		add(labelNomeUtenteNonDiposinibile, gc);
		
		gc.gridx = 0;
		gc.gridy = 2;
		gc.anchor = GridBagConstraints.WEST;
		add(labelPassword, gc);
		
		gc.gridx = 0;
		gc.gridy = 3;
		add(campoPassword, gc);
		
		gc.gridx = 0;
		gc.gridy = 4;
		gc.anchor = GridBagConstraints.WEST;
		add(labelConfermaPassword, gc);
		
		gc.gridx = 0;
		gc.gridy = 5;
		add(confermaPassword, gc);
		
		gc.gridx = 1;
		gc.gridy = 5;
		add(labelPasswordNonCoincidente, gc);
		
		gc.gridx = 0;
		gc.gridy = 6;
		add(btnRegistrazione, gc);
		
		
		/**
		 * Al click del mouse sul button registra verranno effettuati
		 * vari controlli prima di procedere all'effettiva registrazione
		 * dell'utente
		 */
		
		btnRegistrazione.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				registrati(campoTestoNome.getText(), campoPassword.getPassword());
			}
		});
		
		/**
		 * Se clicco con il mouse si disattiva la label di informazione
		 * relativa alla password non coincidente 
		 */
		
		this.confermaPassword.addMouseListener(new MouseListener() {
			public void mouseExited(MouseEvent e) {
				// evento non usato
			}
			public void mouseClicked(MouseEvent e) {
				labelPasswordNonCoincidente.setVisible(false);
				adattaDimensioniDialog();
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
		
		/**
		 * Se clicco con il mouse si disattiva la label di informazione
		 * relativa all'utente non disponibile per la registrazione
		 */
		
		this.campoTestoNome.addMouseListener(new MouseListener() {
			
			public void mouseReleased(MouseEvent e) {
				// evento non usato
			}
			public void mousePressed(MouseEvent e) {
				labelNomeUtenteNonDiposinibile.setVisible(false);
				adattaDimensioniDialog();
			}
			public void mouseExited(MouseEvent e) {
				// evento non usato
			}
			public void mouseEntered(MouseEvent e) {
				// evento non usato
			}
			public void mouseClicked(MouseEvent e) {
				// evento non usato
			}
		});
		
	}	
	
	private void registrati(String nomeUtente, char[] password){
		
		if (!loginUtility.verificaNomeUtenteUsato(nomeUtente)) {

			if (!loginUtility.verificaCoincidenzaPassword(campoPassword.getPassword(),
					confermaPassword.getPassword())) {
				if (!labelPasswordNonCoincidente.isVisible())
					labelPasswordNonCoincidente.setVisible(true);
				adattaDimensioniDialog();
			} else {

				try {
					loginUtility.registrazioneNuovoUtente(nomeUtente, password);
				} catch (UtenteNonValidoException e) {
					logger.info(e, e.getMessage());
					// Non gestito, controllo gia effettuato
				}

				registrati(nomeUtente, password);
				JDialog dialogParent = (JDialog) getRootPane().getParent();
				dialogParent.dispose();

			}
		} else

		{

			if (!labelNomeUtenteNonDiposinibile.isVisible()) {
				labelNomeUtenteNonDiposinibile.setVisible(true);
				JDialog dialogParent = (JDialog) getRootPane().getParent();
				dialogParent.pack();
			}
		}

	}
	
	public String getTextnome() {
		return this.campoTestoNome.getText();
	}
	
	public String getPassword() {
		char[] tmp = this.campoPassword.getPassword();
		return Arrays.toString(tmp);
	}

	public String getConfermaPassword() {
		char[] tmp = this.confermaPassword.getPassword();
		return Arrays.toString(tmp);
	}
	
	private void adattaDimensioniDialog() {
		((JDialog) this.getRootPane().getParent()).pack();
	}
}
