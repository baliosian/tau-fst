/* 
 * es.upc.tffst
 * Copyright (C) 2004 Javier Baliosian
 * All rights reserved.
 *
 */

package uy.edu.fing.mina.fsa.tf;

import java.util.ArrayList;
import java.util.Iterator;

import orbital.logic.imp.Formula;
import uy.edu.fing.mina.fsa.logics.Utils;

public class CompositeTf extends Tf {

  /**
   * Comment for <code>serialVersionUID</code>
   */
  private static final long serialVersionUID = 1L;

  public TfI leftTf;

  public String operator;

  public TfI rightTf;
  
  public CompositeTf() {
    super();
  }

  public CompositeTf(String op, TfI leftTf, TfI rightTf) {
    super();
    
    this.operator = op;
    this.leftTf = leftTf;
    this.rightTf = rightTf;
    
    this.getFormula();
    
//    this.setSLabel();
  }

  /*
   * Methods
   * **************************************************************************
   */

  /**
   * Builds a CompositeTF with the TFs in partitionList with the order of
   * indices
   * 
   * @param indices
   *          an array og int with the new order for partitionList
   * @param partitionList
   *          An ArrayList with the TFs to be ordered
   * @return a CompositeTF with the new permutation of TFs and an OR between all
   *         of them
   */
  public static TfI arrayLisToctf(int[] indices, ArrayList<TfI> partitionList) {

    if (indices.length == 0) return null; // this shall never happens but...

    if (indices.length == 1) return (TfI) partitionList.get(0);

    // length is at least 2
    CompositeTf ctf1 = new CompositeTf(Operator.TAUTER_THAN, (TfI) partitionList.get(indices[0]),
        (TfI) partitionList.get(indices[1]));

    for (int i = 2; i < indices.length; i++) {
      CompositeTf ctf2 = new CompositeTf(Operator.TAUTER_THAN, ctf1, (TfI) partitionList
          .get(indices[i]));
      ctf1 = ctf2;
    }

    return ctf1;
  }

  /**
   * @param tf
   * @return
   */
  public static ArrayList<TfI> ctfToArrayList(TfI tf) {
    ArrayList<TfI> listOfTfs = new ArrayList<TfI>();

    if (tf instanceof SimpleTf) {
      listOfTfs.add(tf);
      return listOfTfs;
    } else {
      if (tf instanceof CompositeTf) {
        CompositeTf ctf = (CompositeTf) tf;
        listOfTfs.addAll(ctfToArrayList(ctf.leftTf));
        listOfTfs.addAll(ctfToArrayList(ctf.rightTf));
      }
    }
    return listOfTfs;
  }

  public Object clone() throws CloneNotSupportedException {
    CompositeTf clon = (CompositeTf) super.clone();

    if (leftTf instanceof SimpleTf)
      clon.leftTf = leftTf;
    else
      clon.leftTf = (TfI) leftTf.clone();
    if (rightTf instanceof SimpleTf)
      clon.rightTf = rightTf;
    else
      clon.rightTf = (TfI) rightTf.clone();
    
    clon.operator = operator;
    
    clon.formula = formula;

    return clon;
  }

  public boolean equals(Object o) {
        if (o instanceof CompositeTf) {
            CompositeTf ctf = (CompositeTf) o;
            if (ctf.operator == operator
                    && ctf.isEpsilon() == isEpsilon()
                    && ctf.isNot() == isNot()
                    && ctf.leftTf.equals(leftTf) && ctf.rightTf.equals(rightTf))
                return true;
        }
        return false;
    }

//  public float evaluate(MessageI e) {
//
//    if (this.operator.equals(Operator.AND)) {
//      this.setValue(Math.min(this.leftTf.evaluate(e), this.rightTf.evaluate(e)));
//    } else if (this.operator.equals(Operator.OR)) {
//      this.setValue(Math.max(this.leftTf.evaluate(e), this.rightTf.evaluate(e)));
//    } else if (this.operator.equals(Operator.AS_TAUT_AS)) {
//      if (this.leftTf.evaluate(e) == this.rightTf.evaluate(e)) {
//        this.setValue(this.leftTf.evaluate(e));
//      } else {
//        this.setValue(MIN_TF);
//      }
//    } else if (this.operator.equals(Operator.TAUTER_THAN)) {
//      if (this.leftTf.evaluate(e) < this.rightTf.evaluate(e)) {
//        this.setValue(this.leftTf.evaluate(e));
//      } else {
//        this.setValue(MIN_TF);
//      }
//    }
//
//    if (this.isNot()) {
//      this.setValue(-this.getValue());
//    }
//
//    return this.getValue();
//  }

  /**
   * @return Returns the leftTf.
   */
  public TfI getLeftTf() {
    return leftTf;
  }

  /**
   * @return Returns the operator.
   */
  public String getOperator() {
    return operator;
  }

  /**
   * @return Returns the rightTf.
   */
  public TfI getRightTf() {
    return rightTf;
  }

  /**
   * @param leftTf
   *          The leftTf to set.
   */
  public void setLeftTf(TfI leftTf) {
    this.leftTf = leftTf;
  }

  /**
   * @param operator
   *          The operator to set.
   */
  public void setOperator(String operator) {
    this.operator = operator;
  }

