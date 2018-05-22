package com.omniacom.omniapp.config;


import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ITemplateResolver;

import com.omniacom.omniapp.converter.ClientConverter;
import com.omniacom.omniapp.converter.DateConverter;
import com.omniacom.omniapp.converter.NatureConverter;
import com.omniacom.omniapp.converter.ProjectConverter;
import com.omniacom.omniapp.converter.ServiceConverter;
import com.omniacom.omniapp.converter.SiteConverter;

@Configuration
@ComponentScan(basePackages = { "com.omniacom.omniapp.controller",
"com.omniacom.omniapp.validator" })
public class ThymeleafWebMvcConfig extends WebMvcConfigurerAdapter {

	@Bean
	public ViewResolver viewResolver() {
		ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
		viewResolver.setTemplateEngine(templateEngine());
		return viewResolver;
	}

	@Bean
	public ITemplateEngine templateEngine() {
		SpringTemplateEngine templateEngine = new SpringTemplateEngine();
		templateEngine.setTemplateResolver(templateResolver());
		// templateEngine.addDialect(new LayoutDialect());
		return templateEngine;
	}
	

	private ITemplateResolver templateResolver() {
		SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
		templateResolver.setPrefix("classpath:/templates/");
		templateResolver.setSuffix(".html");
		templateResolver.setTemplateMode(TemplateMode.HTML);
		templateResolver.setCharacterEncoding("UTF-8");
		templateResolver.setCacheable(false);
		return templateResolver;
	}

	@Override
	public void addFormatters(FormatterRegistry registry) {
		registry.addConverter(new ClientConverter());
		registry.addConverter(new ProjectConverter());
		registry.addConverter(new ServiceConverter());
		registry.addConverter(new DateConverter());
		registry.addConverter(new NatureConverter());
		registry.addConverter(new SiteConverter());

	}

	

}
