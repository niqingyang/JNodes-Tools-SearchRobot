package com.jnodes.tools.searchrobot.test;

import java.io.File;
import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.FileFileFilter;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.lang.StringUtils;

public class ScannerDirectorys {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		File targetDir = new File("D:/01 project/01 基础框架/06 我的系统/08 Eclipse RCP/02 源码/org.gephi-0.8.2/src/main/java");
		
		Collection<File> collection = FileUtils.listFilesAndDirs(targetDir, FileFileFilter.FILE, DirectoryFileFilter.DIRECTORY);
		
		for (File file : collection) {
			if(file.isDirectory()){
				String name = file.getAbsolutePath();
				
				if(name.equals(targetDir.getAbsolutePath())){
					continue;
				}
				
				Collection<File> javaFiles = FileUtils.listFiles(file, new String[]{"java"}, false);
				
				if(javaFiles.size() == 0){
					continue;
				}
				
				name = name.substring(targetDir.getAbsolutePath().length() + 1);
				
				name = StringUtils.replace(name, "\\", ".");
				
				System.out.println(" " + name + ",");
			}
		}
		
	}
	
	

}
