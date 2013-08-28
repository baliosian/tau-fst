/*
 * Created on 04-Aug-2004
 *
 * Copyright (C) 2004 Javier Baliosian
 * All rights reserved.
 * */
package uy.edu.fing.mina.fsa.tffst;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.management.relation.Relation;

import uy.edu.fing.mina.fsa.tf.TfI;
import uy.edu.fing.mina.fsa.tf.TfString;

/**
 * @author Javier Baliosian &lt; <a
 *         href="mailto:jbaliosian@tsc.upc.es">jbaliosian@tsc.upc.es </a>&gt;
 */

public class ProtoT {

  /**
   * the state correstponding to the set of pairs {State, TfString} that are
   * going to be a new state in the deterministic Tffst
   * 
   * @uml.property name="s"
   * @uml.associationEnd multiplicity="(0 1)"
   */
  State s;

  /**
   * An exclusive combination of TFf
   * 
   * @uml.property name="exclTFs"
   * @uml.associationEnd multiplicity="(0 1)"
   */
  TfI exclTFs;

  /**
   * The union of pairs produced by TransP
   */
  P unionOfTransP;

  /**
   * @param np
   * @param relation
   * @param union
   */
  public ProtoT(State np, TfI relation, P union) {
    s = np;
    exclTFs = relation;
    unionOfTransP = union;
  }

  
  @Override
  public String toString() {
    return "(" + s + "," + exclTFs + "," + unionOfTransP + ")";
  }

  /* (non-Javadoc)
   * @see java.lang.Object#clone()
   */
  @Override
  protected Object clone() throws CloneNotSupportedException {
    return new ProtoT(s,  (TfI) exclTFs.clone(), (P) unionOfTransP.clone());
    
  }

  

}