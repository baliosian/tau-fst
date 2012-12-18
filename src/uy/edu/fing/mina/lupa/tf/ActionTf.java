package uy.edu.fing.mina.lupa.tf;

import uy.edu.fing.mina.fsa.tf.SimpleTf;

public class ActionTf extends SimpleTf {

	private static final long serialVersionUID = 1L;
	
	private String luaCode;

	public ActionTf() {
		super();
	}
	
	public ActionTf(String luaCode) {
		super();
		this.luaCode = luaCode;
	}

	public String getLuaCode() {
		return luaCode;
	}

	public void setLuaCode(String luaCode) {
		this.luaCode = luaCode;
	}


}
