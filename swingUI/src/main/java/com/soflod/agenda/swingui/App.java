package com.soflod.agenda.swingui;


import javax.swing.SwingUtilities;

public class App {

	private App() {
		throw new IllegalAccessError();
	}
	
	public static void main(String[] args) {

		// creo un thread in cui lanciare il programma
		// per poterlo gestire corretamente
		SwingUtilities.invokeLater(new Runnable() {
			// in questo modo gestisco operazioni asincrone
			// passo come new Runnable direttamente tutto il metodo

			public void run() {
				new AgendaFrame();
			}
		});

	}
	
}
