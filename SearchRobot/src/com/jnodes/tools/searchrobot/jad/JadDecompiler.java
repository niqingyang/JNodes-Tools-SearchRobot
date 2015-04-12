package com.jnodes.tools.searchrobot.jad;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JadDecompiler {

	private static final Pattern packagePattern = Pattern.compile("package ([a-zA-Z_0-9]++)([.][a-zA-Z_0-9]++)++;", Pattern.DOTALL);

	public static final String OPTION_ANNOTATE = "-a";
	public static final String OPTION_ANNOTATE_FQ = "-af";
	public static final String OPTION_BRACES = "-b";
	public static final String OPTION_CLEAR = "-clear";
	public static final String OPTION_DIR = "-d";
	public static final String OPTION_DEAD = "-dead";
	public static final String OPTION_DISASSEMBLER = "-dis";
	public static final String OPTION_FULLNAMES = "-f";
	public static final String OPTION_FIELDSFIRST = "-ff";
	public static final String OPTION_DEFINITS = "-i";
	public static final String OPTION_SPLITSTR_MAX = "-l";
	public static final String OPTION_LNC = "-lnc";
	public static final String OPTION_LRADIX = "-lradix";
	public static final String OPTION_SPLITSTR_NL = "-nl";
	public static final String OPTION_NOCONV = "-noconv";
	public static final String OPTION_NOCAST = "-nocast";
	public static final String OPTION_NOCLASS = "-noclass";
	public static final String OPTION_NOCODE = "-nocode";
	public static final String OPTION_NOCTOR = "-noctor";
	public static final String OPTION_NODOS = "-nodos";
	public static final String OPTION_NOFLDIS = "-nofd";
	public static final String OPTION_NOINNER = "-noinner";
	public static final String OPTION_NOLVT = "-nolvt";
	public static final String OPTION_NONLB = "-nonlb";
	public static final String OPTION_OVERWRITE = "-o";
	public static final String OPTION_SENDSTDOUT = "-p";
	public static final String OPTION_PA = "-pa";
	public static final String OPTION_PC = "-pc";
	public static final String OPTION_PE = "-pe";
	public static final String OPTION_PF = "-pf";
	public static final String OPTION_PI = "-pi";
	public static final String OPTION_PL = "-pl";
	public static final String OPTION_PM = "-pm";
	public static final String OPTION_PP = "-pp";
	public static final String OPTION_PV = "-pv";
	public static final String OPTION_RESTORE = "-r";
	public static final String OPTION_IRADIX = "-radix";
	public static final String OPTION_EXT = "-s";
	public static final String OPTION_SAFE = "-safe";
	public static final String OPTION_SPACE = "-space";
	public static final String OPTION_STAT = "-stat";
	public static final String OPTION_INDENT_SPACE = "-t";
	public static final String OPTION_INDENT_TAB = "-t";
	public static final String OPTION_VERBOSE = "-v";
	public static final String OPTION_ANSI = "-8";
	public static final String OPTION_REDSTDERR = "-&";
	public static final String USE_TAB = "use tab";
	public static final String TOGGLE_OPTION[] = { "-a", "-af", "-b", "-clear", "-dead", "-dis", "-f", "-ff", "-i", "-lnc", "-nl", "-noconv", "-nocast", "-noclass", "-nocode", "-noctor", "-nodos", "-nofd", "-noinner", "-nolvt", "-nonlb", "-safe", "-space", "-stat", "-t", "-v", "-8" };
	public static final String VALUE_OPTION_STRING[] = { "-pa", "-pc", "-pe", "-pf", "-pl", "-pm", "-pp" };
	public static final String VALUE_OPTION_INT[] = { "-l", "-lradix", "-pi", "-pv", "-radix" };

	/**
	 * 反编译
	 */
	public static SourceCode decompile(File classFile) {

		SourceCode sourceCode = new SourceCode();

		String className = classFile.getName();

		if (className.indexOf(".class") != -1) {
			className = className.substring(0, className.lastIndexOf("."));
		}

		List<Exception> expList = sourceCode.getExceptions();

		sourceCode.setClassName(className);

		StringWriter output;
		StringWriter errors;

		output = new StringWriter();

		boolean inc = true;
		boolean align = true;
		Writer decor = ((Writer) (!inc || !align ? ((Writer) (output)) : ((Writer) (new DebugAlignWriter(output)))));

		errors = new StringWriter();
		PrintWriter errorsP = new PrintWriter(errors);
		int status = 0;
		long time = System.currentTimeMillis();
		String code = null;
		StringBuffer log = new StringBuffer();
		try {
			errorsP.println("Jad reported messages/errors:");

			Process p = Runtime.getRuntime().exec(buildCmdLine(classFile.getName()), new String[0], classFile.getParentFile());
			StreamRedirectThread outRedirect = new StreamRedirectThread("output_reader", p.getInputStream(), decor);
			StreamRedirectThread errRedirect = new StreamRedirectThread("error_reader", p.getErrorStream(), errors);
			outRedirect.start();
			errRedirect.start();
			status = p.waitFor();
			outRedirect.join();
			errRedirect.join();
			if (outRedirect.getException() != null) {
				expList.add(outRedirect.getException());
			}
			if (errRedirect.getException() != null) {
				expList.add(errRedirect.getException());
			}
		} catch (Exception e) {
			expList.add(e);
		} finally {
			try {
				decor.flush();
				decor.close();
				errorsP.println("Exit status: " + status);
				errors.flush();
				errorsP.close();
			} catch (Exception e) {
				expList.add(e);
			}
			time = System.currentTimeMillis() - time;
		}

		code = output.toString();
		log = errors.getBuffer();

		sourceCode.setCode(code);
		sourceCode.setLog(log.toString());
		sourceCode.setTime(time);

		if (code != null) {
			Matcher matcher = packagePattern.matcher(code);

			while (matcher.find()) {

				String packageName = matcher.group();

				packageName = packageName.substring("package".length() + 1, packageName.length() - 1);

				sourceCode.setPackageName(packageName);

				break;
			}
		}

		return sourceCode;
	}

	/**
	 * 反编译输入目录下的所有.class文件到输出目录下
	 * 
	 * @param inputDir
	 * @param outputDir
	 */
	public static SourceCode decompile(File inputDir, File outputDir) {

		SourceCode sourceCode = new SourceCode();

		List<Exception> expList = sourceCode.getExceptions();

		StringWriter output;
		StringWriter errors;

		output = new StringWriter();

		boolean inc = true;
		boolean align = true;
		Writer decor = ((Writer) (!inc || !align ? ((Writer) (output)) : ((Writer) (new DebugAlignWriter(output)))));

		errors = new StringWriter();
		PrintWriter errorsP = new PrintWriter(errors);
		int status = 0;
		long time = System.currentTimeMillis();
		String code = null;
		StringBuffer log = new StringBuffer();
		try {
			errorsP.println("Jad reported messages/errors:");

			Process p = Runtime.getRuntime().exec(buildCmdLine(inputDir.getAbsolutePath(), outputDir.getAbsolutePath()), new String[0], inputDir.getParentFile());
			StreamRedirectThread outRedirect = new StreamRedirectThread("output_reader", p.getInputStream(), decor);
			StreamRedirectThread errRedirect = new StreamRedirectThread("error_reader", p.getErrorStream(), errors);
			outRedirect.start();
			errRedirect.start();
			status = p.waitFor();
			outRedirect.join();
			errRedirect.join();
			if (outRedirect.getException() != null) {
				expList.add(outRedirect.getException());
			}
			if (errRedirect.getException() != null) {
				expList.add(errRedirect.getException());
			}
		} catch (Exception e) {
			e.printStackTrace();
			expList.add(e);
		} finally {
			try {
				decor.flush();
				decor.close();
				errorsP.println("Exit status: " + status);
				errors.flush();
				errorsP.close();
			} catch (Exception e) {
				expList.add(e);
			}
			time = System.currentTimeMillis() - time;
		}

		code = output.toString();
		log = errors.getBuffer();

		sourceCode.setCode(code);
		sourceCode.setLog(log.toString());
		sourceCode.setTime(time);

		if (code != null) {
			Matcher matcher = packagePattern.matcher(code);

			while (matcher.find()) {

				String packageName = matcher.group();

				packageName = packageName.substring("package".length() + 1, packageName.length() - 1);

				sourceCode.setPackageName(packageName);

				break;
			}
		}

		return sourceCode;

	}

	/**
	 * @param classFileName
	 * @return
	 */
	private static String[] buildCmdLine(String input, String output) {

		List<String> cmdLine = new ArrayList<String>();

		cmdLine.add(Config.getString(Config.JAD_CMD_KEY));
		cmdLine.add("-o");
		cmdLine.add("-r");
		cmdLine.add("-s");
		cmdLine.add("java");
		cmdLine.add("-d");
		cmdLine.add(input);
		cmdLine.add(output + "/**/*.class");

		return (String[]) cmdLine.toArray(new String[cmdLine.size()]);
	}

	/**
	 * @param classFileName
	 * @return
	 */
	private static String[] buildCmdLine(String classFileName) {

		List<String> cmdLine = new ArrayList<String>();

		cmdLine.add(Config.getString(Config.JAD_CMD_KEY));
		cmdLine.add("-p");
		String indent = Config.getString("-t");
		if (indent.equals("use tab"))
			cmdLine.add("-t");
		else
			try {
				Integer.parseInt(indent);
				cmdLine.add("-t" + indent);
			} catch (Exception e) {
			}
		for (int i = 0; i < TOGGLE_OPTION.length; i++)

			if (Config.getBoolean(TOGGLE_OPTION[i])) {
				cmdLine.add(TOGGLE_OPTION[i]);
			}

		for (int i = 0; i < VALUE_OPTION_INT.length; i++) {
			int iValue = Config.getInt(VALUE_OPTION_INT[i]);
			if (iValue > 0)
				cmdLine.add(VALUE_OPTION_INT[i] + iValue);
		}

		for (int i = 0; i < VALUE_OPTION_STRING.length; i++) {
			String sValue = Config.getString(VALUE_OPTION_STRING[i]);
			if (sValue != null && sValue.length() > 0)
				cmdLine.add(VALUE_OPTION_STRING[i] + " " + sValue);
		}

		cmdLine.add(classFileName);

		return (String[]) cmdLine.toArray(new String[cmdLine.size()]);
	}

	void debugCmdLine(List<String> segments) {
		StringBuffer cmdline = new StringBuffer();
		for (int i = 0; i < segments.size(); i++) {
			cmdline.append(segments.get(i)).append(" ");
		}
	}

	void deltree(File root) {
		if (root.isFile()) {
			root.delete();
			return;
		}
		File children[] = root.listFiles();
		for (int i = 0; i < children.length; i++)
			deltree(children[i]);

		root.delete();
	}

}
