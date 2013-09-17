/* 
 * es.upc.tffst
 * Copyright (C) 2004 Javier Baliosian
 * All rights reserved.
 *
 */

package uy.edu.fing.mina.fsa.tf;

import java.util.ArrayList;
import java.util.Iterator;

import uy.edu.fing.mina.fsa.logics.Utils;

public class CompositeTf extends Tf {

  /**
   * Comment for <code>serialVersionUID</code>
   */
  private static final long serialVersionUID = 1L;

  public TfI left;

  public String op;

  public TfI right;
  
  public CompositeTf() {
    super();
  }

  public CompositeTf(String op, TfI leftTf, TfI rightTf) {
    super();
    
    this.op = op;
    this.left = leftTf;
    this.right = rightTf;
    
  }

  /*
   * Methods
   * **************************************************************************
   */


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
        listOfTfs.addAll(ctfToArrayList(ctf.left));
        listOfTfs.addAll(ctfToArrayList(ctf.right));
      }
    }
    return listOfTfs;
  }

  public Object clone() throws CloneNotSupportedException {
    CompositeTf clon = (CompositeTf) super.clone();

    if (left instanceof SimpleTf)
      clon.left = left;
    else
      clon.left = (TfI) left.clone();
    if (right instanceof SimpleTf)
      clon.right = right;
    else
      clon.right = (TfI) right.clone();
    
    clon.op = op;
    
//    clon.formula = formula;

    return clon;
  }

  public boolean equals(Object o) {
	if (o instanceof CompositeTf) {
	  CompositeTf ctf = (CompositeTf) o;

	  CompositeTf simpleThis = (CompositeTf) Utils.simplify(this);
	  CompositeTf simpleThat = (CompositeTf) Utils.simplify(ctf);

	  if (simpleThis.op == simpleThat.op && simpleThis.isEpsilon() == simpleThat.isEpsilon() && simpleThis.isNot() == simpleThat.isNot()
		  && ((simpleThis.left.equals(simpleThat.left) && simpleThis.right.equals(simpleThat.right))
		  || (simpleThis.right.equals(simpleThat.left) && simpleThis.left.equals(simpleThat.right))))
		return true;
	}
	return false;
  }

  /**
   * @return Returns the leftTf.
   */
  public TfI getLeftTf() {
    return left;
  }

  /**
   * @return Returns the operator.
   */
  public String getOperator() {
    return op;
  }

  /**
   * @return Returns the rightTf.
   */
  public TfI getRightTf() {
    return right;
  }

  /**
   * @param leftTf
   *          The leftTf to set.
   */
  public void setLeftTf(TfI leftTf) {
    this.left = leftTf;
  }

  /**
   * @param operator
   *          The operator to set.
   */
  public void setOperator(String operator) {
    this.op = operator;
  }

  /**
   * @param rightTf
   *          The rightTf to set.
   */
  public void setRightTf(TfI rightTf) {
    this.right = rightTf;
  }

  /** 
   *
   * 
   */
  public String toString() {
      return (isNot() ? Operator.NOT : "") + "(" + left.toString() + op + right.toString() + ")";
  }

  /*
   * (non-Javadoc)
   * 
   * @see uy.edu.fing.mina.omega.tffst.utils.tf.TfI#size()
   */
  public int size() {
    return this.left.size() + this.right.size();
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
      ret = this.left.compareTo(ctf.left);
      if (ret == 0)
        ret = this.right.compareTo(ctf.right);
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
    String ret = super.getName();
    return ret + "(" + left.getName() + op + right.getName() + ")";
  }

  @Override
  public boolean isEpsilon() {
    return false;
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
    return (left.hashCode() + right.hashCode() + op.hashCode())%Integer.MAX_VALUE;
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

             if (partitionR2 instanceof CompositeTf) {
                CompositeTf ctfPartitionR2 = (CompositeTf) partitionR2;
                partitionR1 = ctfPartitionR2;
             }
          }
       }
       
       return partitionR1;
    }
 }

 
  /**
   * Recursively uses the de Morgan theorem to push NOTs to the leaves. Recurses
   * all the way to the leaves and works backwards, i.e. depth-first processing
   * 
   * <ul>
   * <li>NOT(a AND b) -> NOT(a) OR NOT(b)</li>
   * <li>NOT(a OR b) -> NOT(a) AND NOT(b)</li>
   * </ul>
   * 
   * @param expression
   * @return
   */
  public CompositeTf pushNotsDown() {
    CompositeTf ret = new CompositeTf();
    try {
      if (this.not) {
        if (left instanceof SimpleTf) 
          ret.left = ((SimpleTf) left.clone()).not();
        else 
          ret.left = ((CompositeTf) left.not()).pushNotsDown();
        
        if (right instanceof SimpleTf) 
          ret.right = ((SimpleTf) right.clone()).not();
        else 
          ret.right = ((CompositeTf) right.not()).pushNotsDown();

        if (op.equals(Operator.AND)) 
          ret.op = Operator.OR;
        else 
          ret.op = Operator.AND;
        return ret;
      } else {
        if (left instanceof SimpleTf) 
          ret.left = ((SimpleTf) left.clone());
        else 
          ret.left = ((CompositeTf) left).pushNotsDown();
        
        if (right instanceof SimpleTf) 
          ret.right = ((SimpleTf) right.clone());
        else 
          ret.right = ((CompositeTf) right).pushNotsDown();
        ret.op = op;
        return ret;
      }
    } catch (CloneNotSupportedException e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * 
   * transform this tf to its dnf. assumes all negatives where pushed down to
   * the leaves and aplies 
   * 
   * a∧(b∨c)=(a∧b)∨(a∧c) 
   * (a∨b)∧(c∨d)=((a∧c)v(a∧d))v((b∧c)v(b∧d))
   * 
   * @return
   */
  
  public CompositeTf pushORsUP() {

    CompositeTf ret = new CompositeTf();

    try {
      if (op.equals(Operator.AND)) {
        if (left instanceof CompositeTf) {
          ret.left = ((CompositeTf) left).pushORsUP();
          if (right instanceof CompositeTf) {
            ret.right = ((CompositeTf) right).pushORsUP();
            if (((CompositeTf) ret.left).op.equals(Operator.OR)) {
              if (((CompositeTf) ret.right).op.equals(Operator.OR)) {
                //  (a∨b)∧(c∨d)=((a∧c)v(a∧d))v((b∧c)v(b∧d)) 
                CompositeTf newl  = new CompositeTf();
                CompositeTf newr  = new CompositeTf();
                CompositeTf newll = new CompositeTf();
                CompositeTf newlr = new CompositeTf();
                CompositeTf newrl = new CompositeTf();
                CompositeTf newrr = new CompositeTf();
                
                newll.left  = ((CompositeTf) ret.left).left; 
                newll.right = ((CompositeTf) ret.right).left; 
                newll.op    = Operator.AND;

                newlr.left  = ((CompositeTf) ret.left).left; 
                newlr.right = ((CompositeTf) ret.right).right; 
                newlr.op    = Operator.AND;
                
                newrl.left  = ((CompositeTf) ret.left).right; 
                newrl.right = ((CompositeTf) ret.right).left; 
                newrl.op    = Operator.AND;
                
                newrr.left  = ((CompositeTf) ret.left).right; 
                newrr.right = ((CompositeTf) ret.right).right; 
                newrr.op    = Operator.AND;
                
                newl.left  = newll;
                newl.right = newlr;
                newl.op    = Operator.OR;

                newr.left  = newrl;
                newr.right = newrr;
                newr.op    = Operator.OR;
                
                ret.left  = newl;
                ret.right = newr;
                ret.op    = Operator.OR;
                
                return ret;
              } else { 
                //  (a∨b)∧(c∧d)=(a∧(c∧d))v(b∧(c∧d)) 
                
                CompositeTf newl  = new CompositeTf();
                CompositeTf newr  = new CompositeTf();
                
                CompositeTf newlr = new CompositeTf();
                CompositeTf newrr = new CompositeTf();
                
                newlr.left  = ((CompositeTf) ret.right).left; 
                newlr.right = ((CompositeTf) ret.right).right; 
                newlr.op    = Operator.AND;
                
                newrr.left  = ((CompositeTf) ret.right).left; 
                newrr.right = ((CompositeTf) ret.right).right; 
                newrr.op    = Operator.AND;
                
                newl.left  = ((CompositeTf) ret.left).left;
                newl.right = newlr;
                newl.op    = Operator.AND;

                newr.left  = ((CompositeTf) ret.left).right;
                newr.right = newrr;
                newr.op    = Operator.AND;
                
                ret.left  = newl;
                ret.right = newr;
                ret.op    = Operator.OR;
                
                return ret;
              }
            } else { //left is not OR 
              if (((CompositeTf) ret.right).op.equals(Operator.OR)) {
                //  (a∧b)∧(cvd)=((a∧b)∧c)v((a∧b)∧d) 
                
                CompositeTf newl  = new CompositeTf();
                CompositeTf newr  = new CompositeTf();
                
                CompositeTf newll = new CompositeTf();
                CompositeTf newrl = new CompositeTf();
                
                newll.left  = ((CompositeTf) ret.left).left; 
                newll.right = ((CompositeTf) ret.left).right; 
                newll.op    = Operator.AND;
                
                newrl.left  = ((CompositeTf) ret.left).left; 
                newrl.right = ((CompositeTf) ret.left).right; 
                newrl.op    = Operator.AND;
                
                newl.left  = newll;
                newl.right = ((CompositeTf) ret.right).left;
                newl.op    = Operator.AND;

                newr.left  = newrl;
                newr.right = ((CompositeTf) ret.right).right;
                newr.op    = Operator.AND;
                
                ret.left  = newl;
                ret.right = newr;
                ret.op    = Operator.OR;
                
                return ret;
              } else { // both are AND  
                ret.op = op;
                return ret; 
              }
            } 
          } else { // right is simple
            ret.right = (SimpleTf) right.clone();
            if (((CompositeTf) ret.left).op.equals(Operator.OR)) {
              // (b∨c)∧a=(a∧b)∨(a∧c)
              
              CompositeTf newltf = new CompositeTf();
              newltf.left = ret.right;
              newltf.right = ((CompositeTf) ret.left).left;
              newltf.op = Operator.AND;
              
              CompositeTf newrtf = new CompositeTf();
              newrtf.left = ret.right;
              newrtf.right = ((CompositeTf) ret.left).right;
              newrtf.op = Operator.AND;
              
              ret.left = newltf;
              ret.right = newrtf;
              ret.op = Operator.OR;
              
              return ret;

            } else { // both are AND
              ret.op = op;
              return ret;
            }
          }
        } else { // left is simple
          ret.left = (SimpleTf) left.clone();
          if (right instanceof CompositeTf) {
            ret.right = ((CompositeTf) right).pushORsUP();
            if (((CompositeTf) ret.right).op.equals(Operator.OR)) {
              //a∧(b∨c)=(a∧b)∨(a∧c) 
              
              CompositeTf newltf = new CompositeTf();
              newltf.left = ret.left;
              newltf.right = ((CompositeTf) ret.right).left;
              newltf.op = Operator.AND;
              
              CompositeTf newrtf = new CompositeTf();
              newrtf.left = ret.left;
              newrtf.right = ((CompositeTf) ret.right).right;
              newrtf.op = Operator.AND;
              
              ret.left = newltf;
              ret.right = newrtf;
              ret.op = Operator.OR;
              
              return ret;

            } else { // right is AND 
              ret.op = op;
              return ret;
            }
          } else { // both are simple
            ret.right = (SimpleTf) right.clone();
            ret.op = op;
            return ret;
          }
        }
      } else {
        return (CompositeTf) this.clone();
      }
    } catch (CloneNotSupportedException e) {
      e.printStackTrace();
      return null;
    }
  }

  public CompositeTf toDNF() {
    CompositeTf ctf = pushNotsDown();
    return ctf.pushORsUP();
  }
  
 }