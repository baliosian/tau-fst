/* 
 * es.upc.tffst
 * Copyright (C) 2004 Javier Baliosian
 * All rights reserved.
 *
 */

package uy.edu.fing.mina.fsa.tf;

public class SimpleTf extends Tf {

  public static final String EPSILON = "(e)";
  public static final String ALL = "ALL";
  public static final String NONE = "NONE";
  
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
        stfepsilon.setName(SimpleTf.EPSILON);
        return stfepsilon;
    }

    private boolean isEpsilon = false;

    private String name = null;

    public SimpleTf() {
        super();
        setName(SimpleTf.ALL);
    }
    
    public SimpleTf(String sLabel) {
      this();
      setName(sLabel);
    }
    
    public boolean acceptsAll() {
        return getName().equals("All");
    }

    public boolean acceptsNone() {
        return getName().equals("None");
    }

    public Object clone() throws CloneNotSupportedException {
        SimpleTf clon = (SimpleTf) super.clone();
        clon.name = name;
        if (this.isEpsilon())
            clon.setEpsilon();
        else if (this.acceptsAll())
            clon.setAcceptAll();
        else if (this.acceptsNone())
            clon.setAcceptNone();
        return clon;
    }
    
    /**
     * Compares this object with the specified object for order. 
     */
    public int compareTo(Object o) {
      if (o instanceof SimpleTf) {
        SimpleTf stf = (SimpleTf) o;
        return this.getName().compareTo(stf.getName());
        
      }
        return Integer.MIN_VALUE;
    }

    public boolean isEpsilon() {
        return isEpsilon;
    }

    public void setAcceptAll() {
        setName(SimpleTf.ALL);
    }

    /**
     * 
     */
    public void setAcceptNone() {
        setName(SimpleTf.NONE);
    }

    public void setEpsilon() {
        isEpsilon = true;
        setName(SimpleTf.EPSILON);
    }

    public void setName(String label) {
        name = label;
    }

    /**
     * @see uy.edu.fing.mina.omega.tffst.utils.tf.TfI#size()
     */
    public int size() {
        return 1;
    }

    public String toString() {
        String out = getName();
        if (getIdentityTf() != null)
            out = "<" + out + ">";
        return out;
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

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
      final int prime = 31;
      int result = super.hashCode();
      result = prime * result + (isEpsilon ? 1231 : 1237);
      result = prime * result + ((name == null) ? 0 : getName().hashCode());
      return result;
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj) return true;
      if (!super.equals(obj)) return false;
      if (obj instanceof SimpleTf) {
            SimpleTf otherStf = (SimpleTf) obj;
            if (this.not == otherStf.not
                    && this.isEpsilon == otherStf.isEpsilon
                    && this.getName().equals(otherStf.getName())
                    && this.acceptsAll() == otherStf.acceptsAll()
                    && this.acceptsNone() == otherStf.acceptsNone())
                return true;
        }
        return false;
    }

    /* (non-Javadoc)
     * @see uy.edu.fing.mina.fsa.tf.Tf#getName()
     */
    @Override
    public String getName() {
      return super.not ? "!" + name : name;
    }
    
}