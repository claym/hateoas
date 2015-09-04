package hateoas;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.hateoas.config.EnableHypermediaSupport.HypermediaType;
import org.springframework.hateoas.hal.Jackson2HalModule;
import org.springframework.hateoas.mvc.TypeConstrainedMappingJackson2HttpMessageConverter;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
@Configuration
//@EnableHypermediaSupport(type= {HypermediaType.HAL})
public class HateoasApplication {

	public static void main(String[] args) {
		SpringApplication.run(HateoasApplication.class, args);
	}

	@Bean
	public RestTemplate restTemplate() {
		RestTemplate restTemplate = new RestTemplate();
		List<HttpMessageConverter<?>> existingConverters = restTemplate.getMessageConverters();
		List<HttpMessageConverter<?>> newConverters = new ArrayList<>();
		newConverters.add(getHalMessageConverter());
		newConverters.addAll(existingConverters);
		restTemplate.setMessageConverters(newConverters);

		HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
		// requestFactory.setConnectTimeout(TIMEOUT);
		// requestFactory.setReadTimeout(TIMEOUT);

		restTemplate.setRequestFactory(requestFactory);
		return restTemplate;
	}

	protected HttpMessageConverter getHalMessageConverter() {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new Jackson2HalModule());
		MappingJackson2HttpMessageConverter halConverter = new TypeConstrainedMappingJackson2HttpMessageConverter(
				ResourceSupport.class);
		halConverter.setSupportedMediaTypes(Arrays.asList(MediaTypes.HAL_JSON));
		halConverter.setObjectMapper(objectMapper);
		return halConverter;
	}
/**
	@Bean
	public ObjectMapper getObjectMapperWithHalModule() {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new Jackson2HalModule());
		return objectMapper;
	}
**/
}
