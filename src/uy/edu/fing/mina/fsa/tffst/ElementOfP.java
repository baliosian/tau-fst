/*
 * Created on 07-Aug-2004
 *
 * Copyright (C) 2004 Javier Baliosian
 * All rights reserved.
 * */
package uy.edu.fing.mina.fsa.tffst;

import uy.edu.fing.mina.fsa.tf.TfString;


/**
 * models an element of the P set. is a pair of a state and the pending TF outputs. 
 * 
 * @author Javier Baliosian &lt; <a
 *         href="mailto:jbaliosian@tsc.upc.es">jbaliosian@tsc.upc.es </a>&gt;
 */

public class ElementOfP {

   /**
    * 
    * @uml.property name="arriving"
    * @uml.associationEnd multiplicity="(0 1)"
    */
   TfString arrivingTFs;

   /**
    * 
    * @uml.property name="state"
    * @uml.associationEnd multiplicity="(0 1)"
    */
   State state;

   
   //Map<Transition,Transition> usedTrans = new HashMap<Transition,Transition>();

   /**
    * @param state
    * @param arriving
    */
   public ElementOfP(State state, TfString arriving) {
      super();
      this.state = state;
      this.setArrivingTFs(arriving);
   }

//   /**
//    * @param to
//    * @param newSE
//    * @param newMap
//    */
//   public ElementOfP(State to, TfString newSE, Map<Transition,Transition> newMap) {
//      this(to,newSE);
//      //this.usedTrans = newMap;
//   }

//   /*
//    * (non-Javadoc)
//    * 
//    * @see java.lang.Comparable#compareTo(java.lang.Object)
//    */
//   public int compareTo(ElementOfP compTo) {
//      
//      if (this.state.compareTo(compTo.state) == 0 ){
//         return this.arrivingTFs.compareTo(compTo.arrivingTFs); 
//      } else 
//         return this.state.compareTo(compTo.state);
//   }

   /* (non-Javadoc)
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((getArrivingTFs().isEpsilon()) ? 0 : getArrivingTFs().hashCode());
    result = prime * result + ((state == null) ? 0 : state.hashCode());
    //result = prime * result + ((usedTrans == null) ? 0 : usedTrans.hashCode());
    return result;
  }

  /* (non-Javadoc)
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object obj) {
    boolean ret = true;
    if (this == obj) ret = true;
    if (obj == null) ret = false;
    if (!(obj instanceof ElementOfP)) ret = false;
    ElementOfP other = (ElementOfP) obj;
    if (getArrivingTFs() == null) {
      if (other.getArrivingTFs() != null) ret = false;
    } else if (!getArrivingTFs().equals(other.getArrivingTFs())) ret = false;
    if (state == null) {
      if (other.state != null) ret = false;
    } else if (!state.equals(other.state)) ret = false;
    return ret;
  }

   /*
    * (non-Javadoc)
    * 
    * @see java.lang.Object#toString()
    */
   public String toString() {
      return "(" + state.toString() + "," + getArrivingTFs().toString() + ")";
   }

  /* (non-Javadoc)
   * @see java.lang.Object#clone()
   */
  @Override
  protected Object clone() throws CloneNotSupportedException {
    return new ElementOfP(state, getArrivingTFs().clone());
  }

  TfString getArrivingTFs() {
    return arrivingTFs;
  }

  void setArrivingTFs(TfString arrivingTFs) {
    this.arrivingTFs = arrivingTFs;
  }

   
   
 
}