package uy.edu.fing.mina.simulaciones.pricing;

import java.util.HashSet;
import java.util.Set;

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
		
		State estado1, estado2, estado3;
		Tffst iterador;
		
//		REGLAS DE DEMANDA
//		REGLAS EN CASO DE POCOS CLIENTES
		
//		Regla 1 : if few users and users increasing fast then increase price fast
		estado1 = estados.nuevoEstado();
		estado2 = estados.nuevoEstado();
		estado3 = estados.nuevoEstado();
		estado3.setAccept(true);
		
		estado1.addOutTran(new Transition(eventos.getEvento("fewUsers"), epsilon, estado2));
		estado2.addOutTran(new Transition(eventos.getEvento("usersIncreasingFast"), acciones.getAccion("increasePriceFast"), estado3));
		
		iterador = new Tffst();
		iterador.setInitialState(estado1);
		reglas.add(iterador);
		
//		Regla 2 : if few users and users increasing slow then increase price slow
		estado1 = estados.nuevoEstado();
		estado2 = estados.nuevoEstado();
		estado3 = estados.nuevoEstado();
		estado3.setAccept(true);
		
		estado1.addOutTran(new Transition(eventos.getEvento("fewUsers"), epsilon, estado2));
		estado2.addOutTran(new Transition(eventos.getEvento("usersIncreasingSlow"), acciones.getAccion("increasePriceSlow"), estado3));
		
		iterador = new Tffst();
		iterador.setInitialState(estado1);
		reglas.add(iterador);
		
//		Regla 3 : if few users and users steady then decrease price fast
		estado1 = estados.nuevoEstado();
		estado2 = estados.nuevoEstado();
		estado3 = estados.nuevoEstado();
		estado3.setAccept(true);
		
		estado1.addOutTran(new Transition(eventos.getEvento("fewUsers"), epsilon, estado2));
		estado2.addOutTran(new Transition(eventos.getEvento("usersSteady"), acciones.getAccion("decreasePriceSlow"), estado3));
		
		iterador = new Tffst();
		iterador.setInitialState(estado1);
		reglas.add(iterador);
		
//		Regla 4 : if few users and users decreasing slow then decrease price slow
		estado1 = estados.nuevoEstado();
		estado2 = estados.nuevoEstado();
		estado3 = estados.nuevoEstado();
		estado3.setAccept(true);
		
		estado1.addOutTran(new Transition(eventos.getEvento("fewUsers"), epsilon, estado2));
		estado2.addOutTran(new Transition(eventos.getEvento("usersDecreasingSlow"), acciones.getAccion("decreasePriceSlow"), estado3));
		
		iterador = new Tffst();
		iterador.setInitialState(estado1);
		reglas.add(iterador);
		
//		Regla 5 : if few users and users decreasing fast then decrease price fast
		estado1 = estados.nuevoEstado();
		estado2 = estados.nuevoEstado();
		estado3 = estados.nuevoEstado();
		estado3.setAccept(true);
		
		estado1.addOutTran(new Transition(eventos.getEvento("fewUsers"), epsilon, estado2));
		estado2.addOutTran(new Transition(eventos.getEvento("usersDecreasingFast"), acciones.getAccion("decreasePriceFast"), estado3));
		
		iterador = new Tffst();
		iterador.setInitialState(estado1);
		reglas.add(iterador);
		
//		FIN DE LAS REGLAS PARA EL CASO DE POCOS CLIENTES
		
//		REGLAS PARA EL CASO DE UNA CANTIDAD MEDIA DE CLIENTES
		
//		Regla 6 : if mid users and users increasing fast then increase price slow
		estado1 = estados.nuevoEstado();
		estado2 = estados.nuevoEstado();
		estado3 = estados.nuevoEstado();
		estado3.setAccept(true);
		
		estado1.addOutTran(new Transition(eventos.getEvento("midUsers"), epsilon, estado2));
		estado2.addOutTran(new Transition(eventos.getEvento("usersIncreasingFast"), acciones.getAccion("increasePriceSlow"), estado3));
		
		iterador = new Tffst();
		iterador.setInitialState(estado1);
		reglas.add(iterador);
		
