/*
 * Created on 12-Aug-2004
 *
 * Copyright (C) 2004 Javier Baliosian
 * All rights reserved.
 * */
package uy.edu.fing.mina.fsa.test.old;

//import orbital.logic.imp.Formula;
//import orbital.moon.logic.ClassicalLogic;
//import uy.edu.fing.mina.fsa.tf.SimpleTf;
//import uy.edu.fing.mina.fsa.tf.TfI;
//import uy.edu.fing.mina.fsa.tffst.Tffst;
//import uy.edu.fing.mina.fsa.utils.Utils;

/**
 * @author Javier Baliosian &lt; <a
 *         href="mailto:jbaliosian@tsc.upc.es">jbaliosian@tsc.upc.es </a>&gt;
 */
public class TestLogics3 {

   /**
    * uy.edu.fing.mina.omega.tffst.test logics 1. it uy.edu.fing.mina.omega.tffst.test the simplification of a composite tf. 
    * that composite tf must have only ANDs and ORs.
    * it works well!
    * 
    *  
    * @param args
    */
   
//   public static void main(String[] args) {
//      
//      TestLogics3 test = new TestLogics3();
//      Tffst.setMinimizeAlways(false);
//
//      SimpleTf tf1 = new SimpleTf();
//      tf1.setSLabel("A");
//      tf1.setEventName("a");
//
//      SimpleTf tf2 = new SimpleTf();
//      tf2.setSLabel("B");
//      tf2.setEventName("b");
//
//      SimpleTf tf3 = new SimpleTf();
//      tf3.setSLabel("C");
//      tf3.setEventName("c");
//
//      SimpleTf tf4 = new SimpleTf();
//      tf4.setSLabel("D");
//      tf4.setEventName("d");
//
//      SimpleTf tf21 = new SimpleTf();
//      tf21.setSLabel("E");
//      tf21.setEventName("a");
//
//      SimpleTf tf22 = new SimpleTf();
//      tf22.setSLabel("F");
//      tf22.setEventName("a");
//
//      SimpleTf tf23 = new SimpleTf();
//      tf23.setSLabel("G");
//      tf23.setEventName("g");
//
//      SimpleTf tf24 = new SimpleTf();
//      tf24.setSLabel("H");
//      tf24.setEventName("h");
//      
//      TfI and1 = tf1.and(tf2);
//      
//      TfI nottf2 = tf2.not();
//      
//      TfI and2 = and1.and(nottf2);
//      
//      TfI and3 = and2.and(tf2);
//
//      System.out.println("TF is:");
//      System.out.println(and3);
//
//      Formula f = Utils.toFormula(and3);
//      System.out.println("f: " + f.toString());
//      
//      Formula fdnf = ClassicalLogic.Utilities.disjunctiveForm(f, true);
//      System.out.println("fdnf: " + fdnf.toString());
//      
//      System.out.println(fdnf.getFreeVariables().toString());
//      System.out.println(fdnf.getBoundVariables().toString());
//      System.out.println(fdnf.getVariables().toString());
//
//      
//      Formula fdnfnot = ClassicalLogic.Utilities.disjunctiveForm(fdnf.not(), true);
//      System.out.println("fdnfnot: " + fdnfnot.toString());
//
//      Formula fcnf = ClassicalLogic.Utilities.conjunctiveForm(fdnfnot.not(), true);
//      System.out.println("fcnf: " + fcnf.toString());
//      
//      TfI tfdnf = Utils.toTF(fdnf);
//      System.out.println("tfdnf: " + tfdnf.toString());
//    
//   }
  
}