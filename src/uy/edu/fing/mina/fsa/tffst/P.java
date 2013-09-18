package uy.edu.fing.mina.fsa.tffst;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import uy.edu.fing.mina.fsa.tf.TfI;
import uy.edu.fing.mina.fsa.tf.TfString;

public class P implements Set<ElementOfP> {
  
  private Set<ElementOfP> p = null;
  
  public P() {
    super();
    this.p = new HashSet<ElementOfP>();
  }

  public boolean add(ElementOfP e) {
    return p.add(e);
  }

  public boolean addAll(Collection<? extends ElementOfP> c) {
    return p.addAll(c);
  }

  public void clear() {
    p.clear();
  }

  public boolean contains(Object o) {
    return p.contains(o);
  }

  public boolean containsAll(Collection<?> c) {
    return p.containsAll(c);
  }

  public boolean isEmpty() {
    return p.isEmpty();
  }

  /* (non-Javadoc)
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    if ( p == null )
      result = prime * result;
    else 
      result = prime * result + p.hashCode();
    return result;
  }

  /* (non-Javadoc)
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (!(obj instanceof P)) return false;
    P other = (P) obj;

    if (p == null) {
      if (other.p != null) 
        return false;
    } else if (!p.equals(other.p)) 
      return false;
    
    return true;
  }

  public Iterator<ElementOfP> iterator() {
    return p.iterator();
  }

  public boolean remove(Object o) {
    return p.remove(o);
  }

  public boolean removeAll(Collection<?> c) {
    return p.removeAll(c);
  }

  public boolean retainAll(Collection<?> c) {
    return p.retainAll(c);
  }

  public int size() {
    return p.size();
  }

  public Object[] toArray() {
    return p.toArray();
  }

  public <T> T[] toArray(T[] a) {
    return p.toArray(a);
  }

  @Override
  public String toString() {
    
    return p.toString();
  }

  /* (non-Javadoc)
   * @see java.lang.Object#clone()
   */
  @Override
  protected Object clone() throws CloneNotSupportedException {
    P pout = new P();
    for (ElementOfP elementOfP : p) {
      pout.add((ElementOfP)elementOfP.clone());
    }
    return pout;
  }
  
  /**
   * simplifies the union of transP sets
   * 
   */
  public void simplifyTargetByState() {

	Set<ElementOfP> toRemove = new HashSet<ElementOfP>();
	int toRemoveSize;

	do {
	  toRemoveSize = toRemove.size();
	  for (ElementOfP workingPair : this)
		if (!toRemove.contains(workingPair))
		  for (ElementOfP currentPair : this)
			if (!toRemove.contains(currentPair))
//			  if ((currentPair.state.equals(workingPair.state) || currentPair.state.isAccept() || workingPair.state.isAccept()) && workingPair != currentPair) {
			  if ((currentPair.state.equals(workingPair.state)) && workingPair != currentPair) {
				TfString newSE = new TfString();

				TfString currentSE = currentPair.getArrivingTFs();
				TfString workingSE = workingPair.getArrivingTFs();

				Iterator<TfI> workingSEiter = workingSE.iterator();
				Iterator<TfI> currentSEiter = currentSE.iterator();
				while (currentSEiter.hasNext() || workingSEiter.hasNext()) {
				  TfI currentTFinSE = null;
				  TfI workingTFinSE = null;

				  if (currentSEiter.hasNext())
					currentTFinSE = currentSEiter.next();
				  if (workingSEiter.hasNext())
					workingTFinSE = workingSEiter.next();

				  if (currentTFinSE != null && workingTFinSE != null)
					newSE.add(workingTFinSE.andSimple(currentTFinSE));
				  else if (currentTFinSE == null)
					newSE.add(workingTFinSE);
				  else if (workingTFinSE == null)
					newSE.add(currentTFinSE);
				}
				toRemove.add(currentPair);
				workingPair.setArrivingTFs(newSE);
			  }
	} while (toRemoveSize != toRemove.size());

	this.removeAll(toRemove);

  }

