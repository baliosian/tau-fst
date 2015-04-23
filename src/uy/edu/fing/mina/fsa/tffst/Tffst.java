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
    s0.addOutTran(t);
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
      for (Transition t : s.getTransitions()){
        new Transition(p, t.getLabelIn().clone(), t.getLabelOut().clone(),m.get(t.getTo()));
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

    Tffst thisSimple = this.toSingleLabelTransitions();
    Tffst theOtherSimple = theOther.toSingleLabelTransitions();
    
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
			boolean go = false; 

			if ((t1.labelOut.isEpsilon() && t2.labelIn.isEpsilon())) go = true;
			if ((!t1.labelOut.isEpsilon() && !t2.labelIn.isEpsilon())) 
			  if (t1.labelOut.get(0).andSimple(t2.labelIn.get(0)).satisfiable()) go = true; 
			  
			if (go) { 
			  // get the new state that is equivalent to this pair, create it.
			  State outToState = newstates.get(new StatePair(t1.getTo(), t2.getTo()));
			  if (outToState == null) {
				outToState = new State();
				newstates.put(new StatePair(t1.getTo(), t2.getTo()), outToState);
				if (t1.getTo().equals(thisSimple.initial) && t2.getTo().equals(theOtherSimple.initial))
				  out.initial = outToState;
				outToState.setAccept(t1.getTo().isAccept() && t2.getTo().isAccept());
			  }
			  try {
				TfString lin = t1.labelIn.clone();
				TfString lout = t2.labelOut.clone();

				if (!lout.isEpsilon() && !t2.labelOut.isEpsilon() && !lin.isEpsilon()) {
				  lout.get(0).setIdentityType(t2.labelOut.get(0).getIdentityType());
				  if (lout.get(0).getIdentityType() != 0)
					lout.get(0).setRefersTo(lin.get(0));
				}
				outStartState.addOutTran(new Transition(lin, lout, outToState));

			  } catch (CloneNotSupportedException e) {
				e.printStackTrace();
			  }
			}
		  }
		}
      }
    }
    
    out.removeInputEpsilonLabel(); 
    out.removeDeadTransitions(); 
    
