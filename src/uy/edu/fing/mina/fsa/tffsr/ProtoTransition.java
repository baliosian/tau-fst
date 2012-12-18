/*
 * Created on 04-Aug-2004
 *
 * Copyright (C) 2004 Javier Baliosian
 * All rights reserved.
 * */
package uy.edu.fing.mina.fsa.tffsr;

import java.util.Set;

import uy.edu.fing.mina.fsa.tf.TfI;


/**
 * @author Javier Baliosian &lt; <a
 *         href="mailto:jbaliosian@tsc.upc.es">jbaliosian@tsc.upc.es </a>&gt;
 */

public class ProtoTransition {

   /**
    * An exclusive combination of TFf
    * 
    * @uml.property name="exclTFs"
    * @uml.associationEnd multiplicity="(0 1)"
    */
   TfI exclTFs;


   /**
    * The union of states produced by TransD
    */
   Set<State> statesTransD;

   /**
    * the state correstponding to the set of pairs {State, TfString} that are
    * going to be a new state in the deterministic Tffst
    * 
    * @uml.property name="d"
    * @uml.associationEnd multiplicity="(0 1)"
    */
   State d;

   /**
    * @param np
    * @param relation
    * @param union
    */
   public ProtoTransition(State nd, TfI relation, Set<State> union) {
      d = nd;
      exclTFs = relation;
      statesTransD = union;
   }

}