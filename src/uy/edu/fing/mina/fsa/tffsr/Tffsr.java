/*
 * based on
 * 
 * dk.brics.automaton Copyright (C) 2001-2004 Anders Moeller All rights
 * reserved.
 * 
 * es.upc.tffsr Copyright (C) 2004 Javier Baliosian All rights reserved.
 *  
 */

package uy.edu.fing.mina.fsa.tffsr;

import java.io.IOException;
import java.io.InputStream;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import uy.edu.fing.mina.fsa.logics.Utils;
import uy.edu.fing.mina.fsa.tf.Partition;
import uy.edu.fing.mina.fsa.tf.SimpleTf;
import uy.edu.fing.mina.fsa.tf.Tf;
import uy.edu.fing.mina.fsa.tf.TfI;
import uy.edu.fing.mina.fsa.tf.TfPair;
import uy.edu.fing.mina.fsa.tf.TfString;
import uy.edu.fing.mina.fsa.tffst.Tffst;


/*
 * Class invariants: - An Tffsr is either represented explicitly (with State and
 * Transition objects) - tffsrs are always reduced (see reduce()) and have no
 * transitions to dead states (see removeDeadTransitions()). - If an automaton
 * is nondeterministic, then isDeterministic() returns false (but the converse
 * is not required).
 * 
 * labels are a pair of SimpleTf or a CompositeTF, never a TfString.
 */

public class Tffsr implements Serializable {

  /** Minimize always flag. */
  static boolean minimize_always;

  static final long serialVersionUID = 10001;

  /**
   * If true, then this Tffsr is definitely deterministic (i.e., there are no
   * choices for any run, but a run may crash).
   * 
   * @uml.property name="deterministic"
   */
  public boolean deterministic;

  /** Hash code. Recomputed by {@link #minimize()}. */
  public int hash_code;

  /**
   * Extra data associated with this Tffsr.
   */
  public Object info;

  /**
   * Initial state of this Tffsr.
   */
  public State initial;

  /**
   * Constructs new Tffsr that accepts the empty language. Using this
   * constructor, Tffsr can be constructed manually from {@link State}and
   * {@link Transition}objects.
   * 
   * @see #setInitialState(State)
   * @see State
   * @see Transition
   */
  public Tffsr() {
    this.initial = new State();
    this.deterministic = true;
  }

  /**
   * CONCATENATION Returns new Tffsr that accepts the concatenation of the
   * languages of the given automata.
   * <p>
   * Complexity: linear in total number of states.
   */
  static public Tffsr concatenate(List<Tffsr> l) {
    if (l.isEmpty()) return makeEmptyString();
    Iterator<Tffsr> i = l.iterator();
    Tffsr b = (Tffsr) (i.next()).clone();
    Iterator<State> j = b.getAcceptStates().iterator();
    while (i.hasNext()) {
      Tffsr a = (Tffsr) i.next().clone();
      Set<State> ns = a.getAcceptStates();
      while (j.hasNext()) {
        State s = j.next();
        s.setAccept(false);
        s.addEpsilon(a.initial);
        if (s.isAccept()) ns.add(s);
      }
      j = ns.iterator();
    }
    b.deterministic = false;
    b.checkMinimizeAlways();
    return b;
  }

  /*
   * 
   * Auxiliary operations
   * ************************************************************************
   */

  /**
   * Retrieves a serialized <code>Tffsr</code> from a stream.
   * 
   * @param stream
   *          input stream with serialized Tffsr
   * @exception IOException
   *              if input/output related exception occurs
   * @exception OptionalDataException
   *              if the data is not a serialized object
   * @exception InvalidClassException
   *              if the class serial number does not match
   * @exception ClassCastException
   *              if the data is not a serialized <code>Tffsr</code>
   * @exception ClassNotFoundException
   *              if the class of the serialized object cannot be found
   */
  public static Tffsr load(InputStream stream) throws IOException, OptionalDataException,
      ClassCastException, ClassNotFoundException, InvalidClassException {
    ObjectInputStream s = new ObjectInputStream(stream);
    return (Tffsr) s.readObject();
  }

  /**
   * Retrieves a serialized <code>Tffsr</code> located by a URL.
   * 
   * @param url
   *          URL of serialized Tffsr
   * @exception IOException
   *              if input/output related exception occurs
   * @exception OptionalDataException
   *              if the data is not a serialized object
   * @exception InvalidClassException
   *              if the class serial number does not match
   * @exception ClassCastException
   *              if the data is not a serialized <code>Tffsr</code>
   * @exception ClassNotFoundException
   *              if the class of the serialized object cannot be found
   */
  public static Tffsr load(URL url) throws IOException, OptionalDataException, ClassCastException,
      ClassNotFoundException, InvalidClassException {
    return load(url.openStream());
  }

  /** Returns new (deterministic) automaton with the empty language. */
  public static Tffsr makeEmpty() {
    Tffsr a = new Tffsr();
    State s = new State();
    a.initial = s;
    a.deterministic = true;
    return a;
  }

  /** Returns new (deterministic) automaton that does not acceps anything. */
  public static Tffsr makeEmptyString() {
    Tffsr a = new Tffsr();
    State s0 = new State();
    a.initial = s0;
    State s1 = new State();
    s1.setAccept(true);
    Transition t = new Transition(new TfString( new SimpleTf()), s1);
    s0.addOutTran(t);
    a.deterministic = true;
    return a;
  }

  /**
   * Sets or resets minimize always flag. If this flag is set, then
   * {@link #minimize()}will automatically be invoked after all operations that
   * otherwise may produce non-minimal automata. By default, the flag is not
   * set.
   * 
   * @param flag
   *          if true, the flag is set
   */
  static public void setMinimizeAlways(boolean flag) {
    minimize_always = flag;
  }