//   out.checkMinimizeAlways();  //TODO need this?
    //out.setDeterministic(false);
    //out.determinize();
    return out;
  }
  
  /**
   * COMPOSITION
   * b o a (i.e. b(a(x)) )   
   * 
   * Constraint: a cannot have e-input labels and b cannot have e-output labels.  
   * 
   * 
   * \[
      \begin{array}{ll}
          \Pi=\{((p_1,p_2),d,r,(q_1,q_2),i)\mid
                    & p_1, d, \tau_1, q_1, i_1) \in \Pi_1, \\
                    & (p_2, \tau_2, r, q_2, i_2) \in \Pi_2, \\
                    & \tau_1 \wedge \tau_2 \\
                    & i=0 \: if\: i_1=0 \: or\: i_2=0\: or\: i_1 \neq i_2,\\
                    & i=1 \: if\: i_1=1\: and\: i_2=1, \\
                    & i=-1 \: if\: i_1=-1\: and\: i_2=-1\}\\
          \multicolumn{2}{l}{\cup\;\{((p_1, p_2), \epsilon, d, (q_1, q_2), 0) \mid
                    (p_1,\epsilon,d',q_1,i_1)\in\Pi_1,(p_2,d',d,q_2,i_2)\in\Pi_2\}} \\
          \multicolumn{2}{l}{\cup\;\{((p_{1},p_{2}),r,\epsilon,(q_{1},q_{2}),0)\mid
                    (p_1,r,d',q_1,i_1)\in\Pi_1,(p_2,d',\epsilon,q_2,i_2)\in\Pi_2\}}
      \end{array}
      \]
   * 
   * 
   * 
   * 
   */
  
  static public Tffst composition(Tffst b, Tffst a) { 
	
    Tffst aSimple = a.toSingleLabelTransitions();
    Tffst bSimple = b.toSingleLabelTransitions();
    
    Tffst out = new Tffst();

    Set<State> aStates = aSimple.getLiveStates();
    Set<State> bStates = bSimple.getLiveStates();

    HashMap<StatePair, State> newstates = new HashMap<StatePair, State>();

    for (State aState : aStates){
      for (State bState : bStates){
        
        // get the new state that is equivalent to this pair, create it. 
        State outStartState = newstates.get(new StatePair(aState, bState));
        if (outStartState == null) {
          outStartState = new State();
          newstates.put(new StatePair(aState, bState), outStartState);
          if (aState.equals(aSimple.initial) && bState.equals(bSimple.initial))
            out.initial = outStartState;
          outStartState.setAccept(aState.isAccept() && bState.isAccept());
        }

        //add fake e/e transition loop  
        aState.addOutTran(new Transition(SimpleTf.Epsilon(),SimpleTf.Epsilon(),aState));
        bState.addOutTran(new Transition(SimpleTf.Epsilon(),SimpleTf.Epsilon(),bState));

        Set<Transition> aStateTrans = aState.getTransitions();
        Set<Transition> bStateTrans = bState.getTransitions(); 

		for (Transition at : aStateTrans) {
		  for (Transition bt : bStateTrans) {

			TfI tOutLabelIn = null;
			TfI tOutLabelOut = null;
			try {
			  // epsilon as a especial tf 
			  if (at.labelOut.isEpsilon() && bt.labelIn.isEpsilon()) {
				tOutLabelIn = (TfI) at.labelIn.get(0).clone();
				tOutLabelOut = (TfI) bt.labelOut.get(0).clone();
			  }

			  if (!at.labelOut.isEpsilon() && !bt.labelIn.isEpsilon()) {

				if (((TfI) at.labelOut.get(0).clone()).andSimple((TfI) bt.labelIn.get(0).clone()).satisfiable()) {
				  // ct((p1,π1,π1,q1,1),(p2,π2,π2,q2,1))=((p1,p2),π1∧π2,π1∧π2,(q1,q2),1)
				  if (at.labelOut.get(0).getIdentityType() == 1 && bt.labelOut.get(0).getIdentityType() == 1) {
					tOutLabelIn = ((TfI) at.labelOut.get(0).clone()).andSimple((TfI) bt.labelIn.get(0).clone());
					tOutLabelOut = (TfI) tOutLabelIn.clone();
					tOutLabelOut.setIdentityType(1);
					tOutLabelOut.setRefersTo(tOutLabelIn);
				  }
				  // ct((p1,φ,π1,q1,0),(p2,π2,π2,q2,1))=((p1,p2),φ,π1∧π2,(q1,q2),0)
				  if (at.labelOut.get(0).getIdentityType() == 0 && bt.labelOut.get(0).getIdentityType() == 1) {
					tOutLabelIn = (TfI) at.labelIn.get(0).clone();
					tOutLabelOut = ((TfI) at.labelOut.get(0).clone()).andSimple((TfI) bt.labelIn.get(0).clone());
					tOutLabelOut.setIdentityType(0);
				  }

				  // ct((p1,π1,π1,q1,1),(p2,π2,ψ,q2,0))=((p1,p2),π1∧π2,ψ,(q1,q2),0)
				  if (at.labelOut.get(0).getIdentityType() == 1 && bt.labelOut.get(0).getIdentityType() == 0) {
					tOutLabelIn = ((TfI) at.labelOut.get(0).clone()).andSimple((TfI) bt.labelIn.get(0).clone());
					tOutLabelOut = (TfI) bt.labelOut.get(0).clone();
					tOutLabelOut.setIdentityType(0);
				  }
				  // ct((p1,φ,π1,q1,0),(p2,π2,ψ,q2,0))=((p1,p2),φ,ψ,(q1,q2),0)
				  // if
				  // satisfiable(π1∧π2)
				  if (at.labelOut.get(0).getIdentityType() == 0 && bt.labelOut.get(0).getIdentityType() == 0) {
					tOutLabelIn = (TfI) at.labelIn.get(0).clone();
					tOutLabelOut = (TfI) bt.labelOut.get(0).clone();
					tOutLabelOut.setIdentityType(0);
				  }
				}
			  }
			  // {((p1,p2 ), ,ψ,(p1,q2),0)|p1 ∈ Q1,(p2,,ψ,q2,0) ∈ E2 }
			  ; // TODO

			  // {((p1,p2 ),φ, ,(q1,p2),0)|p2 ∈ Q2,(p1,φ,,q1,0) ∈ E1 }
			  ; // TODO

			  if (tOutLabelIn != null && tOutLabelOut != null) {
				// get the new state that is equivalent to this pair, create it.
				State outToState = newstates.get(new StatePair(at.getTo(), bt.getTo()));
				if (outToState == null) {
				  outToState = new State();
				  newstates.put(new StatePair(at.getTo(), bt.getTo()), outToState);
				  if (at.getTo().equals(a.initial) && bt.getTo().equals(bSimple.initial))
					out.initial = outToState;
				  outToState.setAccept(at.getTo().isAccept() && bt.getTo().isAccept());
				}
				
				outStartState.addOutTran(new Transition(tOutLabelIn, tOutLabelOut, outToState));
			  }
			  
			} catch (CloneNotSupportedException e) {
			  e.printStackTrace();
			}
		  }
		}
      }
    }
    
    out.removeInputEpsilonLabel(); 
    out.removeDeadTransitions(); 
    
    //out.checkMinimizeAlways();  //TODO need this?
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

  /**
   * EPSILON REMOVAL FOR TRANSDUCERS
   * 
   * cannot have epsilon loops!!!!! except for epsilon/epsilon loops. the method
   * removes epsilon-input labeled arcs. this is actually changing the tffst
   * semantics.
   * 
   * the tffst cannot recognize the empty language
   * 
   */
  public void removeInputEpsilonLabel() {
	
    try {
      Set<State> workingStates = new HashSet<State>();
      Set<State> visitedStates = new HashSet<State>();
      workingStates.addAll(getStates());
      State state;
      boolean removeState = false;

      // push all output labels of transitions with epsilon input labels forward
      while (!workingStates.isEmpty()) {
        removeState = true;
        state = workingStates.iterator().next();
        visitedStates.add(state);

        Set<Transition> toRemove = new HashSet<Transition>();
        Set<Transition> toAdd = new HashSet<Transition>();

        for (Transition t : state.getTransitions()) {

          if (t.labelIn.isEpsilon()) {
            toRemove.add(t);
            if (!t.getTo().equals(t.getFrom())) { // ignore in-epsilon loops
              if (state != initial) {
                for (Transition inTrans : state.getArrivingTransitions()) {
                  State origin = inTrans.getFrom();
                  if (origin != null) {
                    if (!state.equals(origin)) {
                      TfString lbout = (inTrans.labelOut.clone());
                      lbout.addAll(t.labelOut);
                      new Transition(origin, inTrans.labelIn.clone(),lbout , t.getTo());
                      workingStates.add(origin);
                    } else {
                      if (!inTrans.labelIn.isEpsilon()) {
                        State n = new State();
                        new Transition(n, inTrans.labelIn, inTrans.labelOut, n);
                        workingStates.add(n);
                        for (Transition arriTransition2 : state.getArrivingTransitions()) {
                          State origin2 = arriTransition2.getFrom();
                          if (!state.equals(origin2)) {
                            new Transition(origin2, arriTransition2.labelIn, inTrans.labelOut, n);
                            workingStates.add(origin2);
                          }
                        }
                        new Transition(n, inTrans.labelIn, t.labelOut, t.getTo());
                      }
                    }
                  }
                }
              } else {
                for (Transition tNext : t.getTo().getTransitions()) {
                  if (!(tNext.labelIn.isEpsilon() && tNext.getTo().equals(tNext.getFrom()))) {
                    TfString lbout = tNext.labelOut.clone();
                    for (int i = 0; i < t.labelOut.size(); i++)
                      lbout.add(i, t.labelOut.get(i));
                    Transition tNextClone = new Transition(null, tNext.labelIn.clone(), lbout, tNext.getTo());
                    toAdd.add(tNextClone);
                    removeState = false;
                  }
                }
              }
            }
          }
        }
        state.removeAllTransitions(toRemove);
        state.addAllTransitions(toAdd);
        if (removeState) workingStates.remove(state);
      }
    } catch (CloneNotSupportedException e) {
      e.printStackTrace();
    }
    
    removeDeadTransitions();
  }

  
  
  /**
   * returns a Tffsr representing this tffst. it expands subEpochs in simpe
   * pairts of TFs adding epsilons
   * 
   * @return
   */
  public Tffsr firstProjection() {
   
    Tffsr outTffsr = new Tffsr();
    Map<State, uy.edu.fing.mina.fsa.tffsr.State> stateMap = new HashMap<State, uy.edu.fing.mina.fsa.tffsr.State>();

    for (State fstState : getStates()) {
      uy.edu.fing.mina.fsa.tffsr.State fsrState = new uy.edu.fing.mina.fsa.tffsr.State();
      fsrState.setAccept(fstState.isAccept());
      if (initial.equals(fstState)) 
        outTffsr.initial = fsrState;
      stateMap.put(fstState, fsrState);
    }
    
    for (State fstState : stateMap.keySet()) {
      uy.edu.fing.mina.fsa.tffsr.State fsrState = stateMap.get(fstState);
      for (Transition fstTransition :fstState.getTransitions()) {
        // can do this because  simpleTffst = toSimpleTransitions() 
        fsrState.addOutTran(
        		new uy.edu.fing.mina.fsa.tffsr.Transition(
        			new TfString(fstTransition.labelIn.isEpsilon() ? SimpleTf.Epsilon() : fstTransition.labelIn.get(0)), 
        				stateMap.get(fstTransition.getTo())));
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
      Iterator<Transition> i = s.getTransitionsIterator();
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
      for (Transition t : s.getTransitions())
        map.get(t.getTo()).add(s);

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
      Iterator<Transition> i = s.getTransitionsIterator();
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
        Iterator<Transition> i = s.getTransitionsIterator();
        while (i.hasNext()) {
          Transition t = (Transition) i.next();
          worklist.add(t.getTo());
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
    return initial.accept == false && initial.getTransitions().size() == 0;
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
	toSingleLabelTransitions();
    Tffsr tffsr = toTffsr();
    tffsr.minimize();
    Tffst tffst = tffsr.toTffst();
    assign(tffst);
    removeInputEpsilonLabel();
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
     return toDot(label, "TB"); 
  }

  
  
  /**
   * Returns <a href="http://www.research.att.com/sw/tools/graphviz/"
   * target="_top">Graphviz Dot </a> representation of this Tffst.
   */
  public String toDot(String label, String orientation) {

    StringBuffer b = new StringBuffer("digraph Tffst {\n"); 
    
    b.append("graph [ bgcolor=white,fontname=Arial, fontcolor=black, fontsize=12 , "
    	+ "overlap=compress, ranksep=\"0.3\", nodesep=\"0.1\", splines=true];\n");
    b.append("node [ fontname=Arial, fontcolor=black, fontsize=10];\n");
    b.append("edge [ fontname=Helvetica, fontcolor=black, fontsize=11, arrowsize=0.6, color=grey ];\n");
    
    if (orientation.equalsIgnoreCase("LR")) b.append("rankdir = \"LR\";\n");
    
    b.append("label = \"").append(label).append("\";\n");
    
    Iterator<State> i = getStates().iterator();
    while (i.hasNext()) {
      State s = i.next();
      b.append("  ").append(s.getNumber()); 
      if (s.accept) 
        b.append(" [shape=doublecircle,label=\"").append(s.getLabel()).append("\"];\n"); 
//     
//      if (s == initial)
//        b.append(" [shape=circle, style=filled, fillcolor=grey, abel=\"").append(s.getLabel()).append("\"];\n"); 
//      
      else
        b.append(" [shape=circle,label=\"").append(s.getLabel()).append("\"];\n"); 
      if (s == initial) {
        b.append("  initial [shape=plaintext,label=\"\"];\n"); 
        b.append("  initial -> ").append(s.getNumber()).append("\n"); 
      }
      Iterator<Transition> j = s.getTransitionsIterator();
      while (j.hasNext()) {
        Transition t = j.next();
        b.append("  ").append(s.getNumber()); 
        t.appendDot(b);
      }
    }
    return b.append("}\n").toString(); 
  }

  /**
   * returns a Tffst representing this tffst but with simple input and output labels on each edge. 
   * it expands TfStrings in simple pairs of TFs adding epsilons
   * 
   * @return
   */
  public Tffst toSingleLabelTransitions() {
	
	Tffst simpleTffst = new Tffst();
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
		
		try {
		  TfString prelabelIn = t.labelIn.clone();
		  TfString prelabelOut = t.labelOut.clone();
		  State ns = last;

		  while (prelabelIn.size() > 1 || prelabelOut.size() > 1) {
			ns = new State();
			if ((prelabelIn.size() > 1 && prelabelOut.size() > 1) 
				|| (prelabelIn.size() > 1 && prelabelOut.size() == 1) 
				||  (prelabelIn.size() == 1 && prelabelOut.size() > 1))
			  new Transition(last, new TfString(prelabelIn.remove(0)), new TfString(prelabelOut.remove(0)), ns);
			else if (prelabelIn.size() > 1)
			  new Transition(last, new TfString(prelabelIn.remove(0)), prelabelOut, ns);
			else if (prelabelOut.size() > 1)
			  new Transition(last, prelabelIn, new TfString(prelabelOut.remove(0)), ns);
			last = ns;
		  }
		  new Transition(ns, prelabelIn, prelabelOut, m.get(t.getTo()));

		} catch (CloneNotSupportedException e) {
		  e.printStackTrace();
		}
	  }
	}

	simpleTffst.deterministic = deterministic;
	simpleTffst.info = info;

	return simpleTffst;
  }

  /**
   * returns a Tffst representing this tffst but with simple input labels on each edge. 
   * it expands TfStrings in simple pairs of TFs adding epsilons
   * 
   * @return
   */
  public Tffst toSingleInputLabelTransitions() {

	Tffst simpleTffst = new Tffst();
	HashMap<State, State> m = new HashMap<State, State>();

	State last;
	Set<State> states = getStates();

	for (State s : states) {
	  
	  System.out.println(s.toString());
	  
	  if (m.get(s) == null) {
		last = new State();
		m.put(s, last);
		last.accept = s.accept;
		if (s == initial)
		  simpleTffst.initial = last;
	  } else {
		last = m.get(s);
	  }
	  
	  for (Transition t : s.getTransitions()) {

		if (m.get(t.getTo()) == null) {
		  State newTo = new State();
		  m.put(t.getTo(), newTo);
		  newTo.accept = t.getTo().accept;
		  if (t.getTo() == initial)
			  simpleTffst.initial = t.getTo();
		}

		Transition lastTran = new Transition(last, new TfString(t.labelIn), new TfString(t.labelOut), m.get(t.getTo()));

		while (lastTran.getLabelIn().size() > 1 ) {
		  last = lastTran.getFrom(); 
		  State ns = new State();
		  lastTran.setFrom(ns);
		  if (lastTran.getLabelIn().size() > 1 ) {
			new Transition(last, new TfString(lastTran.getLabelIn().remove(0)), new TfString(SimpleTf.Epsilon()), ns);
		  } 
		}
	  }
	}

	return simpleTffst;
  }

  
  
  /**
   * 
   * 
   * 
   */
  public void simplifyTransitionLabels() {
	Iterator<State> i = getStates().iterator();
	while (i.hasNext()) {
	  State s = (State) i.next();
	  Set<Transition> sTransToRem = new HashSet<Transition>();
	  Iterator<Transition> j = s.getTransitions().iterator();
	  while (j.hasNext()) {
		Transition t = j.next();
		if (!t.labelIn.isEpsilon())
		  t.labelIn.set(0, uy.edu.fing.mina.fsa.logics.Utils.simplify(t.labelIn.get(0)));
		if (t.labelIn.get(0).acceptsNone())
		  sTransToRem.add(t);
		else if (!t.labelOut.isEpsilon())
		  t.labelOut.set(0, uy.edu.fing.mina.fsa.logics.Utils.simplify(t.labelOut.get(0)));
	  }
	  s.getTransitions().removeAll(sTransToRem);
	}
  }
  
  
  
  /**
   * returns a Tffsr representing this tffst. it expands subEpochs in simpe
   * pairts of TFs adding epsilons
   * 
   * @return
   */
  public Tffsr toTffsr() {

    Tffst simpleTffst = toSingleLabelTransitions(); 
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
        fsrState.addOutTran(new uy.edu.fing.mina.fsa.tffsr.Transition(new TfString(
            new TfPair(fstTransition.labelIn.isEpsilon() ? SimpleTf.Epsilon(): fstTransition.labelIn.get(0),
            		fstTransition.labelOut.isEpsilon() ? SimpleTf.Epsilon() : fstTransition.labelOut.get(0))), 
            		stateMap.get(fstTransition.getTo())));
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
      
      Utils.showDot(a.toDot("a"));
      
      Tffst b = this.clone();

      Utils.showDot(b.toDot("b"));

      
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
   * UNION Returns new Tffst that accepts the union of the languages of a and b.
   * <p>
   * Complexity: linear in number of states.
   */
  public static Tffst union(Tffst a, Tffst b) {
    Tffst out = new Tffst();
    try {
      Tffst aout = (Tffst) a.clone();
      Tffst bout = (Tffst) b.clone();
      
      State s = new State();

      s.addEpsilon(aout.initial);
      s.addEpsilon(bout.initial);
      
      out.initial = s;
      out.deterministic = false;
      out.checkMinimizeAlways();
      
    } catch (CloneNotSupportedException e) {
      e.printStackTrace();
    }
    return out;
  }

  /**
   *  Returns a string representation of this Tffst. 
   *  
   */
  public String toString() {
    StringBuffer b = new StringBuffer();
    b.append("initial state: ").append(initial.getNumber()).append("\n"); 
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
   * incomming TfString label
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
        for (ElementOfP eop : setOfelementsOfP) {
          for (Transition t : eop.state.getTransitions()) {
            if (tf.equals(t.labelIn.isEpsilon() ? SimpleTf.Epsilon(): t.labelIn.get(0))) {
              TfString newSE = new TfString();

              
              for (TfI tfpairp : eop.getArrivingTFs()) {
                  if (!tfpairp.isEpsilon() && !t.labelIn.isEpsilon()) tfpairp.addWeight(t.labelIn.get(0));
                  newSE.add(tfpairp); 
              }

			  if (!t.labelOut.isEpsilon()) {
				for (TfI tfI : t.labelOut) {
				  if (!t.labelIn.isEpsilon())
					tfI.addWeight(t.labelIn.get(0));
				  newSE.add(tfI);
				}
			  }
              
              retPair = new ElementOfP(t.getTo(), newSE);

              unionOfTransPelements.add(retPair);
            }
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
    Iterator<Transition> i = s.getTransitionsIterator();
    while (i.hasNext()) {
      Transition t = i.next();
      if (path.contains(t.getTo()) || !isFinite(t.getTo(), path)) return false;
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
      for (Transition t : pp.state.getTransitions())
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

	int g = 0;
	// removes all epsilon labels in the domain part
    removeInputEpsilonLabel();
    
//  toSimpleInLabels();
    // stores new states
    Map<P, State> newStates = new HashMap<P, State>();
    // stores visited new states
    Map<P, State> visitedNewStates = new HashMap<P, State>();
    // creates the first newstate
    P initialP = new P();
    for (State initialState : initialSetOfStates) initialP.add(new ElementOfP(initialState, new TfString()));
    initial = new State();
    newStates.put(initialP, initial);
    visitedNewStates.put(initialP, initial);

    while (newStates.keySet().iterator().hasNext()) {
      // the first partition
      P p = newStates.keySet().iterator().next();
      State pNewState = newStates.remove(p);
      
	  for (ElementOfP e : p)
		if (e.state.accept) {
		  pNewState.setAccept(true);
		  break;
		}
      
	  // for each possible partitions of the relevant tfs in two subsets
      for (Partition partition : Partition.getPartitions3(getRelevantTFs(p))) {
        // for each partition computes an exclusive TF
        TfI tfrelation = Partition.toTfrelation3(partition);
		if (!tfrelation.equals(SimpleTf.AcceptsNone())) {
		  P transPset = unionOfTransP(p, partition.left);
		  ProtoT pt = new ProtoT(pNewState, tfrelation, transPset);

		  pt.unionOfTransP.simplifyTargetByState(); 
		  
		  P lngstPosfxState = pt.unionOfTransP.longestPosfixState(visitedNewStates)[1];
		  pt.unionOfTransP.simplifyTargetByPosfixState(lngstPosfxState);

		  TfString prefix = pt.unionOfTransP.longestPrefix();

		  if (!pt.unionOfTransP.isEmpty()) {
			if (!visitedNewStates.keySet().contains(pt.unionOfTransP)) {
			  newStates.put(pt.unionOfTransP, new State());
			  visitedNewStates.put(pt.unionOfTransP, newStates.get(pt.unionOfTransP));
			}
			pNewState.addOutTran(new Transition(new TfString(tfrelation), prefix, visitedNewStates.get(pt.unionOfTransP)));
		  }
		}
      }
      //    Utils.showDot(toDot(Integer.toString(g++)));
    }

    removeInputEpsilonLabel();
    deterministic = true;
  }

  /**
   * Removes transitions to dead states and calls {@link #reduce()}(a state is
   * "dead" if no accept state is reachable from it).
   */
  public void removeDeadTransitions() {
    Set<State> states = getStates();
    Set<State> live = getLiveStates(states);
    
    for (State state : states) {
      Set<Transition> toRemove = new HashSet<Transition>();
	  for (Transition t : state.getTransitions()) {
		if (!live.contains(t.getTo()))
		  toRemove.add(t);
		if (t.labelIn.get(0).equals(t.labelOut.get(0).not()) && t.labelOut.get(0).getIdentityType() == 1 )
		  toRemove.add(t);

	  }
	  state.removeAllTransitions(toRemove);	  
	}
  }
  
  /**
   * 
   * 
   */
  
  private void checkMinimizeAlways() {
    if (minimize_always) minimize();
  }

  /**
   * Computes a total version of this tffst. The not operation on the pair labelin/labelout considers its identity part too. 
   * 
   * -(<a>/<a>) = ([a]/[a] + -a/? + a/-a)
   * 
   * @return
   */
  
  public Tffst totalize() {
	
	//TODO add identity
	
	Tffst simple = this.toSingleLabelTransitions(); 
	
	// the crash state
	State s = new State();
	//s.setAccept(true);
    s.addOutTran(new Transition(new TfString(SimpleTf.AcceptsAll()), new TfString(SimpleTf.Epsilon()), s));
    
	Iterator<State> i = simple.getStates().iterator();
    while (i.hasNext()) {
      State p = i.next();
      Set<Transition> toAdd = new HashSet<Transition>();
      
      TfI newTf = SimpleTf.AcceptsNone();
      
      Iterator<Transition> j = p.getTransitions().iterator();
      if (j.hasNext()) {
        while (j.hasNext()) {
          Transition t = j.next();
          newTf = newTf.orSimple(t.getLabelIn().get(0));
          toAdd.add(new Transition( new TfString(t.getLabelIn().get(0)),new TfString(t.getLabelOut().get(0).not()), s)); 
        }
      }
      
      if (!newTf.acceptsAll()) {
    	p.addOutTran(new Transition( new TfString(newTf.not()),new TfString(SimpleTf.AcceptsAll()), s));
      	p.addAllTransitions(toAdd);
      }
    }
    
//    simple.simplifyTransitionLabels();
//    simple.removeInputEpsilonLabel();
    
    return simple;

  }

  /**
   * Computes a total version of this tffst. The not operation on the pair labelin/labelout considers its identity part too. 
   * 
   * -(<a>/<a>) = ([a]/[a] + -a/? + a/-a)
   * 
   * @return
   */
  
  public Tffst totalizeInput() {
	
	//TODO add identity
	
	Tffst simple = this.toSingleInputLabelTransitions(); 
	
	// the crash state
	State s = new State();

    s.addOutTran(new Transition(new TfString(SimpleTf.AcceptsAll()), new TfString(SimpleTf.Epsilon()), s));
    
	Iterator<State> i = simple.getStates().iterator();
    while (i.hasNext()) {
      State p = i.next();
      Set<Transition> toAdd = new HashSet<Transition>();
      TfI newTf = SimpleTf.AcceptsNone();
      Iterator<Transition> j = p.getTransitions().iterator();
      if (j.hasNext()) {
        while (j.hasNext()) {
          Transition t = j.next();
          newTf = newTf.orSimple(t.getLabelIn().get(0));
        }
      }
      if (!newTf.equals(SimpleTf.AcceptsAll()))
    	p.addOutTran(new Transition( new TfString(newTf.not()),new TfString(SimpleTf.Epsilon()), s));
      p.addAllTransitions(toAdd);
    }
    
//    simple.simplifyTransitionLabels();
//    simple.removeInputEpsilonLabel();
    
    return simple;

  }

  
  /**
   * INTERSECTION Returns new Tffst that accepts the intersection of the
   * languages of this and the given Tffst.
   * <p>
   * Complexity: linear in number of states.
   * 
   * Description:
   * 
   * The intersection of two transducers results in a transducer that defines
   * the relation resulting from the intersection of the relations of the two
   * original transducers. It is one of the most important and powerful
   * operations on automata. As TFFSTs are not always closed under intersection,
   * it is necessary to start thinking about recognisers instead of transducers.
   * 
   * In the classical case, the intersection of two given automata $M_{1}$ and
   * $M_{2}$ is constructed by considering the cross product of states of
   * $M_{1}$ and $M_{2}$.
   * 
   * A transition $((p_1,p_2),\sigma,(q_1,q_2))$ exists in the intersection iff
   * the corresponding transition $(p_1,\sigma,q_1)$ exists in $M_1$ and
   * $(p_2,\sigma,q_2)$ exists in $M_2$. In the case of TFFSR, instead of
   * requiring that the symbol $\sigma$ occur in the corresponding transitions
   * of $M_1$ and $M_2$, the resulting tautness function must be the conjunction
   * of the corresponding tautness functions in $M_1$ and $M_2$.
   * 
   * Given two $\epsilon$-free TFFSRs $M_1=(Q_1, E, T, \Pi_1, S_1, F_1)$ and
   * $M_2=(Q_2, E, T, \Pi_2, S_2, F_2)$, the intersection $L(M_1) \cap L(M_2)$
   * is the language accepted by $M=(Q_1 \times Q_2, E, T, \Pi, S_1 \times S_2,
   * F_1 \times F_2)$ and
   * $\Pi=\{((p_1,q_1),\tau_1\wedge\tau_2,(p,q))\mid(p_1,\tau_1
   * ,p)\in\Pi_1,(q_1,\tau_2,q)\in\Pi_2\}$.
   * 
   * The transducers used in our model will always be analogous to the letter
   * transducers presented in \cite{Roche97}. Then we will be able to use this
   * intersection definition on the underlying TFFSRs, which we will define
   * below, to calculate the intersection of the transducers themselves.
   * 
   * \begin{defn} \label{def:underlying} Underlying TFFSR. If
   * $M=(Q,E,T,\Pi,S,F)$ is an TFFST, its underlying TFFSR is
   * $M'=(Q,E,T,\Pi',S,F)$ where:
   * 
   * \[\Pi'=\{(p,(x,y),q)\mid(p,x,y,p,i)\in\Pi\}\]
   * 
   * \end{defn}
   * 
   * All properties of finite state automata apply to the underlying automaton
   * of a transducer. For example, the intersection algorithm could be applied
   * to the underlying TFFSR and in our conditions, properly interpreted as the
   * intersection of two TFFSTs.
   * 
   */
  public Tffst intersection(Tffst b) {
	Tffsr afsr = this.toTffsr();
	Tffsr bfsr = b.toTffsr();	
	Tffsr ifsr = afsr.intersection(bfsr);
    return ifsr.toTffst();
  }
  
  /**
   * COMPLEMENT Returns new (deterministic) Tffsr that accepts the complement of
   * the language of this Tffsr.
   * <p>
   * Complexity: linear in number of states (if already deterministic).
   */
  public Tffst complement() {
//    a.determinize(); TODO check if is necesary to determinize 
  Tffst a = this.totalize(); 
//  Tffst a = this;
    Iterator<State> i = a.getStates().iterator();
    while (i.hasNext()) {
      State p = i.next();
      p.setAccept(!p.isAccept());
    }
    a.removeDeadTransitions();
    return a;
  }
  
  
  public void renumerateStateLabels(){
	
	int newnumber = 0;
	
    HashSet<State> visited = new HashSet<State>();
    LinkedList<State> worklist = new LinkedList<State>();
    
    worklist.add(initial);
    visited.add(initial);
    
    while (worklist.size() > 0) {
      
      State s = worklist.removeFirst();
      s.setLabel(Integer.toString(newnumber++));

      for (Transition t : s.getTransitions()) {
        if (!visited.contains(t.getTo())) {
          visited.add(t.getTo());
          worklist.add(t.getTo());
        }
      }
    }
  }

  
 
}
