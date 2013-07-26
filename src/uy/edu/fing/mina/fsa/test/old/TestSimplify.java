package uy.edu.fing.mina.fsa.test.old;
import uy.edu.fing.mina.fsa.logics.Utils;
import uy.edu.fing.mina.fsa.tf.CompositeTf;
import uy.edu.fing.mina.fsa.tf.Operator;
import uy.edu.fing.mina.fsa.tf.TfI;
import uy.edu.fing.mina.lupa.tf.ActionTf;
import uy.edu.fing.mina.lupa.tf.EventTf;

/**
 * @author Javier Baliosian &lt; <a
 *         href="mailto:jbaliosian@tsc.upc.es">jbaliosian@tsc.upc.es </a>&gt;
 */
public class TestSimplify {


	
	public static void main(String[] args) {

		
		EventTf etf_ih = new EventTf();
		etf_ih.setName("ih");
		
		EventTf etf_ii = new EventTf();
		etf_ii.setName("ii");
		EventTf etf_is = new EventTf();
		etf_is.setName("is");
		 
		ActionTf atf_togwh = new ActionTf();
		atf_togwh.setName("togwh");
		
		ActionTf atf_Eps = new ActionTf();
		atf_Eps.setName("Eps");
		atf_Eps.setEpsilon();

		// ((is and !ii) and !ih)
		
		TfI tf = new CompositeTf(Operator.AND,etf_is,etf_ii.not());
		tf = new CompositeTf(Operator.AND,tf,etf_ih);
	//	TfI tf = etf_is.and(etf_ii.not()).and(etf_ih);
		
		System.out.println(tf);
		
		System.out.println(Utils.simplify(tf));
		
	}


}