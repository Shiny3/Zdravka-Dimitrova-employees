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

	public static String defaultDateFormat = "d-m-yyyy";

	public ArrayList<String> formats;

	private static final String sp = "\\s+";
	private static final String a1 = "?([AaPp][Mm])";// "a";
	private static final String ka = "\\d:\\d\\d";// "k";
	private static final String w52 = "\\d{4}-W\\d{2}-\\d+";
	private static final String w3 = "(W([1-9]|[1-5][0-2]))";// "'W'ww";
	private static final String t = "T";// "'t'";

	private static final String y4 = "\\d\\d\\d\\d";// "yyyy";
	private static final String y5 = "\\d\\d\\d\\d\\d";// "yyyy";
	private static final String y2 = "\\d\\d";// "yy";
	private static final String M2 = "(0?[1-9]|1[0-2])";// "mm";
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

	private static final String dayOftheWeek = "^(sun|Sun|mon|Mon|t(ues|hurs)|(T(ues|hurs))|Fri|fri)(day|\\.)?$"
			+ "|wed(\\.|nesday)?$|Wed(\\.|nesday)?$|Sat(\\.|urday)?$|sat(\\.|urday)?$|t((ue?)|(hu?r?))\\.?$|T((ue?)|(hu?r?))\\.?$";

	private static final String y_ml_wd = y5 + "." + dayOftheWeek + ".\\d{2}"; // 02001.July.04

	private static final String e3_m3 = "[a-zA-Z]{3},\\s+[a-zA-Z]{3},\\s+\\d{2}\\s+";// "([a-zA-Z]{3},\s+){2}";
																						// //![a-zA-Z]{3}\s+,[a-zA-Z]{3}\s+,\d{2}\s+
	// ![a-zA-Z]{3}\s+[a-zA-Z]{3}\s+\d{2}\s+

	private static final String ap = h2 + ":" + m2 + " " + a1;
	/*
	 * all of the formats of SimpleDateFormat class
	 */
	public static final Map<String, String> DATES_REGEX = new HashMap<String, String>() {
		private static final long serialVersionUID = 1L;
		{
			/*
			 * 12:08 PM
			 */
			put(defaultDateFormat, "(" + d2 + "-" + m2 + "-" + y4 + ")");

			/*
			 * 12:08 PM
			 */
			// put("(" + d2 + "-" + m2 + "-" + y4 + ")", defaultDateFormat);

			/*
			 * "yyyy.MM.dd G 'at' HH:mm:ss z" 2001.07.04 AD at 12:08:56 PDT
			 */
			put("yyyy.MM.dd G 'at' HH:mm:ss z",
					"(" + y4 + "." + M2 + "." + d2 + " AD at" + h2 + ":" + m2 + ":" + s2 + pdt + "?)");

			/*
			 * 2001.07.04 AD at 12:08:56 PDT
			 */

			/*
			 * Wed, Jul 4, '01
			 */
			put("EEE, MMM d, ''yy", "(" + e3_m3 + d2 + sp + a1 + ",\\s+'" + y2 + ")");

			/*
			 * 12:08 PM
			 */
			put("h:mm a", "(" + ap + ")");
			
			/*
			 * 12 o'clock PM, Pacific Daylight Time
			 */
			put("hh 'o''clock' a, zzzz", "(" + h2 + clock + sp + a1 + ")");

			/*
			 * 0:08 PM, PDT
			 */
			put("hh 'o''clock' a", "(" + h2 + clock + sp + a1 + ")");


			put("K:mm a, z", "(" + ka + ":" + m2 + " " + a1 + ",\\s+" + z1 + ")");

			// todo
			/*
			 * 02001.July.04 AD 12:08 PM
			 */
			put("yyyyy.MMMMM.dd GGG hh:mm aaa", "(" + y_ml_wd + sp + "AD" + sp + ap + ")");

			/*
			 * Wed, 4 Jul 2001 12:08:56-0700
			 */
			put("EEE, d MMM yyyy HH:mm:ss Z",
					"(" + e3_d2_m3 + sp + y4 + sp + h2 + ":" + m2 + ":" + s2 + "?"+sp + "-" + z1 + ")");

			/*
			 * 010704120856-0700
			 */
			put("yyMMddHHmmssZ", "(" + y2 + M2 + d2 + h2 + m2 + s2 + "-" + z1 + ")");

			/*
			 * 2018-08-01 23:58:32.425
			 */
			put("YYYY-MM-DD HH:MM:SS.fff",
					"(" + y4 + "-" + M2 + "-" + d2 + sp + h2 + ":" + m2 + ":" + s2 + "\\." + s3 + ")");

			/*
			 * 2001-07-04T12:08:56.235-0700
			 */
			put("yyyy-MM-dd'T'HH:mm:ss.SSSZ",
					"(" + y4 + "-" + M2 + "-" + d2 + t + h2 + ":" + m2 + ":" + s2 + "\\." + s3 + "-" + z1 + ")");

			/*
			 * put("(" + y4 + "-" + w52 + ")", "YYYY-'W'ww-u"); // 2001-W27-3
			 */
			put("yyyy-MM-dd'T'HH:mm:ss.SSSXXX",
					"(" + y4 + "-" + M2 + "-" + d2 + t + h2 + ":" + m2 + ":" + s2 + "\\." + s3 + "-" + x3 + ")"); // 2001-07-04T12:08:56.235-07:00

			/*
			 * put("(" + y4 + "-" + w3 +")", "YYYY-'W'ww-u"); // 2001-W27-3
			 */
			put("YYYY-'W'ww-u", "(\\d{4}-W(\\d{1}|\\d{2})-(\\d{1}|\\d{2}))");

		}
	};

	public static String getDateFormat(String value_date) {
		for (String date_format_case : DateHelper.DATES_REGEX.keySet()) {
			if (value_date.matches(date_format_case)) {
				System.out.println("success parsing: " + date_format_case);
				return DateHelper.DATES_REGEX.get(date_format_case);
			}
		}
		System.out.println("failed parsing:	" + value_date);
		return null;
	}

	public static Date convertDate(String value_date) {
		Date date = null;
		String format = DateHelper.getDateFormat(value_date);
		try {
			if (value_date.equals("NULL"))
				return new Date();
			if (format == null)
				format = defaultDateFormat;
			SimpleDateFormat fmt = new SimpleDateFormat(format);
			date = fmt.parse(value_date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	public static Date convertDate(String value_date, String format) {
		Date date = null;
		try {
			if (value_date.equals("NULL")) {
				return new Date();
			}
			SimpleDateFormat fmt = new SimpleDateFormat(format);
			date = fmt.parse(value_date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	public static ArrayList<Date> matchestDates(String dates) {

		ArrayList<Date> matched_dates = null;

		if (dates.startsWith("NULL")) {
			matched_dates.set(0, (new Date()));
		}
		if (dates.endsWith("NULL")) {
			matched_dates.set(0, (new Date()));
		}

		for (Entry<String, String> date_format_case : DateHelper.DATES_REGEX.entrySet()) {

			ArrayList<Date> matched_dates_ = datesMatcher(dates, date_format_case);

			System.out.println("matched_dates_" + matched_dates_);

			if (matched_dates_.size() == 2) {
				// matched_dates.add(date_format_case);
				System.out.println(matched_dates_);
				return matched_dates_;
			}

		}
		return matched_dates;
	}

	// todo null date
	public static ArrayList<Date> datesMatcher(String dates, Entry<String, String> regex) {

		ArrayList<Date> dates_seperated = new ArrayList<Date>();
		Pattern pattern = Pattern.compile(regex.getValue());
		Matcher matcher = pattern.matcher(dates);

		MatchResult result = matcher.toMatchResult();
		// System.out.println("Current Matcher: " + result);

		while (matcher.find()) {
			String date = matcher.group();
			Date date_converted = convertDate(date, regex.getKey());
			dates_seperated.add(date_converted);
			System.out.println("group: " + regex.getValue() + date_converted + "	" + matcher.group());
		}
		return dates_seperated;
	}

}
