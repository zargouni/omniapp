package com.omniacom.omniapp.data;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.omniacom.omniapp.entity.Comment;
import com.omniacom.omniapp.entity.Operation;
import com.omniacom.omniapp.entity.Project;
import com.omniacom.omniapp.entity.Service;
import com.omniacom.omniapp.entity.Snag;
import com.omniacom.omniapp.entity.User;
import com.omniacom.omniapp.repository.custom.OperationRepositoryCustom;

@Repository
@Transactional
public class OperationRepositoryImpl implements OperationRepositoryCustom {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public List<Service> findAllServices(Operation operation) {
		// TODO Auto-generated method stub
		List<Service> services = null;
		String q = "SELECT s FROM Service s WHERE s.operation.id = :param";
		Query query = entityManager.createQuery(q).setParameter("param", operation.getId());
		services = (List<Service>) query.getResultList();
		return services;
	}

	@Override
	public List<Comment> findAllComments(Operation operation) {
		// TODO Auto-generated method stub
		List<Comment> comments = null;
		String q = "SELECT c FROM Comment c WHERE c.operation.id = :param";
		Query query = entityManager.createQuery(q).setParameter("param", operation.getId());
		comments = (List<Comment>) query.getResultList();
		return comments;
	}

	@Override
	public List<Snag> findAllSnags(Operation operation) {
		// TODO Auto-generated method stub
		List<Snag> snags = null;
		String q = "SELECT c FROM Snag c WHERE c.operation.id = :param";
		Query query = entityManager.createQuery(q).setParameter("param", operation.getId());
		snags = (List<Snag>) query.getResultList();
		return snags;
	}

	@Override
	public List<User> findContributingUsers(Operation operation) {
		List<User> users = null;
		Query query = entityManager
				.createQuery("SELECT u FROM User u, Operation o JOIN o.workingUsersList user WHERE o.id = :param AND user.id = u.id")
				.setParameter("param", operation.getId());
		users = (List<User>) query.getResultList();
		return users;
	}
	
	@Override
	public List<Operation> findAllSyncedOperations() {
		List<Operation> operations = null;
		Query query = entityManager.createQuery(
				"SELECT op FROM Operation op WHERE op.zohoId != 0");
		operations = (List<Operation>) query.getResultList();
		return operations;
	}

	@Override
	public List<Service> findAllUnsyncedServices(Operation op) {
		List<Service> services = null;
		Query query = entityManager.createQuery(
				"SELECT service FROM Service service WHERE service.operation.id = :param AND service.zohoId = 0")
				.setParameter("param", op.getId());
		services = (List<Service>) query.getResultList();
		return services;
	}

}
