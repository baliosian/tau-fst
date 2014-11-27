package uy.edu.fing.mina.fsa.test;

import java.util.HashSet;
import java.util.Set;

import uy.edu.fing.mina.fsa.tf.SimpleTf;
import uy.edu.fing.mina.fsa.tf.TfI;
import uy.edu.fing.mina.fsa.tffst.State;
import uy.edu.fing.mina.fsa.tffst.Tffst;
import uy.edu.fing.mina.fsa.tffst.Transition;
import uy.edu.fing.mina.fsa.utils.Utils;

public class RateAndPower_1 {

  public Tffst ruleTemplate(TfI tfinp1,TfI tfinp2,TfI tfinp3,TfI tfout4,TfI tfout5) {
    
    State s0 = new State();
    State s1 = new State();
    State s2 = new State();
    State s3 = new State();
    State s4 = new State();
    State s5 = new State();

    Tffst tffst = new Tffst();
    tffst.setInitialState(s0);
    s2.setAccept(true);
    
    s0.addOutTran(new Transition(tfinp1, SimpleTf.Epsilon(),s1));
    s0.addOutTran(new Transition(tfinp1.not(), tfinp1.not(),s0,1));

    s1.addOutTran(new Transition(tfinp2, SimpleTf.Epsilon(),s2));
    s1.addOutTran(new Transition(tfinp2.not(), tfinp2.not(),s1,1));

    s2.addOutTran(new Transition(tfinp3.not(), tfinp3.not(),s2,1));
//    s2.addTransition(new Transition(tfinp3, SimpleTf.Epsilon(),s3));

//    s3.addTransition(new Transition(SimpleTf.Epsilon(), tfout4,s4));
//    s4.addTransition(new Transition(SimpleTf.Epsilon(), tfout5,s5));
    
    tffst.removeInputEpsilonLabel();
    
    return tffst;
  }
  
  
  
  public static void main(String[] args) {
    
    RateAndPower_1 rap = new RateAndPower_1();
    Set<Tffst> rules = new HashSet<Tffst>();
    
    
    rules.add(rap.ruleTemplate((new SimpleTf("ml"))   , SimpleTf.Epsilon(),(new SimpleTf("hp"))       , new SimpleTf("dr"), new SimpleTf("kp")));
    rules.add(rap.ruleTemplate((new SimpleTf("hl"))   , SimpleTf.Epsilon(),(new SimpleTf("hp"))       , new SimpleTf("dr"), new SimpleTf("kp")));
    
    Tffst rateAndPower = new Tffst();
    
    for (Tffst tffst : rules) {
      rateAndPower = rateAndPower.union(tffst);
    }

    Utils.showDot(rateAndPower.toDot("before"));

    rateAndPower.setDeterministic(false);
    rateAndPower.determinize();
//    
//    rateAndPower = rateAndPower.kleene();
//    
    Utils.showDot(rateAndPower.toDot("after"));

    
  }
  
  
}




