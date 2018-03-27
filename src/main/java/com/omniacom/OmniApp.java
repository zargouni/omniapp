package com.omniacom;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.omniacom.omniapp.data.UserRepositoryImpl;
import com.omniacom.omniapp.entity.Client;
import com.omniacom.omniapp.entity.Comment;
import com.omniacom.omniapp.entity.Operation;
import com.omniacom.omniapp.entity.Project;
import com.omniacom.omniapp.entity.Role;
import com.omniacom.omniapp.entity.Service;
import com.omniacom.omniapp.entity.Snag;
import com.omniacom.omniapp.entity.Task;
import com.omniacom.omniapp.entity.User;
import com.omniacom.omniapp.repository.ClientRepository;
import com.omniacom.omniapp.repository.CommentRepository;
import com.omniacom.omniapp.repository.OperationRepository;
import com.omniacom.omniapp.repository.ProjectRepository;
import com.omniacom.omniapp.repository.RoleRepository;
import com.omniacom.omniapp.repository.ServiceRepository;
import com.omniacom.omniapp.repository.SnagRepository;
import com.omniacom.omniapp.repository.TaskRepository;
import com.omniacom.omniapp.repository.UserRepository;
import com.omniacom.omniapp.service.UserService;
import com.omniacom.omniapp.zohoAPI.OperationsAPI;
import com.omniacom.omniapp.zohoAPI.ProjectsAPI;
import com.omniacom.omniapp.zohoAPI.UsersAPI;

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
		*/
		
		
		Project project = new Project("contribution",new Date(), "test","EUR","France","none");
		project.setOwner(userRepo.findOneByUserName("Iheb"));
		//projectRepo.save(project);
		
		Project project2 = new Project("contribution",new Date(), "test","EUR","France","none");
		project2.setOwner(userRepo.findOneByUserName("Iheb"));

		//projectRepo.save(project2);
		User user1 = new User("xx", "xx", "xx");
		User user2 = new User("aa","aa","aa");
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
		List<Project> projects = userRepo.findContributedProjects(userRepo.findOneByUserName("aa"));
		
		for(Project p : projects) {
			System.out.println(p.toString());
		}*/
		
		
		//System.out.println("Number of created projects by iheb: "+projectRepo.findOwnedProjectsByUser(userRepo.findOneByUserName("xx")).size());
		
		//System.out.println("Number of contributed projects: "+userRepo.findContributedProjects(userRepo.findOneByUserName("aa")).size());
		
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
	
	@Autowired
	UsersAPI usersApi;
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
	
	@Autowired
	private TaskRepository taskRepo;
	
	@Autowired
	private RoleRepository roleRepo;
	
	@Autowired
	private ClientRepository clientRepo;
	
	@PostConstruct
	private void testTaskRepo() {
		/*Task task = new Task("task", "none", "none", new Date(), new Date(), new Date(), 1);
		
		User user = new User("wala","wala","wala");
		User user2 = new User("howa","howa","howa");
		User user3 = new User("howa","howa","howa");

		userRepo.save(user);
		userRepo.save(user2);
		userRepo.save(user3);
		
		task.addUser(user2);
		task.addUser(user);
		task.addUser(user3);
		
		taskRepo.save(task);
		
		System.out.println("Number of users working: "+ taskRepo.findAllUsers(task).size()); */
		
		//Task task = new Task("task1", "none", "none", new Date(), new Date(), new Date(), 1);
		//task.setStatus(StaticString.TASK_STATUS_COMPLETED);
		//Task task2 = new Task("task2", "none", "none", new Date(), new Date(), new Date(), 1);
		//task2.setStatus(StaticString.TASK_STATUS_ONGOING);
		User user2 = new User("wala","wala","wala");
		user2.setFirstName("wala");
		user2.setLastName("zargouni");
		//user2.setZohoToken("ac3d1f234e2dc1807d102298c5a0dac0");
		Role admin = new Role("ADMIN");
	
		roleRepo.save(admin);	
		user2.setRole(admin);
		
		
		userRepo.save(user2);
		//task.addUser(user2);
		//task2.addUser(user2);
		//taskRepo.save(task);
		//taskRepo.save(task2);
		
		Client client = new Client("Ooredoo");
		Client client2 = new Client("Orange");
		
		clientRepo.save(client);
		clientRepo.save(client2);
		
		//Project project = new Project();
		//project.setName("aaaaaaaaa");
		//project.setCreationDate(new Date());
		//project.setClient(client2);
		
		//projectRepo.save(project);
		
		Project proj = new Project();
		proj.setName("xD");
		proj.setCreationDate(new Date());
		
		Operation oper = new Operation();
		oper.setProject(proj);
		
		Service serv = new Service();
		serv.setOperation(oper);
		
		Task task3 = new Task();
		task3.setService(serv);
		task3.setStatus(StaticString.TASK_STATUS_COMPLETED);
		Task task4 = new Task();
		task4.setStatus(StaticString.TASK_STATUS_ONGOING);
		task4.setService(serv);
		
		projectRepo.save(proj);
		operationRepo.save(oper);
		serviceRepo.save(serv);
		taskRepo.save(task3);
		taskRepo.save(task4);
		
		
		//System.out.println("CLient id is: "+clientRepo.findByName("Orange").getName());
		
//		System.out.println("Number of tasks for him: "+ userRepo.findAllTasks(userRepo.findOneByUserName("HIM")).size()); 

	}

}
