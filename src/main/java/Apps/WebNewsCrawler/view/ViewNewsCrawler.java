package Apps.WebNewsCrawler.view;

import Apps.WebNewsCrawler.controller.ControllerWebNewsCrawler;
import shared.models.facebook.Page;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Created by Nicholas on 23/4/2016.
 */
public class ViewNewsCrawler extends JFrame{

	private JPanel mainPanel;
	private JComboBox<Page> cmbPages;
	private JButton btnStart;

	public ViewNewsCrawler() {
		initUIComponents();
		setTitle("News Crawler");
		setSize(250, 160);
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	private void initUIComponents() {
		mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout(0, 5));
		mainPanel.setBorder(new EmptyBorder(10, 10, 10 , 10));
		buildMainPanelNorth();
		buildMainPanelSouth();
		buildMainPanelCenter();
		add(mainPanel);
	}

	private void buildMainPanelNorth() {
		JLabel title = new JLabel("News Crawler", SwingConstants.CENTER);
		title.setFont(new Font(null, Font.BOLD, 20));
		mainPanel.add(title, BorderLayout.NORTH);
	}

	private void buildMainPanelSouth() {
		JPanel p = new JPanel();
		btnStart = new JButton("Start");
		p.add(btnStart);
		add(p, BorderLayout.SOUTH);
	}

	private void buildMainPanelCenter() {

		JPanel p = new JPanel();
		JLabel lbl = new JLabel("Pages: ");
		cmbPages = new JComboBox<>();

		GroupLayout layout = new GroupLayout(p);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);

		layout.setHorizontalGroup(layout.createSequentialGroup()
			.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addComponent(lbl)
			)
			.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addComponent(cmbPages)
			)
		);

		layout.setVerticalGroup(layout.createSequentialGroup()
			.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
				.addComponent(lbl).addComponent(cmbPages)
			)
		);

		p.setLayout(layout);
		mainPanel.add(p, BorderLayout.CENTER);
	}

	public JComboBox<Page> getPagesComboBox() {
		return this.cmbPages;
	}

	public ArrayList<Page> getPagesComboBoxItems() {

		ArrayList<Page> pages = new ArrayList<>();

		for (int i = 0; i < cmbPages.getItemCount(); i++) {
			pages.add(cmbPages.getItemAt(i));
		}

		pages.remove(0);

		return (pages.size() > 0 ? pages : null);
	}

	public void setBtnStartListener(ActionListener ac) {
		this.btnStart.addActionListener(ac);
	}
}
