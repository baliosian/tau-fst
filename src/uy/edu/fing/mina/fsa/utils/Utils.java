/*
 * Created on 26-Jul-2004
 *
 * Copyright (C) 2004 Javier Baliosian
 * All rights reserved.
 * */
package uy.edu.fing.mina.fsa.utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import uy.edu.fing.mina.fsa.tf.TfString;
import uy.edu.fing.mina.fsa.tffst.Tffst;
import uy.edu.fing.mina.fsa.tffst.Transition;

/**
 * @author Javier Baliosian &lt; <a
 *         href="mailto:jbaliosian@tsc.upc.es">jbaliosian@tsc.upc.es </a>&gt;
 */
public class Utils {

  public static void showDot(String dot) {
	String filename = "/tmp/tffst" + (new Random((new Date()).getTime())).nextInt(1000) + ".dot";
	showDot(dot, filename);
  }

  public static void showDot(String dot, String filename) {
	showDot(dot, filename, "png");  
  }
  
  public static void showDot(String dot, String filename, String format) {

	writeDot(dot, filename);

	try {
	  String cmd = "/usr/bin/dot -T" + format + " -o " + filename + "." + format + " " + filename;
	  System.out.println("Calling Dot: " + cmd);
	  Process p = Runtime.getRuntime().exec(cmd);
	  p.waitFor();
	  System.out.println("return: " + p.exitValue());
	  p = Runtime.getRuntime().exec("eog " + filename + "." + format + " ");
	} catch (IOException | InterruptedException e) {
	  System.out.println("exception happened - here's what I know: ");
	  e.printStackTrace();
	  System.exit(-1);
	}

  }

  public static void writeDot(String dot, String filename) {

	try {
	  BufferedWriter out = new BufferedWriter(new FileWriter(filename));
	  out.write(dot);
	  out.close();
	} catch (IOException e) {
	}

  }

  public static void writeLabels(String filename, Tffst tffst) {
	Set<TfString> outLabels = new HashSet<TfString>();
	try {
	  BufferedWriter out = new BufferedWriter(new FileWriter(filename));
	  Set<Transition> trans = tffst.getTransitions();
	  for (Transition transition : trans)
		outLabels.add(transition.getLabelOut());
	  for (TfString tfs : outLabels)
		out.write(tfs.toString() + "\n");
	  out.close();
	} catch (IOException e) {
	}

  }

}
