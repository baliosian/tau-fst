package uy.edu.fing.mina.lupa.tf;

import java.util.HashSet;
import java.util.Set;

import uy.edu.fing.mina.fsa.tf.SimpleTf;

public class EventTf extends SimpleTf {
	

	private static final long serialVersionUID = 1L;

	public EventTf() {
	}

	public EventTf(float value) {
		super(value);
	}

	private Filter filter=new Filter();
	
	private LuaPredicate luaPredicate=new LuaPredicate();
	
	public float tautnessValue;
	
	private Set<String> init_strings=new HashSet<String>();

	public Filter getFilter() {
		return filter;
	}
	
	public Set<String> getInitStrings() {
		return init_strings;
	}
	
	public void addInitString(String newString){
		init_strings.add(newString);
	}

	public LuaPredicate getLuaPredicate() {
		return luaPredicate;
	}
	
}
