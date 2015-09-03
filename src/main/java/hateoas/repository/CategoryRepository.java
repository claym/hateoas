package hateoas.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import hateoas.domain.Category;

@RepositoryRestResource(path = "category", itemResourceRel = "category", collectionResourceRel = "category")
public interface CategoryRepository extends PagingAndSortingRepository<Category, Long> {

}
