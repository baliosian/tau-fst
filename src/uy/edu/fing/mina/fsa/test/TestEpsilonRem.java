/*
 * Created on 12-Aug-2004
 *
 * Copyright (C) 2004 Javier Baliosian
 * All rights reserved.
 * */
package uy.edu.fing.mina.fsa.test;

import uy.edu.fing.mina.fsa.tf.SimpleTf;
import uy.edu.fing.mina.fsa.tf.TfI;
import uy.edu.fing.mina.fsa.tffst.State;
import uy.edu.fing.mina.fsa.tffst.Tffst;
import uy.edu.fing.mina.fsa.tffst.Transition;
import uy.edu.fing.mina.fsa.utils.Utils;

/**
 * @author Javier Baliosian &lt; <a
 *         href="mailto:jbaliosian@tsc.upc.es">jbaliosian@tsc.upc.es </a>&gt;
 */
public class TestEpsilonRem {

   /**
    * uy.edu.fing.mina.omega.tffst.test 8. it shows determinization of a union 
    * 
    * @param args
    */
//   public static void main(String[] args) {
//      
//      TestEpsilonRem test = new TestEpsilonRem();
//      Tffst.setMinimizeAlways(false);
//      
//      //tffst1
//      Tffst tffst1 = new Tffst();
//
//      State s0 = new State();
//      tffst1.setInitialState(s0);
//      State s1 = new State();
//      State s2 = new State();
//      s2.setAccept(true);
//
//      SimpleTf tfd = new SimpleTf();
//      tfd.setSLabel("D");
//      tfd.setEventName("d");
//      Transition trans1 = new Transition(tfd, tfd, s1, 1);
//      s0.addTransition(trans1);
//      
//      SimpleTf tfall = new SimpleTf();
//      tfall.setAcceptAll();
//      SimpleTf tfc = new SimpleTf();
//      tfc.setSLabel("C");
//      tfc.setEventName("c");
//      TfI tfall_c = tfall.and(tfc.not());
//      
//      Transition trans2 = new Transition(tfall_c, tfall_c, s1, 1);
//      s1.addTransition(trans2);
//      
//      SimpleTf tfk = new SimpleTf();
//      tfk.setSLabel("K");
//      tfk.setEventName("k");
//      SubEpoch sec = new SubEpoch(tfc);
//      SubEpoch seck = new SubEpoch(tfc);
//      seck.add(tfk);
//      Transition trans3 = new Transition(sec, seck, s2, 1);
//      s1.addTransition(trans3);
//  
//      tffst1 = tffst1.toSimpleTransitions();
//      Utils.showDot(tffst1.toDot("tffst1"));
//      
//      tffst1.epsilonRemoval();
//      Utils.showDot(tffst1.toDot("tffst1 without epsilons"));
//
//      tffst1.removeDeadTransitions();
//      Utils.showDot(tffst1.toDot("tffst1 without dead transitions"));
//   }
  
}