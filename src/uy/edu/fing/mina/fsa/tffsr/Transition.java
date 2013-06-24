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
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import uy.edu.fing.mina.fsa.tf.TfI;
import uy.edu.fing.mina.fsa.tf.TfPair;


public class Transition implements Serializable {
	static final long serialVersionUID = 40001;
	private String sLabel; //contains a string representation of the label

   /**
    * 
    * @uml.property name="label"
    * @uml.associationEnd multiplicity="(0 1)"
    */
   TfI label;

   /**
    * 
    * @uml.property name="to"
    * @uml.associationEnd multiplicity="(0 1)"
    */
   State to;


	/**
	 * Constructs new transition.
	 * 
		 */
	public Transition() {
	}

	/**
	 * Constructs new transition.
	 * 
	 * @param labelIn
	 * @param labelOut
	 * @param to
	 *            destination state
	 */
	public Transition(TfI label, State to) {
		this.to = to;
		this.label = label;
	}

	/**
	 * Clones this transition.
	 * 
	 * @return clone with same character interval and destination state
	 */
	public Object clone() {
		return new Transition(this.label, to);
	}

	/** Returns destination of this transition. */
	public State getDest() {
		return to;
	}

   /**
    * @return Returns the label.
    * 
    * @uml.property name="label"
    */
   public TfI getLabel() {
      return label;
   }

   /**
    * @return Returns the to.
    * 
    * @uml.property name="to"
    */
   public State getTo() {
      return to;
   }

   /**
    * @param label The label to set.
    * 
    * @uml.property name="label"
    */
   public void setLabel(TfI label) {
      this.label = label;
   }


	public void setSLabel() {
		this.sLabel = this.label.toString();
	}

   /**
    * @param to The to to set.
    * 
    * @uml.property name="to"
    */
   public void setTo(State to) {
      this.to = to;
   }

	
	public String toString(){
		if (sLabel == null) setSLabel();
		return sLabel;
	}

	void appendDot(StringBuffer b) {
		b.append(" -> ").append(to.getNumber()).append(" [label=\"");
		b.append(toString());
		b.append("\"]\n");
	}

   /**
    * @param to2
    * @return
    */
   public Set<uy.edu.fing.mina.fsa.tffst.Transition> toTffstTrans(uy.edu.fing.mina.fsa.tffst.State to) {
      Set<uy.edu.fing.mina.fsa.tffst.Transition> out = new HashSet<uy.edu.fing.mina.fsa.tffst.Transition>();
      Set<TfPair> tfpairs = TfPair.breakTfPairs(this.label);
      for (Iterator<TfPair> iter = tfpairs.iterator(); iter.hasNext();) {
         TfPair tfp = (TfPair) iter.next();
         uy.edu.fing.mina.fsa.tffst.Transition t = new uy.edu.fing.mina.fsa.tffst.Transition(tfp.tfIn, tfp.tfOut, to);
         t.setSLabel();
         out.add(t);
      }
      return out;
   }


}

