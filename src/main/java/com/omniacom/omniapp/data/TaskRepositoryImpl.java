package com.omniacom.omniapp.data;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.omniacom.omniapp.entity.Task;
import com.omniacom.omniapp.entity.User;
import com.omniacom.omniapp.repository.custom.TaskRepositoryCustom;

@Repository
@Transactional
public class TaskRepositoryImpl implements TaskRepositoryCustom {
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public List<User> findAllUsers(Task task) {
		// TODO Auto-generated method stub
		List<User> users = null;
		Query query = entityManager
				.createQuery(
				"SELECT u FROM User u, Task t JOIN t.users users WHERE t.id = :param AND users.id = u.id")
				.setParameter("param", task.getId());
		users = (List<User>) query.getResultList();
		return users;
	}

}
