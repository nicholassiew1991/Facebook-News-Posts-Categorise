package shared.models.news;

import shared.models.news.categories.MainNewsCategoryEnum;

/**
 * Created by Nicholas on 30/8/2016.
 */
public class NewsCategory {

	private String newsCat;
	private String subNewsCat;

	public NewsCategory() {
		this.newsCat = MainNewsCategoryEnum.UNCATEGORISED.name();
		this.subNewsCat = "";
	}

	public NewsCategory(MainNewsCategoryEnum e) {
		this();
		this.newsCat = e.name();
	}

	public String getNewsCat() {
		return newsCat;
	}

	public void setNewsCat(String newsCat) {
		this.newsCat = newsCat;
	}

	public String getSubNewsCat() {
		return subNewsCat;
	}

	public void setSubNewsCat(String subNewsCat) {
		this.subNewsCat = subNewsCat;
	}

	@Override
	public String toString() {
		return "Main:" + newsCat + " | Sub: " + subNewsCat;
	}
}