  /**
   * @param rightTf
   *          The rightTf to set.
   */
  public void setRightTf(TfI rightTf) {
    this.rightTf = rightTf;
  }

//  /**
//   * @see uy.edu.fing.mina.omega.tffst.utils.tf.TfI#setSLabel()
//   */
//  public void setSLabel() {
//    String lab = "";
//
//    lab += "(" + (this.leftTf == null ? "null" : this.leftTf.toString()) + " " + this.operator
//        + " " + (this.rightTf == null ? "null" : this.rightTf.toString()) + ")";
//    this.setSLabel(lab);
//  }

//  /*
//   * (non-Javadoc)
//   * 
//   * @see uy.edu.fing.mina.omega.tffst.utils.tf.TfI#setValue()
//   */
//  public void setValue() {
//
//    float tf = MIN_TF;
//
//    if (this.operator.equals(Operator.AND)) {
//      this.setValue(Math.min(this.leftTf.getFixedValue(), this.rightTf.getFixedValue()));
//    } else if (this.operator.equals(Operator.OR)) {
//      this.setValue(Math.max(this.leftTf.getFixedValue(), this.rightTf.getFixedValue()));
//    } else if (this.operator.equals(Operator.AS_TAUT_AS)) {
//      if (this.leftTf.getFixedValue() == this.rightTf.getFixedValue()) {
//        this.setValue(this.leftTf.getFixedValue());
//      } else {
//        this.setValue(MIN_TF);
//      }
//    } else if (this.operator.equals(Operator.TAUTER_THAN)) {
//      if (this.leftTf.getFixedValue() < this.rightTf.getFixedValue()) {
//        this.setValue(this.leftTf.getFixedValue());
//      } else {
//        this.setValue(MIN_TF);
//      }
//    }
//    if (this.isNot()) this.setValue(-tf);
//  }

  /** */
  public String toString() {
      return isNot() ? "!" : "" + "(" + leftTf.toString() + " " + operator + " " +  rightTf.toString() + ")";
  }

  /*
   * (non-Javadoc)
   * 
   * @see uy.edu.fing.mina.omega.tffst.utils.tf.TfI#size()
   */
  public int size() {
    return this.leftTf.size() + this.rightTf.size();
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Comparable#compareTo(java.lang.Object)
   */
  public int compareTo(Object arg0) {
    int ret = 0;
    if (arg0 instanceof CompositeTf) {
      CompositeTf ctf = (CompositeTf) arg0;
      ret = this.leftTf.compareTo(ctf.leftTf);
      if (ret == 0)
        ret = this.rightTf.compareTo(ctf.rightTf);
      else
        return ret;
    } else {
      System.err.println("ERROR: comparing diferent types");
      System.exit(0);
    }
    return 1;
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
  public String getName() {
    return isNot() ? "!" : "" + "(" + leftTf.getName() + " " + operator + " " +  rightTf.getName() + ")";
  }

  @Override
  public boolean isEpsilon() {
    return false;
  }

  public Formula getFormula() {
    if (operator.equals(Operator.AND))
      this.formula = leftTf.getFormula().and(rightTf.getFormula());
    if (operator.equals(Operator.OR))
      this.formula = leftTf.getFormula().or(rightTf.getFormula());
    return this.formula;
  }


public boolean in(TfI tf) {

    tf = Utils.simplify(tf);
    TfI simplythis = Utils.simplify(this);
    if (simplythis.equals(tf)) return true;
    else if (tf.acceptsAll()) return true;
    else if (simplythis.and(tf).equals(tf)) return true;
    
    return false;
}

@Override
public int hashCode() {
    return (leftTf.hashCode() + rightTf.hashCode() + operator.hashCode())%Integer.MAX_VALUE;
}

/**
  * @param b
  * @param operator
  * @return
  */
 public static TfI listToCTF(String operator, ArrayList<TfI> rightArray) {
    
    ArrayList<TfI> auxArrayList = new ArrayList<TfI>();
    
    for (Iterator<TfI> iter = rightArray.iterator(); iter.hasNext();) {
       TfI o = iter.next();
       if (o != null)
          auxArrayList.add(o);
    }
    
    rightArray = auxArrayList;
    
    Iterator<TfI> raIter = rightArray.iterator();
    TfI partitionR1 = null;
    
    Object o1 = null;
    Object o2 = null;

    if (raIter.hasNext()) {
       o1 = raIter.next();
    }
    if (raIter.hasNext()) {
       o2 = raIter.next();
    }
    if (o1 == null)
       return null;
    else if (o2 == null)
       return (TfI) o1;
    else {
       if (operator.equals(Operator.AND))
          partitionR1 = ((TfI)o1).and((TfI) o2);
       else if (operator.equals(Operator.OR))
          partitionR1 = ((TfI)o1).or((TfI) o2);
       
       while (raIter.hasNext()) {
          Object o = raIter.next();
          if (o != null) {
             TfI partitionR2 = null;
             if (operator.equals(Operator.AND))
                partitionR1 = partitionR1.and((TfI) o);
             else if (operator.equals(Operator.OR))
                partitionR2 = partitionR1.or((TfI) o);
             else if (operator.equals(Operator.AS_TAUT_AS))

             if (partitionR2 instanceof CompositeTf) {
                CompositeTf ctfPartitionR2 = (CompositeTf) partitionR2;
                partitionR1 = ctfPartitionR2;
             }
          }
       }
       
       return partitionR1;
    }
 }




 }