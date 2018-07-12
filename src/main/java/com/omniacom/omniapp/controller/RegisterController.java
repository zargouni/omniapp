package com.omniacom.omniapp.controller;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.omniacom.omniapp.entity.User;
import com.omniacom.omniapp.repository.RoleRepository;
import com.omniacom.omniapp.service.EmailService;
import com.omniacom.omniapp.service.UserService;

@Controller
public class RegisterController {

	@Autowired
	UserService userService;

	@Autowired
	EmailService emailService;
	
	@Autowired
	RoleRepository roleRepo;
	
	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;

	@GetMapping("/register")
	public ModelAndView index(ModelAndView modelAndView, User user) {
		modelAndView.addObject("user", user);
		modelAndView.setViewName("register");
		return modelAndView;

	}

	@ModelAttribute("newUser")
	public User getNewUser() {
		return new User();
	}

	@PostMapping("/register")
	public ModelAndView registerNewUser(ModelAndView modelAndView, @Valid User user, BindingResult bindingResult,
			HttpServletRequest request) {

		if (userService.findByEmail(user.getEmail()) != null) {
			modelAndView.addObject("alreadyRegisteredEmailMessage",
					"Oops!  There is already a user registered with the email provided.");
			modelAndView.setViewName("register");
			bindingResult.reject("email");
		}

		if (userService.findByUsername(user.getUserName()) != null) {
			modelAndView.addObject("alreadyRegisteredUsernameMessage",
					"Oops!  There is already a user registered with the username provided.");
			modelAndView.setViewName("register");
			bindingResult.reject("userName");
		}

		if (bindingResult.hasErrors()) {
			modelAndView.setViewName("register");
		} else {
			user.setConfirmationToken(UUID.randomUUID().toString());
			user.setRole(roleRepo.findOne(2L));
			user.setEnabled(false);
			user.setRegisterDate(new Date());
			String appUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();

			userService.save(user);

			SimpleMailMessage registrationEmail = new SimpleMailMessage();
			registrationEmail.setTo(user.getEmail());
			registrationEmail.setSubject("Registration Confirmation");
			registrationEmail.setText("To confirm your e-mail address, please click the link below:\n" + appUrl
					+ "/confirm?token=" + user.getConfirmationToken());
			registrationEmail.setFrom("noreply@omniapp.com");

			emailService.sendEmail(registrationEmail);
			modelAndView.setViewName("login");
			modelAndView.addObject("confirmationMessage", "A confirmation e-mail has been sent to " + user.getEmail());

		}

		return modelAndView;
	}

	// Process confirmation link
	@GetMapping("/confirm")
	public ModelAndView showConfirmationPage(ModelAndView modelAndView, @RequestParam("token") String token) {

		User user = userService.findByConfirmationToken(token);

		if (user == null) { // No token found in DB
			modelAndView.addObject("invalidToken", "Oops!  This is an invalid confirmation link.");
		} else { // Token found
			modelAndView.addObject("confirmationToken", user.getConfirmationToken());
		}

		modelAndView.setViewName("confirm");
		return modelAndView;
	}

	// Process confirmation link
	@PostMapping("/confirm")
	public ModelAndView processConfirmationForm(ModelAndView modelAndView, BindingResult bindingResult,
			@RequestParam Map requestParams, RedirectAttributes redir) {

		modelAndView.setViewName("confirm");

		// Find the user associated with the reset token
		User user = userService.findByConfirmationToken((String) requestParams.get("token"));

		// Set new password
		 user.setPassword(bCryptPasswordEncoder.encode((CharSequence) requestParams.get("password")));

		 // Set user to enabled
		user.setEnabled(true);

		// Save user
		userService.save(user);
		modelAndView.setViewName("login");
		modelAndView.addObject("successMessage", "Your password has been set!");
		return modelAndView;
	}

}
