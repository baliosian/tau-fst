/*
 * Created on 12-Aug-2004
 *
 * Copyright (C) 2004 Javier Baliosian
 * All rights reserved.
 * */
package uy.edu.fing.mina.fsa.test;

import uy.edu.fing.mina.fsa.tf.SimpleTf;
import uy.edu.fing.mina.fsa.tffsr.Tffsr;
import uy.edu.fing.mina.fsa.tffst.State;
import uy.edu.fing.mina.fsa.tffst.Tffst;
import uy.edu.fing.mina.fsa.tffst.Transition;
import uy.edu.fing.mina.fsa.utils.Utils;

/**
 * @author Javier Baliosian &lt; <a
 *         href="mailto:jbaliosian@tsc.upc.es">jbaliosian@tsc.upc.es </a>&gt;
 */
public class Test {

  public static void main(String[] args) {

    Tffst.setMinimizeAlways(false);

    Tffst tffst1 = new Tffst();
    State s0 = new State();
    tffst1.setInitialState(s0);

    State s1 = new State();
    s1.setAccept(true);

    SimpleTf tf1 = new SimpleTf();
    tf1.setSLabel("A");

    SimpleTf tf2 = new SimpleTf();
    tf2.setSLabel("B");

    SimpleTf tf3 = new SimpleTf();
    tf3.setSLabel("C");

    SimpleTf tf4 = new SimpleTf();
    tf4.setSLabel("D");

    Transition trans1 = new Transition(tf1, tf2, s1);

    Transition trans2 = new Transition(tf3, tf4, s1);

    s0.addTransition(trans1);

    s0.addTransition(trans2);

    Tffst tffst2 = new Tffst();
    State s20 = new State();
    tffst2.setInitialState(s20);

    State s21 = new State();
    s21.setAccept(true);

    SimpleTf tf21 = new SimpleTf();
    tf21.setSLabel("E");

    SimpleTf tf22 = new SimpleTf();
    tf22.setSLabel("F");

    SimpleTf tf23 = new SimpleTf();
    tf23.setSLabel("G");

    SimpleTf tf24 = new SimpleTf();
    tf24.setSLabel("H");

    Transition trans21 = new Transition(tf21, tf22, s21);

    Transition trans22 = new Transition(tf23, tf24, s21);

    s20.addTransition(trans21);

    s20.addTransition(trans22);

    tffst1 = tffst1.union(tffst2);

    Utils.showDot(tffst1.toDot("union"));
    
    Tffsr tffsr = tffst1.toTffsr();

    Utils.showDot(tffsr.toDot("proyeccion"));
    
    tffsr = tffsr.kleene();

    Utils.showDot(tffsr.toDot("kleene"));

    //FIXME falla el minimize 
    tffsr.minimize();

    Utils.showDot(tffsr.toDot("minimize"));

  }

}