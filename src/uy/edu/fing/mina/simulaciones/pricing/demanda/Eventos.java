package uy.edu.fing.mina.simulaciones.pricing.demanda;

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
		nuevo.setName("fewUsers");
		conjuntoEventos.put(nuevo.getName(), nuevo);
		nuevo = new EventTf();
		nuevo.setName("midUsers");
		conjuntoEventos.put(nuevo.getName(), nuevo);
		nuevo = new EventTf();
		nuevo.setName("lotsUsers");
		conjuntoEventos.put(nuevo.getName(), nuevo);
		nuevo = new EventTf();
		nuevo.setName("usersIncreasingFast");
		conjuntoEventos.put(nuevo.getName(), nuevo);
		nuevo = new EventTf();
		nuevo.setName("usersIncreasingSlow");
		conjuntoEventos.put(nuevo.getName(), nuevo);
		nuevo = new EventTf();
		nuevo.setName("usersSteady");
		conjuntoEventos.put(nuevo.getName(), nuevo);
		nuevo = new EventTf();
		nuevo.setName("usersDecreasingSlow");
		conjuntoEventos.put(nuevo.getName(), nuevo);
		nuevo = new EventTf();
		nuevo.setName("usersDecreasingFast");
		conjuntoEventos.put(nuevo.getName(), nuevo);
	}
	
	public EventTf getEvento(String name){
		return conjuntoEventos.get(name);
	}
}
