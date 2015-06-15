/*
 * Created on 12-Aug-2004
 *
 * Copyright (C) 2004 Javier Baliosian
 * All rights reserved.
 * */
package uy.edu.fing.mina.fsa.test.operations;

import uy.edu.fing.mina.fsa.tf.SimpleTf;
import uy.edu.fing.mina.fsa.utils.Utils;
import uy.edu.fing.mina.fsa.tffst.*;
/**
 * @author Javier Baliosian &lt; <a
 *         href="mailto:jbaliosian@tsc.upc.es">jbaliosian@tsc.upc.es </a>&gt;
 */
public class Determinization {

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
      tf1.setName("A:0.1");

      SimpleTf tf2 = new SimpleTf();
      tf2.setName("C:0.2");

      SimpleTf tf3 = new SimpleTf();
      tf3.setName("D:0.3");

      SimpleTf tf4 = new SimpleTf();
      tf4.setName("E:0.4");

      SimpleTf tf5 = new SimpleTf();
      tf5.setName("F:0.5");

      SimpleTf tf6 = new SimpleTf();
      tf6.setName("G:0.6");

      SimpleTf tf7 = new SimpleTf();
      tf7.setName("I:0.7");

      SimpleTf tf8 = new SimpleTf();
      tf8.setName("J:0.8");

      Transition trans1 = new Transition(tf1, tf2, s1);
      Transition trans2 = new Transition(tf1, tf3, s2);
      Transition trans3 = new Transition(tf4, tf5, s3);
      Transition trans4 = new Transition(tf6, tf5, s3);
      Transition trans5 = new Transition(tf7, tf8, s4);
      Transition trans6 = new Transition(tf7, tf8, s4);
      
      s0.addOutTran(trans1);
      s0.addOutTran(trans2);
      s1.addOutTran(trans3);
      s2.addOutTran(trans4);
      s3.addOutTran(trans5);
      s4.addOutTran(trans6);

      Utils.showDot(tffst1.toDot("", "LR"), "/home/javier/Downloads/predet" );

      tffst1.setDeterministic(false);
      tffst1.determinize(); 

      Utils.showDot(tffst1.toDot("", "LR"), "/home/javier/Downloads/posdet");

      
   }
   

}