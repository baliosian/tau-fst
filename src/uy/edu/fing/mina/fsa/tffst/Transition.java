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

  private Double weight;
  
  public boolean visited = false;

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

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Transition)
			return labelIn.equals(((Transition) obj).labelIn) && labelOut.equals(((Transition) obj).labelOut)
					&& to.equals(((Transition) obj).to);
		else
			return false;
	}
	
	@Override
	public int hashCode() {
		return (labelIn.hashCode() + labelOut.hashCode() + to.hashCode())%Integer.MAX_VALUE;
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
      labelOut.setIdentityTf(labelIn);
      labelIn.setIdentityTf(labelOut);
    }
    labelOut.setIdentityType(identityType);    
    labelIn.setIdentityType(identityType);    
  }
  
  void appendDot(StringBuffer b) {
    b.append(" -> ").append(to.getNumber()).append(" [label=\"");
    b.append(this.toString());
    b.append("\"]\n");
  }

  /**
   * Clones this transition.
   * 
   * @return clone with same character interval and destination state
   * @throws CloneNotSupportedException 
   */
  public Transition clone() throws CloneNotSupportedException {
    return new Transition(this.labelIn.clone(), this.labelOut.clone(), to);
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
