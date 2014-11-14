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
import uy.edu.fing.mina.fsa.tffsr.State;
import uy.edu.fing.mina.fsa.tffsr.Tffsr;
import uy.edu.fing.mina.fsa.tffsr.Transition;
import uy.edu.fing.mina.fsa.utils.Utils;


/**
 * @author Javier Baliosian &lt; <a
 *         href="mailto:jbaliosian@tsc.upc.es">jbaliosian@tsc.upc.es </a>&gt;
 */
public class TotalizeTffsr {

   /**
    * uy.edu.fing.mina.omega.tffst.test 8. it shows determinization of a union 
    * 
    * @param args
    */
   public static void main(String[] args) {
      
      Tffsr tffsr1 = new Tffsr();

      State s0 = new State();
      tffsr1.setInitialState(s0);
      State s1 = new State();
      State s2 = new State();
      s2.setAccept(true);

      SimpleTf tfd = new SimpleTf();
      tfd.setName("D");
      Transition trans1 = new Transition(new TfString(tfd), s1);
      s0.addOutTran(trans1);
      
      SimpleTf tfall = new SimpleTf();
      tfall.setAcceptAll();
      SimpleTf tfc = new SimpleTf();
      tfc.setName("C");
      TfI tfall_c = tfall.and(tfc.not());
      
      Transition trans2 = new Transition(new TfString(tfall_c), s2);
      s1.addOutTran(trans2);
      
      SimpleTf tfk = new SimpleTf();
      tfk.setName("K");
      TfString sec = new TfString(tfc);
      Transition trans3 = new Transition(sec, s2);
      s1.addOutTran(trans3);

      Utils.showDot(tffsr1.toDot("tffsr1"));
       
      Tffsr tffsr1total = tffsr1.totalize();

      Utils.showDot(tffsr1total.toDot("totalized tffsr1"));


   }
  
}