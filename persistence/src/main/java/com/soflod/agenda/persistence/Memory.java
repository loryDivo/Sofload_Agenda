package com.soflod.agenda.persistence;


import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.soflod.agenda.core.Attivita;
import com.soflod.agenda.core.Categoria;
import com.soflod.agenda.core.Contatto;
import com.soflod.agenda.core.Impegno;
import com.soflod.agenda.persistence.exception.CategoriaOrmNonValidaException;
import com.soflod.agenda.persistence.exception.RimozioneCategoriaException;

public class Memory {
	
	private Dao<ImpegnoORM, Integer> impegniDAO;
	private Dao<AttivitaORM, Integer> attivitaDAO;
	private Dao<CategoriaORM, Integer> categorieDAO; 
	private Dao<ContattoORM, Integer> contattiDAO;
	private Dao<ContattoCategoriaORM, Integer> contattiCategorieDAO;
     

	public Memory(ConnectionSource connection) throws SQLException {
		TableUtils.createTableIfNotExists(connection, ImpegnoORM.class);
		TableUtils.createTableIfNotExists(connection, AttivitaORM.class);
		TableUtils.createTableIfNotExists(connection, CategoriaORM.class);
		TableUtils.createTableIfNotExists(connection, ContattoORM.class);
		TableUtils.createTableIfNotExists(connection, ContattoCategoriaORM.class);
		
		this.impegniDAO = 
				DaoManager.createDao(connection, ImpegnoORM.class);
		this.attivitaDAO =
				DaoManager.createDao(connection, AttivitaORM.class);
		this.categorieDAO = 
				DaoManager.createDao(connection, CategoriaORM.class);
		this.contattiDAO = 
				DaoManager.createDao(connection, ContattoORM.class);
		this.contattiCategorieDAO = 
				DaoManager.createDao(connection, ContattoCategoriaORM.class);
	}
	
	public Dao<ContattoCategoriaORM, Integer> getContattiCategorieDAO() {
		return contattiCategorieDAO;
	}

	
	/**
	 * Viene inserita l'attività all'interno
	 * del db ritornando true ad avvenuto 
	 * successo
	 * @param attivitaORM
	 * @return
	 * @throws SQLException
	 */
	
	public boolean inserisciAttivitaORM(AttivitaORM attivitaORM) throws SQLException{
		if(attivitaDAO.create(attivitaORM) == 1){
			return true;
		}
		return false;
	}
	
	public boolean inserisciImpegnoORM(ImpegnoORM impegnoORM) throws SQLException{
		if(impegniDAO.create(impegnoORM)==1){
			return true;
		}
		return false;
	}
	
	public boolean inserisciCategoriaORM(CategoriaORM categoriaORM) throws SQLException{
		
		categorieDAO.createIfNotExists(categoriaORM);
			return true;
		
	}
	
	public boolean inserisciContattoORM(ContattoORM contattoORM) throws SQLException{
		if(contattiDAO.create(contattoORM)==1){
		
			for (CategoriaORM categoriaORM : contattoORM.getCategorieORM()) {
				
				this.contattiCategorieDAO.create(new ContattoCategoriaORM(contattoORM,
						categoriaORM));
				
			}
			
			return true;
		}
		return false;
	}
	
	/**
	 * Viene rimossa una categoria
	 * Tutte le eccezioni vengono reindirizzate a 
	 * "RimozioneCategoriaException"
	 * Vi possono essere eccezioni dovute
	 * alla manca rimozione della categoria
	 * per via dell'associazione di questa ad un
	 * impegno o attivita
	 * @param categoriaORM
	 * @return
	 * @throws RimozioneCategoriaException
	 */
	
