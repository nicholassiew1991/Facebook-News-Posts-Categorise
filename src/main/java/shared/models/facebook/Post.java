package shared.models.facebook;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;

/**
 * Created by Nicholas Siew on 8/4/2016.
 */
@org.springframework.data.mongodb.core.mapping.Document(collection="posts")
public class Post {

	public static final String ID = "post_id";
	public static final String MESSAGE = "message";
	public static final String STORY = "story";
	public static final String NAME = "name";
	public static final String LINK = "link";
	public static final String LIKE_COUNT = "like_count";
	public static final String SHARE_COUNT = "share_count";
	public static final String CREATE_TIME = "create_time";
	public static final String COMMENTS = "comments";

	@Id
	private String postId;
	@Field
	private String pageId;
	@Field
	private String pageName;
	@Field
	private String message;
	@Field
	private String story;
	@Field
	private String name;
	@Field
	private String link;
	@Field
	private long shareCount;
	@Field
	private long createTime;
	@Field
	private Reaction reaction;
	@DBRef(db="comments")
	private ArrayList<Comment> postComments;

	public Post() {
		postComments = new ArrayList<Comment>();
		reaction = new Reaction();
	}

	public String getPostId() {
		return postId;
	}

	public void setPostId(String postId) {
		this.postId = postId;
	}

	public String getPageId() {
		return pageId;
	}

	public void setPageId(String pageId) {
		this.pageId = pageId;
	}

	public String getPageName() {
		return pageName;
	}

	public void setPageName(String pageName) {
		this.pageName = pageName;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getStory() {
		return story;
	}

	public void setStory(String story) {
		this.story = story;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public long getShareCount() {
		return shareCount;
	}

	public void setShareCount(long shareCount) {
		this.shareCount = shareCount;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public Reaction getReaction() {
		return new Reaction(reaction);
	}

	public void setReaction(Reaction reaction) {
		this.reaction = reaction;
	}

	public ArrayList<Comment> getPostComments() {
		return postComments;
	}

	public void addComments(Comment fc) {
		postComments.add(fc);
	}

	public void addComments(ArrayList<Comment> fc) {
		postComments.addAll(fc);
	}
}
