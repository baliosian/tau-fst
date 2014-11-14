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
public class TffstComplement1 {

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
      tffst1 = tffst1.toSingleLabelTransitions();

      Tffst tffst2 = new Tffst();

      State s02 = new State();
      tffst2.setInitialState(s02);
      State s12 = new State();
      State s22 = new State();
      s22.setAccept(true);

      SimpleTf tfe = new SimpleTf();
      tfe.setName("E");
      Transition trans12 = new Transition(tfe, tfe, s12, 1);
      s02.addOutTran(trans12);
      
      Transition trans22 = new Transition(tfall_c, tfall_c, s12, 1);
      s12.addOutTran(trans22);
      
      TfI tfepsilon = SimpleTf.Epsilon();
      Transition trans32 = new Transition(tfc, tfepsilon, s22, 0);
      s12.addOutTran(trans32);

      Transition trans42 = new Transition(SimpleTf.AcceptsAll().and(tfe.not()) ,SimpleTf.AcceptsAll().and(tfe.not()), s02, 1);
      s02.addOutTran(trans42);

      Transition trans52 = new Transition(SimpleTf.AcceptsAll(),SimpleTf.AcceptsAll(), s22, 1);
      s22.addOutTran(trans52);
      
      
      Tffst tffst2comp = tffst2.complement();
            
      Utils.showDot(tffst2.toDot("tffst2"));

      Utils.showDot(tffst2comp.toDot("tffst2comp"));
      
//      Tffst tffstunion = tffst2.union(tffst2comp);

      
//      Utils.showDot(tffstunion.toDot("tffstunion"));
      
//      tffstunion.determinize();
      
//      Utils.showDot(tffstunion.toDot("tffstunion Determinized"));
      
//      Tffst tffst2k = tffst2.kleene();
//      Tffst tffst2compk = tffst2comp.kleene();
      
//      Tffst tffst4 = tffst2k.union(tffst2compk);
//      Utils.showDot(tffst4.toDot());
//      tffst4.determinize();
//      Utils.showDot(tffst4.toDot(""));
//      
//      Tffst out = tffst1.composition(tffst4);
//      Utils.showDot(out.toDot());

   }
  
}