/*
 * Created on 12-Aug-2004
 *
 * Copyright (C) 2004 Javier Baliosian
 * All rights reserved.
 * */
package uy.edu.fing.mina.fsa.test;

import java.util.LinkedList;

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
public class Lupa {

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
      s0.setNumber(0);
      tffst1.setInitialState(s0);
      State s1 = new State();
      s1.setAccept(true);
      
      EventTf eventD = new EventTf();
      eventD.setName("D");
      
      EventTf notEventD = new EventTf();
      notEventD.setName("D");
      
      ActionTf actionC = new ActionTf();
      actionC.setName("C");
      
      ActionTf actionD = new ActionTf();
      actionD.setName("D");
      
      LinkedList<TfI> list = new LinkedList<TfI>();
      list.add(actionD);
      list.add(actionC);
      TfString tfString = new TfString (list);

      s0.addOutTran(new Transition(eventD, actionC, s1));
      Transition t = new Transition(notEventD.not(), actionD.not(), s0);
      t.setLabelOut(tfString);
      s0.addOutTran(t);
      
      Utils.showDot(tffst1.toDot("tffst1"));
      

//            tffst2
//      
//                       !C/!C             
//                     ┌───────────┐           
//                     ▼           │           
//                   ┌───────────────┐  C/C  ╔═══╗
//      initial  ──▶ │       20      │ ────▶ ║21 ║
//                   └───────────────┘       ╚═══╝
      
      Tffst tffst2 = new Tffst();

      State s20 = new State();
      tffst2.setInitialState(s20);
      State s21 = new State();
      s21.setAccept(true);
      
      EventTf eventC = new EventTf();
      eventC.setName("C");
      
      ActionTf actionC2 = new ActionTf();
      actionC2.setName("C");
      
      ActionTf actionE = new ActionTf();
      actionE.setName("E");

      s20.addOutTran(new Transition(eventC.not(), actionC2.not(), s20));
      s20.addOutTran(new Transition(eventC,actionE , s21));
      
      Utils.showDot(tffst2.toDot("tffst2"));
      
      Tffst tffstComposition = tffst1.composition(tffst2);
      
      try {
		LupaExporter.generateLupaFiles(tffst1, "src/fsm_template.lua", "out_test_pdp_aux");
	} catch (UnsupportedTFFSTException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
      
      Utils.showDot(tffstComposition.toDot("tffst1 o tffst2"));
      

   }
  

   
   
}