/* 
 * based on 
 *
 * dk.brics.automaton
 * Copyright (C) 2001-2004 Anders Moeller
 * All rights reserved.
 *
 * es.upc.tffst
 * Copyright (C) 2004 Javier Baliosian
 * All rights reserved.
 *
 */

package uy.edu.fing.mina.fsa.tffsr;

import java.io.Serializable;

import uy.edu.fing.mina.fsa.tf.TfI;
import uy.edu.fing.mina.fsa.tf.TfString;



public class Transition implements Serializable {

  static final long serialVersionUID = 40001;

  TfString label;

  private State to = null;

  private State from = null;

  /**
   * @return the from
   */
  public State getFrom() {
    return from;
  }

  /**
   * 
   * @param from the from to set
   */
  public void setFrom(State from) {
    if (this.from != from) {
      State toBak = to;
      if (this.to != null)   this.to.remInTran(this);
      if (this.from != null) this.from.remOutTran(this);
      this.from = from;
      this.to = toBak;
      if (this.from != null) this.from.addOutTran(this);
      if (this.to != null) this.to.addInTran(this);
    }
  }
  
  State getTo() {
    return to;
  }

  void setTo(State to) {
    if (this.to != to) {
      State fromBak = from;
      if (this.to != null)   this.to.remInTran(this);
      if (this.from != null) this.from.remOutTran(this);
      this.to = to;
      this.from = fromBak;
      if (this.from != null) this.from.addOutTran(this);
      if (this.to != null) this.to.addInTran(this);
    }
  }

  private Double weight;


  /**
   * Constructs new transition.
   * 
   * @param label
   * @param to
   *          destination state
   */
	
  public Transition(TfString label, State to) {
    this(null, label, to);
  }

  /**
   * Constructs new transition.
   * 
   * @param label
   * @param to
   *          destination state
   */
	
  public Transition(TfI label, State to) {
    this(null, new TfString(label), to);
  }

  
  /**
   * Constructs new transition.
   * 
   */
    
  public Transition(State from, TfString label, State to) {
    this.label = label;
    this.setFrom(from);
    this.setTo(to);
  }


  /* (non-Javadoc)
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((from == null) ? 0 : from.hashCode());
    result = prime * result + ((label == null) ? 0 : label.hashCode());
    result = prime * result + ((getTo() == null) ? 0 : getTo().hashCode());
    result = prime * result + ((weight == null) ? 0 : weight.hashCode());
    return result;
  }

  /* (non-Javadoc)
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (!(obj instanceof Transition)) return false;
    Transition other = (Transition) obj;
    if (from == null) {
      if (other.from != null) return false;
    } else if (!from.equals(other.from)) return false;
    if (label == null) {
      if (other.label != null) return false;
    } else if (!label.equals(other.label)) return false;
    if (getTo() == null) {
      if (other.getTo() != null) return false;
    } else if (!getTo().equals(other.getTo())) return false;
    if (weight == null) {
      if (other.weight != null) return false;
    } else if (!weight.equals(other.weight)) return false;
    return true;
  }

  
  void appendDot(StringBuffer b) {
    b.append(" -> ").append(getTo().getNumber()).append(" [label=\"");
    b.append(this.toString());
    b.append("\"]\n");
  }

  void appendDot(StringBuffer b, int type) {
    if (type == 0) {
      b.append(" -> ").append(getTo().getNumber()).append(" [label=\"");
      b.append(this.toString());
      b.append("\"]\n");
    }
    if (type == 1) {
      b.append(" -> ").append(getTo().getNumber()).append(" [label=\"");
      b.append("");
      b.append("\"]\n");
    }
  }

  /**
   * Clones this transition.
   * 
   * @return clone with same character interval and destination state
   * @throws CloneNotSupportedException 
   */
  public Transition clone() throws CloneNotSupportedException {
    return new Transition(this.from, this.label.clone(), getTo());
  }

  /** Returns destination of this transition. */
  public State getDest() {
    return getTo();
  }

  /**
   * @return Returns the labelIn.
   * 
   * @uml.property name="labelIn"
   */
  public TfString getLabel() {
    return label;
  }

  /**
   * @param labelIn
   *          The labelIn to set.
   * 
   * @uml.property name="labelIn"
   */
  public void setLabel(TfString label) {
    this.label = label;
  }


  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#toString()
   */
  public String toString() {
    return label.toString();
  }

  public Double getWeight() {
    return weight;
  }

  public void setWeight(Double weight) {
    this.weight = weight;
  }

}
