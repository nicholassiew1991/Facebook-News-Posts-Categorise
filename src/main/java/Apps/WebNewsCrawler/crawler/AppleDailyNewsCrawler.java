package Apps.WebNewsCrawler.crawler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import shared.models.news.NewsImages;
import shared.models.news.categories.MainNewsCategoryEnum;
import shared.utils.Utils;

public class AppleDailyNewsCrawler extends AbstractNewsCrawler {

	@Override
	protected boolean isValid(Document doc) {

		String domain = doc.baseUri().split("/")[2];

		if (domain.equalsIgnoreCase("www.applelive.com.tw")) {
			return false;
		}
		return true;
	}

	@Override
	protected String extractTitle(Document doc) {
		Element mainContent = doc.getElementById("maincontent");

		if (mainContent == null) {
			return "";
		}
		Elements hgroup = mainContent.getElementsByTag("hgroup");
		return Utils.regularize(hgroup.text());
	}

	@Override
	protected String extractNewsCategory(Document doc) {

		String url = doc.baseUri();
		String splitedUrl = null;

		try {
			splitedUrl = url.split("/")[5];
		}
		catch (IndexOutOfBoundsException e) {
			return MainNewsCategoryEnum.UNCATEGORISED.name();
		}

		if (splitedUrl.equalsIgnoreCase("international")) {
			return MainNewsCategoryEnum.INTERNATIONAL.name();
		}
		else if (splitedUrl.equalsIgnoreCase("local")) {
			return MainNewsCategoryEnum.SOCIETY.name();
		}
		else if (splitedUrl.equalsIgnoreCase("politics")) {
			return MainNewsCategoryEnum.POLITIC.name();
		}
		else if (splitedUrl.equalsIgnoreCase("sports")) {
			return MainNewsCategoryEnum.SPORT.name();
		}
		else if (splitedUrl.equalsIgnoreCase("finance")) {
			return MainNewsCategoryEnum.FINANCE.name();
		}
		else if (splitedUrl.equalsIgnoreCase("entertainment")) {
			return MainNewsCategoryEnum.ENTERTAINMENT.name();
		}
		return MainNewsCategoryEnum.UNCATEGORISED.name();
	}

	@Override
	protected String extractNewsContent(Document doc) {
		
		String strArticle = null;
		
		Element summary = doc.getElementById("summary");
		
		if (summary != null) {
			strArticle = Utils.regularize(summary.text());
		}
		else {
			
			Element ele = doc.select(".articulum.trans").first();

			if (ele == null) {
				return "";
			}
			
			Elements figures = ele.getElementsByTag("figure");
			
			if (figures.size() > 0) {
				figures.remove();
			}
			
			Elements others = ele.getElementsByAttribute("_moz_dirty");
			
			if (others.size() > 0) {
				others.remove();
			}
			
			strArticle = ele.text();
		}
		return Utils.regularize(strArticle);
	}

	@Override
	protected ArrayList<String> extractReporters(Document doc) {
		
		ArrayList<String> listReporters = new ArrayList<>();
		String data = null;
		
		String content = extractNewsContent(doc);

		if (content.isEmpty()) {
			return listReporters;
		}

		Pattern pattern = Pattern.compile("\\(([^)]+報導)\\)");
		Matcher matcher = pattern.matcher(content);
		
		if (matcher.find()) {
	    data = matcher.group(1);
		}
		
		if (data != null) {
			String strReporters = data.split("/")[0];
			String[] arrReporters = strReporters.split("、");
			listReporters.addAll(Arrays.asList(arrReporters));
		}
		return listReporters;
	}

	@Override
	protected ArrayList<NewsImages> extractImagesLink(Document doc) {

		ArrayList<NewsImages> images = new ArrayList<>();

		Element mainContent = doc.getElementById("maincontent");

		if (mainContent == null) {
			return images;
		}

		Elements figures = mainContent.getElementsByTag("figure");

		for (Element e : figures) {
			Element img = e.getElementsByTag("img").get(0);
			String url = img.absUrl("src");
			images.add(new NewsImages(url, ""));
		}
		return images;
	}
}
