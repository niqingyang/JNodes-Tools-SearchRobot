package com.jnodes.tools.searchrobot.util;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class PatternConstructor {
	private static class ReplaceStringConstructor {

		private static final int RC_MIXED = 0;
		private static final int RC_UPPER = 1;
		private static final int RC_LOWER = 2;
		private static final int RC_FIRSTUPPER = 3;
		private int fRetainCaseMode;
		private final String fLineDelim;

		private String interpretReplaceEscapes(String replaceText, String foundText) {
			int length = replaceText.length();
			boolean inEscape = false;
			StringBuffer buf = new StringBuffer(length);
			fRetainCaseMode = 0;
			for (int i = 0; i < length; i++) {
				char ch = replaceText.charAt(i);
				if (inEscape) {
					i = interpretReplaceEscape(ch, i, buf, replaceText, foundText);
					inEscape = false;
				} else if (ch == '\\')
					inEscape = true;
				else if (ch == '$') {
					buf.append(ch);
					if (i + 2 < length) {
						char ch1 = replaceText.charAt(i + 1);
						char ch2 = replaceText.charAt(i + 2);
						if (ch1 == '0' && '0' <= ch2 && ch2 <= '9') {
							buf.append("0\\");
							i++;
						}
					}
				} else {
					interpretRetainCase(buf, ch);
				}
			}

			if (inEscape)
				buf.append('\\');
			return buf.toString();
		}

		private int interpretReplaceEscape(char ch, int i, StringBuffer buf, String replaceText, String foundText) {
			int length = replaceText.length();
			switch (ch) {
			case 114: // 'r'
				buf.append('\r');
				break;

			case 110: // 'n'
				buf.append('\n');
				break;

			case 116: // 't'
				buf.append('\t');
				break;

			case 102: // 'f'
				buf.append('\f');
				break;

			case 97: // 'a'
				buf.append('\007');
				break;

			case 101: // 'e'
				buf.append('\033');
				break;

			case 82: // 'R'
				buf.append(fLineDelim);
				break;

			case 48: // '0'
				buf.append('$').append(ch);
				if (i + 1 >= length)
					break;
				char ch1 = replaceText.charAt(i + 1);
				if ('0' <= ch1 && ch1 <= '9')
					buf.append('\\');
				break;

			case 49: // '1'
			case 50: // '2'
			case 51: // '3'
			case 52: // '4'
			case 53: // '5'
			case 54: // '6'
			case 55: // '7'
			case 56: // '8'
			case 57: // '9'
				buf.append('$').append(ch);
				break;

			case 99: // 'c'
				if (i + 1 < length) {
					ch1 = replaceText.charAt(i + 1);
					interpretRetainCase(buf, (char) (ch1 ^ 0x40));
					i++;
				} else {
					String msg = SearchMessages.PatternConstructor_error_escape_sequence;
					throw new PatternSyntaxException(msg, replaceText, i);
				}
				break;

			case 120: // 'x'
				if (i + 2 < length) {
					int parsedInt;
					try {
						parsedInt = Integer.parseInt(replaceText.substring(i + 1, i + 3), 16);
						if (parsedInt < 0)
							throw new NumberFormatException();
					} catch (NumberFormatException e) {
						String msg = SearchMessages.PatternConstructor_error_hex_escape_sequence;
						throw new PatternSyntaxException(msg, replaceText, i);
					}
					interpretRetainCase(buf, (char) parsedInt);
					i += 2;
				} else {
					String msg = SearchMessages.PatternConstructor_error_hex_escape_sequence;
					throw new PatternSyntaxException(msg, replaceText, i);
				}
				break;

			case 117: // 'u'
				if (i + 4 < length) {
					int parsedInt;
					try {
						parsedInt = Integer.parseInt(replaceText.substring(i + 1, i + 5), 16);
						if (parsedInt < 0)
							throw new NumberFormatException();
					} catch (NumberFormatException e) {
						String msg = SearchMessages.PatternConstructor_error_unicode_escape_sequence;
						throw new PatternSyntaxException(msg, replaceText, i);
					}
					interpretRetainCase(buf, (char) parsedInt);
					i += 4;
				} else {
					String msg = SearchMessages.PatternConstructor_error_unicode_escape_sequence;
					throw new PatternSyntaxException(msg, replaceText, i);
				}
				break;

			case 67: // 'C'
				if (foundText.toUpperCase().equals(foundText)) {
					fRetainCaseMode = 1;
					break;
				}
				if (foundText.toLowerCase().equals(foundText)) {
					fRetainCaseMode = 2;
					break;
				}
				if (Character.isUpperCase(foundText.charAt(0)))
					fRetainCaseMode = 3;
				else
					fRetainCaseMode = 0;
				break;

			default:
				buf.append('\\').append(ch);
				break;
			}
			return i;
		}

		private void interpretRetainCase(StringBuffer buf, char ch) {
			if (fRetainCaseMode == 1)
				buf.append(Character.toUpperCase(ch));
			else if (fRetainCaseMode == 2)
				buf.append(Character.toLowerCase(ch));
			else if (fRetainCaseMode == 3) {
				buf.append(Character.toUpperCase(ch));
				fRetainCaseMode = 0;
			} else {
				buf.append(ch);
			}
		}

		public ReplaceStringConstructor(String lineDelim) {
			fLineDelim = lineDelim;
		}
	}

	private PatternConstructor() {
	}

	public static Pattern createPattern(String pattern, boolean isCaseSensitive, boolean isRegex) throws PatternSyntaxException {
		return createPattern(pattern, isRegex, true, isCaseSensitive, false);
	}

	public static Pattern createPattern(String pattern, boolean isRegex, boolean isStringMatcher, boolean isCaseSensitive, boolean isWholeWord) throws PatternSyntaxException {
		if (isRegex) {
			pattern = substituteLinebreak(pattern);
			if (isWholeWord) {
				StringBuffer buffer = new StringBuffer(pattern.length() + 10);
				buffer.append("\\b(?:").append(pattern).append(")\\b");
				pattern = buffer.toString();
			}
		} else {
			int len = pattern.length();
			StringBuffer buffer = new StringBuffer(len + 10);
			if (isWholeWord && len > 0 && isWordChar(pattern.charAt(0)))
				buffer.append("\\b");
			appendAsRegEx(isStringMatcher, pattern, buffer);
			if (isWholeWord && len > 0 && isWordChar(pattern.charAt(len - 1)))
				buffer.append("\\b");
			pattern = buffer.toString();
		}
		int regexOptions = Pattern.CASE_INSENSITIVE;
		if (!isCaseSensitive){
			regexOptions = Pattern.DOTALL ;
		}
		return Pattern.compile(pattern, regexOptions);
	}

	private static String substituteLinebreak(String findString) throws PatternSyntaxException {
		int length = findString.length();
		StringBuffer buf = new StringBuffer(length);
		int inCharGroup = 0;
		int inBraces = 0;
		boolean inQuote = false;
		for (int i = 0; i < length; i++) {
			char ch = findString.charAt(i);
			switch (ch) {
			case 91: // '['
				buf.append(ch);
				if (!inQuote)
					inCharGroup++;
				break;

			case 93: // ']'
				buf.append(ch);
				if (!inQuote)
					inCharGroup--;
				break;

			case 123: // '{'
				buf.append(ch);
				if (!inQuote && inCharGroup == 0)
					inBraces++;
				break;

			case 125: // '}'
				buf.append(ch);
				if (!inQuote && inCharGroup == 0)
					inBraces--;
				break;

			case 92: // '\\'
				if (i + 1 < length) {
					char ch1 = findString.charAt(i + 1);
					if (inQuote) {
						if (ch1 == 'E')
							inQuote = false;
						buf.append(ch).append(ch1);
						i++;
						break;
					}
					if (ch1 == 'R') {
						if (inCharGroup > 0 || inBraces > 0) {
							String msg = SearchMessages.PatternConstructor_error_line_delim_position;
							throw new PatternSyntaxException(msg, findString, i);
						}
						buf.append("(?>\\r\\n?|\\n)");
						i++;
						break;
					}
					if (ch1 == 'Q')
						inQuote = true;
					buf.append(ch).append(ch1);
					i++;
				} else {
					buf.append(ch);
				}
				break;

			default:
				buf.append(ch);
				break;
			}
		}

		return buf.toString();
	}

	private static boolean isWordChar(char c) {
		return Character.isLetterOrDigit(c);
	}

	public static Pattern createPattern(String patterns[], boolean isCaseSensitive) throws PatternSyntaxException {
		StringBuffer pattern = new StringBuffer();
		for (int i = 0; i < patterns.length; i++) {
			if (i > 0)
				pattern.append('|');
			appendAsRegEx(true, patterns[i], pattern);
		}

		return createPattern(pattern.toString(), true, true, isCaseSensitive, false);
	}

	public static StringBuffer appendAsRegEx(boolean isStringMatcher, String pattern, StringBuffer buffer) {
		boolean isEscaped = false;
		for (int i = 0; i < pattern.length(); i++) {
			char c = pattern.charAt(i);
			switch (c) {
			case 92: // '\\'
				if (isStringMatcher && !isEscaped) {
					isEscaped = true;
				} else {
					buffer.append("\\\\");
					isEscaped = false;
				}
				break;

			case 36: // '$'
			case 40: // '('
			case 41: // ')'
			case 43: // '+'
			case 46: // '.'
			case 91: // '['
			case 93: // ']'
			case 94: // '^'
			case 123: // '{'
			case 124: // '|'
			case 125: // '}'
				if (isEscaped) {
					buffer.append("\\\\");
					isEscaped = false;
				}
				buffer.append('\\');
				buffer.append(c);
				break;

			case 63: // '?'
				if (isStringMatcher && !isEscaped) {
					buffer.append('.');
				} else {
					buffer.append('\\');
					buffer.append(c);
					isEscaped = false;
				}
				break;

			case 42: // '*'
				if (isStringMatcher && !isEscaped) {
					buffer.append(".*");
				} else {
					buffer.append('\\');
					buffer.append(c);
					isEscaped = false;
				}
				break;

			default:
				if (isEscaped) {
					buffer.append("\\\\");
					isEscaped = false;
				}
				buffer.append(c);
				break;
			}
		}

		if (isEscaped) {
			buffer.append("\\\\");
			isEscaped = false;
		}
		return buffer;
	}

	public static String interpretReplaceEscapes(String replaceText, String foundText, String lineDelim) {
		return (new ReplaceStringConstructor(lineDelim)).interpretReplaceEscapes(replaceText, foundText);
	}
}
