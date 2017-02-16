import org.junit.Test;
import shared.models.news.NewsInfo;
import shared.models.news.categories.FinanceNewsCategoryEnum;
import shared.models.news.categories.MainNewsCategoryEnum;
import shared.models.news.categories.SocietyNewsCategoryEnum;
import shared.mongodb.SpringMongo;

import java.util.List;

/**
 * Created by Nicholas on 28/10/2016.
 */
public class MongoModifications {

	@Test
	public void modifySubCategorise() {

		SpringMongo sm = SpringMongo.getInstance();
		List<NewsInfo> news = sm.getNews(MainNewsCategoryEnum.FINANCE);

		for (NewsInfo n : news) {
			String subCat = n.getSubCategory();

			if (subCat.equalsIgnoreCase("s")) {
				n.setSubNewsCategory(FinanceNewsCategoryEnum.STOCK.name());
			}
			if (subCat.equalsIgnoreCase("e")) {
				n.setSubNewsCategory(FinanceNewsCategoryEnum.ESTATE.name());
			}
			if (subCat.equalsIgnoreCase("i")) {
				n.setSubNewsCategory(FinanceNewsCategoryEnum.INDUSTRY.name());
			}
			if (subCat.equalsIgnoreCase("p")) {
				n.setSubNewsCategory(FinanceNewsCategoryEnum.EMPLOYMENT.name());
			}
			if (subCat.equalsIgnoreCase("o")) {
				n.setSubNewsCategory(FinanceNewsCategoryEnum.OTHERS.name());
			}
		}

		System.out.println();

		for (NewsInfo n : news) {
			String subCat = n.getSubCategory();
			System.out.println(subCat);
		}

		sm.saveNewsInfo(news);
	}
}
