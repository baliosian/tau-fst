package uy.edu.fing.mina.fsa.test;

import uy.edu.fing.mina.fsa.tf.SimpleTf;
import uy.edu.fing.mina.fsa.tf.TfI;
import uy.edu.fing.mina.fsa.tf.TfString;
import uy.edu.fing.mina.fsa.tffst.State;
import uy.edu.fing.mina.fsa.tffst.Tffst;
import uy.edu.fing.mina.fsa.tffst.Transition;
import uy.edu.fing.mina.fsa.utils.Utils;

public class ToSingleInputLabels {
  public static void main(String[] args) {
    
    Tffst.setMinimizeAlways(false);
    
    Tffst t = new Tffst();
    
    TfI r = new SimpleTf("r");
    TfI a = new SimpleTf("a");
    TfI v = new SimpleTf("v");

    State s0 = new State();
    State s1 = new State();
    State s2 = new State();
    State s3 = new State();
    
    t.setInitialState(s0);
    s2.setAccept(true);

    s0.addOutTran(new Transition(r,  SimpleTf.Epsilon(), s1));
    s1.addOutTran(new Transition(a,  SimpleTf.Epsilon(), s2));
    s2.addOutTran(new Transition(v.not(),  SimpleTf.Epsilon(), s0));
        
    t.renumerateStateLabels();
    
    Utils.showDot(t.toDot("permission"));
    
    Tffst simple = t.toSingleInputLabelTransitions();

    Utils.showDot(simple.toDot("simple"));
    
 }
  
  
}
