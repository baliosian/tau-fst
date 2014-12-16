/*
 * Created on 12-Aug-2004
 *
 * Copyright (C) 2004 Javier Baliosian
 * All rights reserved.
 * */
package uy.edu.fing.mina.fsa.test;

import uy.edu.fing.mina.fsa.logics.Implication;
import uy.edu.fing.mina.fsa.logics.Knowledge;
import uy.edu.fing.mina.fsa.logics.Utils;
import uy.edu.fing.mina.fsa.tf.SimpleTf;

/**
 * @author Javier Baliosian &lt; <a
 *         href="mailto:jbaliosian@tsc.upc.es">jbaliosian@tsc.upc.es </a>&gt;
 */

public class SimplificationWithKnowledge {

	/**
	 *
	 * @param args
	 */

	public static void main(String[] args) {
	  
		SimpleTf tfa = new SimpleTf("A");
		SimpleTf tfb = new SimpleTf("B");
		SimpleTf tfc = new SimpleTf("C");
		SimpleTf tfd = new SimpleTf("D");

	    Knowledge.implications.add(new Implication(tfa, tfb.not()));
		
		System.out.println(tfa.and(tfb));		
		System.out.println(tfa.andSimple(tfb));		

	    
	    System.out.println(
		        tfa.not()			.and(tfb.not()	.and(tfc.not()))
				.or(tfa.not()			.and(tfb.not()	.and(tfc)))
				.or(tfa.not()			.and(tfb		.and(tfd)))
				.or(tfa					.and(tfb.not()	.and(tfd.not())))
				.or(tfa					.and(tfb		.and(tfc.not())))
				.or(tfa					.and(tfb		.and(tfc.and(tfd))))
		);		
	
        System.out.println((Utils.simplify(
            tfa.not()           .and(tfb.not()  .and(tfc.not()))
            .or(tfa.not()           .and(tfb.not()  .and(tfc)))
            .or(tfa.not()           .and(tfb        .and(tfd)))
            .or(tfa                 .and(tfb.not()  .and(tfd.not())))
            .or(tfa                 .and(tfb        .and(tfc.not())))
            .or(tfa                 .and(tfb        .and(tfc.and(tfd))))
    )));        
		 

	}

}