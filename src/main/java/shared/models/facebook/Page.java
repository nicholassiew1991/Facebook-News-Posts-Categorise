package shared.models.facebook;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.ArrayList;

@org.springframework.data.mongodb.core.mapping.Document(collection="pages")
public class Page {

	public static final String ID = "page_id";

	@Id
	private String pageId;
	private String pageName;
	private String pageUrlId;

	@DBRef(db="posts")
	private ArrayList<Post> fbPost;

	@PersistenceConstructor
	public Page(String pageId, String pageName, String pageUrlId) {
		this.pageId = pageId;
		this.pageName = pageName;
		this.pageUrlId = pageUrlId;
		this.fbPost = new ArrayList<Post>();
	}

	public String getPageId() {
		return pageId;
	}

	public void setPageId(String pageId) {
		this.pageId = pageId;
	}

	public String getPageName() {
		return pageName;
	}

	public void setPageName(String pageName) {
		this.pageName = pageName;
	}

	public String getPageUrlId() {
		return pageUrlId;
	}

	public void setPageUrlId(String pageUrlId) {
		this.pageUrlId = pageUrlId;
	}

	public Object getTableData(int index) {
		if (index == 0) {
			return this.pageId;
		}
		else if (index == 1) {
			return this.pageName;
		}
		else if (index == 2) {
			return this.pageUrlId;
		}
		else {
			return null;
		}
	}

	public void addPost(Post fp) {
		fbPost.add(fp);
	}

	public ArrayList<Post> getFbPost() {
		return new ArrayList<>(fbPost);
	}

	public void setFbPost(ArrayList<Post> p) {
		this.fbPost = p;
	}

	/**
	 * The main purpose of overriding this method is bind Page object with JComboBox.
	 * @return Name of page.
	 */
	@Override
	public String toString() {
		// TODO: Chinese word problem when export to jar file
		return pageUrlId;
	}
}
