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
 * @author Javier Baliosian &lt; <a
 *         href="mailto:jbaliosian@tsc.upc.es">jbaliosian@tsc.upc.es </a>&gt;
 */

public class PairP implements Comparable<PairP> {

   /**
    * 
    * @uml.property name="arriving"
    * @uml.associationEnd multiplicity="(0 1)"
    */
   TfString arriving;

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
   public PairP(State state, TfString arriving) {
      super();
      this.state = state;
      this.arriving = arriving;
   }

   /**
    * @param to
    * @param newSE
    * @param newMap
    */
   public PairP(State to, TfString newSE, Map<Transition,Transition> newMap) {
      this(to,newSE);
      this.usedTrans = newMap;
   }

   /*
    * (non-Javadoc)
    * 
    * @see java.lang.Comparable#compareTo(java.lang.Object)
    */
   public int compareTo(PairP compTo) {
      
      if (this.state.compareTo(compTo.state) == 0 ){
         return this.arriving.compareTo(compTo.arriving); 
      } else 
         return this.state.compareTo(compTo.state);
   }

   /*
    * (non-Javadoc)
    * 
    * @see java.lang.Object#equals(java.lang.Object)
    */
   public boolean equals(Object arg0) {
      if (arg0 instanceof PairP) {
         PairP pair = (PairP) arg0;
         if (state.equals(pair.state) && arriving.equals(pair.arriving)) return true;
      }
      return false;
   }

   /*
    * (non-Javadoc)
    * 
    * @see java.lang.Object#toString()
    */
   public String toString() {
      return state.toString() + "arriving: " + arriving.toString();
   }
}