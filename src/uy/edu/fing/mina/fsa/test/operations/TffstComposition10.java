/*
 * Created on 12-Aug-2004
 *
 * Copyright (C) 2004 Javier Baliosian
 * All rights reserved.
 * */
package uy.edu.fing.mina.fsa.test.operations;

import uy.edu.fing.mina.fsa.tf.SimpleTf;
import uy.edu.fing.mina.fsa.tf.TfI;
import uy.edu.fing.mina.fsa.tf.TfString;
import uy.edu.fing.mina.fsa.tffsr.Tffsr;
import uy.edu.fing.mina.fsa.tffst.State;
import uy.edu.fing.mina.fsa.tffst.Tffst;
import uy.edu.fing.mina.fsa.tffst.Transition;
import uy.edu.fing.mina.fsa.utils.Utils;

/**
 * @author Javier Baliosian &lt; <a
 *         href="mailto:jbaliosian@tsc.upc.es">jbaliosian@tsc.upc.es </a>&gt;
 */
public class TffstComposition10 {

   /**
    * composition test 1  
    * 
    * @param args
    */
   public static void main(String[] args) {
      
      Tffst.setMinimizeAlways(false);
      

// tffst1 before toSimpleTransitions:   <!C>/<!C>
//                 <!D>/<!D>                    <!C>/<!C>
//               ┌───────────┐                ┌───────────┐
//               ▼           │                ▼           │
//             ┌───────────────┐  <D>/<D>   ┌───────────────┐  C/CK  ╔═══╗
//initial  ──▶ │       0       │ ─────────▶ │       1       │ ─────▶ ║ 2 ║
//             └───────────────┘            └───────────────┘        ╚═══╝

      
      Tffst tffst1 = new Tffst();

      State s0 = new State();
      tffst1.setInitialState(s0);
      State s1 = new State();
      State s2 = new State();
      s2.setAccept(true);

      s0.addOutTran(new Transition(new SimpleTf("D"), new SimpleTf("D"), s1, 1));
      s0.addOutTran(new Transition((new SimpleTf("D")).not(), (new SimpleTf("D")).not(), s0, 1));
      s1.addOutTran(new Transition((new SimpleTf("C")).not(), (new SimpleTf("C")).not(), s1, 1));
      s1.addOutTran(new Transition(new TfString(new SimpleTf("C")), (new TfString(
      new SimpleTf("C"))).addRetTFString(new SimpleTf("K")), s2));
      
      Utils.showDot(tffst1.toDot("tffst1 before toSimpleTransitions"));
      
//      tffst1 simple transitions:
//                       <!D>/<!D>                    <!C>/<!C>
//                     ┌───────────┐                ┌───────────┐
//                     ▼           │                ▼           │
//                   ┌───────────────┐  <D>/<D>   ┌───────────────┐  C/C   ┌───┐  /K   ╔═══╗
//      initial  ──▶ │       3       │ ─────────▶ │       2       │ ─────▶ │ 0 │ ────▶ ║ 1 ║
//                   └───────────────┘            └───────────────┘        └───┘       ╚═══╝
      
      tffst1 = tffst1.toSingleLabelTransitions();
      
      Utils.showDot(tffst1.toDot("tffst1 simple transitions"));

//            tffst2
//      
//                       <!E>/<!E>                    <!C>/<!C>             
//                     ┌───────────┐                ┌───────────┐           
//                     ▼           │                ▼           │           
//                   ┌───────────────┐  <E>/<E>   ┌───────────────┐  C/C  ╔═══════════════╗
//      initial  ──▶ │       20      │ ─────────▶ │       21      │ ────▶ ║       22      ║
//                   └───────────────┘            └───────────────┘       ╚═══════════════╝
      
      Tffst tffst2 = new Tffst();

      State s20 = new State();
      tffst2.setInitialState(s20);
      State s21 = new State();
      State s22 = new State();
      s22.setAccept(true);

      s20.addOutTran(new Transition(new SimpleTf("E"), new SimpleTf("E"), s21, 1));
      s20.addOutTran(new Transition((new SimpleTf("E")).not(), (new SimpleTf("E")).not(), s20, 1));
      s21.addOutTran(new Transition((new SimpleTf("C")).not(), (new SimpleTf("C")).not(), s21, 1));
      s21.addOutTran(new Transition(new SimpleTf("C"),new SimpleTf("C") , s22));
      
      Utils.showDot(tffst2.toDot("tffst2"));
      
      Tffst tffstComposition = tffst1.composition(tffst2);
      
      Utils.showDot(tffstComposition.toDot("tffst1 o tffst2"));
      

   }
  

   
   
}