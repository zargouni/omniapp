package com.omniacom.omniapp.data;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.omniacom.StaticString;
import com.omniacom.omniapp.entity.Operation;
import com.omniacom.omniapp.entity.Project;
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
				"SELECT t FROM Task t WHERE t.service IN (SELECT s.id FROM Service s WHERE s.operation IN (SELECT op.id FROM Operation op WHERE op.project = :param )) AND t.status = :param2")
				.setParameter("param", project).setParameter("param2", StaticString.TASK_STATUS_COMPLETED);
		tasks = (List<Task>) query.getResultList();

		return tasks;
	}

	@Override
	public List<Task> findAllOnGoingTasks(Project project) {
		List<Task> tasks = null;
		Query query = entityManager.createQuery(
				"SELECT t FROM Task t WHERE t.service IN (SELECT s.id FROM Service s WHERE s.operation IN (SELECT op.id FROM Operation op WHERE op.project = :param )) AND t.status = :param2")
				.setParameter("param", project).setParameter("param2", StaticString.TASK_STATUS_ONGOING);
		tasks = (List<Task>) query.getResultList();

		return tasks;
	}

}
