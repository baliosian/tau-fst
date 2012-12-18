/* 
 * based on 
 *
 * dk.brics.automaton
 * Copyright (C) 2001-2004 Anders Moeller
 * All rights reserved.
 *
 * es.upc.tffst
 * Copyright (C) 2004 Javier Baliosian
 * All rights reserved.
 *
 */

package uy.edu.fing.mina.fsa.tffsr;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


public class State implements Serializable, Comparable<State> {

	static int next_id;

	static final long serialVersionUID = 30001;

	/**
	 * @uml.property  name="accept"
	 */
	public boolean accept;

	public int id;

	public int number;

	/**
	 * @uml.property  name="transitions"
	 */
	public HashSet<Transition> transitions;

	/** Constructs new state. Initially, the new state is a reject state. */
	public State() {
		resetTransitions();
		id = next_id++;
	}

	/**
	 * Adds outgoing transition.
	 * 
	 * @param t
	 *            transition
	 */
	public void addTransition(Transition t) {
		transitions.add(t);
	}

	/**
	 * Compares this object with the specified object for order. States are
	 * ordered by the time of construction.
	 */
	public int compareTo(State o) {
		return o.id - id;
	}

   /**
    * Returns set of outgoing transitions. Subsequent changes are reflected in
    * the automaton.
    * 
    * @return transition set
    * 
    * @uml.property name="transitions"
    */
   public Set<Transition> getTransitions() {
      return transitions;
   }

   /**
    * Returns acceptance status.
    * 
    * @return true is this is an accept state
    * 
    * @uml.property name="accept"
    */
   public boolean isAccept() {
      return accept;
   }

   /**
    * Sets acceptance for this state.
    * 
    * @param accept
    *            if true, this state is an accept state
    * 
    * @uml.property name="accept"
    */
   public void setAccept(boolean accept) {
      this.accept = accept;
   }

//	/**
//	 * Performs a state lookup in transitions.
//	 * 
//	 * @param c
//	 *            character to look up
//	 * @return destination state, null if no matching outgoing transition
//	 */
//	public State stepTo(MessageI e) {
//		Iterator i = transitions.iterator();
//		while (i.hasNext()) {
//			Transition t = (Transition) i.next();
//			if (t.label.evaluate(e) >= 0)
//				return t.to;
//		}
//		return null;
//	}

	/**
	 * Returns string describing this state. Normally invoked via
	 * {@link Automaton#toString()}.
	 */
	public String toString() {
		StringBuffer b = new StringBuffer();
		b.append("state ").append(number).append("/").append(id);
		if (accept)
			b.append(" [final]");
		b.append(": ");
		Iterator<Transition> i = transitions.iterator();
		while (i.hasNext()) {
			Transition t = (Transition) i.next();
			b.append("  ").append(t.toString()).append(",");
		}
		b.append("\n");
		return b.toString();
	}

	/**
	 * adds an epsilon transition but not realy. in fact it adds the necesary
	 * non epsilon transitions. it adds initial states. 
	 * 
	 * @param to
	 */
	void addEpsilon(State to) {
		if (to.accept)
			accept = true;
		Iterator<Transition> i = to.transitions.iterator();
		while (i.hasNext()) {
			Transition t = (Transition) i.next();
			transitions.add(t);
		}
	}

	/**
	 * Returns transitions sorted by (min, reverse max, to) or (to, min, reverse
	 * max)
	 */
	Transition[] getTransitionArray() {
		Transition[] e = (Transition[]) transitions.toArray(new Transition[0]);
		return e;
	}

	List<Transition> getTransitions(boolean to_first) {
		return Arrays.asList(getTransitionArray());
	}

	/** Resets transition set. */
	public void resetTransitions() {
		transitions = new HashSet<Transition>();
	}

}