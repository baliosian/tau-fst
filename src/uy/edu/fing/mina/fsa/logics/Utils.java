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
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import orbital.logic.imp.Formula;
import orbital.moon.logic.ClassicalLogic;
import orbital.moon.logic.resolution.DefaultClausalFactory;
import uy.edu.fing.mina.fsa.logics.quineMcCluskey.QmcFormula;
import uy.edu.fing.mina.fsa.logics.quineMcCluskey.Term;
import uy.edu.fing.mina.fsa.logics.quineMcCluskey.TfTerm;
import uy.edu.fing.mina.fsa.tf.CompositeTf;
import uy.edu.fing.mina.fsa.tf.Operator;
import uy.edu.fing.mina.fsa.tf.SimpleTf;
import uy.edu.fing.mina.fsa.tf.TfI;

/**
 * @author Javier Baliosian &lt; <a
 *         href="mailto:jbaliosian@tsc.upc.es">jbaliosian@tsc.upc.es </a>&gt;
 */
public class Utils {

    public static TfI conjunctiveForm(TfI tf) {

        TfI tfsimplified = tf;

        if (!(tf.acceptsAll() || tf.acceptsNone())) {
            Formula formula = Utils.toFormula(tf);
            DefaultClausalFactory dcf = new DefaultClausalFactory();
            formula = dcf.asClausalSet(formula).toFormula();
            tfsimplified = Utils.toTF(formula);
        }
        return tfsimplified;
    }

    public static TfI disjunctiveForm(TfI tf) {

        TfI tfsimplified = tf;

        if (!(tf.acceptsAll() || tf.acceptsNone())) {
            Formula formula = Utils.toFormula(tf);
            formula = ClassicalLogic.Utilities.disjunctiveForm(formula, true);
            tfsimplified = Utils.toTF(formula);
        }
        return tfsimplified;
    }

    public static TfI disjunctiveFormByMua(TfI tf) {

      TfI dnftf = null;
      
      if (tf instanceof CompositeTf)
        dnftf = ((CompositeTf)tf).toDNF();

      //TODO add other tupes of Tfs. 
      
      return dnftf;
  }

    
    /**
     * Creates a orbital.logic.imp.Formula representing the given TF. It would
     * be usefull for compacting the TF using Orbital library.
     * 
     * @param tf
     * @return a Formula
     */

    public static Formula toFormula(TfI tf) {

        ClassicalLogic cl = new ClassicalLogic();
        Formula out = null;

        if (tf instanceof SimpleTf) {
            SimpleTf stf = (SimpleTf) tf;
            out = cl.createSymbol(stf.getTfSymbol());
        } else if (tf instanceof CompositeTf) {
            CompositeTf ctf = (CompositeTf) tf;

            Formula fLeft = toFormula(ctf.left);
            Formula fRight = toFormula(ctf.right);

            if (ctf.getOperator().equals(Operator.AND))
                out = fLeft.and(fRight);
            else if (ctf.getOperator().equals(Operator.OR))
                out = fLeft.or(fRight);
            else
                System.err.println("ERROR: operator different of AND and OR");
        }

        if (tf.isNot())
            out = out.not();

        return out;
    }

    /**
     * 
     * @param tfFormula
     * @return
     */
    public static TfI toTF(Formula tfFormula) {

        if (tfFormula instanceof Formula.Composite) {
            Formula.Composite fcomp = (Formula.Composite) tfFormula;

            // obtains the operator
            Object o = fcomp.getCompositor();

            if (o.toString().equals(TfSymbol.orbitAND)) {
                CompositeTf ctf = new CompositeTf();
                ctf.setOperator(Operator.AND);
                Object o1 = fcomp.getComponent();
                if (o1 instanceof Formula[]) {
                    Formula[] comps = (Formula[]) o1;
                    ctf.setLeftTf(toTF(comps[0]));
                    ctf.setRightTf(toTF(comps[1]));
                }

                return ctf;

            } else if (o.toString().equals(TfSymbol.orbitOR)) {
                CompositeTf ctf = new CompositeTf();
                ctf.setOperator(Operator.OR);
                Object o1 = fcomp.getComponent();
                if (o1 instanceof Formula[]) {
                    Formula[] comps = (Formula[]) o1;
                    ctf.setLeftTf(toTF(comps[0]));
                    ctf.setRightTf(toTF(comps[1]));
                }

                return ctf;

            } else if (o.toString().equals(TfSymbol.orbitNOT)) {
                TfI tf;
                Object o1 = fcomp.getComponent();
                if (o1 instanceof Formula) {
                    Formula comp = (Formula) o1;
                    tf = toTF(comp);

                    return tf.not();

                }
            }

        } else if (tfFormula instanceof Formula) {
            Formula fas = (Formula) tfFormula;
            // obtains the TfSymbols
            Set sfas = fas.getVariables();
            if (sfas.size() == 1) {
                Iterator iter = sfas.iterator();
                TfSymbol tfs = (TfSymbol) iter.next();
                return tfs.getTf();
            } else if (fas.toString().equals("false")) {
                SimpleTf stf = new SimpleTf();
                stf.setAcceptNone();

                return stf;

            } else if (fas.toString().equals("true")) {
                SimpleTf stf = new SimpleTf();
                stf.setAcceptAll();

                return stf;

            } else {
                System.err.println("Utils.toTF()");
                System.err.println("ERROR: bad formula structure.");
            }
        }
        return null;
    }