  /**
   * computes the tail of final states.
   * 
   */
  public P tail() { // TODO merge with simplifyTargetByState and simplifyTargetByPosfixState

	P outp = new P();
	try {
	  outp = (P) this.clone();

	  Set<ElementOfP> toRemove = new HashSet<ElementOfP>();
	  int toRemoveSize;

	  do {
		toRemoveSize = toRemove.size();
		for (ElementOfP workingPair : outp)
		  if (!toRemove.contains(workingPair))
			for (ElementOfP currentPair : outp)
			  if (!toRemove.contains(currentPair))
				if (workingPair != currentPair) {
				  TfString currentSE = currentPair.getArrivingTFs();
				  TfString workingSE = workingPair.getArrivingTFs();
				  TfString newSE = new TfString();

				  Iterator<TfI> workingSEiter = workingSE.iterator();
				  Iterator<TfI> currentSEiter = currentSE.iterator();
				  while (currentSEiter.hasNext() || workingSEiter.hasNext()) {
					TfI currentTFinSE = null;
					TfI workingTFinSE = null;

					if (currentSEiter.hasNext())
					  currentTFinSE = currentSEiter.next();
					if (workingSEiter.hasNext())
					  workingTFinSE = workingSEiter.next();

					if (currentTFinSE != null && workingTFinSE != null)
					  newSE.add(workingTFinSE.andSimple(currentTFinSE));

					else if (currentTFinSE == null)
					  newSE.add(workingTFinSE);
					else if (workingTFinSE == null)
					  newSE.add(currentTFinSE);
				  }
				  toRemove.add(currentPair);
				  workingPair.setArrivingTFs(newSE);
				}
	  } while (toRemoveSize != toRemove.size());

	  outp.removeAll(toRemove);

	} catch (CloneNotSupportedException e) {
	  e.printStackTrace();
	}

	return outp;

  }

  /**
   * Returns the longest prefix in the list of PairsP. At the same time it
   * prunes the list of that prefix.
   * 
   * @return a TfString containing the longest prefix.
   */
  public TfString longestPrefix() {

	TfString outSE = new TfString();
	boolean match = true;

	if (this.size() != 0) {
	  while (match) {
		Iterator<ElementOfP> iter = this.iterator();
		ElementOfP firstPair = iter.next();
		if (firstPair.getArrivingTFs().isEpsilon())
		  match = false;
		while (iter.hasNext() && match) {
		  ElementOfP pair = iter.next();
		  if (pair.getArrivingTFs().isEpsilon())
			match = false;
		  else if (!firstPair.getArrivingTFs().get(0).equals(pair.getArrivingTFs().get(0)))
			match = false;
		}
		if (match) {
		  outSE.add(firstPair.getArrivingTFs().get(0));
		  Iterator<ElementOfP> iter2 = this.iterator();
		  while (iter2.hasNext()) {
			ElementOfP pair2 = iter2.next();
			pair2.getArrivingTFs().remove(0);
		  }
		}
	  }
	}

	// create a new P because comparison between sets does not tolerates changes
	// in their elements.
	P newP = new P();
	for (ElementOfP elementOfP : this) {
	  newP.add(elementOfP);
	}

	this.p = newP.p;
	return outSE;
  }

