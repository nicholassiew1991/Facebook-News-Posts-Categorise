package Apps.NewsAnalysis.view;

import shared.models.facebook.Page;
import shared.models.news.categories.MainNewsCategoryEnum;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Created by Nicholas on 17/11/2016.
 */
public class ViewSNewsEngine extends JFrame{

	private JPanel panel1;
	private JButton btnTraining;
	private JButton btnPredict;
	private JButton btnStrengthen;
	private JComboBox<String> cmbCategory;

	private String strTitle = "SNews Engine";

	public ViewSNewsEngine() {
		setTitle(strTitle);
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

	private void buildNorthPanel() {
		JLabel lblTitle = new JLabel(strTitle, SwingConstants.CENTER);
		lblTitle.setFont(new Font(null, Font.BOLD, 20));
		panel1.add(lblTitle, BorderLayout.NORTH);
	}

	private void buildSouthPanel() {

		JPanel p = new JPanel();
		btnTraining = new JButton("Training");
		btnPredict = new JButton("Predict");
		btnStrengthen = new JButton("Strengthen");

		p.add(btnTraining);
		p.add(btnPredict);
		p.add(btnStrengthen);
		panel1.add(p, BorderLayout.SOUTH);
	}

	private void buildCenterPanel() {

		JPanel p = new JPanel();

		cmbCategory = new JComboBox<>();

		p.add(cmbCategory);
		panel1.add(p, BorderLayout.CENTER);
	}

	public void addComboBoxItem(String p) {
		cmbCategory.addItem(p);
	}

	public MainNewsCategoryEnum getComboBoxCategory() {

		if (cmbCategory.getSelectedIndex() == 0) {
			return null;
		}
		return MainNewsCategoryEnum.getEnumValue(cmbCategory.getSelectedItem().toString());
	}

	public void addTrainingBtnListener(ActionListener a) {
		btnTraining.addActionListener(a);
	}

	public void addPredictBtnListener(ActionListener a) {
		btnPredict.addActionListener(a);
	}

	public void addStrengthenBtnListener(ActionListener a) {
		btnStrengthen.addActionListener(a);
	}
}
