package com.omniacom.omniapp.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import org.springframework.data.annotation.CreatedDate;

@Entity
public class BillOfQuantities implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@CreatedDate
	private Date creationDate;
	private Date startDate;
	private Date endDate;
	
	@ManyToOne
	private Project project;
	
	@ManyToMany
	@JoinTable(name = "BOQ_SERVICES", joinColumns = @JoinColumn(name = "boq_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "service_id", referencedColumnName = "id"))
	private List<ServiceTemplate> services;
	
	public BillOfQuantities() {

	}
	
	public BillOfQuantities(Date creationDate, Date startDate, Date endDate) {
		this.creationDate = creationDate;
		this.startDate = startDate;
		this.endDate = endDate;
	}
	public BillOfQuantities(long id, Date creationDate, Date startDate, Date endDate) {
		this.id = id;
		this.creationDate = creationDate;
		this.startDate = startDate;
		this.endDate = endDate;
	}
	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}
	/**
	 * @return the creationDate
	 */
	public Date getCreationDate() {
		return creationDate;
	}
	/**
	 * @return the startDate
	 */
	public Date getStartDate() {
		return startDate;
	}
	/**
	 * @return the endDate
	 */
	public Date getEndDate() {
		return endDate;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}
	/**
	 * @param creationDate the creationDate to set
	 */
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	/**
	 * @return the project
	 */
	public Project getProject() {
		return project;
	}

	/**
	 * @return the services
	 */
	public List<ServiceTemplate> getServices() {
		return services;
	}

	/**
	 * @param project the project to set
	 */
	public void setProject(Project project) {
		this.project = project;
	}

	/**
	 * @param services the services to set
	 */
	public void setServices(List<ServiceTemplate> services) {
		this.services = services;
	}
	
	
}
