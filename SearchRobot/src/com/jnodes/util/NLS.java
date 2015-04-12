package com.jnodes.util;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

public abstract class NLS {

	private static final String EXTENSION = ".properties";
	private static String[] nlSuffixes;

	static final int SEVERITY_ERROR = 0x04;
	static final int SEVERITY_WARNING = 0x02;

	static final Object ASSIGNED = new Object();

	public static void initializeMessages(final String baseName, final Class<?> clazz) {
		if (System.getSecurityManager() == null) {
			load(baseName, clazz);
			return;
		}
		AccessController.doPrivileged(new PrivilegedAction<Object>() {
			public Object run() {
				load(baseName, clazz);
				return null;
			}
		});
	}

	private static void load(final String bundleName, Class<?> clazz) {
		final Field[] fields = clazz.getDeclaredFields();
		ClassLoader loader = clazz.getClassLoader();

		boolean isAccessible = (clazz.getModifiers() & Modifier.PUBLIC) != 0;

		final int len = fields.length;
		Map<Object, Object> fieldMap = new HashMap<Object, Object>(len * 2);
		for (int i = 0; i < len; i++) {
			fieldMap.put(fields[i].getName(), fields[i]);
		}

		final String[] variants = buildVariants(bundleName);

		for (int i = 0; i < variants.length; i++) {
			final InputStream input = loader == null ? ClassLoader.getSystemResourceAsStream(variants[i]) : loader.getResourceAsStream(variants[i]);
			if (input == null)
				continue;
			try {
				final MessagesProperties properties = new MessagesProperties(fieldMap, bundleName, isAccessible);
				properties.load(input);
			} catch (IOException e) {
				log(SEVERITY_ERROR, "Error loading " + variants[i], e);
			} finally {
				if (input != null)
					try {
						input.close();
					} catch (IOException e) {
						// 忽略
					}
			}
		}
		computeMissingMessages(bundleName, clazz, fieldMap, fields, isAccessible);
	}

	private static void computeMissingMessages(String bundleName, Class<?> clazz, Map<Object, Object> fieldMap, Field[] fieldArray, boolean isAccessible) {
		final int MOD_EXPECTED = Modifier.PUBLIC | Modifier.STATIC;
		final int MOD_MASK = MOD_EXPECTED | Modifier.FINAL;
		final int numFields = fieldArray.length;
		for (int i = 0; i < numFields; i++) {
			Field field = fieldArray[i];
			if ((field.getModifiers() & MOD_MASK) != MOD_EXPECTED) {
				continue;
			}
			// if the field has a a value assigned, there is nothing to do
			if (fieldMap.get(field.getName()) == ASSIGNED) {
				continue;
			}
			try {
				String value = "NLS missing message: " + field.getName() + " in: " + bundleName; //$NON-NLS-1$ //$NON-NLS-2$
				log(SEVERITY_WARNING, value, null);
				if (!isAccessible) {
					field.setAccessible(true);
				}
				field.set(null, value);
			} catch (Exception e) {
				log(SEVERITY_ERROR, "Error setting the missing message value for: " + field.getName(), e); //$NON-NLS-1$
			}
		}
	}

	private static String[] buildVariants(String root) {
		if (nlSuffixes == null) {
			String nl = Locale.getDefault().toString();
			List<String> result = new ArrayList<String>(4);
			int lastSeparator;
			while (true) {
				result.add('_' + nl + EXTENSION);
				lastSeparator = nl.lastIndexOf('_');
				if (lastSeparator == -1) {
					break;
				}
				nl = nl.substring(0, lastSeparator);
			}
			result.add(EXTENSION);
			nlSuffixes = result.toArray(new String[result.size()]);
		}
		root = root.replace('.', '/');
		String[] variants = new String[nlSuffixes.length];
		for (int i = 0; i < variants.length; i++) {
			variants[i] = root + nlSuffixes[i];
		}
		return variants;
	}

	static void log(int severity, String message, Exception e) {
		String statusMsg;
		switch (severity) {
		case SEVERITY_ERROR:
			statusMsg = "Error: ";
			break;
		case SEVERITY_WARNING:
			// intentionally fall through:
		default:
			statusMsg = "Warning: ";
		}
		if (message != null) {
			statusMsg += message;
		}
		if (e != null) {
			statusMsg += ": " + e.getMessage();
		}
		System.err.println(statusMsg);
		if (e != null) {
			e.printStackTrace();
		}
	}

	private static class MessagesProperties extends Properties {

		private static final int MOD_EXPECTED = Modifier.PUBLIC | Modifier.STATIC;
		private static final int MOD_MASK = MOD_EXPECTED | Modifier.FINAL;
		private static final long serialVersionUID = 1L;

		private final String bundleName;
		private final Map<Object, Object> fields;
		private final boolean isAccessible;

		public MessagesProperties(Map<Object, Object> fieldMap, String bundleName, boolean isAccessible) {
			super();
			this.fields = fieldMap;
			this.bundleName = bundleName;
			this.isAccessible = isAccessible;
		}

		public synchronized Object put(Object key, Object value) {
			Object fieldObject = fields.put(key, ASSIGNED);
			// if already assigned, there is nothing to do
			if (fieldObject == ASSIGNED)
				return null;
			if (fieldObject == null) {
				final String msg = "NLS unused message: " + key + " in: " + bundleName;//$NON-NLS-1$ //$NON-NLS-2$
				log(SEVERITY_WARNING, msg, null);
				return null;
			}
			final Field field = (Field) fieldObject;
			if ((field.getModifiers() & MOD_MASK) != MOD_EXPECTED){
				return null;
			}
			try {
				if (!isAccessible){
					field.setAccessible(true);
				}
				field.set(null, new String(((String) value).toCharArray()));
			} catch (Exception e) {
				log(SEVERITY_ERROR, "Exception setting field value.", e); //$NON-NLS-1$
			}
			return null;
		}
	}
}
