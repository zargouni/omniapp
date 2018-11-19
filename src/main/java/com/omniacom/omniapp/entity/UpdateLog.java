package com.omniacom.omniapp.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PreRemove;

import org.hibernate.annotations.Where;

@Entity
// @SQLDelete(sql = "UPDATE update_log SET deleted = true WHERE id = ?", check =
// ResultCheckStyle.COUNT)
// @Where(clause = "deleted <> true")
public class UpdateLog implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4925170557630092370L;

	/**
	 * 
	 */

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	private Date date;

	@ManyToOne
	private User actor;

	@ManyToOne(fetch = FetchType.LAZY)
	// @JoinColumn(name = "operation_id")
	private Operation operation;

	@ManyToOne
	private Service service;

	@OneToMany(mappedBy = "updateLog", orphanRemoval = true, cascade = CascadeType.ALL)
	@Where(clause = "deleted <> true")
	private List<LogChange> changes = new ArrayList<>();

	private boolean deleted = false;

	@PreRemove
	public void deleteUpdateLog() {
		this.setDeleted(true);
	}

	public UpdateLog() {
		super();
	}

	public UpdateLog(Date date, User actor, Service service) {
		super();
		this.date = date;
		this.actor = actor;
		this.service = service;
	}

	public UpdateLog(Date date, User actor, Operation operation) {
		super();
		this.date = date;
		this.actor = actor;
		this.operation = operation;
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * @return the actor
	 */
	public User getActor() {
		return actor;
	}

	/**
	 * @return the service
	 */
	public Service getService() {
		return service;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @param date
	 *            the date to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	/**
	 * @param actor
	 *            the actor to set
	 */
	public void setActor(User actor) {
		this.actor = actor;
	}

	/**
	 * @param operation
	 *            the operation to set
	 */
	public void setOperation(Operation operation) {
		this.operation = operation;
	}

	/**
	 * @param service
	 *            the service to set
	 */
	public void setService(Service service) {
		this.service = service;
	}

	/**
	 * @return the deleted
	 */
	public boolean isDeleted() {
		return deleted;
	}

	/**
	 * @param deleted
	 *            the deleted to set
	 */
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	/**
	 * @return the changes
	 */
	public List<LogChange> getChanges() {
		return changes;
	}

	/**
	 * @param changes
	 *            the changes to set
	 */
	public void setChanges(List<LogChange> changes) {
		this.changes = changes;
	}

	/**
	 * @return the operation
	 */
	public Operation getOperation() {
		return operation;
	}
	
	public void addChange(LogChange logChange) {
		this.changes.add(logChange);
		logChange.setUpdateLog(this);
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
		result = prime * result + (int) (id ^ (id >>> 32));
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
		UpdateLog other = (UpdateLog) obj;
		if (id != other.id)
			return false;
		return true;
	}

}
