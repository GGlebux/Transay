package project.assay;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

@SpringBootApplication
public class AssayApplication {

	public static void main(String[] args) {
		SpringApplication.run(AssayApplication.class, args);
	}


	@Bean
	@Scope(SCOPE_PROTOTYPE)
	ModelMapper modelMapper(){
		return new ModelMapper();
	}
}
