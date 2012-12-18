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
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;
import java.util.Set;

import uy.edu.fing.mina.fsa.tf.CompositeTf;
import uy.edu.fing.mina.fsa.tf.Operator;
import uy.edu.fing.mina.fsa.tf.Partition;
import uy.edu.fing.mina.fsa.tf.SimpleTf;
import uy.edu.fing.mina.fsa.tf.TfI;
import uy.edu.fing.mina.fsa.tf.TfPair;


/**
 * @author Javier Baliosian &lt; <a
 *         href="mailto:jbaliosian@tsc.upc.es">jbaliosian@tsc.upc.es </a>&gt;
 */
public class Utils {

	/**
	 * @param pairsP
	 * @return
	 */
//	public static Set toSet(LinkedList pairsP) {
//	   Set set = new HashSet();
//	   for (Iterator iter = pairsP.iterator(); iter.hasNext();) {
//         set.add(iter.next());
//      }
//		return set;
//	}

//   /**
//    * Returns each split of tfs into two subsets
//    * @param op2
//    * @param op1
//    * 
//    * @param tfs
//    *           it must have at least one element
//    * @return a set of CompositeTF
//    *  
//    */
//   public static Set<TfI> getPartitions(String op1, String op2, Set<TfI> tfs) {
//      CombinationGenerator cg;
//      //Set<TfI> rightSet = new HashSet<TfI>();
//      Set<TfI> partitions = new HashSet<TfI>();
//      
//      for (int i = 1; i <= tfs.size(); i++) {
//         int[] indices;
//         cg = new CombinationGenerator(tfs.size(), i);
//         while (cg.hasMore()) {
//            ArrayList<TfI> rightArray = new ArrayList<TfI>();
//            rightArray.addAll(tfs);
//            indices = cg.getNext();
//            // building left part of partition with ANDs
//            ArrayList<TfI> leftArray = new ArrayList<TfI>();
//            for (int j = 0; j < indices.length; j++) {
//               leftArray.add(rightArray.get(indices[j]));
//               rightArray.set(indices[j], null);
//            }
//
//            TfI partitionL = listToCTF(op1, leftArray);
//            if (!rightArray.isEmpty()) {
//               TfI partitionR = listToCTF(op2, rightArray);
//               if (partitionR != null) {
//                  TfI partition = partitionL.and(partitionR.not());
//                  partitions.add(partition);
//               } else {
//                  partitions.add(partitionL);
//               }
//            } else {
//               partitions.add(partitionL);
//            }
//         }
//      }
//               
//      return partitions;
//   }


   /**
    * Returns each split of tfs into two subsets
    * @param op2
    * @param op1
    * 
    * @param tfs
    *           it must have at least one element
    * @return a set of TfPairs with a TfString in each side. 
    *  
    */
   public static Set<Partition> getPartitions3(Set<TfI> tfs) {
      
	  CombinationGenerator cg;

      Set<Partition> partitions = new HashSet<Partition>();
      
      for (int i = 1; i <= tfs.size(); i++) {
         int[] indices;
         cg = new CombinationGenerator(tfs.size(), i);
         while (cg.hasMore()) {
            ArrayList<TfI> rightArray = new ArrayList<TfI>();
            rightArray.addAll(tfs);
            indices = cg.getNext();

            ArrayList<TfI> leftArray = new ArrayList<TfI>();
            for (int j = 0; j < indices.length; j++) {
               leftArray.add(rightArray.get(indices[j]));
               rightArray.set(indices[j], null);
            }
            
            for (int j = rightArray.size()-1; j >= 0 ; j--) {
            	if (rightArray.get(j) == null)
            		rightArray.remove(j);
			}
            
            if (!rightArray.isEmpty()) {
            	if (rightArray.get(0) == null) 
                    partitions.add(new Partition(leftArray, null));
            	else 
            		partitions.add(new Partition(leftArray, rightArray));
            } else {
               partitions.add(new Partition(leftArray, null));
            }
         }
      }
      
      //clean logic relations, I think.
//      partitions = cleanPartitions(partitions);

      return partitions;
   }
   /**
    * Returns each split of tfs into two subsets
    * @param op2
    * @param op1
    * 
    * @param tfs
    *           it must have at least one element
    * @return a set of TfPairs
    *  
    */
   public static Set<TfPair> getPartitions2(String op1, String op2, Set<TfI> tfs) {
      CombinationGenerator cg;

      Set<TfPair> partitions = new HashSet<TfPair>();
      
      for (int i = 1; i <= tfs.size(); i++) {
         int[] indices;
         cg = new CombinationGenerator(tfs.size(), i);
         while (cg.hasMore()) {
            ArrayList<TfI> rightArray = new ArrayList<TfI>();
            rightArray.addAll(tfs);
            indices = cg.getNext();

            // building left part of partition with ANDs
            ArrayList<TfI> leftArray = new ArrayList<TfI>();
            for (int j = 0; j < indices.length; j++) {
               leftArray.add(rightArray.get(indices[j]));
               rightArray.set(indices[j], null);
            }

            TfI partitionL = listToCTF(op1, leftArray);
            
            if (!rightArray.isEmpty()) {
               TfI partitionR = listToCTF(op2, rightArray);
               partitions.add(new TfPair(partitionL, partitionR));
            } else {
               partitions.add(new TfPair(partitionL, null));
            }
         }
      }
      partitions = cleanPartitions(partitions);

      return partitions;
   }

