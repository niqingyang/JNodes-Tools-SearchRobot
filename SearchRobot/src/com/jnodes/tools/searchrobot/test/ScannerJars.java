package com.jnodes.tools.searchrobot.test;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import org.apache.commons.io.FileUtils;

public class ScannerJars {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		File targetDir = new File("D:/jars/");
		File outputDir = new File("D:/jars_/");
		
		Collection<File> collection = FileUtils.listFiles(targetDir, new String[]{"jar"}, false);
		
		for (File file : collection) {
			String name = file.getName();
			
			if(name.indexOf(".nl_") == -1){
				FileUtils.copyFile(file, new File(outputDir, name));
			}
		}
		
	}

}
