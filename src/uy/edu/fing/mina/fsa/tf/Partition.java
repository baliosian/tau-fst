package uy.edu.fing.mina.fsa.tf;

import java.util.List;

public class Partition {

	public List<TfI> left = null;
	public List<TfI> right = null;
	
	public Partition(List<TfI> left, List<TfI> right) {
		super();
		this.left = left;
		this.right = right;
	}

	@Override
	public String toString() {
		return left + "-" + right;
	}
	
	
	
	
}
