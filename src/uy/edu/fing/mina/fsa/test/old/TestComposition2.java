/*
 * Created on 12-Aug-2004
 *
 * Copyright (C) 2004 Javier Baliosian
 * All rights reserved.
 * */
package uy.edu.fing.mina.fsa.test.old;

import uy.edu.fing.mina.fsa.tf.SimpleTf;
import uy.edu.fing.mina.fsa.tf.Tf;
import uy.edu.fing.mina.fsa.tf.TfI;
import uy.edu.fing.mina.fsa.tffsr.Tffsr;
import uy.edu.fing.mina.fsa.tffst.State;
import uy.edu.fing.mina.fsa.tffst.Tffst;
import uy.edu.fing.mina.fsa.tffst.Transition;
import uy.edu.fing.mina.fsa.utils.Utils;

/**
 * @author Javier Baliosian &lt; <a
 *         href="mailto:jbaliosian@tsc.upc.es">jbaliosian@tsc.upc.es </a>&gt;
 */
public class TestComposition2 {

   /**
    * uy.edu.fing.mina.omega.tffst.test 8. it shows determinization of a union 
    * 
    * @param args
    */
//   public static void main(String[] args) {
//      
//      TestComposition2 test = new TestComposition2();
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
////      Utils.showDot(tffst1.toDot());
//  
//      tffst1 = tffst1.toSimpleTransitions();
//      Utils.showDot(tffst1.toDot("tffst1"));
//      //tffst1
//
//      Tffst tffst2 = new Tffst();
//
//      State s02 = new State();
//      tffst2.setInitialState(s02);
//      State s12 = new State();
//      State s22 = new State();
//      s22.setAccept(true);
//
//      SimpleTf tfe = new SimpleTf();
//      tfe.setSLabel("E");
//      tfe.setEventName("e");
//      Transition trans12 = new Transition(tfe, tfe, s12, 1);
//      s02.addTransition(trans12);
//      
//      Transition trans22 = new Transition(tfc, SimpleTf.Epsilon(), s22, 1);
//      s12.addTransition(trans22);
//
//      //primer lazo 
//      Transition trans42 = new Transition(Tf.AcceptsAll(),Tf.AcceptsAll(), s02, 1);
//      s02.addTransition(trans42);
//      //segundo lazo 
//      Transition trans52 = new Transition(Tf.AcceptsAll(),Tf.AcceptsAll(), s12, 1);
//      s12.addTransition(trans52);
//      //tercer lazo
//      Transition trans62 = new Transition(Tf.AcceptsAll(),Tf.AcceptsAll(), s22, 1);
//      s22.addTransition(trans62);
//
//      tffst2.setDeterministic(false);
//      tffst2.determinize();
//
//      Utils.showDot(tffst2.toDot("Determinized tffst2"));
//
//      Tffsr tffsr2 = tffst2.firstProjection();
//      tffsr2.deterministic = false;
//      tffsr2.determinize();
//
//      Utils.showDot(tffsr2.toDot("tffsr2 - tffst2's first projection"));
//      
//      Tffsr tffsr2comp = tffsr2.complement();
//      Tffst tffst2comp = tffsr2comp.identity();
//      Utils.showDot(tffst2comp.toDot("tffst2 complement"));
//      
//      Tffst constraint = tffst2comp.union(tffst2);
//      
//      Utils.showDot(constraint.toDot("Constriant"));
//      
//      constraint.determinize();
//      Utils.showDot(constraint.toDot("Determinized Constraint"));
//
//      Tffst out = tffst1.composition(constraint);
//      Utils.showDot(out.toDot("composition"));
//
//   }
  
}