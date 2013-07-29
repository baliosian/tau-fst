/*
 * Created on 12-Aug-2004
 *
 * Copyright (C) 2004 Javier Baliosian
 * All rights reserved.
 * */
package uy.edu.fing.mina.fsa.test;

import uy.edu.fing.mina.fsa.tf.CompositeTf;
import uy.edu.fing.mina.fsa.tf.SimpleTf;

/**
 * @author Javier Baliosian &lt; <a
 *         href="mailto:jbaliosian@tsc.upc.es">jbaliosian@tsc.upc.es </a>&gt;
 */
public class LogicsDNF {

  /**
   * uy.edu.fing.mina.omega.tffst.test logics 1. it
   * uy.edu.fing.mina.omega.tffst.test the simplification of a composite tf.
   * that composite tf must have only ANDs and ORs. it works well!
   * 
   * 
   * @param args
   */

  public static void main(String[] args) {

    SimpleTf tfa = new SimpleTf("a");
    SimpleTf tfb = new SimpleTf("b");
    SimpleTf tfc = new SimpleTf("c");
    SimpleTf tfd = new SimpleTf("d");
    SimpleTf tfe = new SimpleTf("e");
    SimpleTf tff = new SimpleTf("f");
    SimpleTf tfg = new SimpleTf("g");
    SimpleTf tfh = new SimpleTf("h");
    SimpleTf tfi = new SimpleTf("i");
    SimpleTf tfj = new SimpleTf("j");
    SimpleTf tfk = new SimpleTf("k");
    SimpleTf tfl = new SimpleTf("l");

    // (a∨b)∧(c∨d) = ((a∧c)v(a∧d))v((b∧c)v(b∧d))
    System.out.println(((CompositeTf)(tfa.or(tfb)).and(tfc.or(tfd))));
    System.out.println(((CompositeTf)(tfa.or(tfb)).and(tfc.or(tfd))).toDNF());

    // (a∨b)∧(c∧d) = (a∧(c∧d))v(b∧(c∧d))
    System.out.println(((CompositeTf)(tfa.or(tfb)).and(tfc.and(tfd))));
    System.out.println(((CompositeTf)(tfa.or(tfb)).and(tfc.and(tfd))).toDNF());

    // (a∧b)∧(cvd) = ((a∧b)∧c)v((a∧b)∧d)
    System.out.println(((CompositeTf)(tfa.and(tfb)).and(tfc.or(tfd))));
    System.out.println(((CompositeTf)(tfa.and(tfb)).and(tfc.or(tfd))).toDNF());

    // (b∨c)∧a     = (a∧b)∨(a∧c)
    System.out.println(((CompositeTf)((tfb.or(tfc)).and(tfa))));
    System.out.println(((CompositeTf)((tfb.or(tfc)).and(tfa))).toDNF());

    // a∧(b∨c)     = (a∧b)∨(a∧c)
    System.out.println(((CompositeTf)(tfa.and(tfb.or(tfc)))));
    System.out.println(((CompositeTf)(tfa.and(tfb.or(tfc)))).toDNF());

    System.out.println(((CompositeTf)tfa.and((tfb.and(tfc.and(tfc.and(tfd.and(tfe))))).not())));
    System.out.println(((CompositeTf)tfa.and((tfb.and(tfc.and(tfc.and(tfd.and(tfe))))).not())).pushNotsDown());
    System.out.println(((CompositeTf)tfa.and((tfb.and(tfc.and(tfc.and(tfd.and(tfe))))).not())).toDNF());
    
  }

}