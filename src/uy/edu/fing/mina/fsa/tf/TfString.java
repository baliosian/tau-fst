/*
 * Created on 30-Jul-2004
 *
 * Copyright (C) 2004 Javier Baliosian
 * All rights reserved.
 * */

package uy.edu.fing.mina.fsa.tf;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

/**
 * @author Javier Baliosian &lt; <a
 *         href="mailto:jbaliosian@tsc.upc.es">jbaliosian@tsc.upc.es </a>&gt;
 */

public class TfString implements List<TfI> {// extends Tf implements List {

  private LinkedList<TfI> listOfTfs;

  public TfString() {
    this.setEpsilon(true);
  }

  public TfString(TfI tf) {
    this();
    this.add(tf);
  }

  /**
   * @param listOfTfs
   */
  public TfString(TfString se) {
    this();
    this.addAll(se.getListOfTfs());
  }

  public TfString(List<TfI> subList) {
    this();
    this.addAll(subList);
  }

  public void add(int index, TfI element) {
	if (element != null)
	  listOfTfs.add(index, element);
  }

  /**
   * 
   * @see java.util.Collection#add(java.lang.Object)
   */
  public boolean add(TfI tf) {
	if (tf != null && !tf.isEpsilon()) 
		return listOfTfs.add(tf);
    return true;
  }

  /**
   * @see java.util.Collection#add(java.lang.Object)
   */
  public TfString addRetTFString(TfI tf) {
    if (this.add(tf)) return this;
    else
      return null;
  }

  public boolean addAll(Collection<? extends TfI> c) {
    boolean out = true;
    for (TfI tf : c) {
      out = out && this.add(tf);
    }
    return out;
  }

  /**
   * @see java.util.List#addAll(int, java.util.Collection) it must not be used!
   */
  public boolean addAll(int arg0, Collection<? extends TfI> c) {
    return listOfTfs.addAll(arg0, c);
  }

  /**
   * @see java.util.Collection#clear()
   */
  public void clear() {
    this.setEpsilon(true);
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#clone()
   */
  public TfString clone() throws CloneNotSupportedException {
    TfString tfsclone = new TfString();
    for (TfI tf : this.listOfTfs) {
      tfsclone.add((TfI) tf.clone());
    }
    return tfsclone;
  }

  /**
   * @see java.util.Collection#contains(java.lang.Object)
   */
  public boolean contains(Object arg0) {
    return listOfTfs.contains(arg0);
  }

  /**
   * @see java.util.Collection#containsAll(java.util.Collection)
   */
  public boolean containsAll(Collection<?> arg0) {
    return listOfTfs.containsAll(arg0);
  }


  public TfI get(int index) {
	if (isEpsilon()) return SimpleTf.Epsilon();
	else return this.listOfTfs.get(index);
  }

  /**
   * @return Returns the listOfTfs.
   */
  public LinkedList<TfI> getListOfTfs() {
    return listOfTfs;
  }

  /**
   * @see uy.edu.fing.mina.omega.tffst.utils.tf.TfI#getSlabel()
   */
  public String toString() {
	if (isEpsilon()) {
	  return SimpleTf.EPSILON;
	} else {
	  String seString = listOfTfs.get(0).toString();
	  for (int i = 1; i < listOfTfs.size(); i++) 
		seString += "." + listOfTfs.get(i).toString();
	  return seString;
	}
  }

  /**
   * @see java.util.List#indexOf(java.lang.Object)
   */
  public int indexOf(Object arg0) {
    return listOfTfs.indexOf(arg0);
  }

  /**
   * @see java.util.Collection#isEmpty()
   */
  public boolean isEmpty() {
    return listOfTfs.isEmpty();
  }

  public boolean isEpsilon() {
    return listOfTfs.isEmpty();
  }

  /**
   * @see java.util.Collection#iterator()
   */
  public Iterator<TfI> iterator() {
    return listOfTfs.iterator();
  }

  /**
   * @see java.util.List#lastIndexOf(java.lang.Object)
   */
  public int lastIndexOf(Object arg0) {
    return listOfTfs.lastIndexOf(arg0);
  }

  /**
   * @see java.util.List#listIterator()
   */
  public ListIterator<TfI> listIterator() {
    return listOfTfs.listIterator();
  }

  /**
   * @see java.util.List#listIterator(int)
   */
  public ListIterator<TfI> listIterator(int arg0) {
    return listOfTfs.listIterator(arg0);
  }

  /**
   * @see java.util.Collection#remove(java.lang.Object)
   * 
   *      If o isEpsilon() nothing is removed and returns false.
   * 
   */
  public boolean remove(Object o) {
    if (o instanceof TfI) {
      TfI tf = (TfI) o;
      if (tf.isEpsilon()) return false;
    }
    return listOfTfs.remove(o);
  }

  /**
   * @see java.util.List#remove(int)
   */
  public TfI remove(int arg0) {
	TfI o = null;
	o = listOfTfs.remove(arg0);
	return o;
  }

  /**
   * @see java.util.Collection#removeAll(java.util.Collection)
   */
  public boolean removeAll(Collection<?> c) {
    boolean out = true;
    for (Iterator<?> iterator = c.iterator(); iterator.hasNext();) {
      Object object = (Object) iterator.next();
      if (object instanceof TfI) {
        TfI tf = (TfI) object;
        out = out && this.remove(tf);
      } else
        return false;
    }
    return out;
  }

  /**
   * @see java.util.Collection#retainAll(java.util.Collection)
   */
  public boolean retainAll(Collection<?> arg0) {
    return listOfTfs.retainAll(arg0);
  }

  public TfI set(int index, TfI element) {
    return listOfTfs.set(index, element);
  }

  public void setEpsilon(boolean b) {
    listOfTfs = new LinkedList<TfI>();
  }

  /**
   * @param listOfTfs
   *          The listOfTfs to set.
   */
  public void setListOfTfs(LinkedList<TfI> listOfTfs) {
    this.listOfTfs = listOfTfs;
  }

  /**
   * @see java.util.Collection#size()
   */
  public int size() {
    return listOfTfs.size();
  }

  public List<TfI> subList(int fromIndex, int toIndex) {
    return listOfTfs.subList(fromIndex, toIndex);
  }

  /**
   * @see java.util.Collection#toArray()
   */
  public Object[] toArray() {
    return listOfTfs.toArray();
  }

  public <T> T[] toArray(T[] a) {
    return listOfTfs.toArray(a);
  }

  /* (non-Javadoc)
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
 
    if (isEpsilon())
      result = prime * result + 0;
    else 
      result = prime * result + ((listOfTfs == null) ? 0 : listOfTfs.hashCode());
    
    return result;
  }

  /* (non-Javadoc)
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (!(obj instanceof TfString)) return false;
    TfString other = (TfString) obj;
    if (listOfTfs == null) {
      if (other.listOfTfs != null) return false;
    } else if (!listOfTfs.equals(other.listOfTfs)) return false;
    return true;
  }

//  @Override
//  public int hashCode() {
//    int out = 0;
//    for (Iterator<TfI> iterator = listOfTfs.iterator(); iterator.hasNext();) {
//      TfI tf = (TfI) iterator.next();
//      out += tf.hashCode() % Integer.MAX_VALUE;
//    }
//    return out;
//  }

}