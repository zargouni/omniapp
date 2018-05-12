package com.omniacom.omniapp.entity;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class BoqService implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@EmbeddedId
	private BoqServiceId boqServiceId;
	
	@ManyToOne
	private BillOfQuantities boq;
	
	@ManyToOne
	private ServiceTemplate template;

	/**
	 * @return the boqServiceId
	 */
	public BoqServiceId getBoqServiceId() {
		return boqServiceId;
	}

	/**
	 * @return the boq
	 */
	public BillOfQuantities getBoq() {
		return boq;
	}

	/**
	 * @return the template
	 */
	public ServiceTemplate getTemplate() {
		return template;
	}

	/**
	 * @param boqServiceId the boqServiceId to set
	 */
	public void setBoqServiceId(BoqServiceId boqServiceId) {
		this.boqServiceId = boqServiceId;
	}

	/**
	 * @param boq the boq to set
	 */
	public void setBoq(BillOfQuantities boq) {
		this.boq = boq;
	}

	/**
	 * @param template the template to set
	 */
	public void setTemplate(ServiceTemplate template) {
		this.template = template;
	}
	
	
	
}
