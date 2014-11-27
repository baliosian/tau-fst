/*
 * Created on 12-Aug-2004
 *
 * Copyright (C) 2004 Javier Baliosian
 * All rights reserved.
 * */
package uy.edu.fing.mina.fsa.test.operations;

import uy.edu.fing.mina.fsa.tf.SimpleTf;
import uy.edu.fing.mina.fsa.tf.TfString;
import uy.edu.fing.mina.fsa.tffsr.State;
import uy.edu.fing.mina.fsa.tffsr.Tffsr;
import uy.edu.fing.mina.fsa.tffsr.Transition;
import uy.edu.fing.mina.fsa.utils.Utils;

/**
 * @author Javier Baliosian &lt; <a
 *         href="mailto:jbaliosian@tsc.upc.es">jbaliosian@tsc.upc.es </a>&gt;
 */
public class TffsrMnimization1 {

  public static void main(String[] args) {

    Tffsr.setMinimizeAlways(false);

    Tffsr tffsr1 = new Tffsr();
    
    State s0 = new State();
    tffsr1.setInitialState(s0);

    State s1 = new State();
    s1.setAccept(true);
    
    State s2 = new State();
    s2.setAccept(true);

    SimpleTf tf1 = new SimpleTf();
    tf1.setName("A");

    SimpleTf tf2 = new SimpleTf();
    tf2.setName("B");

    SimpleTf tf3 = new SimpleTf();
    tf3.setName("C");

    SimpleTf tf4 = new SimpleTf();
    tf4.setName("D");

    Transition trans1 = new Transition(new TfString(tf1), s1);
    Transition trans2 = new Transition(new TfString(tf2), s1);
    Transition trans3 = new Transition(new TfString(tf3), s2);
    Transition trans4 = new Transition(new TfString(tf4), s2);

    s0.addOutTran(trans1);
    s0.addOutTran(trans2);
    s0.addOutTran(trans3);
    s0.addOutTran(trans4);

    Utils.showDot(tffsr1.toDot("tffsr"));
    
    tffsr1.minimize(); 

    Utils.showDot(tffsr1.toDot("minimize 1"));
//
//    tffsr = tffsr.kleene();
//
//    Utils.showDot(tffsr.toDot("kleene"));
//
//    tffsr.minimize();  
//
//    Utils.showDot(tffsr.toDot("minimize 2"));

  }

}