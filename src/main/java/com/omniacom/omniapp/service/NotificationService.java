package com.omniacom.omniapp.service;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.omniacom.omniapp.entity.Issue;
import com.omniacom.omniapp.entity.Notification;
import com.omniacom.omniapp.entity.Project;
import com.omniacom.omniapp.entity.Task;
import com.omniacom.omniapp.entity.User;
import com.omniacom.omniapp.repository.NotificationRepository;

@Service
public class NotificationService {

	@Autowired
	private NotificationRepository notificationRepository;

	private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

	// public Map<String,Object> updateUserNotification(Notification
	// notification,User user){
	//
	// notification = save(notification);
	// if(notification == null){
	// return
	// KYCUtilities._errorMultipleObject(MessageUtility.getErrorMessage("NotificationNotUpdated"),
	// HttpStatus.INTERNAL_SERVER_ERROR);
	// }
	//
	// return
	// KYCUtilities._successMultipleObject(ObjectMap.objectMap(notification),MessageUtility.getSuccessMessage("NotificationUpdated"));
	// }

	public Notification save(Notification notification) {
		try {
			return notificationRepository.save(notification);
		} catch (Exception e) {
			logger.error("Exception occur while save Notification ", e);
			return null;
		}
	}

	public Notification findByUser(User user) {
		try {
			return notificationRepository.findByUser(user);
		} catch (Exception e) {
			logger.error("Exception occur while fetch Notification by User ", e);
			return null;
		}
	}

	public List<Notification> findByUser(User user, Integer limit) {
		try {
			return notificationRepository.userNotifications(user.getId(), new PageRequest(0, limit));
		} catch (Exception e) {
			logger.error("Exception occur while fetch Notification by User ", e);
			return null;
		}
	}

	public List<Notification> findAllByUser(User user) {
		try {
			return notificationRepository.userNotifications(user.getId());
		} catch (Exception e) {
			logger.error("Exception occur while fetch Notification by User ", e);
			return null;
		}
	}

	public List<Notification> findAllUnseenByUser(User user) {
		try {
			return notificationRepository.unseenUserNotifications(user.getId());
		} catch (Exception e) {
			logger.error("Exception occur while fetch Notification by User ", e);
			return null;
		}
	}

	public Notification createNotificationObject(String message, User user, Task task) {
		return new Notification(message, new Date(), user, task);
	}

	public Notification createIssueNotificationObject(String message, User user, Issue issue) {
		return new Notification(message, new Date(), user, issue);
	}

	public Notification findByUserAndNotificationId(User user, Long notificationId) {
		try {
			return notificationRepository.findByUserAndId(user, notificationId);
		} catch (Exception e) {
			logger.error("Exception occur while fetch Notification by User and Notification Id ", e);
			return null;
		}
	}

	public boolean sendNotification(User user, Task task) {
		if (notificationRepository.findByUserAndTask(user.getId(), task.getId()) == null) {
			Notification notification = this.createNotificationObject("You have a new task " + task.getName(), user,
					task);
			this.save(notification);
			return true;
		}
		return false;
	}

	public boolean sendIssueNotification(User user, Issue issue) {
		if (notificationRepository.findByUserAndIssue(user.getId(), issue.getId()) == null) {
			Notification notification = this.createIssueNotificationObject("You have a new issue " + issue.getName(),
					user, issue);
			this.save(notification);
			return true;
		}
		return false;
	}
	
	public Notification createProjectNotificationObject(String message, User user, Project project) {
		return new Notification(message, new Date(), user, project);
	}
	
	public boolean sendProjectNotification(User user, Project project) {
		if (notificationRepository.findByUserAndProject(user.getId(), project.getId()) == null) {
			Notification notification = this.createProjectNotificationObject("You got a new project to work on : " + project.getName(),
					user, project);
			this.save(notification);
			return true;
		}
		return false;
	}

}