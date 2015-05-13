/*
 * Created on 12-Aug-2004
 *
 * Copyright (C) 2004 Javier Baliosian
 * All rights reserved.
 * */
package uy.edu.fing.mina.papers.interoperability;

import java.util.Set;

import uy.edu.fing.mina.fsa.logics.Implication;
import uy.edu.fing.mina.fsa.logics.Knowledge;
import uy.edu.fing.mina.fsa.tf.Tf;
import uy.edu.fing.mina.fsa.tffsr.Tffsr;
import uy.edu.fing.mina.fsa.tffst.State;
import uy.edu.fing.mina.fsa.tffst.Tffst;
import uy.edu.fing.mina.fsa.tffst.Transition;
import uy.edu.fing.mina.fsa.utils.Utils;

/**
 * @author Javier Baliosian &lt; <a
 *         href="mailto:jbaliosian@tsc.upc.es">jbaliosian@tsc.upc.es </a>&gt;
 */
public class map_rule2_composition extends Tffst {

  Tffst t = new Tffst();

  /**
   * @param obj
   * @return
   * @see java.lang.Object#equals(java.lang.Object)
   */
  public boolean equals(Object obj) {
	return t.equals(obj);
  }

  /**
   * @param tffsr
   * @see uy.edu.fing.mina.fsa.tffst.Tffst#assign(uy.edu.fing.mina.fsa.tffst.Tffst)
   */
  public void assign(Tffst tffsr) {
	t.assign(tffsr);
  }

  /**
   * @return
   * @throws CloneNotSupportedException
   * @see uy.edu.fing.mina.fsa.tffst.Tffst#clone()
   */
  public Tffst clone() throws CloneNotSupportedException {
	return t.clone();
  }

  /**
   * @param theOther
   * @return
   * @see uy.edu.fing.mina.fsa.tffst.Tffst#composition(uy.edu.fing.mina.fsa.tffst.Tffst)
   */
  public Tffst composition(Tffst theOther) {
	return t.composition(theOther);
  }

  /**
   * @param a
   * @return
   * @see uy.edu.fing.mina.fsa.tffst.Tffst#concatenate(uy.edu.fing.mina.fsa.tffst.Tffst)
   */
  public Tffst concatenate(Tffst a) {
	return t.concatenate(a);
  }

  /**
   * 
   * @see uy.edu.fing.mina.fsa.tffst.Tffst#determinize()
   */
  public void determinize() {
	t.determinize();
  }

  /**
   * 
   * @see uy.edu.fing.mina.fsa.tffst.Tffst#removeInputEpsilonLabel()
   */
  public void removeInputEpsilonLabel() {
	t.removeInputEpsilonLabel();
  }

  /**
   * @return
   * @see uy.edu.fing.mina.fsa.tffst.Tffst#firstProjection()
   */
  public Tffsr firstProjection() {
	return t.firstProjection();
  }

  /**
   * @return
   * @see uy.edu.fing.mina.fsa.tffst.Tffst#getAcceptStates()
   */
  public Set<State> getAcceptStates() {
	return t.getAcceptStates();
  }

  /**
   * @return
   * @see uy.edu.fing.mina.fsa.tffst.Tffst#getInfo()
   */
  public Object getInfo() {
	return t.getInfo();
  }

  /**
   * @return
   * @see uy.edu.fing.mina.fsa.tffst.Tffst#getInitialState()
   */
  public State getInitialState() {
	return t.getInitialState();
  }

  /**
   * @return
   * @see uy.edu.fing.mina.fsa.tffst.Tffst#getLiveStates()
   */
  public Set<State> getLiveStates() {
	return t.getLiveStates();
  }

  /**
   * @return
   * @see uy.edu.fing.mina.fsa.tffst.Tffst#getNumberOfStates()
   */
  public int getNumberOfStates() {
	return t.getNumberOfStates();
  }

  /**
   * @return
   * @see uy.edu.fing.mina.fsa.tffst.Tffst#getStates()
   */
  public Set<State> getStates() {
	return t.getStates();
  }

  /**
   * @return
   * @see uy.edu.fing.mina.fsa.tffst.Tffst#getTransitions()
   */
  public Set<Transition> getTransitions() {
	return t.getTransitions();
  }

  /**
   * @return
   * @see uy.edu.fing.mina.fsa.tffst.Tffst#hashCode()
   */
  public int hashCode() {
	return t.hashCode();
  }

  /**
   * @return
   * @see uy.edu.fing.mina.fsa.tffst.Tffst#isDeterministic()
   */
  public boolean isDeterministic() {
	return t.isDeterministic();
  }

  /**
   * @return
   * @see uy.edu.fing.mina.fsa.tffst.Tffst#isEmpty()
   */
  public boolean isEmpty() {
	return t.isEmpty();
  }

  /**
   * @return
   * @see uy.edu.fing.mina.fsa.tffst.Tffst#isFinite()
   */
  public boolean isFinite() {
	return t.isFinite();
  }

  /**
   * @return
   * @see uy.edu.fing.mina.fsa.tffst.Tffst#kleene()
   */
  public Tffst kleene() {
	return t.kleene();
  }

  /**
   * @param min
   * @return
   * @see uy.edu.fing.mina.fsa.tffst.Tffst#kleene(int)
   */
  public Tffst kleene(int min) {
	return t.kleene(min);
  }

