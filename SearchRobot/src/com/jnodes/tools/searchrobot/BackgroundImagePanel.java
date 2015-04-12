package com.jnodes.tools.searchrobot;

import java.awt.Graphics;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 * 绘制背景的JPanel
 * 
 * @author 倪庆洋 <niqy@qq.com>
 * 
 * @date Apr 12, 2015 4:05:10 PM
 * 
 */
public class BackgroundImagePanel extends JPanel {

	private static final long serialVersionUID = 1L;

	/**
	 * 平铺方式
	 */
	public static enum Repeat {
		// 平铺
		Repeat,
		// X轴平铺
		Repeat_X,
		// Y轴平铺
		Repeat_Y,
		// 不平铺
		NO
	}

	// 背景图片
	private ImageIcon backgroundImage;
	// 背景图片平铺方式
	private Repeat repeat = Repeat.Repeat;
	// 背景图片X轴平铺的起始位置
	private int position_x = 0;
	// 背景图片Y轴平铺的起始位置
	private int position_y = 0;

	/**
	 * 设置背景图片
	 * 
	 * @param backgroundImage
	 */
	public void setBackgroundImage(ImageIcon backgroundImage) {
		this.backgroundImage = backgroundImage;
	}

	/**
	 * 设置背景平铺方式
	 * 
	 * @param repeat
	 */
	public void setBackgroundRepeat(Repeat repeat) {
		this.repeat = repeat;
	}

	/**
	 * 设置背景平铺的位置
	 * 
	 * @param x
	 * @param y
	 */
	public void setBackgroundPosition(int x, int y) {
		this.position_x = x;
		this.position_y = x;
	}

	public void setBackgroundPositionX(int x) {
		this.position_x = x;
	}

	public void setBackgroundPositionY(int y) {
		this.position_y = y;
	}

	// 重写的绘图函数，绘制平铺图片
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		// 如果未设置背景图片则不进行绘制
		if (this.backgroundImage == null) {
			return;
		}

		// 每一副图像的位置坐标
		int x = this.position_x;
		int y = this.position_y;

		// 平铺背景图片
		while (true) {
			// 绘制图片
			g.drawImage(this.backgroundImage.getImage(), x, y, this);

			if (this.repeat == Repeat.NO) {
				break;
			}

			// 如果绘制完毕，退出循环
			if (x > this.getSize().width && y > this.getSize().height) {
				break;
			}

			if (this.repeat == Repeat.Repeat_X) {
				if (x <= this.getSize().width) {
					x += this.backgroundImage.getIconWidth();
				} else {
					break;
				}
			} else if (this.repeat == Repeat.Repeat_Y) {
				if (y <= this.getSize().height) {
					y += this.backgroundImage.getIconHeight();
				} else {
					break;
				}
			} else if (this.repeat == Repeat.Repeat) {
				// 如果绘完一行，换行绘制
				if (x > this.getSize().width) {
					x = this.position_x;
					y += this.backgroundImage.getIconHeight();
				} else {
					// 如果在当前行，得到下一个图片的坐标位置
					x += this.backgroundImage.getIconWidth();
				}
			}

		}
	}

}
