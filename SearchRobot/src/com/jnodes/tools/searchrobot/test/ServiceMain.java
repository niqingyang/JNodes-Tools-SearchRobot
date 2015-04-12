package com.jnodes.tools.searchrobot.test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.FileFileFilter;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;

import com.jnodes.tools.searchrobot.util.ZipUtils;

public class ServiceMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		File directory = new File("C:/Program Files (x86)/Gephi-0.8.2/gephi/modules/");
		
		Iterator<File> ite = FileUtils.iterateFiles(directory, FileFilterUtils.fileFileFilter(), FileFilterUtils.directoryFileFilter());
	
		while(ite.hasNext()){
			File file = ite.next();
			
			if(file.isDirectory()){
				continue;
			}
			
			String extension = FilenameUtils.getExtension(file.getName());
			
			if("jar".equals(extension.toLowerCase())){
				ZipFile zipFile = null;
				
				try {
					
					zipFile = new ZipFile(file);
					
					List<ZipEntry> list = ZipUtils.iterateEntrys(zipFile);
					
					for (ZipEntry entry : list) {
						String name = entry.getName();
						
						if(name.startsWith("META-INF/services/") && !name.equals("META-INF/services/")){
							
							String fileName = name.substring(name.lastIndexOf("/") + 1);
							
							InputStream input = zipFile.getInputStream(entry);
							byte[] data = IOUtils.toByteArray(input);
							FileUtils.writeByteArrayToFile(new File("D:/services/", fileName), data);
							
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if(zipFile != null){
						try {
							zipFile.close();
						} catch (IOException e) {
						}
					}
				}
				
				
			}
			
		}
	}

	
	
	
}
