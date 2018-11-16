package com.omniacom.omniapp.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

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

@Entity
@SQLDelete(sql = "UPDATE update_log SET deleted = true WHERE id = ?", check = ResultCheckStyle.COUNT)
@Where(clause = "deleted <> true")
public class UpdateLog implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue( strategy = GenerationType.IDENTITY)
	private long id;

	private Date date;

	private User actor;
	
	@ManyToOne
	private Operation operation;
	
	@ManyToOne
	private Service service;
	
	@OneToMany(mappedBy="updateLog")
	private List<LogChange> changes ;
	
	private boolean deleted = false;


	@PreRemove
	public void deleteService() {
		this.setDeleted(true);
	}

	
	public UpdateLog() {
		super();
	}

	
	public UpdateLog(Date date, User actor, Operation operation) {
		super();
		this.date = date;
		this.actor = actor;
		this.operation = operation;
	}


	public UpdateLog(Date date, User actor, Service service) {
		super();
		this.date = date;
		this.actor = actor;
		this.service = service;
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
	 * @return the operation
	 */
	public Operation getOperation() {
		return operation;
	}

	/**
	 * @return the service
	 */
	public Service getService() {
		return service;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	/**
	 * @param actor the actor to set
	 */
	public void setActor(User actor) {
		this.actor = actor;
	}

	/**
	 * @param operation the operation to set
	 */
	public void setOperation(Operation operation) {
		this.operation = operation;
	}

	/**
	 * @param service the service to set
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
	 * @param deleted the deleted to set
	 */
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
}
