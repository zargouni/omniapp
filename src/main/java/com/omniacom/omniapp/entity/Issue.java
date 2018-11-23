package com.omniacom.omniapp.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PreRemove;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.ResultCheckStyle;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@SQLDelete(sql = "UPDATE issue SET deleted = true, deletion_date = CURRENT_TIMESTAMP WHERE id = ?", check = ResultCheckStyle.COUNT)
@Where(clause = "deleted <> true")
public class Issue implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String name;
	private String description;
	private Date endDate;
	private Date creationDate;
	private Date completedOn;
	private String status;
	private String severity;
	
	private long zohoId;
	
	@ManyToOne
	private User closedBy;
	
	@ManyToOne
	private Operation operation;
	
	@ManyToOne
	private Project project;
	
	@ManyToOne
	private User creator;
	
	@ManyToOne 
	private Classification classification;
	
	@OneToMany( mappedBy = "issue", orphanRemoval=true, cascade = CascadeType.PERSIST)
	@Where(clause = "deleted <> true")
	private List<Comment> comments;
	
	@ManyToMany(fetch = FetchType.EAGER)
	@Fetch(value = FetchMode.SUBSELECT)
	@JoinTable(name = "USER_ISSUE", joinColumns = @JoinColumn(name = "issue_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"))
	private List<User> assignedUsers;
	
	@OneToMany(mappedBy="issue", orphanRemoval=true, cascade = CascadeType.PERSIST)
	@Where(clause = "deleted <> true")
	private List<UploadedFile> attachments;
	
	@OneToMany(mappedBy="issue", orphanRemoval=true, cascade = CascadeType.PERSIST)
	@Where(clause = "deleted <> true")
	private List<Notification> notifications;
	
	private boolean deleted = false;

	private Date deletionDate;
	
	@PreRemove
	public void deleteIssue() {
		this.setDeleted(true);
		this.setDeletionDate(new Date());
	}
	
	public Issue() {
		super();
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
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @return the endDate
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * @return the creationDate
	 */
	public Date getCreationDate() {
		return creationDate;
	}

	/**
	 * @return the completedOn
	 */
	public Date getCompletedOn() {
		return completedOn;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @return the severity
	 */
	public String getSeverity() {
		return severity;
	}

	/**
	 * @return the releaseOperation
	 */
	public Operation getOperation() {
		return operation;
	}

	/**
	 * @return the project
	 */
	public Project getProject() {
		return project;
	}

	/**
	 * @return the creator
	 */
	public User getCreator() {
		return creator;
	}

	/**
	 * @return the assignedUsers
	 */
	public List<User> getAssignedUsers() {
		return assignedUsers;
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
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	/**
	 * @param creationDate the creationDate to set
	 */
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	/**
	 * @param completedOn the completedOn to set
	 */
	public void setCompletedOn(Date completedOn) {
		this.completedOn = completedOn;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @param severity the severity to set
	 */
	public void setSeverity(String severity) {
		this.severity = severity;
	}

	/**
	 * @param releaseOperation the releaseOperation to set
	 */
	public void setOperation(Operation releaseOperation) {
		this.operation = releaseOperation;
	}

	/**
	 * @param project the project to set
	 */
	public void setProject(Project project) {
		this.project = project;
	}

	/**
	 * @param creator the creator to set
	 */
	public void setCreator(User creator) {
		this.creator = creator;
	}

	/**
	 * @param assignedUsers the assignedUsers to set
	 */
	public void setAssignedUsers(List<User> assignedUsers) {
		this.assignedUsers = assignedUsers;
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
	 * @return the closedBy
	 */
	public User getClosedBy() {
		return closedBy;
	}

	/**
	 * @return the attachments
	 */
	public List<UploadedFile> getAttachments() {
		return attachments;
	}

	/**
	 * @param closedBy the closedBy to set
	 */
	public void setClosedBy(User closedBy) {
		this.closedBy = closedBy;
	}

	/**
	 * @param attachments the attachments to set
	 */
	public void setAttachments(List<UploadedFile> attachments) {
		this.attachments = attachments;
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

	/**
	 * @return the zohoId
	 */
	public long getZohoId() {
		return zohoId;
	}

	/**
	 * @param zohoId the zohoId to set
	 */
	public void setZohoId(long zohoId) {
		this.zohoId = zohoId;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((endDate == null) ? 0 : endDate.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		Issue other = (Issue) obj;
		if (endDate == null) {
			if (other.endDate != null)
				return false;
		} else if (!endDate.equals(other.endDate))
			return false;
		if (id != other.id)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	public Date getDeletionDate() {
		return deletionDate;
	}

	public void setDeletionDate(Date deletionDate) {
		this.deletionDate = deletionDate;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

}
