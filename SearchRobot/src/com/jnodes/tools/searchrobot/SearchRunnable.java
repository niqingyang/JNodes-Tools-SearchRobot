package com.jnodes.tools.searchrobot;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import javax.swing.JTable;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.jnodes.tools.searchrobot.util.EncodingUtil;
import com.jnodes.tools.searchrobot.util.ZipUtils;
import com.jnodes.tools.searchrobot.jad.JadDecompiler;
import com.jnodes.tools.searchrobot.jad.SourceCode;

/**
 * 
 * 搜索线程的Runnable
 * 
 * @author 倪庆洋 <niqy@qq.com>
 *
 * @date  Apr 12, 2015 4:00:57 PM
 *
 */
public class SearchRunnable implements Runnable {

	private static Log log = LogFactory.getLog(SearchRunnable.class);

	private Pattern fileNamePattern;
	private Pattern contentPattern;

	private SearchFrame jFrame;
	private JTable jTable;
	private ResultTableModel tableModel;

	private File searchDirFile;

	private boolean running = false;

	public static final String TEMP_DIR = System.getProperty("java.io.tmpdir") + File.separator + "SearchRobot"+ File.separator;
	
	private int index = 0;
	private int count = 0;

	public SearchRunnable(File searchDirFile, SearchFrame jFrame, JTable jTable) {
		this.searchDirFile = searchDirFile;
		this.jFrame = jFrame;
		this.jTable = jTable;
		this.tableModel = (ResultTableModel) this.jTable.getModel();
	}

	/**
	 * 设置文件名Pattern
	 * 
	 * @param fileNamePattern
	 */
	public void setFileNamePattern(Pattern fileNamePattern) {
		this.fileNamePattern = fileNamePattern;
	}

	/**
	 * 设置内容Pattern
	 * 
	 * @param contentPattern
	 */
	public void setContentPattern(Pattern contentPattern) {
		this.contentPattern = contentPattern;
	}

	/**
	 * 是否正在运行
	 * 
	 * @return
	 */
	public boolean isRunning() {
		return running;
	}

