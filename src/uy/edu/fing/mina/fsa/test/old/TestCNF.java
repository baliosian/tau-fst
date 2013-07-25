///*
// * Created on 12-Aug-2004
// *
// * Copyright (C) 2004 Javier Baliosian
// * All rights reserved.
// * */
//package uy.edu.fing.mina.fsa.test.old;
//
//import orbital.logic.imp.Formula;
//import orbital.moon.logic.ClassicalLogic;
//import orbital.moon.logic.resolution.DefaultClausalFactory;
//import uy.edu.fing.mina.fsa.logics.TfSymbol;
//import uy.edu.fing.mina.fsa.logics.Utils;
//import uy.edu.fing.mina.fsa.tf.SimpleTf;
//import uy.edu.fing.mina.fsa.tf.TfI;
//
///**
// * @author Javier Baliosian &lt; <a
// *         href="mailto:jbaliosian@tsc.upc.es">jbaliosian@tsc.upc.es </a>&gt;
// */
//
//public class TestCNF {
//
//   /**
//    * uy.edu.fing.mina.omega.tffst.test 5, is the very example of the policy's paper. 
//    * it works well but A <-> B  and B <-> A must be unified
//    * 
//    * @param args
//    */
//   
//   public static void main(String[] args) {
//
//      SimpleTf tf1 = new SimpleTf();
//      tf1.setSLabel("A");
//
//      SimpleTf tf2 = new SimpleTf();
//      tf2.setSLabel("C");
//
//      SimpleTf tf3 = new SimpleTf();
//      tf3.setSLabel("D");
//
//      SimpleTf tf4 = new SimpleTf();
//      tf4.setSLabel("E");
//
////    TfI ctf = tf1.and(tf2).and(tf1).and(tf1.not()).and(tf2).and(tf1);
//      TfI ctf = tf1.and(tf1.not());
//      
//      System.out.println(ctf);
//      
//      ctf = uy.edu.fing.mina.fsa.logics.Utils.conjunctiveForm(ctf);
//      
//      System.out.println(ctf);
//            
//      ClassicalLogic cl = new ClassicalLogic();
//
//      TfSymbol tfSymbol1 = new TfSymbol(tf1);
//      tfSymbol1.setSignifier("a");
//      Formula f1 = cl.createSymbol(tfSymbol1);
//      
//      TfSymbol tfSymbol2 = new TfSymbol(tf2);
//      tfSymbol2.setSignifier("c");
//      Formula f2 = cl.createSymbol(tfSymbol2);
//      
//      Formula f1not = f1.not();
//      
//      //(D and ((!A or !D) and (!A and (A and !D))))
//      
//      Formula f1andnotf1 = f1.and(f1not);
//      
//      Formula notf1andf1 = f1not.and(f1);
//
//      
////      Formula f2andf1andnotf1 = f2.and(f1andnotf1);
//      
////    Formula f = f2andf1andnotf1.and(f2andf1andnotf1).and(f2andf1andnotf1);
//      Formula f = f1andnotf1;
//      
//      DefaultClausalFactory dcf = new DefaultClausalFactory();
//      
//      f1andnotf1 = dcf.asClausalSet(f1andnotf1).toFormula();
//
//      //                                 ( A and((!A       or  D ) and((!D       or      !A ) and (D and (    !D or   A)))))
//      
//      System.out.println(f1.and((f1.not().or(f2)).and((f2.not().or(f1.not()).and(f2.and(f2.not().or(f1)))))));
//      System.out.println(dcf.asClausalSet(f1.and((f1.not().or(f2)).and((f2.not().or(f1.not()).and(f2.and(f2.not().or(f1))))))));
//
//      System.out.println(ClassicalLogic.Utilities.disjunctiveForm(f1.and((f1.not().or(f2)).and((f2.not().or(f1.not()).and(f2.and(f2.not().or(f1)))))),false));
//      System.out.println(ClassicalLogic.Utilities.disjunctiveForm(f1.and((f1.not().or(f2)).and((f2.not().or(f1.not()).and(f2.and(f2.not().or(f1)))))),true));
//
//      
//      System.out.println(Utils.toTF(ClassicalLogic.Utilities.disjunctiveForm( f1.not().and(f1), true)) );
//
// 
//   }
//   
//
//}