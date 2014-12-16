/*
 * Created on 24-Aug-2004
 *
 * Copyright (C) 2004 Javier Baliosian
 * All rights reserved.
 * */
package uy.edu.fing.mina.fsa.logics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import uy.edu.fing.mina.fsa.logics.quineMcCluskey.QmcFormula;
import uy.edu.fing.mina.fsa.logics.quineMcCluskey.Term;
import uy.edu.fing.mina.fsa.logics.quineMcCluskey.TfTerm;
import uy.edu.fing.mina.fsa.tf.CompositeTf;
import uy.edu.fing.mina.fsa.tf.Operator;
import uy.edu.fing.mina.fsa.tf.SimpleTf;
import uy.edu.fing.mina.fsa.tf.TfI;
import uy.edu.fing.mina.fsa.tf.TfPair;

/**
 * @author Javier Baliosian &lt; <a
 *         href="mailto:jbaliosian@tsc.upc.es">jbaliosian@tsc.upc.es </a>&gt;
 */
public class Utils {

  public static TfI disjunctiveFormByMua(TfI tf) {

	TfI dnftf = null;

	if (tf instanceof CompositeTf)
	  dnftf = ((CompositeTf) tf).toDNF();
	else if (tf instanceof SimpleTf)
	  try {
		dnftf = ((SimpleTf) tf).clone();
	  } catch (CloneNotSupportedException e) {
		e.printStackTrace();
	  }
	else if (tf instanceof TfPair)
	  try {
		dnftf = ((TfPair) tf).clone();
	  } catch (CloneNotSupportedException e) {
		e.printStackTrace();
	  }

	return dnftf;
  }

  /*
   * @see uy.edu.fing.mina.omega.tffst.utils.tf.TfI#simplify()
   */
  public static TfI simplify(TfI tf) {

    // TfI simplifiedTf = disjunctiveForm(tf);
    TfI simplifiedTf = disjunctiveFormByMua(tf);

    List<Term> termList = toTermList(simplifiedTf);
    
    if (Knowledge.implications != null) termList = applyKnowledge(termList);
    
    if (termList.size() > 0) {
      // termList = expandDontCares(termList,0);
      QmcFormula qmcf = new QmcFormula(termList);
      qmcf.reduceToPrimeImplicants();
      qmcf.reducePrimeImplicantsToSubset();
      simplifiedTf = termsListToTf(qmcf.termList);
    } else
      return SimpleTf.AcceptsNone();

    return simplifiedTf;

  }