	/**
	 * 停止
	 * 
	 * @param running
	 */
	public void stop() {
		this.running = false;
	}

//	@Override
//	public void run() {
//
//		long time = System.currentTimeMillis();
//
//		this.running = true;
//
//		this.jFrame.startSearch();
//
//		// 情况表
//		this.tableModel.removeAll();
//		this.jTable.revalidate();
//
//		this.jFrame.setStatus("正在扫描文件...");
//
//		List<File> files = this.listFiles(this.searchDirFile);
//
//		this.jFrame.setStatus("扫描文件完成，开始搜索...");
//
//		int index = 1;
//		int count = files.size();
//
//		String searchDir = this.searchDirFile.getAbsolutePath();
//
//		for (File file : files) {
//
//			// 判断是否停止
//			if (!this.running) {
//				break;
//			}
//
//			String fileName = file.getName();
//			boolean isMatch = fileNamePattern.matcher(fileName).matches();
//
//			String path = "." + file.getAbsolutePath().substring(searchDir.length());
//
//			this.jFrame.setStatus("[" + index + "/" + count + "]" + path);
//			// 设置进度
//			this.jFrame.setProgress(index, count);
//
//			index++;
//
//			if (!isMatch) {
//				continue;
//			}
//
//			String extension = FilenameUtils.getExtension(fileName);
//
//			// 如果是.jar、.zip文件则先解压
//			if ("jar".equals(extension.toLowerCase()) || "zip".equals(extension.toLowerCase())) {
//
//				// 判断是否停止
//				if (!this.running) {
//					break;
//				}
//
//				this.jFrame.setStatus("[" + index + "/" + count + "]" + "开始解析压缩文件：" + file.getAbsolutePath());
//
//				File dstFile = new File(TEMP_DIR, file.getName());
//
//				try {
//					ZipUtils.decompress(file, dstFile);
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//
//				List<File> subFiles = this.listFiles(dstFile);
//
//				ZipFile zipFile = null;
//
//				try {
//					zipFile = new ZipFile(file);
//					List<ZipEntry> zipEntrys = ZipUtils.iterateEntrys(zipFile);
//
//					// 重新计算总数量
//					count = count + zipEntrys.size();
//
//					for (ZipEntry zipEntry : zipEntrys) {
//
//						// 判断是否停止
//						if (!this.running) {
//							break;
//						}
//
//						// 获取Entry的名称，其实是文件的路径
//						String entryName = zipEntry.getName();
//						// //获取文件名
//						// fileName = FilenameUtils.getBaseName(entryName);
//
//						this.jFrame.setStatus("[" + index + "/" + count + "]" + path + "&" + entryName);
//
//						// 设置进度
//						this.jFrame.setProgress(index, count);
//
//						index++;
//
//						String filePath = file.getAbsolutePath() + "$" + entryName;
//
//						if (!zipEntry.isDirectory()) {
//
//							isMatch = fileNamePattern.matcher(entryName).matches();
//
//							if (!isMatch) {
//								continue;
//							}
//
//							InputStream input = zipFile.getInputStream(zipEntry);
//							byte[] data = IOUtils.toByteArray(input);
//
//							if (entryName.endsWith(".class")) {
//								File classFile = Common.getTempClassFile(file, zipEntry);
//
//								// if(classFile.exists()){
//								// String text =
//								// FileUtils.readFileToString(classFile)
//								// }
//								//								
//								FileUtils.writeByteArrayToFile(classFile, data);
//
//								SourceCode sourceCode = JadDecompiler.decompile(classFile);
//
//								// 为提高效率，此处不进行写入操作
//								if (sourceCode.getCode() != null) {
//									// FileUtils.writeByteArrayToFile(classFile,
//									// sourceCode.getCode().getBytes());
//									data = sourceCode.getCode().getBytes();
//								}
//
//							}
//
//							this.matchContent(filePath, data);
//
//						}
//					}
//				} catch (Exception e) {
//					log.error("[" + index + "/" + count + "]" + "解析文件[" + file.getAbsolutePath() + "]时发生错误！", e);
//				} finally {
//					if (zipFile != null) {
//						try {
//							zipFile.close();
//						} catch (IOException e) {
//						}
//					}
//				}
//			} else {
//				try {
//					byte[] data = FileUtils.readFileToByteArray(file);
//					this.matchContent(file.getAbsolutePath(), data);
//				} catch (Exception e) {
//					log.error("[" + index + "/" + count + "]" + "解析文件[" + file.getAbsolutePath() + "]时发生错误！", e);
//				}
//			}
//		}
//
//		time = System.currentTimeMillis() - time;
//
//		if (this.running) {
//			// 设置进度
//			this.jFrame.setProgress(index, count);
//			// 情况状态栏信息
//			this.jFrame.setStatus(String.format("搜索完成，总计搜索文件 %s 个，共搜索到 %s 个相关文件 ！（用时：%s）", count, this.tableModel.getRowCount(), this.formatTime(time)));
//		} else {
//			// 情况状态栏信息
//			this.jFrame.setStatus(String.format("搜索中断，总计已搜索文件 %s 个，共搜索到 %s 个相关文件 ！（用时：%s）", index, this.tableModel.getRowCount(), this.formatTime(time)));
//		}
//
//		this.jFrame.endSearch();
//	}

	@Override
	public void run() {
		long time = System.currentTimeMillis();

		this.running = true;

		//开始搜索
		this.jFrame.startSearch();

		// 情况表
		this.tableModel.removeAll();
		this.jTable.revalidate();

		//解析
		this.resolveDirectory(this.searchDirFile);
		
		//统计时间
		time = System.currentTimeMillis() - time;

		if (this.running) {
			// 设置进度
			this.jFrame.setProgress(index, count);
			// 情况状态栏信息
			this.jFrame.setStatus(String.format("搜索完成，总计搜索文件 %s 个，共搜索到 %s 个相关文件 ！（用时：%s）", count, this.tableModel.getRowCount(), this.formatTime(time)));
		} else {
			// 情况状态栏信息
			this.jFrame.setStatus(String.format("搜索中断，总计已搜索文件 %s 个，共搜索到 %s 个相关文件 ！（用时：%s）", index, this.tableModel.getRowCount(), this.formatTime(time)));
		}

		//结束搜索
		this.jFrame.endSearch();
		
	}
	

	private void resolveDirectory(File targetFile) {

		// 判断是否停止
		if (!this.running) {
			return;
		}

		if (targetFile.isDirectory()) {
			List<File> list = this.listFiles(targetFile);
			for (File file : list) {
				this.resolveDirectory(file);
			}
		} else {
			this.resolveFile(null, targetFile);
		}
	}
	
