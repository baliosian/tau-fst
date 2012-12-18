/*
 * Finite State Transducers extended with Tautness Functions by Copyright (C)
 * 2004 Javier Baliosian All rights reserved.
 */

package uy.edu.fing.mina.fsa.tffst;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import uy.edu.fing.mina.fsa.tf.CompositeTf;
import uy.edu.fing.mina.fsa.tf.Partition;
import uy.edu.fing.mina.fsa.tf.SimpleTf;
import uy.edu.fing.mina.fsa.tf.TfI;
import uy.edu.fing.mina.fsa.tf.TfPair;
import uy.edu.fing.mina.fsa.tf.TfString;
import uy.edu.fing.mina.fsa.tffsr.Tffsr;
import uy.edu.fing.mina.fsa.utils.PermutationGenerator;
import uy.edu.fing.mina.fsa.utils.Utils;

/*
 * Class invariants: - An TFFST is represented explicitly (with State and
 * Transition objects). If a TFFST is nondeterministic, then isDeterministic()
 * returns false (but the converse is not required).
 */

public class Tffst implements Serializable {

	/** Minimize always flag. */
	static boolean minimize_always;

	static final long serialVersionUID = 10001;

	/**
	 * If true, then this Tffst is definitely deterministic (i.e., there are no
	 * choices for any run, but a run may crash).
	 * 
	 * @uml.property name="deterministic"
	 */
	boolean deterministic;

	/** Hash code. Recomputed by {@link #minimize()}. */
	int hash_code;

	/**
	 * Extra data associated with this Tffst.
	 * 
	 * @uml.property name="info"
	 */
	Object info;

	/**
	 * Initial state of this Tffst.
	 * 
	 * @uml.property name="initial"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
	State initial;

	/**
	 * Constructs new Tffst that accepts the empty language. Using this
	 * constructor, Tffst can be constructed manually from {@link State}and
	 * {@link Transition}objects.
	 * 
	 * @see #setInitialState(State)
	 * @see State
	 * @see Transition
	 */
	public Tffst() {
		this.initial = new State();
		this.deterministic = true;
	}

	/**
	 * CONCATENATION Returns new Tffst that accepts the concatenation of the
	 * languages of the given automata.
	 * <p>
	 * Complexity: linear in total number of states.
	 */
	static public Tffst concatenate(List<Tffst> l) {
		if (l.isEmpty())
			return makeEmptyString();
		Iterator<Tffst> i = l.iterator();
		Tffst b = i.next().cloneExpanded();
		Iterator<State> j = b.getAcceptStates().iterator();
		while (i.hasNext()) {
			Tffst a = ((Tffst) i.next()).cloneExpanded();
			Set<State> ns = a.getAcceptStates();
			while (j.hasNext()) {
				State s = j.next();
				s.accept = false;
				s.addEpsilon(a.initial);
				if (s.accept)
					ns.add(s);
			}
			j = ns.iterator();
		}
		b.deterministic = false;
		b.checkMinimizeAlways();
		return b;
	}
	
	
	/** Returns new (deterministic) automaton with the empty language. */
	public static Tffst makeEmpty() {
		Tffst a = new Tffst();
		State s = new State();
		a.initial = s;
		a.deterministic = true;
		return a;
	}

	public static Tffst makeEmptyString() {
		Tffst a = new Tffst();
		State s0 = new State();
		a.initial = s0;
		State s1 = new State();
		s1.setAccept(true);
		Transition t = new Transition(new SimpleTf(TfI.MIN_TF), new SimpleTf(
				TfI.MIN_TF), s1);
		s0.addTransition(t);
		a.deterministic = true;
		return a;
	}

	/** Returns new (deterministic) tffst that acceps everything. */
	public static Tffst all() {
		Tffst a = new Tffst();
		State s0 = new State();
		a.initial = s0;
		State s1 = new State();
		s1.setAccept(true);
		Transition t = new Transition(SimpleTf.AcceptsAll(), SimpleTf.AcceptsAll(), s1, 1);
		s0.addTransition(t);
		a.deterministic = true;
		a = a.kleene();
		return a;
	}

	/**
	 * Sets or resets minimize always flag. If this flag is set, then
	 * {@link #minimize()}will automatically be invoked after all operations
	 * that otherwise may produce non-minimal automata. By default, the flag is
	 * not set.
	 * 
	 * @param flag
	 *            if true, the flag is set
	 */
	static public void setMinimizeAlways(boolean flag) {
		minimize_always = flag;
	}

	/**
	 * Returns new Tffst that accepts the union of the languages of the given
	 * automata.
	 * <p>
	 * Complexity: linear in number of states.
	 */
	static public Tffst union(List<Tffst> l) {
		State s = new State();
		Iterator<Tffst> i = l.iterator();
		while (i.hasNext()) {
			Tffst b = (i.next()).cloneExpanded();
			s.addEpsilon(b.initial);
		}
		Tffst a = new Tffst();
		a.initial = s;
		a.deterministic = false;
		a.checkMinimizeAlways();
		return a;
	}

	/**
	 * Assign another Tffst to this
	 * 
	 * @param Tffst
	 */
	public void assign(Tffst tffsr) {
		this.initial = tffsr.initial;
		this.deterministic = tffsr.deterministic;
		this.info = tffsr.info;
	}

	/**
	 * Returns a clone of this Tffst.
	 */
	public Object clone() {

		Tffst a = new Tffst();
		HashMap<State, State> m = new HashMap<State, State>();
		Set<State> states = getStates();
		Iterator<State> i = states.iterator();
		while (i.hasNext())
			m.put(i.next(), new State());
		i = states.iterator();
		while (i.hasNext()) {
			State s = i.next();
			State p = m.get(s);
			p.accept = s.accept;
			if (s == initial)
				a.initial = p;
			p.transitions = new HashSet<Transition>();
			Iterator<Transition> j = s.transitions.iterator();
			while (j.hasNext()) {
				Transition t = j.next();
				p.transitions.add(new Transition(t.labelIn, t.labelOut, m.get(t.to)));
			}
		}
		a.deterministic = deterministic;
		a.info = info;
		return a;
	}

