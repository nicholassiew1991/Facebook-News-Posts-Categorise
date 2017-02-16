package Apps.NewsAnalysis;

import Apps.NewsAnalysis.WeightsDict.WordWeightsDict;
import Apps.NewsAnalysis.articles.AbstractArticle;
import Apps.NewsAnalysis.articles.EntertainmentArticle;
import Apps.NewsAnalysis.controller.ControllerSNewsEngine;
import Apps.NewsAnalysis.svm.AbstractNewsTrain;
import Apps.NewsAnalysis.view.ViewSNewsEngine;
import com.huaban.analysis.jieba.WordDictionary;
import shared.Resources;
import shared.models.news.NewsInfo;
import shared.models.news.categories.MainNewsCategoryEnum;
import shared.mongodb.MongoNewsTagDict;
import shared.mongodb.SpringMongo;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class SVMMain {

	public SVMMain() {
		loadMyDict();
	}

	public static void main(String[] args) throws IOException {

		SVMMain a = new SVMMain();
		/*SpringMongo sm = SpringMongo.getInstance();

		MainNewsCategoryEnum trainCat = MainNewsCategoryEnum.SOCIETY;

		AbstractNewsTrain snt = new AbstractNewsTrain(trainCat);
		List<NewsInfo> train = sm.getNews(trainCat);
		List<NewsInfo> predict = sm.getUncategoriseNews(trainCat, 100);
		List<NewsInfo> all = sm.getAllNews(trainCat);

		//System.out.println(train.size());
		//snt.training(train);

		System.out.println(predict.size());
		snt.predict(predict, true);*/
		//snt.predict(predict, true);

		//System.out.println(predict.size());
		//System.out.println(predict.get(0).getTitle());
		//snt.predict(all, false);
		//snt.strengthen();

		ViewSNewsEngine view = new ViewSNewsEngine();
		ControllerSNewsEngine c = new ControllerSNewsEngine(view);
		view.setVisible(true);
	}

	private void addNewsTags() {
		SpringMongo sm = SpringMongo.getInstance();
		List<NewsInfo> news = sm.getAllNews(MainNewsCategoryEnum.POLITIC);
		NewsTagMaker ntm = new NewsTagMaker();
		ntm.addNewsTag(news);
	}

	private void loadMyDict() {
		WordDictionary wd = WordDictionary.getInstance();
		Path customDict = Paths.get(Resources.RESOURCE_PATH + "my_dict.txt");
		wd.loadUserDict(customDict);
	}
}
