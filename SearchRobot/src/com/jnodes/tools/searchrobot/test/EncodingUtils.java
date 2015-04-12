package com.jnodes.tools.searchrobot.test;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;

public class EncodingUtils {
	private static final Map<String, String> ENTITIES = new HashMap(5);
	private static final Pattern ESCAPER;

	static {
		ENTITIES.put("lt", "<");
		ENTITIES.put("gt", ">");
		ENTITIES.put("amp", "&");
		ENTITIES.put("apos", "'");
		ENTITIES.put("quot", "\"");
		ESCAPER = Pattern.compile("&([^;]+);");
	}

	public static String unescape(String paramString) {
		StringBuffer localStringBuffer = new StringBuffer(paramString.length());
		Matcher localMatcher = ESCAPER.matcher(paramString);
		while (localMatcher.find()) {
			String str2 = localMatcher.group(1);
			String str1;
			if ((str2.length() > 1) && (str2.charAt(0) == '#')) {
				int i;
				if ((str2.length() > 2) && (str2.charAt(1) == 'x'))
					i = Integer.parseInt(str2.substring(2), 16);
				else
					i = Integer.parseInt(str2.substring(1), 10);
				str1 = Character.toString((char) i);
			} else {
				str1 = (String) ENTITIES.get(str2);
				if (str1 == null)
					str1 = "&" + str2 + ";";
			}
			localMatcher.appendReplacement(localStringBuffer, str1);
		}
		localMatcher.appendTail(localStringBuffer);
		return localStringBuffer.toString().replaceAll("\\s+", " ");
	}
	
	public static void main(String[] args) throws IOException {
		
		byte[] data = FileUtils.readFileToByteArray(new File("D:/path.txt"));
		
		System.out.println(new String(data));
		
		System.out.println(EncodingUtils.unescape(new String(data)));
		System.out.println(EncodingUtils.unescape("\u502A\u5E86\u6D0B"));
	}
}
