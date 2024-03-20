package cvsreader;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DateHelper {

	public static String defaultDateFormat = "d-m-yyyy";

	public ArrayList<String> formats;

	private static final String a1 = "?([AaPp][Mm])";// "a";
	private static final String ka = "\\d:\\d\\d";// "k";
	private static final String w52 = "\\d{4}-W\\d{2}-\\d+";
	private static final String w3 = "(W([1-9]|[1-5][0-2]))";// "'W'ww";
	private static final String t = "T";// "'t'";

	private static final String y4 = "\\d\\d\\d\\d";// "yyyy";
	private static final String y5 = "\\d\\d\\d\\d\\d";// "yyyy";
	private static final String y2 = "";// "yy";
	private static final String M2 = "(0?[1-9]|1[0-2])";// "mm";
	private static final String m2 = "([0-9]|[0-5][0-9])";// "mm";

	private static final String m3 = "";// "mmm";
	private static final String m5 = "";// "mmmmm";
	private static final String d2 = "(0?[1-9]|[12][0-9]|3[01])";// "dd";
	private static final String d1 = "";// "d";
	private static final String s3 = "(\\d{3})";// "";

	private static final String sp = "\\s+";

	private static final String s2 = "([0-9]|[0-5][0-9])";// "ss";
	private static final String e3 = "";// "eee";
	private static final String h1 = "";// "h";
	private static final String h2 = "(00|[0-9]|1[0-9]|2[0-3])";// "hh";
	private static final String z1 = "\\d\\d\\d\\d";// "hhh";
	private static final String x3 = "\\d\\d:\\d\\d";// "hhh";
	private static final String clock = "'o''clock'";// "'o''clock'";
	private static final String pdt = "[a-zA-Z]{3}";
	private static final String e3_d2_m3 = "[a-zA-Z]{3}\\s+,\\d{2}\\s+[a-zA-Z]{3}\\s+"; // Wed, 4 Jul

	private static final String dayOftheWeek = "^(sun|Sun|mon|Mon|t(ues|hurs)|(T(ues|hurs))|Fri|fri)(day|\\.)?$"
			+ "|wed(\\.|nesday)?$|Wed(\\.|nesday)?$|Sat(\\.|urday)?$|sat(\\.|urday)?$|t((ue?)|(hu?r?))\\.?$|T((ue?)|(hu?r?))\\.?$";

	private static final String y_ml_wd = y5 + "." + dayOftheWeek + ".\\d{2}"; // 02001.July.04

	private static final String e3_m3 = "[a-zA-Z]{3}\\s+,[a-zA-Z]{3}\\s+,\\d{2}\\s+";// "([a-zA-Z]{3},\s+){2}";
																						// //![a-zA-Z]{3}\s+,[a-zA-Z]{3}\s+,\d{2}\s+
	// ![a-zA-Z]{3}\s+[a-zA-Z]{3}\s+\d{2}\s+

	private static final String ap = h2 + ":" + m2 + " " + a1;
	/*
	 * all of the formats of SimpleDateFormat class
	 */
	public static final Map<String, String> DATE_REGEX = new HashMap<String, String>() {
		private static final long serialVersionUID = 1L;
		{
			/*
			 * 12:08 PM
			 */
			put("^" + d2 + "-" + m2 + "-" + y4 + "$", defaultDateFormat);

			/*
			 * "yyyy.MM.dd G 'at' HH:mm:ss z" 2001.07.04 AD at 12:08:56 PDT
			 */
			put("^" + y4 + "." + M2 + "." + d2 + " AD at" + h2 + ":" + m2 + ":" + s2 + pdt + "?$",
					"yyyy.MM.dd G 'at' HH:mm:ss z");
			
			/*
			 * 2001.07.04 AD at 12:08:56 PDT
			 */
			put("^" + e3_m3 + d2 + sp + a1 + ",\\s+'" + y2 + "$", "EEE, MMM d, ''yy");
			
			/*
			 * Wed, Jul 4, '01
			 */
			put("^" + ap + "$", "h:mm a");
			
			/*
			 * 12:08 PM
			 */
			put("^" + h2 + clock + sp + a1 + "$", "hh 'o''clock' a, zzzz");

			/*
			 * 12 o'clock PM, Pacific Daylight Time
			 */
			put("^" + h2 + clock + sp + a1 + "$", "hh 'o''clock' a");
			
			/*
			 * 0:08 PM, PDT
			 */
			put("^" + ka + ":" + m2 + " " + a1 + ",\\s+" + z1 + "$", "K:mm a, z");
			
			// todo
			/*
			 * 02001.July.04 AD 12:08 PM
			 */			
			put("^" + sp + "AD" + sp + ap + "$", "yyyyy.MMMMM.dd GGG hh:mm aaa");
			
			/*
			 * Wed, 4 Jul 2001 12:08:56-0700
			 */			
			put("^" + e3_d2_m3 + sp + y4 + sp + h2 + ":" + m2 + ":" + s2 + sp + "-" + z1 + "$",
					"EEE, d MMM yyyy HH:mm:ss Z");

			/*
			 *  010704120856-0700
			 */
			put("^" + y2 + M2 + d2 + h2 + m2 + s2 + "-" + z1 + "$", "yyMMddHHmmssZ"); 

			/*
			 * 2018-08-01 23:58:32.425
			 */			
			put("^" + y4 + "-" + M2 + "-" + d2 + sp + h2 + ":" + m2 + ":" + s2 + "\\." + s3 + "$",
					"YYYY-MM-DD HH:MM:SS.fff"); 

			/*
			 * 2001-07-04T12:08:56.235-0700
			 */
			put("^" + y4 + "-" + M2 + "-" + d2 + t + h2 + ":" + m2 + ":" + s2 + "\\." + s3 + "-" + z1 + "$",
					"yyyy-MM-dd'T'HH:mm:ss.SSSZ");

			/*
			 * put("^" + y4 + "-" + w52 + "$", "YYYY-'W'ww-u"); // 2001-W27-3
			 */
			put("^" + y4 + "-" + M2 + "-" + d2 + t + h2 + ":" + m2 + ":" + s2 + "\\." + s3 + "-" + x3 + "$",
					"yyyy-MM-dd'T'HH:mm:ss.SSSXXX"); // 2001-07-04T12:08:56.235-07:00

			/*
			 * put("^" + y4 + "-" + w3 +"$", "YYYY-'W'ww-u"); // 2001-W27-3
			 */
			put("^\\d{4}-W\\d{2}-(\\d{1}|\\d{2})$", "YYYY-'W'ww-u");

		}
	};

	public static String getDateFormat(String value_date) {
		for (String date_format_case : DateHelper.DATE_REGEX.keySet()) {
			if (value_date.matches(date_format_case)) {
				System.out.println("success parsing: " + date_format_case);
				return DateHelper.DATE_REGEX.get(date_format_case);
			}
		}
		// System.out.println( date_format_case);
		System.out.println("failed parsing:	" + value_date);
		return null;
	}

	public static Date convertDate(String value_date) {

		String format = DateHelper.getDateFormat(value_date);

		if (format == null)
			format = defaultDateFormat;

		if (value_date.equals("NULL")) {
			return new Date();
		}

		SimpleDateFormat fmt = new SimpleDateFormat(format);
		Date date = null;
		try {

			date = fmt.parse(value_date);

		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

}
