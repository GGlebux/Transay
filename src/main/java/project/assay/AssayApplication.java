package project.assay;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import project.assay.dto.MeasureUpdateDTO;
import project.assay.dto.MeasureDTO;
import project.assay.models.Indicator;
import project.assay.models.Referent;

@SpringBootApplication
public class AssayApplication {

	public static void main(String[] args) {
		SpringApplication.run(AssayApplication.class, args);
	}


	@Bean
	ModelMapper modelMapper(){
		ModelMapper modelMapper = new ModelMapper();

		// Для конвертации из метода convertToReferent()
		TypeMap<MeasureUpdateDTO, Referent> typeMap = modelMapper.createTypeMap(MeasureUpdateDTO.class, Referent.class);
		typeMap.addMappings(mapper -> mapper.skip(Referent::setId));

		// Для конвертации из метода convertToMeasureDTO()

		TypeMap<Indicator, MeasureDTO> indicatorTypeMap = modelMapper.createTypeMap(Indicator.class, MeasureDTO.class);
		TypeMap<Referent, MeasureDTO> referentTypeMap = modelMapper.createTypeMap(Referent.class, MeasureDTO.class);

		return modelMapper;
	}

}
