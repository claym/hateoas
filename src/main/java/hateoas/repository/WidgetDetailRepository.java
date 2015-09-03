package hateoas.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import hateoas.domain.WidgetDetail;

@RepositoryRestResource(path = "widgetDetail", itemResourceRel = "widgetDetail", collectionResourceRel = "widgetDetail")
public interface WidgetDetailRepository extends PagingAndSortingRepository<WidgetDetail, Long> {

}
