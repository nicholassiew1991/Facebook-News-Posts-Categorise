package Apps.FeedsCrawler.controller;

import shared.models.facebook.Page;
import shared.utils.JsonUtils;
import shared.Resources;
import Apps.FeedsCrawler.facebook.GraphAPI;
import Apps.FeedsCrawler.view.ViewManagePages;

import javax.json.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.*;

/**
 * Created by Nicholas on 24/4/2016.
 */
public class ControllerManagePages {

	ControllerFacebookFeedCrawler parent;
	ViewManagePages view;

	private void init() {
		initTableData();
		addListeners();
	}

	public ControllerManagePages(ControllerFacebookFeedCrawler parent, ViewManagePages view) {
		this.parent = parent;
		this.view = view;
		init();
	}

	private void initTableData() {

		try {
			JsonReader reader = Json.createReader(new FileReader(Resources.FILE_FACEBOOK_PAGES));

			JsonObject obj = reader.readObject();
			JsonArray pages = obj.getJsonArray("pages");
			
			if (pages == null) {
				return;
			}
			else if (!pages.isEmpty()) {
				for (JsonValue v : pages) {
					JsonObject page = (JsonObject) v;
					String pageId = page.getString("id");
					String pageName = page.getString("name");
					String url_id = page.getString("url_id");
					Page p = new Page(pageId, pageName, url_id);
					view.addTableRow(p);
				}
			}
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void addListeners() {
		view.getBtnAdd().addActionListener(new AddButtonListener());
		view.getBtnDelete().addActionListener(new DeleteButtonListener());
		view.getBtnSave().addActionListener(new SaveButtonListener());
		view.addWindowListener(new DialogWindowListener());
	}

	private class AddButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			final String URL_FORMAT = "%s/%s?fields=id,name&access_token=%s";

			String url_id = JOptionPane.showInputDialog(view, "Enter the url id of the page:");

			if (url_id != null && !url_id.isEmpty()) {
				view.getBtnAdd().setEnabled(false);
				String url = String.format(URL_FORMAT, GraphAPI.FACEBOOK_API_URL, url_id, GraphAPI.ACCESS_TOKEN);
				JsonObject obj = JsonUtils.httpGetJsonObject(url);

				if (obj != null) {
					String name = obj.getString("name");
					String id = obj.getString("id");
					view.addTableRow(new Page(id, name, url_id));
				}
				view.getBtnAdd().setEnabled(true);
			}
		}
	}

	private class DeleteButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			view.removeTableRow();
		}
	}

	private class SaveButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			view.getBtnSave().setEnabled(false);
			view.getBtnAdd().setEnabled(false);
			view.getBtnDelete().setEnabled(false);
			view.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
			JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
			jsonObjectBuilder.add("pages", getPagesJsonArray());

			JsonObject pageJsonObject = jsonObjectBuilder.build();

			JsonWriter jsonWriter = JsonUtils.getPrettyPrintJsonWriter(Resources.FILE_FACEBOOK_PAGES);
			jsonWriter.writeObject(pageJsonObject);
			jsonWriter.close();
			view.getBtnSave().setEnabled(true);
			view.dispose();
		}

		private JsonArrayBuilder getPagesJsonArray() {

			JsonArrayBuilder arr = Json.createArrayBuilder();

			int rowCount = view.getTableRowCount();

			for (int i = 0; i < rowCount; i++) {
				arr.add(getPageJsonObject(view.getPageObject(i)));
			}

			return arr;
		}

		private JsonObject getPageJsonObject(Page p) {
			JsonObject obj = Json.createObjectBuilder()
				.add("id", p.getPageId())
				.add("name", p.getPageName())
				.add("url_id", p.getPageUrlId())
				.build();
			return obj;
		}
	}

	private class DialogWindowListener implements WindowListener {

		@Override
		public void windowOpened(WindowEvent e) {
			parent.setViewEnable(false);
		}

		@Override
		public void windowClosing(WindowEvent e) {

		}

		@Override
		public void windowClosed(WindowEvent e) {
			parent.setViewEnable(true);
			parent.resetComboBox();
		}

		@Override
		public void windowIconified(WindowEvent e) {

		}

		@Override
		public void windowDeiconified(WindowEvent e) {

		}

		@Override
		public void windowActivated(WindowEvent e) {

		}

		@Override
		public void windowDeactivated(WindowEvent e) {

		}
	}
}
