package Apps.NewsAnalysis.articles;

import shared.models.news.NewsInfo;
import shared.models.news.categories.InternationalNewsCategoryEnum;

/**
 * Created by Nicholas on 29/9/2016.
 */
public class InternationalArticle extends AbstractArticle {

	private int asia;
	private int oceania;
	private int america;
	private int europe;

	public InternationalArticle(int classification) {
		super(classification);
	}

	public InternationalArticle(NewsInfo n) {
		super(n);
	}

	@Override
	public void addWeight(String word) {
		if (wwd.internationalAsia.get(word) != null) {
			this.asia += wwd.internationalAsia.get(word);
		}
		if (wwd.internationalOceania.get(word) != null) {
			this.oceania += wwd.internationalOceania.get(word);
		}
		if (wwd.internationalAmerica.get(word) != null) {
			this.america += wwd.internationalAmerica.get(word);
		}
		if (wwd.internationalEurope.get(word) != null) {
			this.europe += wwd.internationalEurope.get(word);
		}
	}

	@Override
	public String toString() {
		return String.format("%d %d:%.1f %d:%.1f %d:%.1f %d:%.1f",
			classification,
			InternationalNewsCategoryEnum.ASIA.getValue(), ((double) asia / 10),
			InternationalNewsCategoryEnum.OCEANIA.getValue(), ((double) oceania / 10),
			InternationalNewsCategoryEnum.AMERICA.getValue(), ((double)  america / 10),
			InternationalNewsCategoryEnum.EUROPE.getValue(), ((double)  europe / 10)
		);
	}
}
