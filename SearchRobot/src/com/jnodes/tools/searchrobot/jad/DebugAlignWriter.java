package com.jnodes.tools.searchrobot.jad;

import java.io.IOException;
import java.io.Writer;

public class DebugAlignWriter extends Writer {

	private int curLine;
	private StringBuffer lineContent;
	private Writer out;

	public DebugAlignWriter(Writer out) {
		super(out);
		curLine = 0;
		lineContent = new StringBuffer();
		this.out = out;
	}

	public void close() throws IOException {
		if (lineContent.length() != 0){
			out.write(lineContent.toString());
		}
		out.flush();
		out.close();
	}

	public void flush() throws IOException {
		out.flush();
	}

	public void write(char cbuf[], int off, int len) throws IOException {
		for (int i = off; i < off + len; i++)
			switch (cbuf[i]) {
			case 10: // '\n'
				String aLine = cleanComment(lineContent.toString());
				lineContent.setLength(0);
				int align;
				if (aLine.length() != 0)
					if ((align = getAlignTarget(aLine)) != -1) {
						if (align < curLine) {
							if (curLine != 0)
								out.write(10);
							out.write("/* <-MISALIGNED-> */ ");
							out.write(aLine);
							curLine++;
						} else if (align == curLine) {
							out.write(aLine);
						} else {
							for (; align > curLine; curLine++)
								out.write(10);

							out.write(aLine);
						}
					} else {
						if (curLine != 0)
							out.write(10);
						curLine++;
						out.write(aLine);
					}
				break;

			case 11: // '\013'
			case 12: // '\f'
			default:
				lineContent.append(cbuf[i]);
				break;

			case 13: // '\r'
				break;
			}

	}

	int getAlignTarget(String line) {
		int end;
		if (!line.startsWith("/*"))
			return -1;
		end = line.indexOf("*/", 2);
		if (end == -1)
			return -1;
		return Integer.parseInt(line.substring(2, end).trim());
	}

	String cleanComment(String line) {
		int comment = line.indexOf("//");
		if (comment == -1)
			return line;
		if (comment == 0 || line.trim().startsWith("//"))
			return "";
		else
			return line.substring(0, comment);
	}
}