	private void resolveDirectory(File parentFile, File targetFile) {

		// 判断是否停止
		if (!this.running) {
			return;
		}

		if (targetFile.isDirectory()) {
			List<File> list = this.listFiles(targetFile);
			for (File file : list) {
				this.resolveDirectory(file);
			}
		} else {
			this.resolveFile(parentFile, targetFile);
		}
	}

	private void resolveFile(File parentFile, File file) {
		
		//索引自加
		this.index++;
		
		// 设置进度
		this.jFrame.setProgress(index, count);
		
		String fileName = file.getName();

		boolean isMatch = fileNamePattern.matcher(fileName).matches();

		if (!isMatch) {
			return;
		}

		this.jFrame.setStatus("[" + index + "/" + count + "]" + file.getAbsolutePath());
		
		byte[] data = null;

		if (fileName.endsWith(".jar") || fileName.endsWith(".zip")) {
			this.resolveJarFile(file);
		} else if (fileName.endsWith(".class")) {
			
			String baseFileName = FilenameUtils.getBaseName(fileName);
			File javaFile = new File(file.getParentFile(), baseFileName + ".java");
			//源代码存在则直接读取
			if(javaFile.exists()){
				try {
					data = FileUtils.readFileToByteArray(javaFile);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}else{
				//不存在则反编译
				SourceCode sourceCode = JadDecompiler.decompile(file);
				if (StringUtils.isNotBlank(sourceCode.getCode())) {
					data = sourceCode.getCode().getBytes();
				}
			}
			
		} else {
			try {
				data = FileUtils.readFileToByteArray(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		if (data != null) {
			
			String filePath = file.getAbsolutePath();
			
			if(parentFile != null && (parentFile.getName().endsWith(".jar") || parentFile.getName().endsWith(".zip"))){
				filePath = filePath.substring(TEMP_DIR.length());
				filePath = filePath.substring(parentFile.getName().length());
				filePath = parentFile.getAbsolutePath() + "$" + filePath;
			}else{
				filePath = file.getAbsolutePath();
			}
			
			this.matchContent(file, filePath, data);
			
		}
	}

	private void resolveJarFile(File jarFile) {

		File dstFile = new File(TEMP_DIR, jarFile.getName());

		try {
			ZipUtils.decompress(jarFile, dstFile);
			//解压后进行反编译
			JadDecompiler.decompile(dstFile, dstFile);
		} catch (Exception e) {
			e.printStackTrace();
		}

		List<File> files = this.listFiles(dstFile);

		for (File file : files) {
			this.resolveDirectory(jarFile, file);
		}

	}

	private List<File> listFiles(File dirFile) {
		
		this.jFrame.setStatus("正在扫描文件：" + dirFile.getAbsolutePath());
		
		Iterator<File> ite = FileUtils.iterateFiles(dirFile, FileFilterUtils.fileFileFilter(), FileFilterUtils.directoryFileFilter());

		List<File> files = new ArrayList<File>();

		while (ite.hasNext()) {
			files.add(ite.next());
		}
		
		//重新计算
		this.count = this.count + files.size();
		
		return files;
	}
	
	private void matchContent(File file, String labelName, byte[] data) {
		String text = EncodingUtil.toString(data);
		boolean isMatch = this.contentPattern.matcher(text).matches();
		if (isMatch) {
			this.tableModel.addRow(file, labelName);
			this.jTable.revalidate();
		}
	}

	/**
	 * 
	 * @param 要转换的毫秒数
	 * @return 该毫秒数转换为 * days * hours * minutes * seconds 后的格式
	 * @author fy.zhang
	 */
	private String formatTime(long mss) {
		long days = mss / (1000 * 60 * 60 * 24);
		long hours = (mss % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
		long minutes = (mss % (1000 * 60 * 60)) / (1000 * 60);
		long seconds = (mss % (1000 * 60)) / 1000;

		StringBuilder text = new StringBuilder();

		if (days > 0) {
			text.append(days).append("天");
		}
		if (hours > 0) {
			text.append(hours).append("时");
		}
		if (minutes > 0) {
			text.append(minutes).append("分");
		}
		if (seconds > 0) {
			text.append(seconds).append("秒 ");
		}

		if (StringUtils.isBlank(text.toString())) {
			text.append(mss).append("毫秒");
		}

		return text.toString();
	}

}
