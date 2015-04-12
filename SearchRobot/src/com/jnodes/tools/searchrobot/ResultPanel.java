/*
 * ResultPanel.java
 *
 * Created on __DATE__, __TIME__
 */

package com.jnodes.tools.searchrobot;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;

import com.jnodes.tools.searchrobot.util.Common;
import com.jnodes.tools.searchrobot.util.Images;
import com.jnodes.tools.searchrobot.jad.JadDecompiler;
import com.jnodes.tools.searchrobot.jad.SourceCode;

/**
 * 
 * 展示结果的面板组件
 * 
 * @author 倪庆洋 <niqy@qq.com>
 * 
 * @date Apr 12, 2015 2:52:52 PM
 * 
 */
public class ResultPanel extends javax.swing.JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;

	private ResultTableModel model;
	private JPopupMenu menu;

	/** Creates new form ResultPanel */
	public ResultPanel() {

		initComponents();

		this.model = new ResultTableModel();
		this.tableResult.setModel(this.model);
		this.tableResult.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
		this.tableResult.getColumnModel().getColumn(0).setPreferredWidth(55);
		this.tableResult.getColumnModel().getColumn(0).setMaxWidth(55);
		this.tableResult.getColumnModel().getColumn(0).setMinWidth(55);
		this.tableResult.getColumnModel().getColumn(1).setPreferredWidth(656);

		DefaultTableCellRenderer render = new DefaultTableCellRenderer();
		render.setHorizontalAlignment(SwingConstants.CENTER);
		this.tableResult.getColumnModel().getColumn(0).setCellRenderer(render);
		render = new DefaultTableCellRenderer();
		render.setHorizontalAlignment(SwingConstants.LEFT);
		this.tableResult.getColumnModel().getColumn(1).setCellRenderer(render);

		this.initMenu();

		// /* 将table加入JScrollPane */
		// JScrollPane scrollPane = new JScrollPane(this.tableResult);
		// /* 将rowHeaderTable作为row header加入JScrollPane的RowHeaderView区域 */
		// scrollPane.setRowHeaderView(new JRowHeaderTable(this.tableResult,
		// 34));
		// this.jScrollPane1.setViewportView(scrollPane);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		int rowIndex = this.tableResult.getSelectedRow();
		// String value = this.tableResult.getModel().getValueAt(rowIndex,
		// 1).toString();
		// if (value.indexOf("$") != -1) {
		// value = value.substring(0, value.indexOf("$"));
		// }
		// File file = new File(value);

		ResultTableModel tableModel = (ResultTableModel) this.tableResult.getModel();

		File file = tableModel.getFileAt(rowIndex);

		if (file == null) {
			return;
		}

		String command = e.getActionCommand();
		if (Messages.Open_File.equals(command)) {

			if (file.getName().endsWith(".class")) {
				SourceCode sourceCode = JadDecompiler.decompile(file);

				String fileName = FilenameUtils.getBaseName(file.getName());

				if (StringUtils.isNotBlank(sourceCode.getCode())) {
					file = new File(file.getParentFile(), fileName + ".java");
					try {
						FileUtils.writeByteArrayToFile(file, sourceCode.getCode().getBytes());
					} catch (IOException ex) {
						ex.printStackTrace();
					}
				}

			}

			Common.openFile(file);

			// //检查是否为压缩文件
			// String extension = FilenameUtils.getExtension(file.getName());
			// if ("jar".equals(extension.toLowerCase()) ||
			// "zip".equals(extension.toLowerCase())) {
			// String entryName =
			// this.tableResult.getModel().getValueAt(rowIndex, 1).toString();
			// entryName = entryName.substring(entryName.indexOf("$") + 1);
			//
			// if(entryName.endsWith(".class")){
			//
			// ZipFile zipFile = null;
			//
			// try {
			// zipFile = new ZipFile(file);
			//
			// ZipEntry entry = ZipUtils.getZipEntry(zipFile, entryName);
			//
			// File classFile = Common.getTempClassFile(file, entry);
			//
			// byte[] data = ZipUtils.readFile(file, entryName);
			// FileUtils.writeByteArrayToFile(classFile, data);
			//
			// SourceCode sourceCode = JadDecompiler.decompile(classFile);
			//
			// if(sourceCode != null){
			// FileUtils.writeByteArrayToFile(classFile,
			// sourceCode.getCode().getBytes());
			// }
			//
			// Common.openFile(classFile);
			//
			// } catch (Exception e2) {
			// } finally {
			// if(zipFile != null){
			// try {
			// zipFile.close();
			// } catch (IOException e1) {
			// }
			// }
			// }
			//
			// }else{
			// byte[] data = ZipUtils.readFile(file, entryName);
			// try {
			// entryName = StringUtils.replace(entryName, "\\", ".");
			// entryName = StringUtils.replace(entryName, "/", ".");
			// File tempFile = File.createTempFile(entryName, "tmp");
			// FileUtils.writeByteArrayToFile(tempFile, data);
			//
			// Common.openFile(tempFile);
			// } catch (IOException e1) {
			// //发生错误则打开压缩文件所在目录
			// file = file.getParentFile();
			// Common.openDirectory(file);
			// }
			// }
			//
			// } else {
			// Common.openFile(file);
			// }
		} else {
			if (file.isFile()) {
				file = file.getParentFile();
			}
			Common.openDirectory(file);
		}
	}

	private void initMenu() {
		this.menu = new JPopupMenu();
		ImageIcon icon1 = new ImageIcon(Images.getImageFile());
		ImageIcon icon2 = new ImageIcon(Images.getImageDirectory());
		JMenuItem item1 = new JMenuItem(Messages.Open_File, icon1);
		JMenuItem item2 = new JMenuItem(Messages.Open_Directory, icon2);

		this.menu.add(item1);
		this.menu.add(item2);

		item1.addActionListener(this);
		item2.addActionListener(this);

		this.add(this.menu);

	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	// GEN-BEGIN:initComponents
	// <editor-fold defaultstate="collapsed" desc="Generated Code">
	private void initComponents() {

		jScrollPane1 = new javax.swing.JScrollPane();
		tableResult = new javax.swing.JTable();

		tableResult.setModel(new javax.swing.table.DefaultTableModel(new Object[][] {

		}, new String[] { Messages.Table_Index, Messages.Table_File }) {
			private static final long serialVersionUID = 1L;
			Class[] types = new Class[] { java.lang.String.class, java.lang.String.class };

			public Class getColumnClass(int columnIndex) {
				return types[columnIndex];
			}
		});
		tableResult.setCellSelectionEnabled(true);
		tableResult.setRowHeight(25);
		tableResult.setSelectionBackground(new java.awt.Color(204, 255, 204));
		tableResult.setSelectionForeground(new java.awt.Color(0, 0, 0));
		tableResult.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mousePressed(java.awt.event.MouseEvent evt) {
				tableResultMousePressed(evt);
			}

			public void mouseReleased(java.awt.event.MouseEvent evt) {
				tableResultMouseReleased(evt);
			}
		});
		tableResult.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
			public void mouseMoved(java.awt.event.MouseEvent evt) {
				tableResultMouseMoved(evt);
			}
		});
		jScrollPane1.setViewportView(tableResult);

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
		this.setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 694, Short.MAX_VALUE));
		layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE));
	}// </editor-fold>
		// GEN-END:initComponents

	private void tableResultMouseReleased(java.awt.event.MouseEvent event) {
		boolean isRight = SwingUtilities.isRightMouseButton(event);
		if (isRight) {
			int row = this.tableResult.rowAtPoint(event.getPoint());
			if (row > -1) {
				// this.tableResult.setRowSelectionInterval(row, row);
				this.tableResult.setRowSelectionInterval(row, row);
			}
			this.menu.show(this.tableResult, event.getX(), event.getY());
		}

	}

	private void tableResultMousePressed(java.awt.event.MouseEvent event) {

	}

	/**
	 * 鼠标悬浮提示
	 */
	private void tableResultMouseMoved(java.awt.event.MouseEvent e) {
		int row = this.tableResult.rowAtPoint(e.getPoint());
		int col = this.tableResult.columnAtPoint(e.getPoint());
		if (row > -1 && col > -1) {
			Object value = this.tableResult.getValueAt(row, 1);
			if (value != null && !"".equals(value)) {
				this.tableResult.setToolTipText(value.toString());// 悬浮显示单元格内容
			} else {
				this.tableResult.setToolTipText(null);// 关闭提示
			}
		}
	}

	/**
	 * 获取表格对象
	 */
	public JTable getJTable() {
		return this.tableResult;
	}

	// GEN-BEGIN:variables
	// Variables declaration - do not modify
	private javax.swing.JScrollPane jScrollPane1;
	private javax.swing.JTable tableResult;
	// End of variables declaration//GEN-END:variables

}