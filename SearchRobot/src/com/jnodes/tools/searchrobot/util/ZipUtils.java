package com.jnodes.tools.searchrobot.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;

public class ZipUtils {

	static {
		// 将系统的ZIP编码格式设置为系统文件名编码方式,否则解压时报异常.
		// 重点，否则代码解压有问题，中文乱码
		System.setProperty("sun.zip.encoding", System.getProperty("sun.jnu.encoding"));
	}

	public static interface EntryRunnable {
		public void run(String fileName, InputStream input);
	}

	/**
	 * 解压
	 * 
	 * @param file
	 * @param runnable
	 * @throws IOException
	 */
	public static void iterate(File file, EntryRunnable runnable) throws IOException {
		ZipFile zipFile = new ZipFile(file);
		Enumeration<ZipEntry> entries = zipFile.getEntries();
		while (entries.hasMoreElements()) {
			ZipEntry entry = (ZipEntry) entries.nextElement();
			String fileName = entry.getName();

			if (!entry.isDirectory()) {
				InputStream input = zipFile.getInputStream(entry);
				runnable.run(fileName, input);
			}

		}
		zipFile.close();
	}

	public static List<ZipEntry> iterateEntrys(ZipFile zipFile) throws IOException {
		List<ZipEntry> list = new ArrayList<ZipEntry>();
		Enumeration<ZipEntry> entries = zipFile.getEntries();
		while (entries.hasMoreElements()) {
			ZipEntry entry = (ZipEntry) entries.nextElement();
			list.add(entry);
		}
		return list;
	}

	public static byte[] readFile(File file, String entryName) {
		ZipFile zipFile = null;
		try {
			zipFile = new ZipFile(file);
			Enumeration<ZipEntry> entries = zipFile.getEntries();
			while (entries.hasMoreElements()) {
				ZipEntry entry = (ZipEntry) entries.nextElement();
				String fileName = entry.getName();
				if (!entry.isDirectory()) {
					if (fileName.equals(entryName)) {
						InputStream input = zipFile.getInputStream(entry);
						return IOUtils.toByteArray(input);
					}
				}
			}
		} catch (Exception e) {
		} finally {
			if (zipFile != null) {
				try {
					zipFile.close();
				} catch (IOException e) {
				}
			}
		}
		return null;
	}

	public static ZipEntry getZipEntry(ZipFile zipFile, String entryName) {
		try {
			Enumeration<ZipEntry> entries = zipFile.getEntries();
			while (entries.hasMoreElements()) {
				ZipEntry entry = (ZipEntry) entries.nextElement();
				String fileName = entry.getName();
				if (!entry.isDirectory()) {
					if (fileName.equals(entryName)) {
						return entry;
					}
				}
			}
		} catch (Exception e) {
		}
		return null;
	}

	/**
	 * 解压缩
	 * 
	 * @param srcFile
	 * @param destFile
	 * @throws IOException 
	 */
	@SuppressWarnings("all")
	public static void decompress(File srcFile, File dstFile) throws IOException {

		// 将系统的ZIP编码格式设置为系统文件名编码方式,否则解压时报异常.
		// 重点，否则代码解压有问题，中文乱码
		System.setProperty("sun.zip.encoding", System.getProperty("sun.jnu.encoding"));

		ZipFile zipFile = null;

		try {
			
			zipFile = new ZipFile(srcFile);
			
			Enumeration<ZipEntry> entries = zipFile.getEntries();
			while (entries.hasMoreElements()) {
				ZipEntry entry = (ZipEntry) entries.nextElement();

				File entryFile = new File(dstFile, entry.getName());
				if (entry.isDirectory()) {
					entryFile.mkdirs();
				} else {
					// 如果指定文件的目录不存在,则创建之.
					File parent = entryFile.getParentFile();
					if (!parent.exists()) {
						parent.mkdirs();
					}
					InputStream input = zipFile.getInputStream(entry);
					FileOutputStream output = new FileOutputStream(entryFile);
					IOUtils.copy(input, output);
					IOUtils.closeQuietly(input);
					IOUtils.closeQuietly(output);
				}
			}
		} catch (IOException e) {
			throw e;
		} finally {
			if (zipFile != null) {
				try {
					zipFile.close();
				} catch (Exception e2) {
				}
			}
		}

	}

}
