package javax.swing;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * 
 * 带键盘事件的渲染器
 * 
 * @author 倪庆洋 <niqy@qq.com>
 * 
 * @date 2015-1-17 下午05:34:02
 * 
 */
public class LabelCellRenderer extends JLabel implements ListCellRenderer, KeyListener {

	private static final long serialVersionUID = 1L;
	private JComboBox comboBox;

	private int selectedIndex = -1;

	/**
	 * 构造函数
	 * 
	 * @param comboBox
	 *            当前渲染器所属的JComboBox
	 */
	public LabelCellRenderer(final JComboBox comboBox) {
		setOpaque(true);
		this.comboBox = comboBox;
		this.comboBox.getEditor().getEditorComponent().addKeyListener(this);
	}

	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {

		this.setText(value.toString());

		this.setFont(this.comboBox.getFont());

		Color background;
		Color foreground;

		if (isSelected) {
			background = Color.decode("#3399FE");
			foreground = Color.WHITE;
			this.selectedIndex = index;
		} else {
			background = Color.WHITE;
			foreground = Color.BLACK;
		}

		setBackground(background);
		setForeground(foreground);

		return this;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();

		if (keyCode == 38) {// 按向上的方向键则索引减一
			int index = this.comboBox.getSelectedIndex();
			if (index == 0) {
				return;
			}
			index--;
			this.comboBox.setSelectedIndex(index);
		} else if (keyCode == 40) {// 按向下的方向键则索引加一
			int index = this.comboBox.getSelectedIndex();
			if (index == this.comboBox.getItemCount() - 1) {
				return;
			}
			index++;
			this.comboBox.setSelectedIndex(index);
		}else if(keyCode == 127){
			// 为防止误删除，只有在下来选项可视的情况下才可以执行删除操作
			if (this.comboBox.isPopupVisible()) {

				// 下来选框可见的情况下，设置Delete对文本框内文字操作无效
				JTextField textField = ((JTextField) this.comboBox.getEditor().getEditorComponent());
				String text = textField.getText();
				int position = ((JTextField) this.comboBox.getEditor().getEditorComponent()).getCaretPosition();
				
				// 如果是按Delete键则移除当前选中的Item
				if (this.selectedIndex >= 0 && this.selectedIndex < this.comboBox.getItemCount()) {
					this.comboBox.removeItemAt(this.selectedIndex);
					textField.setText(text);
					textField.setCaretPosition(position);
				}
				//此处代码在JDK1.6起作用，在JDK1.7失效，请注意
				e.setKeyCode(-1);
			}
		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

}
