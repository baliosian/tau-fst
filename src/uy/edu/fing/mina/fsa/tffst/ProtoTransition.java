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
              if ((currentPair.state.equals(workingPair.state) || workingPair.state.isAccept())
                  && workingPair != currentPair) {
                TfString currentSE = currentPair.getArrivingTFs();
                TfString workingSE = workingPair.getArrivingTFs();
                TfString newSE = new TfString();

                Iterator<TfI> workingSEiter = workingSE.iterator();
                Iterator<TfI> currentSEiter = currentSE.iterator();
                while (currentSEiter.hasNext() || workingSEiter.hasNext()) {
                  TfI currentTFinSE = null;
                  TfI workingTFinSE = null;

                  if (currentSEiter.hasNext()) currentTFinSE = currentSEiter.next();
                  if (workingSEiter.hasNext()) workingTFinSE = workingSEiter.next();

                  if (currentTFinSE != null && workingTFinSE != null && !currentTFinSE.isEpsilon() && !workingTFinSE.isEpsilon())
                    newSE.add(workingTFinSE.orSimple(currentTFinSE));
                  else if (currentTFinSE == null || currentTFinSE.isEpsilon()) newSE.add(workingTFinSE);
                  else if (workingTFinSE == null || workingTFinSE.isEpsilon()) newSE.add(currentTFinSE);
                }
                toRemove.add(currentPair);
                workingPair.setArrivingTFs(newSE);
              }
    } while (toRemoveSize != toRemove.size());
    
    unionOfTransP.removeAll(toRemove);

  }


  @Override
  public String toString() {
    return "(" + s + "," + exclTFs + "," + unionOfTransP + ")";
  }

}