   /**
    * @param partitions
    * @return
    */
   public static Set<TfPair> cleanPartitions(Set<TfPair> partitions) {
      
      for (Iterator<TfPair> iter = partitions.iterator(); iter.hasNext();) {
         TfPair partition = (TfPair) iter.next();

         TfI tfpartition = null;
         if (partition.getTfIn() != null && partition.getTfOut() != null)
            tfpartition = partition.getTfIn().and(partition.getTfOut().not());
         else if (partition.getTfIn() != null ) 
            tfpartition = partition.getTfIn();
         else if (partition.getTfOut() != null) 
            tfpartition = partition.getTfOut().not();

         if (tfpartition.acceptsNone()) {
            iter.remove();
         } else {

            boolean toremove = false;
            
            for (Iterator<TfPair> iter2 = partitions.iterator(); iter2.hasNext();) {
               TfPair partition2 = (TfPair) iter2.next();

               TfI tfpartition2 = null;
               if (partition2.getTfIn() != null && partition2.getTfOut() != null)
                  tfpartition2 = partition2.getTfIn().and(partition2.getTfOut().not());
               else if (partition.getTfIn() != null ) 
                  tfpartition2 = partition2.getTfIn();
               else if (partition.getTfOut() != null) 
                  tfpartition2 = partition2.getTfOut().not();

               if (partition != partition2 && tfpartition.equals(tfpartition2)) {
                  toremove = true;
               }
            }
            
            if (toremove)
               iter.remove();
         }
      }

      return partitions;
   }

	/**
	 * @param relation
	 * @return
	 */
	public static TfI toTfrelation3(Partition relation) {

		TfI tfrelation = null;

		if (relation.left != null) {
			for (Iterator<TfI> iter = relation.left.iterator(); iter.hasNext();) {
				TfI element = iter.next();
				if (tfrelation == null)
					tfrelation = element;
				else
					tfrelation = tfrelation.and(element);
			}
		}
		if (relation.right != null) {
			for (Iterator<TfI> iter2 = relation.right.iterator(); iter2.hasNext();) {
				TfI element = iter2.next();
				tfrelation = tfrelation.and(element.not());
			}
		}
		return tfrelation;
	}

   
 
   /**
    * @param b
    * @param operator
    * @return
    */
   public static TfI listToCTF(String operator, ArrayList<TfI> rightArray) {
      
      ArrayList<TfI> auxArrayList = new ArrayList<TfI>();
      
      for (Iterator<TfI> iter = rightArray.iterator(); iter.hasNext();) {
    	 TfI o = iter.next();
         if (o != null)
            auxArrayList.add(o);
      }
      
      rightArray = auxArrayList;
      
      Iterator<TfI> raIter = rightArray.iterator();
      TfI partitionR1 = null;
      
      Object o1 = null;
      Object o2 = null;

      if (raIter.hasNext()) {
         o1 = raIter.next();
      }
      if (raIter.hasNext()) {
         o2 = raIter.next();
      }
      if (o1 == null)
         return null;
      else if (o2 == null)
         return (TfI) o1;
      else {
         if (operator.equals(Operator.AND))
            partitionR1 = ((TfI)o1).and((TfI) o2);
         else if (operator.equals(Operator.OR))
            partitionR1 = ((TfI)o1).or((TfI) o2);
         else if (operator.equals(Operator.AS_TAUT_AS))
            partitionR1 = ((TfI)o1).asTautas((TfI) o2); 
         else if (operator.equals(Operator.TAUTER_THAN))
            partitionR1 = ((TfI)o1).tauterThan((TfI) o2);
         
         while (raIter.hasNext()) {
            Object o = raIter.next();
            if (o != null) {
               TfI partitionR2 = null;
               if (operator.equals(Operator.AND))
                  partitionR1 = partitionR1.and((TfI) o);
               else if (operator.equals(Operator.OR))
                  partitionR2 = partitionR1.or((TfI) o);
               else if (operator.equals(Operator.AS_TAUT_AS))
                  partitionR2 = partitionR1.asTautas((TfI) o); 
               else if (operator.equals(Operator.TAUTER_THAN))
                  partitionR2 = partitionR1.tauterThan((TfI) o);

               if (partitionR2 instanceof CompositeTf) {
                  CompositeTf ctfPartitionR2 = (CompositeTf) partitionR2;
                  partitionR1 = ctfPartitionR2;
               }
            }
         }
         
         return partitionR1;
      }
   }
   

