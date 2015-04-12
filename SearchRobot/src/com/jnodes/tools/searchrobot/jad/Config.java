package com.jnodes.tools.searchrobot.jad;

import java.io.File;
import java.util.Properties;

public class Config {

	private static Properties properties;
	
	public static final String JAD_CMD_KEY = "jad.cmd";
	
	static {
		reload();
	}

	public static void reload() {
		properties = new Properties();

		File jadFile = new File("C:/jad.exe");
		
		if(jadFile.exists()){
			properties.setProperty(JAD_CMD_KEY, jadFile.getAbsolutePath());
		}else{
			jadFile = new File(System.getProperty("user.dir"), "jad.exe");
			if(jadFile.exists()){
				properties.setProperty(JAD_CMD_KEY, jadFile.getAbsolutePath());
			}else{
				properties.setProperty(JAD_CMD_KEY, "jad");
			}
		}
		
		properties.setProperty("net.sf.jadclipse.tempd", System.getProperty("user.home") + File.separator);
		properties.setProperty("net.sf.jadclipse.reusebuff", "true");
		properties.setProperty("net.sf.jadclipse.alwaysuse", "false");
		properties.setProperty("net.sf.jadclipse.use_eclipse_formatter", "false");
		properties.setProperty("-t", "4");
		properties.setProperty("-radix", "10");
		properties.setProperty("-lradix", "10");
		properties.setProperty("-l", "0");
		properties.setProperty("-pi", "0");
		properties.setProperty("-pv", "0");

	}
	
	public static void set(String key, String value){
		properties.setProperty(key, value);
	}
	
	public static String getString(String key) {
		return properties.getProperty(key);
	}

	public static boolean getBoolean(String key) {
		return Boolean.valueOf(properties.getProperty(key));
	}

	public static Integer getInt(String key) {
		return Integer.valueOf(properties.getProperty(key));
	}
}
