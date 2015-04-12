package com.jnodes.tools.searchrobot;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.apache.commons.lang.StringUtils;

import com.jnodes.tools.searchrobot.BackgroundImagePanel.Repeat;
import com.jnodes.tools.searchrobot.util.Constants;
import com.jnodes.tools.searchrobot.util.Images;

/**
 * 
 * 搜索
 * 
 * @author 倪庆洋 <niqy@qq.com>
 *
 * @date  Apr 12, 2015 11:46:19 AM
 *
 */
public class SearchFrame extends javax.swing.JFrame implements ActionListener, MouseListener {

	private static final long serialVersionUID = -9136658266346373462L;

	private static final String VERSION = "V1.3";

	private ConditionPanel cndPanel;
	private ResultPanel resultPanel;
	private SearchRunnable runnable;
	private BackgroundImagePanel jpanel;
	private JPopupMenu menu;

	/** Creates new form SearchFrame */
	public SearchFrame() {
		initComponents();

		//JOptionPane.showMessageDialog(this, this.rootDir, "警告", JOptionPane.WARNING_MESSAGE);

		this.cndPanel = new ConditionPanel();
		this.resultPanel = new ResultPanel();

		ImageIcon cndIcon = new ImageIcon(Images.getImageCondition());
		ImageIcon resultIcon = new ImageIcon(Images.getImageResult());

		this.tabMain.addTab(Messages.SF_FileSearch, cndIcon, this.cndPanel);
		this.tabMain.addTab(Messages.SF_ShearchResult, resultIcon, this.resultPanel);

		this.jpanel = new BackgroundImagePanel();

		this.jpProgress.getParent().remove(this.jpProgress);
		ImageIcon bgImage = new ImageIcon(Images.getImageLine());
		this.jpanel.setBackgroundImage(bgImage);
		this.jpanel.setBackgroundRepeat(Repeat.Repeat_X);
		this.jpanel.setBackgroundPositionY(-2);
		this.addBackgroundImagePanel(this.jpanel);
		this.lblProgress.getParent().remove(this.lblProgress);
		this.jpanel.add(this.lblProgress);

		//		this.setProgress(0, 100);

		this.tabMain.addMouseListener(this);
		this.addMouseListener(this);

		this.initMenu();

	}

	@Override
	public void update(Graphics graphics) {
		this.paint(graphics);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();

		if (Constants.AC_Setting.equals(command)) {

		} else if (Constants.AC_About.equals(command)) {

			StringBuilder message = new StringBuilder();
			message.append("   SearchRobot");
			message.append("\n");
			message.append("   该搜索工具提供类MyEclipse搜索插件的功能");
			message.append("\n");
			message.append("   可在指定目录内检索包含指定内容的文件，");
			message.append("\n");
			message.append("   支持检索.jar、.zip内的文件,");
			message.append("\n");
			message.append("   并对搜索条件有记忆功能。");
			message.append("\n");
			message.append("   Version: ").append(VERSION);
			message.append("\n");
			message.append("   Author: ").append("niqingyang");
			message.append("\n");
			message.append("   Email: ").append("niqy@qq.com");
			message.append("\n");
			message.append("\n");

			ImageIcon icon = new ImageIcon(Images.getImageApp());
			JOptionPane.showMessageDialog(this, message, "About SearchRobot", JOptionPane.INFORMATION_MESSAGE, icon);
		}
	}

	private void initMenu() {
		this.menu = new JPopupMenu();
		ImageIcon icon1 = new ImageIcon(Images.getImageSetting());
		ImageIcon icon2 = new ImageIcon(Images.getImageAbout());
		JMenuItem item1 = new JMenuItem(Messages.SF_Setting, icon1);
		item1.setActionCommand(Constants.AC_Setting);
		JMenuItem item2 = new JMenuItem(Messages.SF_About, icon2);
		item2.setActionCommand(Constants.AC_About);
		JMenuItem item3 = new JMenuItem("setting", icon1);

		//		JMenu jMenu = new JMenu("123", false);
		//		jMenu.add(item3);
		//		jMenu.setBounds(0, 0, 100, 30);
		//		this.menu.add(jMenu);

		this.menu.add(item1);
		this.menu.add(item2);

		item1.addActionListener(this);
		item2.addActionListener(this);

	}