   /**
    * @param relation
    * @return
    */
   public static LinkedList<TfI> getAtoms(TfI partition) {
      LinkedList<TfI> positiveAtoms = new LinkedList<TfI>();
      if (partition != null )
         if ( partition instanceof SimpleTf ) {
            positiveAtoms.add(partition);
         } else {
            if (partition instanceof CompositeTf) {
               CompositeTf p = (CompositeTf) partition;
               positiveAtoms.addAll(getAtoms(p.leftTf));
               //positiveAtoms.add(new Operator(p.getOperator()));
               positiveAtoms.addAll(getAtoms(p.rightTf));
            } else {
               System.err.println(Messages.getString("Tffsr.8")); //$NON-NLS-1$
            }
         }
      return positiveAtoms;
   }

   /**
    * Returns clusters of TfPairs by the event name in the input part
    * 
    * @param relevantTFs
    * @return an array of sets, each set is a cluster
    */
   
//   public static Map getTFClusters(Set relevantTFs) {
//      Map tfsByEvent = new HashMap();
//      
//      for (Iterator iter = relevantTFs.iterator(); iter.hasNext();) {
//         TfI tf = (TfI) iter.next();
//         if(!tf.acceptsAll()){
//            Set eventNameSet = tf.getSetOfEventNames();
//            for (Iterator iterator = eventNameSet.iterator(); iterator.hasNext();) {
//               String eventName = (String) iterator.next();
//               Set tfSet = (HashSet) tfsByEvent.get(eventName);
//               if (tfSet == null) tfSet = new HashSet();
//               tfSet.add(tf);
//               tfsByEvent.put(eventName, tfSet);
//            }
//         }
//      }
//
//      for (Iterator iter = relevantTFs.iterator(); iter.hasNext();) {
//         TfI tf = (TfI) iter.next();
//         if(tf.acceptsAll()){
//            Set eventNameSet = tfsByEvent.keySet();
//            if (eventNameSet.size() != 0)
//               for (Iterator iterator = eventNameSet.iterator(); iterator.hasNext();) {
//                  String eventName = (String) iterator.next();
//                  Set tfSet = (HashSet) tfsByEvent.get(eventName);
//                  if (tfSet == null) tfSet = new HashSet();
//                  tfSet.add(tf);
//                  tfsByEvent.put(eventName, tfSet);
//               }
//            else {
//               Set tfSet = new HashSet();
//               tfSet.add(tf);
//               tfsByEvent.put(tf.getEventName(), tfSet);
//            }
//         }
//      }
//      
//      return tfsByEvent;
//   }

   
   public static void showDot(String dot) {
      
      String filename = "/tmp/tffst" + (new Random( (new Date()).getTime())).nextInt(1000) + ".dot";
      
      try {
         BufferedWriter out = new BufferedWriter(new FileWriter(filename));
         out.write(dot);
         out.close();
      } catch (IOException e) {}
      
      try {
         //System.out.println("Calling Dotty...");
//       String[] cmd = {"scripts/launchDotty.sh", filename};
//         String[] cmd = {"dotty", filename};
//         Process p = Runtime.getRuntime().exec(cmd);
         //xop
         String cmd="/usr/bin/dot -Tpng -o " + filename + ".png " + filename;
         String[] cmds= {"/bin/sh", "-c",cmd};
         System.out.println("Calling Dot: "+ cmd);
         Process p = Runtime.getRuntime().exec(cmds);
         //p.waitFor();
         //System.out.println("return: "+ p.exitValue());
      } catch (IOException e) {
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

