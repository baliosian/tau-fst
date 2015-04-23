package uy.edu.fing.mina.fsa.test;

import uy.edu.fing.mina.fsa.logics.Implication;
import uy.edu.fing.mina.fsa.logics.Knowledge;
import uy.edu.fing.mina.fsa.logics.Utils;
import uy.edu.fing.mina.fsa.tf.SimpleTf;
import uy.edu.fing.mina.fsa.tf.Tf;
import uy.edu.fing.mina.fsa.tf.TfI;

public class SimplificationWithKnowledge2 {
  
  
  
  
  public static void main(String[] args) {
	
	TfI phy = new SimpleTf("phy");
	TfI man = new SimpleTf("man");
	TfI rek = new SimpleTf("rek");

	TfI hp = new SimpleTf("hp");
	TfI read = new SimpleTf("read");

	Knowledge.implications.add(new Implication(Tf.createdTFs.get("man"),Tf.createdTFs.get("read")));

	Knowledge.implications.add(new Implication(Tf.createdTFs.get("man"),Tf.createdTFs.get("rek").not()));
	Knowledge.implications.add(new Implication(Tf.createdTFs.get("man"),Tf.createdTFs.get("phy").not()));
	Knowledge.implications.add(new Implication(Tf.createdTFs.get("rek"),Tf.createdTFs.get("phy").not()));

	
	System.out.println(phy.andSimple(man.not()));
	System.out.println(phy.andSimple(rek));
	System.out.println(man.andSimple(rek));
	
	System.out.println(phy.andSimple(hp));
	System.out.println(hp.andSimple(read));
	System.out.println(read.andSimple(phy));
	
	
  }

}
