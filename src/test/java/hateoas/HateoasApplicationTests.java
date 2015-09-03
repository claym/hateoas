package hateoas;

import java.net.URI;
import java.net.URISyntaxException;

import javax.annotation.meta.When;

import org.junit.After;
import org.junit.Assert;
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
import org.springframework.web.client.RestTemplate;

import com.google.common.collect.ImmutableMap;

import hateoas.domain.Category;
import hateoas.domain.Property;
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
public class HateoasApplicationTests {

	private URI category1Location;
	private URI category2Location;
	private URI widget1Location;
	private URI widget2Location;
	private URI property1Location;
	private URI property2Location;

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

	/**
	 * String widgetUrl = baseUrl + "/widget"; String categoryUrl = baseUrl +
	 * "/category"; String detailUrl = baseUrl + "/widgetDetail"; String
	 * propertyUrl = baseUrl + "/property";
	 **/
	@Before
	public void setup() {
		log.info("in setup");
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
	public void exposedIdsWorking() {
		Widget widget = new Widget("Expose Ids");
		URI location = restTemplate.postForLocation(this.templateUrl, widget, "widget");
		Widget repoWidget = widgetRepo.findByName("Expose Ids");
		Assert.assertNotNull(repoWidget);
		Assert.assertNotNull(repoWidget.getId());
		log.info("Repo widget: " + repoWidget);
		Widget restWidget = restTemplate.getForObject(location, Widget.class);
		log.info("Rest widget: " + restWidget);
		Assert.assertNotNull(restWidget);
		Assert.assertNotNull(restWidget.getId());
		Assert.assertEquals(repoWidget.getId(), restWidget.getId());
	}

	// fails, should work
	@Test
	public void returnBodyOnCreate() {
		Widget widget = new Widget("Return On Create");
		Widget createdWidget = restTemplate.postForObject(this.templateUrl, widget, Widget.class, "widget");
		Assert.assertNotNull(createdWidget);
		Assert.assertNotNull(createdWidget.getId());
		log.info(createdWidget.toString());
	}

	public void FAILreturnBodyOnUpdate() {
		Widget widget = new Widget("Return On Update");
		URI location = restTemplate.postForLocation(this.templateUrl, widget, Widget.class, "widget");
		Assert.assertNotNull(location);
		Widget updateWidget = restTemplate.getForObject(location, Widget.class);
		updateWidget.setName("Updated Widget Name");
		Widget returnWidget = restTemplate.postForObject(location, updateWidget, Widget.class);
		Assert.assertNull(returnWidget.getId());
		log.info(returnWidget.toString());
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
