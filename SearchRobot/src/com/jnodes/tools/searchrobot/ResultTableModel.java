package com.jnodes.tools.searchrobot;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;

public class ResultTableModel extends AbstractTableModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String[] columnNames = new String[] { Messages.Table_Index, Messages.Table_File };

	//根据行索引存放文件列表
	private List<File> fileList;
	
	private Vector<Vector<Object>> content;

	public ResultTableModel(){
		this.content = new Vector<Vector<Object>>();
		this.fileList = new ArrayList<File>();
	}
	
	@Override
	public Class<?> getColumnClass(int c) {
		if (c == 0) {
			return Integer.class;
		}
		return getValueAt(0, c).getClass();
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public int getRowCount() {
		return this.content.size();
	}

	public File getFileAt(int rowIndex){
		if(rowIndex < this.getRowCount()){
			try {
				return this.fileList.get(rowIndex);
			} catch (Exception e) {
				return null;
			}
		}
		return null;
	}
	
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		if(rowIndex < this.getRowCount() && columnIndex < this.getColumnCount()){
			try {
				return this.content.get(rowIndex).get(columnIndex);
			} catch (Exception e) {
				return null;
			}
			
		}
		return null;
	}

	@Override
	public void setValueAt(Object value, int rowIndex, int columnIndex) {
		if (columnIndex == 0) {
			this.content.get(rowIndex).setElementAt((Boolean) value, columnIndex);
			this.fireTableCellUpdated(rowIndex, columnIndex);
		}
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

	@Override
	public String getColumnName(int index) {
		return columnNames[index];
	}

//	public void addRow(File file) {
//		Vector<Object> column = new Vector<Object>();
//		column.add(this.content.size() + 1);
//		column.add(file.getAbsolutePath());
//		this.content.add(column);
//	}
//	
//	public void addRow(String filePath) {
//		Vector<Object> column = new Vector<Object>();
//		column.add(this.content.size() + 1);
//		column.add(filePath);
//		this.content.add(column);
//	}
	
	public void addRow(File file, String labelName) {
		Vector<Object> column = new Vector<Object>();
		column.add(this.content.size() + 1);
		column.add(labelName);
		this.content.add(column);
		this.fileList.add(file);
	}

	public void removeRow(int row) {
		this.content.remove(row);
	}

	public void removeRows(int row, int count) {
		for (int i = 0; i < count; i++) {
			if (this.content.size() > row) {
				this.content.remove(row);
			}
		}
	}
	
	public void removeAll(){
		this.content.removeAll(this.content);
	}

}
