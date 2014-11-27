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
    
    State s00 = new State();
    State s01 = new State();
    State s02 = new State();
    State s03 = new State();
    State s04 = new State();
    State s05 = new State();

    State s10 = new State();
    State s11 = new State();
    State s12 = new State();
    State s13 = new State();
    State s14 = new State();
    State s15 = new State();

    State s20 = new State();
    State s21 = new State();
    State s22 = new State();
    State s23 = new State();
    State s24 = new State();
    State s25 = new State();

    Tffst tffst0 = new Tffst();
    tffst0.setInitialState(s00);
    s05.setAccept(true);
    
    Tffst tffst1 = new Tffst();
    tffst1.setInitialState(s10);
    s15.setAccept(true);
    
    Tffst tffst2 = new Tffst();
    tffst2.setInitialState(s20);
    s25.setAccept(true);
    
    s00.addOutTran(new Transition(tfinp1, ActionTf.Epsilon(),s01));
    s00.addOutTran(new Transition(tfinp1.not(), ActionTf.Epsilon(),s00));

    s01.addOutTran(new Transition(tfinp2, ActionTf.Epsilon(),s02));
    s01.addOutTran(new Transition(tfinp2.not(), ActionTf.Epsilon(),s01));

    s02.addOutTran(new Transition(tfinp3, ActionTf.Epsilon(),s03));
    s02.addOutTran(new Transition(tfinp3.not(), ActionTf.Epsilon(),s02));

    //----
    
    s11.addOutTran(new Transition(tfinp2, ActionTf.Epsilon(),s12));
    s11.addOutTran(new Transition(tfinp2.not(), ActionTf.Epsilon(),s11));

    s12.addOutTran(new Transition(tfinp1, ActionTf.Epsilon(),s13));
    s12.addOutTran(new Transition(tfinp1.not(), ActionTf.Epsilon(),s12));

    s10.addOutTran(new Transition(tfinp3, ActionTf.Epsilon(),s11));
    s10.addOutTran(new Transition(tfinp3.not(), ActionTf.Epsilon(),s10));

    //-------
    
    s22.addOutTran(new Transition(tfinp1, ActionTf.Epsilon(),s23));
    s22.addOutTran(new Transition(tfinp1.not(), ActionTf.Epsilon(),s22));

    s20.addOutTran(new Transition(tfinp2, ActionTf.Epsilon(),s21));
    s20.addOutTran(new Transition(tfinp2.not(), ActionTf.Epsilon(),s20));

    s21.addOutTran(new Transition(tfinp3, ActionTf.Epsilon(),s22));
    s21.addOutTran(new Transition(tfinp3.not(), ActionTf.Epsilon(),s21));
    
    
    s03.addOutTran(new Transition(EventTf.Epsilon(), tfout4,s04));
    s04.addOutTran(new Transition(EventTf.Epsilon(), tfout5,s05));

    s13.addOutTran(new Transition(EventTf.Epsilon(), tfout4,s14));
    s14.addOutTran(new Transition(EventTf.Epsilon(), tfout5,s15));
    
    s23.addOutTran(new Transition(EventTf.Epsilon(), tfout4,s24));
    s24.addOutTran(new Transition(EventTf.Epsilon(), tfout5,s25));

    tffst0.removeInputEpsilonLabel();
    tffst1.removeInputEpsilonLabel();
    tffst2.removeInputEpsilonLabel();

//    Utils.showDot(tffst0.toDot("tffst0"));
//    Utils.showDot(tffst1.toDot("tffst1"));
//    Utils.showDot(tffst2.toDot("tffst2"));
    
    Tffst tffst = tffst0.union(tffst1).union(tffst2); 
    

//    Utils.showDot(tffst.toDot("tffst"));
    
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
//low      |            |            | increase |   keep
    
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
    
    EventTf lr = new EventTf();
    lr.setName("lr");
    
    EventTf hr = new EventTf();
    hr.setName("hr");
    
    ActionTf kr = new ActionTf();
    kr.setName("kr");
    kr.setUniverse("rate");
    
    ActionTf dr = new ActionTf();
    dr.setName("dr");
    dr.setUniverse("rate");
    
    ActionTf ir = new ActionTf();
    ir.setName("ir");
    ir.setUniverse("rate");
    
    ActionTf ip = new ActionTf();
    ip.setName("ip");
    ip.setUniverse("power");
    
    ActionTf dp = new ActionTf();
    dp.setName("dp");
    dp.setUniverse("power");
    
    ActionTf kp = new ActionTf();
    kp.setName("kp");
    kp.setUniverse("power");
    
    rules.add(rap.ruleTemplate(hl, EventTf.Epsilon(), hp, dr, kp));
    rules.add(rap.ruleTemplate(hl, EventTf.Epsilon(),lp, kr, ip));
    rules.add(rap.ruleTemplate(ll, EventTf.Epsilon(), EventTf.Epsilon(), ir, kp));
    
    //TODO que pasa cuando me llegan eventos de rate, los cuales no considero en las reglas

//-----------------------------------------------------------    
    
//    rules.add(rap.ruleTemplate((new SimpleTf("ml"))   , SimpleTf.Epsilon(),(new SimpleTf("hp")) , new SimpleTf("dr"), new SimpleTf("kp")));
//    rules.add(rap.ruleTemplate((new SimpleTf("hl"))   , SimpleTf.Epsilon(),(new SimpleTf("hp")) , new SimpleTf("dr"), new SimpleTf("kp")));
//    rules.add(rap.ruleTemplate((new SimpleTf("ml"))   , SimpleTf.Epsilon(),(new SimpleTf("lp")) , new SimpleTf("kr"), new SimpleTf("ip")));

    
    //rules.add(rap.ruleTemplate(new CompositeTf(Operator.AND,hl,ml), EventTf.Epsilon(),lp , ip.not(), (new CompositeTf(Operator.AND,ip,kr.not())).not()));
    //rules.add(rap.ruleTemplate(hl, EventTf.Epsilon(),lp , ip.not(), kr));
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
     
    rules.add(rap.ruleTemplate(hl   ,hr ,EventTf.Epsilon(), dr, kp));
    rules.add(rap.ruleTemplate(hl, lr, hp, kr, kp));
//    rules.add(rap.ruleTemplate(hl, lr, lp, kr, ip));
//    rules.add(rap.ruleTemplate(ll, EventTf.Epsilon(), hp, kr, dp));
//    rules.add(rap.ruleTemplate(ll, hr, lp, kr, kp));
//    rules.add(rap.ruleTemplate(ll, lr, lp, ir, kp));
    
    
    Tffst rateAndPower = new Tffst();
    
    for (Tffst tffst : rules) {
      rateAndPower = rateAndPower.union(tffst);
    }
    
    //Utils.showDot(rateAndPower.toDot("before kleene"));

    rateAndPower = rateAndPower.kleene();
    
    //Utils.showDot(rateAndPower.toDot("before det"));

    rateAndPower.setDeterministic(false);
    rateAndPower.determinize();
    
    //Utils.showDot(rateAndPower.toDot("after all"));
    
    try {
  		LupaExporterRatePower.generateLupaFiles(rateAndPower, "src/fsm_template.lua", "fsm_rate_loss");
  	} catch (UnsupportedTFFSTException e) {
  		// TODO Auto-generated catch block
  		e.printStackTrace();
  	}

    
  }
  
  
}




