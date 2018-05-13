package com.omniacom;

import java.util.Date;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.omniacom.omniapp.entity.BillOfQuantities;
import com.omniacom.omniapp.entity.BoqService;
import com.omniacom.omniapp.entity.BoqServiceId;
import com.omniacom.omniapp.entity.Role;
import com.omniacom.omniapp.entity.ServiceTemplate;
import com.omniacom.omniapp.entity.User;
import com.omniacom.omniapp.repository.BoqRepository;
import com.omniacom.omniapp.repository.BoqServiceRepository;
import com.omniacom.omniapp.repository.RoleRepository;
import com.omniacom.omniapp.repository.ServiceTemplateRepository;
import com.omniacom.omniapp.repository.UserRepository;

@SpringBootApplication
public class OmniApp {

	@Autowired
	BoqRepository boqRepo;
	
	@Autowired
	ServiceTemplateRepository stRepo;
	
	@Autowired
	BoqServiceRepository boqStRepo;
	
	public static void main(String[] args) throws Exception {
		SpringApplication.run(OmniApp.class, args);

	}

	@Autowired
	UserRepository userRepo;
	
	@Autowired
	RoleRepository roleRepo;
	
	@PostConstruct
	private void testTaskRepo() {
//		Role admin = new Role("ADMIN");
//		roleRepo.save(admin);
//		User wala = new User("wala","wala","wala");
//		wala.setRole(admin);
//		userRepo.save(wala);
		
		
//		BoqService boqService = new BoqService();
//		
//		BillOfQuantities boq = new BillOfQuantities();
//		boq.setName("boq");
//		boq.setCreationDate(new Date());
//		boq.setEndDate(new Date());
//		boq.setStartDate(new Date());
//		
//		
//		ServiceTemplate st = new ServiceTemplate();
//		st.setName("template1");
//		st.setDescription("description");
//		
//		boqRepo.save(boq);
//		stRepo.save(st);
//		boq.assignBoqServiceToThisBoq(boqService);
//		st.assignBoqServiceToThisTemplate(boqService);
//		
//		boqStRepo.save(boqService);
		
		

	}

}
