package uy.edu.fing.mina.fsa.test;

import java.util.HashSet;
import java.util.Set;

import uy.edu.fing.mina.fsa.tf.SimpleTf;
import uy.edu.fing.mina.fsa.tf.TfI;
import uy.edu.fing.mina.fsa.tffst.State;
import uy.edu.fing.mina.fsa.tffst.Tffst;
import uy.edu.fing.mina.fsa.tffst.Transition;
import uy.edu.fing.mina.fsa.utils.Utils;

public class RateAndPower_nohalt {

  public Tffst ruleTemplate(TfI tfinp1,TfI tfinp2,TfI tfinp3,TfI tfout4,TfI tfout5) {
    
    State s0 = new State();
    State s1 = new State();
    State s2 = new State();
    State s3 = new State();
    State s4 = new State();
    State s5 = new State();

    Tffst tffst = new Tffst();
    tffst.setInitialState(s0);
    s5.setAccept(true);
    
    s0.addOutTran(new Transition(tfinp1, SimpleTf.Epsilon(),s1));
    s0.addOutTran(new Transition(tfinp1.not(), tfinp1.not(),s0,1));

    s1.addOutTran(new Transition(tfinp2, SimpleTf.Epsilon(),s2));
    s1.addOutTran(new Transition(tfinp2.not(), tfinp2.not(),s1,1));

    s2.addOutTran(new Transition(tfinp3, SimpleTf.Epsilon(),s3));
    s2.addOutTran(new Transition(tfinp3.not(), tfinp3.not(),s2,1));

    s3.addOutTran(new Transition(SimpleTf.Epsilon(), tfout4,s4));
    s4.addOutTran(new Transition(SimpleTf.Epsilon(), tfout5,s5));
    
    tffst.removeInputEpsilonLabel();
    
    return tffst;
  }
  
  
  
  public static void main(String[] args) {
    
    RateAndPower_nohalt rap = new RateAndPower_nohalt();
    Set<Tffst> rules = new HashSet<Tffst>();
    
    
    
//  Inputs                 |       Outputs
//-------------------------------------------------------------   
//Loss     | Rate       | Power      |  Rate    |  Power
//-------------------------------------------------------------   
//not low  |            | high       | decrease |   keep
//not low  |            | not high   | keep     |  increase
//low      |            | increase   | keep     |   keep

    
//    rules.add(rap.ruleTemplate((new SimpleTf("ll")).not()   , SimpleTf.Epsilon(),(new SimpleTf("hp"))       , new SimpleTf("dr"), new SimpleTf("kp")));
//    rules.add(rap.ruleTemplate((new SimpleTf("ll")).not()   , SimpleTf.Epsilon(),(new SimpleTf("hp")).not() , new SimpleTf("kr"), new SimpleTf("ip")));
//    rules.add(rap.ruleTemplate((new SimpleTf("ll"))         , SimpleTf.Epsilon(),(new SimpleTf("pi"))       , new SimpleTf("kr"), new SimpleTf("kp")));

//-----------------------------------------------------------    
    
//    rules.add(rap.ruleTemplate((new SimpleTf("ml"))   , SimpleTf.Epsilon(),(new SimpleTf("hp")) , new SimpleTf("dr"), new SimpleTf("kp")));
//    rules.add(rap.ruleTemplate((new SimpleTf("hl"))   , SimpleTf.Epsilon(),(new SimpleTf("hp")) , new SimpleTf("dr"), new SimpleTf("kp")));
//    rules.add(rap.ruleTemplate((new SimpleTf("ml"))   , SimpleTf.Epsilon(),(new SimpleTf("lp")) , new SimpleTf("kr"), new SimpleTf("ip")));
//    rules.add(rap.ruleTemplate((new SimpleTf("hl"))   , SimpleTf.Epsilon(),(new SimpleTf("lp")) , new SimpleTf("kr"), new SimpleTf("ip")));
//    rules.add(rap.ruleTemplate((new SimpleTf("ml"))   , SimpleTf.Epsilon(),(new SimpleTf("mp")) , new SimpleTf("kr"), new SimpleTf("ip")));
//    rules.add(rap.ruleTemplate((new SimpleTf("hl"))   , SimpleTf.Epsilon(),(new SimpleTf("mp")) , new SimpleTf("kr"), new SimpleTf("ip")));
//    rules.add(rap.ruleTemplate((new SimpleTf("ll"))   , SimpleTf.Epsilon(),(new SimpleTf("pi")) , new SimpleTf("kr"), new SimpleTf("kp")));
    
    
  //Inputs                          |  Outputs       
 // ---------------------------------------------------------------------   
 //Loss     |  Rate     | Power     |  Rate      |    Power   
 // ---------------------------------------------------------------------   
 //not low  |  not low  |           |  decrease  |    keep    
 //not low  |  low      |      high |  keep      |    keep    
 //not low  |  low      | not  high |  keep      |    increase    
 //low      |           |   not low |  keep      |    decrease    
 //low      |  high     |      low  |  keep      |    keep    
 //low      |  not high |      low  |  increase  |    keep    
     
//    rules.add(rap.ruleTemplate((new SimpleTf("ll")).not()   ,(new SimpleTf("lr")).not() ,SimpleTf.Epsilon()         , new SimpleTf("dr"), new SimpleTf("kp")));
//    rules.add(rap.ruleTemplate((new SimpleTf("ll")).not()   ,(new SimpleTf("lr"))       ,(new SimpleTf("hp"))       , new SimpleTf("kr"), new SimpleTf("kp")));
//    rules.add(rap.ruleTemplate((new SimpleTf("ll")).not()   ,(new SimpleTf("lr"))       ,(new SimpleTf("hp")).not() , new SimpleTf("kr"), new SimpleTf("ip")));
//    rules.add(rap.ruleTemplate((new SimpleTf("ll"))         ,SimpleTf.Epsilon()         ,(new SimpleTf("lp")).not() , new SimpleTf("kr"), new SimpleTf("dp")));
//    rules.add(rap.ruleTemplate((new SimpleTf("ll"))         ,(new SimpleTf("hr"))       ,(new SimpleTf("lp"))       , new SimpleTf("kr"), new SimpleTf("kp")));
//    rules.add(rap.ruleTemplate((new SimpleTf("ll"))         ,(new SimpleTf("hr")).not() ,(new SimpleTf("lp"))       , new SimpleTf("ir"), new SimpleTf("kp")));
    
    
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




