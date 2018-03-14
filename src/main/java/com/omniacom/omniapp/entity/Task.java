package com.omniacom.omniapp.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

@Entity
public class Task implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String name;
	private String priority;
	private String status;
	private Date startDate;
	private Date endDate;
	private Date completedOn;
	private int estimation;
	private long zohoId;
	
	@ManyToOne
	private Service service;
	
	@ManyToMany(mappedBy="tasks")
	private List<User> users;
	
	public Task() {

	}

	public Task(String name, String priority, String status, Date startDate, Date endDate, Date completedOn,
			int estimation) {
		this.name = name;
		this.priority = priority;
		this.status = status;
		this.startDate = startDate;
		this.endDate = endDate;
		this.completedOn = completedOn;
		this.estimation = estimation;
	}

	public Task(String name, String priority, String status, Date startDate, Date endDate, Date completedOn,
			int estimation, long zohoId) {
		this.name = name;
		this.priority = priority;
		this.status = status;
		this.startDate = startDate;
		this.endDate = endDate;
		this.completedOn = completedOn;
		this.estimation = estimation;
		this.zohoId = zohoId;
	}

	public Task(long id, String name, String priority, String status, Date startDate, Date endDate, Date completedOn,
			int estimation, long zohoId) {
		this.id = id;
		this.name = name;
		this.priority = priority;
		this.status = status;
		this.startDate = startDate;
		this.endDate = endDate;
		this.completedOn = completedOn;
		this.estimation = estimation;
		this.zohoId = zohoId;
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
	 * @return the priority
	 */
	public String getPriority() {
		return priority;
	}
	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
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
	 * @return the completedOn
	 */
	public Date getCompletedOn() {
		return completedOn;
	}
	/**
	 * @return the estimation
	 */
	public int getEstimation() {
		return estimation;
	}
	/**
	 * @return the zohoId
	 */
	public long getZohoId() {
		return zohoId;
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
	 * @param priority the priority to set
	 */
	public void setPriority(String priority) {
		this.priority = priority;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
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
	 * @param completedOn the completedOn to set
	 */
	public void setCompletedOn(Date completedOn) {
		this.completedOn = completedOn;
	}
	/**
	 * @param estimation the estimation to set
	 */
	public void setEstimation(int estimation) {
		this.estimation = estimation;
	}
	/**
	 * @param zohoId the zohoId to set
	 */
	public void setZohoId(long zohoId) {
		this.zohoId = zohoId;
	}

	/**
	 * @return the service
	 */
	public Service getService() {
		return service;
	}

	/**
	 * @return the users
	 */
	public List<User> getUsers() {
		return users;
	}

	/**
	 * @param service the service to set
	 */
	public void setService(Service service) {
		this.service = service;
	}

	/**
	 * @param users the users to set
	 */
	public void setUsers(List<User> users) {
		this.users = users;
	}
	
	

}
