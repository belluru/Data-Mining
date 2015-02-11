package com.ku.multiplescanning;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeMap;

public class Table {
	private ArrayList<Row> rows;
	private TreeMap<String, ArrayList<Double>> cutpointsMap;
	private ArrayList<String> attributeNames;
	private static ArrayList<HashSet<Integer>> decisionStar = new ArrayList<HashSet<Integer>>();
	private HashMap<String, MinMaxValues> attrMinMaxMap = new HashMap<String, MinMaxValues>();
	private String tableName;
	private String decisionName;
	
	public Table(Table t) {
		this.rows = new ArrayList<Row>();
		for (Row r : t.rows) {
			this.rows.add(new Row(r));
		}
		this.cutpointsMap = t.cutpointsMap;
		this.attributeNames = t.attributeNames;
		this.decisionStar = t.decisionStar;
		this.attrMinMaxMap = t.attrMinMaxMap;
	}
	
	public Table(ArrayList<Row> rows, ArrayList<String> attributeNames,String tableName,String decsionName) {
		this.rows = rows;
		this.attributeNames = attributeNames;
		this.tableName=tableName;
		this.cutpointsMap = new TreeMap<String, ArrayList<Double>>();
		this.decisionName=decsionName;
		for (String attributes : attributeNames) {
			cutpointsMap.put(attributes, new ArrayList<Double>());
		}
	
	//decision star calculation
	HashSet<String> decisions=new HashSet<String>();
	for (Row r : rows) {
		if(!(decisions.contains(r.getDecision()))){
			String decision=r.getDecision();
			decisions.add(decision);
			HashSet<Integer> concept=new HashSet<Integer>();
			for(Row inner:rows){
				if(inner.getDecision().equals(decision)){
					concept.add(inner.getRowIndex());
				}
			}
			decisionStar.add(concept);
		}
	}
	// Minimum and maximum values calculation for an attribute
	for (int i = 0; i < attributeNames.size(); i++) {
		double min = 1000000000;
		double max = -1000000000;
		for (Row r : rows) {
			double temp = r.getAttrParams().get(i).getAttributeValue();
			if (min > temp)
				min = temp;
			else if (max < temp) {
				max = temp;
			}

		}
		attrMinMaxMap
				.put(attributeNames.get(i), new MinMaxValues(min, max));
	}
	}
	
	public void addCutPoint(String attribute, double value) {
		ArrayList<Double> listOfCutPoints = cutpointsMap.get(attribute);
		listOfCutPoints.add(value);
		addMinMaxValues(this);
	}
	
	public static void addMinMaxValues(Table table) {

		for (int index = 0; index < table.attributeNames.size(); index++) {
			String attributeName = table.attributeNames.get(index);
			ArrayList<Double> cutpoints = table.getCutpointsMap().get(
					attributeName);
			Collections.sort(cutpoints);
			double min = table.attrMinMaxMap.get(attributeName).getMin();
			double max = table.attrMinMaxMap.get(attributeName).getMax();

			for (Row row : table.rows) {
				AttributeParams attrb = row.getAttrParams().get(index);
				if (cutpoints.isEmpty()) {
					attrb.setInterval(min + ".." + max);
				} else {
					for (int i = 0; i < cutpoints.size(); i++) {
						double temp = attrb.getAttributeValue();
						double cut = cutpoints.get(i);
						if (i == 0) {
							if (min <= temp && temp < cut) {
								attrb.setInterval(min + ".." + cut);
							}
							if(i != cutpoints.size() - 1) {
								if (temp >= cut && temp < cutpoints.get(i + 1)) {
									attrb.setInterval(cut + ".."
											+ cutpoints.get(i + 1));
								}
							}
							
						}
						if (i == cutpoints.size() - 1) {
							if (temp >= cut && temp <= max) {
								attrb.setInterval(cut + ".." + max);
							}
						}
						if (i != 0 && i != cutpoints.size() - 1) {
							if (temp >= cut && temp < cutpoints.get(i + 1)) {
								attrb.setInterval(cut + ".."
										+ cutpoints.get(i + 1));
							}
						}
					}
				}
			}
		}
	}


	public HashMap<String, MinMaxValues> getAttrMinMaxMap() {
		return attrMinMaxMap;
	}

