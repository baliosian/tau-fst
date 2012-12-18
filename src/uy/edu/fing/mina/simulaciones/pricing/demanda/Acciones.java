package uy.edu.fing.mina.simulaciones.pricing.demanda;

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
		nuevo.setSLabel("increasePriceFast");
		
		conjuntoAcciones.put(nuevo.getSLabel(), nuevo);
		nuevo = new ActionTf();
		nuevo.setSLabel("increasePriceSlow");
		conjuntoAcciones.put(nuevo.getSLabel(), nuevo);
		nuevo = new ActionTf();
		nuevo.setSLabel("decreasePriceSlow");
		conjuntoAcciones.put(nuevo.getSLabel(), nuevo);
		nuevo = new ActionTf();
		nuevo.setSLabel("decreasePriceFast");
		conjuntoAcciones.put(nuevo.getSLabel(), nuevo);
		nuevo = new ActionTf();
		nuevo.setSLabel("keepPrice");
		conjuntoAcciones.put(nuevo.getSLabel(), nuevo);
	}
	
	public ActionTf getAccion(String nombre){
		return conjuntoAcciones.get(nombre);
	}

}
