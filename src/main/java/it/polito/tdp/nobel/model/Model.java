package it.polito.tdp.nobel.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import it.polito.tdp.nobel.db.EsameDAO;

public class Model {	
	
	private List<Esame> allEsami;
	private Set<Esame> migliore;
	private double mediaMigliore;
	
	public Model() {
		EsameDAO dao = new EsameDAO();
		this.allEsami = dao.getTuttiEsami();
	}
	
	public Set<Esame> calcolaSottoinsiemeEsami(int numeroCrediti) {
		migliore = new HashSet<>();
		mediaMigliore = 0.0;
		
		Set<Esame> parziale = new HashSet<>();
		
		cercaMeglio(parziale, 0, numeroCrediti);
		
		return migliore;		
	}
	
	private void cercaMeglio(Set<Esame> parziale, int livello, int numeroCrediti) {
		int somma = sommaCrediti(parziale);
		
		if(somma > numeroCrediti) {
			//non ho una soluzione
			return;
		}
		
		if (somma == numeroCrediti) {
			//posso avere una soluzione
			double mediaVoti = calcolaMedia(parziale);
			if(mediaVoti > mediaMigliore) {
				mediaMigliore = mediaVoti;
				migliore = new HashSet<>(parziale);
			}
			return;
		}
		
		if(livello == allEsami.size()) {
			return;
			
		}
		
		//provo ad aggiungere il prossimo elemento
		//livello = 0 {e1} / {}
		//livello = 1 {e1, e2} - {e1} / {e2} - {}
		
		parziale.add(allEsami.get(livello));
		cercaMeglio(parziale, livello+1, numeroCrediti);
		parziale.remove(allEsami.get(livello));
		
		//provo a non aggiungere il prossimo elemento
		cercaMeglio(parziale, livello+1, numeroCrediti);
		
	}
	
	private void cerca(Set<Esame> parziale, int livello, int numeroCrediti) {
		//condizioni di uscita
		int somma = sommaCrediti(parziale);
		
		if(somma > numeroCrediti) {
			//non ho una soluzione
			return;
		}
		
		if (somma == numeroCrediti) {
			//posso avere una soluzione
			double mediaVoti = calcolaMedia(parziale);
			if(mediaVoti > mediaMigliore) {
				mediaMigliore = mediaVoti;
				migliore = new HashSet<>(parziale);
			}
			return;
		}
		
		if(livello == allEsami.size()) {
			return;
			
		}
		
		//se sono arrivato a questo punto: numeroCrediti > sommaCrediti, posso continuare con la ricorsione
		for(Esame e : allEsami) {
			if(!parziale.contains(e)) {
				parziale.add(e);
				cerca(parziale, livello+1, numeroCrediti);
				parziale.remove(e);
			}
		}
		
	}

	public double calcolaMedia(Set<Esame> esami) {
		
		int crediti = 0;
		int somma = 0;
		
		for(Esame e : esami){
			crediti += e.getCrediti();
			somma += (e.getVoto() * e.getCrediti());
		}
		
		return somma/crediti;
	}
	
	public int sommaCrediti(Set<Esame> esami) {
		int somma = 0;
		
		for(Esame e : esami)
			somma += e.getCrediti();
		
		return somma;
	}

}