	/**
	 * COMPOSITION
	 * 
	 * 
	 *                                                     
		ct((p1,π1,π1,q1,1),(p2,π2,π2,q2,1))=((p1,p2),π1∧π2,π1∧π2,(q1,q2),1)
		ct((p1,φ,π1,q1,0),(p2,π2,π2,q2,1))=((p1,p2),φ,π1∧π2,(q1,q2),0)
		ct((p1,π1,π1,q1,1),(p2,π2,ψ,q2,0))=((p1,p2),π1∧π2,ψ,(q1,q2),0)
		ct((p1,φ,π1,q1,0),(p2,π2,ψ,q2,0))=((p1,p2),φ,ψ,(q1,q2),0)
		                                               if satisfiable(π1∧π2)
		                                                   
			E=  {ct(e1,e2 )|e1 ∈ E1 , e2 ∈ E2 }
			   ∪ {((p1,p2 ), ,ψ,(p1,q2),0)|p1 ∈ Q1,(p2,,ψ,q2,0) ∈ E2 }
			   ∪ {((p1,p2 ),φ, ,(q1,p2),0)|p2 ∈ Q2,(p1,φ,,q1,0) ∈ E1 }

	 * 
	 */
	public Tffst composition(Tffst another) {
		
		this.toSimpleTransitions();
		another.toSimpleTransitions();
		
		Set<State> thisStates = this.getLiveStates();
		Set<State> aStates = another.getLiveStates();

		Tffst out = new Tffst();

		HashMap<StatePair, State> newstates = new HashMap<StatePair, State>();
		
		for ( State thisState : thisStates)
			for ( State aState : aStates) 
				newstates.put(new StatePair(thisState, aState), new State());
				
		for (StatePair statePair : newstates.keySet()){
			
			if (statePair.s1.equals(this.initial) && statePair.s2.equals(another.initial))
				out.initial = newstates.get(statePair);
			
			if (statePair.s1.isAccept() && statePair.s2.isAccept())
				newstates.get(statePair).setAccept(true);
		
			for (Transition t1 : statePair.s1.getTransitions()){
				for (Transition t2 : statePair.s2.getTransitions()){

					TfI t1li = t1.labelIn.isEmpty() ? SimpleTf.Epsilon() : t1.labelIn.get(0);
					TfI t1lo = t1.labelOut.isEmpty() ? SimpleTf.Epsilon() : t1.labelOut.get(0);
					TfI t2li = t2.labelIn.isEmpty() ? SimpleTf.Epsilon() : t2.labelIn.get(0);
					TfI t2lo = t2.labelOut.isEmpty() ? SimpleTf.Epsilon() : t2.labelOut.get(0);

					if (!t1lo.isEpsilon() && !t2li.isEpsilon()) {
						if (t1lo.getIdentity() == 1 && t2lo.getIdentity() == 1 && t1li.equals(t1lo)
								&& t2lo.equals(t2lo)) {
							if (!t1li.and(t2li).equals(SimpleTf.AcceptsNone())) {
								State to = newstates.get(new StatePair(t1.to, t2.to));
								newstates.get(statePair).transitions.add(new Transition(t1li.and(t2li), t1lo.and(t2lo),
										to, 1));
							}
						} else if (t2lo.getIdentity() == 1 && t2lo.equals(t2lo)) {
							State to = newstates.get(new StatePair(t1.to, t2.to));
							newstates.get(statePair).transitions.add(new Transition(t1li, t1lo.and(t2lo), to, 0));
						} else if (t1lo.getIdentity() == 1 && t1li.equals(t1lo)) {
							if (!t1li.and(t2li).equals(SimpleTf.AcceptsNone())) {
								State to = newstates.get(new StatePair(t1.to, t2.to));
								newstates.get(statePair).transitions.add(new Transition(t1li.and(t2li), t2lo, to, 0));
							}
						} else if (!t1.labelOut.get(0).and(t2.labelIn.get(0)).equals(SimpleTf.AcceptsNone())) {
							State to = newstates.get(new StatePair(t1.to, t2.to));
							newstates.get(statePair).transitions.add(new Transition(t1li, t2lo, to, 0));
						}
					} else if (t2li.isEpsilon()) {
						State to = newstates.get(new StatePair(statePair.s1, t2.to));
						newstates.get(statePair).transitions.add(new Transition(t2li, t2lo, to, 0));
					} else if (t1lo.isEpsilon()) {
						State to = newstates.get(new StatePair(t1.to, statePair.s2));
						newstates.get(statePair).transitions.add(new Transition(t2li, t2lo, to, 0));
					}
				}
			}
		}
		out.epsilonRemoval();
		out.removeDeadTransitions();
		out.checkMinimizeAlways();
		return out;
	}

//	/**
//	 * COMPOSITION
//	 * 
//	 */
//	public Tffst composition(Tffst a) {
//		determinize();
//		a.determinize();
//
//		this.toSimpleTransitions();
//		a.toSimpleTransitions();
//		
//		Transition[][] transitions1 = getSortedTransitions();
//		Transition[][] transitions2 = a.getSortedTransitions();
//
//		Tffst c = new Tffst();
//
//		LinkedList<StatePair> worklist = new LinkedList<StatePair>();
//		HashMap<StatePair, StatePair> newstates = new HashMap<StatePair, StatePair>();
//		State s = new State();
//		c.initial = s;
//		StatePair p = new StatePair(s, initial, a.initial);
//		worklist.add(p);
//		newstates.put(p, p);
//		while (worklist.size() > 0) {
//			p = (StatePair) worklist.removeFirst();
//			p.s.accept = p.s1.accept && p.s2.accept;
//
//			Transition[] t1 = transitions1[p.s1.getNumber()];
//			Transition[] t2 = transitions2[p.s2.getNumber()];
//
//			for (int n1 = 0; n1 < t1.length; n1++) {
//				for (int n2 = 0; n2 < t2.length; n2++) {
//
//					StatePair q = new StatePair(t1[n1].to, t2[n2].to);
//					StatePair r = (StatePair) newstates.get(q);
//
//					if (r == null) {
//						q.s = new State();
//						worklist.add(q);
//						newstates.put(q, q);
//						r = q;
//					}
//
//					TfI t1In = null;
//					TfI t1Out = null;
//					TfI t2In = null;
//					TfI t2Out = null;
//
//					if (t1[n1].labelIn.isEpsilon())
//						t1In = SimpleTf.Epsilon();
//					else
//						t1In = (TfI) ((TfString) t1[n1].labelIn).get(0);
//
//					if (t1[n1].labelOut.isEpsilon())
//						t1Out = SimpleTf.Epsilon();
//					else
//						t1Out = (TfI) ((TfString) t1[n1].labelOut).get(0);
//
//					if (t2[n2].labelIn.isEpsilon())
//						t2In = SimpleTf.Epsilon();
//					else
//						t2In = (TfI) ((TfString) t2[n2].labelIn).get(0);
//
//					if (t2[n2].labelOut.isEpsilon())
//						t2Out = SimpleTf.Epsilon();
//					else
//						t2Out = (TfI) ((TfString) t2[n2].labelOut).get(0);
//
//					if (t1Out.in(t2In)) {
//						if (t2In.getIdentity() == 1)
//							p.s.transitions.add(new Transition(t1In, t1Out, r.s));
//						else if (t1Out.getIdentity() == -1	|| t2In.getIdentity() == -1)
//							p.s.transitions.add(new Transition(t1In, t2Out,	r.s, -1));
//						else
//							p.s.transitions.add(new Transition(t1In, t2Out,	r.s, 0));
//					}
//				}
//			}
//		}
//		c.deterministic = true;
//		c.epsilonRemoval();
//		c.removeDeadTransitions();
//		c.checkMinimizeAlways();
//		return c;
//	}