	public boolean cancellaCategoriaORM(CategoriaORM categoriaORM) throws RimozioneCategoriaException {
		try {
			
			if (!verificaImpegno(categoriaORM) || !verificaAttivita(categoriaORM)) {
				throw new RimozioneCategoriaException(
						"Impossibile eliminare " + "la categoria. Vi sono Impegni od attivita associati.");
			}
			List<ContattoCategoriaORM> contattiCategorieAssociati;
			contattiCategorieAssociati = contattiCategorieDAO.queryForEq(ContattoCategoriaORM.ID_CATEGORIA,
					categoriaORM.getIdCategoria());

			List<ContattoCategoriaORM> eliminabili = new ArrayList<ContattoCategoriaORM>();
			for (ContattoCategoriaORM contattoCategoriaAssociato : contattiCategorieAssociati) {
				if (contattiCategorieDAO
						.queryForEq(ContattoCategoriaORM.ID_CONTATTO, contattoCategoriaAssociato.getContatto())
						.size() > 1) {
					eliminabili.add(contattoCategoriaAssociato);
				} else {
					throw new CategoriaOrmNonValidaException("Impossibile eliminare la categoria. "
							+ "Vi sono contatti associati alla categoria che rimarrebbero senza categorie.");
				}
			}

			contattiCategorieDAO.delete(eliminabili);
			categorieDAO.delete(categoriaORM);
		} catch(SQLException e) {
			throw new RimozioneCategoriaException(e);
		} catch (RimozioneCategoriaException e) {
			throw new RimozioneCategoriaException(e);
		} catch (CategoriaOrmNonValidaException e) {
			throw new RimozioneCategoriaException(e);
		}
		
		return true;
	}
	
	/**
	 * Viene rimosso un impegno
	 * Vi possono essere errori dovuti
	 * dal db
	 * @param impegnoORM
	 * @return
	 * @throws SQLException
	 */
	
	public boolean cancellaImpegnoORM(ImpegnoORM impegnoORM) throws SQLException{
		if(impegniDAO.delete(impegnoORM) == 1){
			return true;
		}
		return false;
	}
	
	/**
	 * Viene rimossa un attivita
	 * vi possono essere errori dovuti
	 * dal db
	 * @param attivitaORM
	 * @return
	 * @throws SQLException
	 */
	
	public boolean cancellaAttivitaORM(AttivitaORM attivitaORM) throws SQLException{
		if(attivitaDAO.delete(attivitaORM) == 1){
			return true;
		}
		return false;
	}
	
	/**
	 * Viene richiesto il contatto dal database
	 * e viene rimosso
	 * vi possono essere eccezioni dovute al
	 * db per l'eliminazione
	 * @param contattoORM
	 * @return
	 * @throws SQLException
	 */
	
	public boolean cancellaContattoORM(ContattoORM contattoORM) throws SQLException{
		
		List<ContattoCategoriaORM> associati =
				contattiCategorieDAO
				.queryForEq(ContattoCategoriaORM.ID_CONTATTO, contattoORM.getIdContatto());
		
		contattiCategorieDAO.delete(associati);
		if(contattiDAO.delete(contattoORM) == 1){
			return true;
		}
		return false;
	}
	
	/**
	 * Viene l'id della nuova categoria con l'id
	 * di quella vecchia e viene chiamato l'update
	 * in caso di fallimento viene lanciata eccezione
	 * dovuta dal db
	 * @param vecchiaCategoria
	 * @param nuovaCategoria
	 * @return
	 * @throws SQLException
	 */
	
	public boolean modificaCategoriaORM(CategoriaORM vecchiaCategoria, CategoriaORM nuovaCategoria)
			throws SQLException{
		
		Integer tmpId = vecchiaCategoria.getIdCategoria();
		
		// supponiamo che nuovaCategoria non abbia id impostato
		nuovaCategoria.setIdCategoria(tmpId);
		
		if (categorieDAO.update(nuovaCategoria) == 1)
			return true;
		
		return false;
	}
	
	/**
	 * Viene modifica l'impegno nel db
	 * Viene preso l'id del vecchio impegno
	 * viene settato al nuovo impegno
	 * viene chiamata l'update per aggiornare
	 * l'impegno
	 * Eccezione dovuta dal db in caso di errato
	 * update dell'impegno
	 * @param vecchioImpegno
	 * @param nuovoImpegno
	 * @return
	 * @throws SQLException
	 */
	
