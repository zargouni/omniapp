//package com.omniacom.omniapp.entity;
//
//import java.io.Serializable;
//import java.util.Date;
//
//import javax.persistence.Entity;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.Id;
//import javax.persistence.ManyToOne;
//import javax.persistence.PreRemove;
//
//import org.hibernate.annotations.ResultCheckStyle;
//import org.hibernate.annotations.SQLDelete;
//import org.hibernate.annotations.Where;
//import org.springframework.data.annotation.CreatedDate;
//
//@Entity
//@SQLDelete(sql = "UPDATE snag SET deleted = true, deletion_date = CURRENT_TIMESTAMP WHERE id = ?", check = ResultCheckStyle.COUNT)
//@Where(clause = "deleted <> true")
//public class Snag implements Serializable {
//	
//	/**
//	 * 
//	 */
//	private static final long serialVersionUID = 1L;
//	
//	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	private long id;
//	private String title;
//	private String content;
//	
//	@CreatedDate
//	private Date date;
//	private String severity;
//	private long zohoId;
//	
//	@ManyToOne
//	private Operation operation;
//	
//	@ManyToOne
//	private User user;
//	
//	private boolean deleted = false;
//
//	private Date deletionDate;
//
//	@PreRemove
//	public void deleteTask() {
//		this.setDeleted(true);
//		this.setDeletionDate(new Date());
//	}
//	
//	public Snag() {
//
//	}
//
//	public Snag(String title, String content, Date date, String severity) {
//		this.title = title;
//		this.content = content;
//		this.date = date;
//		this.severity = severity;
//	}
//
//	public Snag(String title, String content, Date date, String severity, long zohoId) {
//		this.title = title;
//		this.content = content;
//		this.date = date;
//		this.severity = severity;
//		this.zohoId = zohoId;
//	}
//
//	public Snag(long id, String title, String content, Date date, String severity, long zohoId) {
//		this.id = id;
//		this.title = title;
//		this.content = content;
//		this.date = date;
//		this.severity = severity;
//		this.zohoId = zohoId;
//	}
//	
//	/**
//	 * @return the id
//	 */
//	public long getId() {
//		return id;
//	}
//	/**
//	 * @return the title
//	 */
//	public String getTitle() {
//		return title;
//	}
//	/**
//	 * @return the content
//	 */
//	public String getContent() {
//		return content;
//	}
//	/**
//	 * @return the date
//	 */
//	public Date getDate() {
//		return date;
//	}
//	/**
//	 * @return the severity
//	 */
//	public String getSeverity() {
//		return severity;
//	}
//	/**
//	 * @return the zohoId
//	 */
//	public long getZohoId() {
//		return zohoId;
//	}
//	/**
//	 * @param id the id to set
//	 */
//	public void setId(long id) {
//		this.id = id;
//	}
//	/**
//	 * @param title the title to set
//	 */
//	public void setTitle(String title) {
//		this.title = title;
//	}
//	/**
//	 * @param content the content to set
//	 */
//	public void setContent(String content) {
//		this.content = content;
//	}
//	/**
//	 * @param date the date to set
//	 */
//	public void setDate(Date date) {
//		this.date = date;
//	}
//	/**
//	 * @param severity the severity to set
//	 */
//	public void setSeverity(String severity) {
//		this.severity = severity;
//	}
//	/**
//	 * @param zohoId the zohoId to set
//	 */
//	public void setZohoId(long zohoId) {
//		this.zohoId = zohoId;
//	}
//
//	/**
//	 * @return the operation
//	 */
//	public Operation getOperation() {
//		return operation;
//	}
//
//	/**
//	 * @param operation the operation to set
//	 */
//	public void setOperation(Operation operation) {
//		this.operation = operation;
//	}
//
//	/**
//	 * @return the user
//	 */
//	public User getUser() {
//		return user;
//	}
//
//	/**
//	 * @param user the user to set
//	 */
//	public void setUser(User user) {
//		this.user = user;
//	}
//
//	public boolean isDeleted() {
//		return deleted;
//	}
//
//	public void setDeleted(boolean deleted) {
//		this.deleted = deleted;
//	}
//
//	public Date getDeletionDate() {
//		return deletionDate;
//	}
//
//	public void setDeletionDate(Date deletionDate) {
//		this.deletionDate = deletionDate;
//	}
//	
//	
//
//}
