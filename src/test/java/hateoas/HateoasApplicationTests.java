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
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.client.Traverson;
import org.springframework.hateoas.mvc.TypeReferences.PagedResourcesType;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

import com.google.common.collect.ImmutableMap;

import hateoas.domain.Category;
import hateoas.domain.Widget;
import hateoas.repository.CategoryRepository;
import hateoas.repository.PropertyRepository;
import hateoas.repository.WidgetDetailRepository;
import hateoas.repository.WidgetRepository;
import lombok.extern.slf4j.Slf4j;

/** This Test File is for making sure things are working correctly **/

@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = HateoasApplication.class)
@WebIntegrationTest(randomPort = true)
public class HateoasApplicationTests {

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

	@Test
	public void contextLoads() {
		log.info("in context load");
	}

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
	public void exposedIds() {
		Widget widget = new Widget("Expose Ids");
		URI location = restTemplate.postForLocation(this.templateUrl, widget, "widget");
		Widget repoWidget = widgetRepo.findByName("Expose Ids");
		Assert.notNull(repoWidget);
		Assert.notNull(repoWidget.getId());
		log.info("Repo widget: " + repoWidget);
		Widget restWidget = restTemplate.getForObject(location, Widget.class);
		log.info("Rest widget: " + restWidget);
		Assert.notNull(restWidget);
		Assert.notNull(restWidget.getId());
		Assert.isTrue(repoWidget.getId().equals(restWidget.getId()));
	}

	// should have multiple fails @
	// Assert.assertNull(resource.getContent().getId());
	@Test
	public void exposedIdsInResponseEntity() {
		Category cat1 = categoryRepo.save(new Category("Category 1"));
		Assert.notNull(cat1);
		Category cat2 = categoryRepo.save(new Category("Category 2"));
		Assert.notNull(cat2);
		Traverson traverson = new Traverson(URI.create(baseUrl), MediaTypes.HAL_JSON);
		PagedResources<Resource<Category>> cats = traverson.follow("category", "self")
				.toObject(new PagedResourcesType<Resource<Category>>() {
				});
		Assert.isTrue(2 == cats.getContent().size());
		for (Resource<Category> resource : cats) {
			Assert.notNull(resource.getContent().getId());
			Assert.notNull(resource.getContent().getName());
		}
	}

	@Test
	public void returnBodyOnCreate() {
		Widget widget = new Widget("Return On Create");
		Widget createdWidget = restTemplate.postForObject(this.templateUrl, widget, Widget.class, "widget");
		Assert.notNull(createdWidget);
		Assert.notNull(createdWidget.getId());
		Assert.notNull(createdWidget.getName());
		log.info(createdWidget.toString());
		Widget verifyWidget = widgetRepo.findByName("Return On Create");
		Assert.notNull(verifyWidget);
		Assert.isTrue("Return On Create".equals(verifyWidget.getName()));
		log.info("Verification Widget: " + verifyWidget.toString());
	}

	@Test
	public void returnBodyOnUpdate() {
		Widget widget = new Widget("Return On Update");
		URI location = restTemplate.postForLocation(this.templateUrl, widget, "widget");
		Assert.notNull(location);
		Widget updateWidget = restTemplate.getForObject(location, Widget.class);
		Assert.notNull(updateWidget);
		Assert.notNull(updateWidget.getId());
		updateWidget.setName("Updated Widget Name");
		// Widget returnWidget = restTemplate.postForObject(location,
		// updateWidget, Widget.class);
		HttpEntity<Widget> httpEntity = new HttpEntity<Widget>(updateWidget);
		ResponseEntity<Widget> returnWidget = restTemplate.exchange(location, HttpMethod.PUT, httpEntity, Widget.class);
		Assert.notNull(returnWidget.getBody().getId());
		Assert.notNull(returnWidget.getBody().getName());
		log.info(returnWidget.toString());
		Widget verifyWidget = widgetRepo.findByName("Updated Widget Name");
		Assert.notNull(verifyWidget);
		Assert.isTrue("Updated Widget Name".equals(verifyWidget.getName()));
		log.info("Verification Widget: " + verifyWidget.toString());
	}

	protected void createAssociationViaPatch(String parentLink, String childLink) {
		RestTemplate template = new RestTemplate();
		HttpHeaders reqHeaders = new HttpHeaders();
		reqHeaders.add(HttpHeaders.CONTENT_TYPE, new MediaType("text", "uri-list").toString());
		HttpEntity<String> reqEntity = new HttpEntity<String>(childLink, reqHeaders);
		ResponseEntity<String> string = template.exchange(parentLink, HttpMethod.PUT, reqEntity, String.class,
				ImmutableMap.of());
	}

	protected void createAssociationViaPut() {

	}

}