	public boolean modificaImpegnoORM(ImpegnoORM vecchioImpegno, ImpegnoORM nuovoImpegno) throws SQLException {
		Integer tmpId = vecchioImpegno.getIdImpegno();
		
		nuovoImpegno.setIdImpegno(tmpId);
		
		if (impegniDAO.update(nuovoImpegno) == 1)
			return true;
		
		return false;
	}
	
	/**
	 * Viene modificata un attivita 
	 * viene settato l'id della vecchia 
	 * attivita a quella nuova 
	 * e viene chiamato l'update del db
	 * Se l'update non andasse a buon fine
	 * viene ritornato false altrimenti true
	 * Eccezione dovuta dal db
	 * @param vecchiaAttivita
	 * @param nuovaAttivita
	 * @return
	 * @throws SQLException
	 */
	
	public boolean modificaAttivitaORM(AttivitaORM vecchiaAttivita, AttivitaORM nuovaAttivita)
			throws SQLException {
		
		Integer tmpId = vecchiaAttivita.getIdAttivita();
		nuovaAttivita.setIdAttivita(tmpId);
		
		if (attivitaDAO.update(nuovaAttivita) == 1)
			return true;
		
		return false;
	}
	
	/**
	 * Viene settato l'id del vecchio contatto
	 * a quello nuovo e viene chiamata l'update 
	 * se l'update non funziona viene lanciata eccezione
	 * dovuta dal db
	 * @param vecchioContatto
	 * @param nuovoContatto
	 * @return
	 * @throws SQLException
	 */
	
	public ContattoORM modificaContattoORM(ContattoORM vecchioContatto, ContattoORM nuovoContatto) throws SQLException {
		Integer tmpId = vecchioContatto.getIdContatto();
		nuovoContatto.setIdContatto(tmpId);
		
		if(contattiDAO.update(nuovoContatto) == 1){
			return nuovoContatto;
		}
		
		return null;
	}
	
	/**
	 * Verifica esistenza della categoriaORM 
	 * all'interno del db di impegno
	 * @param categoriaORM
	 * @return
	 * @throws SQLException
	 */
	
	public boolean verificaImpegno(CategoriaORM categoriaORM) throws SQLException{
		List <ImpegnoORM> impegniIdCategorie;
		impegniIdCategorie= impegniDAO.queryForEq(ImpegnoORM.CATEGORIA, categoriaORM.getIdCategoria());
		return impegniIdCategorie.isEmpty();
	}
	
	/**
	 * Verifica esistenza della categoriaORM 
	 * all'interno del db di attività
	 * @param categoriaORM
	 * @return
	 * @throws SQLException
	 */
	
	
	public boolean verificaAttivita(CategoriaORM categoriaORM) throws SQLException{
		List<AttivitaORM> attivitaIdCategorie;
		attivitaIdCategorie = attivitaDAO.queryForEq(AttivitaORM.CATEGORIA, categoriaORM.getIdCategoria());
		return attivitaIdCategorie.isEmpty();
	}
	
	public void svuotaTabelle(ConnectionSource connection) throws SQLException {
		
		TableUtils.clearTable(connection, ImpegnoORM.class);
		TableUtils.clearTable(connection, AttivitaORM.class);
		TableUtils.clearTable(connection, CategoriaORM.class);
		TableUtils.clearTable(connection, ContattoORM.class);
		TableUtils.clearTable(connection, ContattoCategoriaORM.class);
	}
	
	/**
	 * Interrogo la tabella ImpegniORM e prelevo tutti
	 * gli impegni che hanno una data di inizio tra
	 * da e a.
	 * 
	 * @param da
	 * @param a
	 * @return
	 * @throws SQLException 
	 */
	public List<ImpegnoORM> restituisciImpegniIn(Date da, Date a) throws SQLException {

		List<ImpegnoORM> impegni = new ArrayList<ImpegnoORM>();

		Date tmp;
		for (ImpegnoORM impegnoORM : impegniDAO) {

			tmp = impegnoORM.getDataInizio();
			if (tmp.compareTo(da) >= 0 && tmp.compareTo(a) <= 0) {
				impegnoORM.setCategoriaORM(categorieDAO.queryForId(impegnoORM.getCategoriaORM().getIdCategoria()));
				impegni.add(impegnoORM);
			}
		}

		return impegni;
	}
	
