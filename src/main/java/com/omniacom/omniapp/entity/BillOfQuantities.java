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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PreRemove;

import org.hibernate.annotations.ResultCheckStyle;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedDate;

@Entity
@SQLDelete(sql = "UPDATE bill_of_quantities SET deleted = true, deletion_date = CURRENT_TIMESTAMP WHERE id = ?", check = ResultCheckStyle.COUNT)
@Where(clause = "deleted <> true")
public class BillOfQuantities implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	private String name;

	@CreatedDate
	private Date creationDate;
	private Date startDate;
	private Date endDate;

	private boolean deleted = false;

	private Date deletionDate;

	@OneToOne
	private Project project;

	@OneToMany(mappedBy = "boq")
	private List<BoqService> boqServices;

	@PreRemove
	public void deleteBOQ() {
		this.deleted = true;
		this.setDeletionDate(new Date());
	}

	public BillOfQuantities() {

	}

	public BillOfQuantities(String name, Date creationDate, Date startDate, Date endDate) {
		super();
		this.name = name;
		this.creationDate = creationDate;
		this.startDate = startDate;
		this.endDate = endDate;
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
	 * @param id
	 *            the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @param creationDate
	 *            the creationDate to set
	 */
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
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
	 * @param services
	 *            the services to set
	 */

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the boqService
	 */
	public List<BoqService> getBoqServices() {
		return boqServices;
	}

	/**
	 * @param boqService
	 *            the boqService to set
	 */
	public void setBoqServices(List<BoqService> boqServices) {
		this.boqServices = boqServices;
	}

	public void assignBoqServicesToThisBoq(List<BoqService> boqServices) {
		this.setBoqServices(boqServices);
		for (BoqService boqService : boqServices) {
			boqService.setBoq(this);
		}
	}

	public void assignBoqServiceToThisBoq(BoqService boqService) {
		if (this.getBoqServices() != null)
			this.getBoqServices().add(boqService);
		else {
			List<BoqService> boqServices = new ArrayList<BoqService>();
			boqServices.add(boqService);
			this.setBoqServices(boqServices);
		}
		boqService.setBoq(this);

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

}
