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
import uy.edu.fing.mina.fsa.tf.SimpleTf;
import uy.edu.fing.mina.fsa.tf.TfI;
import uy.edu.fing.mina.fsa.tffsr.Tffsr;
import uy.edu.fing.mina.fsa.tffst.State;
import uy.edu.fing.mina.fsa.tffst.Tffst;
import uy.edu.fing.mina.fsa.utils.Utils;

/**
 * @author Javier Baliosian &lt; <a
 *         href="mailto:jbaliosian@tsc.upc.es">jbaliosian@tsc.upc.es </a>&gt;
 */
public class simpleprohibition extends Tffst {

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
  public Set<uy.edu.fing.mina.fsa.tffst.Transition> getTransitions() {
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

  public simpleprohibition() {


	t = new Tffst();
	
//	TfI r = new SimpleTf("phy");
//	TfI a = new SimpleTf("man");
//	TfI v = new SimpleTf("rek");

	TfI r = new SimpleTf("r");
	TfI a = new SimpleTf("a");
	TfI v = new SimpleTf("v");
	
	Knowledge.implications.add(new Implication(r, a.not()));
	Knowledge.implications.add(new Implication(r, v.not()));
	Knowledge.implications.add(new Implication(a, v.not()));

	//at least once
	Tffsr fsr = new Tffsr();
	
	uy.edu.fing.mina.fsa.tffsr.State s0 = new uy.edu.fing.mina.fsa.tffsr.State();
	uy.edu.fing.mina.fsa.tffsr.State s1 = new uy.edu.fing.mina.fsa.tffsr.State();
	uy.edu.fing.mina.fsa.tffsr.State s2 = new uy.edu.fing.mina.fsa.tffsr.State();
	uy.edu.fing.mina.fsa.tffsr.State s3 = new uy.edu.fing.mina.fsa.tffsr.State();
	
	fsr.setInitialState(s0);
	s3.setAccept(true);
	
	s0.addOutTran(new uy.edu.fing.mina.fsa.tffsr.Transition(r, s1));
	s0.addOutTran(new uy.edu.fing.mina.fsa.tffsr.Transition(r.not(), s0));
	s1.addOutTran(new uy.edu.fing.mina.fsa.tffsr.Transition(a, s2));
	s1.addOutTran(new uy.edu.fing.mina.fsa.tffsr.Transition(a.not(), s0));
	s2.addOutTran(new uy.edu.fing.mina.fsa.tffsr.Transition(v, s3));
	s2.addOutTran(new uy.edu.fing.mina.fsa.tffsr.Transition(v.not(), s0));
	
	s3.addOutTran(new uy.edu.fing.mina.fsa.tffsr.Transition(r.not(), s3));
	s3.addOutTran(new uy.edu.fing.mina.fsa.tffsr.Transition(r, s3));

	s3.addOutTran(new uy.edu.fing.mina.fsa.tffsr.Transition(a.not(), s3));
	s3.addOutTran(new uy.edu.fing.mina.fsa.tffsr.Transition(a, s3));
	
	s3.addOutTran(new uy.edu.fing.mina.fsa.tffsr.Transition(v.not(), s3));
	s3.addOutTran(new uy.edu.fing.mina.fsa.tffsr.Transition(v, s3));

	//complement, no one
	fsr = fsr.complement();
	Utils.showDot(fsr.toDot("no one"));
	
	// no one to epsilon
	Tffst fstnoone = fsr.identity();
	
	Tffsr fsrjustit = new Tffsr();
	
	uy.edu.fing.mina.fsa.tffsr.State s01 = new uy.edu.fing.mina.fsa.tffsr.State();
	uy.edu.fing.mina.fsa.tffsr.State s11 = new uy.edu.fing.mina.fsa.tffsr.State();
	uy.edu.fing.mina.fsa.tffsr.State s21 = new uy.edu.fing.mina.fsa.tffsr.State();
	uy.edu.fing.mina.fsa.tffsr.State s31 = new uy.edu.fing.mina.fsa.tffsr.State();

	fsrjustit.setInitialState(s01);
	s31.setAccept(true);
	
	s01.addOutTran(new uy.edu.fing.mina.fsa.tffsr.Transition(r, s11));
	s11.addOutTran(new uy.edu.fing.mina.fsa.tffsr.Transition(a, s21));
	s21.addOutTran(new uy.edu.fing.mina.fsa.tffsr.Transition(v, s31));
	
	Tffst fstceroormany = fsrjustit.toEpsilon().kleene();
	Utils.showDot(fstceroormany.toDot("cero or many"));

	t = fstnoone.concatenate(fstceroormany).kleene();

	t.renumerateStateLabels();
  }

  public static void main(String[] args) {

	simpleprohibition c = new simpleprohibition();

	Utils.showDot(c.toDot("", "LR"), "/home/javier/Dropbox/sharelatex/interoperability/simpleprohibition");

  }

}