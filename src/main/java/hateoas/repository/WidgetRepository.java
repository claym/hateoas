package hateoas.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import hateoas.domain.Widget;

@RepositoryRestResource(path = "widget", itemResourceRel = "widget", collectionResourceRel = "widget")
public interface WidgetRepository extends PagingAndSortingRepository<Widget, Long> {
	public Widget findByName(@Param("name") String name);
}
