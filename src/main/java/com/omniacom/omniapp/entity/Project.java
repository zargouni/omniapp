package com.omniacom.omniapp.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PreRemove;

import org.hibernate.annotations.ResultCheckStyle;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@SQLDelete(sql = "UPDATE project SET deleted = true, deletion_date = CURRENT_TIMESTAMP WHERE id = ?", check = ResultCheckStyle.COUNT)
@Where(clause = "deleted <> true")
public class Project implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	private long id;
	private String name;

	@Column(nullable = false, updatable = false)
	private Date creationDate;
	private String description;
	private String currency;
	private String country;
	private String zone;
	private long zohoId;

	@ManyToOne
	private User owner;

	@ManyToOne
	private Client client;

	@ManyToOne
	private Client finalClient;

	@ManyToOne
	private Nature nature;

	@OneToOne
	private BillOfQuantities boq;

	@ManyToMany(mappedBy = "contributedProjectList")
	// @JoinTable(name = "USER_PROJECTS", joinColumns = @JoinColumn(name =
	// "project_id", referencedColumnName = "id"), inverseJoinColumns =
	// @JoinColumn(name = "user_id", referencedColumnName = "id"))
	private List<User> workingUsersList;

	@OneToMany(mappedBy = "project", orphanRemoval=true, cascade = CascadeType.PERSIST)
	@Where(clause="deleted <> true")
	private List<Operation> operations;

	@OneToMany(mappedBy = "project",orphanRemoval=true, cascade = CascadeType.PERSIST)
	@Where(clause="deleted <> true")
	private List<Service> services;

	@OneToMany(mappedBy = "project",orphanRemoval=true, cascade = CascadeType.PERSIST)
	@Where(clause="deleted <> true")
	private List<Issue> issues;
	
	private boolean deleted = false;

	private Date deletionDate;

	@PreRemove
	public void deleteOperation() {
		this.setDeleted(true);
		this.setDeletionDate(new Date());
	}

	public Project() {

	}

	public Project(String name, Date creationDate, String description, String currency, String country, String zone) {
		this.name = name;
		this.creationDate = creationDate;
		this.description = description;
		this.currency = currency;
		this.country = country;
		this.zone = zone;
		this.operations = new ArrayList<Operation>();
	}

	public Project(String name, Date creationDate, String description, String currency, String country, String zone,
			long zohoId) {
		this.name = name;
		this.creationDate = creationDate;
		this.description = description;
		this.currency = currency;
		this.country = country;
		this.zone = zone;
		this.zohoId = zohoId;
	}

	public Project(long id, String name, Date creationDate, String description, String currency, String country,
			String zone, long zohoId) {
		this.id = id;
		this.name = name;
		this.creationDate = creationDate;
		this.description = description;
		this.currency = currency;
		this.country = country;
		this.zone = zone;
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
	 * @return the creationDate
	 */
	public Date getCreationDate() {
		return creationDate;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @return the currency
	 */
	public String getCurrency() {
		return currency;
	}

	/**
	 * @return the country
	 */
	public String getCountry() {
		return country;
	}

	/**
	 * @return the zone
	 */
	public String getZone() {
		return zone;
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
	 * @param creationDate
	 *            the creationDate to set
	 */
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @param currency
	 *            the currency to set
	 */
	public void setCurrency(String currency) {
		this.currency = currency;
	}

	/**
	 * @param country
	 *            the country to set
	 */
	public void setCountry(String country) {
		this.country = country;
	}

	/**
	 * @param zone
	 *            the zone to set
	 */
	public void setZone(String zone) {
		this.zone = zone;
	}

	/**
	 * @param zohoId
	 *            the zohoId to set
	 */
	public void setZohoId(long zohoId) {
		this.zohoId = zohoId;
	}

	/**
	 * @return the owner
	 */
	public User getOwner() {
		return owner;
	}

	/**
	 * @return the workingTeamList
	 */
	public List<User> getWorkingUsersList() {
		return workingUsersList;
	}

	/**
	 * @return the operations
	 */
	public List<Operation> getOperations() {
		return operations;
	}

	/**
	 * @param owner
	 *            the owner to set
	 */
	public void setOwner(User owner) {
		this.owner = owner;
	}

	/**
	 * @param workingTeamList
	 *            the workingTeamList to set
	 */
	public void setWorkingUsersList(List<User> workingUsersList) {
		this.workingUsersList = workingUsersList;
	}

	/**
	 * @param operations
	 *            the operations to set
	 */
	public void setOperations(List<Operation> operations) {
		this.operations.clear();
		this.operations = operations;
	}

	/**
	 * @return the client
	 */
	public Client getClient() {
		return client;
	}

	/**
	 * @param client
	 *            the client to set
	 */
	public void setClient(Client client) {
		this.client = client;
	}

	public void addUser(User user) {
		this.workingUsersList.add(user);
		user.getContributedProjectList().add(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Project [id=" + id + ", name=" + name + ", creationDate=" + creationDate + ", description="
				+ description + ", currency=" + currency + ", country=" + country + ", zone=" + zone + ", zohoId="
				+ zohoId + ", owner=" + owner + "]";
	}

	/**
	 * @return the services
	 */
	public List<Service> getServices() {
		return services;
	}

	/**
	 * @param services
	 *            the services to set
	 */
	public void setServices(List<Service> services) {
		this.services = services;
	}

	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/**
	 * @return the nature
	 */
	public Nature getNature() {
		return nature;
	}

	/**
	 * @param nature
	 *            the nature to set
	 */
	public void setNature(Nature nature) {
		this.nature = nature;
	}

	/**
	 * @return the boq
	 */
	public BillOfQuantities getBoq() {
		return boq;
	}

	/**
	 * @param boq
	 *            the boq to set
	 */
	public void setBoq(BillOfQuantities boq) {
		this.boq = boq;
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
	 * @return the finalClient
	 */
	public Client getFinalClient() {
		return finalClient;
	}

	/**
	 * @param finalClient
	 *            the finalClient to set
	 */
	public void setFinalClient(Client finalClient) {
		this.finalClient = finalClient;
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

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
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
		Project other = (Project) obj;
		if (id != other.id)
			return false;
		return true;
	}

}
