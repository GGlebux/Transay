package project.assay.utils.converters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
@Converter(autoApply = true)
public class JsonToListConverter implements AttributeConverter<List<String>, String> {
  private final ObjectMapper objectMapper = new ObjectMapper();

  @Override
  public String convertToDatabaseColumn(List<String> attribute) {
    try {
      return attribute == null ? null : objectMapper.writeValueAsString(attribute); // Обработка null
    } catch (JsonProcessingException e) {
      throw new RuntimeException("Error converting List<String> to JSON", e);
    }
  }

  @Override
  public List<String> convertToEntityAttribute(String dbData) {
    try {
      return dbData == null ? null : objectMapper.readValue(dbData, new TypeReference<>() {}); // Обработка null
    } catch (JsonProcessingException e) {
      throw new RuntimeException("Error converting JSON to List<String>", e);
    }
  }
}
