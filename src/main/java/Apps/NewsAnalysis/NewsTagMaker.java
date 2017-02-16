package Apps.NewsAnalysis;

import com.huaban.analysis.jieba.JiebaSegmenter;
import com.huaban.analysis.jieba.SegToken;
import com.mongodb.Tag;
import org.apache.commons.lang3.StringUtils;
import shared.models.news.NewsInfo;
import shared.models.tags.TagPoliticsEvents;
import shared.models.tags.TagPoliticsPeoples;
import shared.models.tags.TagPoliticsTerms;
import shared.mongodb.MongoNewsTagDict;
import shared.mongodb.SpringMongo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nicholas on 20/10/2016.
 */
public class NewsTagMaker {

	JiebaSegmenter js = new JiebaSegmenter();
	MongoNewsTagDict mntd = MongoNewsTagDict.getInstance();
	SpringMongo sm = SpringMongo.getInstance();

	public void addNewsTag(List<NewsInfo> news) {
		for (NewsInfo n : news) {
			addNewsTag(n);
		}
	}

	private void addNewsTag(NewsInfo news) {
		processTitle(news);
		processContent(news);
		sm.saveNewsInfo(news);
	}

	private void processTitle(NewsInfo n) {

		List<String> titles = js.sentenceProcess(n.getTitle());

		for (String t : titles) {

			TagPoliticsPeoples tpp = mntd.findPeoples(t);
			ArrayList<TagPoliticsEvents> tpes = mntd.getEventsTags(t);
			ArrayList<TagPoliticsTerms> tpts = mntd.getTermsTags(t);

			if (tpp != null) {
				n.addTag(tpp.getKey());
			}

			for (TagPoliticsEvents tpe : tpes) {
				n.addTag(tpe.getKey());
			}

			for (TagPoliticsTerms tpt : tpts) {
				n.addTag(tpt.getKey());
			}
		}
	}

	private void processContent(NewsInfo n) {

		String halfContent = n.getContent().substring(0, n.getContent().length() / 2);

		List<String> i = js.sentenceProcess(halfContent);
		List<SegToken> content = js.process(halfContent, JiebaSegmenter.SegMode.INDEX);
		List<SegToken> t = js.process(halfContent, JiebaSegmenter.SegMode.SEARCH);

		for (SegToken st : content) {

			String s = st.word;

			ArrayList<TagPoliticsEvents> tpes = mntd.getEventsTags(s);
			ArrayList<TagPoliticsTerms> tpts = mntd.getTermsTags(s);

			for (TagPoliticsEvents tpe : tpes) {
				n.addTag(tpe.getKey());
			}

			for (TagPoliticsTerms tpt : tpts) {
				n.addTag(tpt.getKey());
			}
		}

	}
}