	public void setAttrMinMaxMap(HashMap<String, MinMaxValues> attrMinMaxMap) {
		this.attrMinMaxMap = attrMinMaxMap;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getDecisionName() {
		return decisionName;
	}

	public void setDecisionName(String decisionName) {
		this.decisionName = decisionName;
	}

	public ArrayList<Row> getRows() {
		return rows;
	}
	public void setRows(ArrayList<Row> rows) {
		this.rows = rows;
	}
	public TreeMap<String, ArrayList<Double>> getCutpointsMap() {
		return cutpointsMap;
	}
	public void setCutpointsMap(TreeMap<String, ArrayList<Double>> cutpointsMap) {
		this.cutpointsMap = cutpointsMap;
	}
	public ArrayList<String> getAttributeNames() {
		return attributeNames;
	}
	public void setAttributeNames(ArrayList<String> attributeNames) {
		this.attributeNames = attributeNames;
	}
	public static Table getInconsistSubTable(Table t){
		if(t.cutpointsMap.isEmpty()){
			return t;
		}
		else{
			Table tmp=new Table(t);
			ArrayList<HashSet<Integer>> astar=new ArrayList<HashSet<Integer>>();
			HashSet<Row> calcSet=new HashSet<Row>();
			for(Row r: tmp.rows){
				if(!(calcSet.contains(r))){
					calcSet.add(r);
					HashSet<Integer> subset=new HashSet<Integer>();
					for(Row inner:tmp.rows){
						if(r.equals(inner))
							subset.add(inner.getRowIndex());
						//System.out.println("subset"+subset);
					}
					astar.add(subset);
					//System.out.println("astar"+astar);
				}
			}
			
			ArrayList<HashSet<Integer>> inconsistentSet=getInconsistentDataSet(astar, decisionStar);
			HashSet<Integer> tempSet=inconsistentSet.get(0);
			
			ArrayList<Integer> rowstoRemove = new ArrayList<Integer>();
			for (int i = 0; i < tmp.rows.size(); i++) {
				Row row = tmp.rows.get(i);
				boolean delFlag = true;
				
					if (tempSet.contains(row.getRowIndex())) {
						delFlag = false;
					}
				
				if (delFlag) {
					rowstoRemove.add(i);
				}

			}
			int indexToBeSubtracted=0;
			for (Integer j : rowstoRemove) {

				tmp.rows.remove(j.intValue()-indexToBeSubtracted++);
			}
			return tmp;
		}
	}
	public boolean isTableConsistent(){
		ArrayList<HashSet<Integer>> astar=new ArrayList<HashSet<Integer>>();
		HashSet<Row> calcSet=new HashSet<Row>();
		for(Row r: this.rows){
			if(!(calcSet.contains(r))){
				calcSet.add(r);
				HashSet<Integer> subset=new HashSet<Integer>();
				for(Row inner:this.rows){
					if(r.equals(inner))
						subset.add(inner.getRowIndex());
				}
				astar.add(subset);
			}
		}
		return getInconsistentDataSet(astar,decisionStar).isEmpty();
	}
	private static ArrayList<HashSet<Integer>> getInconsistentDataSet(
			ArrayList<HashSet<Integer>> astar,
			ArrayList<HashSet<Integer>> dstar) {
		ArrayList<HashSet<Integer>> inConsistSet=new ArrayList<HashSet<Integer>>();
		for(HashSet<Integer> attrSubset:astar){
			boolean subsetFlag=false;
			for(HashSet<Integer> decSubset:dstar){
				if(isSubset(attrSubset,decSubset)){
					subsetFlag=true;
				}
			}
			if(!subsetFlag){
				inConsistSet.add(attrSubset);
			}
		}
		
		return inConsistSet;
		
	}
	private static boolean isSubset(HashSet<Integer> first,
			HashSet<Integer> second) {
		boolean flag=true;
		for(Integer temp:first){
			if(!second.contains(temp)){
				flag=false;
			}
		}
		return flag;
	}
	
	public void merge() {
		// TODO Auto-generated method stub
		for(String attribute : this.attributeNames) {
		ArrayList<Double> listOfCutPoints = cutpointsMap.get(attribute);
		@SuppressWarnings("unchecked")
		ArrayList<Double> tempCutPoints = (ArrayList<Double>) listOfCutPoints.clone();
		for(Double value:tempCutPoints) {
		listOfCutPoints.remove(value);
		addMinMaxValues(this);
		if(!this.isTableConsistent()) {
			listOfCutPoints.add(value);
			addMinMaxValues(this);
		}
		else {
			
		}
		
		}
		
		}
	}
}

