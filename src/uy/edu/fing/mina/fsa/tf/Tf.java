/* 
 * es.upc.tffst
 * Copyright (C) 2004 Javier Baliosian
 * All rights reserved.
 *
 */

package uy.edu.fing.mina.fsa.tf;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import uy.edu.fing.mina.fsa.logics.Utils;

/**
 * Simple Condition.
 * 
 * @author Javier Baliosian &lt; <a
 *         href="mailto:jbaliosian@tsc.upc.es">jbaliosian@tsc.upc.es </a>&gt;
 */

public abstract class Tf implements TfI, Cloneable, Comparable {

  /**
   * 
   */
  private static final long serialVersionUID = 4216877592050045279L;

  protected boolean not;
  
  private int id; //TODO check with lupa team if this can be removed
  
  private TfI refersTo;
  
  private int identityType;
  
  private Set<TfI> weightTf;
  
  /** A map with all the TFs which have a label different than ""*/
  static public Map<String,TfI> createdTFs = new HashMap<String, TfI>();
  
  public Tf() {
    this(false, "");
  }

  /**
   * 
   * 
   * @param event
   * @param eventName
   * @param isEpsilon
   * @param not
   * @param label
   * @param value
   */
  public Tf(boolean not, String label) {
    super();
    this.not = not;
    this.refersTo = null;
    this.identityType = 0;
    this.weightTf = new HashSet<TfI>();
  }

  abstract public boolean acceptsAll();

  abstract public boolean acceptsNone();

  /**
   * The OR operator with no simplification, Except for the basic ones such as A
   * OR {} -> A
   * 
   * @param tf
   * @return
   * 
   */
  public TfI or(TfI tf) {
    TfI outTf;

//    if (this.acceptsAll() || tf.acceptsAll()) {
//      SimpleTf stf = new SimpleTf();
//      stf.setAcceptAll();
//      outTf = stf;
//    } else if (this.acceptsNone()) {
//      outTf = tf;
//    } else if (tf.acceptsNone()) {
//      outTf = this;
//    } else if (this.equals(tf)) {
//      outTf = this;
//    } else if (this.equals(tf.not())) {
//      SimpleTf stf = new SimpleTf();
//      stf.setAcceptAll();
//      outTf = stf;
//    } else {
//      outTf = new CompositeTf(Operator.OR, this, tf);
//    }

     outTf = new CompositeTf(Operator.OR, this, tf);

    return outTf;
  }

  
  /**
   * The OR operator with an automatic Quinn-McCluskey simplification.
   * 
   * @param tf
   * @return a simplified version of the formula.
   */

  public TfI orSimple(TfI tf) {
    TfI outTf;

    // --
    if (this.acceptsAll() || tf.acceptsAll()) {
      SimpleTf stf = new SimpleTf();
      stf.setAcceptAll();
      outTf = stf;
    } else if (this.isEpsilon()) {
      outTf = tf;
    } else if (tf.isEpsilon()) {
      outTf = this;
    } else if (this.acceptsNone()) {
      outTf = tf;
    } else if (tf.acceptsNone()) {
      outTf = this;
    } else if (this.equals(tf)) {
      outTf = this;
    } else if (this.equals(tf.not())) {
      SimpleTf stf = new SimpleTf();
      stf.setAcceptAll();
      outTf = stf;
    } else if (this instanceof SimpleTf && tf instanceof SimpleTf) {
      outTf = new CompositeTf(Operator.OR, this, tf);
    } else {
      outTf = new CompositeTf(Operator.OR, this, tf);
      outTf = Utils.simplify(outTf);
    }
    // --
    return outTf;
  }
  
  /**
   * The AND operator with no simplification, Except for the basic ones such as A AND {} -> {}
   * 
   *  
   */
  
  public TfI and(TfI tf) {
    TfI outTf;

    if (this.acceptsNone() || tf.acceptsNone()) {
      SimpleTf stf = new SimpleTf();
      stf.setAcceptNone();
      outTf = stf;
    } else if (this.acceptsAll()) {
      outTf = tf;
    } else if (tf.acceptsAll()) {
      outTf = this;
    } else if (this.equals(tf)) {
      outTf = this;
    } else if (this.equals(tf.not())) {
      SimpleTf stf = new SimpleTf();
      stf.setAcceptNone();
      outTf = stf;
    } else {
      outTf = new CompositeTf(Operator.AND, this, tf);
    }
    
    return outTf;
    
  }

