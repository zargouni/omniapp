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

import org.springframework.data.annotation.CreatedDate;

@Entity
public class Service implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	private long id;
	private String name;
	
	@CreatedDate
	private Date creationDate;
	private int flag;
	private float priceHT;
	private long zohoId;
	
	@ManyToOne
	private Operation operation;
	
	@OneToMany( mappedBy = "service")
	private List<Task> tasks;
	
	public Service() {

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
	 * @param creationDate the creationDate to set
	 */
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	/**
	 * @param flag the flag to set
	 */
	public void setFlag(int flag) {
		this.flag = flag;
	}
	/**
	 * @param priceHT the priceHT to set
	 */
	public void setPriceHT(float priceHT) {
		this.priceHT = priceHT;
	}
	/**
	 * @param zohoId the zohoId to set
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
	 * @param operation the operation to set
	 */
	public void setOperation(Operation operation) {
		this.operation = operation;
	}

	/**
	 * @param tasks the tasks to set
	 */
	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;
	}
	
	

}
