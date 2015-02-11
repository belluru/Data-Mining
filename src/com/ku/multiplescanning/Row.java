/**
 * 
 */
package com.ku.multiplescanning;

import java.util.ArrayList;

/**
 * @author chandra
 *
 */
public class Row {
	private String decision;
	private int rowIndex;
	private ArrayList<AttributeParams> attrParams;
	public Row(String decision, int rowIndex,
			ArrayList<Double> values,ArrayList<String> attrNames) {
		attrParams=new ArrayList<AttributeParams>();
		this.decision = decision;
		this.rowIndex = rowIndex;
		int valIndex=0;
		for(String attrName:attrNames){
			attrParams.add(new AttributeParams(attrName,values.get(valIndex++),decision));
		}
	}
	
	public Row(Row r) {
		this.decision = r.getDecision();
		this.rowIndex = r.getRowIndex();
		this.attrParams=new ArrayList<AttributeParams>();
		for(AttributeParams attrParam:r.getAttrParams()){
			this.attrParams.add(new AttributeParams(attrParam));
		}
	}

	public String getDecision() {
		return decision;
	}
	public void setDecision(String decision) {
		this.decision = decision;
	}
	public int getRowIndex() {
		return rowIndex;
	}
	public void setRowIndex(int rowIndex) {
		this.rowIndex = rowIndex;
	}
	public ArrayList<AttributeParams> getAttrParams() {
		return attrParams;
	}
	public void setAttrParams(ArrayList<AttributeParams> attrParams) {
		this.attrParams = attrParams;
	}
	
	public boolean equals(Object r){
		ArrayList<AttributeParams> crntRowAtrbList = this.attrParams;
		ArrayList<AttributeParams> argRowAtrbList = ((Row) r).getAttrParams();
		for(int index=0;index<crntRowAtrbList.size();index++) {
			if(!(crntRowAtrbList.get(index)).getInterval().equals(argRowAtrbList.get(index).getInterval())){
				return false;
			}
		}
		
		return true;
		
	}
}
