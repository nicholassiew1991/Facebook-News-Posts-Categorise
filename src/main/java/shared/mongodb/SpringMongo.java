package shared.mongodb;

import com.sun.org.apache.regexp.internal.RE;
import javafx.geometry.Pos;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import shared.models.facebook.Reaction;
import shared.models.news.NewsInfo;
import shared.models.facebook.Comment;
import shared.models.facebook.Page;
import shared.models.facebook.Post;
import shared.models.news.categories.MainNewsCategoryEnum;
import shared.mongodb.repo.FacebookPostsRepo.CommentsRepo;
import shared.mongodb.repo.FacebookPostsRepo.PagesRepo;
import shared.mongodb.repo.FacebookPostsRepo.PostsRepo;
import shared.mongodb.repo.NewsRepo.NewsImageRepo;
import shared.mongodb.repo.NewsRepo.NewsInfoRepo;

import java.util.ArrayList;
import java.util.List;

public class SpringMongo {

	private static SpringMongo instance = null;

	ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new ClassPathResource("spring-config.xml").getPath());
	PagesRepo pagesRepo = context.getBean(PagesRepo.class);
	PostsRepo postsRepo = context.getBean(PostsRepo.class);
	CommentsRepo commentsRepo = context.getBean(CommentsRepo.class);
	NewsInfoRepo newsInfoRepo = context.getBean(NewsInfoRepo.class);
	NewsImageRepo newsImageRepo = context.getBean(NewsImageRepo.class);

	private MongoOperations postsOperation = (MongoOperations) context.getBean("facebookPostsTemplate");
	private MongoOperations newsOperation = (MongoOperations) context.getBean("facebookNewsTemplate");

	private SpringMongo() {}

	public static SpringMongo getInstance() {
		if (instance == null) {
			instance = new SpringMongo();
		}
		return instance;
	}

	public void saveNewsToDb(ArrayList<NewsInfo> news) {
		for (NewsInfo n : news) {
			saveNewsToDb(n);
		}
	}

	public void saveNewsToDb(NewsInfo news) {

		NewsInfo dbNews = getNewsInfoFromDb(news.getPostId());

		if (dbNews == null) {
			newsInfoRepo.save(news);
		}
		else {
			// TODO: Update News
		}
	}

	public NewsInfo getNewsInfoFromDb(String id) {
		NewsInfo news = newsInfoRepo.searchById(id);
		return news;
	}

	public Page getPageData(String id) {
		return pagesRepo.searchById(id);
	}

	public Post getPostById(String id) {
		return postsRepo.searchPostById(id);
	}

	/**
	 * Save ArrayList of the Page objects to the database.
	 * @param pages ArrayList of Page object
	 */
	public void saveToDb(ArrayList<Page> pages) {
		for (Page p : pages) {
			saveToDb(p);
		}
	}

	/**
	 * Save the Page object to the database
	 * @param page Page object
	 */
	public void saveToDb(Page page) {
		Page dbPage = pagesRepo.searchById(page.getPageId());
		ArrayList<Post> dbPosts;

		int newAdded = 0;

		if (dbPage == null) {
			// If the page document can't found in the database
			dbPage = page;
			dbPosts = page.getFbPost();
		}
		else {
			/* If the page document found in database. */
			dbPosts = dbPage.getFbPost();
			ArrayList<Post> newPosts = page.getFbPost();

			/* Add the new post and replace the old post. */
			for (int i = 0; i < newPosts.size(); i++) {

				boolean isExists = false;

				for (int j = 0; j < dbPosts.size(); j++) {
					if (newPosts.get(i).getPostId().equals(dbPosts.get(j).getPostId())) {
						isExists = true;
						dbPosts.set(j, newPosts.get(i));
					}
				}

				if (isExists == false) {
					newAdded++;
					dbPosts.add(newPosts.get(i));
				}
			}
		}

		// Start save
		// TODO: Planning to save to file
		System.out.println("Database: " + dbPage.getFbPost().size());
		System.out.println("New Added: " + newAdded);
		System.out.println("New: " + page.getFbPost().size());
		System.out.println("Total: " + dbPosts.size());
		dbPage.setFbPost(dbPosts);
		for (Post p : dbPosts) {
			ArrayList<Comment> comments = p.getPostComments();
			commentsRepo.save(comments);
		}
		postsRepo.save(dbPosts);
		pagesRepo.save(dbPage);
	}

	public void savePost(ArrayList<Post> posts) {
		postsRepo.save(posts);
	}

	public boolean isNewsInfoExists(NewsInfo n) {
		if (getNewsInfoFromDb(n.getPostId()) != null) {
			return true;
		}
		return false;
	}

	public List<NewsInfo> getNewsByAuthor(String id) {
		Query q = new Query();
		q.addCriteria(Criteria.where("page._id").is(id));
		return newsOperation.find(q, NewsInfo.class);
	}

	public List<NewsInfo> getAllNews(MainNewsCategoryEnum category) {
		Query q = new Query();
		q.addCriteria(Criteria.where("category.newsCat").is(category.name()));
		return newsOperation.find(q, NewsInfo.class);
	}

	public List<NewsInfo> getNews(MainNewsCategoryEnum category) {
		Query q = new Query();
		q.addCriteria(Criteria.where("category.newsCat").is(category.name()).and("category.subNewsCat").ne("UNCATEGORISED"));
		return newsOperation.find(q, NewsInfo.class);
	}

	public List<NewsInfo> getUncategoriseNews(MainNewsCategoryEnum category) {
		return getUncategoriseNews(category, 0);
	}

	public List<NewsInfo> getUncategoriseNews(MainNewsCategoryEnum category, int limit) {

		Query q = new Query();

		q.addCriteria(Criteria.where("category.newsCat").is(category.name()).and("category.subNewsCat").is("UNCATEGORISED"));

		if (limit > 0) {
			q.limit(limit);
		}
		return newsOperation.find(q, NewsInfo.class);
	}

	public List<NewsInfo> getSVMPredictedNews(MainNewsCategoryEnum category) {
		return getSVMPredictedNews(category, 0);
	}

	public List<NewsInfo> getSVMPredictedNews(MainNewsCategoryEnum category, int limit) {
		Query q = new Query();

		q.addCriteria(Criteria.where("category.newsCat").is(category.name()).and("svmPredicted").is(true));

		if (limit > 0) { q.limit(limit); }

		return newsOperation.find(q, NewsInfo.class);
	}

	public List<NewsInfo> getSVMNews(MainNewsCategoryEnum category) {
		Query q = new Query();
		q.addCriteria(Criteria.where("svmCategory.newsCat").is(category.name()));
		return newsOperation.find(q, NewsInfo.class);
	}

	public void saveNewsInfo(List<NewsInfo> n) {
		for (NewsInfo news : n) {
			saveNewsInfo(news);
		}
	}

	public void saveNewsInfo(NewsInfo n) {
		newsOperation.save(n);
	}

	public Reaction getPostReaction(String id) {

		Query q = new Query();
		q.addCriteria(Criteria.where("_id").is(id));

		Post post = postsOperation.findOne(q, Post.class);

		if (post != null) {
			return post.getReaction();
		}
		return null;
	}
}
