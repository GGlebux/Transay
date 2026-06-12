package project.assay.config;

import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.EnableAsync;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

@Configuration
@ConfigurationProperties(prefix = "app")
@EnableAsync
@Getter
@Setter
public class AppConfig {
    private String frontUrl;

    @Bean
    @Scope(SCOPE_PROTOTYPE)
    ModelMapper modelMapper(){
        return new ModelMapper();
    }
}