  // at this point I know that each term has all possible Tfs in the formula
  private static List<Term> applyKnowledge(List<Term> termList) {

	
	Term[] at = (Term[])((ArrayList<Term>) termList).toArray(new Term[termList.size()]);
	Set<Implication> tosubstitute = new HashSet<Implication>();
	
	// knowledge is A -> B,
	// term has A and B 
	//      A=1, B=1 in a term, then B must be set as dontcare
	//      A=1, B=0 in a term, then the term should be removed
	//      A=0, B=1 in a term, know nothing
	//      A=0, B=0 in a term, know nothing
	//
	// 	TODO	A=2, B=1 in a term, then all occurrences of A in all the terms have to be substituted by B.
	// 	TODO	A=2, B=0 in a term, know nothing
	// 	TODO	A=1, B=2 in a term, know nothing
	// 	TODO	A=0, B=2 in a term, know nothing
	// knowledge is A -> -B,
	// term has A and B  
	//      A=1, B=1 in a term, then the term should be removed
	//      A=1, B=0 in a term, then B must be set as dontcare
	//      A=0, B=1 in a term, then A must be set as dontcare
	//      A=0, B=0 in a term, know nothing
	//
	// 	TODO	A=2, B=1 in a term, 
	// 	TODO	A=2, B=0 in a term, 
	// 	TODO	A=1, B=2 in a term, 
	// 	TODO	A=0, B=2 in a term, 
	// knowledge is -A -> B,
	// term has A and B  
	//      A=1, B=1 in a term, know nothing
	//      A=1, B=0 in a term, know nothing
	//      A=0, B=1 in a term, then B must be set as dontcare
	//      A=0, B=0 in a term, then the term should be removed
	//
	// TODO		A=2, B=1 in a term, 
	// TODO		A=2, B=0 in a term, 
	// TODO		A=1, B=2 in a term, 
	// TODO		A=0, B=2 in a term, 
	// knowledge is -A -> -B,
	// term has A and B 
	//      A=1, B=1 in a term, know nothing
	//      A=1, B=0 in a term, know nothing
	//      A=0, B=1 in a term, then the term should be removed
	//      A=0, B=0 in a term, then B must be set as dontcare
	//
	// 	TODO	A=2, B=1 in a term, 
	// 	TODO	A=2, B=0 in a term, 
	// 	TODO	A=1, B=2 in a term, 
	// 	TODO	A=0, B=2 in a term, 

	
	
	for (Implication impl : Knowledge.implications) {
	  // knowledge is A -> B,
	  if (!impl.all.isNot() && !impl.are.isNot()) {
		for (int i = 0; i < at.length; i++) {
		  if (at[i] != null) {
			TfTerm all = null;
			TfTerm are = null;
			for (TfTerm tft : at[i].varVals) {
			  if (impl.all.equals(tft.tf) && tft.b != TfTerm.DontCare.b)
				all = tft;
			  if (impl.are.equals(tft.tf) && tft.b != TfTerm.DontCare.b)
				are = tft;
			}
			if (all != null && are != null) {
			  if (all.b == 1 && are.b == 1)
				are.b = TfTerm.DontCare.b;
			  if (all.b == 1 && are.b == 0)
				at[i] = null;
			}
		  }
		}
	  }
	  if (!impl.all.isNot() && impl.are.isNot()) {
		for (int i = 0; i < at.length; i++) {
		  if (at[i] != null) {
			TfTerm all = null;
			TfTerm are = null;
			for (TfTerm tft : at[i].varVals) {
			  if (impl.all.equals(tft.tf) && tft.b != TfTerm.DontCare.b)
				all = tft;
			  if (impl.are.equals(tft.tf.not()) && tft.b != TfTerm.DontCare.b)
				are = tft;
			}
			if (all != null && are != null) {
			  if (all.b == 1 && are.b == 1)
				at[i] = null;
			  if (all.b == 1 && are.b == 0)
				are.b = TfTerm.DontCare.b;
			  if (all.b == 0 && are.b == 1)
				all.b = TfTerm.DontCare.b;
			}
		  }
		}
	  }
	  if (impl.all.isNot() && !impl.are.isNot()) {
		for (int i = 0; i < at.length; i++) {
		  if (at[i] != null) {
			TfTerm all = null;
			TfTerm are = null;
			for (TfTerm tft : at[i].varVals) {
			  if (impl.all.equals(tft.tf.not()) && tft.b != TfTerm.DontCare.b)
				all = tft;
			  if (impl.are.equals(tft.tf) && tft.b != TfTerm.DontCare.b)
				are = tft;
			}
			if (all != null && are != null) {
			  if (all.b == 0 && are.b == 1)
				are.b = TfTerm.DontCare.b;
			  if (all.b == 0 && are.b == 0)
				at[i] = null;
			}
		  }
		}
	  }
	  if (impl.all.isNot() && impl.are.isNot()) {
		for (int i = 0; i < at.length; i++) {
		  if (at[i] != null) {
			TfTerm all = null;
			TfTerm are = null;
			for (TfTerm tft : at[i].varVals) {
			  if (impl.all.equals(tft.tf.not()) && tft.b != TfTerm.DontCare.b)
				all = tft;
			  if (impl.are.equals(tft.tf.not()) && tft.b != TfTerm.DontCare.b)
				are = tft;
			}
			if (all != null && are != null) {
			  if (all.b == 0 && are.b == 1)
				at[i] = null;
			  if (all.b == 0 && are.b == 0)
				are.b = TfTerm.DontCare.b;
			}
		  }
		}
	  }
	}
		
	List<Term> outTermList = new ArrayList<Term>();
	
	for (int i = 0; i < at.length; i++) {
	  if (at[i] != null) {
		for (Implication imp : tosubstitute) {
		  for (TfTerm tft : at[i].varVals) {
			if (imp.all.equals(tft.tf) && tft.b == 1)
			  tft.b = TfTerm.DontCare.b;
			if (imp.are.equals(tft.tf))
			  tft.b = 1;
		  }
		}
		outTermList.add(at[i]);
	  }
	}
	return outTermList;
  }

  private static TfI termsListToTf(List<Term> termList) {

    TfI out = null;

    for (Iterator<Term> iterator = termList.iterator(); iterator.hasNext();) {
      Term term = (Term) iterator.next();
      if (out == null) out = termToTf(term);
      else
        out = new CompositeTf(Operator.OR, out, termToTf(term));
    }

    return out;
  }

  private static TfI termToTf(Term term) {

    TfI out = null;

    for (int i = 0; i < term.varVals.length; i++) {
      TfTerm tfterm = term.varVals[i];
      if (tfterm.b == 0)
      // assumes the and is simplified already
      if (out == null) out = tfterm.tf.not();
      else
        out = new CompositeTf(Operator.AND, out, tfterm.tf.not());
      else if (tfterm.b == 1)
      // assumes the and is simplified already
        if (out == null) out = tfterm.tf;
        else
          out = new CompositeTf(Operator.AND, out, tfterm.tf);
    }

    if (out == null) return SimpleTf.AcceptsNone();
    else
      return out;

  }

