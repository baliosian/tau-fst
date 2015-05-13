/*
 * Created on 12-Aug-2004
 *
 * Copyright (C) 2004 Javier Baliosian
 * All rights reserved.
 * */
package uy.edu.fing.mina.papers.interoperability;

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
public class outline {

   /**
    * uy.edu.fing.mina.omega.tffst.test 8. it shows determinization of a union 
    * 
    * @param args
    */
   public static void main(String[] args) {
      
      Tffst.setMinimizeAlways(false);
      
      Tffst t = new Tffst();
      
      TfI dep = new SimpleTf("doctor-examines-patient");
      TfI wpr = new SimpleTf("write-patient-record");
      
      State s0 = new State();
      State s1 = new State();
      
      Transition tran = new Transition(dep, dep , s1,1);
      tran.getLabelOut().add(wpr);

      t.setInitialState(s0);
      s1.setAccept(true);
      s0.addOutTran(tran);
      
      t.renumerateStateLabels();
      
      Utils.showDot(t.toDot("","LR"),"/home/javier/Dropbox/sharelatex/interoperability/outline");

   }
  
}