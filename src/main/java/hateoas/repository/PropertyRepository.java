package hateoas.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import hateoas.domain.Property;

@RepositoryRestResource(path = "property", itemResourceRel = "property", collectionResourceRel = "property")
public interface PropertyRepository extends PagingAndSortingRepository<Property, Long> {

}