  public void simplifyTargetByPosfixState(P longestPosfixState) {
	P prep = new P();
	P outp = new P();
	boolean simplify = false;

	try {

	  if (!longestPosfixState.isEmpty()) {
		for (ElementOfP eovp : longestPosfixState)
		  for (ElementOfP eop : p)
			if (eop.state.equals(eovp.state)) {
			  prep.add(new ElementOfP(eop.state, new TfString(eop.arrivingTFs.subList(0, eop.arrivingTFs.size() - eovp.arrivingTFs.size()))));
			  simplify = true;
			}

	  } else {
		for (ElementOfP eop : p) {
		  prep.add(new ElementOfP(eop.state, new TfString(eop.arrivingTFs)));
		  if (eop.state.isAccept()) {
			simplify = true;
		  }
		}
	  }

	  if (simplify) {
		ElementOfP workingPair = prep.iterator().next();
		ElementOfP outWP = (ElementOfP) workingPair.clone();

		for (ElementOfP currentPair : prep) {
		  if (!currentPair.equals(workingPair)) {

			TfString newSE = new TfString();
			Iterator<TfI> wSEiter = outWP.getArrivingTFs().iterator();
			Iterator<TfI> cSEiter = currentPair.getArrivingTFs().iterator();

			while (cSEiter.hasNext() || wSEiter.hasNext()) {
			  TfI currentTFinSE = null;
			  if (cSEiter.hasNext())
				currentTFinSE = cSEiter.next();
			  TfI workingTFinSE = null;
			  if (wSEiter.hasNext())
				workingTFinSE = wSEiter.next();
			  if (currentTFinSE != null && workingTFinSE != null)
				newSE.add(workingTFinSE.andSimple(currentTFinSE));
			  else if (currentTFinSE == null)
				newSE.add(workingTFinSE);
			  else if (workingTFinSE == null)
				newSE.add(currentTFinSE);
			}
			outWP.arrivingTFs = newSE.clone();
		  }
		}

		if (!longestPosfixState.isEmpty()) {
		  for (ElementOfP eoup : prep)
			for (ElementOfP eovp : longestPosfixState)
			  if (eoup.state.equals(eovp.state)) {
				TfString atf = outWP.arrivingTFs.clone();
				atf.addAll(eovp.arrivingTFs);
				outp.add(new ElementOfP(eoup.state, atf));
			  }
		} else {
		  for (ElementOfP eoup : prep)
			outp.add(new ElementOfP(eoup.state, (TfString) outWP.arrivingTFs.clone()));
		}
		this.p = outp.p;
	  }
	} catch (CloneNotSupportedException e) {
	  e.printStackTrace();
	}
  }

  /**
   * retrieves the P state with the longest common arrivingTf tail. 
   * it assumes that this.p and p in the visited states has been simplified beforehand by simplifyTargetByState(). 
   * this means that there is no two target pair with same state.    
   * 
   * @param visitedNewStates
   * @return
   */
  
  
  P[] longestPosfixState(Map<P, State> visitedNewStates) {
    P[] candidate = new P[2];
    candidate[0] = new P();
    candidate[1] = new P();
    int candidateMaxLen = -1;

    for (P visitedP : visitedNewStates.keySet()) {
      Map<State, TfString[] > lastCandidate = new HashMap<State, TfString[]>();
      boolean vpmatch = true;
      if (visitedP.size() == p.size()) {
        int eovpMaxLen = 0;
		for (ElementOfP eop : p) {
		  boolean eovpmatch = false;
		  for (ElementOfP eovp : visitedP) {
			if (eop.state.equals(eovp.state)) {
//			  if (eovp.arrivingTFs.size() <= eop.arrivingTFs.size()) {
				if (eovp.arrivingTFs.isEpsilon()) {
				  eovpmatch = true;
				  eovpMaxLen = Math.max(eovpMaxLen, 0);
				  lastCandidate.put(eop.state, new TfString[] {new TfString(), new TfString()});
				} else if (!eop.arrivingTFs.isEpsilon()) {
				  for (int i = 1 ; 
					  (i <= eovp.arrivingTFs.size() && i <= eop.arrivingTFs.size() && 
					  eop.arrivingTFs.subList(eop.arrivingTFs.size()-i, eop.arrivingTFs.size())
					  .equals(eovp.arrivingTFs.subList(eovp.arrivingTFs.size()-i, eovp.arrivingTFs.size())));
					  i++) {
					lastCandidate.put(eop.state, new TfString[] {new TfString(eovp.arrivingTFs.subList(0, eovp.arrivingTFs.size()-i)), new TfString(eop.arrivingTFs.subList(eop.arrivingTFs.size()-i, eop.arrivingTFs.size()))});
					eovpmatch = true;
					eovpMaxLen = Math.max(eovpMaxLen, i);
				  }
				}
			//  }
			}
		  }
		  vpmatch = vpmatch && eovpmatch;
		}
        if (vpmatch && candidateMaxLen < eovpMaxLen) {
          for (State s : lastCandidate.keySet()) {
			candidate[0].add(new ElementOfP(s, lastCandidate.get(s)[0]));
			candidate[1].add(new ElementOfP(s, lastCandidate.get(s)[1]));
		  }
          candidateMaxLen = eovpMaxLen; 
        }
      }
    }

    return candidate;
  }


}
