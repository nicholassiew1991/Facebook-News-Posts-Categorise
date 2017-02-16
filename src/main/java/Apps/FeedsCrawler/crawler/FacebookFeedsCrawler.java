package Apps.FeedsCrawler.crawler;

import Apps.FeedsCrawler.facebook.GraphAPI;
import org.apache.commons.lang3.time.DateUtils;
import shared.models.facebook.Comment;
import shared.models.facebook.Page;
import shared.models.facebook.Post;
import shared.models.facebook.Reaction;
import shared.utils.JsonUtils;
import javax.json.*;
import javax.swing.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Nicholas on 24/4/2016.
 */
public class FacebookFeedsCrawler {

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	public static final int CRAWL_SUCCESS = 0;
	public static final int CRAWL_EMPTY_DATE = 1;
	public static final int CRAWL_UNTIL_IS_EARLIER = 2;

	public int crawlFeeds(ArrayList<Page> pages, String since, String until) {

		if (since == null || until == null) {
			JOptionPane.showMessageDialog(null, "Date can't be null.");
			return CRAWL_EMPTY_DATE;
		}

		Date dateSince = null;
		Date dateUntil = null;

		try {
			dateSince = sdf.parse(since);
			dateUntil = sdf.parse(until);
		}
		catch (ParseException e) {
			e.printStackTrace();
		}

		if (dateUntil.before(dateSince)) {
			JOptionPane.showMessageDialog(null, "Until date should not earlier than since date.");
			return CRAWL_UNTIL_IS_EARLIER;
		}

		dateUntil = DateUtils.addDays(dateUntil, 1);

		long unixSince = (dateSince.getTime() / 1000) - 1;
		long unixUntil = (dateUntil.getTime() / 1000); // Include the until day

		System.out.println(dateSince);
		System.out.println(dateUntil);

		System.out.println(unixSince);
		System.out.println(unixUntil);

		for (Page p : pages) {
			crawlFeeds(p, unixSince, unixUntil);
		}
		return CRAWL_SUCCESS;
	}

	private void crawlFeeds(Page fp, long since, long until) {

		String url = getFeedUrl(fp.getPageId(), since, until);

		System.out.println(url);

		do {
			JsonObject obj = JsonUtils.httpGetJsonObject(url);
			JsonArray data = obj.getJsonArray("data");

			if (!data.isEmpty()) {
				// Loop through every post
				for (JsonValue val : data) {
					JsonObject o = (JsonObject) val;
					Post fbPost = getFacebookPost(o, fp);
					if (fbPost != null) {
						fp.addPost(fbPost);
					}
				}
			}
			url = haveNextPage(obj);

			if (url == null) {
				break;
			}

		} while (true);
	}

	private Post getFacebookPost(JsonObject jobj, Page p) {

		JsonObject from = jobj.getJsonObject("from");
		String link = decodePostLink(jobj);

		if (isPostByPageOwner(from, p) == false || link == null || isVideoPost(link) == true || isPhotoPost(link) == true) {
			return null;
		}

		String post_id = decodePostId(jobj);
		String message = decodePostMessage(jobj);
		String story = decodePostStory(jobj);
		String name = decodePostName(jobj);
		long createTime = decodePostCreateTime(jobj);
		long shareCount = decodePostShareCount(jobj);
		Reaction reaction = decodePostReactions(post_id);
		ArrayList<Comment> comments = decodeComment(post_id);

		Post fbPost = new Post();
		fbPost.setPostId(post_id);
		fbPost.setPageId(p.getPageId());
		fbPost.setPageName(p.getPageName());
		fbPost.setMessage(message);
		fbPost.setStory(story);
		fbPost.setName(name);
		fbPost.setLink(link);
		fbPost.setCreateTime(createTime);
		fbPost.setShareCount(shareCount);
		fbPost.setReaction(reaction);
		fbPost.addComments(comments);
		return fbPost;
	}

	public String decodePostId(JsonObject obj) {
		return obj.getString("id");
	}

	public String decodePostMessage(JsonObject obj) {
		JsonString jsonMessage = obj.getJsonString("message");
		return (jsonMessage == null ? "" : jsonMessage.getString());
	}

	public String decodePostStory(JsonObject obj) {
		JsonString jsonStory = obj.getJsonString("story");
		return (jsonStory == null ? "" : jsonStory.getString());
	}

	public String decodePostName(JsonObject obj) {
		JsonString jsonName = obj.getJsonString("name");
		return (jsonName == null ? "" : jsonName.getString());
	}

	public String decodePostLink(JsonObject obj) {
		JsonString jsonLink = obj.getJsonString("link");
		return (jsonLink == null ? "" : jsonLink.getString());
	}

	public long decodePostCreateTime(JsonObject obj) {
		return obj.getJsonNumber("created_time").longValue();
	}

	public long decodePostShareCount(JsonObject obj) {
		JsonObject jsonShare = obj.getJsonObject("shares");
		return (jsonShare == null ? 0 : jsonShare.getJsonNumber("count").longValue());
	}

