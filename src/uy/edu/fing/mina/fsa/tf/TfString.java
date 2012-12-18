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
	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 1L;

	private Object consumedEvent;

	private boolean epsilon;

	private LinkedList<TfI> listOfTfs;

	public boolean not;

	public TfString() {
		this.setEpsilon(true);
		this.listOfTfs = new LinkedList<TfI>();
	}

	public TfString(TfI tf) {
		this();
		this.add(tf);
		setEpsilon(tf.isEpsilon());
	}

	@Override
	public int hashCode() {
		int out = 0;
		for (Iterator<TfI> iterator = listOfTfs.iterator(); iterator.hasNext();) {
			TfI tf = (TfI) iterator.next();
			out += tf.hashCode()%Integer.MAX_VALUE;
		}
		return out;		
	}

	/**
	 * @param listOfTfs
	 */
	public TfString(TfString se) {
		this();
		this.addAll(se.getListOfTfs());
		setEpsilon(listOfTfs.size() == 0);
	}

	public void add(int index, TfI element) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @see java.util.Collection#add(java.lang.Object)
	 */
	public boolean add(TfI tf) {
		if (!tf.isEpsilon()) {
			this.setEpsilon(false);
			return listOfTfs.add(tf);
		}
		return true;
	}

	public boolean addAll(Collection<? extends TfI> c) {
		boolean out = true;

		for (Iterator<? extends TfI> iter = c.iterator(); iter.hasNext();) {
			TfI tf =  iter.next();
			if (!tf.isEpsilon()) {
				this.setEpsilon(false);
				out = out && listOfTfs.add(tf);
			} 
		}
		return out;
	}

	
	
	/**
	 * @see java.util.List#addAll(int, java.util.Collection) it must not be
	 *      used!
	 */
	public boolean addAll(int arg0, Collection arg1) {
		System.err.println("This operation must not be used");
		return listOfTfs.addAll(arg0, arg1);
	}

	/**
	 * @see java.util.Collection#clear()
	 */
	public void clear() {
		this.setEpsilon(true);
		listOfTfs.clear();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */
	public TfString clone() {
		TfString out = new TfString(this);
		return out;
	}

	/**
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(TfString arg0) {
		int ret = 0;
		TfString se = arg0;
		Iterator<TfI> seiter = se.iterator();
		Iterator<TfI> thisiter = this.iterator();

		while (seiter.hasNext() && thisiter.hasNext()) {
			TfI ose = seiter.next();
			TfI othis = thisiter.next();
			ret = ose.compareTo(othis);
			if (ret != 0)
				return ret;
		}
		if (seiter.hasNext())
			return -1;
		else
			return 1;
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


	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o) {

		if (o instanceof TfString) {
			TfString se = (TfString) o;
			if (this.size() == se.size()) {
				Iterator<TfI> itThis = this.listOfTfs.iterator();
				for (Iterator<TfI> itThat = se.listOfTfs.iterator(); itThat.hasNext();) {
					TfI thatElem = itThat.next();
					TfI thisElem = itThis.next();
					if (!thatElem.equals(thisElem))
						return false;
				}
				return true;
			}
		} else if (size() == 1 && o instanceof TfI) {
			if (get(0).equals(o))
				return true;
		}
		return false;
	}

	
	
	public TfI get(int index) {
		return this.listOfTfs.get(index);
	}

	public Object getActualEvent() {
		return consumedEvent;
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
	public String getSLabel() {
		String seString = "";
		for (Iterator<TfI> iter = getListOfTfs().iterator(); iter.hasNext();) {
			TfI o = iter.next();
			seString += o.toString();
		}
		return seString;
	}

	public boolean in(TfI tf) {
		boolean out = false;

		if (tf instanceof TfString) {
			TfString se = (TfString) tf;

			if (this.size() == se.size()) {

				Iterator<TfI> itThis = this.listOfTfs.iterator();
				Iterator<TfI> itThat = se.listOfTfs.iterator();

				while (itThat.hasNext()) {
					//TODO Object??
					Object thatElem = itThat.next();
					Object thisElem = itThis.next();

					if (thatElem instanceof TfI && thisElem instanceof TfI) {
						TfI tfthat = (TfI) thatElem;
						TfI tfthis = (TfI) thisElem;

						if (!tfthis.in(tfthat))
							return false;

					} else if (!thisElem.equals(thatElem))
						return false;
				}

				return true;
			}
		}
		return out;
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
		return epsilon;
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
	 */
	public boolean remove(Object arg0) {
		System.err.println("This operation must not be used");
		return listOfTfs.remove(arg0);
	}

	/**
	 * @see java.util.List#remove(int)
	 */
	public TfI remove(int arg0) {
		TfI o = null;
		if (listOfTfs.size() > 0)
			o = listOfTfs.remove(arg0);

		if (listOfTfs.isEmpty())
			this.setEpsilon(true);

		return o;
	}

	/**
	 * @see java.util.Collection#removeAll(java.util.Collection)
	 */
	public boolean removeAll(Collection<?> arg0) {
		System.err.println("This operation must not be used");
		return listOfTfs.removeAll(arg0);
	}

	/**
	 * @see java.util.Collection#retainAll(java.util.Collection)
	 */
	public boolean retainAll(Collection<?> arg0) {
		return listOfTfs.retainAll(arg0);
	}

	public TfI set(int index, TfI element) {
		// TODO Auto-generated method stub
		return null;
	}


	public void setActualEvent(Object ae) {
		this.consumedEvent = ae;
	}

	public void setEpsilon(boolean b) {
		this.epsilon = b;
	}



	/**
	 * @param listOfTfs
	 *            The listOfTfs to set.
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
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see java.util.Collection#toArray()
	 */
	public Object[] toArray() {
		return listOfTfs.toArray();
	}

	public <T> T[] toArray(T[] a) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * toString
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {

		String seString = getSLabel() == null ? "" : getSLabel();
		return seString;
	}
}