//		Regla 7 : if mid users and users increasing slow then keep price
		estado1 = estados.nuevoEstado();
		estado2 = estados.nuevoEstado();
		estado3 = estados.nuevoEstado();
		estado3.setAccept(true);
		
		estado1.addOutTran(new Transition(eventos.getEvento("midUsers"), epsilon, estado2));
		estado2.addOutTran(new Transition(eventos.getEvento("usersIncreasingSlow"), acciones.getAccion("keepPrice"), estado3));
		
		iterador = new Tffst();
		iterador.setInitialState(estado1);
		reglas.add(iterador);
		
//		Regla 8 : if mid users and users steady then increase price slow
		estado1 = estados.nuevoEstado();
		estado2 = estados.nuevoEstado();
		estado3 = estados.nuevoEstado();
		estado3.setAccept(true);
		
		estado1.addOutTran(new Transition(eventos.getEvento("midUsers"), epsilon, estado2));
		estado2.addOutTran(new Transition(eventos.getEvento("usersSteady"), acciones.getAccion("increasePriceSlow"), estado3));
		
		iterador = new Tffst();
		iterador.setInitialState(estado1);
		reglas.add(iterador);
		
//		Regla 9 : if mid users and users decreasing slow then decrease price slow
		estado1 = estados.nuevoEstado();
		estado2 = estados.nuevoEstado();
		estado3 = estados.nuevoEstado();
		estado3.setAccept(true);
		
		estado1.addOutTran(new Transition(eventos.getEvento("midUsers"), epsilon, estado2));
		estado2.addOutTran(new Transition(eventos.getEvento("usersDecreasingSlow"), acciones.getAccion("decreasePriceSlow"), estado3));
		
		iterador = new Tffst();
		iterador.setInitialState(estado1);
		reglas.add(iterador);
		
//		Regla 10 : if mid users and users decreasing fast then decrease price fast
		estado1 = estados.nuevoEstado();
		estado2 = estados.nuevoEstado();
		estado3 = estados.nuevoEstado();
		estado3.setAccept(true);
		
		estado1.addOutTran(new Transition(eventos.getEvento("midUsers"), epsilon, estado2));
		estado2.addOutTran(new Transition(eventos.getEvento("usersDecreasingFast"), acciones.getAccion("decreasePriceFast"), estado3));
		
		iterador = new Tffst();
		iterador.setInitialState(estado1);
		reglas.add(iterador);
		
//		FIN DE REGLAS PARA EL CASO DE CANTIDAD MEDIA DE CLIENTES
		
//		REGLAS PARA EL CASO DE MUCHOS CLIENTES
		
//		Regla 11 : if lots of users and users increasing fast then increase price slow
		estado1 = estados.nuevoEstado();
		estado2 = estados.nuevoEstado();
		estado3 = estados.nuevoEstado();
		estado3.setAccept(true);
		
		estado1.addOutTran(new Transition(eventos.getEvento("lotsUsers"), epsilon, estado2));
		estado2.addOutTran(new Transition(eventos.getEvento("usersIncreasingFast"), acciones.getAccion("increasePriceSlow"), estado3));
		
		iterador = new Tffst();
		iterador.setInitialState(estado1);
		reglas.add(iterador);
		
//		Regla 12 : if lots of users and users increasing slow then increase price slow
		estado1 = estados.nuevoEstado();
		estado2 = estados.nuevoEstado();
		estado3 = estados.nuevoEstado();
		estado3.setAccept(true);
		
		estado1.addOutTran(new Transition(eventos.getEvento("lotsUsers"), epsilon, estado2));
		estado2.addOutTran(new Transition(eventos.getEvento("usersIncreasingSlow"), acciones.getAccion("increasePriceSlow"), estado3));
		
		iterador = new Tffst();
		iterador.setInitialState(estado1);
		reglas.add(iterador);
		