	/**
	 * Returns new Tffst that accepts the concatenation of the languages of this
	 * and the given Tffst.
	 * <p>
	 * Complexity: linear in number of states.
	 */
	public Tffst concatenate(Tffst a) {
		a = a.cloneExpanded();
		Tffst b = cloneExpanded();
		Iterator<State> i = b.getAcceptStates().iterator();
		while (i.hasNext()) {
			State s = i.next();
			s.accept = false;
			s.addEpsilon(a.initial);
		}
		b.deterministic = false;
		b.checkMinimizeAlways();
		return b;
	}

	/**
	 * DETERMINIZATION Determinizes this Tffst.
	 * <p>
	 * Complexity: exponential in number of states.
	 */
	public void determinize() {
		if (deterministic)
			return;
		Set<State> initialset = new HashSet<State>();
		initialset.add(initial);
		determinize(initialset);
	}

	/**
	 * Returns the set of reachable accept states.
	 * 
	 * @return set of {@link State}objects
	 */
	public Set<State> getAcceptStates() {
		HashSet<State> accepts = new HashSet<State>();
		HashSet<State> visited = new HashSet<State>();
		LinkedList<State> worklist = new LinkedList<State>();
		worklist.add(initial);
		visited.add(initial);
		while (worklist.size() > 0) {
			State s = worklist.removeFirst();
			if (s.accept)
				accepts.add(s);
			Iterator<Transition> i = s.transitions.iterator();
			while (i.hasNext()) {
				Transition t = i.next();
				if (!visited.contains(t.to)) {
					visited.add(t.to);
					worklist.add(t.to);
				}
			}
		}
		return accepts;
	}

	/**
	 * Returns extra information associated with this Tffst.
	 * 
	 * @return extra information
	 * @see #setInfo(Object)
	 * 
	 * @uml.property name="info"
	 */
	public Object getInfo() {
		return info;
	}

	/**
	 * @return Returns the initial.
	 * 
	 * @uml.property name="initial"
	 */
	public State getInitial() {
		return initial;
	}

	/**
	 * Gets initial state.
	 * 
	 * @return state
	 */
	public State getInitialState() {
		return initial;
	}

	/**
	 * Returns set of live states. A state is "live" if an accept state is
	 * reachable from it.
	 * 
	 * @return set of {@link State}objects
	 */
	public Set<State> getLiveStates() {
		return getLiveStates(getStates());
	}

	/** Returns number of states in this Tffst. */
	public int getNumberOfStates() {
		return getStates().size();
	}

	/**
	 * Returns number of transitions in this Tffst. This number is counted as
	 * the total number of edges, where one edge may be a character interval.
	 */
	public int getNumberOfTransitions() {
		int c = 0;
		Iterator<State> i = getStates().iterator();
		while (i.hasNext()) {
			State s = i.next();
			c += s.transitions.size();
		}
		return c;
	}

	/**
	 * Returns the set states that are reachable from the initial state.
	 * 
	 * @return set of {@link State}objects
	 */
	public Set<State> getStates() {
		HashSet<State> visited = new HashSet<State>();
		LinkedList<State> worklist = new LinkedList<State>();
		worklist.add(initial);
		visited.add(initial);
		while (worklist.size() > 0) {
			State s = worklist.removeFirst();
			Iterator<Transition> i = s.transitions.iterator();
			while (i.hasNext()) {
				Transition t = i.next();
				if (!visited.contains(t.to)) {
					visited.add(t.to);
					worklist.add(t.to);
				}
			}
		}
		return visited;
	}

	/**
	 * Returns a set with all transitions
	 * 
	 */
	public Set<Transition> getTransitions() {
		Set<Transition> allTrans = new HashSet<Transition>();
		HashSet<State> visited = new HashSet<State>();
		LinkedList<State> worklist = new LinkedList<State>();
		worklist.add(initial);
		while (worklist.size() > 0) {
			State s = worklist.removeFirst();
			if (!visited.contains(s)) {
				visited.add(s);
				Iterator<Transition> i = s.transitions.iterator();
				while (i.hasNext()) {
					Transition t = (Transition) i.next();
					worklist.add(t.to);
					allTrans.add(t);
				}
			}
		}
		return allTrans;
	}

	/**
	 * Returns hash code for this Tffst. The hash code is based on the number of
	 * states and transitions in the minimized Tffst.
	 */
	public int hashCode() {
		// if (hash_code == 0)
		// minimize();
		return hash_code;
	}

	/**
	 * Returns deterministic flag for this Tffst.
	 * 
	 * @return true if the Tffst is definitely deterministic, false if the Tffst
	 *         may be nondeterministic
	 * 
	 * @uml.property name="deterministic"
	 */
	public boolean isDeterministic() {
		return deterministic;
	}

	/** Returns true if this Tffst accepts no strings. */
	public boolean isEmpty() {
		return initial.accept == false && initial.transitions.size() == 0;
	}

	/** Returns true if the language of this Tffst is finite. */
	public boolean isFinite() {
		return isFinite(initial, new HashSet<State>());
	}

	/**
	 * MINIMIZATION Minimizes (and determinizes if not already deterministic)
	 * this Tffst.
	 * 
	 * @see #setMinimization(int)
	 */
	public void minimize() {
		Tffsr tffsr = this.toTffsr();
		tffsr.minimize();
		Tffst tffst = tffsr.toTffst();
		assign(tffst);
		epsilonRemoval();
	}