    /*
     * @see uy.edu.fing.mina.omega.tffst.utils.tf.TfI#simplify()
     */
  public static TfI simplifyByMua(TfI tf) {

    // TfI simplifiedTf = disjunctiveForm(tf);
    TfI simplifiedTf = disjunctiveFormByMua(tf);

    List<Term> termList = toTermList(simplifiedTf);
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

  public static TfI simplify(TfI tf) {

    TfI simplifiedTf = disjunctiveForm(tf);

    List<Term> termList = toTermList(simplifiedTf);
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

    private static TfI termsListToTf(List<Term> termList) {
        
        TfI out = null; 
        
        for (Iterator<Term> iterator = termList.iterator(); iterator.hasNext();) {
            Term term = (Term) iterator.next();
            if (out== null)
                out = termToTf(term);
            else 
                out = new CompositeTf(Operator.OR,out, termToTf(term));         
        }   
                
        return out;
    }

    private static TfI termToTf(Term term) {

        TfI out = null;

        for (int i = 0; i < term.varVals.length; i++) {
            TfTerm tfterm = term.varVals[i];
            if (tfterm.b == 0)
                // assumes the and is simplified already
                if (out==null)
                    out = tfterm.tf.not();
                else
                    out = new CompositeTf(Operator.AND, out, tfterm.tf.not());
            else if (tfterm.b == 1)
                // assumes the and is simplified already
                if (out==null)
                    out = tfterm.tf;
                else
                    out = new CompositeTf(Operator.AND, out, tfterm.tf);
        }
        
        if (out == null) return SimpleTf.AcceptsNone();
        else             return out;
        
    }

    /**
     * transform a TF in a list of terms ready to be minimized by
     * Quine-McCluskey
     * 
     * @param tf
     *            has to be in DNF
     * @return
     */
    public static List<Term> toTermList(TfI tf) {

        List<Term> termList = new ArrayList<Term>();

        if (tf instanceof CompositeTf) {
            if (((CompositeTf) tf).op.equals(Operator.AND)) {

                TfTerm[] tftermarrayL = toTermList(((CompositeTf) tf).left).get(0).varVals;
                TfTerm[] tftermarrayR = toTermList(((CompositeTf) tf).right).get(0).varVals;

                TfTerm[] tftermarray = new TfTerm[tftermarrayL.length + tftermarrayR.length];
                System.arraycopy(tftermarrayL, 0, tftermarray, 0,tftermarrayL.length);
                System.arraycopy(tftermarrayR, 0, tftermarray, tftermarrayL.length, tftermarrayR.length);
                termList.add(new Term(tftermarray));

            } else if (((CompositeTf) tf).op.equals(Operator.OR)) {
                termList = toTermList(((CompositeTf) tf).left);
                termList.addAll(toTermList(((CompositeTf) tf).right));
            }
        } else if (tf instanceof SimpleTf) {
            TfTerm[] tftermarray = new TfTerm[1];
            if (tf.isNot())
                tftermarray[0] = new TfTerm((SimpleTf) tf.not(), (byte) 0);
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
            if (term.varVals.length > start + 1 )
                expandDontCares(out, start + 1 );
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
                tfMap.put(tfterm.tf.toString(), new TfTerm(tfterm.tf, TfTerm.DontCare.b));
            }
        }

        // until here we have a map with all variables -- JB

        for (Iterator iterator = termList.iterator(); iterator.hasNext();) {
            Term term = (Term) iterator.next();
            HashMap<String, TfTerm> tfMapTemp = (HashMap<String, TfTerm>) tfMap.clone();
            boolean skip = false;
            for (int i = 0; i < term.varVals.length; i++) {
                TfTerm tfterm = term.varVals[i];
                if (tfMapTemp.get(tfterm.tf.toString()).b == TfTerm.DontCare.b)
                    tfMapTemp.put(tfterm.tf.toString(), tfterm);
                else if (tfMapTemp.get(tfterm.tf.toString()).b != tfterm.b)
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
        return o1.tf.toString().compareTo(o2.tf.toString());
    }

}
