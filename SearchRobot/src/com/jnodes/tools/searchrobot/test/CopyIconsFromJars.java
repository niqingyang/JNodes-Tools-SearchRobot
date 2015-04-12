package com.jnodes.tools.searchrobot.test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;

import com.jnodes.tools.searchrobot.util.ZipUtils;

public class CopyIconsFromJars {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		File outputDir = new File("D:/icons/");
		File targetDir = new File("C:/Program Files/XMind/plugins/");
		
		Iterator<File> ite = FileUtils.iterateFiles(targetDir, new String[]{"jar"}, true);
		
		while(ite.hasNext()){
			File jarFile = ite.next();
			
			ZipFile zipFile = new ZipFile(jarFile);
			
			List<String> list = new ArrayList<String>();
			
			List<ZipEntry> entrys = ZipUtils.iterateEntrys(zipFile);
			
			for (ZipEntry entry : entrys) {
				
				String name = entry.getName();
				
				if(name.startsWith("icons") && (name.endsWith(".gif") || name.endsWith(".png"))){
					
					File jarDir = new File(outputDir, jarFile.getName());
					
					if(!jarDir.exists()){
						jarDir.mkdirs();
					}
					
					System.out.println(name);
					
					name = StringUtils.replace(name, "\\", "/");
					
					name = name.substring(name.lastIndexOf("/"));
					
					File imageFile = new File(jarDir, name);
					
					if(imageFile.exists()){
						String extension = name.substring(name.lastIndexOf("."));
						name = name.substring(0, name.lastIndexOf(".")) + "_1" + extension;
						imageFile = new File(jarDir, name);
					}
					
					InputStream input = zipFile.getInputStream(entry);
					byte[] data = IOUtils.toByteArray(input);
					FileUtils.writeByteArrayToFile(imageFile, data);
				}
				
			}
			
			zipFile.close();
		}
			
		
		
	}

}
