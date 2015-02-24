package uy.edu.fing.mina.lupa.tf;

import java.util.HashSet;
import java.util.Set;

import uy.edu.fing.mina.fsa.tf.SimpleTf;

public class EventTf extends SimpleTf {
	

	private static final long serialVersionUID = 1L;

	public EventTf() {
	}

	private Filter filter=new Filter();
	
	private LuaPredicate luaPredicate=new LuaPredicate();
	
	public float tautnessValue;
	
	private Set<String> init_notif_strings=new HashSet<String>();
	private Set<String> init_subs_strings=new HashSet<String>();

	public Filter getFilter() {
		return filter;
	}
	
	public Set<String> getInitNotifStrings() {
		return init_notif_strings;
	}
	
	public void addInitNotifString(String newString){
		init_notif_strings.add(newString);
	}

	public Set<String> getInitSubsStrings() {

		return init_subs_strings;
	}
	
	public void addInitSubsString(String newString){
		init_subs_strings.add(newString);
	}

	public LuaPredicate getLuaPredicate() {
		return luaPredicate;
	}
	
}
