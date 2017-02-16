package Apps.WebNewsCrawler.crawler;

import java.util.ArrayList;

import org.jsoup.nodes.Document;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import shared.models.news.NewsImages;
import shared.models.news.categories.MainNewsCategoryEnum;

public class SetNewsCrawler extends AbstractNewsCrawler {

	@Override
	protected boolean isValid(Document doc) {
		return true;
	}

	@Override
	protected String extractTitle(Document doc) {
		Elements eles = doc.getElementsByClass("title");

		if (eles.isEmpty()) {
			return "";
		}
		return eles.get(0).text();
	}

	@Override
	protected String extractNewsCategory(Document doc) {

		final int FINANCE = 2;
		final int INTERNATIONAL = 5;
		final int POLITIC = 6;
		final int SPORT = 34;
		final int SOCIETY = 41;

		final int[] ENTERTAINMENTS = new int[] {
			8, 17, 45, 46
		};

		Element topTitle = doc.getElementById("toptitle");

		if (topTitle == null) {
			return null;
		}

		Elements classGt = topTitle.getElementsByClass("gt");

		if (classGt.size() < 2) {
			return null;
		}
		String categoryUrl = classGt.get(1).attr("href");

		try {

			int categoryId = Integer.parseInt(categoryUrl.split("=")[1]);

			if (searchInArray(ENTERTAINMENTS, categoryId) == true) {
				return MainNewsCategoryEnum.ENTERTAINMENT.name();
			}
			else if (categoryId == INTERNATIONAL) {
				return MainNewsCategoryEnum.INTERNATIONAL.name();
			}
			else if (categoryId == POLITIC) {
				return MainNewsCategoryEnum.POLITIC.name();
			}
			else if (categoryId == SOCIETY) {
				return MainNewsCategoryEnum.SOCIETY.name();
			}
			else if (categoryId == SPORT) {
				return MainNewsCategoryEnum.SPORT.name();
			}
			else if (categoryId == FINANCE) {
				return MainNewsCategoryEnum.FINANCE.name();
			}
		} catch (IndexOutOfBoundsException e) {
			return null;
		}
		return MainNewsCategoryEnum.UNCATEGORISED.name();
	}

	@Override
	protected String extractNewsContent(Document doc) {

		Element content1 = doc.getElementById("Content1");
		Elements p = content1.select("p:not([^])"); // Select the p tag with no attributes

		StringBuilder sb = new StringBuilder();

		for (Element e : p) {
			sb.append(e.text());
		}
		return sb.toString();
	}

	@Override
	protected ArrayList<String> extractReporters(Document doc) {
		return null;
	}

	@Override
	protected ArrayList<NewsImages> extractImagesLink(Document doc) {

		ArrayList<NewsImages> arr = new ArrayList<>();

		Element content1 = doc.getElementById("Content1");
		Elements imgs = content1.getElementsByTag("img");

		for (int i = 0; i < imgs.size(); i++) {
			String url = imgs.get(i).attr("src");
			String desc = imgs.get(i).attr("alt");
			NewsImages n = new NewsImages(url, desc);
			arr.add(n);
		}
		return arr;
	}

	private boolean searchInArray(int[] arr, int data) {
		for (int n : arr) {
			if (data == n) {
				return true;
			}
		}
		return false;
	}
}
