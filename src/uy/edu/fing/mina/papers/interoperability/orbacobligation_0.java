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
public class orbacobligation_0 {

   /**
    * Obligation as a filter, this is in opposition to the Orbac model in which policies are just monitorized.
    * 
    * @param args
    */
   public static void main(String[] args) {
      
      Tffst.setMinimizeAlways(false);
         
      Tffst tffstob = new Tffst();

      State sob0 = new State();
      tffstob.setInitialState(sob0);

      State sob1 = new State();
      State sob2 = new State();
      State sob3 = new State();
      State sob4 = new State();
      State sob5 = new State();
      
      sob2.setAccept(true);
      sob5.setAccept(true);
      
      TfI subject = new SimpleTf("role");
      TfI action = new SimpleTf("activity");
      TfI object = new SimpleTf("view");
      TfI startca = new SimpleTf("start_ac");
      TfI endca = new SimpleTf("end_ac");
      TfI startcv = new SimpleTf("start_vc");
      TfI endcv = new SimpleTf("end_vc");
      TfI fullfilled = new SimpleTf("fullfilled");
      TfI violated = new SimpleTf("violated");
      
      sob0.addOutTran(new Transition(startca,SimpleTf.Epsilon(), sob1));
      sob1.addOutTran(
    	  new Transition(
    		  new TfString(SimpleTf.Epsilon()),
    		  new TfString(subject).addRetTFString(action).addRetTFString(object).addRetTFString(fullfilled), 
    		  sob2));
      sob1.addOutTran(new Transition(endca,SimpleTf.Epsilon(), sob0));
      sob2.addOutTran(new Transition(endca,SimpleTf.Epsilon(), sob0));

      sob1.addOutTran(new Transition(startcv,SimpleTf.Epsilon(), sob4));
      
      sob3.addOutTran(new Transition(startca,SimpleTf.Epsilon(), sob4));
      sob4.addOutTran(new Transition(endca,SimpleTf.Epsilon(), sob3));
      sob4.addOutTran( new Transition(
    	  new TfString(SimpleTf.Epsilon()),
		  new TfString(subject).addRetTFString(action).addRetTFString(object).addRetTFString(fullfilled).addRetTFString(violated), 
		  sob5));
      sob5.addOutTran(new Transition(endca,SimpleTf.Epsilon(), sob3));
      
      tffstob.renumerateStateLabels();
      
      Utils.showDot(tffstob.toDot("", "LR"),"/home/javier/Dropbox/sharelatex/interoperability/orbacobligation");

   }
  
}