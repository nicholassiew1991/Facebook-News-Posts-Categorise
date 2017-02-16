package Apps.NewsAnalysis.articles;

import Apps.NewsAnalysis.WeightsDict.WordWeightsDict;
import shared.models.news.NewsInfo;

/**
 * Created by Nicholas on 7/9/2016.
 */
public abstract class AbstractArticle {

	protected NewsInfo newsInfo;

	protected WordWeightsDict wwd = WordWeightsDict.getInstance();

	protected int classification;

	public AbstractArticle(int classification) {
		this.classification = classification;
	}

	public AbstractArticle(NewsInfo n) {
		this(n.getNumericSubCategory());
		this.newsInfo = n;
	}

	public String aaa(int... bb) {
		return null;
	}

	public final String getTrainDataFormat(int n) {

		StringBuilder sb = new StringBuilder();
		sb.append("%d");

		for (int i = 0; i < n; i++) {
			sb.append(" %d:%.4f");
		}

		return sb.toString();
	}

	public abstract void addWeight(String word);

	public abstract String toString();
}
