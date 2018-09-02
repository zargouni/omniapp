package com.omniacom.omniapp.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Locale;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import org.ocpsoft.prettytime.PrettyTime;

@Entity
public class Notification implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String message;

	private Date createdAt;

	private boolean isRead;

	@ManyToOne
	private Task task;

	@ManyToOne
	private Issue issue;
	
	@ManyToOne
	private Project project;

	@ManyToOne
	private User user;

	@Transient
	private String createdAtPretty;

	@Transient
	private PrettyTime prettyTime = new PrettyTime(Locale.ENGLISH);

	

	public Notification() {

	}

	public Notification(String message, Date createdAt, User user, Task task) {
		this.message = message;
		this.createdAt = createdAt;
		this.user = user;
		this.task = task;
		this.isRead = false;
	}

	public Notification(String message, Date createdAt, User user, Issue issue) {
		this.message = message;
		this.createdAt = createdAt;
		this.user = user;
		this.issue = issue;
		this.isRead = false;
	}

	public Notification(String message, Date date, User user, Project project) {
		this.message = message;
		this.createdAt = date;
		this.user = user;
		this.project = project;
		this.isRead = false;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public boolean getIsRead() {
		return isRead;
	}

	public void setRead(boolean isRead) {
		this.isRead = isRead;
	}

	/**
	 * @return the task
	 */
	public Task getTask() {
		return task;
	}

	/**
	 * @param task
	 *            the task to set
	 */
	public void setTask(Task task) {
		this.task = task;
	}

	/**
	 * @return the createdAtPretty
	 */
	public String getCreatedAtPretty() {
		return this.prettyTime.format(this.createdAt);
	}

	/**
	 * @return the prettyTime
	 */
	public PrettyTime getPrettyTime() {
		return prettyTime;
	}

	/**
	 * @param createdAtPretty
	 *            the createdAtPretty to set
	 */
	public void setCreatedAtPretty(String createdAtPretty) {
		this.createdAtPretty = createdAtPretty;
	}

	/**
	 * @param prettyTime
	 *            the prettyTime to set
	 */
	public void setPrettyTime(PrettyTime prettyTime) {
		this.prettyTime = prettyTime;
	}

	/**
	 * @return the issue
	 */
	public Issue getIssue() {
		return issue;
	}

	/**
	 * @param issue
	 *            the issue to set
	 */
	public void setIssue(Issue issue) {
		this.issue = issue;
	}

	/**
	 * @return the project
	 */
	public Project getProject() {
		return project;
	}

	/**
	 * @param project the project to set
	 */
	public void setProject(Project project) {
		this.project = project;
	}

}
