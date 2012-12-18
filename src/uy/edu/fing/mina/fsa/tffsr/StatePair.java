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

/**
 * Pair of states.
 * 
 * @author Anders M&oslash;ller &lt; <a
 *         href="mailto:amoeller@brics.dk">amoeller@brics.dk </a>&gt;
 */

public class StatePair {

   /**
    * 
    * @uml.property name="s"
    * @uml.associationEnd multiplicity="(0 1)"
    */
   State s;

   /**
    * 
    * @uml.property name="s1"
    * @uml.associationEnd multiplicity="(0 1)"
    */
   State s1;

   /**
    * 
    * @uml.property name="s2"
    * @uml.associationEnd multiplicity="(0 1)"
    */
   State s2;

	StatePair(State s, State s1, State s2) {
		this.s = s;
		this.s1 = s1;
		this.s2 = s2;
	}

	/**
	 * Constructs new state pair.
	 * 
	 * @param s1
	 *            first state
	 * @param s2
	 *            second state
	 */
	public StatePair(State s1, State s2) {
		this.s1 = s1;
		this.s2 = s2;
	}

	/**
	 * Returns first component of this pair.
	 * 
	 * @return first state
	 */
	public State getFirstState() {
		return s1;
	}

	/**
	 * Returns second component of this pair.
	 * 
	 * @return second state
	 */
	public State getSecondState() {
		return s2;
	}

	/**
	 * Checks for equality.
	 * 
	 * @param obj
	 *            object to compare with
	 * @return true if <tt>obj</tt> represents the same pair of states as this
	 *         pair
	 */
	public boolean equals(Object obj) {
		if (obj instanceof StatePair) {
			StatePair p = (StatePair) obj;
			return p.s1 == s1 && p.s2 == s2;
		} else
			return false;
	}

	/**
	 * Returns hash code.
	 * 
	 * @return hash code
	 */
	public int hashCode() {
		return s1.hashCode() + s2.hashCode();
	}
}