/*
 * Finite State Transducers extended with Tautness Functions by Copyright (C)
 * 2004 Javier Baliosian All rights reserved.
 */

package uy.edu.fing.mina.fsa.tffst;

import java.io.Serializable;
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
import uy.edu.fing.mina.fsa.utils.Utils;

/*
 * Class invariants: - An TFFST is represented explicitly (with State and
 * Transition objects). If a TFFST is nondeterministic, then isDeterministic()
 * returns false (but the converse is not required).
 */

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

}

public class Tffst implements Serializable {

  /** Minimize always flag. */
  static boolean minimize_always;

  static final long serialVersionUID = 10001;

  /** Returns new (deterministic) tffst that acceps everything. */
  public static Tffst all() {
    Tffst a = new Tffst();
    State s0 = new State();
    a.initial = s0;
    State s1 = new State();
    s1.setAccept(true);
    Transition t = new Transition(SimpleTf.AcceptsAll(), SimpleTf.AcceptsAll(), s1,1);
    s0.addTransition(t);
    a.deterministic = true;
    a = a.kleene();
    return a;
  }

  /**
   * CONCATENATION Returns new Tffst that accepts the concatenation of the
   * languages of the given automata.
   * <p>
   * Complexity: linear in total number of states.
   */
  static public Tffst concatenate(List<Tffst> l) {
    if (l.isEmpty()) return makeEmptyString();
    Iterator<Tffst> i = l.iterator();
    Tffst b = null;
    try {
      b = i.next().clone();
      Iterator<State> j = b.getAcceptStates().iterator();
      while (i.hasNext()) {
        Tffst a = ((Tffst) i.next()).clone();
        Set<State> ns = a.getAcceptStates();
        while (j.hasNext()) {
          State s = j.next();
          s.accept = false;
          s.addEpsilon(a.initial);
          if (s.accept) ns.add(s);
        }
        j = ns.iterator();
      }
      b.deterministic = false;
      b.checkMinimizeAlways();
    } catch (CloneNotSupportedException e) {
      e.printStackTrace();
    }
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
    Transition t = new Transition(new SimpleTf(), new SimpleTf(), s1);
    s0.addTransition(t);
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
   * Returns new Tffst that accepts the union of the languages of the given
   * automata.
   * <p>
   * Complexity: linear in number of states.
   */
  static public Tffst union(List<Tffst> l) {
    Tffst a = null;
    try {
      a = new Tffst();
      State s = new State();
      Iterator<Tffst> i = l.iterator();
      while (i.hasNext()) {
        Tffst b;
        b = (i.next()).clone();
        s.addEpsilon(b.initial);
      }
      a.initial = s;
      a.deterministic = false;
      a.checkMinimizeAlways();
    } catch (CloneNotSupportedException e) {
      e.printStackTrace();
    }
    return a;
  }

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
   * @throws CloneNotSupportedException 
   */
  public Tffst clone() throws CloneNotSupportedException {

    Tffst a = new Tffst();
    HashMap<State, State> m = new HashMap<State, State>();
    Set<State> states = getStates();

    for ( State s : states) {
      State p = m.get(s);
      if (p == null) {
        p = new State();
        m.put(s, p );
      }
      p.accept = s.accept;
      if (s == initial) a.initial = p;
    }
    for ( State s : states) {
      State p = m.get(s);
      for (Transition t : s.transitions){
        Transition tclone = t.clone();
        p.transitions.add(tclone);
        tclone.to = m.get(t.to);
      }
    }
    a.deterministic = deterministic;
    a.info = info;
    return a;
  }

  /**
   * COMPOSITION
   * this o theOther (i.e. theOther(this) )   
   * 
   * Constraint: this cannot have e-input labels and theOther cannot have e-output labels.  
   * 
   * ct((p1,π1,π1,q1,1),(p2,π2,π2,q2,1))=((p1,p2),π1∧π2,π1∧π2,(q1,q2),1)
   * ct((p1,φ,π1,q1,0),(p2,π2,π2,q2,1))=((p1,p2),φ,π1∧π2,(q1,q2),0)
   * ct((p1,π1,π1,q1,1),(p2,π2,ψ,q2,0))=((p1,p2),π1∧π2,ψ,(q1,q2),0)
   * ct((p1,φ,π1,q1,0),(p2,π2,ψ,q2,0))=((p1,p2),φ,ψ,(q1,q2),0) if
   * satisfiable(π1∧π2)
   * 
   * E= {ct(e1,e2 )|e1 ∈ E1 , e2 ∈ E2 } ∪ {((p1,p2 ), ,ψ,(p1,q2),0)|p1 ∈
   * Q1,(p2,,ψ,q2,0) ∈ E2 } ∪ {((p1,p2 ),φ, ,(q1,p2),0)|p2 ∈ Q2,(p1,φ,,q1,0) ∈
   * E1 }
   * 
   * 
   * 
   * 
   */
  
  public Tffst composition(Tffst theOther) { 

    Tffst thisSimple = this.toSimpleTransitions();
    Tffst theOtherSimple = theOther.toSimpleTransitions();
    
    Tffst out = new Tffst();

    Set<State> thisStates = thisSimple.getLiveStates();
    Set<State> theOtherStates = theOtherSimple.getLiveStates();

    HashMap<StatePair, State> newstates = new HashMap<StatePair, State>();

    for (State thisState : thisStates){
      for (State theOtherState : theOtherStates){
        
        // get the new state that is equivalent to this pair, create it. 
        State outStartState = newstates.get(new StatePair(thisState, theOtherState));
        if (outStartState == null) {
          outStartState = new State();
          newstates.put(new StatePair(thisState, theOtherState), outStartState);
          if (thisState.equals(thisSimple.initial) && theOtherState.equals(theOtherSimple.initial))
            out.initial = outStartState;
          outStartState.setAccept(thisState.isAccept() && theOtherState.isAccept());
        }

        Set<Transition> thisStateTrans = thisState.getTransitions();
        Set<Transition> theOtherStateTrans = theOtherState.getTransitions(); 
        //add fake e/e transition loop  
        thisStateTrans.add(new Transition(SimpleTf.Epsilon(),SimpleTf.Epsilon(),thisState));
        theOtherStateTrans.add(new Transition(SimpleTf.Epsilon(),SimpleTf.Epsilon(),theOtherState));

        for (Transition t1 : thisStateTrans) {
          for (Transition t2 : theOtherStateTrans) {
            if (t1.labelOut.get(0).andSimple(t2.labelIn.get(0)).satisfiable()){
//              if (t1.labelOut.get(0).equals(t2.labelIn.get(0))){

                // get the new state that is equivalent to this pair, create it. 
                State outToState = newstates.get(new StatePair(t1.to, t2.to));
                if (outToState == null){
                  outToState = new State();
                  newstates.put(new StatePair(t1.to, t2.to), outToState );
                  if (t1.to.equals(thisSimple.initial) && t2.to.equals(theOtherSimple.initial))
                    out.initial = outToState;
                  outToState.setAccept(t1.to.isAccept() && t2.to.isAccept());
                }
                try {
                  TfString lin = t1.labelIn.clone();
                  TfString lout = t2.labelOut.clone();
                  lout.get(0).setIdentityType(t2.labelOut.get(0).getIdentityType());
                  if (lout.get(0).getIdentityType() != 0) lout.get(0).setIdentityTf(lin.get(0));
                  
                  outStartState.transitions.add(new Transition(lin, lout, outToState));
                  
                } catch (CloneNotSupportedException e) {
                  e.printStackTrace();
                }
              }
          }
        }
      }
    }
    out.inLabelEpsilonRemoval();
    out.removeDeadTransitions();
//    out.checkMinimizeAlways();
    //out.setDeterministic(false);
    //out.determinize();
    return out;
  }

  /**
   * Returns new Tffst that accepts the concatenation of the languages of this
   * and the given Tffst.
   * <p>
   * Complexity: linear in number of states.
   */
  public Tffst concatenate(Tffst a) {
    Tffst b = null;
    try {
      a = a.clone();
      b = clone();
      Iterator<State> i = b.getAcceptStates().iterator();
      while (i.hasNext()) {
        State s = i.next();
        s.accept = false;
        s.addEpsilon(a.initial);
      }
      b.deterministic = false;
      b.checkMinimizeAlways();
    } catch (CloneNotSupportedException e) {
      e.printStackTrace();
    }
    return b;
  }

  /**
   * DETERMINIZATION Determinizes this Tffst.
   * <p>
   * Complexity: exponential in number of states.
   */
  public void determinize() {
    if (deterministic) return;
    Set<State> initialset = new HashSet<State>();
    initialset.add(initial);
    determinize(initialset);
  }

//  /**
//   * EPSILON REMOVAL FOR TRANSDUCERS
//   * 
//   * tffst cannot contain epsilon cycles
//   * 
//   */
//  public void domainEpsilonsRemoval() {
//
//    List<State> allLiveStates = new LinkedList<State>(getLiveStates());
//
//    while (allLiveStates.size() > 0) {
//      State workingState = allLiveStates.remove(0);
//      for (Transition outgoingT : new HashSet<Transition>(workingState.getTransitions())) {
//        if (outgoingT.labelIn.isEpsilon()) {
//          State stateToAdd = new State();
//          allLiveStates.add(stateToAdd);
//          for (Transition t : outgoingT.to.getTransitions()) {
//            Transition newT;
//            try {
//              newT = t.clone();
//              newT.labelOut = outgoingT.labelOut.clone();
//              newT.labelOut.addAll(t.labelOut.clone());
//              stateToAdd.addTransition(newT);
//            } catch (CloneNotSupportedException e) {
//              e.printStackTrace();
//            }
//          }
//          workingState.removeTransition(outgoingT);
//          workingState.addEpsilon(stateToAdd);
//        }
//      }
//    }
//  }

  /**
   * EPSILON REMOVAL FOR TRANSDUCERS 
   * 
   * cannot have epsilon loops!!!!! except for
   * epsilon/epsilon loops. the method removes epsilon-input labeled arcs. this is actually
   * changing the tffst semantics.
   * 
   * the tffst cannot recognize the empty language
   * 
   */
  public void inLabelEpsilonRemoval() {

    try {
      Set<State> workingStates = new HashSet<State>();
      Map<State,Map<Transition,State>> acceptStates = new HashMap<State,Map<Transition,State>>();
      workingStates.add(initial);
      State state;
      boolean removeState = false;

      //push all output labels of tansitions with epsilon input labels forward
      while (!workingStates.isEmpty()) {
        removeState = true;
        state = workingStates.iterator().next();

        Set<Transition> toRemove = new HashSet<Transition>();
        Set<Transition> toAdd = new HashSet<Transition>();

        for (Transition t : state.transitions) {
          //take the opportunity to build the set off final states
          Map<Transition,State> arriving = acceptStates.get(t.to);
          if (arriving == null){
            arriving =  new HashMap<Transition,State>();
            arriving.put(t, state);
            acceptStates.put(t.to, arriving);
          } else
            arriving.put(t, state);

          workingStates.add(t.to);

          if (t.labelIn.isEpsilon()) {
            toRemove.add(t);
            if (t.to.isAccept()) {
              arriving = acceptStates.get(state); // this is fine because the tffst cannot recognize the empty language
              for (Transition arriTransition : arriving.keySet()){
                State origin = arriving.get(arriTransition);
                Transition arriTransitionClone = arriTransition.clone();
                arriTransitionClone.labelOut.addAll(t.labelOut);
                arriTransitionClone.to = t.to;
                origin.addTransition(arriTransitionClone);
              }
            } else { 
              if (!t.to.equals(state)) {
                for (Transition tNext : t.to.transitions) {
                  if (!(tNext.labelIn.isEpsilon() && tNext.to.equals(t.to))) {
                    Transition tNextClone = tNext.clone();
                    for (int i = 0; i < t.labelOut.size(); i++)
                      tNextClone.labelOut.add(i, t.labelOut.get(i));
                    toAdd.add(tNextClone);
                    removeState = false;
                  }
                }
              }
            }
          }
        }
        state.transitions.removeAll(toRemove);
        state.transitions.addAll(toAdd);
        if (removeState) workingStates.remove(state);
      }


    } catch (CloneNotSupportedException e) {
      e.printStackTrace();
    }

  }

  
//  /**
//   * EPSILON REMOVAL FOR TRANSDUCERS cannot have epsilon loops!!!!! except for
//   * epsilon/epsilon loops. removes epsilon-input labeled arcs. this is actually
//   * changing the tffst semantics.
//   * 
//   */
//  public void inLabelEpsilonRemoval() {
//
//    try {
//      Set<State> workingStates = new HashSet<State>();
//      workingStates.add(initial);
//      State state;
//      boolean removeState = false;
//  
//      while (!workingStates.isEmpty()) {
//        removeState = true;
//        state = workingStates.iterator().next();
//        Set<Transition> toRemove = new HashSet<Transition>();
//        Set<Transition> toAdd = new HashSet<Transition>();
//        for (Transition t : state.transitions) {
//          workingStates.add(t.to);
//          for (Transition tNext : t.to.transitions) {
//            if (tNext.labelIn.isEpsilon() && tNext.to.isAccept() && !tNext.to.equals(t.to)) {
//              State newFinal = new State();
//              newFinal.setAccept(true);
//              Transition newT = t.clone();
//              newT.to = newFinal;
//              for (int i = 0; i < tNext.labelOut.size(); i++)
//                newT.labelOut.add(tNext.labelOut.get(i));
//              toAdd.add(newT);
//              removeState = false;
//            } else {
//              if (t.labelIn.isEpsilon()) {
//                if (t.to.isAccept()) {
//                  toRemove.add(t);
//                } else {
//                  if (t.to.equals(state)) {
//                    toRemove.add(t);
//                  } else {
//                    if (!(tNext.labelIn.isEpsilon() && tNext.to.equals(t.to))) {
//                      Transition tNextClone = tNext.clone();
//                      for (int i = 0; i < t.labelOut.size(); i++)
//                        tNextClone.labelOut.add(i, t.labelOut.get(i));
//                      toAdd.add(tNextClone);
//                      removeState = false;
//                    }
//                    toRemove.add(t);
//                  }
//                }
//              }
//            }
//          }
//        }
//        state.transitions.removeAll(toRemove);
//        state.transitions.addAll(toAdd);
//        if (removeState) workingStates.remove(state);
//      }
//    } catch (CloneNotSupportedException e) {
//      e.printStackTrace();
//    }
//
//  }
//  
  
  
  
  
  
  /**
   * returns a Tffsr representing this tffst. it expands subEpochs in simpe
   * pairts of TFs adding epsilons
   * 
   * @return
   */
  public Tffsr firstProjection() {

    Tffst simpleTffst = toSimpleTransitions(); 
    Tffsr outTffsr = new Tffsr();
    Map<State, uy.edu.fing.mina.fsa.tffsr.State> stateMap = new HashMap<State, uy.edu.fing.mina.fsa.tffsr.State>();

    for (State fstState : simpleTffst.getStates()) {
      uy.edu.fing.mina.fsa.tffsr.State fsrState = new uy.edu.fing.mina.fsa.tffsr.State();
      fsrState.setAccept(fstState.isAccept());
      if (simpleTffst.initial.equals(fstState)) 
        outTffsr.initial = fsrState;
      stateMap.put(fstState, fsrState);
    }
    
    for (State fstState : stateMap.keySet()) {
      uy.edu.fing.mina.fsa.tffsr.State fsrState = stateMap.get(fstState);
      for (Transition fstTransition :fstState.getTransitions()) {
        // can do this because  simpleTffst = toSimpleTransitions() 
        fsrState.addTransition(new uy.edu.fing.mina.fsa.tffsr.Transition(
            fstTransition.labelIn.get(0), stateMap.get(fstTransition.to)));
      }
    }
    return outTffsr;
    
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
      if (s.accept) accepts.add(s);
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

  /** Returns number of states in this Tffst. */
  public int getNumberOfStates() {
    return getStates().size();
  }

  /**
   * Returns number of transitions in this Tffst. This number is counted as the
   * total number of edges, where one edge may be a character interval.
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
   * Returns sorted array of transitions for each state (and sets state
   * numbers).
   */
  public Transition[][] getSortedTransitions() {
    Transition[][] transitions = new Transition[getStates().size()][];
    Iterator<State> i = getStates().iterator();
    while (i.hasNext()) {
      State s = i.next();
      transitions[s.getNumber()] = s.getTransitionArray();
    }
    return transitions;
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
   * KLEENE STAR Returns new Tffst that accepts the Kleene star (zero or more
   * concatenated repetitions) of the language of this Tffst.
   * <p>
   * Complexity: linear in number of states.
   * 
   */
  public Tffst kleene() {
    Tffst a = null;
    try {
      a = clone();
      State s = new State();
      s.accept = true;
      s.addEpsilon(a.initial);

      for (State p : a.getAcceptStates())
        p.addEpsilon(s);

      a.initial = s;

      a.deterministic = false;
      a.checkMinimizeAlways();
    } catch (CloneNotSupportedException e) {
      e.printStackTrace();
    }
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
   * <code>max</code> (including both) concatenated repetitions of the language
   * of this Tffst.
   * <p>
   * Complexity: linear in number of states and in <code>min</code> and
   * <code>max</code>.
   */
  public Tffst kleene(int min, int max) {
    
    Tffst a = null;
    
    try {
      if (min > max) return makeEmpty();
      max -= min;
      if (min == 0) a = makeEmptyString();
      else if (min == 1) a = (Tffst) clone();
      else {
        a = this;
        while (--min > 0)
          a = concatenate(a);
      }
      if (max == 0) return a;
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
    } catch (CloneNotSupportedException e) {
      e.printStackTrace();
    }
    return a;
  }


  /**
   * MINIMIZATION Minimizes (and determinizes if not already deterministic) this
   * Tffst.
   * 
   * @see #setMinimization(int)
   */
  public void minimize() {
    Tffsr tffsr = this.toTffsr();
    tffsr.minimize();
    Tffst tffst = tffsr.toTffst();
    assign(tffst);
    inLabelEpsilonRemoval();
  }

  /**
   * Sets deterministic flag for this Tffst. This method should (only) be used
   * if Tffsrs are constructed manually.
   * 
   * @param deterministic
   *          true if the Tffst is definitely deterministic, false if the Tffst
   *          may be nondeterministic
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

//  /**
//   * Assigns consecutive numbers to this tffst states. FIXED
//   */
//  public void setStateNumbers() {
//    Iterator<State> i = getStates().iterator();
//    int number = 0;
//    while (i.hasNext()) {
//      // System.out.println(number);
//      State s = i.next();
//      s.setNumber(number++);
//    }
//  }

  /**
   * Returns <a href="http://www.research.att.com/sw/tools/graphviz/"
   * target="_top">Graphviz Dot </a> representation of this Tffst.
   */
  public String toDot(String label) {

    StringBuffer b = new StringBuffer("digraph Tffst {\n"); //$NON-NLS-1$
    b.append("rankdir = \"LR\"\n;");
    b.append("label = \"").append(label).append("\"\n;");
    Iterator<State> i = getStates().iterator();
    while (i.hasNext()) {
      State s = i.next();
      b.append("  ").append(s.getNumber()); //$NON-NLS-1$
      if (s.accept) 
        b.append(" [shape=doublecircle,label=\"").append(s.getNumber()).append("\"];\n"); //$NON-NLS-1$
      else
        b.append(" [shape=circle,label=\"").append(s.getNumber()).append("\"];\n"); //$NON-NLS-1$
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

  /**
   * returns a Tffsr representing this tffst. it expands subEpochs in the input
   * label
   * 
   * @return
   */
  public void toSimpleInLabels() {
    HashMap<State, State> m = new HashMap<State, State>();
    Set<State> states = getStates();

    for (State s : states) {
      State p = new State();
      p.accept = s.accept;
      if (s == initial) this.initial = p;
      m.put(s, p);
    }

    for (State s : states) {
      for (Transition t : s.transitions) {
        State last = m.get(s);
        for (int k = 0; k < t.labelIn.size(); k++) {
          State to;
          if (k == t.labelIn.size() - 1) to = m.get(t.to);
          else
            to = new State();
          if (k == 0) last.transitions.add(new Transition(new TfString(t.labelIn.get(0)),
              t.labelOut, to));
          else
            last.transitions.add(new Transition(t.labelIn.get(k), SimpleTf.Epsilon(), to));
          last = to;
        }
      }
    }
  }

  /**
   * returns a Tffsr representing this tffst. it expands subEpochs in simple
   * pairs of TFs adding epsilons
   * 
   * @return
   */
  public Tffst toSimpleTransitions() { 
    
    Tffst simpleTffst = new Tffst();
    HashMap<State, State> m = new HashMap<State, State>();

    Iterator<TfI> tIniter;
    Iterator<TfI> tOutiter;
    Transition newTr;
    State last;
    Set<State> states = getStates(); 
    
    for (State s : states) {
      
      if (m.get(s) == null) {
        last = new State();
        m.put(s, last);
        last.accept = s.accept;
        if (s == initial) simpleTffst.initial = last;
      } 
      
      for (Transition t : s.transitions) {
        
        last = m.get(s);

        tIniter = t.labelIn.iterator();
        tOutiter = t.labelOut.iterator();
        //labels maintain at least a epsilon Tf inside.  
        while (tIniter.hasNext() || tOutiter.hasNext()) {
          // creates an epsilon/epsilon transition
          newTr = new Transition();

          if (tIniter.hasNext()) newTr.setLabelIn(new TfString(tIniter.next()));
          if (tOutiter.hasNext()) newTr.setLabelOut(new TfString(tOutiter.next()));

          if (!tIniter.hasNext() && !tOutiter.hasNext()){
            if (m.get(t.to) == null) {
              
              State newTo = new State();
              m.put(t.to, newTo);
              newTo.accept = t.to.accept;
              if (t.to == initial) simpleTffst.initial = newTo;

              newTr.to = newTo;
            } else {
              newTr.to = m.get(t.to);
            }
          } else {
            // creates a new intermediate state
            State ns = new State();
            m.put(ns, ns);
            newTr.to = ns;
          }
          last.transitions.add(newTr);
          last = newTr.to;
        }
      }
    }

    simpleTffst.deterministic = deterministic;
    simpleTffst.info = info;
    return simpleTffst;
  }

  /**
   * returns a Tffsr representing this tffst. it expands subEpochs in simpe
   * pairts of TFs adding epsilons
   * 
   * @return
   */
  public Tffsr toTffsr() {

    Tffst simpleTffst = toSimpleTransitions(); 
    Tffsr outTffsr = new Tffsr();
    Map<State, uy.edu.fing.mina.fsa.tffsr.State> stateMap = new HashMap<State, uy.edu.fing.mina.fsa.tffsr.State>();

    for (State fstState : simpleTffst.getStates()) {
      uy.edu.fing.mina.fsa.tffsr.State fsrState = new uy.edu.fing.mina.fsa.tffsr.State();
      fsrState.setAccept(fstState.isAccept());
      if (simpleTffst.initial.equals(fstState)) 
        outTffsr.initial = fsrState;
      stateMap.put(fstState, fsrState);
    }
    
    for (State fstState : stateMap.keySet()) {
      uy.edu.fing.mina.fsa.tffsr.State fsrState = stateMap.get(fstState);
      for (Transition fstTransition :fstState.getTransitions()) {
        // can do this because  simpleTffst = toSimpleTransitions() 
        fsrState.addTransition(new uy.edu.fing.mina.fsa.tffsr.Transition(
            new TfPair(fstTransition.labelIn.get(0),fstTransition.labelOut.get(0)), stateMap.get(fstTransition.to)));
      }
    }
    return outTffsr;
  }

  /**
   * UNION Returns new Tffst that accepts the union of the languages of this and
   * the given Tffst.
   * <p>
   * Complexity: linear in number of states.
   */
  public Tffst union(Tffst t) {
    Tffst a = null;
    try {
      a = (Tffst) t.clone();
      Tffst b = clone();
      State s = new State();
      s.addEpsilon(a.initial);
      s.addEpsilon(b.initial);
      a.initial = s;
      a.deterministic = false;
      a.checkMinimizeAlways();
    } catch (CloneNotSupportedException e) {
      e.printStackTrace();
    }
    return a;
  }

  /**
   *  Returns a string representation of this Tffst. 
   *  
   */
  public String toString() {
    StringBuffer b = new StringBuffer();
    b.append("initial state: ").append(initial.getNumber()).append("\n"); //$NON-NLS-1$ //$NON-NLS-2$
    Iterator<State> i = getStates().iterator();
    while (i.hasNext()) {
      State s = i.next();
      b.append(s.toString());
    }
    return b.toString();
  }

  
  
  /**
   * 
   * Implements the function TransP. It assumes that there is only one TF in the
   * incomming TfString
   * 
   * @param setOfPairsP
   *          is a set of PairP
   * @param se
   *          is in fact a subEpoch
   * @param transitions
   *          are all the transitions sorted by state
   * 
   * @return a set of PairP
   */
  private P unionOfTransP(P setOfelementsOfP, List<TfI> left) {

    P unionOfTransPelements = new P();
    ElementOfP retPair;

    for (TfI tf : left) {
      if (!tf.isEpsilon()) {
        for (ElementOfP pairP : setOfelementsOfP) {
          for (Transition t : pairP.state.transitions)
            if (tf.equals(t.labelIn.get(0))) { // FIXME this needs the tfs to be
                                               // simplified to have a chance of
                                               // matching
              TfString newSE = new TfString();
              for (TfI tfpairp : pairP.arrivingTFs){
                newSE.add(tfpairp);
//              newSE.add(tfpairp.and(t.labelIn.get(0)));
              }
//              newSE.add(t.labelOut.get(0).and(t.labelIn.get(0))); //TODO fix this to be more general
              newSE.add(t.labelOut.get(0)); 
              retPair = new ElementOfP(t.to, newSE);
              unionOfTransPelements.add(retPair);
            }
        }
      }
    }
    return unionOfTransPelements;
  }

  /**
   * Checks whether there is a loop containing s. (This is sufficient since
   * there are never transitions to dead states.)
   */
  private boolean isFinite(State s, HashSet<State> path) {
    path.add(s);
    Iterator<Transition> i = s.transitions.iterator();
    while (i.hasNext()) {
      Transition t = i.next();
      if (path.contains(t.to) || !isFinite(t.to, path)) return false;
    }
    path.remove(s);
    return true;
  }


  /**
   * Returns the set of TFs associated to a set of states. It supports a several
   * TFs in the labelIn but must be only one.
   * 
   * @param p
   *          A set of states (State)
   * @return
   */
  private Set<TfI> getRelevantTFs(P p) {
    Set<TfI> relevantTFs = new HashSet<TfI>();
    for (ElementOfP pp : p)
      for (Transition t : pp.state.transitions)
        for (TfI tf : t.labelIn)
          relevantTFs.add(tf);

    return relevantTFs;
  }

  /**
   * DETERMINIZATION Determinizes this Tffst using the given set of initial
   * states.
   * 
   */
  private void determinize(Set<State> initialSetOfStates) {

    // removes all epsilon labels in the domain part
    inLabelEpsilonRemoval();
    toSimpleInLabels();
    
    // stores new states
    Map<P, State> newStates = new HashMap<P, State>();

    // stores visited new states
    Map<P, State> visitedNewStates = new HashMap<P, State>();

    // creates the first newstate
    P initialP = new P();
    for (State initialState : initialSetOfStates)
      initialP.add(new ElementOfP(initialState, new TfString()));
    initial = new State();
    newStates.put(initialP, initial);
    visitedNewStates.put(initialP, initial);

    while (newStates.keySet().iterator().hasNext()) {

      // the first partition
      P p = newStates.keySet().iterator().next();
      State pNewState = newStates.remove(p);

      // if any of the old states was final, this is final
      for (ElementOfP e : p)
        if (pNewState.accept = e.state.accept) break;

      // for each possible partitions of the relevant tfs in two subsets
      for (Partition partition : Partition.getPartitions3(getRelevantTFs(p))) {
        // for each partition computes an exclusive TF
        TfI tfrelation = Partition.toTfrelation3(partition);
        if (!tfrelation.equals(SimpleTf.AcceptsNone())) {
          P transPset = unionOfTransP(p, partition.left);
          ProtoTransition protoTransition = new ProtoTransition(pNewState, tfrelation, transPset);
          protoTransition.simplifyTargetByState();
          TfString prefix = protoTransition.longestPrefix();
          
          if (!protoTransition.unionOfTransP.isEmpty()) {
            if (!visitedNewStates.keySet().contains(protoTransition.unionOfTransP)) {
              newStates.put(protoTransition.unionOfTransP, new State());
              visitedNewStates.put(protoTransition.unionOfTransP, newStates.get(protoTransition.unionOfTransP));
            }
            pNewState.transitions.add(new Transition(new TfString(tfrelation), prefix, visitedNewStates.get(protoTransition.unionOfTransP)));
          }
        }
      }
    }
    inLabelEpsilonRemoval();
    removeDeadTransitions();
    deterministic = true;
  }

//  /**
//   * Recursive method to compute all posible relations of tauterthan and
//   * astautas of a given permutation
//   * 
//   * @param permutation
//   *          each permutation is given by a ComposiiteTF tree
//   * @return a set of TfIs
//   */
//  private Set<TfI> getAllOrderedRelations(TfI permutation) {
//    Set<TfI> returnSet = new HashSet<TfI>();
//
//    if (permutation instanceof TfPair) {
//      TfPair tfpPermutation = (TfPair) permutation;
//      Set<TfI> tfpreturnSet = new HashSet<TfI>();
//      tfpreturnSet.addAll(getAllOrderedRelations(tfpPermutation.getTfIn()));
//      for (Iterator<TfI> iter = tfpreturnSet.iterator(); iter.hasNext();) {
//        TfI relation = iter.next();
//        returnSet.add(new TfPair(relation, tfpPermutation.getTfOut()));
//      }
//    } else if (permutation instanceof SimpleTf) {
//      returnSet.add(permutation);
//    } else if (permutation instanceof CompositeTf) {
//      CompositeTf cpermutation = (CompositeTf) permutation;
//      Set<TfI> left = getAllOrderedRelations(cpermutation.leftTf);
//      Set<TfI> right = getAllOrderedRelations(cpermutation.rightTf);
//      for (Iterator<TfI> iter = left.iterator(); iter.hasNext();) {
//        TfI lRelation = (TfI) iter.next();
//        for (Iterator<TfI> iterator = right.iterator(); iterator.hasNext();) {
//          TfI rRelation = (TfI) iterator.next();
//          returnSet.add(lRelation.tauterThan(rRelation));
//          returnSet.add(lRelation.asTautas(rRelation));
//        }
//      }
//    } else {
//      System.err.println("ERROR: bad permutation structure.");
//    }
//    return returnSet;
//  }

  /**
   * Removes transitions to dead states and calls {@link #reduce()}(a state is
   * "dead" if no accept state is reachable from it).
   */
  private void removeDeadTransitions() {
    Set<State> states = getStates();
    Set<State> live = getLiveStates(states);
    Iterator<State> i = states.iterator();
    while (i.hasNext()) {
      State s = i.next();
      Iterator<Transition> j = s.transitions.iterator();
      s.resetTransitions();
      while (j.hasNext()) {
        Transition t = (Transition) j.next();
        if (live.contains(t.to)) s.transitions.add(t);
      }
    }
  }
  
  private void checkMinimizeAlways() {
    if (minimize_always) minimize();
  }

  
}
