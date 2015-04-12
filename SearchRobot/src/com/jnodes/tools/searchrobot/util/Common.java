package com.jnodes.tools.searchrobot.util;

import java.io.File;
import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.apache.tools.zip.ZipEntry;

public class Common {
	
	private static final String TEMP_DIR = System.getProperty("java.io.tmpdir") + File.separator + "SearchRobot"+ File.separator;
	
	/**
	 * 打开文件
	 * 
	 * @param file
	 * @param properties
	 * @throws IOException
	 */
	public static void openFile(File file) {

		String program = SystemConfig.getProperty("file.open.program");

		if (StringUtils.isBlank(program)) {
			program = "C:/Program Files/Notepad++/notepad++.exe";
			try {
				String[] cmd = new String[] { program.trim(), file.getAbsolutePath() };
				Runtime.getRuntime().exec(cmd);
				SystemConfig.put("file.open.program", program);
			} catch (Exception e) {
				program = "notepad";
			}
		}
		String[] cmd = new String[] { program.trim(), file.getAbsolutePath() };
		try {
			Runtime.getRuntime().exec(cmd);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	/**
	 * 打开目录
	 * 
	 * @param file
	 */
	public static void openDirectory(File file) {
		try {
			java.awt.Desktop.getDesktop().open(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static File getTempClassFile(File jarFile, ZipEntry entry){
		
		File dir = new File(TEMP_DIR, jarFile.getName());
		
		if(entry.isDirectory()){
			dir = new File(dir, entry.getName());
		}else{
			String entryName = entry.getName();
			entryName = entryName.substring(0, entryName.lastIndexOf("/"));
			dir = new File(dir, entryName);
		}
		
		if(!dir.exists()){
			dir.mkdirs();
		}
		
		String entryName = entry.getName();
		
		String className = entryName.substring(entryName.lastIndexOf("/"), entryName.lastIndexOf("."));
		
		File classFile = new File(dir, className + ".java");
		
		return classFile;
		
	}
	
	public static void main(String[] args) {
		
	}
}
