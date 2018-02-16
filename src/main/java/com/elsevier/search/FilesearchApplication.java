package com.elsevier.search;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import com.elsevier.search.service.FileSearchService;

@SpringBootApplication(scanBasePackages={"com.elsevier.search"})//
public class FilesearchApplication {

	public static void main(String[] args) {
		SpringApplication.run(FilesearchApplication.class, args);
	}
}



@Configuration
@PropertySource("classpath:application.properties")
class AllConfiguration{
	@Bean
	FileSearchService fileSearchService(){
		return new FileSearchService();
	}
	
	@Bean
	ResourceConfig config(FileSearchService fs){
		ResourceConfig resourceConfig=new ResourceConfig();
		resourceConfig.register(fs);
		return resourceConfig;
	}
}

/*@Configuration
@PropertySource("classpath:application.properties")
class PropertiesWithJavaConfig {
 
   @Bean
   public static PropertySourcesPlaceholderConfigurer
     propertySourcesPlaceholderConfigurer() {
      return new PropertySourcesPlaceholderConfigurer();
   }
}*/