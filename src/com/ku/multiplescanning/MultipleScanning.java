/**
 * This class is used to perform discretization using
 * multiple scanning on the given input data.
 */
package com.ku.multiplescanning;

import java.util.ArrayList;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;

import com.ku.utility.MulscanUtility;

/**
 * @author chandra
 *
 */
public class MultipleScanning {
	public static void scan(Table table,int noOfScans){
	do {
		Table inconsistentSubTable=Table.getInconsistSubTable(table);
		if(noOfScans>0){
		for(String attributeName:inconsistentSubTable.getAttributeNames()){
		table.addCutPoint(attributeName, (Double)getBestCutPointForAttribute(inconsistentSubTable, attributeName));
		}
		noOfScans--;
		if(table.isTableConsistent()) {
			break;
		}
		}
		else{
			String bestAttribute=getBestAttribute(inconsistentSubTable);
			table.addCutPoint(bestAttribute, (Double)getBestCutPointForAttribute(inconsistentSubTable, bestAttribute));
			if(table.isTableConsistent()) {
				break;
			}
		}
		
		
	}while(true);
	//System.out.println("Discretization has been done on the given data");
	//System.out.println("Merging cutpoints, please wait");
	table.merge();
	Parser.writeToOutputFile(table);
	System.out.println("Finished creating int and data files for given input");
	}
	
public static String getBestAttribute(Table table) {
		
		String minimumAtrribute="";
		Double minimumEntropyValue=null;
		
		for(String attributeName:table.getAttributeNames()){
			
			ArrayList<String[]> listOfvalues=new ArrayList<String[]>();
			HashSet<Double> calculatedValues=new HashSet<Double>();
			
			for(Row row:table.getRows()) {
				Double value=MulscanUtility.getValueForAtrributeInRow(row, attributeName);
				if(!calculatedValues.contains(value)) {
					calculatedValues.add(value);
					ArrayList<String> decisionHolder=new ArrayList<String>();
					
					for(Row innerRow:table.getRows()) {
						Double innerValue=MulscanUtility.getValueForAtrributeInRow(innerRow,attributeName);
						if(value.equals(innerValue)) decisionHolder.add(innerRow.getDecision());					
					}
					listOfvalues.add((String[]) decisionHolder.toArray(new String[0]));
				}				
				
			}
			double calculatedEnrtopy=CalculateEntropy.entropyCalculator(listOfvalues);
			
			//System.out.print(".");
			if(minimumEntropyValue==null) {
				minimumEntropyValue=calculatedEnrtopy;
				minimumAtrribute=attributeName;
			}
			else {
				if(calculatedEnrtopy<minimumEntropyValue) {
					minimumEntropyValue=calculatedEnrtopy;
					minimumAtrribute=attributeName;
				}
			}
			
		}
		
		return minimumAtrribute;
	}
	
	
	private static Object getBestCutPointForAttribute(Table table, String attribute) {
		double bestCutpoint=-1000000000;
		double minEntropyValue=1000000000;
		ArrayList<AttributeParams> bstAttr=new ArrayList<AttributeParams>();
		
		for(Row row:table.getRows()) {
			for(AttributeParams attr: row.getAttrParams()) {
				if(attr.getAttributeName().equals(attribute)) {
					bstAttr.add(attr);
				}
			}
		}
		
		Collections.sort(bstAttr, new AttributeComparator());
		
		for(int i=0;i<bstAttr.size();i++) {
			double curntValue=bstAttr.get(i).getAttributeValue();
			if(i+1==bstAttr.size()) break;
			double nxtValue=bstAttr.get(i+1).getAttributeValue();
			
			ArrayList<String[]> listOfvalues=new ArrayList<String[]>();
			double cutpoint;
			if(curntValue<nxtValue) {
				String firstHalf[]=new String[i+1];
				String otherHalf[]=new String[bstAttr.size()-(i+1)];
				double two=2;
				cutpoint=(curntValue+nxtValue)/two;
				int first=0;
				int second=0;
				for(int j=0;j<bstAttr.size();j++) {
					
					if(j<=i)
						firstHalf[first++]=bstAttr.get(j).getDecision();
					else
						otherHalf[second++]=bstAttr.get(j).getDecision();
						
				}
				
				listOfvalues.add(firstHalf);
				listOfvalues.add(otherHalf);
				double entropy=CalculateEntropy.entropyCalculator(listOfvalues);
				if(i==0) {
					minEntropyValue=entropy;
					bestCutpoint=cutpoint;
				}			
				else {
					if(entropy<minEntropyValue && !table.getCutpointsMap().get(attribute).contains(cutpoint)) {
						minEntropyValue=entropy;
						bestCutpoint=cutpoint;
					}
				}
			}
		}
	//System.out.println("Attribute:"+ attribute+"Best cutpoint: "+bestCutpoint+" Min Value "+minEntropyValue);
		return bestCutpoint;
	
	}
	

}

class AttributeComparator implements Comparator<AttributeParams> {
	@Override
	public int compare(AttributeParams arg0, AttributeParams arg1) {
		// TODO Auto-generated method stub
		return ((Double)arg0.getAttributeValue()).compareTo(arg1.getAttributeValue());
	}
	
}
