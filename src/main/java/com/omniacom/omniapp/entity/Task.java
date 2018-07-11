package com.omniacom.omniapp.entity;

import java.io.Serializable;
import java.util.ArrayList;
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
import javax.persistence.OneToMany;

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
	private String completionPercentage = "0";
	private Date startDate;
	private Date endDate;
	private Date creationDate;
	private Date completedOn;
	private int estimationTime;
	private int estimationRH;
	private long zohoId;
	
	@ManyToOne
	private Service service;
	
	@ManyToMany
	@JoinTable(name = "USER_TASK", joinColumns = @JoinColumn(name = "task_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"))
	private List<User> users = new ArrayList<User>();
	
	@OneToMany(mappedBy="task", orphanRemoval=true)
	private List<UploadedFile> attachments;
	
	@OneToMany( mappedBy = "task", orphanRemoval=true)
	private List<Comment> comments;
	
	@ManyToOne
	private User closedBy;
	
	@OneToMany(mappedBy="task", orphanRemoval=true)
	private List<Notification> notifications;
	
	public Task() {

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
	 * @return the estimationTime
	 */
	public int getEstimationTime() {
		return estimationTime;
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
	 * @param estimationTime the estimationTime to set
	 */
	public void setEstimationTime(int estimation) {
		this.estimationTime = estimation;
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
	
	
	public void addUser(User user) {
		this.users.add(user);
		user.getTasks().add(this);
	}



	/**
	 * @return the estimationRH
	 */
	public int getEstimationRH() {
		return estimationRH;
	}



	/**
	 * @param estimationRH the estimationRH to set
	 */
	public void setEstimationRH(int estimationRH) {
		this.estimationRH = estimationRH;
	}



	/**
	 * @return the attachments
	 */
	public List<UploadedFile> getAttachments() {
		return attachments;
	}



	/**
	 * @param attachments the attachments to set
	 */
	public void setAttachments(List<UploadedFile> attachments) {
		this.attachments = attachments;
	}



	/**
	 * @return the creationDate
	 */
	public Date getCreationDate() {
		return creationDate;
	}



	/**
	 * @param creationDate the creationDate to set
	 */
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}



	/**
	 * @return the comments
	 */
	public List<Comment> getComments() {
		return comments;
	}



	/**
	 * @param comments the comments to set
	 */
	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}



	/**
	 * @return the completionPercentage
	 */
	public String getCompletionPercentage() {
		return completionPercentage;
	}



	/**
	 * @param completionPercentage the completionPercentage to set
	 */
	public void setCompletionPercentage(String completionPercentage) {
		this.completionPercentage = completionPercentage;
	}



	/**
	 * @return the closedBy
	 */
	public User getClosedBy() {
		return closedBy;
	}



	/**
	 * @param closedBy the closedBy to set
	 */
	public void setClosedBy(User closedBy) {
		this.closedBy = closedBy;
	}



	/**
	 * @return the notifications
	 */
	public List<Notification> getNotifications() {
		return notifications;
	}



	/**
	 * @param notifications the notifications to set
	 */
	public void setNotifications(List<Notification> notifications) {
		this.notifications = notifications;
	}
	

}
