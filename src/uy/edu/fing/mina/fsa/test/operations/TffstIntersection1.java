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
public class TffstIntersection1 {

  /**
   * composition test 1
   * 
   * @param args
   */
  public static void main(String[] args) {

	Tffst.setMinimizeAlways(false);

	// tffst1 :                   ┌───┐A/B 
	//             ┌───┐  A/B   ╔═══╗ │
	// initial ──▶ │ 0 │ ─────▶ ║ 1 ║ ┘
	//             └───┘        ╚═══╝
	//                   ─────▶
	//                    C/D

	Tffst tffst1 = new Tffst();

	State s0 = new State();
	tffst1.setInitialState(s0);
	State s1 = new State();
	s1.setAccept(true);

	s0.addOutTran(new Transition(new SimpleTf("A"), new SimpleTf("B"), s1));
	s1.addOutTran(new Transition(new SimpleTf("A"), new SimpleTf("B"), s1));
	s0.addOutTran(new Transition(new SimpleTf("C"), new SimpleTf("D"), s1));

	Utils.showDot(tffst1.toDot("tffst1"));

	// tffst2 :                   ┌───┐A/B 
	//             ┌───┐  A/B   ╔═══╗ │
	// initial ──▶ │ 0 │ ─────▶ ║ 1 ║ ┘
	//             └───┘        ╚═══╝
	//                   ─────▶
	//                    E/F

	Tffst tffst2 = new Tffst();

	State s20 = new State();
	tffst2.setInitialState(s20);
	State s21 = new State();
	s21.setAccept(true);

	s20.addOutTran(new Transition(new SimpleTf("A"), new SimpleTf("B"), s21));
	s21.addOutTran(new Transition(new SimpleTf("A"), new SimpleTf("B"), s21));
	s20.addOutTran(new Transition(new SimpleTf("E"), new SimpleTf("F"), s21));

	Utils.showDot(tffst1.toDot("tffst1"));

	Utils.showDot(tffst2.toDot("tffst2"));

	Tffst tffstintersection = tffst1.intersection(tffst2);

	Utils.showDot(tffstintersection.toDot("tffst1 o tffst2"));

	tffstintersection.setDeterministic(false);
	tffstintersection.determinize();

	Utils.showDot(tffstintersection.toDot("tffst1 int tffst2 determinize"));

  }

}