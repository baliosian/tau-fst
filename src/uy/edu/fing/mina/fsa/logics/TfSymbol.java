/*
 * Created on 31-Aug-2004
 *
 * Copyright (C) 2004 Javier Baliosian
 * All rights reserved.
 * */
package uy.edu.fing.mina.fsa.logics;

import orbital.logic.sign.SymbolBase;
import orbital.logic.sign.type.Types;
import uy.edu.fing.mina.fsa.tf.TfI;

/**
 * @author Javier Baliosian &lt; <a
 *         href="mailto:jbaliosian@tsc.upc.es">jbaliosian@tsc.upc.es </a>&gt;
 */

public class TfSymbol extends SymbolBase {

   /**
    * Comment for <code>serialVersionUID</code>
    */
   private static final long serialVersionUID = 1L;


   /**
    * 
    * @uml.property name="tf"
    * @uml.associationEnd multiplicity="(0 1)"
    */
   TfI tf;

   
   public static String orbitAND = "&";
   public static String orbitOR = "|";
   public static String orbitNOT = "~";
   
   public TfSymbol(TfI tf) {
     super(tf.getSLabel(), Types.TRUTH, null, true );
     this.tf = tf;
  }

   public TfSymbol(TfI tf, String label) {
     super(label, Types.TRUTH, null, true );
     this.tf = tf;
  }

   /**
    * @return Returns the tf.
    * 
    * @uml.property name="tf"
    */
   public TfI getTf() {
      return tf;
   }

   /**
    * @param tf The tf to set.
    * 
    * @uml.property name="tf"
    */
   public void setTf(TfI tf) {
      this.tf = tf;
   }

}
