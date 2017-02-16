package Apps.NewsAnalysis.articles;

import java.util.HashMap;

/**
 * Created by Nicholas on 31/10/2016.
 */
public class ArticleFeatures {

	private HashMap<Integer, HashMap<String, Object>> features = new HashMap<>();

	private String FEATURE_NAME = "name";
	private String FEATURE_VALUE = "value";

	public void addFeature(int index, String name) {
		HashMap<String, Object> hm = new HashMap<>();
		hm.put(FEATURE_NAME, name);
		hm.put(FEATURE_VALUE, Double.valueOf(0));
		features.put(index, hm);
	}

	public void addValue(int index, int value) {

	}
}
