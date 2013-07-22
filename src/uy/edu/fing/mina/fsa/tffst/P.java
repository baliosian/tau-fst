package uy.edu.fing.mina.fsa.tffst;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

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
  


}