  /**
   * transform a TF in a list of terms ready to be minimized by Quine-McCluskey
   * 
   * @param tf
   *          has to be in DNF
   * @return
   */
  public static List<Term> toTermList(TfI tf) {

    List<Term> termList = new ArrayList<Term>();

    if (tf instanceof CompositeTf) {
      if (((CompositeTf) tf).op.equals(Operator.AND)) {

        TfTerm[] tftermarrayL = toTermList(((CompositeTf) tf).left).get(0).varVals;
        TfTerm[] tftermarrayR = toTermList(((CompositeTf) tf).right).get(0).varVals;

        TfTerm[] tftermarray = new TfTerm[tftermarrayL.length + tftermarrayR.length];
        System.arraycopy(tftermarrayL, 0, tftermarray, 0, tftermarrayL.length);
        System.arraycopy(tftermarrayR, 0, tftermarray, tftermarrayL.length, tftermarrayR.length);
        termList.add(new Term(tftermarray));

      } else if (((CompositeTf) tf).op.equals(Operator.OR)) {
        termList = toTermList(((CompositeTf) tf).left);
        termList.addAll(toTermList(((CompositeTf) tf).right));
      }
    } else if (tf instanceof SimpleTf) {
      TfTerm[] tftermarray = new TfTerm[1];
      if (tf.isNot()) tftermarray[0] = new TfTerm((SimpleTf) tf.not(), (byte) 0);
      else
        tftermarray[0] = new TfTerm((SimpleTf) tf, (byte) 1);
      termList.add(new Term(tftermarray));
    }

    termList = completeTermList(termList);

    return termList;
  }

  private static List<Term> expandDontCares(List<Term> termList, int start) {

    List<Term> out = new ArrayList<Term>();
    boolean expand = false;

    for (Iterator<Term> iterator = termList.iterator(); iterator.hasNext();) {
      Term term = (Term) iterator.next();

      Term t0 = new Term(term.varVals.clone());
      Term t1 = new Term(term.varVals.clone());
      expand = false;

      TfTerm tfterm = term.varVals[start];
      if (tfterm.b == TfTerm.DontCare.b) {
        expand = true;
        t0.varVals[start] = new TfTerm(tfterm.tf, (byte) 0);
        t1.varVals[start] = new TfTerm(tfterm.tf, (byte) 1);
      }

      if (expand) {
        out.add(t0);
        out.add(t1);
      } else
        out.add(term);
      if (term.varVals.length > start + 1) expandDontCares(out, start + 1);
    }
    return out;
  }

  @SuppressWarnings("unchecked")
  public static List<Term> completeTermList(List<Term> termList) {

    List<Term> out = new ArrayList<Term>();
    HashMap<String, TfTerm> tfMap = new HashMap<String, TfTerm>();

    for (Iterator iterator = termList.iterator(); iterator.hasNext();) {
      Term term = (Term) iterator.next();
      for (int i = 0; i < term.varVals.length; i++) {
        TfTerm tfterm = term.varVals[i];
        tfMap.put(tfterm.tf.getName(), new TfTerm(tfterm.tf, TfTerm.DontCare.b));
      }
    }

    // until here we have a map with all variables -- JB

    for (Iterator iterator = termList.iterator(); iterator.hasNext();) {
      Term term = (Term) iterator.next();
      HashMap<String, TfTerm> tfMapTemp = (HashMap<String, TfTerm>) tfMap.clone();
      boolean skip = false;
      for (int i = 0; i < term.varVals.length; i++) {
        TfTerm tfterm = term.varVals[i];
        if (tfMapTemp.get(tfterm.tf.getName()).b == TfTerm.DontCare.b) 
          tfMapTemp.put(tfterm.tf.getName(), tfterm);
        else if (tfMapTemp.get(tfterm.tf.getName()).b != tfterm.b) 
          //term and its negation are both in the term list. 
          skip = true;
      }
      if (!skip) {
        TfTerm[] tftermarray = tfMapTemp.values().toArray(new TfTerm[tfMapTemp.values().size()]);
        Arrays.sort(tftermarray, new TfTermsComparator());
        out.add(new Term(tftermarray));
      }
    }
    return out;
  }

}

class TfTermsComparator implements Comparator<TfTerm> {

  public int compare(TfTerm o1, TfTerm o2) {
    return o1.tf.getName().compareTo(o2.tf.getName());
  }

}
