package uy.edu.fing.mina.fsa.tffst;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class P implements Set<ElementOfP> {
  
  Set<ElementOfP> p = new HashSet<ElementOfP>();

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

  public boolean equals(Object o) {
    boolean out = true;
    boolean in = true;

    if (o instanceof P) {
      P op = (P) o;

      if (op.size() == this.size()) {
        out = true;
        for (ElementOfP eop : p) {
          in = false;
          for (ElementOfP eoop : op) 
            if (in = eop.equals(eoop)) break;
          out = out && in;
        }
        if (out) return true;
      }
    }

    return false;
  }
  
  
  /**
   * 
   * @param pairsPSet
   * @param set
   * @return
   */
  private Set<ElementOfP> isIn(Map<Set<ElementOfP>, Set<ElementOfP>> pairsPSet, Set<ElementOfP> newstate) {

    boolean out = true;
    boolean in = true;

    // for each state s in newstate (here state is a set of pairs)
    for (Set<ElementOfP> pairsPSetKey : pairsPSet.keySet())
      if (pairsPSetKey.size() == newstate.size()) {
        out = true;
        for (ElementOfP pairInKeySet : pairsPSetKey) {
          in = false;
          for (ElementOfP pairInNS : newstate)
            in = in || pairInNS.equals(pairInKeySet);
          out = out && in;
        }
        if (out) return pairsPSetKey;
      }
    return null;
  }

  

  public int hashCode() {
    int hashcode = 0;
    for (ElementOfP eop : this) 
      hashcode += eop.hashCode();
    
    return hashcode;
  }

  public boolean isEmpty() {
    return p.isEmpty();
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
  


}
