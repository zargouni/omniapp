package com.omniacom.omniapp.entity;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class BoqServiceId implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private long boqId;
	
	private long templateId;
	
	private float price;

	/**
	 * @return the boqId
	 */
	public long getBoqId() {
		return boqId;
	}

	/**
	 * @return the templateId
	 */
	public long getTemplateId() {
		return templateId;
	}

	/**
	 * @param boqId the boqId to set
	 */
	public void setBoqId(long boqId) {
		this.boqId = boqId;
	}

	/**
	 * @param templateId the templateId to set
	 */
	public void setTemplateId(long templateId) {
		this.templateId = templateId;
	}

	/**
	 * @return the price
	 */
	public float getPrice() {
		return price;
	}

	/**
	 * @param price the price to set
	 */
	public void setPrice(float price) {
		this.price = price;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (boqId ^ (boqId >>> 32));
		result = prime * result + (int) (templateId ^ (templateId >>> 32));
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BoqServiceId other = (BoqServiceId) obj;
		if (boqId != other.boqId)
			return false;
		if (templateId != other.templateId)
			return false;
		return true;
	}
	
	

}
