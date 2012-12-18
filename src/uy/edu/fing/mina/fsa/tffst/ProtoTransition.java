/*
 * Created on 04-Aug-2004
 *
 * Copyright (C) 2004 Javier Baliosian
 * All rights reserved.
 * */
package uy.edu.fing.mina.fsa.tffst;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import uy.edu.fing.mina.fsa.tf.Operator;
import uy.edu.fing.mina.fsa.tf.TfI;
import uy.edu.fing.mina.fsa.tf.TfString;

/**
 * @author Javier Baliosian &lt; <a
 *         href="mailto:jbaliosian@tsc.upc.es">jbaliosian@tsc.upc.es </a>&gt;
 */

public class ProtoTransition {

	/**
	 * An exclusive combination of TFf
	 * 
	 * @uml.property name="exclTFs"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
	TfI exclTFs;

	/**
	 * The union of pairs produced by TransP
	 */
	Set<PairP> pairsP;

	/**
	 * the state correstponding to the set of pairs {State, TfString} that are
	 * going to be a new state in the deterministic Tffst
	 * 
	 * @uml.property name="s"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
	State s;

	/**
	 * @param np
	 * @param relation
	 * @param union
	 */
	public ProtoTransition(State np, TfI relation, Set<PairP> union) {
		s = np;
		exclTFs = relation;
		pairsP = union;

	}


	/**
	 * simplifies the pairsP list
	 * 
	 */
	public void simplifyTargetByState_noOps() {

		Set<PairP> toRemove = new HashSet<PairP>();
		int toRemoveSize;

		do {
			toRemoveSize = toRemove.size();
			for (PairP workingPair : pairsP) 
				if (!toRemove.contains(workingPair))
					for (PairP currentPair : pairsP)
						if (!toRemove.contains(currentPair))
							if ((currentPair.state == workingPair.state || workingPair.state.isAccept())
									&& workingPair != currentPair) {
								TfString currentSE = currentPair.arriving;
								TfString workingSE = workingPair.arriving;
								TfString newSE = new TfString();
								
								Iterator<TfI> workingSEiter = workingSE.iterator();
								Iterator<TfI> currentSEiter = currentSE.iterator();
								while (currentSEiter.hasNext() || workingSEiter.hasNext()) {
									TfI currentOinSE = null;
									TfI workingOinSE = null;

									if (currentSEiter.hasNext())
										currentOinSE = currentSEiter.next();
									if (workingSEiter.hasNext())
										workingOinSE = workingSEiter.next();

									if (currentOinSE != null && workingOinSE != null)
										newSE.add(workingOinSE.or(currentOinSE));
									else if (currentOinSE != null)
										newSE.add(currentOinSE);
									else if (workingOinSE != null)
										newSE.add(workingOinSE);
								}
								toRemove.add(currentPair);
								workingPair.arriving = newSE;
							}
		} while (toRemoveSize != toRemove.size());
		pairsP.removeAll(toRemove);

	}
	

	/**
	 * @param workingOPinSE
	 * @param currentOPinSE
	 * @return
	 */
	private Operator stronger(Operator op1, Operator op2) {
		if (op2.op.equals(Operator.CONCAT)
				|| (op2.op.equals(Operator.AS_TAUT_AS) && !op1.op
						.equals(Operator.CONCAT))
				|| op1.op.equals(Operator.TAUTER_THAN))
			return op1;
		else
			return op2;
	}

	
	/**
	 * Returns the longest prefix in the list of PairsP. At the same time it
	 * prunes the list of that prefix.
	 * 
	 * @return a TfString containing the longest prefix.
	 */
	public TfString longestPrefix() {

		TfString outSE = new TfString();

		boolean match = true;

		if (pairsP.size() != 0) {
			while (match) {
				Iterator<PairP> iter = pairsP.iterator();
				PairP firstPair = iter.next();
				if (firstPair.arriving.isEpsilon())
					match = false;
				while (iter.hasNext() && match) {
					PairP pair = iter.next();
					if (pair.arriving.isEpsilon())
						match = false;
					else if (!firstPair.arriving.get(0).equals(
							pair.arriving.get(0)))
						match = false;
				}
				if (match) {
					outSE.add(firstPair.arriving.get(0));
					Iterator<PairP> iter2 = pairsP.iterator();
					while (iter2.hasNext()) {
						PairP pair2 = iter2.next();
						//TODO re-apuntar todos los refersTo apuntando a 
						// pair2.arriving.get(0) a firstPair.arriving.get(0)
						pair2.arriving.remove(0);
					}
				}
			}
		}
		return outSE;
	}

	@Override
	public String toString() {
		
		return s + "," + exclTFs + "," + pairsP;
	}

}