  /**
   * Returns new Tffsr that accepts the union of the languages of the given
   * automata.
   * <p>
   * Complexity: linear in number of states.
   */
  static public Tffsr union(List<Tffsr> l) {
    State s = new State();
    Iterator<Tffsr> i = l.iterator();
    while (i.hasNext()) {
      Tffsr b = i.next().clone();
      s.addEpsilon(b.initial);
    }
    Tffsr a = new Tffsr();
    a.initial = s;
    a.deterministic = false;
    a.checkMinimizeAlways();
    return a;
  }

  /**
   * Adds epsilon transitions to this Tffsr. This method adds extra character
   * interval transitions that are equivalent to the given set of epsilon
   * transitions.
   * 
   * @param pairs
   *          collection of {@link StatePair}objects representing pairs of
   *          source/destination states where epsilon transitions should be
   *          added
   */
  public void addEpsilons(Collection<StatePair> pairs) {
    HashMap<State, HashSet<State>> forward = new HashMap<State, HashSet<State>>();
    HashMap<State, HashSet<State>> back = new HashMap<State, HashSet<State>>();
    Iterator<StatePair> i = pairs.iterator();
    while (i.hasNext()) {
      StatePair p = i.next();
      HashSet<State> to = forward.get(p.s1);
      if (to == null) {
        to = new HashSet<State>();
        forward.put(p.s1, to);
      }
      to.add(p.s2);
      HashSet<State> from = back.get(p.s2);
      if (from == null) {
        from = new HashSet<State>();
        back.put(p.s2, from);
      }
      from.add(p.s1);
    }
    // calculate epsilon closure
    LinkedList<StatePair> worklist = new LinkedList<StatePair>(pairs);
    HashSet<StatePair> workset = new HashSet<StatePair>(pairs);
    while (!worklist.isEmpty()) {
      StatePair p = worklist.removeFirst();
      workset.remove(p);
      HashSet<State> to = forward.get(p.s2);
      HashSet<State> from = back.get(p.s1);
      if (to != null) {
        Iterator<State> j = to.iterator();
        while (j.hasNext()) {
          State s = j.next();
          StatePair pp = new StatePair(p.s1, s);
          if (!pairs.contains(pp)) {
            pairs.add(pp);
            forward.get(p.s1).add(s);
            back.get(s).add(p.s1);
            worklist.add(pp);
            workset.add(pp);
            if (from != null) {
              Iterator<State> k = from.iterator();
              while (k.hasNext()) {
                State q = k.next();
                StatePair qq = new StatePair(q, p.s1);
                if (!workset.contains(qq)) {
                  worklist.add(qq);
                  workset.add(qq);
                }
              }
            }
          }
        }
      }
    }
    // add transitions
    i = pairs.iterator();
    while (i.hasNext()) {
      StatePair p = i.next();
      p.s1.addEpsilon(p.s2);
    }
    deterministic = false;
    checkMinimizeAlways();
  }

  /**
   * Assign another Tffsr to this
   * 
   * @param Tffsr
   */
  public void assign(Tffsr tffsr) {
    this.initial = tffsr.initial;
    this.deterministic = tffsr.deterministic;
    this.info = tffsr.info;
  }

  /** Returns a clone of this Tffsr. */
  public Tffsr clone() {
    Tffsr a = new Tffsr();
    HashMap<State, State> m = new HashMap<State, State>();
    Set<State> states = getStates();
    Iterator<State> i = states.iterator();
    while (i.hasNext())
      m.put(i.next(), new State());
    i = states.iterator();
    while (i.hasNext()) {
      State s = i.next();
      State p = m.get(s);
      p.setAccept(s.isAccept());
      if (s == initial) a.initial = p;
      p.setTransitions(new HashSet<Transition>());
      Iterator<Transition> j = s.getTransitions().iterator();
      while (j.hasNext()) {
        Transition t = j.next();
        p.addOutTran(new Transition(t.label, m.get(t.getTo())));
      }
    }
    a.deterministic = deterministic;
    a.info = info;
    return a;
  }

  /**
   * COMPLEMENT Returns new (deterministic) Tffsr that accepts the complement of
   * the language of this Tffsr.
   * <p>
   * Complexity: linear in number of states (if already deterministic).
   */
  public Tffsr complement() {
//    a.determinize(); TODO to check if is necesary to determinize 
    Tffsr a = this.totalize();
    Iterator<State> i = a.getStates().iterator();
    while (i.hasNext()) {
      State p = i.next();
      p.setAccept(!p.isAccept());
    }
    a.removeDeadTransitions();
    return a;
  }

  /**
   * Returns new Tffsr that accepts the concatenation of the languages of this
   * and the given Tffsr.
   * <p>
   * Complexity: linear in number of states.
   */
  public Tffsr concatenate(Tffsr a) {
    a = (Tffsr) a.clone();
    Tffsr b = (Tffsr) clone();
    Iterator<State> i = b.getAcceptStates().iterator();
    while (i.hasNext()) {
      State s = i.next();
      s.setAccept(false);
      s.addEpsilon(a.initial);
    }
    b.deterministic = false;
    b.checkMinimizeAlways();
    return b;
  }

  /**
   * DETERMINIZATION Determinizes this Tffsr.
   * <p>
   * Complexity: exponential in number of states.
   */
  public void determinizeTF() {
    if (deterministic) return;
    Set<State> initialset = new HashSet<State>();
    initialset.add(initial);
    determinizeTF(initialset);
  }
  