	/**
	 * Removes transitions to dead states and calls {@link #reduce()}(a state
	 * is "dead" if no accept state is reachable from it).
	 */
	public void removeDeadTransitions() {
		Set<State> states = getStates();
		Set<State> live = getLiveStates(states);
		Iterator<State> i = states.iterator();
		while (i.hasNext()) {
			State s = i.next();
			Iterator<Transition> j = s.transitions.iterator();
			s.resetTransitions();
			while (j.hasNext()) {
				Transition t = (Transition) j.next();
				if (live.contains(t.to))
					s.transitions.add(t);
			}
		}
	}

	/**
	 * KLEENE STAR Returns new Tffst that accepts the Kleene star (zero or more
	 * concatenated repetitions) of the language of this Tffst.
	 * <p>
	 * Complexity: linear in number of states.
	 * 
	 */
	public Tffst kleene() {
		Tffst a = cloneExpanded();
		State s = new State();
		s.accept = true;
		s.addEpsilon(a.initial);
			
		for (State p : a.getAcceptStates() )
			p.addEpsilon(s);
		
		a.initial = s;

		a.deterministic = false;
		a.checkMinimizeAlways();
		return a;
	}

	/**
	 * Returns new Tffst that accepts <code>min</code> or more concatenated
	 * repetitions of the language of this Tffst.
	 * <p>
	 * Complexity: linear in number of states and in <code>min</code>.
	 */
	public Tffst kleene(int min) {
		Tffst a = kleene();
		while (min-- > 0)
			a = concatenate(a);
		return a;
	}

	/**
	 * Returns new Tffst that accepts between <code>min</code> and
	 * <code>max</code> (including both) concatenated repetitions of the
	 * language of this Tffst.
	 * <p>
	 * Complexity: linear in number of states and in <code>min</code> and
	 * <code>max</code>.
	 */
	public Tffst kleene(int min, int max) {
		if (min > max)
			return makeEmpty();
		max -= min;
		Tffst a;
		if (min == 0)
			a = makeEmptyString();
		else if (min == 1)
			a = (Tffst) clone();
		else {
			a = this;
			while (--min > 0)
				a = concatenate(a);
		}
		if (max == 0)
			return a;
		Tffst d = (Tffst) clone();
		while (--max > 0) {
			Tffst c = (Tffst) clone();
			Iterator<State> i = c.getAcceptStates().iterator();
			while (i.hasNext()) {
				State p = i.next();
				p.addEpsilon(d.initial);
			}
			d = c;
		}
		Iterator<State> i = a.getAcceptStates().iterator();
		while (i.hasNext()) {
			State p = i.next();
			p.addEpsilon(d.initial);
		}
		a.deterministic = false;
		a.checkMinimizeAlways();
		return a;
	}

	// /**
	// * Returns true if the given epoch is accepted by this Tffst. As a
	// * side-effect, this Tffst is determinized if not already deterministic.
	// * <p>
	// * Complexity: linear in length of epoch (if Tffst is already
	// deterministic)
	// * and in number of transitions.
	// * <p>
	// */
	//
	
	// comentario de prueba con el git 
	
	
	// public Epoch run(Epoch e) {
	//    
	// //determinize();
	//    
	// State p = initial;
	// Epoch out = new Epoch();
	// SeStatePair sesp = null;
	//
	// for (Iterator iter = e.iterator(); iter.hasNext();) {
	// MessageI message = (MessageI) iter.next();
	// sesp = p.stepOut(message);
	//      
	// if (sesp != null) {
	// if (sesp.se.get(0) instanceof ActionTf) {
	// ActionTf actionTf = (ActionTf) sesp.se.get(0);
	// List<MessageI> action = actionTf.getActionMessage();
	// out.addAll(action);
	// if (sesp.state == null)
	// return null;
	// p = sesp.state;
	// }
	// } else
	// return null;
	// }
	//    
	// if (p.isAccept())
	// return out;
	// else
	// return null;
	// }

	/**
	 * Sets deterministic flag for this Tffst. This method should (only) be used
	 * if Tffsrs are constructed manually.
	 * 
	 * @param deterministic
	 *            true if the Tffst is definitely deterministic, false if the
	 *            Tffst may be nondeterministic
	 * 
	 * @uml.property name="deterministic"
	 */
	public void setDeterministic(boolean deterministic) {
		this.deterministic = deterministic;
	}

	/**
	 * Associates extra information with this Tffst.
	 * 
	 * @param info
	 *            extra information
	 * 
	 * @uml.property name="info"
	 */
	public void setInfo(Object info) {
		this.info = info;
	}

	/**
	 * Sets initial state.
	 * 
	 * @param s
	 *            state
	 */
	public void setInitialState(State s) {
		initial = s;
	}

	/**
	 * Returns <a href="http://www.research.att.com/sw/tools/graphviz/"
	 * target="_top">Graphviz Dot </a> representation of this Tffst.
	 */
	public String toDot(String label) {

		StringBuffer b = new StringBuffer("digraph Tffst {\n"); //$NON-NLS-1$
		b.append("rankdir = \"LR\"\n;");
		b.append("label = \"").append(label).append("\"\n;");
		setStateNumbers();
		Iterator<State> i = getStates().iterator();
		while (i.hasNext()) {
			State s = i.next();
			b.append("  ").append(s.getNumber()); //$NON-NLS-1$
			if (s.accept)
				b
						.append(" [shape=doublecircle,label=\"").append(s.getNumber()).append("\"];\n"); //$NON-NLS-1$
			else
				b
						.append(" [shape=circle,label=\"").append(s.getNumber()).append("\"];\n"); //$NON-NLS-1$
			if (s == initial) {
				b.append("  initial [shape=plaintext,label=\"\"];\n"); //$NON-NLS-1$
				b.append("  initial -> ").append(s.getNumber()).append("\n"); //$NON-NLS-1$ //$NON-NLS-2$
			}
			Iterator<Transition> j = s.transitions.iterator();
			while (j.hasNext()) {
				Transition t = j.next();
				b.append("  ").append(s.getNumber()); //$NON-NLS-1$
				t.appendDot(b);
			}
		}
		return b.append("}\n").toString(); //$NON-NLS-1$
	}

	/** Returns a string representation of this Tffst. */
	public String toString() {
		StringBuffer b = new StringBuffer();
		setStateNumbers();
		b.append("initial state: ").append(initial.getNumber()).append("\n"); //$NON-NLS-1$ //$NON-NLS-2$
		Iterator<State> i = getStates().iterator();
		while (i.hasNext()) {
			State s = i.next();
			b.append(s.toString());
		}
		return b.toString();
	}

