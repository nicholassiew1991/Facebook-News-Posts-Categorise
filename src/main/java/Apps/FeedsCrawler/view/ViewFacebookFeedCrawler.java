package Apps.FeedsCrawler.view;

import shared.models.facebook.Page;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Created by Nicholas on 23/4/2016.
 */
public class ViewFacebookFeedCrawler extends JFrame{

	private JPanel panel1;
	private JButton btnStart;
	private JButton btnManagePages;
	private JComboBox<Page> cmbPages;
	private JTextField txtSince;
	private JTextField txtUntil;

	public ViewFacebookFeedCrawler() {
		setTitle("Facebook Feed Crawler");
		initUI();
		//setContentPane(panel1);
		setSize(325, 250);
		setResizable(false);
		setLocationRelativeTo(null);
		pack();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	private void initUI() {
		buildPanel();
		add(panel1);
	}

	private void buildPanel() {
		this.panel1 = new JPanel();

		panel1.setLayout(new BorderLayout());
		buildNorthPanel();
		buildCenterPanel();
		buildSouthPanel();

		panel1.setBorder(new EmptyBorder(5, 10, 10, 5));
	}

	private void buildCenterPanel() {

		JPanel p = new JPanel();

		JLabel lblPage = new JLabel("Pages: ", SwingConstants.CENTER);
		JLabel lblSince = new JLabel("Since: ", SwingConstants.CENTER);
		JLabel lblUntil = new JLabel("Until: ", SwingConstants.CENTER);
		cmbPages = new JComboBox<>();
		txtSince = new JTextField();
		txtUntil = new JTextField();

		GroupLayout layout = new GroupLayout(p);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);

		layout.setHorizontalGroup(layout.createSequentialGroup()
			.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addComponent(lblPage).addComponent(lblSince).addComponent(lblUntil)
			)
			.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addComponent(cmbPages).addComponent(txtSince).addComponent(txtUntil)
			)
		);

		layout.setVerticalGroup(layout.createSequentialGroup()
			.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
				.addComponent(lblPage).addComponent(cmbPages)
			)
			.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
				.addComponent(lblSince).addComponent(txtSince)
			)
			.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
				.addComponent(lblUntil).addComponent(txtUntil)
			)
		);

		p.setLayout(layout);
		panel1.add(p, BorderLayout.CENTER);
	}

	private void buildNorthPanel() {
		JLabel title = new JLabel("Facebook Feed Crawler", SwingConstants.CENTER);
		title.setFont(new Font(null, Font.BOLD, 20));
		panel1.add(title, BorderLayout.NORTH);
	}

	private void buildSouthPanel() {

		JPanel p = new JPanel();
		btnStart = new JButton("Start");
		btnManagePages = new JButton("Manage Pages");

		p.add(btnStart);
		p.add(btnManagePages);
		panel1.add(p, BorderLayout.SOUTH);
	}

	public JComboBox<Page> getComboBox() {
		return cmbPages;
	}

	public String getSinceText() {
		return txtSince.getText();
	}

	public String getUntilText() {
		return txtUntil.getText();
	}

	public void addComboBoxItem(Page p) {
		cmbPages.addItem(p);
	}

	public ArrayList<Page> getComboBoxItems() {

		ArrayList<Page> pages = new ArrayList<>();

		for (int i = 0; i < getComboBoxItemCount(); i++) {
			pages.add(cmbPages.getItemAt(i));
		}

		pages.remove(0);

		return (pages.size() > 0 ? pages : null);
	}

	public int getComboBoxItemCount() {
		return cmbPages.getItemCount();
	}

	public void setComboBoxEnable(boolean b) {
		cmbPages.setEnabled(b);
	}

	public void addBtnStartListener(ActionListener a) {
		btnStart.addActionListener(a);
	}

	public void addBtnManagePagesListener(ActionListener a) {
		btnManagePages.addActionListener(a);
	}
}
