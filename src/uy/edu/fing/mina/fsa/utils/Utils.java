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
import java.util.Random;



/**
 * @author Javier Baliosian &lt; <a
 *         href="mailto:jbaliosian@tsc.upc.es">jbaliosian@tsc.upc.es </a>&gt;
 */
public class Utils {


   public static void showDot(String dot) {
      
      String filename = "/tmp/tffst" + (new Random( (new Date()).getTime())).nextInt(1000) + ".dot";
      
      try {
         BufferedWriter out = new BufferedWriter(new FileWriter(filename));
         out.write(dot);
         out.close();
      } catch (IOException e) {}
      
      try {

        //System.out.println("Calling Dotty...");
        //String[] cmd = { "scripts/launchDotty.sh", filename };
        //String[] cmd = { "dotty", filename };
        //Process p = Runtime.getRuntime().exec(cmd);

        String cmd = "/usr/bin/dot -Tpng -o " + filename + ".png " + filename;
        System.out.println("Calling Dot: " + cmd);
        Process p = Runtime.getRuntime().exec(cmd);
        p.waitFor();
        System.out.println("return: "+ p.exitValue());
        p = Runtime.getRuntime().exec("eog " + filename + ".png ");
        //p.waitFor();
        //System.out.println("return: "+ p.exitValue());
        
      } catch (IOException | InterruptedException e) {
         System.out.println("exception happened - here's what I know: ");
         e.printStackTrace();
         System.exit(-1);
      }
      
   }

   public static void writeDot(String filename, String dot) {

      try {
         BufferedWriter out = new BufferedWriter(new FileWriter(filename));
         out.write(dot);
         out.close();
      } catch (IOException e) {}
     
   }

 
}

