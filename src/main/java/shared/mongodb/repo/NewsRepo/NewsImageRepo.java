package shared.mongodb.repo.NewsRepo;

import org.springframework.data.repository.CrudRepository;
import shared.models.news.NewsImages;

/**
 * Created by Nicholas on 11/7/2016.
 */
public interface NewsImageRepo extends CrudRepository<NewsImages, Long> {
}
