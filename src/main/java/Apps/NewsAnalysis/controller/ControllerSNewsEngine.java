package Apps.NewsAnalysis.controller;

import Apps.NewsAnalysis.NewsTagMaker;
import Apps.NewsAnalysis.svm.AbstractNewsTrain;
import Apps.NewsAnalysis.view.ViewSNewsEngine;
import shared.models.news.NewsInfo;
import shared.models.news.categories.MainNewsCategoryEnum;
import shared.mongodb.SpringMongo;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Created by Nicholas on 17/11/2016.
 */
public class ControllerSNewsEngine {

	ViewSNewsEngine view;
	SpringMongo sm = SpringMongo.getInstance();

	public ControllerSNewsEngine(ViewSNewsEngine v) {
		this.view = v;
		init();
	}

	private void init() {
		setPageComboBoxData();
		addListeners();
	}

	private void addListeners() {
		view.addTrainingBtnListener(new SNewsTraining());
		view.addPredictBtnListener(new SNewsPredict());
		view.addStrengthenBtnListener(new SNewsStrengthen());
	}

	private void setPageComboBoxData() {

		MainNewsCategoryEnum[] category = MainNewsCategoryEnum.values();

		view.addComboBoxItem("Select Category");

		for (int i = 1; i < category.length; i++) {
			view.addComboBoxItem(category[i].name());
		}
	}

	private void actionTraining() {

		MainNewsCategoryEnum trainCat = view.getComboBoxCategory();

		if (trainCat == null) {
			JOptionPane.showMessageDialog(null, "Select category.");
			return;
		}
		else if (trainCat == trainCat.POLITIC) {
			System.out.println("hi");
			List<NewsInfo> news = sm.getAllNews(MainNewsCategoryEnum.POLITIC);
			NewsTagMaker ntm = new NewsTagMaker();
			ntm.addNewsTag(news);
		}
		else {
			System.out.println("hello");
			AbstractNewsTrain snt = new AbstractNewsTrain(trainCat);
			List<NewsInfo> train = sm.getNews(trainCat);
			snt.training(train);
		}
	}

	private void actionPredict() {

		MainNewsCategoryEnum trainCat = view.getComboBoxCategory();

		if (trainCat == null) {
			JOptionPane.showMessageDialog(null, "Select category.");
			return;
		}

		int reply = JOptionPane.showConfirmDialog(null, "Save?", "Predict", JOptionPane.YES_NO_OPTION);
		String limit = JOptionPane.showInputDialog(null, "Limit (0 if not limit):", "50");

		AbstractNewsTrain snt = new AbstractNewsTrain(trainCat);
		List<NewsInfo> predict = sm.getUncategoriseNews(trainCat, Integer.parseInt(limit));

		if (reply == JOptionPane.YES_OPTION) {
			snt.predict(predict, true);
		}
		else {
			snt.predict(predict, false);
		}
	}

	private void actionStrengthen() {

		MainNewsCategoryEnum trainCat = view.getComboBoxCategory();

		if (trainCat == null) {
			JOptionPane.showMessageDialog(null, "Select category.");
			return;
		}

		AbstractNewsTrain snt = new AbstractNewsTrain(trainCat);
		snt.strengthen();
	}

	private class SNewsTraining implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			actionTraining();
		}
	}

	private class SNewsPredict implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			actionPredict();
		}
	}

	private class SNewsStrengthen implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			actionStrengthen();
		}
	}
}
