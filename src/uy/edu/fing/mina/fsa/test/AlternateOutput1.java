/*
 * Created on 12-Aug-2004
 *
 * Copyright (C) 2004 Javier Baliosian
 * All rights reserved.
 * */
package uy.edu.fing.mina.fsa.test;

import uy.edu.fing.mina.fsa.tf.SimpleTf;
import uy.edu.fing.mina.fsa.tffst.State;
import uy.edu.fing.mina.fsa.tffst.Tffst;
import uy.edu.fing.mina.fsa.tffst.Transition;
import uy.edu.fing.mina.fsa.utils.Utils;

/**
 * @author Javier Baliosian &lt; <a
 *         href="mailto:jbaliosian@tsc.upc.es">jbaliosian@tsc.upc.es </a>&gt;
 */
public class AlternateOutput1 {

   /**
    * composition test 1  
    * 
    * @param args
    */
   public static void main(String[] args) {
      
      Tffst.setMinimizeAlways(false);
      
      Tffst tffst1 = new Tffst();

      State s0 = new State();
      tffst1.setInitialState(s0);
      State s1 = new State();
      State s2 = new State();
      State s3 = new State();
      State s4 = new State();
      s3.setAccept(true);
      s4.setAccept(true);

      s0.addOutTran(new Transition(new SimpleTf("A"), new SimpleTf("J"), s1));
      s0.addOutTran(new Transition(new SimpleTf("A"), new SimpleTf("K"), s2));
      s1.addOutTran(new Transition(new SimpleTf("B"), new SimpleTf("C"), s3));
      s2.addOutTran(new Transition(new SimpleTf("B"), new SimpleTf("D"), s4));
      s3.addOutTran(new Transition(new SimpleTf("B"), new SimpleTf("D"), s1));
      s4.addOutTran(new Transition(new SimpleTf("B"), new SimpleTf("C"), s2));
      
      Utils.showDot(tffst1.toDot("tffst1"));
      
      tffst1.setDeterministic(false);
      tffst1.determinize();
      
      Utils.showDot(tffst1.toDot("determinized tffst1"));

   }
  

   
   
}