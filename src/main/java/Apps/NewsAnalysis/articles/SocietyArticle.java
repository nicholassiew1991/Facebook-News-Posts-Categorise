package Apps.NewsAnalysis.articles;

import shared.models.facebook.Reaction;
import shared.models.news.NewsInfo;
import shared.models.news.categories.SocietyNewsCategoryEnum;
import shared.mongodb.SpringMongo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Nicholas on 29/9/2016.
 */
public class SocietyArticle extends AbstractArticle {

	private int disaster;
	private int crime;
	private int accident;
	private int traffic;
	private int warmth;
	private int family;
	private int police;
	private int reactionFeature;

	private NewsInfo n;

	public SocietyArticle(int classification) {
		super(classification);
	}

	public SocietyArticle(NewsInfo n) {
		super(n);
		this.n = n;
	}

	@Override
	public void addWeight(String word) {
		if (wwd.societyDisaster.get(word) != null) {
			this.disaster += wwd.societyDisaster.get(word);
		}
		if (wwd.societyCrime.get(word) != null) {
			this.crime += wwd.societyCrime.get(word);
		}
		if (wwd.societyAccident.get(word) != null) {
			this.accident += wwd.societyAccident.get(word);
		}
		if (wwd.societyTraffic.get(word) != null) {
			this.traffic += wwd.societyTraffic.get(word);
		}
		if (wwd.societyWarmth.get(word) != null) {
			this.warmth += wwd.societyWarmth.get(word);
		}
		if (wwd.societyFamily.get(word) != null) {
			this.family += wwd.societyFamily.get(word);
		}
		if (wwd.societyPolice.get(word) != null) {
			this.police += wwd.societyPolice.get(word);
		}
	}

	public void setReactions(String id) {

		SpringMongo sm = SpringMongo.getInstance();

		Reaction react = sm.getPostReaction(id);
		ArrayList<Reaction.Type> types = new ArrayList<>();
		HashMap<Reaction.Type, Long> hm = new HashMap<>(react.getReactionData());

		hm.remove(Reaction.Type.LIKE);
		hm.remove(Reaction.Type.NONE);
		hm.remove(Reaction.Type.WOW);
		hm.remove(Reaction.Type.THANKFUL);

		long maxValueInMap = (Collections.max(hm.values()));

		for (Map.Entry<Reaction.Type, Long> entry : hm.entrySet()) {  // Iterate through hashmap
			if (entry.getValue() == maxValueInMap) {
				types.add(entry.getKey());    // this is the key which has the max value
			}
		}

		Reaction.Type t = types.get(0);

		if (t.equals(Reaction.Type.ANGRY)) {
			reactionFeature = -1;
		}
		else if (t.equals(Reaction.Type.SAD)) {
			reactionFeature = -2;
		}
		else if (t.equals(Reaction.Type.HAHA)) {
			reactionFeature = 1;
		}
		else if (t.equals(Reaction.Type.LOVE)) {
			reactionFeature = 2;
		}
	}

	@Override
	public String toString() {

		int reactionFeatureIndex = 8;
		int newsLengthIndex = 9;

		setReactions(newsInfo.getPostId());

		/*return String.format(getTrainDataFormat(9),
			classification,
			SocietyNewsCategoryEnum.DISASTER.getValue(), ((double) disaster / 10),
			SocietyNewsCategoryEnum.CRIME.getValue(), ((double) crime / 10),
			SocietyNewsCategoryEnum.ACCIDENT.getValue(), ((double)  accident / 10),
			SocietyNewsCategoryEnum.TRAFFIC.getValue(), ((double)  traffic / 10),
			SocietyNewsCategoryEnum.WARMTH.getValue(), ((double)  warmth / 10),
			SocietyNewsCategoryEnum.FAMILY.getValue(), ((double)  family / 10),
			SocietyNewsCategoryEnum.POLICE.getValue(), ((double)  police / 10),
			reactionFeatureIndex, (double) reactionFeature,
			newsLengthIndex, (double) n.getContent().length()
		);*/
		return String.format(getTrainDataFormat(8),
			classification,
			SocietyNewsCategoryEnum.DISASTER.getValue(), ((double) disaster * 10 / (double) newsInfo.getContent().length() + 1),
			SocietyNewsCategoryEnum.CRIME.getValue(), ((double) crime * 10 / (double) newsInfo.getContent().length() + 1),
			SocietyNewsCategoryEnum.ACCIDENT.getValue(), ((double)  accident * 10 / (double) newsInfo.getContent().length() + 1),
			SocietyNewsCategoryEnum.TRAFFIC.getValue(), ((double)  traffic * 10 / (double) newsInfo.getContent().length() + 1),
			SocietyNewsCategoryEnum.WARMTH.getValue(), ((double)  warmth * 10 / (double) newsInfo.getContent().length() + 1),
			SocietyNewsCategoryEnum.FAMILY.getValue(), ((double)  family * 10 / (double) newsInfo.getContent().length() + 1),
			SocietyNewsCategoryEnum.POLICE.getValue(), ((double)  police * 10 / (double) newsInfo.getContent().length() + 1),
			reactionFeatureIndex, (double) reactionFeature
		);
	}
}
