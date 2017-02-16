import Apps.FeedsCrawler.crawler.FacebookFeedsCrawler;
import Apps.FeedsCrawler.facebook.GraphAPI;
import javafx.geometry.Pos;
import org.junit.Test;
import shared.models.facebook.Comment;
import shared.models.facebook.Page;
import shared.models.facebook.Post;
import shared.models.facebook.Reaction;
import shared.models.news.NewsInfo;
import shared.mongodb.SpringMongo;
import shared.utils.JsonUtils;

import javax.json.JsonNumber;
import javax.json.JsonObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Nicholas on 9/10/2016.
 */
public class SavePost {

	@Test
	public void save() {
		SpringMongo sm = SpringMongo.getInstance();

		String[] ids = new String[] {"110699089014688"};

		for (String id : ids) {
			List<NewsInfo> news = sm.getNewsByAuthor(id);
			Page p = news.get(0).getPage();
			getPost(p, news);
			sm.saveToDb(p);
		}
	}

	public void getPost(Page p, List<NewsInfo> news) {
		ArrayList<Post> ps = new ArrayList<>();
		for (NewsInfo n : news) {
			p.addPost(getPost(n));
		}
	}

	public Post getPost(NewsInfo news) {

		String urlFormat = "%s/%s?fields=id,message,story,name,link,created_time,from,shares&date_format=U&access_token=%s";
		//String urlFormat = "%s/%s?fields=from&date_format=U&access_token=%s";
		String url = String.format(urlFormat, GraphAPI.FACEBOOK_API_URL, news.getPostId(), GraphAPI.ACCESS_TOKEN);

		FacebookFeedsCrawler ffc = new FacebookFeedsCrawler();
		JsonObject jobj = JsonUtils.httpGetJsonObject(url);

		String post_id = ffc.decodePostId(jobj);
		String message = ffc.decodePostMessage(jobj);
		String story = ffc.decodePostStory(jobj);
		String name = ffc.decodePostName(jobj);
		long createTime = ffc.decodePostCreateTime(jobj);
		long shareCount = ffc.decodePostShareCount(jobj);
		Reaction reaction = ffc.decodePostReactions(post_id);
		ArrayList<Comment> comments = ffc.decodeComment(post_id);

		/*System.out.println(jobj);

		System.out.println(post_id);
		System.out.println("Message: " + message);
		System.out.println("Story: " + story);
		System.out.println("Name: " + name);
		System.out.println(news.getNewsUrl());
		System.out.println(createTime);
		System.out.println(shareCount);
		System.out.println(reaction);
		System.out.println(comments.size());*/

		Post fbPost = new Post();
		fbPost.setPostId(post_id);
		fbPost.setPageId(news.getPage().getPageId());
		fbPost.setPageName(news.getPage().getPageName());
		fbPost.setMessage(message);
		fbPost.setStory(story);
		fbPost.setName(name);
		fbPost.setLink(news.getNewsUrl());
		fbPost.setCreateTime(createTime);
		fbPost.setShareCount(shareCount);
		fbPost.setReaction(reaction);
		fbPost.addComments(comments);

		return fbPost;
	}
}
