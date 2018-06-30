package com.omniacom.omniapp.data;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.omniacom.StaticString;
import com.omniacom.omniapp.entity.BillOfQuantities;
import com.omniacom.omniapp.entity.Issue;
import com.omniacom.omniapp.entity.Operation;
import com.omniacom.omniapp.entity.Project;
import com.omniacom.omniapp.entity.Service;
import com.omniacom.omniapp.entity.Task;
import com.omniacom.omniapp.entity.User;
import com.omniacom.omniapp.repository.custom.ProjectRepositoryCustom;

@Repository
@Transactional
public class ProjectRepositoryImpl implements ProjectRepositoryCustom {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public List<Operation> findAllOperations(Project project) {
		// TODO Auto-generated method stub
		List<Operation> operations = null;
		Query query = entityManager.createQuery("SELECT op FROM Operation op WHERE op.project=:param")
				.setParameter("param", project);
		operations = (List<Operation>) query.getResultList();
		project.setOperations(operations);
		return project.getOperations();

	}

	@Override
	public List<User> findContributingUsers(Project project) {
		// DONE
		List<User> users = null;
		Query query = entityManager.createQuery(
				"SELECT u FROM User u, Project p JOIN p.workingUsersList team WHERE p.id = :param AND team.id = u.id")
				.setParameter("param", project.getId());
		users = (List<User>) query.getResultList();
		return users;
	}

	@Override
	public List<Task> findAllCompletedTasks(Project project) {
		List<Task> tasks = null;
		Query query = entityManager.createQuery(
				"SELECT t FROM Task t WHERE (t.service IN (SELECT s.id FROM Service s WHERE s.operation IN (SELECT op.id FROM Operation op WHERE op.project = :param ))"
				+ "OR t.service.project = :param) "
				+ " AND t.status = :param2")
				.setParameter("param", project).setParameter("param2", StaticString.TASK_STATUS_COMPLETED);
		tasks = (List<Task>) query.getResultList();

		return tasks;
	}

	@Override
	public List<Task> findAllOnGoingTasks(Project project) {
		List<Task> tasks = null;
		Query query = entityManager.createQuery(
				"SELECT t FROM Task t WHERE (t.service IN (SELECT s.id FROM Service s WHERE s.operation IN (SELECT op.id FROM Operation op WHERE op.project = :param ))"
				+ "OR t.service.project = :param )"
				+ " AND t.status = :param2")
				.setParameter("param", project).setParameter("param2", StaticString.TASK_STATUS_ONGOING);
		tasks = (List<Task>) query.getResultList();

		return tasks;
	}
	
	@Override
	public List<Task> findAllTasks(Project project) {
		List<Task> tasks = null;
		Query query = entityManager.createQuery(
				"SELECT t FROM Task t WHERE (t.service IN (SELECT s.id FROM Service s WHERE s.operation IN (SELECT op.id FROM Operation op WHERE op.project = :param ))"
				+ "OR t.service.project = :param)")
				.setParameter("param", project);
		tasks = (List<Task>) query.getResultList();

		return tasks;
	}

	@Override
	public List<Service> findAllServices(Project project) {
		List<Service> services = null;
		Query query = entityManager.createQuery("SELECT s FROM Service s WHERE s.project=:param OR s.operation IN ("
				+ "SELECT op FROM Operation op WHERE op.project=:param ) ORDER BY s.creationDate")
				.setParameter("param", project);
		services = (List<Service>) query.getResultList();
		return services;
	}

	@Override
	public List<BillOfQuantities> findAllBoqs(Project project) {
		List<BillOfQuantities> boqs = null;
		Query query = entityManager.createQuery("SELECT boq FROM BillOfQuantities boq WHERE boq.project=:param")
				.setParameter("param", project);
		boqs = (List<BillOfQuantities>) query.getResultList();
		return boqs;
	}
	
	@Override
	public Integer findProjectUnassignedTasksCount(Project project) {
		Query query = entityManager.createQuery("SELECT t FROM Task t WHERE ( t.service "
				+ "IN (SELECT s FROM Service s WHERE s.operation IN (SELECT op FROM Operation op WHERE op.project.id = :param) )"
				+ "OR t.service.project.id = :param)"
				+ "AND size(t.users) = 0")
				.setParameter("param", project.getId());
		return query.getResultList().size();
	}

	@Override
	public Integer findProjectOverdueTasksCount(Project project) {
		Integer i = 0;
		List<Task> tasks = findAllTasks(project);
		for(Task t : tasks) {
			if(t.getEndDate().before(new Date()) && t.getStatus().equals(StaticString.TASK_STATUS_ONGOING))
				i++;
		}
		return i;
	}

	@Override
	public List<Project> getUnsyncProjects() {
		List<Project> projects = null;
		Query query = entityManager.createQuery(
				"SELECT p FROM Project p WHERE p.zohoId = 0");
		projects = (List<Project>) query.getResultList();
		return projects;
	}

	@Override
	public List<Operation> findAllUnsyncedOperations(Project project) {
		List<Operation> operations = null;
		Query query = entityManager.createQuery(
				"SELECT op FROM Operation op WHERE op.project.id = :param AND op.zohoId = 0")
				.setParameter("param", project.getId());
		operations = (List<Operation>) query.getResultList();
		return operations;
	}

	@Override
	public List<Issue> findAllIssues(Project project) {
		List<Issue> issues = null;
		Query query = entityManager.createQuery("SELECT issue FROM Issue issue WHERE issue.project=:param")
				.setParameter("param", project);
		issues = (List<Issue>) query.getResultList();
		return issues;
	}

	@Override
	public Integer findProjectUnassignedIssuesCount(Project project) {
		Query query = entityManager.createQuery("SELECT issue FROM Issue issue WHERE issue.project.id = :param"
				+ " AND size(issue.assignedUsers) = 0 ")
				.setParameter("param", project.getId());
		return query.getResultList().size();
	}

	@Override
	public Integer findProjectOverdueIssuesCount(Project project) {
		Integer i = 0;
		List<Issue> issues = findAllIssues(project);
		for(Issue issue : issues) {
			if(issue.getEndDate().before(new Date()) && !issue.getStatus().equals(StaticString.ISSUE_STATUS_CLOSED))
				i++;
		}
		return i;
	}

	

	

}
