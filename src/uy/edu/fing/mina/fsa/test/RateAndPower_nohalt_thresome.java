package uy.edu.fing.mina.fsa.test;

import java.util.HashSet;
import java.util.Set;

import uy.edu.fing.mina.fsa.tf.SimpleTf;
import uy.edu.fing.mina.fsa.tf.TfI;
import uy.edu.fing.mina.fsa.tffst.State;
import uy.edu.fing.mina.fsa.tffst.Tffst;
import uy.edu.fing.mina.fsa.tffst.Transition;
import uy.edu.fing.mina.fsa.utils.Utils;

public class RateAndPower_nohalt_thresome {
  
  
  public static void main(String[] args) {
    
    State s0 = new State();
    State s1 = new State();
    State s2 = new State();
    State s3 = new State();
    State s4 = new State();
    State s5 = new State();
    State s6 = new State();

    Tffst tffst = new Tffst();
    tffst.setInitialState(s0);
    s4.setAccept(true);
    s5.setAccept(true);
    s6.setAccept(true);
 
    s0.addOutTran(new Transition((new SimpleTf("hl")).not(), (new SimpleTf("hl")).not(), s0));
    s0.addOutTran(new Transition((new SimpleTf("ml")).not(), (new SimpleTf("ml")).not(), s0));
   // s0.addOutTran(new Transition((new SimpleTf("hl")).not(), (new SimpleTf("hl")).not(), s0));
    
//    s1.addOutTran(new Transition((new SimpleTf("hl")).not(), (new SimpleTf("hl")).not(), s1));
//    s2.addOutTran(new Transition((new SimpleTf("ml")).not(), (new SimpleTf("ml")).not(), s2));
//    s3.addOutTran(new Transition((new SimpleTf("hl")).not(), (new SimpleTf("hl")).not(), s3));

  //  s0.addOutTran(new Transition((new SimpleTf("hl")), SimpleTf.Epsilon(), s4));
    s0.addOutTran(new Transition((new SimpleTf("ml")), SimpleTf.Epsilon(), s5));
//    s0.addOutTran(new Transition((new SimpleTf("hl")), SimpleTf.Epsilon(), s6));

//    s4.addOutTran(new Transition((new SimpleTf("lp")).not(), (new SimpleTf("lp")).not(), s4));
    s5.addOutTran(new Transition((new SimpleTf("mp")).not(), (new SimpleTf("mp")).not(), s5));
//    s6.addOutTran(new Transition((new SimpleTf("lp")).not(), (new SimpleTf("lp")).not(), s6));
    
    Utils.showDot(tffst.toDot("before"));

    tffst.setDeterministic(false);
    tffst.determinize();

    Utils.showDot(tffst.toDot("after"));
    
  }
  
  
}




