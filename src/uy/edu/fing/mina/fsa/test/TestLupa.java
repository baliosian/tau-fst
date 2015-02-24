/*
 * Created on 12-Aug-2004
 *
 * Copyright (C) 2004 Javier Baliosian
 * All rights reserved.
 * */
package uy.edu.fing.mina.fsa.test;

import uy.edu.fing.mina.fsa.tf.SimpleTf;
import uy.edu.fing.mina.fsa.tf.TfI;
import uy.edu.fing.mina.fsa.tf.TfString;
import uy.edu.fing.mina.fsa.tffsr.Tffsr;
import uy.edu.fing.mina.fsa.tffst.State;
import uy.edu.fing.mina.fsa.tffst.Tffst;
import uy.edu.fing.mina.fsa.tffst.Transition;
import uy.edu.fing.mina.fsa.utils.Utils;
import uy.edu.fing.mina.lupa.LupaExporter;
import uy.edu.fing.mina.lupa.exceptions.UnsupportedTFFSTException;
import uy.edu.fing.mina.lupa.tf.ActionTf;
import uy.edu.fing.mina.lupa.tf.EventTf;

/**
 * @author Javier Baliosian &lt; <a
 *         href="mailto:jbaliosian@tsc.upc.es">jbaliosian@tsc.upc.es </a>&gt;
 */
public class TestLupa {

   /**
    * composition test 1  
    * 
    * @param args
    */
   public static void main(String[] args) {
//      
//      Tffst.setMinimizeAlways(false);
//      
//
//// tffst1 :   
////                 !D/!D                
////               ┌───────────┐              
////               ▼           │              
////             ┌───────────────┐  D/D   ╔═══╗
////initial  ──▶ │       0       │ ─────▶ ║ 1 ║
////             └───────────────┘        ╚═══╝
//
//      
//      Tffst tffst1 = new Tffst();
//
//      State s0 = new State();
//      tffst1.setInitialState(s0);
//      State s1 = new State();
//      s1.setAccept(true);
//      
//      EventTf d = new EventTf();
//      d.setSLabel("eventD");
//      
//      ActionTf cAction2 = new ActionTf();
//      cAction2.setSLabel("actionC");
//      
//      ActionTf dAction = new ActionTf();
//      dAction.setSLabel("actionD");
//
//      s0.addTransition(new Transition(d, cAction2, s1));
//      s0.addTransition(new Transition(d.not(), cAction2.not(), s0));
//      
//      Utils.showDot(tffst1.toDot("tffst1"));
//      
//
////            tffst2
////      
////                       !C/!C             
////                     ┌───────────┐           
////                     ▼           │           
////                   ┌───────────────┐  C/C  ╔═══╗
////      initial  ──▶ │       20      │ ────▶ ║21 ║
////                   └───────────────┘       ╚═══╝
//      
//      Tffst tffst2 = new Tffst();
//
//      State s20 = new State();
//      tffst2.setInitialState(s20);
//      State s21 = new State();
//      s21.setAccept(true);
//      
//      EventTf cEvent = new EventTf();
//      cEvent.setSLabel("eventC");
//      
//      ActionTf cAction = new ActionTf();
//      cAction.setSLabel("actionC");
//      
//      ActionTf eAction = new ActionTf();
//      eAction.setSLabel("actionE");
//
//      s20.addTransition(new Transition(cEvent.not(), cAction.not(), s20));
//      s20.addTransition(new Transition(cEvent,eAction , s21));
//      
//      Utils.showDot(tffst2.toDot("tffst2"));
//      
//      Tffst tffstComposition = tffst1.composition(tffst2);
//      
//      try {
//		LupaExporter.generateLupaFiles(tffstComposition, "fsm_template.lua", "out_test_pdp_aux");
//	} catch (UnsupportedTFFSTException e) {
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//	}
//      
//      Utils.showDot(tffstComposition.toDot("tffst1 o tffst2"));
//      
//
   }
  

   
   
}