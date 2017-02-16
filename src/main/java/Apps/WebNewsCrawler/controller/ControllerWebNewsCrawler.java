package Apps.WebNewsCrawler.controller;

import shared.models.news.NewsInfo;
import Apps.Main.ControllerMain;
import Apps.WebNewsCrawler.crawler.AbstractNewsCrawler;
import shared.Resources;
import shared.models.facebook.Page;
import shared.models.facebook.Post;
import shared.mongodb.SpringMongo;
import shared.utils.FileUtils;
import shared.utils.JsonUtils;
import Apps.WebNewsCrawler.view.ViewNewsCrawler;

import javax.json.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

/**
 * Created by Nicholas on 10/7/2016.
 */
public class ControllerWebNewsCrawler {

	ViewNewsCrawler view;
	ControllerMain parent = null;

	public ControllerWebNewsCrawler(ViewNewsCrawler v) {
		this.view = v;
		setPageComboBoxData();
		setActionListeners();
	}
	
	public ControllerWebNewsCrawler(ControllerMain parent, ViewNewsCrawler view) {
		this(view);
		this.parent = parent;
		view.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}

	private void setPageComboBoxData() {

		JComboBox<Page> cmbPages = view.getPagesComboBox();

		cmbPages.setEnabled(false);
		
		File f = new File(Resources.FILE_FACEBOOK_PAGES);
		FileReader fr = null;

		FileUtils.initJsonFile(f);

		try {
			fr = new FileReader(f);
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		if (fr != null) {
			JsonObject obj = JsonUtils.getJsonObject(fr);

			JsonArray pages = obj.getJsonArray("pages");

			if (pages == null) {
				JOptionPane.showMessageDialog(null, "Can't load the pages.");
				return;
			}

			if (!pages.isEmpty()) {
				if (pages.size() > 1) {
					cmbPages.addItem(new Page("All", "All", "All"));
				}
				for (JsonValue v : pages) {
					JsonObject page = (JsonObject) v;
					String pageId = page.getString("id");
					String pageName = page.getString("name");
					String url_id = page.getString("url_id");
					Page p = new Page(pageId, pageName, url_id);
					cmbPages.addItem(p);
				}
			}
		}

		if (cmbPages.getItemCount() > 0) {
			cmbPages.setEnabled(true);
		}
	}

	private void setActionListeners() {
		view.setBtnStartListener(new StartButtonListener());
		view.addWindowListener(new FrameWindowListener());
	}

	private class StartButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			//SpringMongo mongodb = new SpringMongo();
			SpringMongo mongodb = SpringMongo.getInstance();

			ArrayList<Page> pages = new ArrayList<>();
			Page tempPage = (Page) view.getPagesComboBox().getSelectedItem();

			if (tempPage.getPageId().equalsIgnoreCase("All")) {
				pages = view.getPagesComboBoxItems();
			}
			else {
				pages.add((Page) view.getPagesComboBox().getSelectedItem());
			}

			System.out.println(pages);

			ArrayList<NewsInfo> allNews = new ArrayList<>();

			for (Page p : pages) {
				String pageId = p.getPageId();
				Page dbPage = mongodb.getPageData(pageId);
				ArrayList<Post> posts = dbPage.getFbPost();

				AbstractNewsCrawler newsCrawler = AbstractNewsCrawler.getInstance(p.getPageUrlId());
				ArrayList<NewsInfo> news = newsCrawler.extractNews(p, posts);
				allNews.addAll(news);
			}

			mongodb.saveNewsToDb(allNews);
			JOptionPane.showMessageDialog(null, "Completed! News are saved to database.");

			/*JComboBox cmbPages = view.getPagesComboBox();
			Page p = (Page) cmbPages.getSelectedItem();
			String pageId = ((Page)cmbPages.getSelectedItem()).getPageId();
			System.out.println("test");
			Page dbPage = mongodb.getPageData(pageId);
			System.out.println(dbPage);


			AbstractNewsCrawler newsCrawler = AbstractNewsCrawler.getInstance("");
			ArrayList<NewsInfo> news = newsCrawler.extractNews(p, posts);
			mongodb.saveNewsToDb(news);*/
		}
	}
	
	private class FrameWindowListener implements WindowListener {

		@Override
		public void windowActivated(WindowEvent arg0) {
		}

		@Override
		public void windowClosed(WindowEvent arg0) {
			if (parent != null) {
				parent.setViewEnabled(true);
			}
		}

		@Override
		public void windowClosing(WindowEvent arg0) {
		}

		@Override
		public void windowDeactivated(WindowEvent arg0) {
		}

		@Override
		public void windowDeiconified(WindowEvent arg0) {
		}

		@Override
		public void windowIconified(WindowEvent arg0) {
		}

		@Override
		public void windowOpened(WindowEvent arg0) {
			if (parent != null) {
				parent.setViewEnabled(false);
			}
		}
	}
}
