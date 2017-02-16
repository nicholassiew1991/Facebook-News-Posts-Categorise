package Apps.NewsAnalysis.articles;

import shared.models.news.NewsInfo;
import shared.models.news.categories.SportNewsCategoryEnum;

public class SportArticle extends AbstractArticle {

	private int baseball;
	private int basketball;
	private int others;

	public SportArticle(int classification) {
		super(classification);
	}

	public SportArticle(NewsInfo n) {
		super(n);
	}

	@Override
	public void addWeight(String word) {
		if (wwd.sportBaseBall.get(word) != null) {
			this.baseball += wwd.sportBaseBall.get(word);
		}
		if (wwd.sportBasketBall.get(word) != null) {
			this.basketball += wwd.sportBasketBall.get(word);
		}
		if (wwd.sportOthers.get(word) != null) {
			this.others += wwd.sportOthers.get(word);
		}
	}

	@Override
	public String toString() {
		return String.format("%d %d:%.1f %d:%.1f %d:%.1f",
			classification,
			SportNewsCategoryEnum.BASEBALL.getValue(), ((double) baseball / 10),
			SportNewsCategoryEnum.BASKETBALL.getValue(), ((double) basketball / 10),
			SportNewsCategoryEnum.OTHERS.getValue(), ((double)  others / 10)
		);
	}
}
