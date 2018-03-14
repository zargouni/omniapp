package com.omniacom.omniapp.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class TaskTemplate implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	private String name;
	private int estimationTime;
	private int estimationHR;
	
	@ManyToOne
	private ServiceTemplate serviceTemplate;
	
	public TaskTemplate() {

	}
	
	public TaskTemplate(String name, int estimationTime, int estimationHR) {
		this.name = name;
		this.estimationTime = estimationTime;
		this.estimationHR = estimationHR;
	}
	public TaskTemplate(long id, String name, int estimationTime, int estimationHR) {
		this.id = id;
		this.name = name;
		this.estimationTime = estimationTime;
		this.estimationHR = estimationHR;
	}
	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @return the estimationTime
	 */
	public int getEstimationTime() {
		return estimationTime;
	}
	/**
	 * @return the estimationHR
	 */
	public int getEstimationHR() {
		return estimationHR;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @param estimationTime the estimationTime to set
	 */
	public void setEstimationTime(int estimationTime) {
		this.estimationTime = estimationTime;
	}
	/**
	 * @param estimationHR the estimationHR to set
	 */
	public void setEstimationHR(int estimationHR) {
		this.estimationHR = estimationHR;
	}

	/**
	 * @return the serviceTemplate
	 */
	public ServiceTemplate getServiceTemplate() {
		return serviceTemplate;
	}

	/**
	 * @param serviceTemplate the serviceTemplate to set
	 */
	public void setServiceTemplate(ServiceTemplate serviceTemplate) {
		this.serviceTemplate = serviceTemplate;
	}
	
	
}
