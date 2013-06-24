/* 
 * es.upc.tffst
 * Copyright (C) 2004 Javier Baliosian
 * All rights reserved.
 *
 */

package uy.edu.fing.mina.fsa.tf;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import orbital.logic.imp.Formula;
import orbital.moon.logic.ClassicalLogic;
import uy.edu.fing.mina.fsa.logics.TfSymbol;

/**
 * Tautnes Function Pair. Useful for reductions of tffsts into tffsrs.
 * 
 * @author Javier Baliosian &lt; <a
 *         href="mailto:jbaliosian@tsc.upc.es">jbaliosian@tsc.upc.es </a>&gt;
 */

public class TfPair extends SimpleTf {

  /**
   * Comment for <code>serialVersionUID</code>
   */
  private static final long serialVersionUID = 1L;

  public TfI tfIn;

  public TfI tfOut;
  

  /* Constructors *********************************************************** */

  public TfPair() {
    this(SimpleTf.Epsilon(), SimpleTf.Epsilon());
  }

  public TfPair(TfI tfIn, TfI tfOut) {
    this.tfIn = tfIn;
    this.tfOut = tfOut;
    this.setTfSymbol(new TfSymbol(this));
	this.getTfSymbol().setSignifier(this.getSLabel());
    this.setFormula(this.cl.createSymbol(this.getTfSymbol()));
  }

  /* Methods ****************************************************************** */

  
  public void assign(TfPair tf) {
    this.tfIn = tf.tfIn;
    this.tfOut = tf.tfOut;
    setValue(tf.getValue());
    setNot(tf.isNot());
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#equals(java.lang.Object)
   */
  public boolean equals(Object o) {
    if (super.equals(o) && o instanceof TfPair) {
      TfPair tfp = (TfPair) o;
      if (tfIn.equals(tfp.tfIn) && tfOut.equals(tfp.tfOut)) return true;
    }
    return false;
  }

  /**
   * @return Returns the tfIn.
   */
  public TfI getTfIn() {
    return tfIn;
  }

  /**
   * @return Returns the tfOut.
   */
  public TfI getTfOut() {
    return tfOut;
  }

  /**
   * @param tfIn
   *          The tfIn to set.
   * 
   * @uml.property name="tfIn"
   */
  public void setTfIn(TfI tfIn) {
    this.tfIn = tfIn;
  }

  /**
   * @param tfOut
   *          The tfOut to set.
   * 
   * @uml.property name="tfOut"
   */
  public void setTfOut(TfI tfOut) {
    this.tfOut = tfOut;
  }

  /*
   * (non-Javadoc)
   * 
   * @see uy.edu.fing.mina.omega.tffst.utils.tf.TfI#setValue()
   */
  public void setValue() {
  }

  public String toString() {
    if (isNot()) //TODO check this.
      return "'!" + getSLabel() + "'";
    else
      return "'" + getSLabel() +"'";
  }

  /*
   * (non-Javadoc)
   * 
   * @see uy.edu.fing.mina.omega.tffst.utils.tf.TfI#size()
   */
  public int size() {
    return 1;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Comparable#compareTo(java.lang.Object)
   */
  public int compareTo(Object arg0) {
    int ret = 0;
    if (arg0 instanceof TfPair) {
      TfPair tfp = (TfPair) arg0;
      ret = this.tfIn.compareTo(tfp.tfIn);
      if (ret == 0)
        ret = this.tfOut.compareTo(tfp.tfOut);
      else
        return ret;
    } else {
      System.err.println("ERROR: comparing diferent types");
      System.exit(0);
    }
    return 1;
  }

  public static Set<TfPair> breakTfPairs(TfI tf) {
    Set<TfPair> out = new HashSet<TfPair>();
//    tf.deMorgan();

    if (tf instanceof SimpleTf) {
      if (tf instanceof TfPair) {
        TfPair tfp = (TfPair) tf;
        if (tfp.isNot()) {
          SimpleTf aa = new SimpleTf();
          aa.setAcceptAll();
          TfPair tfp1 = new TfPair(tfp.tfIn.not(), aa);
          TfPair tfp2 = new TfPair(tfp.tfIn, tfp.tfOut.not());
          out.add(tfp1);
          out.add(tfp2);
        } else
          out.add(tfp);
        return out;
      } else {
        TfPair tfp = new TfPair(tf, SimpleTf.AcceptsAll());
        out.add(tfp);
      }
    } else if (tf instanceof CompositeTf) {
      CompositeTf ctf = (CompositeTf) tf;
      if (ctf.getOperator().equals(Operator.OR)) {
        out.addAll(breakTfPairs(ctf.leftTf));
        out.addAll(breakTfPairs(ctf.rightTf));
      } else if (ctf.getOperator().equals(Operator.AND)) {

        Set<TfPair> l = breakTfPairs(ctf.leftTf);
        Set<TfPair> r = breakTfPairs(ctf.rightTf);

        for (Iterator<TfPair> iter = l.iterator(); iter.hasNext();) {
          TfPair ltfp = (TfPair) iter.next();
          for (Iterator<TfPair> iterator = r.iterator(); iterator.hasNext();) {
            TfPair rtfp = (TfPair) iterator.next();
            out.add(new TfPair(ltfp.tfIn.and(rtfp.tfIn), ltfp.tfOut.and(rtfp.tfOut)));
          }
        }
      }
    }
    return out;
  }

  @Override
  public boolean acceptsAll() {
    return false;
  }

  @Override
  public boolean acceptsNone() {
    return false;
  }

  @Override
  public String getSLabel() {
	  if (getTfIn() != null && getTfOut() != null)
		  return getTfIn().getSLabel() + "/" + getTfOut().getSLabel();
	  else if (getTfIn() != null)
		  return getTfIn().getSLabel() + "/-" ;
	  else if (getTfOut() != null)
		  return "-/" + getTfOut().getSLabel();
	  return "";
  }

  @Override
  public boolean isEpsilon() {
    return false;
  }

  public Formula getFormula() {
    return tfIn.getFormula().and(tfOut.getFormula());
  }


public boolean in(TfI tf) {

	if (this.equals(tf))
		return true;
	
	return false;
}


}