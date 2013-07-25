///*
// * Created on 12-Aug-2004
// *
// * Copyright (C) 2004 Javier Baliosian
// * All rights reserved.
// * */
//package uy.edu.fing.mina.fsa.test.old;
//
//import orbital.logic.imp.Formula;
//import orbital.logic.sign.Symbol;
//import orbital.logic.sign.SymbolBase;
//import orbital.logic.sign.type.Types;
//import orbital.moon.logic.ClassicalLogic;
//
///**
// * @author Javier Baliosian &lt; <a
// *         href="mailto:jbaliosian@tsc.upc.es">jbaliosian@tsc.upc.es </a>&gt;
// */
//public class TestLogics2 {
//
//   public static void main(String[] args) {
//      
//      TestLogics2 test = new TestLogics2();
//
//      Symbol sa = new SymbolBase("A", Types.TRUTH, null, true );
//      Symbol sb = new SymbolBase("B", Types.TRUTH, null, true );
//      Symbol sc = new SymbolBase("C", Types.TRUTH, null, true );
//      
//      ClassicalLogic cl = new ClassicalLogic(); 
//      
//      Formula fa = cl.createSymbol(sa);
//      Formula fb = cl.createSymbol(sb);
//      Formula fc = cl.createSymbol(sc);
//
//      Formula f = fa.and(fb);
//      f = f.and(fc);
////      f = f.and(fa.not());
//      
//      System.out.println("Original: " + f.toString());
//      
//      Formula fdnf = ClassicalLogic.Utilities.disjunctiveForm(f, true);
//      System.out.println("dnf: " + fdnf.toString());
//
//      if (f instanceof Formula.Composite) {
//         Formula.Composite fcomp = (Formula.Composite) f;
//         Object o = fcomp.getCompositor();
//         System.out.println("f Compositor: " + o.toString());
//         Object o1 = fcomp.getComponent();
//         if (o1 instanceof Formula[]) {
//            Formula[] new_name = (Formula[]) o1;
//            for (int i = 0; i < new_name.length; i++) {
//               System.out.println("symbols: " + new_name[i].toString());
//            }
//         }
//      }
//
//      
//      if (fa instanceof Formula.Composite) {
//         Formula.Composite fcomp = (Formula.Composite) fa;
//         Object o = fcomp.getCompositor();
//         System.out.println("fa Compositor: " + o.toString());
//      }
//      
//      Formula fnot = fa.not();
//      
//      if (fnot instanceof Formula.Composite) {
//         Formula.Composite fcomp = (Formula.Composite) fnot;
//         Object o = fcomp.getCompositor();
//         System.out.println("fnot Compositor: " + o.toString());
//         Object o1 = fcomp.getComponent();
//         System.out.println("symbols: " + o1.toString());
//         System.out.println("class: " + o1.getClass().getName());
//      }
//     
//      
//    
//   }
//  
//}