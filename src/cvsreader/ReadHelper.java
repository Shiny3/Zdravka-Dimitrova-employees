package cvsreader;

public class ReadHelper {

	/*
	 * TODO:
	 * unite the functions and use only own written one instead of matching them and having n-time cycling*/
	
	/*
	 * split the sentence of read file line into array of the columns.
	 */
	public static String[] splitter(String sentence, char delimiter, int LIMIT_COUNTER_WORDS) {

		int index_beginning = 0;
		int cnt_word = 0;
		String[] words = new String[LIMIT_COUNTER_WORDS];
		for (Integer position_delimiter = 0; position_delimiter < sentence.length(); position_delimiter++) {
			if (sentence.charAt(position_delimiter) == delimiter) {
				words[cnt_word] = sentence.substring(index_beginning, position_delimiter);
				index_beginning = position_delimiter + 1;
				cnt_word++;
			}
			if (cnt_word == LIMIT_COUNTER_WORDS - 1) {
				words[cnt_word] = sentence.substring(index_beginning, sentence.length());
				break;
			}
		}
		return words;
	}
	
	/*
	 * replace delimiters for getting the default formatter, because 
	 */
	public static String replaceDelimiterInSentence(String sentence, char[] delimiter, int LIMIT_COUNTER_DELIMITER) {

		// int counter_digit = 0;
		int[] rpl = new int[LIMIT_COUNTER_DELIMITER];
		StringBuffer replacementBuffer = new StringBuffer();

		for (int position_delimiter = 0; position_delimiter < sentence.length(); position_delimiter++) {
			for (int i = 0; i < LIMIT_COUNTER_DELIMITER; i++) {
				char symbol = sentence.charAt(position_delimiter);
				if (symbol == ',') {
					replacementBuffer.append(',');
					break;
				}
				if (symbol == '\s') {
					replacementBuffer.append('-');
					break;
				} else if (symbol == delimiter[i]) {
					replacementBuffer.append('-');
					break;
				} else if ((symbol >= '0' && symbol <= '9')) {
					replacementBuffer.append(symbol);
					// counter_digit++;
					break;
				}
			}
		}
		/*
		 * for (int i = 0; i < LIMIT_COUNTER_DELIMITER; i++) { if (rpl[i] ==
		 * LIMIT_COUNTER_DELIMITER && counter_digit == (LIMIT_COUNTER_DELIMITER -
		 * replacementBuffer.toString().length())) { return
		 * replacementBuffer.toString(); // delimiter[i]; } }
		 */
		return replacementBuffer.toString();// "";//
	}

	/*
	 * checking if it is the default formatter
	 */
	public static String[] isDelimiterExistsMoreThan(String sentence, char delimiter, int LIMIT_COUNTER_DELIMITER) {
		int index_of_seperation = 0;
		int counter_delimiter = 0;
		String[] dates = new String[2];
		for (Integer position_delimiter = 0; position_delimiter < sentence.length(); position_delimiter++) {

			if (sentence.charAt(position_delimiter) == 'W' || sentence.charAt(position_delimiter) == 'w') {
				return null;
			}
			if (sentence.charAt(position_delimiter) == delimiter) {
				counter_delimiter++;
				if (counter_delimiter == LIMIT_COUNTER_DELIMITER) {
					if (index_of_seperation != 0) {
						dates[0] = sentence.substring(0, index_of_seperation);
						dates[1] = sentence.substring(index_of_seperation + 1);
						return dates;
					}
					return null;
				}
			}
			if (sentence.charAt(position_delimiter) == ',')
				index_of_seperation = position_delimiter;
		}
		return null;
	}

}
