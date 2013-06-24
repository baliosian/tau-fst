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
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * <tt>TFFST</tt> state.
 * 
 */
public class State implements Serializable, Comparable<State> {

  static int next_number;
  static final long serialVersionUID = 30001;

  /**
   * @uml.property name="accept"
   */
  private boolean accept;

  private int number;

  /**
   * @uml.property name="transitions"
   */
  private Set<Transition> transitions;

  /** Constructs new state. Initially, the new state is a reject state. */
  public State() {
    resetTransitions();
    number = next_number++;
  }

  /**
   * Adds outgoing transition.
   * 
   * @param t
   *          transition
   */
  public void addTransition(Transition t) {
    transitions.add(t);
  }

  /**
   * Compares this object with the specified object for order. States are
   * ordered by the time of construction.
   */
  public int compareTo(State o) {
    return o.number - number;
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
   * @param transition
   */
  public void removeTransition(Transition transition) {
    transitions.remove(transition);
  }

  /**
   * Sets acceptance for this state.
   * 
   * @param accept
   *          if true, this state is an accept state
   * 
   * @uml.property name="accept"
   */
  public void setAccept(boolean accept) {
    this.accept = accept;
  }

  /**
   * @param transitions
   *          The transitions to set.
   * 
   * @uml.property name="transitions"
   */
  public void setTransitions(HashSet<Transition> transitions) {
    this.transitions = transitions;
  }

  /**
   * Returns string describing this state. Normally invoked via
   * 
   */
  public String toString() {
    if (accept) return "((" + number + "))";
    else
      return "(" + number + ")";
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#clone()
   */
  protected State clone() throws CloneNotSupportedException {
    State s = new State();
    s.resetTransitions();
    // xop Set<Transition> ts = getTransitions();
    for (Iterator<Transition> iter = transitions.iterator(); iter.hasNext();) {
      Transition t = (Transition) iter.next();
      s.transitions.add(t);
    }
    s.setAccept(isAccept());
    return s;
  }

  void addEpsilon(State to) {
    if (to.accept) accept = true;
    Iterator<Transition> i = to.transitions.iterator();
    while (i.hasNext()) {
      Transition t = (Transition) i.next();
      transitions.add(t);
    }
  }

  /**
   * Returns transitions
   * 
   */
  Transition[] getTransitionArray() {
    Transition[] e = (Transition[]) transitions.toArray(new Transition[0]);
    return e;
  }

  /** Resets transition set. */
  public void resetTransitions() {
    transitions = new HashSet<Transition>();
  }

  public int getNumber() {
    return number;
  }

  public void setNumber(int n) {
    number = n;
  }

  public void setTransitions(Set<Transition> transitions) {
    this.transitions = transitions;
  }

}