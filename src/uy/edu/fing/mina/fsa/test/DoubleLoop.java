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
public class DoubleLoop {

   /**
    * composition test 1  
    * 
    * @param args
    */
   public static void main(String[] args) {
      
      Tffst.setMinimizeAlways(false);

      // tffst1 :   
      //                   !D/!C                
      //                ┌───────────┐              
      //                │           │              
      //                │ ┌───────┐ │            
      //                │ │       │ │            
      //                ▼ ▼       │ │              
      //              ┌───────────────┐  D/E   ╔═══╗
      // initial  ──▶ │       0       │ ─────▶ ║ 1 ║
      //              └───────────────┘        ╚═══╝
      
      Tffst tffst1 = new Tffst();

      State s0 = new State();
      tffst1.setInitialState(s0);
      State s1 = new State();
      State s2 = new State();
      s2.setAccept(true);

      s0.addOutTran(new Transition(new SimpleTf("D"), SimpleTf.Epsilon(), s1));
      s1.addOutTran(new Transition(SimpleTf.Epsilon(), new SimpleTf("E"), s2));
      
      s0.addOutTran(new Transition((new SimpleTf("D")).not(), SimpleTf.Epsilon(), s0));
      
      Utils.showDot(tffst1.toDot("tffst1"));
      
      tffst1.setDeterministic(false);
      tffst1.determinize();     
      
      Utils.showDot(tffst1.toDot("determinized tffst1"));  
      

   }
  

   
   
}