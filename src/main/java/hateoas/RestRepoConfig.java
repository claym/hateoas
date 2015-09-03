package hateoas;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;

import hateoas.domain.Category;
import hateoas.domain.Property;
import hateoas.domain.Widget;
import hateoas.domain.WidgetDetail;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class RestRepoConfig extends RepositoryRestConfigurerAdapter {

	@Override
	public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
		log.warn("setting config");
		config.setReturnBodyForPutAndPost(true);
		config.setReturnBodyOnCreate(true);
		config.setReturnBodyOnUpdate(true);
		config.exposeIdsFor(Widget.class, Category.class, WidgetDetail.class, Property.class);
		log.warn(config.toString());
	}
}
