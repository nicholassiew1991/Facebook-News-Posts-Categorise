package Apps.NewsAnalysis.articles;

import shared.models.news.NewsInfo;
import shared.models.news.categories.FinanceNewsCategoryEnum;

/**
 * Created by Nicholas on 29/9/2016.
 */
public class FinanceArticle extends AbstractArticle {

	private int stock;
	private int estate;
	private int industry;
	private int employment;
	private int others;

	public FinanceArticle(int classification) {
		super(classification);
	}

	public FinanceArticle(NewsInfo n) {
		super(n);
	}

	@Override
	public void addWeight(String word) {
		if (wwd.financeStock.get(word) != null) {
			this.stock += wwd.financeStock.get(word);
		}
		if (wwd.financeEstate.get(word) != null) {
			this.estate += wwd.financeEstate.get(word);
		}
		if (wwd.financeIndustry.get(word) != null) {
			this.industry += wwd.financeIndustry.get(word);
		}
		if (wwd.financeEmployment.get(word) != null) {
			this.employment += wwd.financeEmployment.get(word);
		}
		if (wwd.financeOthers.get(word) != null) {
			this.others += wwd.financeOthers.get(word);
		}
	}

	@Override
	public String toString() {
		return String.format(getTrainDataFormat(5),
			classification,
			FinanceNewsCategoryEnum.STOCK.getValue(), ((double) stock / 10),
			FinanceNewsCategoryEnum.ESTATE.getValue(), ((double) estate / 10),
			FinanceNewsCategoryEnum.INDUSTRY.getValue(), ((double)  industry / 10),
			FinanceNewsCategoryEnum.EMPLOYMENT.getValue(), ((double)  employment / 10),
			FinanceNewsCategoryEnum.OTHERS.getValue(), ((double)  others / 10)
		);
	}
}
