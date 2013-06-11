/* 
 * es.upc.tffst
 * Copyright (C) 2004 Javier Baliosian
 * All rights reserved.
 *
 */

package uy.edu.fing.mina.fsa.tf;

import orbital.logic.imp.Formula;
import orbital.moon.logic.ClassicalLogic;
import uy.edu.fing.mina.fsa.logics.TfSymbol;
import uy.edu.fing.mina.fsa.utils.Configuration;

public class SimpleTf extends Tf {

    private static int sequence = 0;

    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 1L;

    public static SimpleTf AcceptsAll() {
        SimpleTf stfall = new SimpleTf();
        stfall.setAcceptAll();
        return stfall;
    }

    public static SimpleTf AcceptsNone() {
        SimpleTf stfnone = new SimpleTf();
        stfnone.setAcceptNone();
        return stfnone;
    }

    public static SimpleTf Epsilon() {
        SimpleTf stfepsilon = new SimpleTf();
        stfepsilon.isEpsilon = true;
        stfepsilon.setSLabel(Configuration.getString("SimpleTf.epsilon"));
        return stfepsilon;
    }

    private boolean isEpsilon = false;

    private int number;

    private String sLabel = null;

    private TfSymbol tfSymbol = null;
    
    public ClassicalLogic cl = new ClassicalLogic();


    public SimpleTf() {
        super();
        this.number = sequence;
        this.tfSymbol = new TfSymbol(this);
        setSLabel("All");
        sequence++;
    }

    public SimpleTf(float value) {
        this();
        this.setValue(value);
    }

    public boolean acceptsAll() {
        return getSLabel().equals("All");
    }

    public boolean acceptsNone() {
        return getSLabel().equals("None");
    }

    public Object clone() throws CloneNotSupportedException {
        SimpleTf clon = (SimpleTf) super.clone();
        if (this.isEpsilon())
            clon.setEpsilon();
        else if (this.acceptsAll())
            clon.setAcceptAll();
        else if (this.acceptsNone())
            clon.setAcceptNone();
        
        clon.setTfSymbol(this.tfSymbol);
        clon.setFormula(this.formula);
        
        return clon;
    }

    /**
     * Compares this object with the specified object for order. States are
     * ordered by the time of construction.
     */
    public int compareTo(Object o) {
        return ((SimpleTf) o).number - number;
    }


    /**
     * @see uy.edu.fing.mina.omega.tffst.utils.tf.TfI#getSlabel()
     */
    public String getSLabel() {
        if (this.isNot()) {
            return "!"+this.sLabel;
        } else {
            return this.sLabel;
        }
    }

    /**
     * @return Returns the tfSymbol.
     */
    public TfSymbol getTfSymbol() {
        return tfSymbol;
    }

    public boolean isEpsilon() {
        return isEpsilon;
    }

    public void setAcceptAll() {
        setSLabel("All");
        this.tfSymbol.setSignifier(this.getSLabel());
    }

    /**
     * 
     */
    public void setAcceptNone() {
        setSLabel("None");
        this.tfSymbol.setSignifier(this.getSLabel());
    }

    public void setEpsilon() {
        isEpsilon = true;
        setSLabel(Configuration.getString("SimpleTf.epsilon"));
    }

    public void setSLabel(String label) {
        sLabel = label;
        this.tfSymbol.setSignifier(this.getSLabel());
        this.setFormula(cl.createSymbol(this.getTfSymbol()));
    }


    /**
     * @param tfSymbol
     *            The tfSymbol to set.
     */
    public void setTfSymbol(TfSymbol tfSymbol) {
        this.tfSymbol = tfSymbol;
        this.setFormula(cl.createSymbol(this.getTfSymbol()));
    }

    /**
     * @see uy.edu.fing.mina.omega.tffst.utils.tf.TfI#size()
     */
    public int size() {
        return 1;
    }

    public String toString() {
        String out = getSLabel();
        if (getIdentity() == 1)
            out = "<" + out + ">";
        return out;
    }

  public Formula getFormula() {
    return this.formula;
  }


    @Override
    public boolean equals(Object obj) {
        if (obj instanceof SimpleTf) {
            SimpleTf otherStf = (SimpleTf) obj;
            if (this.not == otherStf.not
                    && this.isEpsilon == otherStf.isEpsilon
                    && this.tfSymbol.equals(otherStf.tfSymbol)
                    && this.acceptsAll() == otherStf.acceptsAll()
                    && this.acceptsNone() == otherStf.acceptsNone())
                return true;
        }
        return false;
    }

    public boolean in(TfI tf) {
        
        //TODO faltan casos
        
        if (this.equals(tf))
            return true;
        else if (this.acceptsNone())
            return true;
        else if (tf.acceptsAll())
            return true;
        else if (this.and(tf).equals(this))
            return true;
        else        
            return false;
    }

    @Override
    public int hashCode() {
        return getTfSymbol().hashCode();
    }



}