	//GEN-BEGIN:initComponents
	// <editor-fold defaultstate="collapsed" desc="Generated Code">
	private void initComponents() {

		jPanel1 = new javax.swing.JPanel();
		jPanel2 = new javax.swing.JPanel();
		jScrollPane1 = new javax.swing.JScrollPane();
		lblStatus = new javax.swing.JLabel();
		btnSearch = new javax.swing.JButton();
		btnCancel = new javax.swing.JButton();
		jpProgress = new javax.swing.JPanel();
		lblProgress = new javax.swing.JLabel();
		jPanel3 = new javax.swing.JPanel();
		tabMain = new javax.swing.JTabbedPane();

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

		jScrollPane1.setBorder(null);
		jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		jScrollPane1.setViewportView(lblStatus);

		btnSearch.setFont(new java.awt.Font("微软雅黑", 0, 14));
		btnSearch.setText(Messages.Search);
		btnSearch.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				btnSearchMouseClicked(evt);
			}
		});

		btnCancel.setFont(new java.awt.Font("微软雅黑", 0, 14));
		btnCancel.setText(Messages.Cancel);
		btnCancel.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				btnCancelMouseClicked(evt);
			}
		});

		lblProgress.setText("0%");
		lblProgress.setOpaque(true);

		javax.swing.GroupLayout jpProgressLayout = new javax.swing.GroupLayout(jpProgress);
		jpProgress.setLayout(jpProgressLayout);
		jpProgressLayout.setHorizontalGroup(jpProgressLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jpProgressLayout.createSequentialGroup().addComponent(lblProgress).addContainerGap(691, Short.MAX_VALUE)));
		jpProgressLayout.setVerticalGroup(jpProgressLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jpProgressLayout.createSequentialGroup().addComponent(lblProgress).addContainerGap(9, Short.MAX_VALUE)));

		javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
		jPanel2.setLayout(jPanel2Layout);
		jPanel2Layout.setHorizontalGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING,
				jPanel2Layout.createSequentialGroup().addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup().addGap(12, 12, 12).addComponent(jpProgress, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).addGroup(jPanel2Layout.createSequentialGroup().addContainerGap().addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 538, Short.MAX_VALUE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(btnSearch).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(btnCancel))).addContainerGap()));
		jPanel2Layout.setVerticalGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup().addComponent(jpProgress, javax.swing.GroupLayout.DEFAULT_SIZE, 29, Short.MAX_VALUE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(btnCancel).addComponent(btnSearch)).addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)).addContainerGap()));

		tabMain.setFont(new java.awt.Font("微软雅黑", 0, 14));

		javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
		jPanel3.setLayout(jPanel3Layout);
		jPanel3Layout.setHorizontalGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel3Layout.createSequentialGroup().addContainerGap().addComponent(tabMain, javax.swing.GroupLayout.DEFAULT_SIZE, 710, Short.MAX_VALUE).addContainerGap()));
		jPanel3Layout.setVerticalGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel3Layout.createSequentialGroup().addContainerGap().addComponent(tabMain, javax.swing.GroupLayout.DEFAULT_SIZE, 272, Short.MAX_VALUE)));

		javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
		jPanel1.setLayout(jPanel1Layout);
		jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
		jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup().addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)));

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
		layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));

		pack();
	}// </editor-fold>
	//GEN-END:initComponents

	private void addBackgroundImagePanel(BackgroundImagePanel jpProgress) {
		javax.swing.GroupLayout jpProgressLayout = new javax.swing.GroupLayout(jpProgress);
		jpProgress.setLayout(jpProgressLayout);
		jpProgressLayout.setHorizontalGroup(jpProgressLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jpProgressLayout.createSequentialGroup().addComponent(lblProgress).addContainerGap(688, Short.MAX_VALUE)));
		jpProgressLayout.setVerticalGroup(jpProgressLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jpProgressLayout.createSequentialGroup().addComponent(lblProgress).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

		javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
		jPanel2.setLayout(jPanel2Layout);
		jPanel2Layout.setHorizontalGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				jPanel2Layout.createSequentialGroup().addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup().addContainerGap().addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 538, Short.MAX_VALUE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(btnSearch).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(btnCancel)).addGroup(jPanel2Layout.createSequentialGroup().addGap(12, 12, 12).addComponent(jpProgress, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))).addContainerGap()));
		jPanel2Layout.setVerticalGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING,
				jPanel2Layout.createSequentialGroup().addComponent(jpProgress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(btnCancel).addComponent(btnSearch)).addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)).addContainerGap()));

	}

	/**
	 * 取消按钮的事件
	 */
	private void btnCancelMouseClicked(java.awt.event.MouseEvent evt) {
		/**
		 * 当程序正在搜索时则停止搜索，当程序未搜索时则关闭程序
		 */
		if (!this.btnSearch.isEnabled() && this.runnable != null && this.runnable.isRunning()) {
			this.runnable.stop();
		} else {
			System.exit(1);
		}

	}

	public void startSearch() {
		//		File file = new File(this.rootDir, "wait.gif");
		//		ImageIcon icon;
		//		try {
		//			icon = new ImageIcon(FileUtils.readFileToByteArray(file));
		//			this.tabMain.setIconAt(1, icon);
		//		} catch (IOException e) {
		//			e.printStackTrace();
		//		}
		this.btnCancel.setText(Messages.Stop);
		this.btnSearch.setEnabled(false);
	}

	public void endSearch() {
		//		File file = new File(this.rootDir, "flatLayout.gif");
		//		ImageIcon icon;
		//		try {
		//			icon = new ImageIcon(FileUtils.readFileToByteArray(file));
		//			this.tabMain.setIconAt(1, icon);
		//		} catch (IOException e) {
		//			e.printStackTrace();
		//		}
		this.btnCancel.setText(Messages.Cancel);
		this.btnSearch.setEnabled(true);
	}

	/**
	 * 设置状态
	 * @param text
	 */
	public void setStatus(String text) {
		this.lblStatus.setText(text);
	}

	/**
	 * 保存搜索参数数据
	 */
	public void saveData() {
		this.cndPanel.saveData();
	}

	/**
	 * 设置进度
	 * @param number
	 */
	public void setProgress(double index, double count) {
		if (index != 0) {
			this.lblProgress.setVisible(true);
		}
		int width = this.jpanel.getWidth() - this.lblProgress.getWidth();
		int number = (int) (index / count * 100);
		this.lblProgress.setText(number + "%");
		Point point = this.lblProgress.getLocation();
		double x = width * (index / count);
		//		double y = this.jpanel.getLocation().getY();
		double y = point.getY();
		point.setLocation(x, y);
		this.lblProgress.setLocation(point);
		//		if (number == 100 || index == 0) {
		//			this.lblProgress.setVisible(false);
		//		}
		//		System.out.println(x + "-" + width);
	}

	/**
	 * 点击搜索按钮
	 */
	private void btnSearchMouseClicked(java.awt.event.MouseEvent evt) {

		final Pattern contentPattern = this.cndPanel.getContentPattern();

		if (contentPattern == null) {
			JOptionPane.showMessageDialog(this, "搜索条件不能为空！", "警告", JOptionPane.WARNING_MESSAGE);
			return;
		}

		final Pattern fileNamePattern = this.cndPanel.getFileNamePattern();

		if (fileNamePattern == null) {
			JOptionPane.showMessageDialog(this, "文件名过滤器不能为空！", "警告", JOptionPane.WARNING_MESSAGE);
			return;
		}

		String searchDir = this.cndPanel.getDirectory();

		if (StringUtils.isBlank(searchDir)) {
			JOptionPane.showMessageDialog(this, "搜索目录不能为空！", "警告", JOptionPane.WARNING_MESSAGE);
			return;
		}

		//保存搜索参数
		this.saveData();

		this.tabMain.setSelectedIndex(1);

		File searchDirFile = new File(searchDir);

		JTable jTable = this.resultPanel.getJTable();

		this.runnable = new SearchRunnable(searchDirFile, this, jTable);
		this.runnable.setContentPattern(contentPattern);
		this.runnable.setFileNamePattern(fileNamePattern);

		Thread thread = new Thread(this.runnable);
		thread.start();

	}

	/**
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String args[]) {
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				} catch (Exception e) {
					e.printStackTrace();
				}

				SearchFrame frame = new SearchFrame();
				try {
					frame.setIconImage(ImageIO.read(new ByteArrayInputStream(Images.getImageApp())));
				} catch (Exception e) {
					e.printStackTrace();
				}
				frame.setTitle("文件搜索 " + VERSION);
				frame.setVisible(true);
				frame.setLocationRelativeTo(null);
				frame.setSize(756, 435);
				frame.addWindowListener(new WindowAdapter() {
					@Override
					public void windowClosing(WindowEvent e) {
						System.exit(1);
					}
				});
			}
		});
	}

	//GEN-BEGIN:variables
	// Variables declaration - do not modify
	private javax.swing.JButton btnCancel;
	private javax.swing.JButton btnSearch;
	private javax.swing.JPanel jPanel1;
	private javax.swing.JPanel jPanel2;
	private javax.swing.JPanel jPanel3;
	private javax.swing.JScrollPane jScrollPane1;
	private javax.swing.JPanel jpProgress;
	private javax.swing.JLabel lblProgress;
	private javax.swing.JLabel lblStatus;
	private javax.swing.JTabbedPane tabMain;

	// End of variables declaration//GEN-END:variables

	@Override
	public void mouseClicked(MouseEvent event) {
		boolean isRight = SwingUtilities.isRightMouseButton(event);
		if (isRight) {
			this.menu.show(this, event.getX(), event.getY());
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

	@Override
	public void mousePressed(MouseEvent e) {

	}

	@Override
	public void mouseReleased(MouseEvent e) {

	}

}