	/**
	 * Vengono restituite le attivitàORM attive nel mese 
	 * interrogazione mediante attivitaDAO
	 * @param month
	 * @return
	 * @throws SQLException 
	 */
	public List<AttivitaORM> restituisciAttivitaNelMese(Date meseConsiderato) throws SQLException {
		return restituisciAttivitaAttiveIn(meseConsiderato, Agenda.PRECISIONE_MESE);
	}
	
	public List<AttivitaORM> restituisciAttivitaAttiveIn(Date meseConsiderato, String formato) throws SQLException {

		SimpleDateFormat sdfMese = new SimpleDateFormat(formato);
		String precisioneMese = sdfMese.format(meseConsiderato);

		List<AttivitaORM> attivita = new ArrayList<AttivitaORM>();
		try {
			Date mese = sdfMese.parse(precisioneMese);


			String stringaDataInizio;
			String stringaDataScadenza;

			Date meseInizio;
			Date meseScadenza;
			for (AttivitaORM attivitaORM : attivitaDAO) {

				stringaDataInizio = sdfMese.format(attivitaORM.getDataInizio());
				stringaDataScadenza = sdfMese.format(attivitaORM.getDataScadenza());

				meseInizio = sdfMese.parse(stringaDataInizio);
				meseScadenza = sdfMese.parse(stringaDataScadenza);
				if (meseInizio.compareTo(mese) <= 0
						&& meseScadenza.compareTo(mese) >= 0) {
					attivitaORM.setCategoriaORM(categorieDAO.queryForId(attivitaORM.getCategoriaORM().getIdCategoria()));
					attivita.add(attivitaORM);
				}

			}
		} catch (ParseException e) {
			// Non serve gestire l'eccezione
		}
		
		return attivita;
	}

	/**
	 * Vengono restituite tutte le categorieORM
	 * presenti con query generale
	 * @return
	 * @throws SQLException
	 */
	
	public List<CategoriaORM> restituisciCategorieORM()
			throws SQLException {
		return categorieDAO.queryForAll();
	}
	
	/**
	 * Vengono restituite tutte le categorie
	 * presenti con trasformazione da categorieORM
	 * @return
	 * @throws SQLException
	 */
	
	public List<Categoria> restituisciCategorie() throws SQLException {
		List<Categoria> categorie = new ArrayList<Categoria>();
		try {

			for (CategoriaORM categoriaORM : restituisciCategorieORM()) {
				categorie.add(categoriaORM.restituisciCategoria());
			}

		} catch (CategoriaOrmNonValidaException conve) {
			throw new SQLException(conve);
		}
		return categorie;
	}
	
	/**
	 * Restituisce tutte le categorieORM associate al contatto.
	 * 
	 * @param contattoORM
	 * @return
	 * @throws SQLException 
	 */
	public List<CategoriaORM> restituisciCategorieORMContatto(ContattoORM contattoORM) throws SQLException {
		
		List<ContattoCategoriaORM> tmpList = contattiCategorieDAO
				.queryForEq(ContattoCategoriaORM.ID_CONTATTO, contattoORM.getIdContatto());
		
		List<CategoriaORM> categorieORM = new ArrayList<CategoriaORM>();
		
		for (ContattoCategoriaORM contattoCategoriaORM : tmpList) {
			categorieORM.add(categorieDAO.queryForId(contattoCategoriaORM.getCategoria().getIdCategoria()));
		}
		return categorieORM;
	}
	
	/**
	 * Data una categoria in input viene
	 * ricercata, può anche non esistere ritornando
	 * null
	 * @param categoria
	 * @return
	 * @throws SQLException
	 */
	
