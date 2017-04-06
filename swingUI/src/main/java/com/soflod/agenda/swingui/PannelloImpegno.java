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
import javax.swing.JTextArea;

import com.soflod.agenda.core.Impegno;
import com.soflod.agenda.core.Ripetizione;
import com.soflod.agenda.persistence.ImpegnoORM;
import com.soflod.agenda.persistence.exception.ImpegnoOrmNonValidoException;

public class PannelloImpegno extends JPanel {

	private static final long serialVersionUID = 1L;
	private FramePrincipale framePrincipale;


	private JLabel titolo;
	private JLabel titoloImpegno;

	private JLabel dataInizio;
	private JLabel dataInizioImpegno;

	private JLabel dataFine;
	private JLabel dataFineImpegno;

	private JLabel labelRipetizione;
	private JLabel ripetizioneImpegno;

	private JLabel labelAllarme;
	private JLabel allarmeImpegno;

	private JLabel categoria;
	private JLabel categoriaImpegno;
	
	private JLabel labelNote;
	private JTextArea campoNote;

	private JButton btnModifica;

	private JButton btnTornaIndietro;

	private transient ImpegnoORM impegnoOrm;
	
	public PannelloImpegno(FramePrincipale framePrincipale, ImpegnoORM impegnoOrm) throws ImpegnoOrmNonValidoException {

		this.framePrincipale = framePrincipale;
		this.impegnoOrm = impegnoOrm;
			
		Impegno impegno = impegnoOrm.restituisciImpegno();			
		
		inizializzione(impegno);

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
		add(titoloImpegno, gc);

		gc.gridy = 1;
		gc.gridx = 0;
		gc.anchor = GridBagConstraints.WEST;
		gc.fill = GridBagConstraints.BOTH;
		add(dataInizio, gc);

		gc.gridy = 1;
		gc.gridx = 1;
		gc.anchor = GridBagConstraints.CENTER;
		gc.fill = GridBagConstraints.BOTH;
		add(dataInizioImpegno, gc);

		gc.gridy = 2;
		gc.gridx = 0;
		gc.anchor = GridBagConstraints.WEST;
		gc.fill = GridBagConstraints.BOTH;
		add(dataFine, gc);

		gc.gridy = 2;
		gc.gridx = 1;
		gc.fill = GridBagConstraints.BOTH;
		gc.anchor = GridBagConstraints.CENTER;
		add(dataFineImpegno, gc);

		gc.gridy = 3;
		gc.gridx = 0;
		gc.anchor = GridBagConstraints.WEST;
		gc.fill = GridBagConstraints.BOTH;
		add(labelRipetizione, gc);

		gc.gridy = 3;
		gc.gridx = 1;
		gc.fill = GridBagConstraints.BOTH;
		gc.anchor = GridBagConstraints.CENTER;
		add(ripetizioneImpegno, gc);

		gc.gridy = 4;
		gc.gridx = 0;
		gc.anchor = GridBagConstraints.WEST;
		gc.fill = GridBagConstraints.BOTH;
		add(labelAllarme, gc);

		gc.gridy = 4;
		gc.gridx = 1;
		gc.fill = GridBagConstraints.BOTH;
		gc.anchor = GridBagConstraints.CENTER;
		add(allarmeImpegno, gc);

		gc.gridy = 5;
		gc.gridx = 0;
		gc.anchor = GridBagConstraints.WEST;
		gc.fill = GridBagConstraints.BOTH;
		add(categoria, gc);

		gc.gridy = 5;
		gc.gridx = 1;
		gc.fill = GridBagConstraints.BOTH;
		gc.anchor = GridBagConstraints.CENTER;
		add(categoriaImpegno, gc);
		
		gc.gridy = 6;
		gc.gridx = 0;
		gc.anchor = GridBagConstraints.WEST;
		gc.fill = GridBagConstraints.BOTH;
		add(labelNote, gc);

		gc.gridy = 6;
		gc.gridx = 1;
		gc.fill = GridBagConstraints.BOTH;
		gc.anchor = GridBagConstraints.CENTER;
		add(campoNote, gc);

		gc.gridy = 7;
		gc.gridx = 0;
		gc.weighty = 0.1;
		gc.weightx = 0.1;
		gc.anchor = GridBagConstraints.CENTER;
		gc.fill = GridBagConstraints.NONE;
		add(btnTornaIndietro, gc);

		gc.gridy = 7;
		gc.gridx = 1;
		gc.anchor = GridBagConstraints.CENTER;
		gc.fill = GridBagConstraints.NONE;
		add(btnModifica, gc);
		
		btnModifica.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				richiamaModificaImpegnoOrm();
			}
		});
		
		btnTornaIndietro.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tornaIndietro();
			}
		});

	}

	private void inizializzione(Impegno impegno) {
		titolo = new JLabel("Titolo:");
		dataInizio = new JLabel("Data inizio:");
		dataFine = new JLabel("Data fine:");
		labelRipetizione = new JLabel("Ripetizione:");
		labelAllarme = new JLabel("Allarme:");
		categoria = new JLabel("Categoria associata:");
		labelNote = new JLabel("Note:");

		titolo.setFont(new Font(null, Font.BOLD, 20));
		dataInizio.setFont(new Font(null, Font.BOLD, 20));
		dataFine.setFont(new Font(null, Font.BOLD, 20));
		labelRipetizione.setFont(new Font(null, Font.BOLD, 20));
		labelAllarme.setFont(new Font(null, Font.BOLD, 20));
		categoria.setFont(new Font(null, Font.BOLD, 20));
		labelNote.setFont(new Font(null, Font.BOLD, 20));
		
		
		SimpleDateFormat sdf = new SimpleDateFormat(Impegno.FORMATO_DATA_IMPEGNO);

		this.titoloImpegno = new JLabel(impegno.getTitolo());
		this.dataInizioImpegno = new JLabel(sdf.format(impegno.getDataInizio()));
		this.dataFineImpegno = new JLabel(sdf.format(impegno.getDataFine()));
		Ripetizione ripetizione = impegno.getRipetizione();
		boolean allarme = impegno.getAllarme();
		this.ripetizioneImpegno = new JLabel(ripetizione.toString());
		if (allarme) {
			this.allarmeImpegno = new JLabel("ALLARME ATTIVO");
		} else {
			this.allarmeImpegno = new JLabel("ALLARME NON ATTIVO");
		}
		this.categoriaImpegno = new JLabel(impegno.getCategoria().getEtichetta());
		
		this.campoNote = new JTextArea(40, 40);
		this.campoNote.setText(impegno.getNote());
		this.campoNote.setEditable(false);
		this.btnTornaIndietro = new JButton("Indietro");
		this.btnModifica = new JButton("Modifica");

	}
	
	private void richiamaModificaImpegnoOrm() {
		framePrincipale.richiamaModificaImpegnoOrm(impegnoOrm);
	}
	
	private void tornaIndietro() {
		framePrincipale.visualizzaCarta(FramePrincipale.VISTA_GIORNO);
	}

}
