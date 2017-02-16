package Apps.FeedsCrawler;

import Apps.FeedsCrawler.controller.ControllerFacebookFeedCrawler;
import Apps.FeedsCrawler.view.ViewFacebookFeedCrawler;

/**
 * Created by Nicholas on 8/7/2016.
 */
public class Main {

	public static void main(String[] args) {
		ViewFacebookFeedCrawler f = new ViewFacebookFeedCrawler();
		ControllerFacebookFeedCrawler c = new ControllerFacebookFeedCrawler(f);
		f.setVisible(true);
	}
}
