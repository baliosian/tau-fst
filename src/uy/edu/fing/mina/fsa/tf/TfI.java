/* 
 * es.upc.tffst
 * Copyright (C) 2004 Javier Baliosian
 * All rights reserved.
 *
 */

package uy.edu.fing.mina.fsa.tf;

import java.util.Set;



/**
 * Tautnes Function.
 * 
 * @author Javier Baliosian &lt; <a
 *         href="mailto:jbaliosian@tsc.upc.es">jbaliosian@tsc.upc.es </a>&gt;
 */

public interface TfI extends java.io.Serializable {
  public static final float MAX_TF = 1;

  public static final float MIN_TF = 0;

  public boolean acceptsAll();

  public boolean acceptsNone();

  public TfI not();

  public TfI or(TfI tf);

  public TfI and(TfI tf);

  public TfI orSimple(TfI tf);

  public TfI andSimple(TfI tf);

  public Object clone() throws CloneNotSupportedException;

  public int compareTo(Object arg0);

  public int getIdentityType();

  public void setIdentityType(int i);

  public String getName();

  public boolean in(TfI tf);

  public boolean isEpsilon();

  public boolean isNot();

  public void setNot(boolean b);

  public int size();
  
  public String toString();

  public int getId();  

  public TfI refersTo(); 
  
  public void setRefersTo(TfI refersTo);
  
  public boolean satisfiable();
  
  public boolean equals(Object o);
  
  public int hashCode();
  
  public Set<TfI> getWeight();
  
  public void addWeight(TfI tf);
  
}