package com.omniacom.omniapp.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.data.annotation.CreatedDate;

@Entity
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
//	@Temporal(TemporalType.DATE)
//	@CreatedDate
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
	private Nature nature;

	@OneToOne
	private BillOfQuantities boq;

	@ManyToMany
	@JoinTable(name = "USER_PROJECTS", joinColumns = @JoinColumn(name = "project_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"))
	private List<User> workingUsersList;

	@OneToMany(mappedBy = "project")
	private List<Operation> operations;
	
	@OneToMany(mappedBy = "project")
	private List<Service> services;

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

	/* (non-Javadoc)
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
	 * @param services the services to set
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
	 * @param nature the nature to set
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
	 * @param boq the boq to set
	 */
	public void setBoq(BillOfQuantities boq) {
		this.boq = boq;
	}

}
