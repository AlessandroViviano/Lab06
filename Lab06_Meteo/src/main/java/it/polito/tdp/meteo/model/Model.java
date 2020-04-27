package it.polito.tdp.meteo.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import it.polito.tdp.meteo.DAO.MeteoDAO;

public class Model {
	
	private final static int COST = 100;
	private final static int NUMERO_GIORNI_CITTA_CONSECUTIVI_MIN = 3;
	private final static int NUMERO_GIORNI_CITTA_MAX = 6;
	private final static int NUMERO_GIORNI_TOTALI = 15;
	
	private List<Citta> leCitta ;
	private List<Citta> bestSoluzione ;
	
	public Model() {
		MeteoDAO dao = new MeteoDAO();
		this.leCitta = dao.getAllCitta();
	}
	
	
	public List<Citta> getLeCitta(){
		return leCitta;
	}
	
	public double calcolaMedia(List<Rilevamento> lista) {
		
		int somma = 0;
		int contatore = 0;
		
		for(Rilevamento r: lista) {
			somma += r.getUmidita();
			contatore++;
		}
		
		return (somma/contatore);
	}

	// of course you can change the String output with what you think works best
	public String getUmiditaMedia(int mese) {
		
		String str = "";
		
		MeteoDAO dao = new MeteoDAO();
		
		str += "Genova "+calcolaMedia(dao.getAllRilevamentiLocalitaMese(mese, "Genova"))+"\n"+
			   "Milano "+calcolaMedia(dao.getAllRilevamentiLocalitaMese(mese, "Milano"))+"\n"+
			   "Torino "+calcolaMedia(dao.getAllRilevamentiLocalitaMese(mese, "Torino"))+"\n";
		
		return str;
	}
	
	// of course you can change the String output with what you think works best
	public List<Citta> trovaSequenza(int mese) {
		
		List<Citta> parziale = new ArrayList<>();
		
		this.bestSoluzione = null;
		
		MeteoDAO dao = new MeteoDAO();
		
		//Carica in ciascuna istanza di leCitta la lista di rilevamenti nel mese considerato
		for(Citta c: leCitta)
			c.setRilevamenti(dao.getAllRilevamentiLocalitaMese(mese, c.getNome()));
		
		cerca(parziale, 0);
		
		return bestSoluzione;
	}
	
	
	private void cerca(List<Citta> parziale, int livello) {
		
		if(livello == 15) {
			//Caso terminale
			double costo = calcolaCosto(parziale);
			if(bestSoluzione == null || costo < calcolaCosto(bestSoluzione))
				bestSoluzione = new ArrayList<>(parziale);
		}
		else {
			//Caso intermedio
			for(Citta prova: leCitta) {
				if(aggiuntaValida(prova, parziale)) {
					parziale.add(prova);
					cerca(parziale, livello+1);
					parziale.remove(parziale.size()-1);
				}
			}
		}
	}
	
	private double calcolaCosto(List<Citta> parziale) {
		
		double costo = 0.0;
		
		for(int giorno = 1 ; giorno < 15 ; giorno ++) {
			//Dove mi trovo
			Citta c = parziale.get(giorno-1);
			//Prendo l'umidità che ho in quel giorno in quella città
			double umid = c.getRilevamenti().get(giorno-1).getUmidita();
			costo += umid;
		}
		//Poi faccio la somma di 100 per ogni volta che cambio città
		for(int giorno = 2 ; giorno <= 15 ; giorno ++) {
			if(!parziale.get(giorno-1).equals(parziale.get(giorno-2)))
				costo += 100;
		}
		return costo;
	}
	
	private boolean aggiuntaValida(Citta prova, List<Citta> parziale) {
		
		//Conto quante volte la città è già comparsa in parziale
		int conta = 0;
		for(Citta precedente: parziale) {
			if(precedente.equals(prova))
				conta++;
		}
		
		if(conta>=6)
			return false;
		
		//Verifica dei giorni minimi ( 3 giorni )
		if(parziale.size()==0) //Al primo giorno posso aggiungere qualsiasi città
			return true;
		
		if(parziale.size()==1 || parziale.size()==2) {
			//Al secondo o terzo giorno la città deve essere uguale alla prima
			return parziale.get(parziale.size()-1).equals(prova);
		}
		
		//Se ho passato i controlli precedenti posso anche rimanere nella stessa città
		if(parziale.get(parziale.size()-1).equals(prova))
			return true;
		
		//Ma se cambio città devo assicurarmi che sono stato fermo i tre giorni precedenti
		if(parziale.get(parziale.size()-1).equals(parziale.get(parziale.size()-2))
				&& parziale.get(parziale.size()-2).equals(parziale.get(parziale.size()-3)))
			return true;
		
		return false;
	}
	
	/*public void setRilevamenti(int mese, List<Citta> citta) {
		MeteoDAO dao = new MeteoDAO();
		
		for(Citta c: citta) {
			c.setRilevamenti(dao.getAllRilevamentiPrimi15Giorni(mese, c.getNome()));
		}
	}*/

	
	

}