//		Regla 13 : if lots of users and users steady then keep price
		estado1 = estados.nuevoEstado();
		estado2 = estados.nuevoEstado();
		estado3 = estados.nuevoEstado();
		estado3.setAccept(true);
		
		estado1.addOutTran(new Transition(eventos.getEvento("lotsUsers"), epsilon, estado2));
		estado2.addOutTran(new Transition(eventos.getEvento("usersSteady"), acciones.getAccion("keepPrice"), estado3));
		
		iterador = new Tffst();
		iterador.setInitialState(estado1);
		reglas.add(iterador);
		
//		Regla 14 : if lots of users and users decreasing slow then decrease price slow
		estado1 = estados.nuevoEstado();
		estado2 = estados.nuevoEstado();
		estado3 = estados.nuevoEstado();
		estado3.setAccept(true);
		
		estado1.addOutTran(new Transition(eventos.getEvento("lotsUsers"), epsilon, estado2));
		estado2.addOutTran(new Transition(eventos.getEvento("usersDecreasingSlow"), acciones.getAccion("decreasePriceSlow"), estado3));
		
		iterador = new Tffst();
		iterador.setInitialState(estado1);
		reglas.add(iterador);
		
//		Regla 15 : if lots of users and users decreasing fast then decrease price fast
		estado1 = estados.nuevoEstado();
		estado2 = estados.nuevoEstado();
		estado3 = estados.nuevoEstado();
		estado3.setAccept(true);
		
		estado1.addOutTran(new Transition(eventos.getEvento("lotsUsers"), epsilon, estado2));
		estado2.addOutTran(new Transition(eventos.getEvento("usersDecreasingFast"), acciones.getAccion("decreasePriceFast"), estado3));
		
		iterador = new Tffst();
		iterador.setInitialState(estado1);
		reglas.add(iterador);
		
//		FIN DE REGLAS PARA EL CASO DE MUCHOS CLIENTES
		
//		FIN DE REGLAS PARA LA DEMANDA
		
//		REGLAS PARA LA COMPETENCIA
		
//		REGLAS PARA EL CASO DE QUE LA COMPETENCIA OFREZCA PRECIOS MÁS ALTOS
		
//		Regla 16 : if competitor price higher and competitor price increasing fast then increase price fast
		estado1 = estados.nuevoEstado();
		estado2 = estados.nuevoEstado();
		estado3 = estados.nuevoEstado();
		estado3.setAccept(true);
		
		estado1.addOutTran(new Transition(eventos.getEvento("competitorPriceHigher"), epsilon, estado2));
		estado2.addOutTran(new Transition(eventos.getEvento("competitorPriceIncreasingFast"), acciones.getAccion("increasePriceFast"), estado3));
		
		iterador = new Tffst();
		iterador.setInitialState(estado1);
		reglas.add(iterador);
		
//		Regla 17 : if competitor price higher and competitor price increasing slow then increase price slow
		estado1 = estados.nuevoEstado();
		estado2 = estados.nuevoEstado();
		estado3 = estados.nuevoEstado();
		estado3.setAccept(true);
		
		estado1.addOutTran(new Transition(eventos.getEvento("competitorPriceHigher"), epsilon, estado2));
		estado2.addOutTran(new Transition(eventos.getEvento("competitorPriceIncreasingSlow"), acciones.getAccion("increasePriceFast"), estado3));
		
		iterador = new Tffst();
		iterador.setInitialState(estado1);
		reglas.add(iterador);
		
//		Regla 18 : if competitor price higher and competitor price steady then increase price slow
		estado1 = estados.nuevoEstado();
		estado2 = estados.nuevoEstado();
		estado3 = estados.nuevoEstado();
		estado3.setAccept(true);
		
		estado1.addOutTran(new Transition(eventos.getEvento("competitorPriceHigher"), epsilon, estado2));
		estado2.addOutTran(new Transition(eventos.getEvento("competitorPriceSteady"), acciones.getAccion("increasePriceSlow"), estado3));
		
		iterador = new Tffst();
		iterador.setInitialState(estado1);
		reglas.add(iterador);
		
