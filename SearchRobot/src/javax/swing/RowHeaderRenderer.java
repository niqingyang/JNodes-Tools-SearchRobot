package javax.swing;

import java.awt.Color;
import java.awt.Component;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;

public class RowHeaderRenderer extends JLabel implements TableCellRenderer, ListSelectionListener {
	JTable reftable;// 需要添加rowHeader的JTable
	JTable tableShow;// 用于显示rowHeader的JTable

	public RowHeaderRenderer(JTable reftable, JTable tableShow) {
		this.reftable = reftable;
		this.tableShow = tableShow;
		// 增加监听器，实现当在reftable中选择行时，RowHeader会发生颜色变化
		ListSelectionModel listModel = reftable.getSelectionModel();
		listModel.addListSelectionListener(this);
	}

	public Component getTableCellRendererComponent(JTable table, Object obj, boolean isSelected, boolean hasFocus, int row, int col) {
		((RowHeaderTableModel) table.getModel()).setRowCount(reftable.getRowCount());
		JTableHeader header = reftable.getTableHeader();
		this.setOpaque(true);
		setBorder(UIManager.getBorder("TableHeader.cellBorder"));// 设置为TableHeader的边框类型
		setHorizontalAlignment(CENTER);// 让text居中显示
		setBackground(header.getBackground());// 设置背景色为TableHeader的背景色
		if (isSelect(row)) // 当选取单元格时,在row header上设置成选取颜色
		{
			setForeground(Color.white);
			setBackground(Color.lightGray);
		} else {
			setForeground(header.getForeground());
		}
		setFont(header.getFont());
		setText(String.valueOf(row + 1));
		return this;
	}

	public void valueChanged(ListSelectionEvent e) {
		this.tableShow.repaint();
	}

	private boolean isSelect(int row) {
		int[] sel = reftable.getSelectedRows();
		for (int i = 0; i < sel.length; i++)
			if (sel[i] == row)
				return true;
		return false;
	}
}
