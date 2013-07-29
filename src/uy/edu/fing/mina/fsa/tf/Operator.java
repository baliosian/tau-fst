/*
 * Created on 07-Aug-2004
 *
 * Copyright (C) 2004 Javier Baliosian
 * All rights reserved.
 * */
package uy.edu.fing.mina.fsa.tf;

import java.io.Serializable;

/**
 * @author Javier Baliosian &lt; <a
 *         href="mailto:jbaliosian@tsc.upc.es">jbaliosian@tsc.upc.es </a>&gt;
 */
public class Operator implements Comparable<Operator>, Serializable {

    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 1L;

    public static final String AND = "∧";
    public static final String OR = "∨";
    public static final String NOT = "¬";

    public String op;

    /**
     * @param op
     */
    public Operator(String op) {
        super();
        this.op = op;
    }

    public Operator() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object arg0) {
        if (arg0 instanceof Operator) {
            Operator op2 = (Operator) arg0;
            return op.equals(op2.op);
        } else {
            return false;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return op == null ? "" : op;

    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(Operator arg0) {
        return op.compareTo(arg0.op);
    }

    public static Operator and() {
        return new Operator(Operator.AND);
    }

    public static Operator or() {
        return new Operator(Operator.OR);
    }

    public static Operator not() {
        return new Operator(Operator.NOT);
    }
}
