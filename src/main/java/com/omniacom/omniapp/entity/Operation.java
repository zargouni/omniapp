package com.omniacom.omniapp.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PreRemove;

import org.hibernate.annotations.ResultCheckStyle;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@SQLDelete(sql = "UPDATE operation SET deleted = true, deletion_date = CURRENT_TIMESTAMP WHERE id = ?", check = ResultCheckStyle.COUNT)
@Where(clause = "deleted <> true")
public class Operation implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String name;
	private Date startDate;
	private Date endDate;
	private Date creationDate;
	private int flag;
	private long zohoId;

	@ManyToOne
	private Project project;

	@ManyToOne
	private User responsible;

	@ManyToOne
	private User createdBy;

	@OneToMany(mappedBy = "operation", orphanRemoval = true, cascade = CascadeType.PERSIST)
	@Where(clause = "deleted <> true")
	private List<Service> services;

	@ManyToMany
	@JoinTable(name = "USER_OPERATION", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "operation_id", referencedColumnName = "id"))
	private List<User> workingUsersList;

	@ManyToOne
	private Site site;

//	@OneToMany(mappedBy = "operation", orphanRemoval = true, cascade = CascadeType.PERSIST)
//	@Where(clause = "deleted <> true")
//	private List<Snag> snags;

	@OneToMany(mappedBy = "operation", orphanRemoval = true, cascade = CascadeType.PERSIST)
	@Where(clause = "deleted <> true")
	private List<Comment> comments;

	@OneToMany(mappedBy = "operation", orphanRemoval = true, cascade = CascadeType.PERSIST)
	@Where(clause = "deleted <> true")
	private List<Issue> issues;

	@OneToMany(mappedBy = "operation", orphanRemoval = true, cascade = CascadeType.ALL)
	@Where(clause = "deleted <> true")
	private List<UpdateLog> updates = new ArrayList<>();

	@ManyToOne
	private User deletedBy;

	private boolean deleted = false;

	private Date deletionDate;

	@PreRemove
	public void deleteOperation() {
		this.setDeleted(true);
		this.setDeletionDate(new Date());
	}

	public Operation() {

	}

	public Operation(String name, Date startDate, Date endDate, int flag) {
		this.name = name;
		this.startDate = startDate;
		this.endDate = endDate;
		this.flag = flag;
	}

	public Operation(String name, Date startDate, Date endDate, int flag, long zohoId) {
		this.name = name;
		this.startDate = startDate;
		this.endDate = endDate;
		this.flag = flag;
		this.zohoId = zohoId;
	}

	public Operation(long id, String name, Date startDate, Date endDate, int flag, long zohoId) {
		this.id = id;
		this.name = name;
		this.startDate = startDate;
		this.endDate = endDate;
		this.flag = flag;
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
	 * @return the flag
	 */
	public int getFlag() {
		return flag;
	}

	/**
	 * @return the zohoId
	 */
	public long getZohoId() {
		return zohoId;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param startDate
	 *            the startDate to set
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	/**
	 * @param endDate
	 *            the endDate to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	/**
	 * @param flag
	 *            the flag to set
	 */
	public void setFlag(int flag) {
		this.flag = flag;
	}

	/**
	 * @param zohoId
	 *            the zohoId to set
	 */
	public void setZohoId(long zohoId) {
		this.zohoId = zohoId;
	}

	/**
	 * @return the project
	 */
	public Project getProject() {
		return project;
	}

	/**
	 * @return the responsible
	 */
	public User getResponsible() {
		return responsible;
	}

	/**
	 * @return the services
	 */
	public List<Service> getServices() {
		return services;
	}

	/**
	 * @return the workingUserList
	 */
	public List<User> getWorkingUsersList() {
		return workingUsersList;
	}

	/**
	 * @return the snags
	 */
//	public List<Snag> getSnags() {
//		return snags;
//	}

	/**
	 * @return the comments
	 */
	public List<Comment> getComments() {
		return comments;
	}

	/**
	 * @param project
	 *            the project to set
	 */
	public void setProject(Project project) {
		this.project = project;
	}

	/**
	 * @param responsible
	 *            the responsible to set
	 */
	public void setResponsible(User responsible) {
		this.responsible = responsible;
	}

	/**
	 * @param services
	 *            the services to set
	 */
	public void setServices(List<Service> services) {
		this.services = services;
	}

	/**
	 * @param workingUserList
	 *            the workingUserList to set
	 */
	public void setWorkingUsersList(List<User> workingUsersList) {
		this.workingUsersList = workingUsersList;
	}

	/**
	 * @param snags
	 *            the snags to set
	 */
//	public void setSnags(List<Snag> snags) {
//		this.snags = snags;
//	}

	/**
	 * @param comments
	 *            the comments to set
	 */
	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

	/**
	 * @return the site
	 */
	public Site getSite() {
		return site;
	}

	/**
	 * @param site
	 *            the site to set
	 */
	public void setSite(Site site) {
		this.site = site;
	}

	public void addUser(User user) {
		this.workingUsersList.add(user);
		user.getContributedOperationList().add(this);
	}

	/**
	 * @return the creationDate
	 */
	public Date getCreationDate() {
		return creationDate;
	}

	/**
	 * @param creationDate
	 *            the creationDate to set
	 */
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	/**
	 * @return the issues
	 */
	public List<Issue> getIssues() {
		return issues;
	}

	/**
	 * @param issues
	 *            the issues to set
	 */
	public void setIssues(List<Issue> issues) {
		this.issues = issues;
	}

	/**
	 * @return the createdBy
	 */
	public User getCreatedBy() {
		return createdBy;
	}

	/**
	 * @param createdBy
	 *            the createdBy to set
	 */
	public void setCreatedBy(User createdBy) {
		this.createdBy = createdBy;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public Date getDeletionDate() {
		return deletionDate;
	}

	public void setDeletionDate(Date deletionDate) {
		this.deletionDate = deletionDate;
	}

	/**
	 * @return the deletedBy
	 */
	public User getDeletedBy() {
		return deletedBy;
	}

	/**
	 * @param deletedBy
	 *            the deletedBy to set
	 */
	public void setDeletedBy(User deletedBy) {
		this.deletedBy = deletedBy;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((endDate == null) ? 0 : endDate.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((responsible == null) ? 0 : responsible.hashCode());
		result = prime * result + ((site == null) ? 0 : site.hashCode());
		result = prime * result + ((startDate == null) ? 0 : startDate.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
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
		Operation other = (Operation) obj;
		if (endDate == null) {
			if (other.endDate != null)
				return false;
		} else if (!endDate.equals(other.endDate))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (responsible == null) {
			if (other.responsible != null)
				return false;
		} else if (!responsible.equals(other.responsible))
			return false;
		if (site == null) {
			if (other.site != null)
				return false;
		} else if (!site.equals(other.site))
			return false;
		if (startDate == null) {
			if (other.startDate != null)
				return false;
		} else if (!startDate.equals(other.startDate))
			return false;
		return true;
	}

	/**
	 * @return the updates
	 */
	public List<UpdateLog> getUpdates() {
		return updates;
	}

	/**
	 * @param updates
	 *            the updates to set
	 */
	public void setUpdates(List<UpdateLog> updates) {
		this.updates = updates;
	}

	public void addUpdate(UpdateLog update) {
		this.updates.add(update);
		update.setOperation(this);
	}

}