//		Regla 19 : if competitor price higher and competitor price increasing fast then increase price fast
		estado1 = estados.nuevoEstado();
		estado2 = estados.nuevoEstado();
		estado3 = estados.nuevoEstado();
		estado3.setAccept(true);
		
		estado1.addOutTran(new Transition(eventos.getEvento("competitorPriceHigher"), epsilon, estado2));
		estado2.addOutTran(new Transition(eventos.getEvento("competitorPriceDecreasingFast"), acciones.getAccion("decreasePriceSlow"), estado3));
		
		iterador = new Tffst();
		iterador.setInitialState(estado1);
		reglas.add(iterador);
		
//		FIN DE REGLAS PARA EL CASO DE QUE LA COMPETENCIA OFREZCA PRECIOS MÁS ALTOS
		
//		REGLAS PARA EL CASO DE QUE LA COMPETENCIA OFREZCA PRECIOS MÁS BAJOS
		
//		Regla 20 : if competitor price higher and competitor price increasing fast then increase price fast
		estado1 = estados.nuevoEstado();
		estado2 = estados.nuevoEstado();
		estado3 = estados.nuevoEstado();
		estado3.setAccept(true);
		
		estado1.addOutTran(new Transition(eventos.getEvento("competitorPriceLower"), epsilon, estado2));
		estado2.addOutTran(new Transition(eventos.getEvento("competitorPriceIncreasingFast"), acciones.getAccion("decreasePriceFast"), estado3));
		
		iterador = new Tffst();
		iterador.setInitialState(estado1);
		reglas.add(iterador);
		
//		Regla 21 : if competitor price higher and competitor price increasing fast then increase price fast
		estado1 = estados.nuevoEstado();
		estado2 = estados.nuevoEstado();
		estado3 = estados.nuevoEstado();
		estado3.setAccept(true);
		
		estado1.addOutTran(new Transition(eventos.getEvento("competitorPriceLower"), epsilon, estado2));
		estado2.addOutTran(new Transition(eventos.getEvento("competitorPriceIncreasingSlow"), acciones.getAccion("decreasePriceSlow"), estado3));
		
		iterador = new Tffst();
		iterador.setInitialState(estado1);
		reglas.add(iterador);
		
//		Regla 22 : if competitor price higher and competitor price increasing fast then increase price fast
		estado1 = estados.nuevoEstado();
		estado2 = estados.nuevoEstado();
		estado3 = estados.nuevoEstado();
		estado3.setAccept(true);
		
		estado1.addOutTran(new Transition(eventos.getEvento("competitorPriceLower"), epsilon, estado2));
		estado2.addOutTran(new Transition(eventos.getEvento("competitorPriceSteady"), acciones.getAccion("decreasePriceFast"), estado3));
		
		iterador = new Tffst();
		iterador.setInitialState(estado1);
		reglas.add(iterador);
		
//		Regla 23 : if competitor price higher and competitor price increasing fast then increase price fast
		estado1 = estados.nuevoEstado();
		estado2 = estados.nuevoEstado();
		estado3 = estados.nuevoEstado();
		estado3.setAccept(true);
		
		estado1.addOutTran(new Transition(eventos.getEvento("competitorPriceLower"), epsilon, estado2));
		estado2.addOutTran(new Transition(eventos.getEvento("competitorPriceDecreasingSlow"), acciones.getAccion("decreasePriceSlow"), estado3));
		
		iterador = new Tffst();
		iterador.setInitialState(estado1);
		reglas.add(iterador);

//		Regla 24 : if competitor price higher and competitor price increasing fast then increase price fast
		estado1 = estados.nuevoEstado();
		estado2 = estados.nuevoEstado();
		estado3 = estados.nuevoEstado();
		estado3.setAccept(true);
		
		estado1.addOutTran(new Transition(eventos.getEvento("competitorPriceLower"), epsilon, estado2));
		estado2.addOutTran(new Transition(eventos.getEvento("competitorPriceDecreasingFast"), acciones.getAccion("decreasePriceFast"), estado3));
		
		iterador = new Tffst();
		iterador.setInitialState(estado1);
		reglas.add(iterador);
	
//		FIN DE REGLAS PARA EL CASO DE QUE LA COMPETENCIA OFREZCA PRECIOS MÁS ALTOS
		
//		FIN DE REGLAS PARA LA COMPETENCIA
		
		
	}

}
