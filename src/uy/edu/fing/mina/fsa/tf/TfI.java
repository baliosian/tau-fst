/* 
 * es.upc.tffst
 * Copyright (C) 2004 Javier Baliosian
 * All rights reserved.
 *
 */

package uy.edu.fing.mina.fsa.tf;

import orbital.logic.imp.Formula;

import uy.edu.fing.mina.fsa.logics.TfSymbol;


/**
 * Tautnes Function.
 * 
 * @author Javier Baliosian &lt; <a
 *         href="mailto:jbaliosian@tsc.upc.es">jbaliosian@tsc.upc.es </a>&gt;
 */

public interface TfI extends java.io.Serializable {
  public static final float MAX_TF = 1;

  public static final float MIN_TF = -1;

  public boolean acceptsAll();

  public boolean acceptsNone();

  public TfI and(TfI tf);

  public TfI asTautas(TfI tf);

  public Object clone() throws CloneNotSupportedException;

  public int compareTo(Object arg0);

  public boolean equals(Object o);

  public int getIdentity();
  
  public TfI getIdentityTf();

  public String getSLabel();

  public boolean in(TfI tf);

  public boolean isEpsilon();

  public boolean isNot();

  public TfI not();

  public TfI or(TfI tf);

  public void setIdentity(int identity);

  public void setIdentityTf(TfI labelIn);

  public void setNot(boolean b);

  public int size();

  public TfI tauterThan(TfI tf);

  public String toString();

  public int getId();  

  public Formula getFormula();
  
  public void setFormula(Formula formula);
  
  

}