  /**
   * DETERMINIZATION Determinizes this Tffsr.
   * <p>
   * Complexity: exponential in number of states.
   */
  public void determinizeSymbols() {
    if (deterministic) return;
    Set<State> initialset = new HashSet<State>();
    initialset.add(initial);
    determinizeSymbols(initialset);
  }
  

  private void determinizeSymbols(Set<State> initialset) {
	
	simplifyTransitionLabels();

    // subset construction
    Map<Set<State>, Set<State>> sets = new HashMap<Set<State>, Set<State>>();
    // stores new states
    Map<Set<State>, State> newstate = new HashMap<Set<State>, State>();
    // this isthe first partition
    sets.put(initialset, initialset);

    LinkedList<Set<State>> worklist = new LinkedList<Set<State>>();
    worklist.add(initialset);

    initial = new State();
    newstate.put(initialset, initial);
    // the new set of states each one
    // associated with a partition

	while (worklist.size() > 0) {
	  // the first partition
	  Set<State> d = worklist.removeFirst();
	  // the new corresponding state
	  State nd = (State) newstate.get(d);

	  // if any of the old states was final, this is final
	  for (State q : d)
		if (q.isAccept()) {
		  nd.setAccept(true);
		  break;
		}

	  // obtener el conjunto de tfs asociadas a los estados de p
	  Set<TfI> relevatTFs = getRelevant(d);
	  for (TfI rtf : relevatTFs) {
		// transp union
		Set<State> targets = new HashSet<State>();
		for (State s : d)
		  for (Transition t : s.getTransitions())
			if (rtf.equals(t.label.get(0))) {
			  targets.add(t.getTo());
			}

		if (!sets.containsKey(targets)) {
		  sets.put(targets, targets);
		  worklist.add(targets);
		  newstate.put(targets, new State());
		}
		nd.addOutTran(new Transition(new TfString(rtf), (State) newstate.get(targets)));
	  }
	}      
  
    removeDeadTransitions(); // removes the old tffsr
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
      if (s.isAccept()) accepts.add(s);
      Iterator<Transition> i = s.getTransitions().iterator();
      while (i.hasNext()) {
        Transition t = i.next();
        if (!visited.contains(t.getTo())) {
          visited.add(t.getTo());
          worklist.add(t.getTo());
        }
      }
    }
    return accepts;
  }

  /**
   * Returns extra information associated with this Tffsr.
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

  /** Returns number of states in this Tffsr. */
  public int getNumberOfStates() {
    return getStates().size();
  }

  /**
   * Returns number of transitions in this Tffsr. This number is counted as the
   * total number of edges, where one edge may be a character interval.
   */
  public int getNumberOfTransitions() {
    int c = 0;
    Iterator<State> i = getStates().iterator();
    while (i.hasNext()) {
      State s = i.next();
      c += s.getTransitions().size();
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
      Iterator<Transition> i = s.getTransitions().iterator();
      while (i.hasNext()) {
        Transition t = i.next();
        if (!visited.contains(t.getTo())) {
          visited.add(t.getTo());
          worklist.add(t.getTo());
        }
      }
    }
    return visited;
  }

  /**
   * Returns hash code for this Tffsr. The hash code is based on the number of
   * states and transitions in the minimized Tffsr.
   */
  public int hashCode() {
    if (hash_code == 0) minimize();
    return hash_code;
  }

  /**
   * INTERSECTION Returns new (deterministic) Tffsr that accepts the
   * intersection of the languages of this and the given Tffsr. As a
   * side-effect, both this and the given Tffsr are determinized, if not already
   * deterministic.
   * <p>
   * Complexity: quadratic in number of states (if already deterministic).
   */
  public Tffsr intersection(Tffsr a) {
    determinizeSymbols();
    a.determinizeSymbols();
    Map<Integer, Set<Transition>> transitions1 = getTransitions(getStates());
    Map<Integer, Set<Transition>> transitions2 = getTransitions(a.getStates());
    Tffsr c = new Tffsr();
    LinkedList<StatePair> worklist = new LinkedList<StatePair>();
    HashMap<StatePair, StatePair> newstates = new HashMap<StatePair, StatePair>();
    State s = new State();
    c.initial = s;
    StatePair p = new StatePair(s, initial, a.initial);
    worklist.add(p);
    newstates.put(p, p);
    while (worklist.size() > 0) {
      p = worklist.removeFirst();
      p.s.setAccept(p.s1.isAccept() && p.s2.isAccept());
      Set<Transition> ts1 = transitions1.get(Integer.valueOf(p.s1.getNumber()));
      Set<Transition> ts2 = transitions2.get(Integer.valueOf(p.s2.getNumber()));
      
      for (Transition t1 : ts1)
    	for (Transition t2 : ts2) {
          StatePair q = new StatePair(t1.getTo(), t2.getTo());
          StatePair r = newstates.get(q);
          if (r == null) {
            q.s = new State();
            worklist.add(q);
            newstates.put(q, q);
            r = q;
          }
          p.s.addOutTran(new Transition(new TfString(t1.label.get(0).andSimple(t2.label.get(0))), r.s));  
    	}
    }
    c.deterministic = true;
    c.removeDeadTransitions();
    c.checkMinimizeAlways();
    return c;
  }

  /**
   * Returns deterministic flag for this Tffsr.
   * 
   * @return true if the Tffsr is definitely deterministic, false if the Tffsr
   *         may be nondeterministic
   * 
   * @uml.property name="deterministic"
   */
  public boolean isDeterministic() {
    return deterministic;
  }

  /** Returns true if this Tffsr accepts no strings. */
  public boolean isEmpty() {
    return initial.isAccept() == false && initial.getTransitions().size() == 0;
  }

  /** Returns true if the language of this Tffsr is finite. */
  public boolean isFinite() {
    return isFinite(initial, new HashSet<State>());
  }

  /**
   * MINIMIZATION Minimizes (and determinizes if not already deterministic) this
   * Tffsr.
   * 
   * @see #setMinimization(int)
   */
  public void minimize() {
    this.setDeterministic(false);
    this.minimizeBrzozowski();
    // recompute hash code
    hash_code = getNumberOfStates() * 3 + getNumberOfTransitions() * 2;
    if (hash_code == 0) hash_code = 1;
  }

  /**
   * Returns new Tffsr that accepts the union of the empty string and the
   * language of this Tffsr.
   * <p>
   * Complexity: linear in number of states.
   */
  public Tffsr optional() {
    Tffsr a = (Tffsr) clone();
    State s = new State();
    s.addEpsilon(a.initial);
    s.setAccept(true);
    a.initial = s;
    a.deterministic = false;
    a.checkMinimizeAlways();
    return a;
  }

  /**
   * Removes transitions to dead states and calls {@link #reduce()}(a state is
   * "dead" if no accept state is reachable from it).
   */
  public void removeDeadTransitions() {
    Set<State> states = getStates();
    Set<State> live = getLiveStates(states);
    Iterator<State> i = states.iterator();
    while (i.hasNext()) {
      State s = i.next();
      Iterator<Transition> j = s.getTransitions().iterator();
      s.resetTransitions();
      while (j.hasNext()) {
        Transition t = j.next();
        if (live.contains(t.getTo())) s.addOutTran(t);
      }
    }
  }

  /**
   * KLEENE STAR Returns new Tffsr that accepts the Kleene star (zero or more
   * concatenated repetitions) of the language of this Tffsr.
   * <p>
   * Complexity: linear in number of states.
   */
  public Tffsr kleene() {
    Tffsr a = (Tffsr) clone();
    State s = new State();
    s.setAccept(true);

    s.addEpsilon(a.initial);

    Iterator<State> i = a.getAcceptStates().iterator();
    while (i.hasNext()) {
      State p = i.next();
      p.addEpsilon(s);
    }
    a.initial = s;
    a.deterministic = false;
    a.checkMinimizeAlways();
    return a;
  }

  /**
   * Returns new Tffsr that accepts <code>min</code> or more concatenated
   * repetitions of the language of this Tffsr.
   * <p>
   * Complexity: linear in number of states and in <code>min</code>.
   */
  public Tffsr kleene(int min) {
    Tffsr a = kleene();
    while (min-- > 0)
      a = concatenate(a);
    return a;
  }

  /**
   * Returns new Tffsr that accepts between <code>min</code> and
   * <code>max</code> (including both) concatenated repetitions of the language
   * of this Tffsr.
   * <p>
   * Complexity: linear in number of states and in <code>min</code> and
   * <code>max</code>.
   */
  public Tffsr kleene(int min, int max) {
    if (min > max) return makeEmpty();
    max -= min;
    Tffsr a;
    if (min == 0) a = makeEmptyString();
    else if (min == 1) a = (Tffsr) clone();
    else {
      a = this;
      while (--min > 0)
        a = concatenate(a);
    }
    if (max == 0) return a;
    Tffsr d = (Tffsr) clone();
    while (--max > 0) {
      Tffsr c = (Tffsr) clone();
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

  /**
   * Restores representation invariant. This method must be invoked before any
   * built-in tffsrs operation is performed if Tffsr states or transitions are
   * manipulated manually.
   * 
   * @see #setDeterministic(boolean)
   */
  public void restoreInvariant() {
    removeDeadTransitions();
    hash_code = 0;
  }

  /**
   * Reverses the language of this (non-singleton) automaton while returning the
   * set of new initial states. (Used for <code>minimizeBrzozowski()</code>)
   */
  public Set<State> reverse() {
	
	toSingleLabelTransitions();
	
    // reverse all edges
    HashMap<State, HashSet<Transition>> m = new HashMap<State, HashSet<Transition>>();
    Set<State> states = getStates();
    Set<State> accept = getAcceptStates();
    Iterator<State> i = states.iterator();
    while (i.hasNext()) {
      State r = i.next();
      m.put(r, new HashSet<Transition>());
      r.setAccept(false);
    }
    i = states.iterator();
    while (i.hasNext()) {
      State r = i.next();
      Iterator<Transition> j = r.getTransitions().iterator();
      while (j.hasNext()) {
        Transition t = j.next();
        (m.get(t.getTo())).add(new Transition(t.label, r)); 
      }
    }
    i = states.iterator();
    while (i.hasNext()) {
      State r = i.next();
      r.setTransitions(m.get(r));
    }
    // make new initial+final states
    initial.setAccept(true);
    initial = new State();
    i = accept.iterator();
    while (i.hasNext()) {
      State r = i.next();
      initial.addEpsilon(r); // ensures that all initial states are reachable
    }
    deterministic = false;
    return accept;
  }

  /**
   * Sets deterministic flag for this Tffsr. This method should (only) be used
   * if Tffsrs are constructed manually.
   * 
   * @param deterministic
   *          true if the Tffsr is definitely deterministic, false if the Tffsr
   *          may be nondeterministic
   * 
   * @uml.property name="deterministic"
   */
  public void setDeterministic(boolean deterministic) {
    this.deterministic = deterministic;
  }

  /**
   * Associates extra information with this Tffsr.
   * 
   * @param info
   *          extra information
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
   *          state
   */
  public void setInitialState(State s) {
    initial = s;
  }

  /**
   * Writes this <code>Tffsr</code> to the given stream.
   * 
   * @param stream
   *          output stream for serialized Tffsr
   * @exception IOException
   *              if input/output related exception occurs
   */
  public void store(OutputStream stream) throws IOException {
    ObjectOutputStream s = new ObjectOutputStream(stream);
    s.writeObject(this);
    s.flush();
  }

  /**
   * Returns <a href="http://www.research.att.com/sw/tools/graphviz/"
   * target="_top">Graphviz Dot </a> representation of this Tffsr.
   */
  public String toDot() {

    StringBuffer b = new StringBuffer("digraph Tffst {\n"); //$NON-NLS-1$
    b.append("  rankdir = LR;\n"); //$NON-NLS-1$
    Set<State> states = getStates();
    setStateNumbers(states);
    Iterator<State> i = states.iterator();
    while (i.hasNext()) {
      State s = i.next();
      b.append("  ").append(s.getNumber()); //$NON-NLS-1$
      if (s.isAccept()) b
          .append(" [shape=doublecircle,label=\"").append(s.getNumber()).append("\"];\n"); //$NON-NLS-1$
      else
        b.append(" [shape=circle,label=\"").append(s.getNumber()).append("\"];\n"); //$NON-NLS-1$
      if (s == initial) {
        b.append("  initial [shape=plaintext,label=\"\"];\n"); //$NON-NLS-1$
        b.append("  initial -> ").append(s.getNumber()).append("\n"); //$NON-NLS-1$ //$NON-NLS-2$
      }
      Iterator<Transition> j = s.getTransitions().iterator();
      while (j.hasNext()) {
        Transition t = j.next();
        b.append("  ").append(s.getNumber()); //$NON-NLS-1$
        t.appendDot(b);
      }
    }
    return b.append("}\n").toString(); //$NON-NLS-1$
  }

  /**
   * Returns <a href="http://www.research.att.com/sw/tools/graphviz/"
   * target="_top">Graphviz Dot </a> representation of this Tffst.
   */
  public String toDot(String label) {

    StringBuffer b = new StringBuffer("digraph Tffst {\n"); //$NON-NLS-1$
    b.append("rankdir = \"LR\"\n;");
    b.append("label = \"").append(label).append("\"\n;");
    Set<State> states = getStates();
    setStateNumbers(states);
    Iterator<State> i = states.iterator();
    while (i.hasNext()) {
      State s = i.next();
      b.append("  ").append(s.getNumber()); //$NON-NLS-1$
      if (s.isAccept()) b
          .append(" [shape=doublecircle,label=\"").append(s.getNumber()).append("\"];\n"); //$NON-NLS-1$
      else
        b.append(" [shape=circle,label=\"").append(s.getNumber()).append("\"];\n"); //$NON-NLS-1$
      if (s == initial) {
        b.append("  initial [shape=plaintext,label=\"\"];\n"); //$NON-NLS-1$
        b.append("  initial -> ").append(s.getNumber()).append("\n"); //$NON-NLS-1$ //$NON-NLS-2$
      }
      Iterator<Transition> j = s.getTransitions().iterator();
      while (j.hasNext()) {
        Transition t = j.next();
        b.append("  ").append(s.getNumber()); //$NON-NLS-1$
        t.appendDot(b);
      }
    }
    return b.append("}\n").toString(); //$NON-NLS-1$
  }

  /** Returns a string representation of this Tffsr. */
  public String toString() {
    StringBuffer b = new StringBuffer();
    Set<State> states = getStates();
    setStateNumbers(states);
    b.append("initial state: ").append(initial.getNumber()).append("\n"); //$NON-NLS-1$ //$NON-NLS-2$
    Iterator<State> i = states.iterator();
    while (i.hasNext()) {
      State s = i.next();
      b.append(s.toString());
    }
    return b.toString();
  }

  /**
   * returns the Tffst corresponding to this tffsr. it leaves epsilon
   * transitions as is
   * 
   * @return
   */

  public Tffst toTffst() {
    Tffst a = new Tffst();
    HashMap<State, uy.edu.fing.mina.fsa.tffst.State> m = new HashMap<State, uy.edu.fing.mina.fsa.tffst.State>();

    for (State s : getStates()) 
      m.put(s, new uy.edu.fing.mina.fsa.tffst.State());

    for (State s : m.keySet()) {
      uy.edu.fing.mina.fsa.tffst.State p = m.get(s);
      p.setAccept(s.isAccept());
      if (s == initial) a.setInitialState(p);
      for (Transition t :  s.getTransitions()) {
        uy.edu.fing.mina.fsa.tffst.State to = m.get(t.getTo());
        if (t.label.get(0) instanceof TfPair) //FIXME needs to be simplified
          p.addOutTran(new uy.edu.fing.mina.fsa.tffst.Transition(((TfPair)t.label.get(0)).tfIn, ((TfPair)t.label.get(0)).tfOut, to));
       
          
      }
    }
    a.setDeterministic(deterministic);
    a.setInfo(info);
    return a;
  }

  /**
   * Returns the Tffst result of apply the identity operation to this tffsr
   * 
   * @return a TFFST
   */

  public Tffst identity() {
    Tffst a = new Tffst();
    HashMap<State, uy.edu.fing.mina.fsa.tffst.State> m = new HashMap<State, uy.edu.fing.mina.fsa.tffst.State>();
    Set<State> states = getStates();

    for (Iterator<State> iter = states.iterator(); iter.hasNext();) {
      State s = iter.next();
      m.put(s, new uy.edu.fing.mina.fsa.tffst.State());
    }

    for (Iterator<State> iter = states.iterator(); iter.hasNext();) {
      State s = iter.next();
      uy.edu.fing.mina.fsa.tffst.State p = m.get(s);

      p.setAccept(s.isAccept());
      if (s == initial) a.setInitialState(p);

      p.setTransitions(new HashSet<uy.edu.fing.mina.fsa.tffst.Transition>());

      for (Iterator<Transition> iterator = s.getTransitions().iterator(); iterator.hasNext();) {
        Transition t = iterator.next();
        uy.edu.fing.mina.fsa.tffst.State to = m.get(t.getTo());
        Set<uy.edu.fing.mina.fsa.tffst.Transition> trans = p.getTransitions();
        uy.edu.fing.mina.fsa.tffst.Transition id = new uy.edu.fing.mina.fsa.tffst.Transition(
            t.label.get(0), t.label.get(0), to, 1); //FIXME label must be simple
        trans.add(id);
      }
    }
    a.setDeterministic(deterministic);
    a.setInfo(info);
    return a;
  }

  
  /**
   * Returns a Tffst that relates the language recognized by the tffsr with epsilon.
   * 
   * @return a TFFST
   */

  public Tffst toEpsilon() {
    Tffst a = new Tffst();
    HashMap<State, uy.edu.fing.mina.fsa.tffst.State> m = new HashMap<State, uy.edu.fing.mina.fsa.tffst.State>();
    Set<State> states = getStates();

    for (Iterator<State> iter = states.iterator(); iter.hasNext();) {
      State s = iter.next();
      m.put(s, new uy.edu.fing.mina.fsa.tffst.State());
    }

    for (Iterator<State> iter = states.iterator(); iter.hasNext();) {
      State s = iter.next();
      uy.edu.fing.mina.fsa.tffst.State p = m.get(s);

      p.setAccept(s.isAccept());
      if (s == initial) a.setInitialState(p);

      p.setTransitions(new HashSet<uy.edu.fing.mina.fsa.tffst.Transition>());

      for (Iterator<Transition> iterator = s.getTransitions().iterator(); iterator.hasNext();) {
        Transition t = iterator.next();
        uy.edu.fing.mina.fsa.tffst.State to = m.get(t.getTo());
        Set<uy.edu.fing.mina.fsa.tffst.Transition> trans = p.getTransitions();
        uy.edu.fing.mina.fsa.tffst.Transition id = new uy.edu.fing.mina.fsa.tffst.Transition(
            t.label.get(0), SimpleTf.Epsilon(), to); //FIXME label must be simple
        trans.add(id);
      }
    }
    a.setDeterministic(deterministic);
    a.setInfo(info);
    return a;
  }

  
  /**
   * Returns a Tffst with the original tffsr as range and * as domain
   * 
   * @return a TFFST
   */

  public Tffst asRange() {
    Tffst a = new Tffst();
    HashMap<State, uy.edu.fing.mina.fsa.tffst.State> m = new HashMap<State, uy.edu.fing.mina.fsa.tffst.State>();
    Set<State> states = getStates();

    for (Iterator<State> iter = states.iterator(); iter.hasNext();) {
      State s = iter.next();
      m.put(s, new uy.edu.fing.mina.fsa.tffst.State());
    }

    for (Iterator<State> iter = states.iterator(); iter.hasNext();) {
      State s = iter.next();
      uy.edu.fing.mina.fsa.tffst.State p = m.get(s);

      p.setAccept(s.isAccept());
      if (s == initial) a.setInitialState(p);

      p.setTransitions(new HashSet<uy.edu.fing.mina.fsa.tffst.Transition>());

      for (Iterator<Transition> iterator = s.getTransitions().iterator(); iterator.hasNext();) {
        Transition t = iterator.next();
        uy.edu.fing.mina.fsa.tffst.State to = m.get(t.getTo());
        Set<uy.edu.fing.mina.fsa.tffst.Transition> trans = p.getTransitions();
        uy.edu.fing.mina.fsa.tffst.Transition rg = new uy.edu.fing.mina.fsa.tffst.Transition(
            SimpleTf.AcceptsAll(), t.label.get(0), to, 1);
        trans.add(rg);
      }
    }
    a.setDeterministic(deterministic);
    a.setInfo(info);
    return a;
  }

  /*
   * 
   * Main operations
   * ************************************************************************
   */

  /**
   * UNION Returns new Tffsr that accepts the union of the languages of this and
   * the given Tffsr.
   * <p>
   * Complexity: linear in number of states.
   */
  public Tffsr union(Tffsr a) {
    a = (Tffsr) a.clone();
    Tffsr b = (Tffsr) clone();
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
   */
  private void determinizeTF(Set<State> initialset) {
	
	this.simplifyTransitionLabels();

    // subset construction
    Map<Set<State>, Set<State>> sets = new HashMap<Set<State>, Set<State>>();
    // stores new states
    Map<Set<State>, State> newstate = new HashMap<Set<State>, State>();
    // this isthe first partition
    sets.put(initialset, initialset);

    LinkedList<Set<State>> worklist = new LinkedList<Set<State>>();
    worklist.add(initialset);

    initial = new State();
    newstate.put(initialset, initial);
    // the new set of states each one
    // associated with a partition

    while (worklist.size() > 0) {
      // the first partition
      Set<State> d = worklist.removeFirst();
      // the new corresponding state
      State nd = (State) newstate.get(d);

      // if any of the old states was final, this is final
      for (State q : d)
        if (q.isAccept()) {
          nd.setAccept(true);
          break;
        }

      // obtener el conjunto de tfs asociadas a los estados de p
      Set<Partition> partitions = Partition.getPartitions3(getRelevant(d));
      for (Partition partition : partitions) {
        TfI tfpartition = Partition.toTfrelation3(partition);
        // transD union of each positive TF
        if (!tfpartition.equals(SimpleTf.AcceptsNone())) {
          Set<State> tDUnion = unionOfTransD(d, partition.left); 
          if (!sets.containsKey(tDUnion)) {
            sets.put(tDUnion, tDUnion);
            worklist.add(tDUnion);
            newstate.put(tDUnion, new State());
          }
          nd.addOutTran(new Transition(new TfString(tfpartition), (State) newstate.get(tDUnion)));
        }
      }      
    }
    deterministic = true;
    removeDeadTransitions(); // removes the old tffsr
  }

  private Set<TfI> getRelevant(Set<State> p) {
    Set<TfI> relevantTFs = new HashSet<TfI>();
    for (State s : p)
      for (Transition t : s.getTransitions())
        relevantTFs.add(t.label.get(0)); //FIXME tffst must have been simplified

    return relevantTFs;
  }

  /**
   * Minimize using Brzozowski's algorithm. aca asi se puede ahorrar la
   * computacion de las tf exclusivas
   */
  private void minimizeBrzozowski() {
    reverse();
    determinizeSymbols();    
    reverse();
    determinizeSymbols();
  }
  
  

//	/** Minimize using Huffman's algorithm. */
//	private void minimizeHuffman() {
//		determinize();
//		totalize();
//		Set ss = getStates();
//		Transition[][] transitions = new Transition[ss.size()][];
//		State[] states = (State[]) ss.toArray(new State[0]);
//		boolean[][] mark = new boolean[states.length][states.length];
//		HashSet[][] triggers = new HashSet[states.length][states.length];
//		// initialize marks based on acceptance status and find transition
//		// arrays
//		for (int n1 = 0; n1 < states.length; n1++) {
//			states[n1].setNumber(n1);
//			transitions[n1] = states[n1].getSortedTransitionArray(false);
//			for (int n2 = n1 + 1; n2 < states.length; n2++)
//				if (states[n1].accept != states[n2].accept)
//					mark[n1][n2] = true;
//		}
//		// for all pairs, see if states agree
//		for (int n1 = 0; n1 < states.length; n1++)
//			for (int n2 = n1 + 1; n2 < states.length; n2++)
//				if (!mark[n1][n2]) {
//					if (statesAgree(transitions, mark, n1, n2))
//						addTriggers(transitions, mark, triggers, n1, n2);
//					else
//						markPair(mark, triggers, n1, n2);
//				}
//		// assign equivalence class numbers to states
//		int numclasses = 0;
//		for (int n = 0; n < states.length; n++)
//			states[n].setNumber(-1);
//		for (int n1 = 0; n1 < states.length; n1++)
//			if (states[n1].getNumber() == -1) {
//				states[n1].setNumber(numclasses);
//				for (int n2 = n1 + 1; n2 < states.length; n2++)
//					if (!mark[n1][n2])
//						states[n2].setNumber(numclasses);
//				numclasses++;
//			}
//		// make a new state for each equivalence class
//		State[] newstates = new State[numclasses];
//		for (int n = 0; n < numclasses; n++)
//			newstates[n] = new State();
//		// select a class representative for each class and find the new initial
//		// state
//		for (int n = 0; n < states.length; n++) {
//			newstates[states[n].getNumber()].setNumber(n);
//			if (states[n] == initial)
//				initial = newstates[states[n].getNumber()];
//		}
//		// build transitions and set acceptance
//		for (int n = 0; n < numclasses; n++) {
//			State s = newstates[n];
//			s.accept = states[s.getNumber()].accept;
//			Iterator i = states[s.getNumber()].getTransitions().iterator();
//			while (i.hasNext()) {
//				Transition t = (Transition) i.next();
//				s.addOutTran(new Transition(t.label,newstates[t.getTo().getNumber()]));
//			}
//		}
//		removeDeadTransitions();
//	}

  
  
  
  /**
   * 
   * implements the function TransD. it returns a set of states in transitions
   * must be getSortedTransitions' output
   * 
   */
  
  private Set<State> unionOfTransD(Set<State> states, List<TfI> left) {

    Set<State> targets = new HashSet<State>();

    for (TfI tf : left) {
      if (!tf.isEpsilon()) 
        for (State s : states)
        for (Transition t : s.getTransitions())
          if (tf.equals(t.label.get(0))) { 
            targets.add(t.getTo());
          }
    }
    return targets;
  }

  
  
  /**
     * 
     * 
     */
  void checkMinimizeAlways() {
    if (minimize_always) minimize();
  }

  /**
   * 
   * @param states
   * @return
   */
  Set<State> getLiveStates(Set<State> states) {
    HashMap<State, HashSet<State>> map = new HashMap<State, HashSet<State>>();
    Iterator<State> i = states.iterator();
    while (i.hasNext()) {
      State s = i.next();
      map.put(s, new HashSet<State>());
    }
    i = states.iterator();
    while (i.hasNext()) {
      State s = i.next();
      Iterator<Transition> j = s.getTransitions().iterator();
      while (j.hasNext()) {
        Transition t = j.next();
        map.get(t.getTo()).add(s);
      }
    }
    Set<State> live = new HashSet<State>(getAcceptStates());
    LinkedList<State> worklist = new LinkedList<State>(live);
    while (worklist.size() > 0) {
      State s = (State) worklist.removeFirst();
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
   * Returns sorted array of transitions for each state, by state "number". (and
   * sets state numbers).
   */
  Transition[][] getSortedTransitions(Set<State> states) {
    setStateNumbers(states);
    Transition[][] transitions = new Transition[states.size()][];
    Iterator<State> i = states.iterator();
    while (i.hasNext()) {
      State s = i.next();
      transitions[s.getNumber()] = s.getTransitions().toArray(transitions[s.getNumber()]); 
    }
    return transitions;
  }

  /**
   * Returns sorted array of transitions for each state, by state "number". (and
   * sets state numbers).
   */
  Map<Integer,Set<Transition>> getTransitions(Set<State> states) {
    setStateNumbers(states);
    Map<Integer,Set<Transition>> transitions = new HashMap<Integer, Set<Transition>>();
    for (State state : states) {
	  transitions.put(state.getNumber(), state.getTransitions());
	}
    return transitions;
  }
  
  
  /**
   * Checks whether there is a loop containing s. (This is sufficient since
   * there are never transitions to dead states.)
   */
  boolean isFinite(State s, HashSet<State> path) {
    path.add(s);
    Iterator<Transition> i = s.getTransitions().iterator();
    while (i.hasNext()) {
      Transition t = i.next();
      if (path.contains(t.getTo()) || !isFinite(t.getTo(), path)) return false;
    }
    path.remove(s);
    return true;
  }

  /**
   * Assigns consecutive numbers to the given states. FIXED
   */
  void setStateNumbers(Set<State> states) {
    Iterator<State> i = states.iterator();
    int number = 0;
    while (i.hasNext()) {
      State s = i.next();
      s.setNumber(number++);
    }
  }

  /**
   * Adds transitions to explicit crash state to ensure that transition function
   * is total.
   */
  public Tffsr totalize() {
	
	Tffsr simple = this.toSingleLabelTransitions();
	
	// the crash state
	State s = new State();
//    s.addOutTran(new Transition(new TfString(SimpleTf.AcceptsAll()), s));
    s.addEpsilon(this.initial);
    
	Iterator<State> i = simple.getStates().iterator();
    while (i.hasNext()) {
      State p = i.next();
      TfI newTf = SimpleTf.AcceptsNone();
      Iterator<Transition> j = p.getTransitions().iterator();
      if (j.hasNext()) {
        while (j.hasNext()) {
          Transition t = j.next();
          newTf = newTf.orSimple(t.getLabel().get(0));
        }
      }
      p.addOutTran(new Transition( new TfString(newTf.not()), s));
    }

    simple.simplifyTransitionLabels();
    
    return simple;
  }

  /**
   * simplify all transitions in the tffsr
   * 
   */
  void simplifyTransitionLabels() {
    Iterator<State> i = getStates().iterator();
    while (i.hasNext()) {
      State s = (State) i.next();
      Set<Transition> sTransToRem = new HashSet<Transition>();
      Iterator<Transition> j = s.getTransitions().iterator();
      while (j.hasNext()) {
        Transition t = j.next();
        t.label = new TfString(Utils.simplify(t.label.get(0)));
        if (t.label.get(0).acceptsNone()) sTransToRem.add(t);
      }
      s.getTransitions().removeAll(sTransToRem);
    }
  }



  /**
   * returns a Tffst representing this tffst but with simple labels on each
   * edge. it expands subEpochs in simple pairs of TFs adding epsilons
   * 
   * @return
   */
  public Tffsr toSingleLabelTransitions() {

	Tffsr simpleTffst = new Tffsr();
	HashMap<State, State> m = new HashMap<State, State>();

	State last;
	Set<State> states = getStates();

	for (State s : states) {
	  if (m.get(s) == null) {
		last = new State();
		m.put(s, last);
		last.accept = s.accept;
		if (s == initial)
		  simpleTffst.initial = last;
	  } else
		last = m.get(s);

	  for (Transition t : s.getTransitions()) {

		if (m.get(t.getTo()) == null) {
		  State newTo = new State();
		  m.put(t.getTo(), newTo);
		  newTo.accept = t.getTo().accept;
		  if (t.getTo() == initial)
			  simpleTffst.initial = t.getTo();
		}

		Transition lastTran = new Transition(last, new TfString(t.label), m.get(t.getTo()));

		while (lastTran.getLabel().size() > 1) {
		  last = lastTran.getFrom();
		  State ns = new State();
		  lastTran.setFrom(ns);
		  new Transition(last, new TfString(lastTran.getLabel().remove(0)), ns);
		}
	  }
	}

	simpleTffst.deterministic = deterministic;
	simpleTffst.info = info;

	return simpleTffst;
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
    if (sl.size++ == 0) sl.first = sl.last = this;
    else {
      sl.last.next = this;
      prev = sl.last;
      sl.last = this;
    }
  }

  void remove() {
    sl.size--;
    if (sl.first == this) sl.first = next;
    else
      prev.next = next;
    if (sl.last == this) sl.last = prev;
    else
      next.prev = prev;
  }

  
//	private boolean statesAgree(Transition[][] transitions, boolean[][] mark,
//		int n1, int n2) {
//	Transition[] t1 = transitions[n1];
//	Transition[] t2 = transitions[n2];
//	for (int k1 = 0, k2 = 0; k1 < t1.length && k2 < t2.length;) {
//		if (t1[k1].max < t2[k2].min)
//			k1++;
//		else if (t2[k2].max < t1[k1].min)
//			k2++;
//		else {
//			int m1 = t1[k1].to.number;
//			int m2 = t2[k2].to.number;
//			if (m1 > m2) {
//				int t = m1;
//				m1 = m2;
//				m2 = t;
//			}
//			if (mark[m1][m2])
//				return false;
//			if (t1[k1].max < t2[k2].max)
//				k1++;
//			else
//				k2++;
//		}
//	}
//	return true;
//}
  
}
