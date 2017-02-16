package Apps.NewsAnalysis.articles;

import shared.models.news.NewsInfo;
import shared.models.news.categories.EntertainmentNewsCategoryEnum;

/**
 * Created by Nicholas on 9/9/2016.
 */
public class EntertainmentArticle extends AbstractArticle {

	private int china;
	private int korea;
	private int occident;

	public EntertainmentArticle(int classification) {
		super(classification);
	}

	public EntertainmentArticle(NewsInfo n) {
		super(n);
	}

	@Override
	public void addWeight(String word) {
		if (wwd.entertainmentChina.get(word) != null) {
			this.china += wwd.entertainmentChina.get(word);
		}
		if (wwd.entertainmentKorea.get(word) != null) {
			this.korea += wwd.entertainmentKorea.get(word);
		}
		if (wwd.entertainmentOccident.get(word) != null) {
			this.occident += wwd.entertainmentOccident.get(word);
		}
	}

	@Override
	public String toString() {
		return String.format("%d %d:%.1f %d:%.1f %d:%.1f",
			classification,
			EntertainmentNewsCategoryEnum.CHINA.getValue(), ((double) china * 10 / (double) newsInfo.getContent().length()),
			EntertainmentNewsCategoryEnum.KOREA.getValue(), ((double) korea * 10 / (double) newsInfo.getContent().length()),
			EntertainmentNewsCategoryEnum.OCCIDENT.getValue(), ((double) occident * 10 / (double) newsInfo.getContent().length())
		);
	}
}
