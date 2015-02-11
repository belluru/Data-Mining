package com.ku.multiplescanning;

public class AttributeParams {
	private String attributeName;
	private double attributeValue;
	private String decision;
	private String interval;

	
	public AttributeParams(String attributeName, double attributeValue,
			String decision) {
		super();
		this.attributeName = attributeName;
		this.attributeValue = attributeValue;
		this.decision = decision;
		this.interval = "";
	}

	public AttributeParams(AttributeParams attrParams) {
		this.attributeName = attrParams.attributeName;
		this.attributeValue = attrParams.attributeValue;
		this.decision = attrParams.decision;
		this.interval = attrParams.interval;
	}

	public String getAttributeName() {
		return attributeName;
	}
	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}
	public double getAttributeValue() {
		return attributeValue;
	}
	public void setAttributeValue(double attributeValue) {
		this.attributeValue = attributeValue;
	}
	public String getDecision() {
		return decision;
	}
	public void setDecision(String decision) {
		this.decision = decision;
	}
	public String getInterval() {
		return interval;
	}
	public void setInterval(String interval) {
		this.interval = interval;
	}
}
