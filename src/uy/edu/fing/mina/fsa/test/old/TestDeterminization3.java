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
public class TestDeterminization3 {

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
      State s5 = new State();
      s5.setAccept(true);

      SimpleTf tf1 = new SimpleTf();
      tf1.setName("A");

      SimpleTf tf2 = new SimpleTf();
      tf2.setName("C");

      SimpleTf tf3 = new SimpleTf();
      tf3.setName("D");

      SimpleTf tf4 = new SimpleTf();
      tf4.setName("E");

      Transition trans1 = new Transition(tf1, tf2, s4);
      Transition trans2 = new Transition(tf3, tf4, s4);
     
      s0.addOutTran(trans1);
      s0.addOutTran(trans2);

      Utils.showDot(tffst1.toDot(""));

      tffst1.setDeterministic(false);
      tffst1.determinize(); 
      Utils.showDot(tffst1.toDot(""));

      tffst1.setDeterministic(false);
      tffst1.determinize(); 
      Utils.showDot(tffst1.toDot(""));

      
   }
   

}