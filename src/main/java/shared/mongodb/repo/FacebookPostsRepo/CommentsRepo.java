package shared.mongodb.repo.FacebookPostsRepo;

import org.springframework.data.repository.CrudRepository;
import shared.models.facebook.Comment;

/**
 * Created by Nicholas on 28/4/2016.
 */
public interface CommentsRepo extends CrudRepository<Comment, Long> {
}