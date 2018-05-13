package com.omniacom.omniapp.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
	//private float price;
	private ServiceCategory category;
	
	@OneToMany(mappedBy = "template")
	private List<BoqService> boqServices;
	
	@OneToMany(mappedBy = "serviceTemplate", orphanRemoval=true)
	private List<TaskTemplate> tasks;
	
	
	public ServiceTemplate() {

	}
	
	public ServiceTemplate(String name, String description, float price) {
		this.name = name;
		this.description = description;
		
	}
	public ServiceTemplate(long id, String name, String description, float price) {
		this.id = id;
		this.name = name;
		this.description = description;
		
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
	 * @return the tasks
	 */
	public List<TaskTemplate> getTasks() {
		return tasks;
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

	/**
	 * @return the boqService
	 */
	public List<BoqService> getBoqServices() {
		return boqServices;
	}

	/**
	 * @param boqService the boqService to set
	 */
	public void setBoqServices(List<BoqService> boqServices) {
		this.boqServices = boqServices;
	}
	
	public void assignBoqServicesToThisTemplate(List<BoqService> boqServices) {
		this.setBoqServices(boqServices);
		for(BoqService boqService : boqServices) {
			boqService.setTemplate(this);
		}
	}
	
	public void assignBoqServiceToThisTemplate(BoqService boqService) {
		if(this.getBoqServices() != null)
			this.getBoqServices().add(boqService);
		else {
			List<BoqService> boqServices = new ArrayList<BoqService>(); 
			boqServices.add(boqService);
			this.setBoqServices(boqServices);
		}
		boqService.setTemplate(this);
		
	}
	
	
}
