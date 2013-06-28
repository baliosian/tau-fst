package uy.edu.fing.mina.simulaciones.pricing;

import java.util.HashMap;
import java.util.Map;

import uy.edu.fing.mina.lupa.tf.EventTf;

public class Eventos {
	private Map<String, EventTf> conjuntoEventos;
	private static Eventos instancia;
	
	private Eventos(){
		super();
		this.conjuntoEventos = new HashMap<String,EventTf>();
		this.popular();
	}
	
	public static Eventos getInstance(){
		if(instancia == null){
			instancia = new Eventos();
		}
		return instancia;
	}
	
	private void popular(){
		EventTf nuevo = new EventTf();
		nuevo.setSLabel("fewUsers");
		conjuntoEventos.put(nuevo.getName(), nuevo);
		nuevo = new EventTf();
		nuevo.setSLabel("midUsers");
		conjuntoEventos.put(nuevo.getName(), nuevo);
		nuevo = new EventTf();
		nuevo.setSLabel("lotsUsers");
		conjuntoEventos.put(nuevo.getName(), nuevo);
		nuevo = new EventTf();
		nuevo.setSLabel("usersIncreasingFast");
		conjuntoEventos.put(nuevo.getName(), nuevo);
		nuevo = new EventTf();
		nuevo.setSLabel("usersIncreasingSlow");
		conjuntoEventos.put(nuevo.getName(), nuevo);
		nuevo = new EventTf();
		nuevo.setSLabel("usersSteady");
		conjuntoEventos.put(nuevo.getName(), nuevo);
		nuevo = new EventTf();
		nuevo.setSLabel("usersDecreasingSlow");
		conjuntoEventos.put(nuevo.getName(), nuevo);
		nuevo = new EventTf();
		nuevo.setSLabel("usersDecreasingFast");
		conjuntoEventos.put(nuevo.getName(), nuevo);
		nuevo = new EventTf();
		nuevo.setSLabel("competitorPriceHigher");
		conjuntoEventos.put(nuevo.getName(), nuevo);
		nuevo = new EventTf();
		nuevo.setSLabel("competitorPriceLower");
		conjuntoEventos.put(nuevo.getName(), nuevo);
		nuevo = new EventTf();
		nuevo.setSLabel("competitorPriceIncreasingFast");
		conjuntoEventos.put(nuevo.getName(), nuevo);
		nuevo = new EventTf();
		nuevo.setSLabel("competitorPriceIncreasingSlow");
		conjuntoEventos.put(nuevo.getName(), nuevo);
		nuevo = new EventTf();
		nuevo.setSLabel("competitorPriceSteady");
		conjuntoEventos.put(nuevo.getName(), nuevo);
		nuevo = new EventTf();
		nuevo.setSLabel("competitorPriceDecreasingSlow");
		conjuntoEventos.put(nuevo.getName(), nuevo);
		nuevo = new EventTf();
		nuevo.setSLabel("competitorPriceDecreasingFast");
		conjuntoEventos.put(nuevo.getName(), nuevo);
	}
	
	public EventTf getEvento(String name){
		return conjuntoEventos.get(name);
	}
}
