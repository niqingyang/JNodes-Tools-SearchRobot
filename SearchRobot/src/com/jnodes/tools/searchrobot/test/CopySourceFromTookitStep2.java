package com.jnodes.tools.searchrobot.test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;

public class CopySourceFromTookitStep2 {

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

		String javaOutput = "D:/gephi/java/";
		String otherOutput = "D:/gephi/resources/";

		List<String> lines = FileUtils.readLines(new File("D:/gephi-tookit.list"));

		File scannerDir = new File("D:/01 project/01 基础框架/06 我的系统/05 Gephi/gephi-parent/modules/");

		File[] dirs = scannerDir.listFiles();

		for (File dir : dirs) {
			for (String line : lines) {

				File file = null;
				
				if(line.endsWith(".java")){
					file = new File(dir, "/src/main/java/" + line);
				}else{
					file = new File(dir, "/src/main/resources/" + line);
				}

				if (file.exists() && file.isFile()) {
					file.getParentFile().mkdirs();
					File destFile = null;
					if (line.endsWith(".java")) {
						destFile = new File(javaOutput, line);
					} else {
						destFile = new File(otherOutput, line);
					}
					FileUtils.copyFile(file, destFile);
				}

			}
		}

	}

}
