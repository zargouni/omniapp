package com.omniacom.omniapp.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.omniacom.omniapp.entity.Notification;
import com.omniacom.omniapp.entity.User;

@Repository
public interface NotificationRepository extends CrudRepository<Notification, Long> {

	Notification findByUser(User user);

	@Query("select n from Notification n where n.user.id=:userId ORDER BY n.createdAt DESC")
	List<Notification> userNotifications(@Param("userId") Long userId,Pageable pageSize);
	
	@Query("select n from Notification n where n.user.id=:userId ORDER BY n.createdAt DESC")
	List<Notification> userNotifications(@Param("userId") Long userId);
	
	@Query("select n from Notification n where n.task.id=:taskId and n.user.id=:userId")
	Notification findByUserAndTask(@Param("userId") Long userId, @Param("taskId") Long taskId);
	
	@Query("select n from Notification n where n.issue.id=:issueId and n.user.id=:userId")
	Notification findByUserAndIssue(@Param("userId") Long userId, @Param("issueId") Long issueId);
	
	@Query("select n from Notification n where n.user.id=:userId and n.isRead = false ORDER BY n.createdAt DESC")
	List<Notification> unseenUserNotifications(@Param("userId") Long userId);

	Notification findByUserAndId(User user,Long id);
}
