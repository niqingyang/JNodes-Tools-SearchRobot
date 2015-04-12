package com.jnodes.tools.searchrobot.test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;

import com.jnodes.tools.searchrobot.util.ZipUtils;

/**
 * 从Gephi Tookit中解析出对应的gephi中的代码
 * 
 * @author 倪庆洋 <niqy@qq.com>
 * 
 * @date 2015-2-5 下午09:34:03
 * 
 */
public class CopySourceFromTookitMain {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		
		String toJavaFilePrePath = "D:/gephi/java/";
		String toResoucesFilePrePath = "D:/gephi/resources/";
		
		String fromJavaFilePrePath = "D:/gephi/java/";
		String fromResoucesFilePrePath = "D:/gephi/resources/";
		
		File tookitJar = new File("D:/01 project/01 基础框架/06 我的系统/05 Gephi/IGephi/lib/gephi-toolkit.jar");
		
		ZipFile zipFile = new ZipFile(tookitJar);
		
		List<String> list = new ArrayList<String>();
		
		List<ZipEntry> entrys = ZipUtils.iterateEntrys(zipFile);
		
		for (ZipEntry entry : entrys) {
			
			String name = entry.getName();
			
			if(name.indexOf("org/gephi") != -1){
				System.out.println(name);
				
				if(name.endsWith(".class")){
					name = StringUtils.replace(name, ".class", ".java");
					if(name.indexOf("$") != -1){
						name = name.substring(0, name.indexOf("$"));
						name = name + ".java";
					}
				}else{
					
				}
				list.add(name);
			}
			
		}
		
		zipFile.close();
		
		FileUtils.writeByteArrayToFile(new File("D:/gephi-tookit.list"), StringUtils.join(list.toArray(), "\n").getBytes());
		
	}

}
