package com.omniacom;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.omniacom.omniapp.data.UserRepositoryImpl;
import com.omniacom.omniapp.entity.Comment;
import com.omniacom.omniapp.entity.Operation;
import com.omniacom.omniapp.entity.Project;
import com.omniacom.omniapp.entity.Service;
import com.omniacom.omniapp.entity.Snag;
import com.omniacom.omniapp.entity.User;
import com.omniacom.omniapp.repository.CommentRepository;
import com.omniacom.omniapp.repository.OperationRepository;
import com.omniacom.omniapp.repository.ProjectRepository;
import com.omniacom.omniapp.repository.ServiceRepository;
import com.omniacom.omniapp.repository.SnagRepository;
import com.omniacom.omniapp.repository.UserRepository;
import com.omniacom.omniapp.service.UserService;
import com.omniacom.omniapp.zohoAPI.OperationsAPI;
import com.omniacom.omniapp.zohoAPI.ProjectsAPI;

@SpringBootApplication
public class OmniApp {

	
	@Autowired
	ProjectsAPI papi;

	@Autowired
	ProjectRepository projectRepo;
	/*
	@Autowired
	OperationRepository operationRepo;

	@Autowired
	OperationsAPI opapi;
*/
	@Autowired
	UserService userService;
	
	@Autowired
	UserRepository userRepo;

	public static void main(String[] args) throws Exception {
		SpringApplication.run(OmniApp.class, args);

	}
	public void test() throws Exception {
		/*
		 * Utils.token = Utils.getAuthToken("wala.zargouni@esprit.tn",
		 * "sihemmylove1210"); Project pro = new Project("ay", new Date(), "helloooo",
		 * "TND", "country", "zone"); com.zoho.projects.model.Project project =
		 * papi.pushProject(pro);
		 * System.out.println("ADDED PROJECT: "+project.getName());
		 */

		// System.out.println(papi.deleteProject(project));
		// Project p = projectRepo.findOne(1L);

		/*
		 * Operation op = new Operation("op3", "05-27-2014", "05-29-2014", 1);
		 * op.setProject(pro); operationRepo.save(op);
		 * opapi.pushMilestone(op,project.getIdString());
		 */

		// projectRepo.save(pro);

		// System.out.println(projectRepo.addOperation(op, pro));

		// System.out.println("SIZE: "+projectRepo.findAllOperations(p).size());
		
		//Login Local User plus get Token if !Exists and update user with permanent token
		/*String userName = "walaz";
		String password = "12101993";
		User user = userService.doLogin(userName, password);
		if ( user != null)
			System.out.println("passaran");
		else
			System.out.println("no passaran");
		if(!userService.zohoAuthTokenExists(user))
			System.out.println("no token");
		
		String email="walazargouni@gmail.com";
		String passwordZ = "12101993";
		userRepo.updateLocalUserFromZoho(user, email, passwordZ);*/
		
		
		Project project = new Project("contribution",new Date(), "test","EUR","France","none");
		project.setOwner(userRepo.findOneByUserName("Iheb"));
		//projectRepo.save(project);
		
		Project project2 = new Project("contribution",new Date(), "test","EUR","France","none");
		project2.setOwner(userRepo.findOneByUserName("Iheb"));

		//projectRepo.save(project2);
		User user1 = new User("Iheb", "iheb", "iheb");
		User user2 = new User("mum","mum","mum");
		List<User> team = new ArrayList<User>();
		userRepo.save(user2);
		userRepo.save(user1);

		team.add(user2);
		team.add(user1);
		
		//team.add(user1);
		project.setWorkingUsersList(team);
		project2.setWorkingUsersList(team);
		projectRepo.save(project);
		projectRepo.save(project2);
		
		//project.setOwner(user1);
		
		/*
		List<Project> projects = userRepo.findContributedProjects(userRepo.findOneByUserName("mum"));
		
		for(Project p : projects) {
			System.out.println(p.toString());
		}*/
		
		
		//System.out.println("Number of created projects by iheb: "+projectRepo.findOwnedProjectsByUser(userRepo.findOneByUserName("Iheb")).size());
		
		//System.out.println("Number of contributed projects: "+userRepo.findContributedProjects(userRepo.findOneByUserName("mum")).size());
		
		System.out.println("Number of contributing users : "+projectRepo.findContributingUsers(projectRepo.findOne(1L)).size());
		
	}
	@Autowired
	OperationRepository operationRepo;
	
	@Autowired
	ServiceRepository serviceRepo;

	@Autowired
	CommentRepository commentRepo;

	@Autowired
	SnagRepository snagRepo;
	
	@PostConstruct
	private void testOperationRepo() {
		Operation operation = new Operation("Operation1", "7-11-2017", "15-6-2018", 1);
		Service service1 = new Service("service1", new Date(), 1, 2.2f);
		Service service2 = new Service("service2", new Date(), 1, 2.2f);
		operationRepo.save(operation);
		service1.setOperation(operation);
		service2.setOperation(operation);
		serviceRepo.save(service1);
		serviceRepo.save(service2);
		
		Comment comment1 = new Comment(new Date(), "xx");
		comment1.setOperation(operation);
		commentRepo.save(comment1);
		
		Snag snag1 = new Snag("snag", "cc", new Date(), "barcha");
		snag1.setOperation(operation);
		snagRepo.save(snag1);
		
		Snag snag2 = new Snag("snag2", "cc", new Date(), "barcha");
		snag2.setOperation(operation);
		snagRepo.save(snag2);
		
		System.out.println("Number of services:"+ operationRepo.findAllServices(operation).size());
		System.out.println("Number of comments:"+ operationRepo.findAllComments(operation).size());
		System.out.println("Number of snags:"+ operationRepo.findAllSnags(operation).size());
		
		
		
	}

}
