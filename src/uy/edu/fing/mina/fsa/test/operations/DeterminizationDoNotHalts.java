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
public class DeterminizationDoNotHalts {

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
      s1.setAccept(true);

      s0.addOutTran(new Transition((new SimpleTf("D")).not(),(new SimpleTf("D")).not(), s0));
      s0.addOutTran(new Transition((new SimpleTf("D")).not(), new SimpleTf("D"), s1));
      
      s1.addOutTran(new Transition((new SimpleTf("D")).not(),(new SimpleTf("D")), s1));      
      
      Utils.showDot(tffst1.toDot("tffst1"));
      
      
      tffst1.setDeterministic(false);
      tffst1.determinize();      
      
      Utils.showDot(tffst1.toDot("determinized tffst1"));  
      
      

   }
  

   
   
}