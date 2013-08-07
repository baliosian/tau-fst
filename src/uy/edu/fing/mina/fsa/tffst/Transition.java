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

package uy.edu.fing.mina.fsa.tffst;

import java.io.Serializable;

import uy.edu.fing.mina.fsa.tf.SimpleTf;
import uy.edu.fing.mina.fsa.tf.TfI;
import uy.edu.fing.mina.fsa.tf.TfString;


public class Transition implements Serializable {

  static final long serialVersionUID = 40001;

  TfString labelIn;

  TfString labelOut;

  State to;

  private State from;

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
    this.from = from;
  }

  private Double weight;

  /**
   * Constructs new transition with only one tf in the output.
   * 
   * @param labelIn
   * @param labelOut
   * @param to
   *          destination state
   */
  public Transition() {
    labelIn = new TfString(SimpleTf.Epsilon()); 
    labelOut = new TfString(SimpleTf.Epsilon());
  }

  /**
   * Constructs new transition with only one tf in the output.
   * 
   * @param labelIn
   * @param labelOut
   * @param to
   *          destination state
   */
  public Transition(TfI labelIn, TfI labelOut, State to) {
    this(new TfString(labelIn), new TfString(labelOut), to);
  }

  /**
   * Constructs new transition.
   * 
   * @param labelIn
   * @param labelOut
   * @param to
   *          destination state
   */
	
  public Transition(TfString labelIn, TfString labelOut, State to) {
    this.to = to;
    this.labelIn = labelIn;
    this.labelOut = labelOut;
  }

  /**
   * Constructs new transition.
   * 
   */
    
  public Transition(State from, TfString labelIn, TfString labelOut, State to) {
    this.from = from;
    this.to = to;
    this.labelIn = labelIn;
    this.labelOut = labelOut;
  }


  /* (non-Javadoc)
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((from == null) ? 0 : from.hashCode());
    result = prime * result + ((labelIn == null) ? 0 : labelIn.hashCode());
    result = prime * result + ((labelOut == null) ? 0 : labelOut.hashCode());
    result = prime * result + ((to == null) ? 0 : to.hashCode());
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
    if (labelIn == null) {
      if (other.labelIn != null) return false;
    } else if (!labelIn.equals(other.labelIn)) return false;
    if (labelOut == null) {
      if (other.labelOut != null) return false;
    } else if (!labelOut.equals(other.labelOut)) return false;
    if (to == null) {
      if (other.to != null) return false;
    } else if (!to.equals(other.to)) return false;
    if (weight == null) {
      if (other.weight != null) return false;
    } else if (!weight.equals(other.weight)) return false;
    return true;
  }

  /**
   * Constructs new transition.
   * 
   * @param labelIn
   * @param labelOut
   * @param to
   *          destination state
   *          
   */
  
  public Transition(TfI labelIn, TfI labelOut, State to, int identityType) {
    this.to = to;
    this.labelIn = new TfString(labelIn);
    this.labelOut = new TfString(labelOut);
    if (identityType == 1 || identityType == -1 ) {
      labelOut.setRefersTo(labelIn);
      labelIn.setRefersTo(labelOut);
    }
    labelOut.setIdentityType(identityType);    
    labelIn.setIdentityType(identityType);    
  }
  
  void appendDot(StringBuffer b) {
    b.append(" -> ").append(to.getNumber()).append(" [label=\"");
    b.append(this.toString());
    b.append("\"]\n");
  }

  void appendDot(StringBuffer b, int type) {
    if (type == 0) {
      b.append(" -> ").append(to.getNumber()).append(" [label=\"");
      b.append(this.toString());
      b.append("\"]\n");
    }
    if (type == 1) {
      b.append(" -> ").append(to.getNumber()).append(" [label=\"");
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
    return new Transition(this.from, this.labelIn.clone(), this.labelOut.clone(), to);
  }

  /** Returns destination of this transition. */
  public State getDest() {
    return to;
  }

  /**
   * @return Returns the labelIn.
   * 
   * @uml.property name="labelIn"
   */
  public TfString getLabelIn() {
    return labelIn;
  }

  /**
   * @param labelIn
   *          The labelIn to set.
   * 
   * @uml.property name="labelIn"
   */
  public void setLabelIn(TfString labelIn) {
    this.labelIn = labelIn;
  }

  /**
   * @return Returns the labelOut.
   * 
   * @uml.property name="labelOut"
   */
  public TfString getLabelOut() {
    return labelOut;
  }

  /**
   * @param labelOut
   *          The labelOut to set.
   * 
   * @uml.property name="labelOut"
   */
  public void setLabelOut(TfString labelOut) {
    this.labelOut = labelOut;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#toString()
   */
  public String toString() {
    return labelIn.toString() + "/" + labelOut.toString();
  }

  public Double getWeight() {
    return weight;
  }

  public void setWeight(Double weight) {
    this.weight = weight;
  }

}
