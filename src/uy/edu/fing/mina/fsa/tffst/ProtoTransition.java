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

import uy.edu.fing.mina.fsa.tf.Operator;
import uy.edu.fing.mina.fsa.tf.TfI;
import uy.edu.fing.mina.fsa.tf.TfString;

/**
 * @author Javier Baliosian &lt; <a
 *         href="mailto:jbaliosian@tsc.upc.es">jbaliosian@tsc.upc.es </a>&gt;
 */

public class ProtoTransition {

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
  public ProtoTransition(State np, TfI relation, P union) {
    s = np;
    exclTFs = relation;
    unionOfTransP = union;
  }

  /**
   * simplifies the union of transP sets
   * 
   */
  public void simplifyTargetByState() {

    Set<ElementOfP> toRemove = new HashSet<ElementOfP>();
    int toRemoveSize;

    do {
      toRemoveSize = toRemove.size();
      for (ElementOfP workingPair : unionOfTransP)
        if (!toRemove.contains(workingPair))
          for (ElementOfP currentPair : unionOfTransP)
            if (!toRemove.contains(currentPair))
              if ((currentPair.state == workingPair.state || workingPair.state.isAccept())
                  && workingPair != currentPair) {
                TfString currentSE = currentPair.arrivingTFs;
                TfString workingSE = workingPair.arrivingTFs;
                TfString newSE = new TfString();

                Iterator<TfI> workingSEiter = workingSE.iterator();
                Iterator<TfI> currentSEiter = currentSE.iterator();
                while (currentSEiter.hasNext() || workingSEiter.hasNext()) {
                  TfI currentOinSE = null;
                  TfI workingOinSE = null;

                  if (currentSEiter.hasNext()) currentOinSE = currentSEiter.next();
                  if (workingSEiter.hasNext()) workingOinSE = workingSEiter.next();

                  if (currentOinSE != null && workingOinSE != null) newSE.add(workingOinSE.or(currentOinSE));
                  else if (currentOinSE != null) newSE.add(currentOinSE);
                  else if (workingOinSE != null) newSE.add(workingOinSE);
                }
                toRemove.add(currentPair);
                workingPair.arrivingTFs = newSE;
              }
    } while (toRemoveSize != toRemove.size());
    unionOfTransP.removeAll(toRemove);

  }

  /**
   * Returns the longest prefix in the list of PairsP. At the same time it
   * prunes the list of that prefix.
   * 
   * @return a TfString containing the longest prefix.
   */
  public TfString longestPrefix() {

    TfString outSE = new TfString();

    boolean match = true;

    if (unionOfTransP.size() != 0) {
      while (match) {
        Iterator<ElementOfP> iter = unionOfTransP.iterator();
        ElementOfP firstPair = iter.next();
        if (firstPair.arrivingTFs.isEpsilon()) match = false;
        while (iter.hasNext() && match) {
          ElementOfP pair = iter.next();
          if (pair.arrivingTFs.isEpsilon()) match = false;
          else if (!firstPair.arrivingTFs.get(0).equals(pair.arrivingTFs.get(0))) match = false;
        }
        if (match) {
          outSE.add(firstPair.arrivingTFs.get(0));
          Iterator<ElementOfP> iter2 = unionOfTransP.iterator();
          while (iter2.hasNext()) {
            ElementOfP pair2 = iter2.next();
            // TODO re-apuntar todos los refersTo apuntando a
            // pair2.arriving.get(0) a firstPair.arriving.get(0)
            pair2.arrivingTFs.remove(0);
          }
        }
      }
    }
    return outSE;
  }

  @Override
  public String toString() {
    return "(" + s + "," + exclTFs + "," + unionOfTransP + ")";
  }

}