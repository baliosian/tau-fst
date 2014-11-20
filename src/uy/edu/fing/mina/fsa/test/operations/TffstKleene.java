/*
 * Created on 12-Aug-2004
 *
 * Copyright (C) 2004 Javier Baliosian
 * All rights reserved.
 * */
package uy.edu.fing.mina.fsa.test.operations;

import uy.edu.fing.mina.fsa.tf.SimpleTf;
import uy.edu.fing.mina.fsa.tffst.State;
import uy.edu.fing.mina.fsa.tffst.Tffst;
import uy.edu.fing.mina.fsa.tffst.Transition;
import uy.edu.fing.mina.fsa.utils.Utils;

/**
 * @author Javier Baliosian &lt; <a
 *         href="mailto:jbaliosian@tsc.upc.es">jbaliosian@tsc.upc.es </a>&gt;
 */
public class TffstKleene {

   /**
    * uy.edu.fing.mina.omega.tffst.test 8. it shows determinization of a union 
    * 
    * @param args
    */
   public static void main(String[] args) {
      
      Tffst.setMinimizeAlways(false);
      
      Tffst tffst1 = new Tffst();

      State s0 = new State();
      tffst1.setInitialState(s0);
      State s1 = new State();
      s1.setAccept(true);

      SimpleTf tfa = new SimpleTf("A");
      
      s0.addOutTran(new Transition(tfa, tfa, s1));
      s0.addOutTran(new Transition(tfa.not(),SimpleTf.Epsilon(),s0));
      s1.addOutTran(new Transition(tfa.not(),SimpleTf.Epsilon(),s1));
 
      Tffst tffst1kleene = tffst1.kleene();
            
      Utils.showDot(tffst1.toDot("tffst1"));

      Utils.showDot(tffst1kleene.toDot("tffst1 kleene"));

   }
  
}