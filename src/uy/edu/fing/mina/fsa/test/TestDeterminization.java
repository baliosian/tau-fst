/*
 * Created on 12-Aug-2004
 *
 * Copyright (C) 2004 Javier Baliosian
 * All rights reserved.
 * */
package uy.edu.fing.mina.fsa.test;

import uy.edu.fing.mina.fsa.tf.SimpleTf;
import uy.edu.fing.mina.fsa.utils.Utils;
import uy.edu.fing.mina.fsa.tffst.*;
/**
 * @author Javier Baliosian &lt; <a
 *         href="mailto:jbaliosian@tsc.upc.es">jbaliosian@tsc.upc.es </a>&gt;
 */
public class TestDeterminization {

   /**
    * uy.edu.fing.mina.omega.tffst.test 5, is the very example of the Policy 2004's paper. 
    * 
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
      s4.setAccept(true);

      SimpleTf tf1 = new SimpleTf();
      tf1.setSLabel("A");

      SimpleTf tf2 = new SimpleTf();
      tf2.setSLabel("C");

      SimpleTf tf3 = new SimpleTf();
      tf3.setSLabel("D");

      SimpleTf tf4 = new SimpleTf();
      tf4.setSLabel("E");

      SimpleTf tf5 = new SimpleTf();
      tf5.setSLabel("F");

      SimpleTf tf6 = new SimpleTf();
      tf6.setSLabel("G");

      SimpleTf tf7 = new SimpleTf();
      tf7.setSLabel("I");

      SimpleTf tf8 = new SimpleTf();
      tf8.setSLabel("J");

      Transition trans1 = new Transition(tf1, tf2, s1);
      Transition trans2 = new Transition(tf1, tf3, s2);
      Transition trans3 = new Transition(tf4, tf5, s3);
      Transition trans4 = new Transition(tf6, tf5, s3);
      Transition trans5 = new Transition(tf7, tf8, s4);
      
      s0.addTransition(trans1);
      s0.addTransition(trans2);
      s1.addTransition(trans3);
      s2.addTransition(trans4);
      s3.addTransition(trans5);
      s4.addTransition(trans5);

      Utils.showDot(tffst1.toDot(""));

      tffst1.setDeterministic(false);
      tffst1.determinize(); 

      Utils.showDot(tffst1.toDot(""));

      
   }
   

}