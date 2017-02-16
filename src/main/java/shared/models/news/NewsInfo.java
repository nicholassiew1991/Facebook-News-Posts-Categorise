package shared.models.news;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Field;
import shared.models.facebook.Page;
import shared.models.news.categories.*;

import java.util.ArrayList;

@org.springframework.data.mongodb.core.mapping.Document(collection="newsInfo")
public class NewsInfo {

	@Id
	private String postId;
	@Field
	private String title = "";
	@Field
	private String mainContent = "";
	@Field
	private Page page;
	@Field
	private long createTime;
	@Field
	private String newsUrl = "";
	@Field
	private ArrayList<String> reporters = new ArrayList<>();
	@Field
	private ArrayList<NewsImages> images = new ArrayList<>();
	@Field
	private NewsCategory category = new NewsCategory();
	@Field
	private NewsCategory svmCategory = new NewsCategory();
	@Field
	private ArrayList<String> tags = new ArrayList<>();
	@Field
	private boolean svmPredicted = false;

	@PersistenceConstructor
	public NewsInfo(String postId) {
		this.postId = postId;
	}

	public String getPostId() {
		return postId;
	}

	public void setPostId(String postId) {
		this.postId = postId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setContent(String content) {
		this.mainContent = content;
	}

	public String getContent() {

		String str = this.mainContent;

		str = StringUtils.substringBefore(str, "看了這");
		//str = StringUtils.substringBefore(str, "看了這");
		return str;
	}

	public Page getPage() { return page; }

	public void setPage(Page p) { this.page = p; }

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public String getNewsUrl() {
		return newsUrl;
	}

	public void setNewsUrl(String newsUrl) {
		this.newsUrl = newsUrl;
	}

	public void setReporters(ArrayList<String> author) {
		this.reporters = author;
	}

	public ArrayList<String> getReporters() {
		return this.reporters;
	}

	public void setImages(ArrayList<NewsImages> images) {
		this.images = images;
	}

	public ArrayList<NewsImages> getImages() {
		return this.images;
	}

	public void setMainCategory(String i) {
		this.category.setNewsCat(i);
		this.category.setSubNewsCat("UNCATEGORISED");
	}

	public void setSubNewsCategory(String i) {
		this.category.setSubNewsCat(i);
	}

	public String getMainCategory() {
		return this.category.getNewsCat();
	}

	public String getSubCategory() {
		return this.category.getSubNewsCat();
	}

	public int getNumericMainCategory() {
		MainNewsCategoryEnum nce = MainNewsCategoryEnum.getEnumValue(getMainCategory());
		return nce.ordinal();
	}

	public int getNumericSubCategory() {

		MainNewsCategoryEnum nce = MainNewsCategoryEnum.getEnumValue(getMainCategory());

		Enum e = null;
		String subCat = getSubCategory();

		if (subCat.isEmpty()) {
			return SportNewsCategoryEnum.UNCATEGORISED.ordinal();
		}

		if (nce.getValue() == MainNewsCategoryEnum.SPORT.getValue()) {
			e = SportNewsCategoryEnum.getEnumValue(subCat);
		}
		else if (nce.getValue() == MainNewsCategoryEnum.ENTERTAINMENT.getValue()) {
			e = EntertainmentNewsCategoryEnum.getEnumValue(subCat);
		}
		else if (nce.getValue() == MainNewsCategoryEnum.FINANCE.getValue()) {
			e = FinanceNewsCategoryEnum.getEnumValue(subCat);
		}
		else if (nce.getValue() == MainNewsCategoryEnum.INTERNATIONAL.getValue()) {
			e = InternationalNewsCategoryEnum.getEnumValue(subCat);
		}
		else if (nce.getValue() == MainNewsCategoryEnum.POLITIC.getValue()) {
			e = null; // TODO: Continue Extends
		}
		else if (nce.getValue() == MainNewsCategoryEnum.SOCIETY.getValue()) {
			e = SocietyNewsCategoryEnum.getEnumValue(subCat);
		}
		return (e == null ? -1 : e.ordinal());
	}

	public void setSVMMainCategory(String str) {
		this.svmCategory.setNewsCat(str);
	}

	public void setSVMSubNewsCategory(String str) {
		this.svmCategory.setSubNewsCat(str);
	}

	public String getSVMMainCategory() {
		return this.svmCategory.getNewsCat();
	}

	public String getSVMSubNewsCategory() {
		return this.svmCategory.getSubNewsCat();
	}

	public NewsCategory getCategory() {
		return category;
	}

	public void setCategory(NewsCategory category) {
		this.category = category;
	}

	public NewsCategory getSvmCategory() {
		return svmCategory;
	}

	public void setSvmCategory(NewsCategory svmCategory) {
		this.svmCategory = svmCategory;
	}

	public ArrayList<String> getTags() {
		return this.tags;
	}

	public void setTags(ArrayList<String> tag) {
		this.tags = tag;
	}

	public void addTag(ArrayList<String> tags) {
		for (String tag : tags) {
			addTag(tag);
		}
	}

	public void addTag(String tag) {
		if (this.tags == null) {
			this.tags = new ArrayList<>();
		}
		if (isTagExists(tag) == false) {
			this.tags.add(tag);
		}
	}

	public boolean isSvmPredicted() {
		return svmPredicted;
	}

	public void setSvmPredicted(boolean svmPredicted) {
		this.svmPredicted = svmPredicted;
	}

	public boolean isTagExists(String tag) {
		return this.tags.contains(tag);
	}
}
