package project.assay;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

import java.text.SimpleDateFormat;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

@SpringBootApplication
public class AssayApplication {
	public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy");

	public static void main(String[] args) {
		SpringApplication.run(AssayApplication.class, args);
	}


	@Bean
	@Scope(SCOPE_PROTOTYPE)
	ModelMapper modelMapper(){
		return new ModelMapper();
	}
}
