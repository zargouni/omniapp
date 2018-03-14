package com.omniacom.omniapp.data;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.omniacom.omniapp.entity.Project;
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
		// TODO Auto-generated method stub
		User user = null;
		Query query = entityManager.createQuery("select u from User u where userName=:param").setParameter("param",
				userName);
		List<User> results = (List<User>) query.getResultList();
		if (!results.isEmpty())
			// ignores multiple results
			user = (User) results.get(0);

		return user;
	}

	@Override
	public boolean updateLocalUserFromZoho(User user, String email, String password) {
		// TODO Auto-generated method stub
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
		// DONE
		String q = "SELECT p FROM Project p, User u JOIN u.contributedProjectList projects WHERE u.id = :param AND projects.id = p.id";
		List<Project> projects = null;
		Query query = entityManager.createQuery(q).setParameter("param", user.getId());
		projects = (List<Project>) query.getResultList();
		return projects;
	}
	
	@Override
	public List<Project> findOwnedProjects(User user) {
		// TODO Auto-generated method stub
		List<Project> projects = null;
		Query query = entityManager.createQuery("select p from Project p where p.owner=:param").setParameter("param", user);
		projects = (List<Project>) query.getResultList();
		return projects;
	}
	
	
	

}
