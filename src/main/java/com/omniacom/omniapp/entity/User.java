package com.omniacom.omniapp.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

@Entity
public class User implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column( nullable = false, unique = true, updatable = false)
	@NotEmpty(message = "Please provide a username")
	private String userName;
	private String password;
	
	@NotEmpty(message = "Please provide your first name")
	private String firstName;
	
	@NotEmpty(message = "Please provide your last name")
	private String lastName;
	
	private String profilePic;
	
	private Date registerDate;
	
	@Column(name = "email", nullable = false, unique = true)
	@Email(message = "Please provide a valid e-mail")
	@NotEmpty(message = "Please provide an e-mail")
	private String email;
	
	private String zohoToken;
	private long zohoId;
	
	private String confirmationToken;
	
	private boolean enabled;

	@ManyToOne
	private Role role;

	@OneToMany(mappedBy = "owner")
	private List<Project> ownedProjects = new ArrayList<Project>();
	
	@ManyToMany
	@JoinTable(name = "USER_PROJECTS", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "project_id", referencedColumnName = "id"))
	private List<Project> contributedProjectList = new ArrayList<Project>();

	@OneToMany(mappedBy = "responsible")
	private List<Operation> operations = new ArrayList<Operation>();

	@ManyToMany(mappedBy="workingUsersList")
	private List<Operation> contributedOperationList = new ArrayList<Operation>();

	@ManyToMany(mappedBy="users")
	private List<Task> tasks = new ArrayList<Task>();
	
	@OneToMany(mappedBy="closedBy")
	private List<Task> closedTasks = new ArrayList<Task>();
	
	@OneToMany(mappedBy="closedBy")
	private List<Issue> closedIssues = new ArrayList<Issue>();

	@OneToMany(mappedBy = "user")
	private List<Comment> comments = new ArrayList<Comment>();
	
	@OneToMany(mappedBy = "user")
	private List<Snag> snags = new ArrayList<Snag>();
	
	@OneToMany(mappedBy="user")
	private List<Notification> notifications;
	
	@OneToMany(mappedBy = "creator")
	private List<Issue> createdIssues;
	
	@ManyToMany(mappedBy = "assignedUsers")
	private List<Issue> issues;

	public User() {

	}

	public User(String userName, String password, String email) {
		this.userName = userName;
		this.password = password;
		this.email = email;
	}

	public User(String userName, String password, String email, String zohoToken, long zohoId) {
		this.userName = userName;
		this.password = password;
		this.email = email;
		this.zohoToken = zohoToken;
		this.zohoId = zohoId;
	}

	public User(long id, String userName, String password, String email, String zohoToken, long zohoId) {
		this.id = id;
		this.userName = userName;
		this.password = password;
		this.email = email;
		this.zohoToken = zohoToken;
		this.zohoId = zohoId;
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @return the zohoToken
	 */
	public String getZohoToken() {
		return zohoToken;
	}

	/**
	 * @return the zohoId
	 */
	public long getZohoId() {
		return zohoId;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @param userName
	 *            the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @param email
	 *            the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @param zohoToken
	 *            the zohoToken to set
	 */
	public void setZohoToken(String zohoToken) {
		this.zohoToken = zohoToken;
	}

	/**
	 * @param zohoId
	 *            the zohoId to set
	 */
	public void setZohoId(long zohoId) {
		this.zohoId = zohoId;
	}

	/**
	 * @return the role
	 */
	public Role getRole() {
		return role;
	}

	/**
	 * @return the ownedProjects
	 */
	public List<Project> getOwnedProjects() {
		return ownedProjects;
	}

	/**
	 * @return the contributedProjectList
	 */
	public List<Project> getContributedProjectList() {
		return contributedProjectList;
	}

	/**
	 * @return the operations
	 */
	public List<Operation> getOperations() {
		return operations;
	}

	/**
	 * @return the contributedOperationList
	 */
	public List<Operation> getContributedOperationList() {
		return contributedOperationList;
	}

	/**
	 * @return the tasks
	 */
	public List<Task> getTasks() {
		return tasks;
	}

	/**
	 * @return the comments
	 */
	public List<Comment> getComments() {
		return comments;
	}

	/**
	 * @param role
	 *            the role to set
	 */
	public void setRole(Role role) {
		this.role = role;
	}

	/**
	 * @param ownedProjects
	 *            the ownedProjects to set
	 */
	public void setOwnedProjects(List<Project> ownedProjects) {
		this.ownedProjects = ownedProjects;
	}

	/**
	 * @param contributedProjectList
	 *            the contributedProjectList to set
	 */
	public void setContributedProjectList(List<Project> contributedProjectList) {
		this.contributedProjectList = contributedProjectList;
	}

	/**
	 * @param operations
	 *            the operations to set
	 */
	public void setOperations(List<Operation> operations) {
		this.operations = operations;
	}

	/**
	 * @param contributedOperationList
	 *            the contributedOperationList to set
	 */
	public void setContributedOperationList(List<Operation> contributedOperationList) {
		this.contributedOperationList = contributedOperationList;
	}

	/**
	 * @param tasks
	 *            the tasks to set
	 */
	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;
	}

	/**
	 * @param comments
	 *            the comments to set
	 */
	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}
	
	public void addTask(Task task) {
		this.tasks.add(task);
		task.getUsers().add(this);
	}
	
	public void addProject(Project project) {
		this.contributedProjectList.add(project);
		project.getWorkingUsersList().add(this);
	}
	
	public void addOperation(Operation operation) {
		this.contributedOperationList.add(operation);
		operation.getWorkingUsersList().add(this);
	}
	

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "User [id=" + id + ", userName=" + userName + ", password=" + password + "]";
	}

	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @return the notifications
	 */
	public List<Notification> getNotifications() {
		return notifications;
	}

	/**
	 * @param notifications the notifications to set
	 */
	public void setNotifications(List<Notification> notifications) {
		this.notifications = notifications;
	}

	/**
	 * @return the snags
	 */
	public List<Snag> getSnags() {
		return snags;
	}

	/**
	 * @param snags the snags to set
	 */
	public void setSnags(List<Snag> snags) {
		this.snags = snags;
	}

	/**
	 * @return the createdIssues
	 */
	public List<Issue> getCreatedIssues() {
		return createdIssues;
	}

	/**
	 * @return the issues
	 */
	public List<Issue> getIssues() {
		return issues;
	}

	/**
	 * @param createdIssues the createdIssues to set
	 */
	public void setCreatedIssues(List<Issue> createdIssues) {
		this.createdIssues = createdIssues;
	}

	/**
	 * @param issues the issues to set
	 */
	public void setIssues(List<Issue> issues) {
		this.issues = issues;
	}

	/**
	 * @return the closedTasks
	 */
	public List<Task> getClosedTasks() {
		return closedTasks;
	}

	/**
	 * @param closedTasks the closedTasks to set
	 */
	public void setClosedTasks(List<Task> closedTasks) {
		this.closedTasks = closedTasks;
	}

	/**
	 * @return the registerDate
	 */
	public Date getRegisterDate() {
		return registerDate;
	}

	/**
	 * @param registerDate the registerDate to set
	 */
	public void setRegisterDate(Date registerDate) {
		this.registerDate = registerDate;
	}

	/**
	 * @return the closedIssues
	 */
	public List<Issue> getClosedIssues() {
		return closedIssues;
	}

	/**
	 * @param closedIssues the closedIssues to set
	 */
	public void setClosedIssues(List<Issue> closedIssues) {
		this.closedIssues = closedIssues;
	}

	/**
	 * @return the profilePic
	 */
	public String getProfilePic() {
		return profilePic;
	}

	/**
	 * @param profilePic the profilePic to set
	 */
	public void setProfilePic(String profilePic) {
		this.profilePic = profilePic;
	}

	/**
	 * @return the confirmationToken
	 */
	public String getConfirmationToken() {
		return confirmationToken;
	}

	/**
	 * @param confirmationToken the confirmationToken to set
	 */
	public void setConfirmationToken(String confirmationToken) {
		this.confirmationToken = confirmationToken;
	}

	/**
	 * @return the enabled
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * @param enabled the enabled to set
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

}
