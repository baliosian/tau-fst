package uy.edu.fing.mina.fsa.test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import uy.edu.fing.mina.fsa.tf.CompositeTf;
import uy.edu.fing.mina.fsa.tf.Operator;
import uy.edu.fing.mina.fsa.tf.SimpleTf;
import uy.edu.fing.mina.fsa.tf.TfI;
import uy.edu.fing.mina.fsa.tffst.State;
import uy.edu.fing.mina.fsa.tffst.Tffst;
import uy.edu.fing.mina.fsa.tffst.Transition;
import uy.edu.fing.mina.fsa.utils.Utils;
import uy.edu.fing.mina.lupa.LupaExporter;
import uy.edu.fing.mina.lupa.LupaExporterRatePower;
import uy.edu.fing.mina.lupa.exceptions.UnsupportedTFFSTException;
import uy.edu.fing.mina.lupa.tf.ActionTf;
import uy.edu.fing.mina.lupa.tf.EventTf;

public class RateAndPower_LuPA {

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
    
    s0.addOutTran(new Transition(tfinp1, ActionTf.Epsilon(),s1));
    s0.addOutTran(new Transition(tfinp1.not(), ActionTf.Epsilon(),s0));

    s1.addOutTran(new Transition(tfinp2, ActionTf.Epsilon(),s2));
    s1.addOutTran(new Transition(tfinp2.not(), ActionTf.Epsilon(),s1));

    s2.addOutTran(new Transition(tfinp3, ActionTf.Epsilon(),s3));
    s2.addOutTran(new Transition(tfinp3.not(), ActionTf.Epsilon(),s2));

    s3.addOutTran(new Transition(EventTf.Epsilon(), tfout4,s4));
    s4.addOutTran(new Transition(EventTf.Epsilon(), tfout5,s5));

    //Utils.showDot(tffst.toDot("tffst before"));
    
    tffst.inLabelEpsilonRemoval();
    
    //Utils.showDot(tffst.toDot("tffst after"));
    
    return tffst;
  }
  
  
  
  public static void main(String[] args) {
    
    RateAndPower_LuPA rap = new RateAndPower_LuPA();
    Set<Tffst> rules = new HashSet<Tffst>();
    
    
    
//  Inputs                 |       Outputs
//-------------------------------------------------------------   
//Loss     | Rate       | Power      |  Rate    |  Power
//-------------------------------------------------------------   
//not low  |            | high       | decrease |   keep
//not low  |            | not high   | keep     |  increase
//low      |            |            | keep     |   keep
    
    EventTf ll = new EventTf();
    ll.setName("ll");    

    EventTf ml = new EventTf();
    ml.setName("ml");

    EventTf hl = new EventTf();
    hl.setName("hl");
    
    EventTf lp = new EventTf();
    lp.setName("lp");    
    
    EventTf mp = new EventTf();
    mp.setName("mp");
    
    EventTf hp = new EventTf();
    hp.setName("hp");
    
    ActionTf kr = new ActionTf();
    kr.setName("kr");
    kr.setUniverse("rate");
    
    ActionTf dr = new ActionTf();
    dr.setName("dr");
    dr.setUniverse("rate");
    
    ActionTf ip = new ActionTf();
    ip.setName("ip");
    ip.setUniverse("power");
    
    ActionTf dp = new ActionTf();
    dp.setName("dp");
    dp.setUniverse("power");
    
    ActionTf kp = new ActionTf();
    kp.setName("kp");
    kp.setUniverse("power");
    
/*    rules.add(rap.ruleTemplate(ll.not(), EventTf.Epsilon(), hp, dr, kp));
    rules.add(rap.ruleTemplate(ll.not(), EventTf.Epsilon(),hp.not(), kr, ip));
    rules.add(rap.ruleTemplate(ll, EventTf.Epsilon(), EventTf.Epsilon(), kr, kp));*/

//-----------------------------------------------------------    
    
//    rules.add(rap.ruleTemplate((new SimpleTf("ml"))   , SimpleTf.Epsilon(),(new SimpleTf("hp")) , new SimpleTf("dr"), new SimpleTf("kp")));
//    rules.add(rap.ruleTemplate((new SimpleTf("hl"))   , SimpleTf.Epsilon(),(new SimpleTf("hp")) , new SimpleTf("dr"), new SimpleTf("kp")));
//    rules.add(rap.ruleTemplate((new SimpleTf("ml"))   , SimpleTf.Epsilon(),(new SimpleTf("lp")) , new SimpleTf("kr"), new SimpleTf("ip")));

    
    //rules.add(rap.ruleTemplate(new CompositeTf(Operator.AND,hl,ml), EventTf.Epsilon(),lp , ip.not(), (new CompositeTf(Operator.AND,ip,kr.not())).not()));
    rules.add(rap.ruleTemplate(hl, EventTf.Epsilon(),lp , ip.not(), kr));
    //rules.add(rap.ruleTemplate(ml, SimpleTf.Epsilon(),mp , kr, new CompositeTf("AND", ip, new CompositeTf("AND",kr,dp))));
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
    
    rateAndPower = rateAndPower.kleene();
    
    Utils.showDot(rateAndPower.toDot("after"));
    
    try {
  		LupaExporterRatePower.generateLupaFiles(rateAndPower, "src/fsm_template.lua", "out_test_pdp_aux");
  	} catch (UnsupportedTFFSTException e) {
  		// TODO Auto-generated catch block
  		e.printStackTrace();
  	}

    
  }
  
  
}




