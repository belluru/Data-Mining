package com.ku.utility;

import com.ku.multiplescanning.AttributeParams;
import com.ku.multiplescanning.Row;

public class MulscanUtility {
public static Double getValueForAtrributeInRow(Row row,String attrName) {
		
		for(AttributeParams attr:row.getAttrParams()) {
			if(attr.getAttributeName().equals(attrName)) {
				return  attr.getAttributeValue();
			}
		}
		return null;
	}
}
