package shared.mongodb.repo.FacebookPostsRepo;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.CrudRepository;
import shared.models.facebook.Page;

/**
 * Created by Nicholas on 28/4/2016.
 */
public interface PagesRepo extends CrudRepository<Page, Long> {

	/*@Query("{'name' : ?0}")
	public Iterable<Person> searchByName(String personName);*/

	@Query("{'_id' : ?0}")
	public Page searchById(String id);
}
