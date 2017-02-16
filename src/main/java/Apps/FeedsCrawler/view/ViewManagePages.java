package Apps.FeedsCrawler.view;

import shared.models.facebook.Page;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.util.ArrayList;

public class ViewManagePages extends JDialog {

	private JPanel contentPane;
	private JButton btnAdd;
	private JButton btnDelete;
	private JButton btnSave;
	private JTable table1;
	private JScrollPane tableContainer;
	private JButton buttonOK;

	private PageTableModel tab;

	public ViewManagePages() {
		createUIComponents();
		setTitle("Manage Pages");
		setModal(true);
		setSize(525, 450);
		setLocationRelativeTo(null);
		setResizable(false);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	}

	private void createUIComponents() {
		initContentPane();
		/*tab = new PageTableModel();
		table1 = new JTable(tab);
		tableContainer = new JScrollPane(table1);
		add(tableContainer);*/
	}

	private void initContentPane() {
		contentPane = new JPanel();

		contentPane.setLayout(new BorderLayout());
		initContentPaneNorth();
		initContentPaneSouth();
		initContentPaneCentre();

		add(contentPane);
	}

	private void initContentPaneNorth() {
		JLabel lblTitle = new JLabel("Manage Pages", SwingConstants.CENTER);
		lblTitle.setFont(new Font(null, Font.BOLD, 20));
		contentPane.add(lblTitle, BorderLayout.NORTH);
	}

	private void initContentPaneSouth() {

		JPanel p = new JPanel();

		btnAdd = new JButton("Add");
		btnDelete = new JButton("Delete");
		btnSave = new JButton("Save");

		p.add(btnAdd);
		p.add(btnDelete);
		p.add(btnSave);

		contentPane.add(p, BorderLayout.SOUTH);
	}

	private void initContentPaneCentre() {
		tab = new PageTableModel();
		table1 = new JTable(tab);
		tableContainer = new JScrollPane(table1);
		contentPane.add(tableContainer);
	}

	public void addTableRow(Page p) {
		tab.addRow(p);
	}

	public void removeTableRow() {
		tab.removeRow(table1.getSelectedRow());
	}

	public int getTableRowCount() {
		return tab.getRowCount();
	}

	public Page getPageObject(int r) {
		return tab.getPageObjectAt(r);
	}

	public JButton getBtnAdd() {
		return btnAdd;
	}

	public JButton getBtnDelete() {
		return btnDelete;
	}

	public JButton getBtnSave() {
		return btnSave;
	}

	private class PageTableModel extends AbstractTableModel {

		private String[] columnNames = {"Page ID", "Page Name", "Page URL ID"};

		private ArrayList<Page> data;

		public PageTableModel() {
			data = new ArrayList<>();
		}

		@Override
		public int getRowCount() {
			return data.size();
		}

		@Override
		public String getColumnName(int i) {
			return columnNames[i];
		}

		@Override
		public int getColumnCount() {
			return columnNames.length;
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			return data.get(rowIndex).getTableData(columnIndex);
		}

		@Override
		public Class getColumnClass(int c) {
			return getValueAt(0, c).getClass();
		}

		@Override
		public boolean isCellEditable(int row, int col) {
			return false;
		}

		public void addRow(Page p) {
			data.add(p);
			this.fireTableDataChanged();
		}

		public void removeRow(int row) {
			if (row < 0) {
				return;
			}
			data.remove(row);
			fireTableDataChanged();
		}

		public Page getPageObjectAt(int row) {
			return data.get(row);
		}
	}
}