  /**
   * The AND operator with an automatic Quinn-McCluskey simplification. 
   *  
   * @param tf
   * @return a simplified version of the formula. 
   */
  
  public TfI andSimple(TfI tf) {  
    TfI outTf;

    if (this.acceptsNone() || tf.acceptsNone()) {
      SimpleTf stf = new SimpleTf();
      stf.setAcceptNone();
      outTf = stf;
    } else if (this.acceptsAll()) {
      outTf = tf;
    } else if (tf.acceptsAll()) {
      outTf = this;
    } else if (this.equals(tf)) {
      outTf = this;
    } else if (this.equals(tf.not())) {
      SimpleTf stf = new SimpleTf();
      stf.setAcceptNone();
      outTf = stf;
    }else {
      outTf = new CompositeTf(Operator.AND, this, tf);
      outTf = Utils.simplify(outTf);
    }

    return outTf;
  }
  
  
  /*
   * @see java.lang.Object#clone()
   */
  public Object clone() throws CloneNotSupportedException {
    Tf tf = (Tf) super.clone();
    tf.id = this.id;
    tf.refersTo = this.refersTo;
    tf.identityType = this.identityType;
    tf.not = this.not;
    tf.refersTo = this.refersTo;
    return tf;
  }

  /**
   * @return Returns the slabel.
   */
  public String getName() {
    return isNot() ? Operator.NOT : "" ;
  }

  /**
   * @return Returns the isEpsilon.
   */
  abstract public boolean isEpsilon();

  /*
   * @see uy.edu.fing.mina.omega.tffst.utils.tf.TfI#isNot()
   */
  public boolean isNot() {
    return not;
  }

  public TfI not() {

	if (isEpsilon())
	  return SimpleTf.AcceptsAll();
	else if (acceptsAll())
	  return SimpleTf.AcceptsNone();
	else if (acceptsNone())
	  return SimpleTf.AcceptsAll();
	else

	  try {
		TfI r;
		r = (TfI) clone();
		r.setNot(!isNot());
		return r;
	  } catch (CloneNotSupportedException e) {
		e.printStackTrace();
	  }

	return null;
  }

  public abstract String toString();


public int getId() {
    return id;
}

public TfI refersTo() {
    return refersTo;
}

public void setRefersTo(TfI refersTo) {
    this.refersTo = refersTo;
}

public void setNot(boolean not) {
    this.not = not;
}

  /**
   * Returns true if the tf is satisfiable. It must be overwritten by
   * CompositeTf to make the composition work.
   * 
   */

  public boolean satisfiable() {
    return !this.equals(SimpleTf.AcceptsNone()); // TODO this MUST be
                                                 // overwritten properly in
                                                 // CompositeTf
  }

public int getIdentityType() {
  return identityType;
}

public void setIdentityType(int identityType) {
  this.identityType = identityType;
}

/* (non-Javadoc)
 * @see java.lang.Object#hashCode()
 */
@Override
public int hashCode() {
  final int prime = 31;
  int result = 1;
  result = prime * result + id;
  //result = prime * result + ((identityTf == null) ? 0 : identityTf.hashCode());
  //result = prime * result + identityType;
  result = prime * result + (not ? 1231 : 1237);
  //result = prime * result + ((refersTo == null) ? 0 : refersTo.hashCode());
  return result;
}

/* (non-Javadoc)
 * @see java.lang.Object#equals(java.lang.Object)
 */
  @Override
  public boolean equals(Object obj) {
	if (this == obj)
	  return true;
	if (obj == null)
	  return false;
	if (!(obj instanceof Tf))
	  return false;
	Tf other = (Tf) obj;
	if (id != other.id)
	  return false;
	if (not != other.not)
	  return false;
	return true;
  }

/**
 * @return the weight
 */
public Set<TfI> getWeight() {
  return weightTf;
}

/**
 * @param weight the weight to set
 */
public void addWeight(TfI weight) {
  this.weightTf.add(weight);
}

}