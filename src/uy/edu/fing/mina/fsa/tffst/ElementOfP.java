/*
 * Created on 07-Aug-2004
 *
 * Copyright (C) 2004 Javier Baliosian
 * All rights reserved.
 * */
package uy.edu.fing.mina.fsa.tffst;

import java.util.HashMap;
import java.util.Map;

import uy.edu.fing.mina.fsa.tf.TfString;


/**
 * models an element of the P set. is a pair of a state and the pending TF outputs. 
 * 
 * @author Javier Baliosian &lt; <a
 *         href="mailto:jbaliosian@tsc.upc.es">jbaliosian@tsc.upc.es </a>&gt;
 */

public class ElementOfP implements Comparable<ElementOfP> {

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

   
   Map<Transition,Transition> usedTrans = new HashMap<Transition,Transition>();

   /**
    * @param state
    * @param arriving
    */
   public ElementOfP(State state, TfString arriving) {
      super();
      this.state = state;
      this.arrivingTFs = arriving;
   }

   /**
    * @param to
    * @param newSE
    * @param newMap
    */
   public ElementOfP(State to, TfString newSE, Map<Transition,Transition> newMap) {
      this(to,newSE);
      this.usedTrans = newMap;
   }

   /*
    * (non-Javadoc)
    * 
    * @see java.lang.Comparable#compareTo(java.lang.Object)
    */
   public int compareTo(ElementOfP compTo) {
      
      if (this.state.compareTo(compTo.state) == 0 ){
         return this.arrivingTFs.compareTo(compTo.arrivingTFs); 
      } else 
         return this.state.compareTo(compTo.state);
   }

   /*
    * (non-Javadoc)
    * 
    * @see java.lang.Object#equals(java.lang.Object)
    */
   public boolean equals(Object arg0) {
      if (arg0 instanceof ElementOfP) {
         ElementOfP pair = (ElementOfP) arg0;
         if (state.equals(pair.state) && arrivingTFs.equals(pair.arrivingTFs)) return true;
      }
      return false;
   }

   /*
    * (non-Javadoc)
    * 
    * @see java.lang.Object#toString()
    */
   public String toString() {
      return "(" + state.toString() + "," + arrivingTFs.toString() + ")";
   }

  @Override
  public int hashCode() {
    return state.id + arrivingTFs.hashCode();
  }
}