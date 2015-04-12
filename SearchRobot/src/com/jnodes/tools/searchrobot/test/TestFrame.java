/*
 * TestFrame.java
 *
 * Created on __DATE__, __TIME__
 */

package com.jnodes.tools.searchrobot.test;

import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import org.apache.commons.io.FileUtils;

import com.jnodes.tools.searchrobot.BackgroundImagePanel;
import com.jnodes.tools.searchrobot.BackgroundImagePanel.Repeat;

/**
 *
 * @author  __USER__
 */
public class TestFrame extends javax.swing.JFrame {

	private BackgroundImagePanel bgPanel;

	/** Creates new form TestFrame */
	public TestFrame() {
		initComponents();

		try {
			String dir = Thread.currentThread().getContextClassLoader().getResource(".").toURI().getPath();
			this.bgPanel = new BackgroundImagePanel();
			File bgFile = new File(dir, "line.png");
			byte[] data = FileUtils.readFileToByteArray(bgFile);
			ImageIcon backgroundImage = new ImageIcon(data);
			this.bgPanel.setBackgroundImage(backgroundImage);
			this.bgPanel.setBackgroundRepeat(Repeat.Repeat);
			this.bgPanel.setBackgroundPosition(-7, 0);
			this.addJPanel(this.bgPanel);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
	//GEN-BEGIN:initComponents
	// <editor-fold defaultstate="collapsed" desc="Generated Code">
	private void initComponents() {

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 400, Short.MAX_VALUE));
		layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 300, Short.MAX_VALUE));

		pack();
	}// </editor-fold>
	//GEN-END:initComponents

	public void addJPanel(JPanel jPanel) {

		jPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

		javax.swing.GroupLayout jPanelLayout = new javax.swing.GroupLayout(jPanel);
		jPanel.setLayout(jPanelLayout);
		jPanelLayout.setHorizontalGroup(jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 398, Short.MAX_VALUE));
		jPanelLayout.setVerticalGroup(jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 140, Short.MAX_VALUE));

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
		layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGap(88, 88, 88).addComponent(jPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap(70, Short.MAX_VALUE)));

	}

	/**
	 * @param args the command line arguments
	 */
	public static void main(String args[]) {
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				new TestFrame().setVisible(true);
			}
		});
	}

	//GEN-BEGIN:variables
	// Variables declaration - do not modify
	// End of variables declaration//GEN-END:variables

}