/* 
 * es.upc.tffst
 * Copyright (C) 2004 Javier Baliosian
 * All rights reserved.
 *
 */

package uy.edu.fing.mina.fsa.tf;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import orbital.logic.imp.Formula;

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

  protected boolean not = false;

  private TfI identityTf = null;
  
  private int id; //TODO check with lupa team if this can be removed
  
  private TfI refersTo = null;
  
  protected Formula formula;
  
  private int identityType = 0;

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
    this.identityTf = null;
    this.identityType = 0;
  }

  /**
   * it sets as not a set of tfs it must be removed when tffsr.determinize would
   * be fixed
   * 
   * @param tfs
   * @return
   */
  public static Set<Tf> notS(Set<Tf> tfs) {
    Set<Tf> newtfs = new HashSet<Tf>();
    for (Iterator<Tf> iter = tfs.iterator(); iter.hasNext();) {
      Tf tf = iter.next();
      tf.setNot(!tf.isNot());
    }
    return newtfs;
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
    } else {
      outTf = new CompositeTf(Operator.OR, this, tf);
    }

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
      }else if (this instanceof SimpleTf && tf instanceof SimpleTf ) {
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
  
  public TfI and(TfI tf) { //FIXME arreglar lo de los epsilons
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
  
  public TfI andSimple(TfI tf) {  //FIXME look at the (epsilon and *) case 
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
    }else if (this instanceof SimpleTf && tf instanceof SimpleTf ) {
      outTf = new CompositeTf(Operator.AND, this, tf);
    } else {
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
    tf.formula = this.formula;
    tf.id = this.id;
    tf.identityTf = this.identityTf;
    tf.identityType = this.identityType;
    tf.not = this.not;
    tf.refersTo = this.refersTo;
    return tf;
  }

  /**
   * @return Returns the slabel.
   */
  abstract public String getName();

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
    TfI r;
    try {
      r = (TfI) clone();
      
      if (acceptsAll())
        ((SimpleTf) r).setAcceptNone();
      else if (acceptsNone())
        ((SimpleTf) r).setAcceptAll();
      else {
        r.setNot(!isNot());
        r.setFormula(getFormula().not());
      }
      
      return r;
      
    } catch (CloneNotSupportedException e) {
      e.printStackTrace();
    }
    return null;
  }

  public abstract String toString();

  public void setIdentityTf(TfI labelIn) {
    this.identityTf = labelIn;
  }

  public TfI getIdentityTf() {
    return identityTf;
  }

public int getId() {
    return id;
}

public TfI refersTo() {
    return refersTo;
}

public void setRefersTo(TfI refersTo) {
    this.refersTo = refersTo;
}

/**
 * beware, you should not use this
 * 
 */
public void setFormula(Formula formula) {
  this.formula = formula;
}

public void setNot(boolean not) {
    this.not = not;
    this.formula = getFormula().not();
}


/**
 * Returns true if the tf is satisfiable. It must be overwritten by CompositeTf to make the composition work.  
 * 
 */

public boolean satisfiable(){

  return !this.equals(SimpleTf.AcceptsNone()); //TODO this MUST be overwritten properly in CompositeTf
  
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
  result = prime * result + ((formula == null) ? 0 : formula.hashCode());
  result = prime * result + id;
  result = prime * result + ((identityTf == null) ? 0 : identityTf.hashCode());
  result = prime * result + identityType;
  result = prime * result + (not ? 1231 : 1237);
  result = prime * result + ((refersTo == null) ? 0 : refersTo.hashCode());
  return result;
}

/* (non-Javadoc)
 * @see java.lang.Object#equals(java.lang.Object)
 */
@Override
public boolean equals(Object obj) {
  if (this == obj) return true;
  if (obj == null) return false;
  if (!(obj instanceof Tf)) return false;
  Tf other = (Tf) obj;
  if (formula == null) {
    if (other.formula != null) return false;
  } else if (!formula.equals(other.formula)) return false;
  if (id != other.id) return false;
  if (identityTf == null) {
    if (other.identityTf != null) return false;
  } else if (!identityTf.equals(other.identityTf)) return false;
  if (identityType != other.identityType) return false;
  if (not != other.not) return false;
  if (refersTo == null) {
    if (other.refersTo != null) return false;
  } else if (!refersTo.equals(other.refersTo)) return false;
  return true;
}

//@Override
//public int hashCode() {
//  return getName().hashCode();
//}
//
//@Override
//public boolean equals(Object obj) {
//  
//  if (obj instanceof TfI) {
//    TfI tfin = (TfI) obj;
//    return tfin.getName().equals(this.getName());
//  }
//  return false;
//}



}