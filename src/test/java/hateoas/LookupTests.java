package hateoas;

import java.net.URI;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.client.Hop;
import org.springframework.hateoas.client.Traverson;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

import hateoas.domain.Widget;
import hateoas.repository.CategoryRepository;
import hateoas.repository.PropertyRepository;
import hateoas.repository.WidgetDetailRepository;
import hateoas.repository.WidgetRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = HateoasApplication.class)
@WebIntegrationTest(randomPort = true)
@TestPropertySource(locations = "classpath:test.properties")
public class LookupTests {

	@Autowired
	WidgetRepository widgetRepo;

	@Autowired
	WidgetDetailRepository detailRepo;

	@Autowired
	CategoryRepository categoryRepo;

	@Autowired
	PropertyRepository propertyRepo;

	@Autowired
	RestTemplate restTemplate;

	@Value("${local.server.port}")
	int port;

	String baseUrl = "http://localhost:" + port;
	String templateUrl = baseUrl + "/{path}";

	@Before
	public void setup() {
		this.baseUrl = "http://localhost:" + port;
		this.templateUrl = this.baseUrl + "/{path}";
	}

	@After
	public void tearDown() {
		detailRepo.deleteAll();
		widgetRepo.deleteAll();
		categoryRepo.deleteAll();
		propertyRepo.deleteAll();
	}

	@Test
	public void traversonSearch() {
		Widget w = widgetRepo.save(new Widget("Single"));
		Traverson t = new Traverson(URI.create(baseUrl), MediaTypes.HAL_JSON);

		ParameterizedTypeReference<Resource<Widget>> resourceParameterizedTypeReference = new ParameterizedTypeReference<Resource<Widget>>() {
		};

		Resource<Widget> widget = t.follow("widget", "search")
				.follow(Hop.rel("findByName").withParameter("name", "Single"))
				.toObject(resourceParameterizedTypeReference);
		// ImmutableMap.of("name", "Single")
		Assert.hasText(widget.getLink(Link.REL_SELF).getHref());
		Assert.isTrue("Single".equals(widget.getContent().getName()));
		log.info("Widget Link: {}", widget.getLink(Link.REL_SELF).getHref());
		log.info("Widget {}", widget.getContent());
	}
	
	//Fails
	@Test
	public void traversonEncodedSearch() {
		Widget w = widgetRepo.save(new Widget("Black & White"));
		Traverson t = new Traverson(URI.create(baseUrl), MediaTypes.HAL_JSON);

		ParameterizedTypeReference<Resource<Widget>> resourceParameterizedTypeReference = new ParameterizedTypeReference<Resource<Widget>>() {
		};

		Resource<Widget> widget = t.follow("widget", "search")
				.follow(Hop.rel("findByName").withParameter("name", "Black & White"))
				.toObject(resourceParameterizedTypeReference);
		// ImmutableMap.of("name", "Single")
		Assert.hasText(widget.getLink(Link.REL_SELF).getHref());
		Assert.isTrue("Black & White".equals(widget.getContent().getName()));
		log.info("Widget Link: {}", widget.getLink(Link.REL_SELF).getHref());
		log.info("Widget {}", widget.getContent());
	}
}
