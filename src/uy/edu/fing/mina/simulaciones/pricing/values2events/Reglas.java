package uy.edu.fing.mina.simulaciones.pricing.values2events;

import java.util.HashSet;
import java.util.Set;

import uy.edu.fing.mina.fsa.tf.TfI;
import uy.edu.fing.mina.fsa.tffst.State;
import uy.edu.fing.mina.fsa.tffst.Tffst;
import uy.edu.fing.mina.fsa.tffst.Transition;
import uy.edu.fing.mina.lupa.tf.ActionTf;

public class Reglas {
	private static Reglas instancia;
	public Set<Tffst> reglas = new HashSet<Tffst>();
	
	public static Reglas getInstance(){
		if(instancia == null){
			instancia = new Reglas();
		}
		return instancia;
	}
	
	private Reglas(){
		super();
	}
	
	public Set<Tffst> getReglas(){
		return reglas;
	}
	
	public void construirReglas(){
		
		ActionTf epsilon = new ActionTf();
		epsilon.setEpsilon();
		Eventos eventos = Eventos.getInstance();
		Acciones acciones = Acciones.getInstance();
		Estados estados = Estados.getInstance();
		
		State estado1, estado2;
		Tffst iterador;
		
//		REGLAS DE PASAJE DE EVENTOS DE PRECIOS VECINOS A EVENTOS COMPETITOR_PRICE_x
		
		estado1 = estados.nuevoEstado();
		estado2 = estados.nuevoEstado();
		estado2.setAccept(true);
		
		TfI accion = acciones.getAccion("generateCompetitorPriceEvents");
		
		estado1.addOutTran(new Transition(eventos.getEvento("localPriceInformation"), accion, estado2));
		
		iterador = new Tffst();
		iterador.setInitialState(estado1);
		reglas.add(iterador);
		
		//////////////////////////////////////////////////////////////
		
		estado1 = estados.nuevoEstado();
		estado2 = estados.nuevoEstado();
		estado2.setAccept(true);
		
		accion = acciones.getAccion("generateNumberOfClientsEvents");
		
		estado1.addOutTran(new Transition(eventos.getEvento("localClientsInformation"), accion, estado2));
		
		iterador = new Tffst();
		iterador.setInitialState(estado1);
		reglas.add(iterador);
		
//		estado1 = estados.nuevoEstado();
//		estado2 = estados.nuevoEstado();
//		estado2.setAccept(true);
//		
//		TfI accion = acciones.getAccion("generateCompetitorPriceLower").and(acciones.getAccion("generateCompetitorPriceHigher"));
//		accion = accion.and(acciones.getAccion("generateCompetitorPriceIncreasingFast").and(acciones.getAccion("generateCompetitorPriceIncreasingSlow")));
//		accion = accion.and(acciones.getAccion("generateCompetitorPriceDecreasingFast").and(acciones.getAccion("generateCompetitorPriceDecreasingSlow")));
//		accion = accion.and(acciones.getAccion("generateCompetitorPriceSteady"));
//		
//		estado1.addTransition(new Transition(eventos.getEvento("localPriceInformation"), accion, estado2));
//		
//		iterador = new Tffst();
//		iterador.setInitialState(estado1);
//		reglas.add(iterador);
		
//		estado1 = estados.nuevoEstado();
//		estado2 = estados.nuevoEstado();
//		estado2.setAccept(true);
//		
//		accion = acciones.getAccion("generateUsersHigh").and(acciones.getAccion("generateUsersMid").and(acciones.getAccion("generateUsersLow")));
//		accion = accion.and(acciones.getAccion("generateUsersIncreasingFast").and(acciones.getAccion("generateUsersIncreasingSlow")));
//		accion = accion.and(acciones.getAccion("generateUsersDecreasingFast").and(acciones.getAccion("generateUsersDecreasingSlow")));
//		accion = accion.and(acciones.getAccion("generateUsersSteady"));
//		
//		estado1.addTransition(new Transition(eventos.getEvento("localUsersInformation"), accion, estado2));
		
//		iterador = new Tffst();
//		iterador.setInitialState(estado1);
//		reglas.add(iterador);
		
//		FIN DE REGLAS PARA LA COMPETENCIA
		
		
	}

}
