package Apps.WebNewsCrawler.crawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import org.jsoup.select.Elements;
import shared.models.news.NewsInfo;
import shared.models.news.NewsImages;
import shared.models.facebook.Page;
import shared.models.facebook.Post;
import shared.mongodb.SpringMongo;

import java.io.IOException;
import java.util.ArrayList;

public abstract class AbstractNewsCrawler {

	protected static final int CRAWL_SUCCESS = 0;
	protected static final int ERR_DOC_NULL_POINTER = -1;
	protected static final int ERR_INVALID_HTML_DOC = -2;
	protected static final int ERR_NOT_NEWS_CATEGORY = -3;
	protected static final int ERR_EMPTY_LINK = -4;
	protected static final int ERR_INVALID_CONDITION = -5;
	protected static final int ERR_NEWS_EXISTS = -6;

	protected AbstractNewsCrawler() {
	}

	public static AbstractNewsCrawler getInstance(String urlId) {

		if (urlId.equalsIgnoreCase("apple.realtimenews")) {
			return new AppleDailyNewsCrawler();
		}
		else if (urlId.equalsIgnoreCase("setnews")) {
			return new SetNewsCrawler();
		}
		// TODO: How should I design this for multiple pages?
		return null;
	}

	public Document getDocument(String url) {

		String usrAgent = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.103 Safari/537.36";
		Document doc = null;

		try {
			doc = Jsoup.connect(url).userAgent(usrAgent).get();
		}
		catch (IOException e) {
			System.err.println("Failed to get document from: " + url);
			return null;
		}
		return doc;
	}

	public ArrayList<NewsInfo> extractNews(Page p, ArrayList<Post> posts) {

		ArrayList<NewsInfo> news = new ArrayList<>();

		for (int i = 0; i < posts.size(); ) {

			NewsInfo newsInfo = new NewsInfo(posts.get(i).getPostId());

			System.out.print("i: " + i + " ");

			int crawlStatus = extractNews(p, posts.get(i), newsInfo);

			if (crawlStatus == CRAWL_SUCCESS) {
				news.add(newsInfo);
				i++;
			}
			else if (crawlStatus <= ERR_INVALID_HTML_DOC) {
				i++;
			}

			try {
				Thread.sleep(1000);
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return news;
	}

	public int extractNews(Page p, Post post, NewsInfo newsInfo) {

		int status = extractNews(post.getLink(), newsInfo);

		newsInfo.setPage(p);
		newsInfo.setCreateTime(post.getCreateTime());

		return status;
	}

	public int extractNews(String url, NewsInfo outNewsInfo) {

		if (url == null || url.isEmpty() || url.length() < 1) {
			return ERR_EMPTY_LINK;
		}

		SpringMongo sm = SpringMongo.getInstance();

		if (sm.isNewsInfoExists(outNewsInfo)) {
			return ERR_NEWS_EXISTS;
		}

		Document doc = getDocument(url);

		if (doc != null) {

			if (!isBodyEmpty(doc)) {
				return ERR_INVALID_HTML_DOC;
			}
			else if (isValid(doc) == false) {
				return ERR_INVALID_CONDITION;
			}

			System.out.println(url);
			String title = extractTitle(doc);

			String cat = extractNewsCategory(doc);

			if (cat == null) {
				return ERR_NOT_NEWS_CATEGORY;
			}
			String strMainContent = extractNewsContent(doc);
			ArrayList<String> strAuthor = extractReporters(doc);
			ArrayList<NewsImages> images = extractImagesLink(doc);

			outNewsInfo.setTitle(title);
			outNewsInfo.setMainCategory(cat);
			outNewsInfo.setContent(strMainContent);
			outNewsInfo.setReporters(strAuthor);
			outNewsInfo.setNewsUrl(url);
			outNewsInfo.setImages(images);
			return CRAWL_SUCCESS;
		}
		else {
			return ERR_DOC_NULL_POINTER;
		}
	}

	protected boolean isBodyEmpty(Document doc) {
		Elements e = doc.getElementsByTag("body");
		return e.get(0).children().size() > 0;
	}

	protected abstract boolean isValid(Document doc);

	protected abstract String extractTitle(Document doc);

	protected abstract String extractNewsCategory(Document doc);

	protected abstract String extractNewsContent(Document doc);

	protected abstract ArrayList<String> extractReporters(Document doc);

	protected abstract ArrayList<NewsImages> extractImagesLink(Document doc);
}