  /**
   * @param min
   * @param max
   * @return
   * @see uy.edu.fing.mina.fsa.tffst.Tffst#kleene(int, int)
   */
  public Tffst kleene(int min, int max) {
	return t.kleene(min, max);
  }

  /**
   * 
   * @see uy.edu.fing.mina.fsa.tffst.Tffst#minimize()
   */
  public void minimize() {
	t.minimize();
  }

  /**
   * @param deterministic
   * @see uy.edu.fing.mina.fsa.tffst.Tffst#setDeterministic(boolean)
   */
  public void setDeterministic(boolean deterministic) {
	t.setDeterministic(deterministic);
  }

  /**
   * @param info
   * @see uy.edu.fing.mina.fsa.tffst.Tffst#setInfo(java.lang.Object)
   */
  public void setInfo(Object info) {
	t.setInfo(info);
  }

  /**
   * @param s
   * @see uy.edu.fing.mina.fsa.tffst.Tffst#setInitialState(uy.edu.fing.mina.fsa.tffst.State)
   */
  public void setInitialState(State s) {
	t.setInitialState(s);
  }

  /**
   * @param label
   * @return
   * @see uy.edu.fing.mina.fsa.tffst.Tffst#toDot(java.lang.String)
   */
  public String toDot(String label) {
	return t.toDot(label);
  }

  /**
   * @return
   * @see uy.edu.fing.mina.fsa.tffst.Tffst#toSingleLabelTransitions()
   */
  public Tffst toSingleLabelTransitions() {
	return t.toSingleLabelTransitions();
  }

  /**
   * @return
   * @see uy.edu.fing.mina.fsa.tffst.Tffst#toSingleInputLabelTransitions()
   */
  public Tffst toSingleInputLabelTransitions() {
	return t.toSingleInputLabelTransitions();
  }

  /**
   * 
   * @see uy.edu.fing.mina.fsa.tffst.Tffst#simplifyTransitionLabels()
   */
  public void simplifyTransitionLabels() {
	t.simplifyTransitionLabels();
  }

  /**
   * @return
   * @see uy.edu.fing.mina.fsa.tffst.Tffst#toTffsr()
   */
  public Tffsr toTffsr() {
	return t.toTffsr();
  }

  /**
   * @param t
   * @return
   * @see uy.edu.fing.mina.fsa.tffst.Tffst#union(uy.edu.fing.mina.fsa.tffst.Tffst)
   */
  public Tffst union(Tffst t) {
	return t.union(t);
  }

  /**
   * @return
   * @see uy.edu.fing.mina.fsa.tffst.Tffst#toString()
   */
  public String toString() {
	return t.toString();
  }

  /**
   * 
   * @see uy.edu.fing.mina.fsa.tffst.Tffst#removeDeadTransitions()
   */
  public void removeDeadTransitions() {
	t.removeDeadTransitions();
  }

  /**
   * @return
   * @see uy.edu.fing.mina.fsa.tffst.Tffst#totalize()
   */
  public Tffst totalize() {
	return t.totalize();
  }

  /**
   * @return
   * @see uy.edu.fing.mina.fsa.tffst.Tffst#totalizeInput()
   */
  public Tffst totalizeInput() {
	return t.totalizeInput();
  }

  /**
   * @param b
   * @return
   * @see uy.edu.fing.mina.fsa.tffst.Tffst#intersection(uy.edu.fing.mina.fsa.tffst.Tffst)
   */
  public Tffst intersection(Tffst b) {
	return t.intersection(b);
  }

  /**
   * @return
   * @see uy.edu.fing.mina.fsa.tffst.Tffst#complement()
   */
  public Tffst complement() {
	return t.complement();
  }

  /**
   * 
   * @see uy.edu.fing.mina.fsa.tffst.Tffst#renumerateStateLabels()
   */
  public void renumerateStateLabels() {
	t.renumerateStateLabels();
  }

  public map_rule2_composition() {


	Tffst sp = new simplepermission_h2();

	Utils.showDot(sp.toDot("rule1"));

	Tffst map = new map_h1_h2_2();

	Utils.showDot(map.toDot("map"));
	
	Knowledge.implications.add(new Implication(Tf.createdTFs.get("read"),Tf.createdTFs.get("man")));

	Knowledge.implications.add(new Implication(Tf.createdTFs.get("man"),Tf.createdTFs.get("rec").not()));
	Knowledge.implications.add(new Implication(Tf.createdTFs.get("man"),Tf.createdTFs.get("phy").not()));
	Knowledge.implications.add(new Implication(Tf.createdTFs.get("man"),Tf.createdTFs.get("hp").not()));
	Knowledge.implications.add(new Implication(Tf.createdTFs.get("rec"),Tf.createdTFs.get("hp").not()));
	Knowledge.implications.add(new Implication(Tf.createdTFs.get("rec"),Tf.createdTFs.get("phy").not()));
	Knowledge.implications.add(new Implication(Tf.createdTFs.get("hp"),Tf.createdTFs.get("phy").not()));

	System.out.println(Tf.createdTFs.get("phy").andSimple(Tf.createdTFs.get("hp")));
	
	t = composition(sp, map);

	t.renumerateStateLabels();
  }

  public static void main(String[] args) {

	map_rule2_composition c = new map_rule2_composition();

	Utils.showDot(c.toDot("", "LR"), "/home/javier/Dropbox/sharelatex/interoperability/map_rule2_composition");

  }

}