	public CategoriaORM cercaCategoria(Categoria categoria) throws SQLException {
		
		CategoriaORM tmp = new CategoriaORM();
		tmp.setEtichetta(categoria.getEtichetta());
		tmp.setColore(categoria.getColore());
		tmp.setPriorita(categoria.getPriorita());
		
		CategoriaORM risultato;
		risultato = categorieDAO.queryBuilder().where()
			.eq(CategoriaORM.ETICHETTA, tmp.getEtichetta())
			.and()
			.eq(CategoriaORM.COLORE, tmp.getColore())
			.and()
			.eq(CategoriaORM.PRIORITA, tmp.getPriorita())
			.queryForFirst();
		
		return risultato;
	}
	
	/**
	 * Ricerca un impegno all'interno del db
	 * Viene interrogato il database e restituito
	 * l'impegno presente se esiste
	 * Prima di tutto questo viene verificata la presenza
	 * della categoria a cui è associato l'impegno
	 * @param impegno
	 * @return
	 * @throws SQLException
	 */
	
	public ImpegnoORM cercaImpegno(Impegno impegno) throws SQLException {
		
		ImpegnoORM risultato = null;
		try {
			ImpegnoORM tmp = new ImpegnoORM();
			tmp.setTitolo(impegno.getTitolo());
			
			CategoriaORM tmpCategoria = cercaCategoria(impegno.getCategoria());
			
			if (tmpCategoria == null)
				throw new CategoriaOrmNonValidaException("La categoria dell'impegno non e' presente nel DB.");
			
			tmp.setCategoriaORM(tmpCategoria);
			tmp.setDataInizio(impegno.getDataInizio());
			tmp.setDataFine(impegno.getDataFine());
			tmp.setRipetizione(impegno.getRipetizione());
			tmp.setAllarme(impegno.getAllarme());
			tmp.setNote(impegno.getNote());
			
			risultato = impegniDAO.queryBuilder().where()
					.eq(ImpegnoORM.TITOLO, tmp.getTitolo())
					.and()
					.eq(ImpegnoORM.CATEGORIA, tmp.getCategoriaORM())
					.and()
					.eq(ImpegnoORM.DATA_INIZIO, tmp.getDataInizio())
					.and()
					.eq(ImpegnoORM.DATA_FINE, tmp.getDataFine())
					.and()
					.eq(ImpegnoORM.ALLARME, tmp.getAllarme())
					.and()
					.eq(ImpegnoORM.RIPETIZIONE, tmp.getRipetizione())
					.and()
					.eq(ImpegnoORM.NOTE, tmp.getNote())
					.queryForFirst();
			if(risultato != null){
				risultato.setCategoriaORM(tmp.getCategoriaORM());
			}
		} catch(SQLException e) {
			throw new SQLException(e);
		} catch (CategoriaOrmNonValidaException e) {
			throw new SQLException(e);
		}
		return risultato;
	}
	
	/**
	 * Ricerca un attività all'interno del
	 * database. Le possibili eccezioni scaturite 
	 * vengono reindirizzate a sql exception 
	 * che verrà gestito dagli eventuali gestori
	 * @param attivita
	 * @return
	 * @throws SQLException
	 */
	
	public AttivitaORM cercaAttivita(Attivita attivita) throws SQLException {
		
		AttivitaORM risultato = null;
		try {
			AttivitaORM tmp = new AttivitaORM();
			tmp.setTitolo(attivita.getTitolo());
			tmp.setDataInizio(attivita.getDataInizio());
			tmp.setDataScadenza(attivita.getDataScadenza());

			CategoriaORM tmpCategoria = cercaCategoria(attivita.getCategoria());

			if (tmpCategoria == null)
				throw new CategoriaOrmNonValidaException("La categoria associata all'attivita non e' presente nel DB.");
			tmp.setCategoriaORM(tmpCategoria);

			risultato = attivitaDAO.queryBuilder().where().eq(AttivitaORM.TITOLO, tmp.getTitolo()).and()
					.eq(AttivitaORM.DATA_INIZIO, tmp.getDataInizio()).and()
					.eq(AttivitaORM.DATA_SCADENZA, tmp.getDataScadenza()).and()
					.eq(AttivitaORM.CATEGORIA, tmp.getCategoriaORM()).queryForFirst();

			if(risultato != null){
				risultato.setCategoriaORM(tmp.getCategoriaORM());
			}
			
		} catch (SQLException e) {
			throw new SQLException(e);
		} catch (CategoriaOrmNonValidaException e) {
			throw new SQLException(e);
		}
		return risultato;
	}
	
