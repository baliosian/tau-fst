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
public class TffstComposition0 {

   /**
    * composition test 1  
    * 
    * @param args
    */
   public static void main(String[] args) {
      
      Tffst.setMinimizeAlways(false);
      

// tffst1 :   
//                 !D/!D                
//               ┌───────────┐              
//               ▼           │              
//             ┌───────────────┐  D/C   ╔═══╗
//initial  ──▶ │       0       │ ─────▶ ║ 1 ║
//             └───────────────┘        ╚═══╝

      
      Tffst tffst1 = new Tffst();

      State s0 = new State();
      tffst1.setInitialState(s0);
      State s1 = new State();
      s1.setAccept(true);

      s0.addOutTran(new Transition(new SimpleTf("D"), new SimpleTf("C"), s1));
      s0.addOutTran(new Transition((new SimpleTf("D")).not(), (new SimpleTf("D")).not(), s0));
      
      Utils.showDot(tffst1.toDot("tffst1"));
      

//            tffst2
//      
//                       !C/!C             
//                     ┌───────────┐           
//                     ▼           │           
//                   ┌───────────────┐  C/E  ╔═══╗
//      initial  ──▶ │       20      │ ────▶ ║21 ║
//                   └───────────────┘       ╚═══╝
      
      Tffst tffst2 = new Tffst();

      State s20 = new State();
      tffst2.setInitialState(s20);
      State s21 = new State();
      s21.setAccept(true);

      s20.addOutTran(new Transition((new SimpleTf("C")).not(), (new SimpleTf("C")).not(), s20));
      s20.addOutTran(new Transition(new SimpleTf("C"),new SimpleTf("E") , s21));
      
      Utils.showDot(tffst2.toDot("tffst2"));
      
      Tffst tffstComposition = tffst1.composition(tffst2);
      
      Utils.showDot(tffstComposition.toDot("tffst1 o tffst2"));
      
      tffstComposition.setDeterministic(false);
      tffstComposition.determinize();  
      
      Utils.showDot(tffstComposition.toDot("determinized tffst1 o tffst2"));  
      
      

   }
  

   
   
}