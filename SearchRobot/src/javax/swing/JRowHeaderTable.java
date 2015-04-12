package javax.swing;

import java.awt.Dimension;

public class JRowHeaderTable extends JTable {
	
	private static final long serialVersionUID = 1L;
	
	private JTable refTable;// 需要添加rowHeader的JTable

	/**
	 * 为JTable添加RowHeader，
	 * 
	 * @param refTable
	 *            需要添加rowHeader的JTable
	 * @param columnWideth
	 *            rowHeader的宽度
	 */
	public JRowHeaderTable(JTable refTable, int columnWidth) {
		super(new RowHeaderTableModel(refTable.getRowCount()));
		this.refTable = refTable;
		this.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);// 不可以调整列宽
		this.getColumnModel().getColumn(0).setPreferredWidth(columnWidth);
		this.setDefaultRenderer(Object.class, new RowHeaderRenderer(refTable, this));// 设置渲染器
		this.setPreferredScrollableViewportSize(new Dimension(columnWidth, 0));
	}
}