	/**
	 * Ricerca un contatto in memoria
	 * viene verificata l'esistenza
	 * della categoria e in caso contrario viene
	 * lanciata eccezione.
	 * Viene ritornato il contatto oppure null in
	 * caso di non esistenza
	 * @param contatto
	 * @return
	 * @throws SQLException
	 */
	
	public ContattoORM cercaContatto(Contatto contatto) throws SQLException {
		
		ContattoORM risultato = null;
		try {
			ContattoORM tmp = new ContattoORM();
			tmp.setNome(contatto.getNome());
			tmp.setCognome(contatto.getCognome());
			tmp.setEmails(contatto.getEmails());
			tmp.setTelefoni(contatto.getTelefoni());

			/* Se il contatto (escludendo la ricerca delle categorie associate),
			 *esiste gia' allora restituiscilo
			 */
			
			for (Categoria categoria : contatto.getCategorie()) {

				CategoriaORM tmpCategoria = cercaCategoria(categoria);

				if (tmpCategoria == null)
					throw new CategoriaOrmNonValidaException(
							"La categoria associata al contatto non e' presente nel DB.");

			}

			risultato = contattiDAO.queryBuilder().where().eq(ContattoORM.NOME, tmp.getNome()).and()
					.eq(ContattoORM.COGNOME, tmp.getCognome()).and().eq(ContattoORM.EMAILS, tmp.getEmails()).and()
					.eq(ContattoORM.TELEFONI, tmp.getTelefoni()).queryForFirst();
		} catch(SQLException e) {
			throw new SQLException(e);
		} catch (CategoriaOrmNonValidaException e) {
			throw new SQLException(e);
		}
		
		return risultato;
	}
	
	/**
	 * Verifica l'esistenza del contatto in memoria
	 * Viene ritornato un booleano
	 * @param contatto
	 * @return
	 * @throws SQLException
	 */
	
	public boolean verificaEsistenzaContatto(Contatto contatto) throws SQLException {
		return cercaContatto(contatto) != null;
	}
	
	/**
	 * Verifica l'esistenza dell'attività
	 * Restituisce un booleano riguardo alla 
	 * eventuale esistenza
	 * @param attivita
	 * @return
	 * @throws SQLException
	 */
	public boolean verificaEsistenzaAttivita(Attivita attivita) throws SQLException {
		return cercaAttivita(attivita) != null;
	}
	
	/**
	 * Verifica un eventuale impegno già presente
	 * nel db che corrisponde all'impegno da inserire
	 * ritornando un booleano 
	 * @param impegno
	 * @return
	 * @throws SQLException
	 */
	
	public boolean verificaEsistenzaImpegno(Impegno impegno) throws SQLException {
		return cercaImpegno(impegno) != null;
	}
	
	/**
	 * Verifica l'esistenza della categoria
	 * all'interno del db ritornando booleano
	 * @param categoria
	 * @return
	 * @throws SQLException
	 */
	
	public boolean verificaEsistenzaCategoria(Categoria categoria) throws SQLException {
		return cercaCategoria(categoria) != null;
	}
	
	/**
	 * Restituisce tutti i contatti
	 * presenti nel db
	 * @return
	 * @throws SQLException
	 */
	public List<ContattoORM> restituisciContattiORM() throws SQLException {
		return contattiDAO.queryForAll();
	}
	
	public Dao<ImpegnoORM, Integer> getImpegniDAO() {
		return impegniDAO;
	}
	
	public Dao<AttivitaORM, Integer> getAttivitaDAO() {
		return attivitaDAO;
	}
	
	public Dao<CategoriaORM, Integer> getCategorieDAO() {
		return categorieDAO;
	}
	
	public Dao<ContattoORM, Integer> getContattiDAO() {
		return contattiDAO;
	}
	
}
