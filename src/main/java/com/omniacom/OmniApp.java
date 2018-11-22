package com.omniacom;

import java.io.File;
import java.util.Date;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.multipart.support.MultipartFilter;

import com.omniacom.omniapp.entity.Role;
import com.omniacom.omniapp.entity.User;
import com.omniacom.omniapp.repository.RoleRepository;
import com.omniacom.omniapp.repository.UserRepository;

//@Import({ SchedulerConfig.class })
@SpringBootApplication
public class OmniApp {

	// @Autowired
	// BoqRepository boqRepo;
	//
	// @Autowired
	// ServiceTemplateRepository stRepo;
	//
	// @Autowired
	// BoqServiceRepository boqStRepo;
	//
	// @Autowired
	// public ProjectService projectService;

	@Autowired
	UserRepository userRepo;

	@Autowired
	RoleRepository roleRepo;

	// public static List<Project> unsyncedProjects;
	// @Autowired
	// SyncZohoPortal syncService;

	public static void main(String[] args) throws Exception {
		SpringApplication.run(OmniApp.class, args);
		// testSync();

	}

	@Bean
	public CommonsMultipartResolver multipartResolver() {
		CommonsMultipartResolver multipart = new CommonsMultipartResolver();
		multipart.setMaxUploadSize(3 * 1024 * 1024);
		return multipart;
	}

	@Bean
	@Order(0)
	public MultipartFilter multipartFilter() {
		MultipartFilter multipartFilter = new MultipartFilter();
		multipartFilter.setMultipartResolverBeanName("multipartReso‌​lver");
		return multipartFilter;
	}

	// private static void testSync() throws SchedulerException {
	//
	// //System.out.println("elements: "+projectService.getUnsyncProjects().size());
	//
	// //unsyncedProjects = projectService.getUnsyncProjects();
	//
	// Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
	//
	// // define the job and tie it to our MyJob class
	// JobDetail job = newJob(SyncZohoPortal.class)
	// .withIdentity("Sync Zoho projects", "Sync")
	// .build();
	//
	// // Trigger the job to run now, and then repeat every 40 seconds
	// Trigger trigger = newTrigger()
	// .withIdentity("trigger1", "group1")
	// .startNow()
	// .withSchedule(simpleSchedule()
	// .withIntervalInMinutes(1)
	// .repeatForever())
	// .build();
	//
	// // Tell quartz to schedule the job using our trigger
	// scheduler.scheduleJob(job, trigger);
	//
	// scheduler.start();
	//
	//
	//
	// }

	@Autowired
	private PasswordEncoder passwordEncoder;

	@PostConstruct
	private void populateDB() {
		
		//deployResourcesFolder();
		
		Role admin = new Role("ADMIN");
		Role user = new Role("USER");
		Role pm = new Role("PROJECT_MANAGER");
		if (roleRepo.findByName("ADMIN") == null)
			roleRepo.save(admin);
		if (roleRepo.findByName("USER") == null)
			roleRepo.save(user);
		if (roleRepo.findByName("PROJECT_MANAGER") == null)
			roleRepo.save(pm);

		User adminUser = new User("omniapp", "", "admin@omniapp.com");
		adminUser.setPassword(passwordEncoder.encode("omniapp"));
		adminUser.setFirstName("Omniapp");
		adminUser.setLastName("Omniacom");
		adminUser.setRegisterDate(new Date());
		adminUser.setRole(admin);
		adminUser.setEnabled(true);
		if (userRepo.findOneByUserName("omniapp") == null)
			userRepo.save(adminUser);
	}

	private void deployResourcesFolder() {
		String absolutePath = new File("").getAbsolutePath();
		File resourcesFolder = new File(absolutePath + StaticString.RESOURCES_FOLDER);
		File usersFolder = new File(absolutePath + StaticString.RESOURCES_FOLDER + StaticString.USERS_PICS_FOLDER);
		File logosFolder = new File(absolutePath + StaticString.RESOURCES_FOLDER + StaticString.LOGOS_FOLDER);
		if (!resourcesFolder.exists())
			resourcesFolder.mkdirs();
		if (!usersFolder.exists())
			usersFolder.mkdirs();
		if (!logosFolder.exists())
			logosFolder.mkdirs();
		// return new File(directory + "/" + filename);
	}

}
