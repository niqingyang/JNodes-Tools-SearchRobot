package com.jnodes.tools.searchrobot.util;

import java.io.File;

import javax.swing.filechooser.FileFilter;

/**
 * 扩展名过滤器
 * 
 * @author 倪庆洋 <niqy@qq.com>
 * 
 * @date 2015-1-13 下午11:54:21
 * 
 */
public class ExtensionFilter extends FileFilter {

	private String extensions[];

	private String description;

	public ExtensionFilter(String description, String extension) {
		this(description, new String[] { extension });
	}

	public ExtensionFilter(String description, String extensions[]) {
		this.description = description;
		this.extensions = (String[]) extensions.clone();
	}

	public boolean accept(File file) {
		if (file.isDirectory()) {
			return true;
		}
		int count = extensions.length;
		String path = file.getAbsolutePath();
		for (int i = 0; i < count; i++) {
			String ext = extensions[i];
			if (path.endsWith(ext) && (path.charAt(path.length() - ext.length()) == '.')) {
				return true;
			}
		}
		return false;
	}

	public String getDescription() {
		return (description == null ? extensions[0] : description);
	}
}
