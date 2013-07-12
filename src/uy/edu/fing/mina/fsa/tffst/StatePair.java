/* 
 *
 * dk.brics.automaton
 * Copyright (C) 2001-2004 Anders Moeller
 * All rights reserved.
 *
 *
 */

package uy.edu.fing.mina.fsa.tffst;

/**
 * Pair of states.
 * 
 * @author Anders M&oslash;ller &lt; <a
 *         href="mailto:amoeller@brics.dk">amoeller@brics.dk </a>&gt;
 */

public class StatePair {

   /**
    * 
    * @uml.property name="s"
    * @uml.associationEnd multiplicity="(0 1)"
    */
   State s;

   /**
    * 
    * @uml.property name="s1"
    * @uml.associationEnd multiplicity="(0 1)"
    */
   State s1;

   /**
    * 
    * @uml.property name="s2"
    * @uml.associationEnd multiplicity="(0 1)"
    */
   State s2;

   StatePair(State s, State s1, State s2) {
      this.s = s;
      this.s1 = s1;
      this.s2 = s2;
   }

   /**
    * Constructs new state pair.
    * 
    * @param s1
    *           first state
    * @param s2
    *           second state
    */
   public StatePair(State s1, State s2) {
      this.s1 = s1;
      this.s2 = s2;
   }

   /**
    * Returns first component of this pair.
    * 
    * @return first state
    */
   public State getFirstState() {
      return s1;
   }

   /**
    * Returns second component of this pair.
    * 
    * @return second state
    */
   public State getSecondState() {
      return s2;
   }

  /* (non-Javadoc)
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((s == null) ? 0 : s.hashCode());
    result = prime * result + ((s1 == null) ? 0 : s1.hashCode());
    result = prime * result + ((s2 == null) ? 0 : s2.hashCode());
    return result;
  }

  /* (non-Javadoc)
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (!(obj instanceof StatePair)) return false;
    StatePair other = (StatePair) obj;
    if (s == null) {
      if (other.s != null) return false;
    } else if (!s.equals(other.s)) return false;
    if (s1 == null) {
      if (other.s1 != null) return false;
    } else if (!s1.equals(other.s1)) return false;
    if (s2 == null) {
      if (other.s2 != null) return false;
    } else if (!s2.equals(other.s2)) return false;
    return true;
  }

   /**
    * Checks for equality.
    * 
    * @param obj
    *           object to compare with
    * @return true if <tt>obj</tt> represents the same pair of states as this
    *         pair
    */
//   public boolean equals(Object obj) {
//      if (obj instanceof StatePair) {
//         StatePair p = (StatePair) obj;
//         return p.s1.equals(s1) && p.s2.equals(s2);
//      } else
//         return false;
//   }
//
//   /**
//    * Returns hash code.
//    * 
//    * @return hash code
//    */
//   public int hashCode() {
//      return s1.hashCode() + s2.hashCode();
//   }
   
   
   
   
}