	/**
	 * returns a Tffsr representing this tffst. it expands subEpochs in simpe
	 * pairts of TFs adding epsilons
	 * 
	 * @return
	 */
	public Tffsr toTffsr() {
		Tffsr a = new Tffsr();
		HashMap m = new HashMap();
		Set<State> states = getStates();
		TfPair tfp;

		Iterator tIniter;
		Iterator tOutiter;

		Iterator<State> i = states.iterator();
		while (i.hasNext())
			m.put(i.next(), new uy.edu.fing.mina.fsa.tffsr.State());

		i = states.iterator();

		while (i.hasNext()) {
			State s = i.next();
			uy.edu.fing.mina.fsa.tffsr.State p = (uy.edu.fing.mina.fsa.tffsr.State) m
					.get(s);
			p.accept = s.accept;
			if (s == initial)
				a.initial = p;
			p.transitions = new HashSet<uy.edu.fing.mina.fsa.tffsr.Transition>();
			Iterator<Transition> j = s.transitions.iterator();
			while (j.hasNext()) {
				Transition t = j.next();

				tIniter = t.labelIn.iterator();
				tOutiter = t.labelOut.iterator();

				int maxTrans = Math.max(t.labelIn.size(), t.labelOut.size());

				uy.edu.fing.mina.fsa.tffsr.State last = p;

				for (int k = 1; k < maxTrans; k++) {
					// creates a new state
					uy.edu.fing.mina.fsa.tffsr.State ns = new uy.edu.fing.mina.fsa.tffsr.State();
					m.put(ns, ns);
					uy.edu.fing.mina.fsa.tffsr.Transition newTr = new uy.edu.fing.mina.fsa.tffsr.Transition();
					// creates an epsilon/epsilon transition
					TfI tfptfIn;
					TfI tfptfOut;
					if (tIniter.hasNext())
						tfptfIn = (TfI) tIniter.next();
					else
						tfptfIn = SimpleTf.Epsilon();
					
					if (tOutiter.hasNext())
						tfptfOut = (TfI) tOutiter.next();
					else
						tfptfOut = SimpleTf.Epsilon();
					
					tfp = new TfPair(tfptfIn, tfptfOut);
					
					newTr.setLabel(tfp);
					newTr.setTo(ns);
					newTr.setSLabel();
					last.transitions.add(newTr);
					last = ns;
				}

				uy.edu.fing.mina.fsa.tffsr.Transition newTr = new uy.edu.fing.mina.fsa.tffsr.Transition();
				// creates an epsilon/epsilon transition
				TfI tfptfIn;
				TfI tfptfOut;
				if (tIniter.hasNext())
					tfptfIn = (TfI) tIniter.next();
				else
					tfptfIn = SimpleTf.Epsilon();
				if (tOutiter.hasNext())
					tfptfOut = (TfI) tOutiter.next();
				else
					tfptfOut = SimpleTf.Epsilon();
				tfp = new TfPair(tfptfIn, tfptfOut);
				newTr.setLabel(tfp);
				newTr.setTo((uy.edu.fing.mina.fsa.tffsr.State) m.get(t.to));
				newTr.setSLabel();
				last.transitions.add(newTr);
			}
		}
		a.deterministic = deterministic;
		a.info = info;
		return a;
	}

	/**
	 * returns a Tffsr representing this tffst. it expands subEpochs in simpe
	 * pairts of TFs adding epsilons
	 * 
	 * @return
	 */
	public Tffst toSimpleTransitions() {
		Tffst a = new Tffst();
		HashMap<State, State> m = new HashMap<State, State>();
		Set<State> states = getStates();

		Iterator<TfI> tIniter;
		Iterator<TfI> tOutiter;

		Iterator<State> i = states.iterator();
		while (i.hasNext())
			m.put(i.next(), new State());

		i = states.iterator();

		while (i.hasNext()) {
			State s = i.next();
			State p = m.get(s);
			p.accept = s.accept;
			if (s == initial)
				a.initial = p;
			p.transitions = new HashSet<Transition>();
			Iterator<Transition> j = s.transitions.iterator();
			while (j.hasNext()) {
				Transition t = j.next();

				tIniter = t.labelIn.iterator();
				tOutiter = t.labelOut.iterator();

				int maxTrans = Math.max(t.labelIn.size(), t.labelOut.size());

				State last = p;

				for (int k = 1; k < maxTrans; k++) {
					// creates a new state
					State ns = new State();
					m.put(ns, ns);
					Transition newTr = new Transition();
					// creates an epsilon/epsilon transition
					TfI oIn;
					TfI oOut;

					if (tIniter.hasNext())
						oIn = tIniter.next();
					else
						oIn = SimpleTf.Epsilon();

					if (tOutiter.hasNext())
						oOut = tOutiter.next();
					else
						oOut = SimpleTf.Epsilon();

					newTr.setLabelIn(new TfString(oIn));

					newTr.setLabelOut(new TfString(oOut));

					newTr.to = ns;
					newTr.setSLabel();
					last.transitions.add(newTr);
					last = ns;
				}

				Transition newTr = new Transition();
				// creates an epsilon/epsilon transition
				TfI tfptfIn;
				TfI tfptfOut;
				
				if (tIniter.hasNext())
					tfptfIn = (TfI) tIniter.next();
				else
					tfptfIn = SimpleTf.Epsilon();

				if (tOutiter.hasNext())
					tfptfOut = (TfI) tOutiter.next();
				else
					tfptfOut = SimpleTf.Epsilon();
				
				newTr.setLabelIn(new TfString(tfptfIn));
				newTr.setLabelOut(new TfString(tfptfOut));
				
				newTr.to = (State) m.get(t.to);
				newTr.setSLabel();
				last.transitions.add(newTr);
			}
		}
		a.deterministic = deterministic;
		a.info = info;
		a.setStateNumbers();//xop
		return a;
	}

	
	/**
	 * returns a Tffsr representing this tffst. it expands subEpochs in the input label
	 * 
	 * @return
	 */
	public void toSimpleInLabels() {
		HashMap<State, State> m = new HashMap<State, State>();
		Set<State> states = getStates();

		for (State s : states ){
			State p = new State();
			p.accept = s.accept;
			if (s == initial) this.initial = p;
			m.put(s, p);
		}

		for (State s : states){
			for (Transition t : s.transitions){
				State last = m.get(s);
				for (int k = 0; k < t.labelIn.size(); k++) {
					State to;
					if (k == t.labelIn.size()-1)
						to = m.get(t.to);
					else 
						to = new State();
					if (k == 0)
						last.transitions.add(new Transition(new TfString(t.labelIn.get(0)), t.labelOut, to));
					else 
						last.transitions.add(new Transition(t.labelIn.get(k), SimpleTf.Epsilon(), to));
					last = to;
				}
			}
		}
	}

	
	
