package com.omniacom.omniapp.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Client implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String name;
	private String country;
	private String address;
	private String phone;
	private String email;
	
	@OneToMany(mappedBy = "client")
	private List<Project> projects;
	
	@OneToMany(mappedBy = "client")
	private List<Site> sites;
	
	public Client() {

	}


	public Client(String name) {
		this.name = name;
	}
	
	
	public Client(String name, String country, String address, String phone, String email) {
		this.name = name;
		this.country = country;
		this.address = address;
		this.phone = phone;
		this.email = email;
	}
	
	


	public Client(long id, String name, String country, String address, String phone, String email) {
		this.id = id;
		this.name = name;
		this.country = country;
		this.address = address;
		this.phone = phone;
		this.email = email;
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
	 * @return the country
	 */
	public String getCountry() {
		return country;
	}
	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}
	/**
	 * @return the phone
	 */
	public String getPhone() {
		return phone;
	}
	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
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
	 * @param country the country to set
	 */
	public void setCountry(String country) {
		this.country = country;
	}
	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}
	/**
	 * @param phone the phone to set
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}
	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}


	/**
	 * @return the projects
	 */
	public List<Project> getProjects() {
		return projects;
	}


	/**
	 * @return the sites
	 */
	public List<Site> getSites() {
		return sites;
	}


	/**
	 * @param projects the projects to set
	 */
	public void setProjects(List<Project> projects) {
		this.projects = projects;
	}


	/**
	 * @param sites the sites to set
	 */
	public void setSites(List<Site> sites) {
		this.sites = sites;
	}
	
	

}
