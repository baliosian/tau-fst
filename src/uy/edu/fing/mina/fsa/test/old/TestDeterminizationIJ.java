/*
 * Created on 12-Aug-2004
 *
 * Copyright (C) 2004 Javier Baliosian
 * All rights reserved.
 * */
package uy.edu.fing.mina.fsa.test.old;

import uy.edu.fing.mina.fsa.tf.SimpleTf;
import uy.edu.fing.mina.fsa.utils.Utils;
import uy.edu.fing.mina.fsa.tffst.*;
/**
 * @author Javier Baliosian &lt; <a
 *         href="mailto:jbaliosian@tsc.upc.es">jbaliosian@tsc.upc.es </a>&gt;
 */
public class TestDeterminizationIJ {

   /**
    * uy.edu.fing.mina.omega.tffst.test 5, is the very example of the policy's paper. 
    * it works well but A <-> B  and B <-> A must be unified
    * 
    * @param args
    */
   
   public static void main(String[] args) {
      
      Tffst.setMinimizeAlways(false);
      
      Tffst tffst1 = new Tffst();

      State s0 = new State();
      tffst1.setInitialState(s0);

      State s4 = new State();
      s4.setAccept(true);

      SimpleTf tf1 = new SimpleTf();
      tf1.setName("A");

      SimpleTf tf2 = new SimpleTf();
      tf2.setName("C");

      SimpleTf tf7 = new SimpleTf();
      tf7.setName("I");

      SimpleTf tf8 = new SimpleTf();
      tf8.setName("J");

      Transition trans1 = new Transition(tf1, tf2, s4);
      Transition trans5 = new Transition(tf7, tf8, s4);
      
      s0.addOutTran(trans1);
      s4.addOutTran(trans5);

      Utils.showDot(tffst1.toDot(""));

      tffst1.setDeterministic(false);
      tffst1.determinize(); 

      Utils.showDot(tffst1.toDot(""));
      
      Utils.showDot(tffst1.toSingleLabelTransitions().toDot(""));
            
      
   }
   

}