	public Reaction decodePostReactions(String postId) {

		final String URL_FORMAT = "%s/%s/?fields=reactions.type(NONE).limit(0).summary(total_count).as(NONE),reactions.type(LIKE).limit(0).summary(total_count).as(LIKE),reactions.type(LOVE).limit(0).summary(total_count).as(LOVE),reactions.type(WOW).limit(0).summary(total_count).as(WOW),reactions.type(HAHA).limit(0).summary(total_count).as(HAHA),reactions.type(SAD).limit(0).summary(total_count).as(SAD),reactions.type(ANGRY).limit(0).summary(total_count).as(ANGRY),reactions.type(THANKFUL).limit(0).summary(total_count).as(THANKFUL)&access_token=%s";

		Reaction reaction = new Reaction();
		HashMap<Reaction.Type, Long> hmReactData = reaction.getReactionData();

		String url = String.format(URL_FORMAT, GraphAPI.FACEBOOK_API_URL, postId, GraphAPI.ACCESS_TOKEN);

		JsonObject reactsObj = JsonUtils.httpGetJsonObject(url);

		for (Reaction.Type t : Reaction.Type.values()) {
			JsonObject reactData = reactsObj.getJsonObject(t.toString());
			JsonObject reactDataSummary = reactData.getJsonObject("summary");
			JsonNumber reactCounts = reactDataSummary.getJsonNumber("total_count");
			hmReactData.put(t, reactCounts.longValue());
		}
		reaction.setReactionData(hmReactData);
		return reaction;
	}

	public ArrayList<Comment> decodeComment(String postId) {
		final String URL_FORMAT = "%s/%s/?fields=comments.limit(100){message,created_time,id,comments.limit(100){message,created_time,id}}&date_format=U&access_token=%s";
		String url = String.format(URL_FORMAT, GraphAPI.FACEBOOK_API_URL, postId, GraphAPI.ACCESS_TOKEN);
		return decodeComment(JsonUtils.httpGetJsonObject(url));
	}

	private ArrayList<Comment> decodeComment(JsonObject obj) {

		ArrayList<Comment> comments = new ArrayList<Comment>();

		JsonObject commentsObject = obj.getJsonObject("comments");

		if (commentsObject == null) {
			return comments; // This should be return empty.
		}

		String url = "";

		do {
			if (!url.isEmpty()) {
				commentsObject = JsonUtils.httpGetJsonObject(url);
			}
			JsonArray commentData = commentsObject.getJsonArray("data");

			if (!commentData.isEmpty()) {
				for (JsonValue v : commentData) {
					JsonObject tupleComment = (JsonObject) v;
					Comment c = getComment(tupleComment);
					ArrayList<Comment> commentReplies = decodeComment(tupleComment); // Get the comment replies
					c.addAllCommentReply(commentReplies); // Add the comment replies to the comment
					comments.add(c); // Add the comment to the post's comments
				}
			}

			url = haveNextPage(commentsObject);

			if (url == null) {
				break;
			}

		} while (true);

		return comments;
	}

	private boolean isPhotoPost(String link) {
		String fbPhotosRegex = "^(https?://www\\.facebook\\.com/(?:photo\\.php\\?fbid=\\S+|.*?/photos/\\S+/?))$";
		return link.matches(fbPhotosRegex);
	}

	private boolean isVideoPost(String link) {
		String fbVideoRegex = 	"^(https?://www\\.facebook\\.com/(?:video\\.php\\?v=\\d+|.*?/videos/\\d+/?))$";
		String ytVideoRegex = "^https?://.*(?:youtube|youtu.be/|v/|u/\\w/|embed/|watch?v=)([^#&?]*).*$";
		String combine = fbVideoRegex + "|" + ytVideoRegex;
		return link.matches(combine);
	}

	private Comment getComment(JsonObject obj) {
		String commentMessage = obj.getString("message");
		String commentId = obj.getString("id");
		long createdTime = obj.getJsonNumber("created_time").longValue();
		return new Comment(commentId, commentMessage, createdTime);
	}

	private String haveNextPage(JsonObject obj) {
		JsonObject paging = obj.getJsonObject("paging");

		if (paging != null) {

			JsonString nextPage = paging.getJsonString("next");

			if (nextPage != null) {
				return nextPage.getString();
			}
		}
		return null;
	}

	private boolean isPostByPageOwner(JsonObject obj, Page p) {
		String pageId = obj.getString("id");
		return pageId.equals(p.getPageId());
	}

	private String getFeedUrl(String pageId, long since, long until) {
		int limit = 100;
		final String URL_FORMAT = "%s/%s/feed/?fields=%s&since=%s&until=%s&date_format=U&limit=%s&access_token=%s";
		return String.format(URL_FORMAT, GraphAPI.FACEBOOK_API_URL, pageId, getUrlFields(), since, until, limit, GraphAPI.ACCESS_TOKEN);
	}

	private String getUrlFields() {

		String[] fields = new String[] {
			"message", "story", "name", "link", "created_time", "id", "from", "shares"
		};

		String val = "";

		for (int a = 0; a < fields.length; a++) {

			val += fields[a];

			if (a != fields.length - 1) {
				val += ",";
			}
		}
		return val;
	}
}
