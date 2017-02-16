package Apps.Main;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class ViewMain extends JFrame {
	
	private JPanel mainPanel;
	private JButton btnFeedCrawler;
	private JButton btnNewsCrawler;

	public ViewMain() {
		createUIComponents();
		setTitle("Facebook News Crawler");
		setSize(325, 250);
		setResizable(false);
		setLocationRelativeTo(null);
		pack();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	private void createUIComponents() {
		initMainPanel();
	}
	
	private void initMainPanel() {
		mainPanel = new JPanel();
		
		mainPanel.setLayout(new BorderLayout());
		initMainPanelNorth();
		initMainPanelCentre();
		
		add(mainPanel);
	}
	
	private void initMainPanelNorth() {
		JLabel lblTitle = new JLabel("Facebook News Crawler", SwingConstants.CENTER);
		lblTitle.setFont(new Font(null, Font.BOLD, 20));
		mainPanel.add(lblTitle, BorderLayout.NORTH);
	}
	
	private void initMainPanelCentre() {
		JPanel panel = new JPanel();
		btnFeedCrawler = new JButton("Facebook Feeds Crawler");
		btnNewsCrawler = new JButton("Web News Crawler");
		
		panel.add(btnFeedCrawler);
		panel.add(btnNewsCrawler);
		
		mainPanel.add(panel);
	}

	public JButton getBtnFeedCrawler() {
		return btnFeedCrawler;
	}

	public void setBtnFeedCrawler(JButton btnFeedCrawler) {
		this.btnFeedCrawler = btnFeedCrawler;
	}

	public JButton getBtnNewsCrawler() {
		return btnNewsCrawler;
	}

	public void setBtnNewsCrawler(JButton btnNewsCrawler) {
		this.btnNewsCrawler = btnNewsCrawler;
	}	
	

}
