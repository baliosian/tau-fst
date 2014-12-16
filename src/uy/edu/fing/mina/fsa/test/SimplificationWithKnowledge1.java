package uy.edu.fing.mina.fsa.test;

import uy.edu.fing.mina.fsa.logics.Implication;
import uy.edu.fing.mina.fsa.logics.Knowledge;
import uy.edu.fing.mina.fsa.logics.Utils;
import uy.edu.fing.mina.fsa.tf.SimpleTf;
import uy.edu.fing.mina.fsa.tf.TfI;

public class SimplificationWithKnowledge1 {
  
  
  
  
  public static void main(String[] args) {
	
	
	TfI r1 = new SimpleTf("r'");
	TfI a1 = new SimpleTf("a'");
	TfI v1 = new SimpleTf("v'");

	TfI r2 = new SimpleTf("r\\\"");
	TfI a2 = new SimpleTf("a\\\"");
	TfI v2 = new SimpleTf("v\\\"");

	Knowledge.implications.add(new Implication(r1, a1.not()));
	
	System.out.println(r1.and(a1));
	System.out.println(r1.andSimple(a1));
	
	System.out.println(r1.not().and(a1));
	System.out.println(r1.not().andSimple(a1));
	
	System.out.println((r1.and(a1)).or(a1));
	System.out.println(Utils.simplify((r1.and(a1)).or(a1)));
	
	
  }

}
