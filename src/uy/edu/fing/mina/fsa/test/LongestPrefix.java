package uy.edu.fing.mina.fsa.test;

import uy.edu.fing.mina.fsa.tf.SimpleTf;
import uy.edu.fing.mina.fsa.tf.TfString;
import uy.edu.fing.mina.fsa.tffst.ElementOfP;
import uy.edu.fing.mina.fsa.tffst.P;
import uy.edu.fing.mina.fsa.tffst.ProtoT;
import uy.edu.fing.mina.fsa.tffst.State;

public class LongestPrefix {
  
  public static void main(String[] args) {
    
    P p = new P();
    
    TfString arriving1 = new TfString();
    arriving1.add(new SimpleTf("A"));
    arriving1.add(new SimpleTf("B"));
    arriving1.add(new SimpleTf("C"));
    arriving1.add(new SimpleTf("D"));
    
    TfString arriving2 = new TfString();
    arriving2.add(new SimpleTf("A"));
    arriving2.add(new SimpleTf("B"));
    arriving2.add(new SimpleTf("E"));
    arriving2.add(new SimpleTf("F"));
    
    p.add(new ElementOfP(new State(), arriving1));
    p.add(new ElementOfP(new State(), arriving2));
    
    ProtoT pt = new ProtoT(new State(), new SimpleTf(), p);
    
    System.out.println(p.longestPrefix());
    
  }
  
  

}
