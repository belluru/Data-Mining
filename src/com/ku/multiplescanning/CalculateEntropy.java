package com.ku.multiplescanning;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class CalculateEntropy {
	/**
	 * @param coloumn
	 * @return
	 */
	public static double entropyCalculator(ArrayList<String[]> grpbranch) {
		
		double totalLength=0;
		double entropy=0;
		
		for(String[] a :grpbranch) {
			totalLength+=a.length;
		}
		
		for(String[] branch :grpbranch) {
			entropy+=(branch.length/totalLength)*(entropyCalculatorForBin(branch));
		}		
		
		return entropy; 
	}
	
	public static double entropyCalculatorForBin(String[] decisions) {
		
		double subTotal=0;
		double total=decisions.length;
		Set<String> descionSet=new HashSet<String>();
		
		for(String decision:decisions) {
			if(!descionSet.contains(decision)) {
				descionSet.add(decision);				
				double count=0;
				for(String decisionInner:decisions) 
					if(decisionInner.equals(decision)) count++;
				double temp=count/total;
				subTotal+=-(temp)*(Math.log(temp)/Math.log(2));
			}
		}
				
		return subTotal;
	}

}
