package com.omniacom.omniapp.data;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.omniacom.omniapp.entity.Operation;
import com.omniacom.omniapp.entity.Project;
import com.omniacom.omniapp.entity.Task;
import com.omniacom.omniapp.entity.User;
import com.omniacom.omniapp.repository.custom.UserRepositoryCustom;
import com.omniacom.omniapp.zohoAPI.Utils;

@Repository
@Transactional
public class UserRepositoryImpl implements UserRepositoryCustom {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public User findOneByUserName(String userName) {
		User user = null;
		Query query = entityManager
				.createQuery("SELECT u FROM User u WHERE userName=:param")
				.setParameter("param",userName);
		List<User> results = (List<User>) query.getResultList();
		if (!results.isEmpty())
			// ignores multiple results
			user = (User) results.get(0);

		return user;
	}

	@Override
	public boolean updateLocalUserFromZoho(User user, String email, String password) {
		String token = Utils.getAuthToken(email, password);
		if (user.getZohoToken() == null) {
			if (!token.equals("INVALID_PASSWORD")) {
				user.setZohoToken(token);
				entityManager.merge(user);
				entityManager.flush();
				return true;
			}
		}
		return false;
	}

	@Override
	public List<Project> findContributedProjects(User user) {
		List<Project> projects = null;
		Query query = entityManager
				.createQuery(
				"SELECT p FROM Project p, User u JOIN u.contributedProjectList project WHERE u.id = :param AND project.id = p.id")
				.setParameter("param", user.getId());
		projects = (List<Project>) query.getResultList();
		return projects;
	}

	@Override
	public List<Project> findOwnedProjects(User user) {
		List<Project> projects = null;
		Query query = entityManager
				.createQuery("SELECT p FROM Project p WHERE p.owner=:param")
				.setParameter("param",
				user);
		projects = (List<Project>) query.getResultList();
		return projects;
	}

	@Override
	public List<Task> findAllTasks(User user) {
		List<Task> tasks = null;
		Query query = entityManager
				.createQuery("SELECT t FROM Task t, User u JOIN u.tasks task WHERE u.id = :param AND task.id = t.id")
				.setParameter("param", user.getId());
		tasks = (List<Task>) query.getResultList();
		return tasks;
	}

	@Override
	public List<Operation> findContributedOperations(User user) {
		List<Operation> operations = null;
		Query query = entityManager
				.createQuery(
				"SELECT o FROM Operation o, User u JOIN u.contributedOperationList operation WHERE u.id = :param AND operation.id = o.id")
				.setParameter("param", user.getId());
		operations = (List<Operation>) query.getResultList();
		return operations;
	}

}
