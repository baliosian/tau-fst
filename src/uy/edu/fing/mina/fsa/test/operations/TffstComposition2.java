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
public class TffstComposition2 {

   /**
    * composition test 1  
    * 
    * @param args
    */
   public static void main(String[] args) {
      
      Tffst.setMinimizeAlways(false);

// tffst1 :                                                          
//               ┌───┐  A/A    ┌───┐  B/e   ┌───┐  C/e   ┌───┐  D/D  ╔═══╗
//  initial  ──▶ │ 0 │ ─────▶  │ 1 │ ─────▶ │ 2 │ ─────▶ │ 3 │ ─────▶║ 4 ║
//               └───┘         └───┘        └───┘        └───┘       ╚═══╝
      
      Tffst tffst1 = new Tffst();

      State s0 = new State();
      tffst1.setInitialState(s0);
      State s1 = new State();
      State s2 = new State();
      State s3 = new State();
      State s4 = new State();
      s4.setAccept(true);

      s0.addOutTran(new Transition(new SimpleTf("A"), new SimpleTf("A"), s1));
      s1.addOutTran(new Transition(new SimpleTf("B"), SimpleTf.Epsilon(), s2));
      s2.addOutTran(new Transition(new SimpleTf("C"), SimpleTf.Epsilon(), s3));
      s3.addOutTran(new Transition(new SimpleTf("D"), new SimpleTf("D"), s4));
      
      Utils.showDot(tffst1.toDot("tffst1"));

// tffst1 :                                                          
//               ┌───┐  A/D    ┌───┐  e/E   ┌───┐  D/A  ╔═══╗
//  initial  ──▶ │ 0 │ ─────▶  │ 1 │ ─────▶ │ 2 │ ─────▶║ 3 ║
//               └───┘         └───┘        └───┘       ╚═══╝


  Tffst tffst2 = new Tffst();
  
  State s20 = new State();
  tffst2.setInitialState(s20);
  State s21 = new State();
  State s22 = new State();
  State s23 = new State();
  s23.setAccept(true);

  s20.addOutTran(new Transition(new SimpleTf("A"), new SimpleTf("D"), s21));
  s21.addOutTran(new Transition(SimpleTf.Epsilon(), new SimpleTf("E"), s22));
  s22.addOutTran(new Transition(new SimpleTf("D"), new SimpleTf("A"), s23));

      Utils.showDot(tffst2.toDot("tffst2"));
      
      Tffst tffstComposition = tffst1.composition(tffst2);
      
      Utils.showDot(tffstComposition.toDot("tffst1 o tffst2"));

      tffstComposition.setDeterministic(false);
      tffstComposition.determinize();
      
      Utils.showDot(tffstComposition.toDot("tffst1 o tffst2 determinize"));
      

   }
  

   
   
}