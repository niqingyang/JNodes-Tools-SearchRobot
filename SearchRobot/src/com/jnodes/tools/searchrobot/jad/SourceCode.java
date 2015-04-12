package com.jnodes.tools.searchrobot.jad;

import java.util.ArrayList;
import java.util.List;

/**
 * 源代码
 * 
 * @author Administrator
 * 
 */
public class SourceCode {

	// 包名
	private String packageName;
	// 类名
	private String className;
	// 反编译后的源代码
	private String code;
	// 编译过程中遇到的异常列表
	private List<Exception> exceptions;
	// 编译日志
	private String log;
	// 编译耗时
	private long time;

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public List<Exception> getExceptions() {
		if (this.exceptions == null) {
			this.exceptions = new ArrayList<Exception>();
		}
		return exceptions;
	}

	public void setExceptions(List<Exception> exceptions) {
		this.exceptions = exceptions;
	}

	public String getLog() {
		return log;
	}

	public void setLog(String log) {
		this.log = log;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	@Override
	public String toString() {
		return this.getCode();
	}
}