	// /**
	// * returns a Tffsr representing this tffst. it expands subEpochs in simpe
	// * pairts of TFs adding epsilons
	// *
	// * @return
	// */
	// public Tffsr firstProjection() {
	// Tffsr a = new Tffsr();
	// HashMap m = new HashMap();
	// Set<State> states = getStates();
	// Iterator tIniter;
	//
	// Iterator<State> i = states.iterator();
	// while (i.hasNext())
	// m.put(i.next(), new uy.edu.fing.mina.fsa.tffsr.State());
	//
	// i = states.iterator();
	//
	// while (i.hasNext()) {
	// State s = i.next();
	// uy.edu.fing.mina.fsa.tffsr.State p = (uy.edu.fing.mina.fsa.tffsr.State) m
	// .get(s);
	// p.accept = s.accept;
	//
	// if (s == initial)
	// a.initial = p;
	// p.transitions = new HashSet<uy.edu.fing.mina.fsa.tffsr.Transition>();
	// Iterator<Transition> j = s.transitions.iterator();
	// while (j.hasNext()) {
	// Transition t = j.next();
	// tIniter = t.labelIn.iterator();
	// int maxTrans = t.labelIn.size();
	// uy.edu.fing.mina.fsa.tffsr.State last = p;
	//
	// for (int k = 1; k < maxTrans; k++) {
	// // creates a new state
	// uy.edu.fing.mina.fsa.tffsr.State ns = new
	// uy.edu.fing.mina.fsa.tffsr.State();
	// m.put(ns, ns);
	// uy.edu.fing.mina.fsa.tffsr.Transition newTr = new
	// uy.edu.fing.mina.fsa.tffsr.Transition();
	//
	// // creates an epsilon/epsilon transition
	// TfI tfptfIn;
	//
	// if (tIniter.hasNext())
	// tfptfIn = (TfI) tIniter.next();
	// else
	// tfptfIn = SimpleTf.Epsilon();
	//
	// newTr.setLabel(tfptfIn);
	// newTr.setTo(ns);
	// newTr.setSLabel();
	// last.transitions.add(newTr);
	// last = ns;
	// }
	//
	// uy.edu.fing.mina.fsa.tffsr.Transition newTr = new
	// uy.edu.fing.mina.fsa.tffsr.Transition();
	// // creates an epsilon/epsilon transition
	// TfI tfptfIn;
	// if (tIniter.hasNext())
	// tfptfIn = (TfI) tIniter.next();
	// else
	// tfptfIn = SimpleTf.Epsilon();
	// newTr.setLabel(tfptfIn);
	// newTr.setTo((uy.edu.fing.mina.fsa.tffsr.State) m.get(t.to));
	// newTr.setSLabel();
	// last.transitions.add(newTr);
	// }
	// }
	// a.deterministic = deterministic;
	// a.info = info;
	// return a;
	// }

	/**
	 * UNION Returns new Tffst that accepts the union of the languages of this
	 * and the given Tffst.
	 * <p>
	 * Complexity: linear in number of states.
	 */
	public Tffst union(Tffst t) {
		Tffst a = (Tffst) t.clone();
		Tffst b = cloneExpanded();
		State s = new State();
		s.addEpsilon(a.initial);
		s.addEpsilon(b.initial);
		a.initial = s;
		a.deterministic = false;
		a.checkMinimizeAlways();
		return a;
	}

	/**
	 * DETERMINIZATION Determinizes this Tffst using the given set of initial
	 * states.
	 * 
	 */
	private void determinize(Set<State> initialset) {
		
		// removes all epsilon labels in the domain part
		domainEpsilonsRemoval();
		toSimpleInLabels();
		
		// subset construction
		Map<Set<PairP>, Set<PairP>> sets = new HashMap<Set<PairP>, Set<PairP>>();
		// stores new states
		Map<Set<PairP>, State> newstate = new HashMap<Set<PairP>, State>();
		Set<PairP> initialListOfP = new HashSet<PairP>();

		for (State initialState : initialset) 
			initialListOfP.add(new PairP(initialState, new TfString()));

		// this is the first partition
		sets.put(initialListOfP, initialListOfP);

		LinkedList<Set<PairP>> worklist = new LinkedList<Set<PairP>>();
		worklist.add(initialListOfP);

		initial = new State();
		newstate.put(initialListOfP, initial);

		while (worklist.size() > 0) {
			// the first partition
			Set<PairP> p = worklist.removeFirst();
			// the new corresponding state
			State np = newstate.get(p);

			// if any of the old states was final, this is final
			for (PairP q : p) 
				if (q.state.accept) {
					np.accept = true;
					break;
				}

			// geting the set of TFs related to states in p
			Set<TfI> relevantTFs = getRelevant(p);
			Set<Partition> partitions = Utils.getPartitions3(relevantTFs);

			System.out.println(relevantTFs);
			
			for (Partition partition : partitions) {
				TfI tfrelation = Utils.toTfrelation3(partition);
				if (!tfrelation.equals(SimpleTf.AcceptsNone())) {
					ProtoTransition pt = new ProtoTransition(np, tfrelation, transP(p, tfrelation));
					pt.simplifyTargetByState_noOps();
					TfString prefix = pt.longestPrefix();
					if (!pt.pairsP.isEmpty()) {
						Set<PairP> key = isIn(sets, pt.pairsP);
						if (key == null) {
							sets.put(pt.pairsP, pt.pairsP);
							worklist.add(pt.pairsP);
							newstate.put(pt.pairsP, new State());
							key = pt.pairsP;
						}
						np.transitions.add(new Transition(new TfString(tfrelation),prefix,newstate.get(key)));
					}
				}
			}
		}
		epsilonRemoval();
		deterministic = true;
		removeDeadTransitions();
	}

	
	/**
	 * 
	 * @param pairsPSet
	 * @param set
	 * @return
	 */
	private Set<PairP> isIn(Map<Set<PairP>, Set<PairP>> pairsPSet, Set<PairP> newstate) {
		boolean out = true;
		boolean in = true;
		// for each state s in newstate (here state is a set of pairs)
		for (Set<PairP> pairsPSetKey : pairsPSet.keySet()) 
			if (pairsPSetKey.size() == newstate.size()) {
				out = true;
				for (PairP pairInKeySet : pairsPSetKey){
					in = false;
					for (PairP pairInNS : newstate)
						in = in || pairInNS.equals(pairInKeySet);
					out = out && in;
				}
				if (out) return pairsPSetKey;
			}
		return null;
	}

	
	/**
	 * EPSILON REMOVAL FOR TRANSDUCERS
	 * 
	 * tffst cannot contain epsilon cycles 
	 * 
	 */
	public void domainEpsilonsRemoval() {

		List<State> allLiveStates = new LinkedList<State>(getLiveStates());
		
		while (allLiveStates.size() > 0) {
			State workingState = allLiveStates.remove(0);
			for (Transition outgoingT : new HashSet<Transition>(workingState.getTransitions())) {
				if (outgoingT.labelIn.isEpsilon()){
					State stateToAdd = new State();
					allLiveStates.add(stateToAdd);
					for (Transition t : outgoingT.to.getTransitions()){
						Transition newT = t.clone();
						newT.labelOut = outgoingT.labelOut.clone();
						newT.labelOut.addAll(t.labelOut.clone());
						stateToAdd.addTransition(newT);						
					}
					workingState.removeTransition(outgoingT);
					workingState.addEpsilon(stateToAdd);
				}
			}
		}
	}

