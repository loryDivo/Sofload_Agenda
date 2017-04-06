package com.soflod.agenda.core;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
public class Rubrica {
		
	
		private List<Contatto> contatti;
		
		/**
		 * Costruttore di rubrica ha come
		 * parametro obbligatorio (e unico)
		 * contatti.
		 */
		public Rubrica(){
			this.contatti = new ArrayList<Contatto>();
		}
		
		public List<Contatto> getContatti(){
			return contatti;
		}
		
		/**
		 * Aggiunge un contatto alla rubrica
		 * Se il contatto è gia presente
		 * non lo aggiunge e ritorna true 
		 * e non lancia alcuna eccezione
		 * @param contatto
		 * @return
		 */
		public boolean aggiungiContatto(Contatto contatto){
			if(this.contatti.contains(contatto)){
				return true;
			}
			return this.contatti.add(contatto);
		}
		
		/**
		 * Rimuove un contatto dalla rubrica
		 * se il contatto è presente viene rimosso
		 * e viene ritornato il contatto
		 * altrimenti si ritorna null
		 * @param contatto
		 * @return
		 */
		public Contatto rimuoviContatto(Contatto contatto){
			if(this.contatti.contains(contatto)){
				this.contatti.remove(contatto);
				return contatto;
			}
			else{
				return null;
			}
		}
		
		public int getNumeroContatti(){
			return contatti.size();
		}
		
		/**
		 * Ricerca un contatto all'interno di 
		 * rubrica per nome
		 * Vi possono essere più contatti con lo stesso
		 * nome pertanto verrà restituita una lista di
		 * contatti ricercati
		 * @param nome
		 * @return
		 */
		public List<Contatto> ricercaNomeContatto(String nome){
			List<Contatto> contattiRicercati = new ArrayList<Contatto>();
			Iterator<Contatto> contatIterator = contatti.iterator();
			while(contatIterator.hasNext()){
				Contatto contatto = contatIterator.next();
				if(contatto.getNome().equals(nome)){
					contattiRicercati.add(contatto);
				}
			}
			return contattiRicercati;
		}

		/*
		 * Stampa rubrica in formato JSON(non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		
		@Override
		public String toString() {
			StringBuilder str = new StringBuilder();
			str.append("{\"rubrica\" : {\"contatti\" : [" );
			Iterator<Contatto> contatIterator = contatti.iterator();
			while(contatIterator.hasNext()){
				Contatto contatto = contatIterator.next();
				if(contatIterator.hasNext()){
				str.append(contatto.toString());
				str.append(",");
				}
				else{
					str.append(contatto.toString());
				}
			}
			str.append("]}}");
			return str.toString();
		}

		
}

