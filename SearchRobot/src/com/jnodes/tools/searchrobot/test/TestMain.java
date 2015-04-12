package com.jnodes.tools.searchrobot.test;

import java.io.File;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.LabelCellRenderer;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

public class TestMain {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
//		String dir = Thread.currentThread().getContextClassLoader().getResource(".").toURI().getPath();
		String dir = "D:/";
		File file = new File(dir, "jad.exe");
		
		byte[] data = FileUtils.readFileToByteArray(file);
		
//		String text = new String(Base64.encodeBase64(data));

		String text = com.sun.org.apache.xml.internal.security.utils.Base64.encode(data);
		
		
//		FileUtils.writeByteArrayToFile(new File("D:/jad.data"), text.getBytes());
		
		StringBuilder lines = new StringBuilder();
		
		String[] arrays = text.split("\n");
		
		for (String line : arrays) {
			lines.append("\"").append(line).append("\" + \n");
		}
		
		System.out.println(lines.toString());
		
//		System.out.println(FilenameUtils.getBaseName("/project/api/WorkspaceListener.java"));
		
	}

}
