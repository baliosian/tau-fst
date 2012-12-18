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
		nuevo.setSLabel("generateCompetitorPriceEvents");
		conjuntoAcciones.put(nuevo.getSLabel(), nuevo);
		nuevo = new ActionTf();
		nuevo.setSLabel("generateNumberOfClientsEvents");
		conjuntoAcciones.put(nuevo.getSLabel(), nuevo);
//		nuevo.setSLabel("generateCompetitorPriceLower");
//		conjuntoAcciones.put(nuevo.getSLabel(), nuevo);
//		nuevo = new ActionTf();
//		nuevo.setSLabel("generateCompetitorPriceIncreasingFast");
//		conjuntoAcciones.put(nuevo.getSLabel(), nuevo);
//		nuevo = new ActionTf();
//		nuevo.setSLabel("generateCompetitorPriceIncreasingSlow");
//		conjuntoAcciones.put(nuevo.getSLabel(), nuevo);
//		nuevo = new ActionTf();
//		nuevo.setSLabel("generateCompetitorPriceDecreasingFast");
//		conjuntoAcciones.put(nuevo.getSLabel(), nuevo);
//		nuevo = new ActionTf();
//		nuevo.setSLabel("generateCompetitorPriceDecreasingSlow");
//		conjuntoAcciones.put(nuevo.getSLabel(), nuevo);
//		nuevo = new ActionTf();
//		nuevo.setSLabel("generateCompetitorPriceSteady");
//		conjuntoAcciones.put(nuevo.getSLabel(), nuevo);
//		nuevo = new ActionTf();
//		nuevo.setSLabel("generateUsersHigh");
//		conjuntoAcciones.put(nuevo.getSLabel(), nuevo);
//		nuevo = new ActionTf();
//		nuevo.setSLabel("generateUsersLow");
//		conjuntoAcciones.put(nuevo.getSLabel(), nuevo);
//		nuevo = new ActionTf();
//		nuevo.setSLabel("generateUsersMid");
//		conjuntoAcciones.put(nuevo.getSLabel(), nuevo);
//		
//		nuevo = new ActionTf();
//		nuevo.setSLabel("generateUsersDecreasingSlow");
//		conjuntoAcciones.put(nuevo.getSLabel(), nuevo);
//		nuevo = new ActionTf();
//		nuevo.setSLabel("generateUsersDecreasingFast");
//		conjuntoAcciones.put(nuevo.getSLabel(), nuevo);
//		
//		nuevo = new ActionTf();
//		nuevo.setSLabel("generateUsersIncreasingSlow");
//		conjuntoAcciones.put(nuevo.getSLabel(), nuevo);
//		nuevo = new ActionTf();
//		nuevo.setSLabel("generateUsersIncreasingFast");
//		conjuntoAcciones.put(nuevo.getSLabel(), nuevo);
//		
//		nuevo = new ActionTf();
//		nuevo.setSLabel("generateUsersSteady");
//		conjuntoAcciones.put(nuevo.getSLabel(), nuevo);
		
	}
	
	public ActionTf getAccion(String nombre){
		return conjuntoAcciones.get(nombre);
	}

}