	/**
	 * EPSILON REMOVAL FOR TRANSDUCERS
	 * 
	 */
	public void epsilonRemoval() {

		// FIXME mal ver ejemplo composition y tranlation

		LinkedList<State> workingList = new LinkedList<State>();
		workingList.add(initial);

		// subset construction
		Map<State, State> sets = new HashMap<State, State>();

		// for each state
		while (!workingList.isEmpty()) {
			Set<Transition> toRemove = new HashSet<Transition>();
			Set<Transition> toAdd = new HashSet<Transition>();
			State workingState = workingList.removeFirst();
			Set<Transition> transitions = workingState.getTransitions();

			// for each transition
			for (Iterator<Transition> transitionIter = transitions.iterator(); transitionIter
					.hasNext();) {
				Transition transition = transitionIter.next();
				State destination = transition.to;
				if (destination != workingState) {
					if (!sets.containsKey(destination)) {
						sets.put(destination, destination);
						workingList.add(destination);
					}
					Set<Transition> destTransitions = destination
							.getTransitions();
					// for each transition at this transition's end
					for (Iterator<Transition> iterator = destTransitions
							.iterator(); iterator.hasNext();) {
						Transition destTransition = iterator.next();
						// if incoming label is epsilon
						if (destTransition.labelIn.isEpsilon()) {
							try {
								State newDestState = destination.clone();
								Transition oldTransition = new Transition(
										transition.labelIn,
										transition.labelOut, newDestState);
								// adding a transition without epsilon to the
								// new state
								toAdd.add(oldTransition);
								newDestState.getTransitions().remove(
										destTransition);
								TfString newLabelOut = new TfString(
										transition.labelOut);

								// newLabelOut.add(new
								// Operator(Operator.CONCAT));

								newLabelOut.addAll(destTransition.labelOut);
								Transition newTransition = new Transition(
										transition.labelIn, newLabelOut,
										destTransition.to);
								// adding a transition to destTransition.to
								toAdd.add(newTransition);
								toRemove.add(transition);

							} catch (CloneNotSupportedException e) {
								e.printStackTrace();
							}
						}
					}
				}
			}
			for (Iterator<Transition> toAddIt = toAdd.iterator(); toAddIt
					.hasNext();)
				workingState.addTransition((Transition) toAddIt.next());

			for (Iterator<Transition> toRemoveIt = toRemove.iterator(); toRemoveIt
					.hasNext();)
				workingState.removeTransition((Transition) toRemoveIt.next());
		}

		sets = new HashMap<State, State>();

		workingList.add(initial);
		while (!workingList.isEmpty()) {
			Set<Transition> toRemove = new HashSet<Transition>();
			Set<Transition> toAdd = new HashSet<Transition>();

			State workingState = workingList.removeFirst();
			Set<Transition> transitions = workingState.getTransitions();
			for (Iterator<Transition> transitionIter = transitions.iterator(); transitionIter
					.hasNext();) {
				Transition transition = (Transition) transitionIter.next();
				if (transition.labelIn.isEmpty()) {
					State destination = transition.to;
					if (!sets.containsKey(destination)) {
						sets.put(destination, destination);
						workingList.add(destination);
					}
					Set<Transition> destTransitions = destination
							.getTransitions();
					for (Iterator<Transition> iterator = destTransitions
							.iterator(); iterator.hasNext();) {
						Transition destTransition = iterator.next();
						TfString newLabelOut = new TfString(transition.labelOut);

						// newLabelOut.add(new Operator(Operator.CONCAT));

						newLabelOut.addAll(destTransition.labelOut);
						Transition newTransition = new Transition(
								destTransition.labelIn, newLabelOut,
								destTransition.to);
						toAdd.add(newTransition);
					}
					toRemove.add(transition);
				}
			}
			for (Iterator<Transition> toAddIt = toAdd.iterator(); toAddIt
					.hasNext();)
				workingState.addTransition((Transition) toAddIt.next());

			for (Iterator<Transition> toRemoveIt = toRemove.iterator(); toRemoveIt
					.hasNext();)
				workingState.removeTransition((Transition) toRemoveIt.next());
		}
	}


	/**
	 * Recursive method to compute all posible relations of tauterthan and
	 * astautas of a given permutation
	 * 
	 * @param permutation
	 *            each permutation is given by a ComposiiteTF tree
	 * @return a set of TfIs
	 */
	private Set<TfI> getRelations2(TfI permutation) {
		Set<TfI> returnSet = new HashSet<TfI>();

		if (permutation instanceof TfPair) {
			TfPair tfpPermutation = (TfPair) permutation;
			Set<TfI> tfpreturnSet = new HashSet<TfI>();
			tfpreturnSet.addAll(getRelations2(tfpPermutation.getTfIn()));
			for (Iterator<TfI> iter = tfpreturnSet.iterator(); iter.hasNext();) {
				TfI relation = iter.next();
				returnSet.add(new TfPair(relation, tfpPermutation.getTfOut()));
			}
		} else if (permutation instanceof SimpleTf) {
			returnSet.add(permutation);
		} else if (permutation instanceof CompositeTf) {
			CompositeTf cpermutation = (CompositeTf) permutation;
			Set left = getRelations2(cpermutation.leftTf);
			Set right = getRelations2(cpermutation.rightTf);
			for (Iterator iter = left.iterator(); iter.hasNext();) {
				TfI lRelation = (TfI) iter.next();
				for (Iterator iterator = right.iterator(); iterator.hasNext();) {
					TfI rRelation = (TfI) iterator.next();
					returnSet.add(lRelation.tauterThan(rRelation));
					returnSet.add(lRelation.asTautas(rRelation));
				}
			}
		} else {
			System.err.println("ERROR: bad permutation structure.");
		}
		return returnSet;
	}

