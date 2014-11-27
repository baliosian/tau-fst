/*
 * Created on 12-Aug-2004
 *
 * Copyright (C) 2004 Javier Baliosian
 * All rights reserved.
 * */
package uy.edu.fing.mina.fsa.test.old;

import uy.edu.fing.mina.fsa.tf.SimpleTf;
import uy.edu.fing.mina.fsa.tf.TfI;
import uy.edu.fing.mina.fsa.tf.TfString;
import uy.edu.fing.mina.fsa.tffst.State;
import uy.edu.fing.mina.fsa.tffst.Tffst;
import uy.edu.fing.mina.fsa.tffst.Transition;
import uy.edu.fing.mina.fsa.utils.Utils;

/**
 * @author Javier Baliosian &lt; <a
 *         href="mailto:jbaliosian@tsc.upc.es">jbaliosian@tsc.upc.es </a>&gt;
 */
public class TestEpsilonRem {

   /**
    * uy.edu.fing.mina.omega.tffst.test 8. it shows determinization of a union 
    * 
    * @param args
    */
   public static void main(String[] args) {
      
      Tffst.setMinimizeAlways(false);
      
      //tffst1
      Tffst tffst1 = new Tffst();

      State s0 = new State();
      tffst1.setInitialState(s0);
      State s1 = new State();
      State s2 = new State();
      s2.setAccept(true);

      SimpleTf tfd = new SimpleTf();
      tfd.setName("D");
      Transition trans1 = new Transition(tfd, tfd, s1, 1);
      s0.addOutTran(trans1);
      
      SimpleTf tfall = new SimpleTf();
      tfall.setAcceptAll();
      SimpleTf tfc = new SimpleTf();
      tfc.setName("C");
      TfI tfall_c = tfall.and(tfc.not());
      
      Transition trans2 = new Transition(tfall_c, tfall_c, s1, 1);
      s1.addOutTran(trans2);
      
      SimpleTf tfk = new SimpleTf();
      tfk.setName("K");
      TfString sec = new TfString(tfc);
      TfString seck = new TfString(tfc);
      seck.add(tfk);
      Transition trans3 = new Transition(sec, seck, s2);
      s1.addOutTran(trans3);

      Utils.showDot(tffst1.toDot("tffst1"));
  
      tffst1 = tffst1.toSingleLabelTransitions(); //FIXME
      Utils.showDot(tffst1.toDot("tffst1 simple"));
      
      tffst1.removeInputEpsilonLabel(); 
      Utils.showDot(tffst1.toDot("tffst1 without epsilons"));

//      tffst1.removeDeadTransitions();
//      Utils.showDot(tffst1.toDot("tffst1 without dead transitions"));
   }
  
}