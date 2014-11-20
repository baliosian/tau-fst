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
public class TffstPermissionComplement0 {

   /**
    * uy.edu.fing.mina.omega.tffst.test 8. it shows determinization of a union 
    * 
    * @param args
    */
   public static void main(String[] args) {
      
      Tffst.setMinimizeAlways(false);
      
      Tffst tffst1 = new Tffst();

      State s0 = new State();
      tffst1.setInitialState(s0);
      State s1 = new State();
      s1.setAccept(true);

      SimpleTf tfa = new SimpleTf("A");
      
      s0.addOutTran(new Transition(tfa, tfa, s1));
      s0.addOutTran(new Transition(tfa.not(),SimpleTf.Epsilon(),s1));
 
      Utils.showDot(tffst1.toDot("tffst1"));
      
      Tffst tffst1kleene = tffst1.kleene(1);

      Utils.showDot(tffst1kleene.toDot("tffst1 kleene"));
      
      Tffst total = tffst1kleene.totalize();

      Utils.showDot(total.toDot("totalized tffst1kleene"));

      Tffst tffst1kleeneComp = tffst1kleene.complement(); 
           
      Utils.showDot(tffst1kleeneComp.toDot("tffst1 kleene comp"));
//      
//      Tffst tffstunion = tffst1kleene.union(tffst1kleeneComp);
//      
//      Utils.showDot(tffstunion.toDot("union"));
//      
//      tffstunion.determinize();
//      
//      Utils.showDot(tffstunion.toDot("determinized union"));

   }
  
}