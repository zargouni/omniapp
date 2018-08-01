package com.omniacom;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.quartz.JobDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.multipart.support.MultipartFilter;

import com.omniacom.omniapp.config.SchedulerConfig;
import com.omniacom.omniapp.entity.Project;
import com.omniacom.omniapp.repository.BoqRepository;
import com.omniacom.omniapp.repository.BoqServiceRepository;
import com.omniacom.omniapp.repository.RoleRepository;
import com.omniacom.omniapp.repository.ServiceTemplateRepository;
import com.omniacom.omniapp.repository.UserRepository;
import com.omniacom.omniapp.service.ProjectService;
import com.omniacom.omniapp.zohoAPI.SyncZohoPortal;


import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;
import static org.quartz.JobBuilder.*;
import static org.quartz.TriggerBuilder.*;
import static org.quartz.SimpleScheduleBuilder.*;

//@Import({ SchedulerConfig.class })
@SpringBootApplication
public class OmniApp {

	@Autowired
	BoqRepository boqRepo;

	@Autowired
	ServiceTemplateRepository stRepo;

	@Autowired
	BoqServiceRepository boqStRepo;
	
	@Autowired
	public ProjectService projectService;
	
	//public static List<Project> unsyncedProjects;
//	@Autowired
//	SyncZohoPortal syncService;

	public static void main(String[] args) throws Exception {
		SpringApplication.run(OmniApp.class, args);
		//testSync();


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

	@Autowired
	UserRepository userRepo;

	@Autowired
	RoleRepository roleRepo;

	
	private static void testSync() throws SchedulerException {
		
		//System.out.println("elements: "+projectService.getUnsyncProjects().size());
		
		//unsyncedProjects = projectService.getUnsyncProjects();
		
		 Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
		 
		// define the job and tie it to our MyJob class
		  JobDetail job = newJob(SyncZohoPortal.class)
		      .withIdentity("Sync Zoho projects", "Sync")
		      .build();

		  // Trigger the job to run now, and then repeat every 40 seconds
		  Trigger trigger = newTrigger()
		      .withIdentity("trigger1", "group1")
		      .startNow()
		      .withSchedule(simpleSchedule()
		              .withIntervalInMinutes(1)
		              .repeatForever())
		      .build();

		  // Tell quartz to schedule the job using our trigger
		  scheduler.scheduleJob(job, trigger);
		  
		  scheduler.start();
		
		

	}

}
