package uy.edu.fing.mina.fsa.tf;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import uy.edu.fing.mina.fsa.utils.CombinationGenerator;

public class Partition {

	public List<TfI> left = null;
	public List<TfI> right = null;
	
	public Partition(List<TfI> left, List<TfI> right) {
		super();
		this.left = left;
		this.right = right;
	}

	@Override
	public String toString() {
		return left + "-" + right;
	}

  /**
   * @param relation
   * @return
   */
  public static TfI toTfrelation3(Partition relation) {
  
    TfI tfrelation = null;
  
    if (relation.left != null) {
      if (relation.left.size() == 1) 
    	System.out.println(relation.left);
      for (Iterator<TfI> iter = relation.left.iterator(); iter.hasNext();) {
        TfI element = iter.next();
        if (tfrelation == null) tfrelation = element;
        else
          tfrelation = tfrelation.andSimple(element);
      }
    }
    if (relation.right != null) {
      for (Iterator<TfI> iter2 = relation.right.iterator(); iter2.hasNext();) {
        TfI element = iter2.next();
        tfrelation = tfrelation.andSimple(element.not());
      }
    }
    return tfrelation;
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
  
            TfI partitionL = CompositeTf.listToCTF(op1, leftArray);
            
            if (!rightArray.isEmpty()) {
               TfI partitionR = CompositeTf.listToCTF(op2, rightArray);
               partitions.add(new TfPair(partitionL, partitionR));
            } else {
               partitions.add(new TfPair(partitionL, null));
            }
         }
      }
      partitions = Partition.cleanPartitions(partitions);
  
      return partitions;
   }

  /**
      * Returns each split of tfs into two subsets
      * 
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
	
}
