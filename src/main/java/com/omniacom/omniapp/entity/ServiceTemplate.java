package com.omniacom.omniapp.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

@Entity
public class ServiceTemplate implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String name;
	private String description;
	private float price;
	private ServiceCategory category;
	
	@ManyToMany(mappedBy = "services")
	private List<BillOfQuantities> boqs;
	
	@OneToMany(mappedBy = "serviceTemplate", orphanRemoval=true)
	private List<TaskTemplate> tasks;
	
	
	public ServiceTemplate() {

	}
	
	public ServiceTemplate(String name, String description, float price) {
		this.name = name;
		this.description = description;
		this.price = price;
	}
	public ServiceTemplate(long id, String name, String description, float price) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.price = price;
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
	 * @return the price
	 */
	public float getPrice() {
		return price;
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
	 * @param price the price to set
	 */
	public void setPrice(float price) {
		this.price = price;
	}

	/**
	 * @return the boqs
	 */
	public List<BillOfQuantities> getBoqs() {
		return boqs;
	}

	/**
	 * @return the tasks
	 */
	public List<TaskTemplate> getTasks() {
		return tasks;
	}

	/**
	 * @param boqs the boqs to set
	 */
	public void setBoqs(List<BillOfQuantities> boqs) {
		this.boqs = boqs;
	}

	/**
	 * @param tasks the tasks to set
	 */
	public void setTasks(List<TaskTemplate> tasks) {
		this.tasks = tasks;
	}

	/**
	 * @return the category
	 */
	public ServiceCategory getCategory() {
		return category;
	}

	/**
	 * @param category the category to set
	 */
	public void setCategory(ServiceCategory category) {
		this.category = category;
	}
	
	
}
