package cvsreader;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DateHelper {

	public static String defaultDateFormat = "dd-MM-yyyy";

	public ArrayList<String> formats;

	private static final String delimiter = "(-|\\|/|\\.)";// s|-|\\? ...: escape : \
	private static final String delimiter_dash = "-";
	private static final String sp = "\\s+";
	private static final String a1 = "([AaPp][Mm])?";// "a";
	private static final String ka = "\\d:\\d\\d";// "k";
	private static final String w52 = "\\d{4}-W\\d{2}-\\d+";
	private static final String w3 = "(W([1-9]|[1-5][0-2]))";// "'W'ww";
	private static final String t = "T";// "'t'";
	private static final String year55 = "\\d\\d\\d\\d\\d";
	private static final String year = "\\d\\d\\d\\d";
	// private static final String year55 = "(\\d\\d{2}|d{4})";// "yyyy";
	private static final String year1 = "(\\d?((\\d{2})|(19[7-9]\\d)|(20\\d{4}))))";// "yyyy";
	private static final String M2 = "((0?[1-9]|1[0-2]))";// "mm";
	private static final String m2 = "([0-9]|[0-5][0-9])";// "mm";

	private static final String d2 = "(0?[1-9]|[12][0-9]|3[01])";// "dd";
	private static final String s3 = "(\\d\\d\\d)";// "";
	private static final String s2 = "([0-9]|[0-5][0-9])";// "ss";
	private static final String h2 = "(00|[0-9]|1[0-9]|2[0-3])";// "hh";
	private static final String z1 = "\\d\\d\\d\\d";// "hhh";
	private static final String x3 = "\\d\\d:\\d\\d";// "hhh";
	private static final String clock = "'o''clock'";// "'o''clock'";
	private static final String pdt = "[a-zA-Z]{3}";
	private static final String e3_d2_m3 = "[a-zA-Z]{3},\\s+\\d{2}\\s+[a-zA-Z]{3}\\s+"; // Wed, 4 Jul

	private static final String dayOftheWeek = "(sun|Sun|mon|Mon|t(ues|hurs)|(T(ues|hurs))|Fri|fri)(day|\\.)?"
			+ "|wed(\\,|\\.|nesday)?$|Wed(\\,|\\.|nesday)?|Sat(\\,|\\.|urday)?$|sat(\\,|\\.|urday)?|t((ue?)|(hu?r?))\\.?|T((ue?)|(hu?r?))\\.?";

	private static final String months = "(?:Jan(?:uary)?|Feb(?:ruary)?|Mar(?:ch)?|Apr(?:il)?|May|Jun(?:e)?|Jul(?:y)?|Aug(?:ust)?|Sep(?:tember)?|Oct(?:ober)?|(Nov|Dec)(?:ember)?)";
	private static final String y_ml_wd = year55 + "." + "(" + m2 + "|" + months + ")" + ".\\d{2}";

	private static final String e3_m3 = "[a-zA-Z]{3},\\s+[a-zA-Z]{3},\\s+\\d{2}\\s+";

	private static final String ap = h2 + ":" + m2 + "\\s+" + a1;

	public static String defaultDateFormat_variation = "dd-MM-yyyy";

	/*
	 * all of the formats of SimpleDateFormat class that are shown from the official
	 * sites
	 */
	public static final Map<String, String> LONG_DATES_REGEX_FORMATS = new HashMap<String, String>() {
		private static final long serialVersionUID = 1L;
		{
			/*
			 * JUST DATES - LONG VERSIONS
			 */

			/*
			 * default variations
			 */

			// put(defaultDateFormat, "(" + year + delimiter_dash + M2 + delimiter_dash + d2
			// + ")");
			put(defaultDateFormat, "(" + d2 + delimiter + M2 + delimiter + year + ")");
			// put(defaultDateFormat, "(" + d2 + "-" + M2 + "-" + year + ")");
			// put(defaultDateFormat_variation, "(" + d2 + delimiter + M2 + delimiter + year
			// + ")");

			// put(defaultDateFormat, "(" + d2 + " " + M2 + " " + year + ")");

			/*
			 * 010704120856-0700
			 */
			put("yyMMddHHmmssZ", "(" + year + M2 + d2 + h2 + m2 + s2 + "-" + z1 + ")");

			/*
			 * 2018-08-01 23:58:32.425
			 */
			put("YYYY-MM-DD HH:MM:SS.fff",
					"(" + year + "-" + M2 + "-" + d2 + sp + h2 + ":" + m2 + ":" + s2 + "\\." + s3 + ")");

			/*
			 * 2001-07-04T12:08:56.235-0700
			 */
			put("yyyy-MM-dd'T'HH:mm:ss.SSSZ",
					"(" + year + "-" + M2 + "-" + d2 + t + h2 + ":" + m2 + ":" + s2 + "\\." + s3 + "-" + z1 + ")");

			/*
			 * put("(" + y4 + "-" + w52 + ")", "YYYY-'W'ww-u"); // 2001-W27-3
			 */
			put("yyyy-MM-dd'T'HH:mm:ss.SSSXXX",
					"(" + year + "-" + M2 + "-" + d2 + t + h2 + ":" + m2 + ":" + s2 + "\\." + s3 + "-" + x3 + ")"); // 2001-07-04T12:08:56.235-07:00

			/*
			 * put("(" + y4 + "-" + w3 +")", "YYYY-'W'ww-u"); // 2001-W27-3
			 */
			put("YYYY-'W'ww-u", "(\\d{4}-W(\\d{1}|\\d{2})-(\\d{1}|\\d{2}))");

			// todo difference of this format;
			/*
			 * 02001.July.04 AD 12:08 PM
			 */
			put("yyyyy.MMMMM.dd GGG hh:mm aaa", "(" + y_ml_wd + sp + "AD" + sp + ap + ")");
			/*
			 * "yyyy.MM.dd G 'at' HH:mm:ss z" 2001.07.04 AD at 12:08:56 PDT
			 */
			put("yyyy.MM.dd G 'at' HH:mm:ss z", "(" + year + "." + M2 + "." + d2 + sp + "AD" + sp + "at" + sp + h2 + ":"
					+ m2 + ":" + s2 + sp + pdt + "?)");

			// Wed, 4 Jun 2001 12:08:56-0700
			/*
			 * Wed, 4 Jul 2001 12:08:56-0700
			 */
			put("EEE, d MMM yyyy HH:mm:ss Z", "(" + dayOftheWeek + d2 + sp + months + sp + year + sp + h2 + ":" + m2
					+ ":" + s2 + "?" + sp + "-" + z1 + ")");

		}
	};

	public static final Map<String, String> TODAY_DATES_REGEX = new HashMap<String, String>() {
		private static final long serialVersionUID = 1L;
		{
			/*
			 * TODAY DATES
			 */

			/*
			 * 12 o'clock PM, Pacific Daylight Time
			 */
			put("hh 'o''clock' a, zzzz", "(" + h2 + clock + sp + a1 + ")");
			put("hh 'o''clock' a", "(" + h2 + clock + sp + a1 + ")");

			/*
			 * Wed, Jul 4, '01
			 */
			put("EEE, MMM d, ''yy", "(" + e3_m3 + d2 + sp + a1 + ",\\s+'" + year + ")");

			/*
			 * 12:08 PM
			 */
			put("h:mm a", "(" + ap + ")");

			/*
			 * 0:08 PM, PDT
			 */
			put("K:mm a, z", "(" + ka + ":" + m2 + " " + a1 + ",\\s+" + z1 + ")");
		}
	};

	public static Date convertDate(String value_date, String format) {
		Date date = null;
		try {
			if (value_date.equals("NULL")) {
				return (new SimpleDateFormat(format)).parse(new SimpleDateFormat(format).format(new Date()));
			}
			date = (new SimpleDateFormat(format)).parse(value_date); // Unparseable date: "02001.December.18 AD 12:08
																		// PM"
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	public static ArrayList<Date> matchestDates(String dates) throws ParseException {

		/*
		 * TODO should be improved.... because of other types length and null cases to
		 * skip not necessary duplicated steps
		 */
		char[] delimiters = { '/', '\\', '/', '.' };
		ArrayList<Date> matched_dates = new ArrayList<Date>();
		System.out.println("	DATES:  " + dates);

		if (dates.startsWith("NULL")) {
			Date today = new Date();
			matched_dates.add(0, today);
			matched_dates.add(0, today);
			return matched_dates;
		}

		if (dates.endsWith("NULL")) {
			try {
				String from = dates.substring(0, dates.indexOf(','));
				String format = getDateFormat(from);
				/*
				 * TODO
				 * 
				 */
				SimpleDateFormat formatSD = new SimpleDateFormat(format);
				Date today = formatSD.parse(new SimpleDateFormat(format).format(new Date()));
				matched_dates.add(convertDate(from, format));
				matched_dates.add(today);
				return matched_dates;
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}

		/* TODO:
		 * try again with regex, why parsing with delimiters is failing
		 */
	/**/	int length = dates.length();
		if (length > 12 && length < 21 && dates.charAt(1) != '-' && dates.charAt(2) != '-') {
			String replacement = ReadHelper.replaceDelimiterInSentence(dates, delimiters, 4);
			if (!replacement.isEmpty())
				dates = replacement.trim();
			// System.out.println("replacement:" + dates);
		}

		String[] seperation = ReadHelper.isDelimiterExistsMoreThan(dates, '-', 4);
		if (seperation != null && seperation.length == 2) {
			matched_dates.add(DateHelper.convertDateWithDefault(seperation[0]));
			matched_dates.add(DateHelper.convertDateWithDefault(seperation[1]));
		} else {
			System.out.println("LONG_DATES_REGEX_FORMATS");
			for (Entry<String, String> date_format_case : DateHelper.LONG_DATES_REGEX_FORMATS.entrySet()) {
				matched_dates = datesMatcher(dates, date_format_case);
				if (matched_dates.size() == 2) {
					// System.out.println("date format: " + date_format_case);
					// System.out.println(" " + matched_dates);
					return matched_dates;
				}
			}
		}

		return matched_dates;
	}

	public static ArrayList<Date> datesMatcher(String dates, Entry<String, String> regex) {

		ArrayList<Date> dates_seperated = new ArrayList<Date>();
		Pattern pattern = Pattern.compile(regex.getValue());
		Matcher matcher = pattern.matcher(dates);
		MatchResult result = matcher.toMatchResult();
		// System.out.println("Current Matcher: " + result.groupCount());
		while (matcher.find()) {
			String date = matcher.group();
			Date date_converted = convertDate(date, regex.getKey());
			dates_seperated.add(date_converted);
			// System.out.println("group: " + date_converted + " " + matcher.group());
		}
		return dates_seperated;
	}

	public static String getDateFormat(String value_date) {
		for (Entry<String, String> date_format_case : DateHelper.LONG_DATES_REGEX_FORMATS.entrySet()) {
			if (value_date.matches(date_format_case.getValue())) {
				// System.out.println("success parsing: " + date_format_case.getKey());
				return date_format_case.getKey();
			}
		}
		System.out.println("failed parsing:	" + value_date);
		return null;
	}

	public static Date convertDateWithDefault(String value_date) throws ParseException {
		if (!value_date.equals("NULL")) {
			return (new SimpleDateFormat(defaultDateFormat)).parse(value_date);
		}
		return (new SimpleDateFormat(defaultDateFormat))
				.parse(new SimpleDateFormat(defaultDateFormat).format(new Date()));
	}
}
