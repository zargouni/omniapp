package com.omniacom.omniapp.data;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.omniacom.omniapp.entity.Task;
import com.omniacom.omniapp.entity.UploadedFile;
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

	@Override
	public boolean addOneOwner(Task task, User user) {
//		if(task.getService().getOperation() != null) {
//			task.getService().getOperation().getProject().getWorkingUsersList().add(user);
//		}else {
//			task.getService().getProject().getWorkingUsersList().add(user);
//		}
		return task.getUsers().add(user);
	}

	@Override
	public List<UploadedFile> findAllFiles(Task task) {
		List<UploadedFile> files = null;
		Query query = entityManager
				.createQuery(
				"SELECT f FROM UploadedFile f WHERE f.task.id = :param")
				.setParameter("param", task.getId());
		files = (List<UploadedFile>) query.getResultList();
		return files;
	}

	@Override
	public List<Task> findAllSyncedTasks() {
		List<Task> tasks = null;
		Query query = entityManager.createQuery("SELECT t FROM Task t WHERE t.zohoId != 0");
		tasks = (List<Task>) query.getResultList();
		return tasks;
	}

}
