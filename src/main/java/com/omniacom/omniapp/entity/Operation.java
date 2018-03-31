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
import javax.persistence.OneToMany;

@Entity
public class Operation implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID =  1L;
	
	@Id
	@GeneratedValue( strategy = GenerationType.IDENTITY)
	private long id;
	private String name;
	private Date startDate;
	private Date endDate;
	private int flag;
	private long zohoId;
	
	@ManyToOne
	private Project project;
	
	@ManyToOne
	private User responsible;
	
	@OneToMany( mappedBy = "operation")
	private List<Service> services;
	
	@ManyToMany
	@JoinTable(name = "USER_OPERATION", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "operation_id", referencedColumnName = "id"))
	private List<User> workingUsersList;
	
	@ManyToOne
	private Site site;
	
	@OneToMany(mappedBy = "operation")
	private List<Snag> snags;
	
	@OneToMany( mappedBy = "operation")
	private List<Comment> comments;
	
	
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
	 * @param flag the flag to set
	 */
	public void setFlag(int flag) {
		this.flag = flag;
	}
	/**
	 * @param zohoId the zohoId to set
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
	public List<Snag> getSnags() {
		return snags;
	}

	/**
	 * @return the comments
	 */
	public List<Comment> getComments() {
		return comments;
	}

	/**
	 * @param project the project to set
	 */
	public void setProject(Project project) {
		this.project = project;
	}

	/**
	 * @param responsible the responsible to set
	 */
	public void setResponsible(User responsible) {
		this.responsible = responsible;
	}

	/**
	 * @param services the services to set
	 */
	public void setServices(List<Service> services) {
		this.services = services;
	}

	/**
	 * @param workingUserList the workingUserList to set
	 */
	public void setWorkingUsersList(List<User> workingUsersList) {
		this.workingUsersList = workingUsersList;
	}

	/**
	 * @param snags the snags to set
	 */
	public void setSnags(List<Snag> snags) {
		this.snags = snags;
	}

	/**
	 * @param comments the comments to set
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
	 * @param site the site to set
	 */
	public void setSite(Site site) {
		this.site = site;
	}
	
	public void addUser(User user) {
		this.workingUsersList.add(user);
		user.getContributedOperationList().add(this);
	}
	
	
}
