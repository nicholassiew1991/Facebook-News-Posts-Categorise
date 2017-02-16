package Apps.NewsAnalysis.svm;

import Apps.NewsAnalysis.articles.*;
import com.huaban.analysis.jieba.JiebaSegmenter;
import ksvm.data.BasicRDIter;
import ksvm.run.SVMPredict;
import ksvm.run.SVMTrain;
import shared.Resources;
import shared.models.news.NewsCategory;
import shared.models.news.NewsInfo;
import shared.models.news.categories.*;
import shared.mongodb.SpringMongo;
import shared.utils.FileUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nicholas on 7/9/2016.
 */
public class AbstractNewsTrain {

	public enum SVMActions {
		TRAIN, PREDICT, STRENGTHEN
	}

	private MainNewsCategoryEnum e;
	private String category;

	public AbstractNewsTrain(MainNewsCategoryEnum e) {
		this.e = e;
		this.category = e.name().toLowerCase();
	}

	public void training(List<NewsInfo> news) {
		prepareData(news, SVMActions.TRAIN);
		training();
	}

	public void training() {

		String trainDataPath = Resources.RESOURCE_PATH + "trainData\\" + category + ".tf";
		String modelPath = Resources.RESOURCE_PATH + "trainedModel\\" + category + ".models";

		try {
			BasicRDIter basicRDIter = new BasicRDIter(new File(trainDataPath));
			SVMTrain train = new SVMTrain(basicRDIter);

			if(train.start()) {
				System.out.printf("\t[Info] Training is done!\n");

				File modelFile = new File(modelPath);
				System.out.println(modelPath);
				if (!modelFile.exists()) {
					modelFile.getParentFile().mkdirs();
					modelFile.createNewFile();
				}
				train.saveModel(modelFile);
			}
			else {
				System.out.printf("\t[Info] Something wrong while training:\n");
				for(String em : train.errMsg) {
					System.out.printf("\t%s\n", em);
				}
				return;
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void strengthen() {

		SpringMongo sm = SpringMongo.getInstance();

		ArrayList<NewsInfo> predictedNews = (ArrayList<NewsInfo>) sm.getSVMPredictedNews(e);
		ArrayList<NewsInfo> trainedNews = (ArrayList<NewsInfo>) sm.getNews(e);

		System.out.println(predictedNews.size());
		System.out.println(trainedNews.size());

		for (NewsInfo n : predictedNews) {
			n.setCategory(n.getSvmCategory());
			n.setSvmCategory(new NewsCategory());
			n.setSvmPredicted(false);
		}

		trainedNews.addAll(trainedNews.size(), predictedNews);

		prepareData(trainedNews, SVMActions.STRENGTHEN);
		training();
		sm.saveNewsInfo(predictedNews);
	}

	public void predict(List<NewsInfo> news) {
		predict(news, false);
	}

	public void predict(List<NewsInfo> news, boolean save) {

		if (news.isEmpty()) {
			System.err.println("News is empty.");
			System.exit(1);
		}

		prepareData(news, SVMActions.PREDICT);
		predict();

		if (save == true) {

			for (NewsInfo n : news) {
				n.setSvmPredicted(true);
			}

			save(news);
		}
	}

	public void predict() {

		File modelFile = new File(Resources.RESOURCE_PATH + "trainedModel\\" + category + ".models");
		File testFile = new File(Resources.RESOURCE_PATH + "testData\\" + category + ".tf");
		File resultFile = new File(Resources.RESOURCE_PATH + "resultData\\" + category + ".pid");

		if (!modelFile.exists()) {
			System.err.println(modelFile + " not exists. Please train first.");
			return;
		}

		try {

			if (!resultFile.exists()) {
				resultFile.getParentFile().mkdirs();
				resultFile.createNewFile();
			}
			SVMPredict svmPredict = new SVMPredict(modelFile);
			svmPredict.start(new BasicRDIter(testFile), resultFile, true);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void save(List<NewsInfo> news) {

		File resultFile = new File(Resources.RESOURCE_PATH + "resultData\\" + category + ".pid");

		List<String> results = FileUtils.readFileLine(resultFile);
		results.remove(0);

		if (news.size() == results.size()) {

			Enum[] val = null;

			// TODO: Continue Extends
			if (category.equalsIgnoreCase("sport")) {
				val = SportNewsCategoryEnum.values();
			}
			else if (category.equalsIgnoreCase("entertainment")) {
				val = EntertainmentNewsCategoryEnum.values();
			}
			else if (category.equalsIgnoreCase("international")) {
				val = InternationalNewsCategoryEnum.values();
			}
			else if (category.equalsIgnoreCase("society")) {
				val = SocietyNewsCategoryEnum.values();
			}
			else if (category.equalsIgnoreCase("finance")) {
				val = FinanceNewsCategoryEnum.values();
			}
			else if (val == null) {
				System.err.println("Could not save the predicted result. Enum value is null.");
				return;
			}

			SpringMongo sm = SpringMongo.getInstance();

			for (int i = 0; i < news.size(); i++) {
				String resultRecord = results.get(i);
				int predictAnswer = (int) Double.parseDouble(resultRecord.split("/")[0]);
				NewsInfo n = news.get(i);
				n.setSVMMainCategory(n.getMainCategory());
				n.setSVMSubNewsCategory(val[predictAnswer].name());
				sm.saveNewsInfo(n);
			}
		}
	}

	public void prepareData(List<NewsInfo> news, SVMActions e) {

		if (news == null || news.isEmpty()) {
			System.err.println("prepareData: News list is empty.");
			return;
		}
		else if (e == null) {
			System.err.println("prepareData: SVMActions can't be null.");
			return;
		}

		String path = null;
		//String category = news.get(0).getMainCategory();

		if (e.name().equalsIgnoreCase(SVMActions.TRAIN.name()) || e.name().equalsIgnoreCase(SVMActions.STRENGTHEN.name())) {
			path = Resources.RESOURCE_PATH + "trainData\\" + category + ".tf";
		}
		else if (e.name().equalsIgnoreCase(SVMActions.PREDICT.name())) {
			path = Resources.RESOURCE_PATH + "testData\\" + category + ".tf";
		}

		List<String> line = getTrainData(news);

		StringBuilder sb = new StringBuilder();

		for (NewsInfo n : news) {
			sb.append(getNumericalNews(n).toString() + "\n");
		}

		try {
			writeData(path, sb.toString());
		}
		catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	public List<String> getTrainData(List<NewsInfo> news) {

		List<String> line = new ArrayList<>();

		for (NewsInfo n : news) {
			AbstractArticle a = getNumericalNews(n);
			line.add(a.toString());
		}
		return line;
	}

	public AbstractArticle getNumericalNews(NewsInfo n) {

		JiebaSegmenter js = new JiebaSegmenter();

		AbstractArticle article = getArticleObject(n);

		if (article != null) {
			String newsContent = n.getContent();
			List<String> ss = js.sentenceProcess(newsContent);

			for (String word : ss) {
				article.addWeight(word);
			}
		}
		return article;
	}

	public AbstractArticle getArticleObject(NewsInfo n) {

		int category = n.getNumericMainCategory();

		if (category == MainNewsCategoryEnum.SPORT.ordinal()) {
			return new SportArticle(n);
		}
		else if (category == MainNewsCategoryEnum.ENTERTAINMENT.ordinal()) {
			return new EntertainmentArticle(n);
		}
		else if (category == MainNewsCategoryEnum.FINANCE.ordinal()) {
			return new FinanceArticle(n);
		}
		else if (category == MainNewsCategoryEnum.INTERNATIONAL.ordinal()) {
			return new InternationalArticle(n);
		}
		else if (category == MainNewsCategoryEnum.POLITIC.ordinal()) {
			return null; // TODO: Continue Extends
		}
		else if (category == MainNewsCategoryEnum.SOCIETY.ordinal()) {
			return new SocietyArticle(n);
		}
		return null;
	}

	protected void writeData(String path, String str) throws IOException {

		File f = new File(path);

		if (!f.exists()) {
			f.getParentFile().mkdirs();
			f.createNewFile();
		}

		FileWriter fw = new FileWriter(f);

		fw.write(str);
		fw.close();
	}
}
