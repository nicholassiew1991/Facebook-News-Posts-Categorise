package shared.models.facebook;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;

import java.util.ArrayList;

/**
 * Created by Nicholas Siew on 8/4/2016.
 */
@org.springframework.data.mongodb.core.mapping.Document(collection = "comments")
public class Comment {

	public static final String ID = "commentId";
	public static final String MESSAGE = "commentMessage";
	public static final String CREATE_TIME = "create_time";

	@Id
	private String commentId;
	private String commentMessage;
	private long commentCreatedTime;
	private ArrayList<Comment> commentReplies;

	@PersistenceConstructor
	public Comment(String commentId, String commentMessage, long commentCreatedTime) {
		this.commentId = commentId;
		this.commentMessage = commentMessage;
		this.commentCreatedTime = commentCreatedTime;
		commentReplies = new ArrayList<Comment>();
	}

	public String getCommentId() {
		return commentId;
	}

	public void setCommentId(String commentId) {
		this.commentId = commentId;
	}

	public String getCommentMessage() {
		return commentMessage;
	}

	public void setCommentMessage(String commentMessage) {
		this.commentMessage = commentMessage;
	}

	public long getCommentCreatedTime() {
		return commentCreatedTime;
	}

	public void setCommentCreatedTime(long commentCreatedTime) {
		this.commentCreatedTime = commentCreatedTime;
	}

	public ArrayList<Comment> getCommentReplies() {
		return commentReplies;
	}

	public void addCommentReply(Comment fc) {
		commentReplies.add(fc);
	}

	public void addAllCommentReply(ArrayList<Comment> fc) {
		commentReplies.addAll(fc);
	}

	@Override
	public String toString() {
		//return String.format("{ID: %s, Message: %s, commentCreatedTime: %s, Reply: %d}\n", commentId, commentMessage, commentCreatedTime, commentReplies.size());
		StringBuilder sb = new StringBuilder();

		sb.append(String.format("Comment ID: %s\nComment Message: %s\nComment Created Time: %s\n", commentId, commentMessage, commentCreatedTime));

		for (Comment c : commentReplies) {
			sb.append(String.format("  %s\n", c.toString()));
		}

		return sb.toString();
	}
}
