/*
 * Created on 12-Aug-2004
 *
 * Copyright (C) 2004 Javier Baliosian
 * All rights reserved.
 * */
package uy.edu.fing.mina.fsa.test;

import uy.edu.fing.mina.fsa.logics.Utils;
import uy.edu.fing.mina.fsa.tf.SimpleTf;

/**
 * @author Javier Baliosian &lt; <a
 *         href="mailto:jbaliosian@tsc.upc.es">jbaliosian@tsc.upc.es </a>&gt;
 */

public class QuineMcCluskey {

	/**
	 * uy.edu.fing.mina.omega.tffst.test 5, is the very example of the
	 * policy's paper. it works well but A <-> B and B <-> A must be unified
	 * 
	 * @param args
	 */

	public static void main(String[] args) {

		SimpleTf tfa = new SimpleTf();
		tfa.setName("A");

		SimpleTf tfb = new SimpleTf();
		tfb.setName("B");

		SimpleTf tfc = new SimpleTf();
		tfc.setName("C");

		SimpleTf tfd = new SimpleTf();
		tfd.setName("D");


//		System.out.println((
//		        tfa.not()	.and(tfb.not()	.and(tfc.not()	.and(tfd.not())))
//			.or(tfa.not()	.and(tfb.not()	.and(tfc.not()	.and(tfd))))
//			.or(tfa.not()	.and(tfb.not()	.and(tfc		.and(tfd.not()))))
//			.or(tfa.not()	.and(tfb.not()	.and(tfc		.and(tfd))))
//			.or(tfa.not()	.and(tfb		.and(tfc.not()	.and(tfd))))
//			.or(tfa.not()	.and(tfb		.and(tfc		.and(tfd))))
//			.or(tfa			.and(tfb.not()	.and(tfc.not()	.and(tfd.not()))))
//			.or(tfa			.and(tfb.not()	.and(tfc		.and(tfd.not()))))
//			.or(tfa			.and(tfb		.and(tfc.not()	.and(tfd.not()))))
//			.or(tfa			.and(tfb		.and(tfc.not()	.and(tfd))))
//			.or(tfa			.and(tfb		.and(tfc		.and(tfd))))
//		));		
//	
//		System.out.println(Utils.simplify(
//		        tfa.not()	.and(tfb.not()	.and(tfc.not()	.and(tfd.not())))
//			.or(tfa.not()	.and(tfb.not()	.and(tfc.not()	.and(tfd))))
//			.or(tfa.not()	.and(tfb.not()	.and(tfc		.and(tfd.not()))))
//			.or(tfa.not()	.and(tfb.not()	.and(tfc		.and(tfd))))
//			.or(tfa.not()	.and(tfb		.and(tfc.not()	.and(tfd))))
//			.or(tfa.not()	.and(tfb		.and(tfc		.and(tfd))))
//			.or(tfa			.and(tfb.not()	.and(tfc.not()	.and(tfd.not()))))
//			.or(tfa			.and(tfb.not()	.and(tfc		.and(tfd.not()))))
//			.or(tfa			.and(tfb		.and(tfc.not()	.and(tfd.not()))))
//			.or(tfa			.and(tfb		.and(tfc.not()	.and(tfd))))
//			.or(tfa			.and(tfb		.and(tfc		.and(tfd))))
//		));		
	
/*
 * 
{A0 B0 C0 }
{A0 B0 C1 }
{A0 B1 D1 }
{A1 B0 D0 }
{A1 B1 C0}
{A1 B1 C1 D1 }
 * 
 */
		
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

		

//		System.out.println((
//			    tfa.not()	.and(tfb.not()	.and(tfc.not()	.and(tfd.not())))
//			.or(tfa			.and(tfb.not()	.and(tfc.not()	.and(tfd.not()))))
//			.or(tfa.not()	.and(tfb		.and(tfc.not()	.and(tfd.not()))))
//			.or(tfa			.and(tfb.not()	.and(tfc		.and(tfd.not()))))
//			.or(tfa.not()	.and(tfb		.and(tfc		.and(tfd.not()))))
//			.or(tfa			.and(tfb		.and(tfc		.and(tfd.not()))))
//			.or(tfa.not()	.and(tfb.not()	.and(tfc.not()	.and(tfd))))
//			.or(tfa			.and(tfb.not()	.and(tfc.not()	.and(tfd))))
//			.or(tfa.not()	.and(tfb.not()	.and(tfc		.and(tfd))))
//			.or(tfa			.and(tfb.not()	.and(tfc		.and(tfd))))
//			.or(tfa.not()	.and(tfb		.and(tfc		.and(tfd))))		
//			.or(tfa			.and(tfb		.and(tfc		.and(tfd))))
//			));		
//
//		System.out.println(Utils.simplify(
//			    tfa.not()	.and(tfb.not()	.and(tfc.not()	.and(tfd.not())))
//			.or(tfa			.and(tfb.not()	.and(tfc.not()	.and(tfd.not()))))
//			.or(tfa.not()	.and(tfb		.and(tfc.not()	.and(tfd.not()))))
//			.or(tfa			.and(tfb.not()	.and(tfc		.and(tfd.not()))))
//			.or(tfa.not()	.and(tfb		.and(tfc		.and(tfd.not()))))
//			.or(tfa			.and(tfb		.and(tfc		.and(tfd.not()))))
//			.or(tfa.not()	.and(tfb.not()	.and(tfc.not()	.and(tfd))))
//			.or(tfa			.and(tfb.not()	.and(tfc.not()	.and(tfd))))
//			.or(tfa.not()	.and(tfb.not()	.and(tfc		.and(tfd))))
//			.or(tfa			.and(tfb.not()	.and(tfc		.and(tfd))))
//			.or(tfa.not()	.and(tfb		.and(tfc		.and(tfd))))		
//			.or(tfa			.and(tfb		.and(tfc		.and(tfd))))
//			));		

		/*
000
010
011
110
101
111

		 */

//		System.out.println(Utils.simplify(
//			    tfa.not()	.and(tfb.not()	.and(tfc.not()))
//			.or(tfa.not()	.and(tfb		.and(tfc.not())))
//			.or(tfa.not()	.and(tfb		.and(tfc)))
//			.or(tfa			.and(tfb		.and(tfc.not())))
//			.or(tfa			.and(tfb.not()	.and(tfc)))
//			.or(tfa			.and(tfb		.and(tfc)))
//			));		
//		
//		System.out.println(Utils.simplify(
//			    tfa.or(tfb)	
//			    .and(tfa.or(tfb.not()))
//			    		));		
//		
//		
		//-----
		
//		System.out.println(
//			    (tfb		.and(tfc		.and(tfd)))
//			.or(tfa			.and(tfb.not()	.and(tfc		.and(tfd))))
//			.or(tfa.not()	.and(tfb		.and(tfc.not()	.and(tfd))))
//			.or((tfb.not()	.and(tfc.not()	.and(tfd))))
//			.or((tfb		.and(tfc		.and(tfd.not()))))
//			.or((tfb		.and(tfc.not()	.and(tfd.not()))))
//			.or((tfb.not()	.and(tfc.not()	.and(tfd.not()))))
//			);		
//		
		
		
		
//		TfTerm tfterma = new TfTerm(tfa, (byte) 1);
//		TfTerm tftermb = new TfTerm(tfb, (byte) 1);
//
//		TfTerm tftermna = new TfTerm((SimpleTf) tfa.not(), (byte) 0);
//		TfTerm tftermnb = new TfTerm((SimpleTf) tfb.not(), (byte) 0);

		// ( A and((!A or D ) and((!D or !A ) and (D and ( !D or A)))))

//		System.out.println(tfa.and((tfa.not().or(tfb)).and((tfb.not().or(tfa.not()).and(tfb.and(tfb.not().or(tfa)))))));
//
//		System.out.println(Utils.disjunctiveForm(tfa.and((tfa.not().or(tfb)).and((tfb.not().or(tfa.not()).and(tfb.and(tfb.not().or(tfa))))))));
//
//		TfTerm[] termarray1 = new TfTerm[2];
//		termarray1[0] = tfterma;
//		termarray1[1] = tftermb;
//
//		TfTerm[] termarray2 = new TfTerm[2];
//		termarray2[0] = tfterma;
//		termarray2[1] = tftermnb;
//
//		Term term1 = new Term(termarray1);
//		Term term2 = new Term(termarray2);
//
//		List<Term> termList = new ArrayList<Term>();
//		termList.add(term1);
//		termList.add(term2);
//		
//		QmcFormula qmcf = new QmcFormula(termList);
//		System.out.println(qmcf);
//		qmcf.reduceToPrimeImplicants();
//		System.out.println(qmcf);
//		qmcf.reducePrimeImplicantsToSubset();
//        System.out.println(qmcf);
//

		 

	}

}