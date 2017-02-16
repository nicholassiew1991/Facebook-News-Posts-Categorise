package shared.mongodb.repo.NewsRepo;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.CrudRepository;
import shared.models.news.NewsInfo;

/**
 * Created by Nicholas on 11/7/2016.
 */
public interface NewsInfoRepo extends CrudRepository<NewsInfo, Long> {

	@Query("{'_id' : ?0}")
	public NewsInfo searchById(String id);
}
