package uy.edu.fing.mina.fsa.test.old;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import uy.edu.fing.mina.fsa.tf.TfI;
import uy.edu.fing.mina.fsa.tffst.State;
import uy.edu.fing.mina.fsa.tffst.Tffst;
import uy.edu.fing.mina.fsa.tffst.Transition;
import uy.edu.fing.mina.fsa.utils.Utils;
import uy.edu.fing.mina.lupa.tf.ActionTf;
import uy.edu.fing.mina.lupa.tf.EventTf;

/**
 * @author Javier Baliosian &lt; <a
 *         href="mailto:jbaliosian@tsc.upc.es">jbaliosian@tsc.upc.es </a>&gt;
 */
public class TestSimplifyNOMS10 {

	// public static void setSharedId(Tf tf1, Tf tf2, String id) {
	// tf1.setSharedId(id);
	// tf2.setSharedId(id);
	// }
	// public static void setSharedId(Transition t, String id) {
	// ((Tf)t.getLabelIn().get(0)).setSharedId(id);
	// ((Tf)t.getLabelOut().get(0)).setSharedId(id);
	// }


	public static Tffst ruletypeA(TfI ev1, TfI ev2, TfI act1, TfI act2) {
		
		Tffst rule1a = new Tffst();
		
		State s0 = new State();
		rule1a.setInitialState(s0);
		State s1 = new State();
		State s2 = new State();
		s2.setAccept(true);

		Transition trans0 = new Transition(ev1, act1, s1);
		Transition trans1 = new Transition(ev2.not(), act1, s1);
		Transition trans2 = new Transition(ev2, act2, s2);

		s0.addOutTran(trans0);
		s1.addOutTran(trans1);
		s1.addOutTran(trans2);

		Tffst rule1b = new Tffst();
		
		State s0b = new State();
		rule1b.setInitialState(s0b);
		State s1b = new State();
		State s2b = new State();
		s2b.setAccept(true);

		Transition trans0b = new Transition(ev2, act1, s1b);				
		Transition trans1b = new Transition(ev1.not(), act1, s1b);
		Transition trans2b = new Transition(ev1, act2, s2b);

		s0b.addOutTran(trans0b);
		s1b.addOutTran(trans1b);
		s1b.addOutTran(trans2b);

		//return rule1a.union(rule1b);
		
		return rule1a;
		
	}
	
	
	
	public static void main(String[] args) {

		
		/*
		 * 
		 * 
		 * 
if is\_high($T/H$) and increasing($T/H$) then turn\_on($GW_H$)
if is\_high($T/H$) and steady($T/H$) then turn\_on($GW_H$)

if is\_low($T/H$) and steady($T/H$) then turn\_off($GW_H$)
if is\_low($T/H$) and decreasing($T/H$) then turn\_off($GW_H$)


if is\_low($T/H$) and increasing($T/H$) then decrease\_price\_fast
if is\_medium($T/H$) and steady($T/H$) then decrease\_price\_slow
if is\_medium($T/H$) and decreasing($T/H$) then  decrease\_price\_fast
if is\_medium($T/H$) and increasing($T/H$) then do\_nothing (no need to write this rule)
if is\_high($T/H$) and steady($T/H$) then increase\_price\_slow
if is\_high($T/H$) and decreasing($T/H$) then  decrease\_price\_slow
if is\_high($T/H$) and increasing($T/H$) then increase\_price\_fast

if new\_price(gwX) and\\ price(gwX) < current\_price(defaultGW)\\ then setGW(gwX)

		 * 
		 */
		
		Tffst.setMinimizeAlways(false);
		
		EventTf etf_ih = new EventTf();
		etf_ih.setName("ih");
		EventTf etf_ii = new EventTf();
		etf_ii.setName("ii");
		EventTf etf_is = new EventTf();
		etf_is.setName("is");
		EventTf etf_il = new EventTf();
		etf_il.setName("il");
		EventTf etf_id = new EventTf();
		etf_id.setName("id");
		EventTf etf_im = new EventTf();
		etf_im.setName("im");
		 
		ActionTf atf_tongwh = new ActionTf();
		atf_tongwh.setName("tongwh");
		ActionTf atf_toffgwh = new ActionTf();
		atf_toffgwh.setName("toffgwh");
		ActionTf atf_dpf = new ActionTf();
		atf_dpf.setName("dpf");
		ActionTf atf_dps = new ActionTf();
		atf_dps.setName("dps");
		ActionTf atf_ips = new ActionTf();
		atf_ips.setName("ips");
		ActionTf atf_ipf = new ActionTf();
		atf_ipf.setName("ipf");
		
		
		ActionTf atf_Eps = new ActionTf();
		atf_Eps.setName("Eps");
		atf_Eps.setEpsilon();

		//antes   ((tongwh or toffgwh) or dpf)
		//despues (((((!dpf and !toffgwh) and tongwh) or ((!dpf and toffgwh) and !tongwh)) or ((dpf and !toffgwh) and !tongwh)) or ((dpf and toffgwh) and tongwh))

		System.out.println(atf_tongwh.or(atf_toffgwh).or(atf_dpf));
//		System.out.println(uy.edu.fing.mina.fsa.logics.Utils.simplify(atf_tongwh.or(atf_toffgwh)));
		
		
	}


}