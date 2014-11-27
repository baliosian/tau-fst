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
public class TffstMinimization0 {

   /**
    * uy.edu.fing.mina.omega.tffst.test 8. it shows determinization of a union 
    * 
    * @param args
    */
   public static void main(String[] args) {
     
     Tffst.setMinimizeAlways(false);
     
     Tffst t = new Tffst();
     
     TfI r = new SimpleTf("r");
     TfI a = new SimpleTf("a");
     TfI v = new SimpleTf("v");

     State s0 = new State();
     State s1 = new State();
     State s2 = new State();
     State s3 = new State();
     
     t.setInitialState(s0);
     s3.setAccept(true);

     s0.addOutTran(new Transition(r, r, s1));
     s1.addOutTran(new Transition(a, a, s2));
     s2.addOutTran(new Transition(v, v, s3));
      
     Utils.showDot(t.toDot("Permission"));
     
     t = t.totalize();
     
     Utils.showDot(t.toDot("Totalized Permission"));
     
     t = t.kleene(1);
           
     Utils.showDot(t.toDot("Continuous Permission"));
     
     t.minimize();

     Utils.showDot(t.toDot("Continuous Permission Minimized"));

     t = t.complement();

     Utils.showDot(t.toDot("Prohibition"));

   }
  
}