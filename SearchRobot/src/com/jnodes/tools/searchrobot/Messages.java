package com.jnodes.tools.searchrobot;

import java.util.Locale;
import java.util.ResourceBundle;

import com.jnodes.util.NLS;

/**
 * 
 * 国际化的消息
 * 
 * @author 倪庆洋 <niqy@qq.com>
 *
 * @date  Apr 12, 2015 4:01:56 PM
 *
 */
public class Messages extends NLS {

	private static String baseName = "messages";

	static {
		initializeMessages(baseName, Messages.class);
	}

	public static String Search;
	public static String Start;
	public static String Stop;
	public static String Cancel;
	public static String Open_File;
	public static String Open_Directory;
	public static String Table_Index;
	public static String Table_File;

	public static String Cnd_ContentText;
	public static String Cnd_ContentText_Desc;
	public static String Cnd_FileName;
	public static String Cnd_FileName_Desc;
	public static String Cnd_Directory;
	public static String Cnd_Case;
	public static String Cnd_Regular;
	public static String Cnd_ChooseDir;
	public static String Cnd_Choose;

	public static String SF_FileSearch;
	public static String SF_ShearchResult;
	public static String SF_Setting;
	public static String SF_About;

	public static void main(String[] args) {
		ResourceBundle bundle = ResourceBundle.getBundle("messages");
		System.out.println(Locale.getDefault());
	}
}
