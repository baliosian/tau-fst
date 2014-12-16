package uy.edu.fing.mina.fsa.logics.quineMcCluskey;

import uy.edu.fing.mina.fsa.tf.SimpleTf;
import uy.edu.fing.mina.fsa.tf.TfI;

public class TfTerm {

  public TfTerm(TfI t, byte i) {
	b = i;
	this.tf = t;
  }

  public byte b;
  public TfI tf;

  public static final TfTerm DontCare = new TfTerm(SimpleTf.AcceptsNone(), (byte) 2);

  @Override
  protected Object clone() throws CloneNotSupportedException {
	return new TfTerm(this.tf, this.b);
  }

  @Override
  public boolean equals(Object obj) {
	if (obj instanceof TfTerm) {
	  if (this.b == ((TfTerm) obj).b && this.tf.equals(((TfTerm) obj).tf))
		return true;
	}
	return false;
  }

  @Override
  public String toString() {
	return this.tf.toString() + this.b;
  }

  public void setB(byte b) {
	this.b = b;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + b + tf.hashCode();
	return result;
  }

}
