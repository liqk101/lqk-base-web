package com.bj;

import java.io.File;

import javax.servlet.MultipartConfigElement;

import org.springframework.boot.ApplicationHome;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {	

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		// registry.addViewController("/admin/login").setViewName("admin/login");
		// registry.addViewController("/hello").setViewName("hello");
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**");
	}
    
	@Bean
	public MultipartConfigElement multipartConfigElement() {
		ApplicationHome home = new ApplicationHome(getClass());
	    File dirFile = home.getDir();
	    String tmpPath = dirFile.getPath() + File.separator + "tmp";
	    File tmpDir = new File(tmpPath);
	    if(!tmpDir.exists()) {
	    	tmpDir.mkdirs();
	    }
		MultipartConfigFactory factory = new MultipartConfigFactory();
		factory.setLocation(tmpPath);
		return factory.createMultipartConfig();
	}
}