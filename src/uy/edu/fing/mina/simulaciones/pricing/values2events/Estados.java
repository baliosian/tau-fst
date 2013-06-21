package uy.edu.fing.mina.simulaciones.pricing.values2events;

import java.util.HashSet;
import java.util.Set;

import uy.edu.fing.mina.fsa.tffst.State;

public class Estados {
	private Set<State> conjuntoEstados = new HashSet<State>();
	private State estadoInicial;
	private int numerador;
	private static Estados instancia;
	
	private Estados(){
		super();
		numerador = 0;
		estadoInicial = new State();
		estadoInicial.setNumber(numerador);
		conjuntoEstados.add(estadoInicial);
		numerador++;
	}
	
	public static Estados getInstance(){
		if(Estados.instancia == null){
			Estados.instancia = new Estados();
		}
		return instancia;
	}

	public State getEstadoInicial() {
		return estadoInicial;
	}
	
	public State nuevoEstado(){
		State nuevo = new State();
		nuevo.setNumber(numerador);
		conjuntoEstados.add(nuevo);
		numerador++;
		return nuevo;
	}
	

}
