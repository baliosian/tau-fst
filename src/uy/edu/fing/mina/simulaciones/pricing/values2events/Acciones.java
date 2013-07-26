package uy.edu.fing.mina.simulaciones.pricing.values2events;

import java.util.HashMap;
import java.util.Map;

import uy.edu.fing.mina.lupa.tf.ActionTf;

public class Acciones {
	private Map<String, ActionTf> conjuntoAcciones;
	private static Acciones instancia;
	
	private Acciones(){
		super();
		this.conjuntoAcciones = new HashMap<String,ActionTf>();
		this.popular();
	}
	
	public static Acciones getInstance(){
		if(instancia == null){
			instancia = new Acciones();
		}
		return instancia;
	}
	
	
	private void  popular(){
		ActionTf nuevo = new ActionTf();
		nuevo.setName("generateCompetitorPriceEvents");
		conjuntoAcciones.put(nuevo.getName(), nuevo);
		nuevo = new ActionTf();
		nuevo.setName("generateNumberOfClientsEvents");
		conjuntoAcciones.put(nuevo.getName(), nuevo);
//		nuevo.setName("generateCompetitorPriceLower");
//		conjuntoAcciones.put(nuevo.getSLabel(), nuevo);
//		nuevo = new ActionTf();
//		nuevo.setName("generateCompetitorPriceIncreasingFast");
//		conjuntoAcciones.put(nuevo.getSLabel(), nuevo);
//		nuevo = new ActionTf();
//		nuevo.setName("generateCompetitorPriceIncreasingSlow");
//		conjuntoAcciones.put(nuevo.getSLabel(), nuevo);
//		nuevo = new ActionTf();
//		nuevo.setName("generateCompetitorPriceDecreasingFast");
//		conjuntoAcciones.put(nuevo.getSLabel(), nuevo);
//		nuevo = new ActionTf();
//		nuevo.setName("generateCompetitorPriceDecreasingSlow");
//		conjuntoAcciones.put(nuevo.getSLabel(), nuevo);
//		nuevo = new ActionTf();
//		nuevo.setName("generateCompetitorPriceSteady");
//		conjuntoAcciones.put(nuevo.getSLabel(), nuevo);
//		nuevo = new ActionTf();
//		nuevo.setName("generateUsersHigh");
//		conjuntoAcciones.put(nuevo.getSLabel(), nuevo);
//		nuevo = new ActionTf();
//		nuevo.setName("generateUsersLow");
//		conjuntoAcciones.put(nuevo.getSLabel(), nuevo);
//		nuevo = new ActionTf();
//		nuevo.setName("generateUsersMid");
//		conjuntoAcciones.put(nuevo.getSLabel(), nuevo);
//		
//		nuevo = new ActionTf();
//		nuevo.setName("generateUsersDecreasingSlow");
//		conjuntoAcciones.put(nuevo.getSLabel(), nuevo);
//		nuevo = new ActionTf();
//		nuevo.setName("generateUsersDecreasingFast");
//		conjuntoAcciones.put(nuevo.getSLabel(), nuevo);
//		
//		nuevo = new ActionTf();
//		nuevo.setName("generateUsersIncreasingSlow");
//		conjuntoAcciones.put(nuevo.getSLabel(), nuevo);
//		nuevo = new ActionTf();
//		nuevo.setName("generateUsersIncreasingFast");
//		conjuntoAcciones.put(nuevo.getSLabel(), nuevo);
//		
//		nuevo = new ActionTf();
//		nuevo.setName("generateUsersSteady");
//		conjuntoAcciones.put(nuevo.getSLabel(), nuevo);
		
	}
	
	public ActionTf getAccion(String nombre){
		return conjuntoAcciones.get(nombre);
	}

}
