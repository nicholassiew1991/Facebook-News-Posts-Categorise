package Apps.Main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import Apps.FeedsCrawler.controller.ControllerFacebookFeedCrawler;
import Apps.FeedsCrawler.view.ViewFacebookFeedCrawler;
import Apps.WebNewsCrawler.controller.ControllerWebNewsCrawler;
import Apps.WebNewsCrawler.view.ViewNewsCrawler;

public class ControllerMain {
	
	private ViewMain view;
	
	public ControllerMain(ViewMain v) {
		this.view = v;
		init();
	}
	
	private void init() {
		addListener();
	}
	
	public void setViewEnabled(boolean b) {
		view.setEnabled(b);
		
		if (b == true) {
			view.requestFocus();
		}
	}
	
	private void addListener() {
		view.getBtnFeedCrawler().addActionListener(new FacebookFeedCrawlerListener());
		view.getBtnNewsCrawler().addActionListener(new WebNewsCrawlerListener());
	}
	
	private class FacebookFeedCrawlerListener implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			ViewFacebookFeedCrawler f = new ViewFacebookFeedCrawler();
			ControllerFacebookFeedCrawler c = new ControllerFacebookFeedCrawler(ControllerMain.this, f);
			f.setVisible(true);
		}
	}
	
	private class WebNewsCrawlerListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			ViewNewsCrawler a = new ViewNewsCrawler();
			ControllerWebNewsCrawler c = new ControllerWebNewsCrawler(ControllerMain.this, a);
			a.setVisible(true);
		}
		
	}
}
