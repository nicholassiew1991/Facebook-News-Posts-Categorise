package Apps.FeedsCrawler.controller;

import Apps.FeedsCrawler.crawler.FacebookFeedsCrawler;
import shared.models.facebook.Page;
import shared.mongodb.SpringMongo;
import shared.utils.JsonUtils;
import shared.utils.Utils;
import shared.Resources;
import Apps.FeedsCrawler.view.ViewFacebookFeedCrawler;
import Apps.FeedsCrawler.view.ViewManagePages;
import Apps.Main.ControllerMain;

import javax.json.*;
import javax.rmi.CORBA.Util;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.*;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Nicholas on 23/4/2016.
 */
public class ControllerFacebookFeedCrawler {

	ViewFacebookFeedCrawler view;
	ControllerMain parent = null;

	private void init() {
		setPageComboBoxData();
		addListeners();
	}

	public ControllerFacebookFeedCrawler(ViewFacebookFeedCrawler view) {
		this.view = view;
		init();
	}
	
	public ControllerFacebookFeedCrawler(ControllerMain parent, ViewFacebookFeedCrawler view) {
		this(view);
		this.parent = parent;
		view.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}

	public void resetComboBox() {
		view.getComboBox().removeAllItems();
		setPageComboBoxData();
	}

	public void setViewEnable(boolean b) {

		view.setEnabled(b);

		if (b == true) {
			view.requestFocus();
		}
	}

	private void actionStartButton() {

		String since = view.getSinceText();
		String until = view.getUntilText();

		if (since.isEmpty() || until.isEmpty()) {
			JOptionPane.showMessageDialog(null, "Since and Until date can't be empty.");
			return;
		}

		ArrayList<Page> pages = new ArrayList<>();

		Page p = (Page) view.getComboBox().getSelectedItem();

		if (p.getPageId().equalsIgnoreCase("All")) {
			pages = view.getComboBoxItems();
		}
		else {
			pages.add((Page) view.getComboBox().getSelectedItem());
		}

		FacebookFeedsCrawler crawler = new FacebookFeedsCrawler();
		int status = crawler.crawlFeeds(pages, since, until);

		if (status == FacebookFeedsCrawler.CRAWL_SUCCESS) {
			//SpringMongo sm = new SpringMongo();
			SpringMongo sm = SpringMongo.getInstance();
			sm.saveToDb(pages);
		}
	}

	private void actionManagePagesButton() {
		ViewManagePages v = new ViewManagePages();
		ControllerManagePages c = new ControllerManagePages(this, v);
		//ControllerManagePages c = new ControllerManagePages(this, v);
		v.setVisible(true);
	}

	private void addListeners() {
		view.addBtnStartListener(new StartButtonListener());
		view.addBtnManagePagesListener(new ManagePagesButtonListener());
		view.addWindowListener(new FrameWindowListener());
	}

	private void setPageComboBoxData() {

		view.setComboBoxEnable(false);
		
		File f = new File(Resources.FILE_FACEBOOK_PAGES);
		FileReader fr = null;

		if (!f.exists()) {
			try {
				f.getParentFile().mkdirs();
				f.createNewFile();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}

		if (f.length() <= 0) {
			JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
			JsonObject pageJsonObject = jsonObjectBuilder.build();
			JsonWriter jsonWriter = JsonUtils.getPrettyPrintJsonWriter(Resources.FILE_FACEBOOK_PAGES);
			jsonWriter.writeObject(pageJsonObject);
			jsonWriter.close();
		}

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
					view.addComboBoxItem(new Page("All", "All", "All"));
				}
				for (JsonValue v : pages) {
					JsonObject page = (JsonObject) v;
					String pageId = page.getString("id");
					String pageName = page.getString("name");
					String url_id = page.getString("url_id");
					Page p = new Page(pageId, pageName, url_id);
					view.addComboBoxItem(p);
				}
			}
		}

		if (view.getComboBoxItemCount() > 0) {
			view.setComboBoxEnable(true);
		}
	}

	private class StartButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			actionStartButton();
		}
	}

	private class ManagePagesButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			actionManagePagesButton();
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
