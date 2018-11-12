package com.omniacom.omniapp.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class UploadedFile {
	private Long id;
	  private String name;
	  private String location;
	  private Long size;
	  private String type;
	  private Date creationDate;
	  
	  private boolean deleted = false;
	  
	  private Task task;
	  
	  private Issue issue;

	  @Id
	  @GeneratedValue(strategy = GenerationType.AUTO)
	  public Long getId() {
	    return id;
	  }

	  @Column(nullable = false)
	  public String getName() {
	    return name;
	  }

	  @Column(nullable = false)
	  public String getLocation() {
	    return location;
	  }

	  @Column()
	  public Long getSize() {
	    return size;
	  }

	  @Column(nullable = false)
	  public String getType() {
	    return type;
	  }

	  public void setId(Long id) {
	    this.id = id;
	  }

	  public void setName(String name) {
	    this.name = name;
	  }

	  public void setLocation(String location) {
	    this.location = location;
	  }

	  public void setSize(Long size) {
	    this.size = size;
	  }

	  public void setType(String type) {
	    this.type = type;
	  }

	
	@ManyToOne
	public Task getTask() {
		return task;
	}

	public void setTask(Task task) {
		this.task = task;
	}

	/**
	 * @return the creationDate
	 */
	public Date getCreationDate() {
		return creationDate;
	}

	/**
	 * @param creationDate the creationDate to set
	 */
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
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

	/**
	 * @return the issue
	 */
	@ManyToOne
	public Issue getIssue() {
		return issue;
	}

	/**
	 * @param issue the issue to set
	 */
	public void setIssue(Issue issue) {
		this.issue = issue;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	/* (non-Javadoc)
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
		UploadedFile other = (UploadedFile) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
