package project.assay;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import project.assay.dto.MeasureUpdateDTO;
import project.assay.models.Referent;

@SpringBootApplication
public class AssayApplication {

	public static void main(String[] args) {
		SpringApplication.run(AssayApplication.class, args);
	}


	@Bean
	ModelMapper modelMapper(){
		ModelMapper modelMapper = new ModelMapper();

		return modelMapper;
	}

}