	/**
	 * Returns the set of TFs associated to a set of states. It supports a
	 * several TFs in the labelIn but must be only one.
	 * 
	 * @param p
	 *            A set of states (State)
	 * @return
	 */
	private Set<TfI> getRelevant(Set<PairP> p) {
		Set<TfI> relevantTFs = new HashSet<TfI>();
		for (PairP pp : p)
			for (Transition t : pp.state.transitions)
				for (TfI tf : t.labelIn)
					relevantTFs.add(tf);

		return relevantTFs;
	}

	/**
	 * 
	 * Implements the function TransP. It assumes that there is only one TF in
	 * the incomming TfString
	 * 
	 * @param setOfPairsP
	 *            is a set of PairP
	 * @param se
	 *            is in fact a subEpoch
	 * @param transitions
	 *            are all the transitions sorted by state
	 * 
	 * @return a set of PairP
	 */
	private Set<PairP> transP(Set<PairP> listOfPairsP, TfI tf) {
		Set<PairP> pElements = new HashSet<PairP>();
		PairP retPair;

		for (PairP pairP : listOfPairsP) {
			for (Transition t : pairP.state.transitions) {
				tf = uy.edu.fing.mina.fsa.logics.Utils.simplify(tf);
				if (tf.and(t.labelIn.get(0)).equals(tf) && !tf.isEpsilon()) {
					TfString newSE = new TfString();
					newSE.addAll(pairP.arriving);
					newSE.addAll(t.labelOut);
					Map<Transition, Transition> newMap = new HashMap<Transition, Transition>();
					newMap.putAll(pairP.usedTrans);
					newMap.put(t, t);
					retPair = new PairP(t.to, newSE, newMap);
					pElements.add(retPair);
				}
			}
		}
		return pElements;
	}

	void checkMinimizeAlways() {
		if (minimize_always)
			minimize();
	}

	/** Returns a clone of this Tffst. */
	Tffst cloneExpanded() {
		Tffst a = (Tffst) clone();
		return a;
	}

	Set<State> getLiveStates(Set<State> states) {
		Map<State, HashSet<State>> map = new HashMap<State, HashSet<State>>();
		for (State s : states)
			map.put(s, new HashSet<State>());
		
		for (State s : states)
			for (Transition t : s.transitions)
				map.get(t.to).add(s);
		
		Set<State> live = new HashSet<State>(getAcceptStates());
		LinkedList<State> worklist = new LinkedList<State>(live);
		while (worklist.size() > 0) {
			State s = worklist.removeFirst();
			Iterator<State> j = map.get(s).iterator();
			while (j.hasNext()) {
				State p = j.next();
				if (!live.contains(p)) {
					live.add(p);
					worklist.add(p);
				}
			}
		}
		return live;
	}

	/**
	 * Returns sorted array of transitions for each state (and sets state
	 * numbers).
	 */
	Transition[][] getSortedTransitions() {
		setStateNumbers();
		Transition[][] transitions = new Transition[getStates().size()][];
		Iterator<State> i = getStates().iterator();
		while (i.hasNext()) {
			State s = i.next();
			transitions[s.getNumber()] = s.getTransitionArray();
		}
		return transitions;
	}

	/**
	 * Returns sorted array of transitions for each state (and sets state
	 * numbers).
	 */
	boolean[][] getVisitedArray(Set<State> states) {
		boolean[][] visited = new boolean[states.size()][];
		Iterator<State> i = states.iterator();
		while (i.hasNext()) {
			State s = i.next();
			visited[s.getNumber()] = new boolean[s.getTransitionArray().length];
			for (int j = 0; j < visited[s.getNumber()].length; j++) {
				visited[s.getNumber()][j] = false;
			}
		}
		return visited;
	}

	/**
	 * Checks whether there is a loop containing s. (This is sufficient since
	 * there are never transitions to dead states.)
	 */
	boolean isFinite(State s, HashSet<State> path) {
		path.add(s);
		Iterator<Transition> i = s.transitions.iterator();
		while (i.hasNext()) {
			Transition t = i.next();
			if (path.contains(t.to) || !isFinite(t.to, path))
				return false;
		}
		path.remove(s);
		return true;
	}

	/**
	 * Assigns consecutive numbers to the given states. FIXED
	 */
	public void setStateNumbers() {
		Iterator<State> i = getStates().iterator();
		int number = 0;
		while (i.hasNext()) {
			//System.out.println(number);
			State s = i.next();
			s.setNumber(number++);
		}
	}

}

class IntPair {
	int n1;

	int n2;

	IntPair(int n1, int n2) {
		this.n1 = n1;
		this.n2 = n2;
	}
}

class StateList {

	/**
	 * 
	 * @uml.property name="first"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
	StateListNode first;

	/**
	 * 
	 * @uml.property name="last"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
	StateListNode last;

	int size;

	StateListNode add(State q) {
		return new StateListNode(q, this);
	}
}

class StateListNode {

	/**
	 * 
	 * @uml.property name="next"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
	StateListNode next;

	/**
	 * 
	 * @uml.property name="prev"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
	StateListNode prev;

	/**
	 * 
	 * @uml.property name="q"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
	State q;

	/**
	 * 
	 * @uml.property name="sl"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
	StateList sl;

	StateListNode(State q, StateList sl) {
		this.q = q;
		this.sl = sl;
		if (sl.size++ == 0)
			sl.first = sl.last = this;
		else {
			sl.last.next = this;
			prev = sl.last;
			sl.last = this;
		}
	}

	void remove() {
		sl.size--;
		if (sl.first == this)
			sl.first = next;
		else
			prev.next = next;
		if (sl.last == this)
			sl.last = prev;
		else
			next.prev = prev;
	}

}
