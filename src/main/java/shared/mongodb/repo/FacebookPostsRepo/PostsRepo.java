package shared.mongodb.repo.FacebookPostsRepo;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.CrudRepository;
import shared.models.facebook.Post;

/**
 * Created by Nicholas on 28/4/2016.
 */
public interface PostsRepo extends CrudRepository<Post, Long> {

	@Query("{'_id' : ?0}")
	public Post searchPostById(String id);
}