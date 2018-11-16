package com.omniacom.omniapp.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PreRemove;

import org.hibernate.annotations.ResultCheckStyle;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedDate;

@Entity
@SQLDelete(sql = "UPDATE service SET deleted = true, deletion_date = CURRENT_TIMESTAMP WHERE id = ?", check = ResultCheckStyle.COUNT)
@Where(clause = "deleted <> true")
public class Service implements Serializable, Comparable<Service> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String name;
	private String description;
	@CreatedDate
	private Date creationDate;
	private int flag;
	private float priceHT;
	private long zohoId;
	
	private String poNumber = "NOPO";

	private ServiceCategory category;

	@ManyToOne
	private Project project;

	@ManyToOne
	private Operation operation;

	@ManyToOne
	private User createdBy;
	
	@OneToMany(mappedBy = "service",orphanRemoval=true, cascade = CascadeType.PERSIST)
	@Where(clause="deleted <> true")
	private List<Task> tasks;
	
	@OneToMany(mappedBy = "service",orphanRemoval=true, cascade = CascadeType.PERSIST)
	@Where(clause="deleted <> true")
	private List<UpdateLog> updates;
	
	@ManyToOne
	private User deletedBy;
	
	private boolean deleted = false;

	private Date deletionDate;

	@PreRemove
	public void deleteService() {
		this.setDeleted(true);
		this.setDeletionDate(new Date());
	}

	public Service() {

	}

	public Service(String name, Project project) {
		this.name = name;
		this.project = project;
	}

	public Service(String name, Date creationDate, int flag, float priceHT) {
		this.name = name;
		this.creationDate = creationDate;
		this.flag = flag;
		this.priceHT = priceHT;
	}

	public Service(String name, Date creationDate, int flag, float priceHT, long zohoId) {
		this.name = name;
		this.creationDate = creationDate;
		this.flag = flag;
		this.priceHT = priceHT;
		this.zohoId = zohoId;
	}

	public Service(long id, String name, Date creationDate, int flag, float priceHT, long zohoId) {
		this.id = id;
		this.name = name;
		this.creationDate = creationDate;
		this.flag = flag;
		this.priceHT = priceHT;
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
	 * @return the flag
	 */
	public int getFlag() {
		return flag;
	}

	/**
	 * @return the priceHT
	 */
	public float getPriceHT() {
		return priceHT;
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
	 * @param flag
	 *            the flag to set
	 */
	public void setFlag(int flag) {
		this.flag = flag;
	}

	/**
	 * @param priceHT
	 *            the priceHT to set
	 */
	public void setPriceHT(float priceHT) {
		this.priceHT = priceHT;
	}

	/**
	 * @param zohoId
	 *            the zohoId to set
	 */
	public void setZohoId(long zohoId) {
		this.zohoId = zohoId;
	}

	/**
	 * @return the operation
	 */
	public Operation getOperation() {
		return operation;
	}

	/**
	 * @return the tasks
	 */
	public List<Task> getTasks() {
		return tasks;
	}

	/**
	 * @param operation
	 *            the operation to set
	 */
	public void setOperation(Operation operation) {
		this.operation = operation;
	}

	/**
	 * @param tasks
	 *            the tasks to set
	 */
	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;
	}

	/**
	 * @return the project
	 */
	public Project getProject() {
		return project;
	}

	/**
	 * @param project
	 *            the project to set
	 */
	public void setProject(Project project) {
		this.project = project;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the category
	 */
	public ServiceCategory getCategory() {
		return category;
	}

	/**
	 * @param category
	 *            the category to set
	 */
	public void setCategory(ServiceCategory category) {
		this.category = category;
	}

	@Override
	public int compareTo(Service o) {
		// TODO Auto-generated method stub
		if (creationDate.before(o.creationDate)) {
			return -1;
		} else if (creationDate.after(o.creationDate))
			return 1;
		return 0;
	}

	/**
	 * @return the poNumber
	 */
	public String getPoNumber() {
		return poNumber;
	}

	/**
	 * @param poNumber the poNumber to set
	 */
	public void setPoNumber(String poNumber) {
		this.poNumber = poNumber;
	}

	/**
	 * @return the createdBy
	 */
	public User getCreatedBy() {
		return createdBy;
	}

	/**
	 * @param createdBy the createdBy to set
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
	 * @param deletedBy the deletedBy to set
	 */
	public void setDeletedBy(User deletedBy) {
		this.deletedBy = deletedBy;
	}

}
