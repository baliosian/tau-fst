package uy.edu.fing.mina.fsa.tffst;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class P implements Set<ElementOfP> {
  
  Set<ElementOfP> p = null;

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
    return p.equals(o);
  }

  public int hashCode() {
    return p.hashCode();
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
  
  
  

}
