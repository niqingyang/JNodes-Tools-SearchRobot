package com.jnodes.tools.searchrobot.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

public class SystemConfig {

	private static Properties properties;
	private static final String SEARCH_ROBOT_FILENAME = "search.data";

	public static final String RootDir = System.getProperty("user.dir");

	static {
		reload();
	}

	public static String getProperty(String key) {
		return properties.getProperty(key);
	}
	
	public static void put(String key, Object value){
		properties.put(key, value);
	}
	
	public static Object remove(String key){
		return properties.remove(key);
	}

	public static void reload() {
		
		properties = new Properties();
		
		try {
			File file = new File(RootDir, SEARCH_ROBOT_FILENAME);
			byte[] data = FileUtils.readFileToByteArray(file);
			String text = EncodingUtil.toString(data);

			if (text == null) {
				return;
			}

			String[] lines = text.split("\n");

			for (String line : lines) {
				if (line.startsWith("#")) {
					continue;
				}
				if (line.indexOf("=") == -1) {
					continue;
				}
				String key = line.substring(0, line.indexOf("="));
				String value = line.substring(line.indexOf("=") + 1);
				properties.setProperty(key, value);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void save() {

		try {
			StringBuilder text = new StringBuilder();

			Set<String> keySet = properties.stringPropertyNames();

			for (String key : keySet) {

				String value = properties.get(key).toString();

				value = StringUtils.replace(value, "\\", "/");
				value = StringUtils.replace(value, "//", "/");

				text.append(key);
				text.append("=");
				text.append(value);
				text.append("\n");
			}

			File file = new File(RootDir, SEARCH_ROBOT_FILENAME);
			FileUtils.writeByteArrayToFile(file, text.toString().getBytes("UTF-8"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Set<String> getKeySet() {
		return properties.stringPropertyNames();
	}

}
