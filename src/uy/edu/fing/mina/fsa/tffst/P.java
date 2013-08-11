package uy.edu.fing.mina.fsa.tffst;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import uy.edu.fing.mina.fsa.tf.TfI;
import uy.edu.fing.mina.fsa.tf.TfString;

public class P implements Set<ElementOfP> {
  
  private Set<ElementOfP> p;
  
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
    result = prime * result + ((p == null) ? 0 : p.hashCode());
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
              if ((currentPair.state.equals(workingPair.state)) && workingPair != currentPair) {
                TfString currentSE = currentPair.arrivingTFs;
                TfString workingSE = workingPair.arrivingTFs;
                TfString newSE = new TfString();

                Iterator<TfI> workingSEiter = workingSE.iterator();
                Iterator<TfI> currentSEiter = currentSE.iterator();
                while (currentSEiter.hasNext() || workingSEiter.hasNext()) {
                  TfI currentTFinSE = null;
                  TfI workingTFinSE = null;

                  if (currentSEiter.hasNext()) currentTFinSE = currentSEiter.next();
                  if (workingSEiter.hasNext()) workingTFinSE = workingSEiter.next();

                  if (currentTFinSE != null && workingTFinSE != null && !currentTFinSE.isEpsilon() && !workingTFinSE.isEpsilon())
                    newSE.add(workingTFinSE.orSimple(currentTFinSE));
                  else if (currentTFinSE == null || currentTFinSE.isEpsilon()) newSE.add(workingTFinSE);
                  else if (workingTFinSE == null || workingTFinSE.isEpsilon()) newSE.add(currentTFinSE);
                }
                toRemove.add(currentPair);
                workingPair.arrivingTFs = newSE;
              }
    } while (toRemoveSize != toRemove.size());
    
    this.removeAll(toRemove);

  }

  /**
   * computes the tail of final states. 
   * 
   */
  public void tail() {

    Set<ElementOfP> toRemove = new HashSet<ElementOfP>();
    int toRemoveSize;

    do {
      toRemoveSize = toRemove.size();
      for (ElementOfP workingPair : this)
        if (!toRemove.contains(workingPair))
          for (ElementOfP currentPair : this)
            if (!toRemove.contains(currentPair))
              if ((workingPair.state.isAccept()||currentPair.state.isAccept()) && workingPair != currentPair) {
                TfString currentSE = currentPair.arrivingTFs;
                TfString workingSE = workingPair.arrivingTFs;
                TfString newSE = new TfString();

                Iterator<TfI> workingSEiter = workingSE.iterator();
                Iterator<TfI> currentSEiter = currentSE.iterator();
                while (currentSEiter.hasNext() || workingSEiter.hasNext()) {
                  TfI currentTFinSE = null;
                  TfI workingTFinSE = null;

                  if (currentSEiter.hasNext()) currentTFinSE = currentSEiter.next();
                  if (workingSEiter.hasNext()) workingTFinSE = workingSEiter.next();

                  if (currentTFinSE != null && workingTFinSE != null && !currentTFinSE.isEpsilon() && !workingTFinSE.isEpsilon())
                    newSE.add(workingTFinSE.orSimple(currentTFinSE));
                  else if (currentTFinSE == null || currentTFinSE.isEpsilon()) newSE.add(workingTFinSE);
                  else if (workingTFinSE == null || workingTFinSE.isEpsilon()) newSE.add(currentTFinSE);
                }
                toRemove.add(currentPair);
                workingPair.arrivingTFs = newSE;
              }
    } while (toRemoveSize != toRemove.size());
    
    this.removeAll(toRemove);

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
        if (firstPair.arrivingTFs.isEpsilon()) match = false;
        while (iter.hasNext() && match) {
          ElementOfP pair = iter.next();
          if (pair.arrivingTFs.isEpsilon()) match = false;
          else 
            if (!firstPair.arrivingTFs.get(0).equals(pair.arrivingTFs.get(0))) match = false;
        }
        if (match) {
          outSE.add(firstPair.arrivingTFs.get(0));
          Iterator<ElementOfP> iter2 = this.iterator();
          while (iter2.hasNext()) {
            ElementOfP pair2 = iter2.next();
            pair2.arrivingTFs.remove(0);
          }
        }
      }
    }
    
    // create a new P because comparison between sets does not tolerates changes in their elements.
    P newP = new P(); 
    for (ElementOfP elementOfP : this) {
      newP.add(elementOfP);
    } 
    
    this.p = newP; 
    return outSE;
  }


}
