package com.omniacom.omniapp.entity;

import java.io.Serializable;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

public class LogChange implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private String concernedField;
	
	private String oldValue;
	
	private String newValue;

	@ManyToOne
	private UpdateLog updateLog;
	
	public LogChange() {
		super();
	}

	public LogChange(String concernedField, String oldValue, String newValue) {
		super();
		this.concernedField = concernedField;
		this.oldValue = oldValue;
		this.newValue = newValue;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return the concernedField
	 */
	public String getConcernedField() {
		return concernedField;
	}

	/**
	 * @return the oldValue
	 */
	public String getOldValue() {
		return oldValue;
	}

	/**
	 * @return the newValue
	 */
	public String getNewValue() {
		return newValue;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @param concernedField the concernedField to set
	 */
	public void setConcernedField(String concernedField) {
		this.concernedField = concernedField;
	}

	/**
	 * @param oldValue the oldValue to set
	 */
	public void setOldValue(String oldValue) {
		this.oldValue = oldValue;
	}

	/**
	 * @param newValue the newValue to set
	 */
	public void setNewValue(String newValue) {
		this.newValue = newValue;
	}

	/**
	 * @return the updateLog
	 */
	public UpdateLog getUpdateLog() {
		return updateLog;
	}

	/**
	 * @param updateLog the updateLog to set
	 */
	public void setUpdateLog(UpdateLog updateLog) {
		this.updateLog = updateLog;
	}
	
	
	

}
