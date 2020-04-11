package edu.mum.bd.m;

import java.util.ArrayList;
import java.util.List;

public final class Util {

	private static String pattern1 = "([a-zA-Z])+";
	private static String pattern2 = "([a-zA-Z])+[.?!,\"']{1}";
	private static String pattern3 = "[\"']{1}([a-zA-Z])+?";
	private static String pattern4 = "[a-zA-Z]+-[a-zA-Z]+";

	public static List<String> tokenizeSplit(String str) {
		List<String> rs = new ArrayList<>();
		String[] words = str.split(" ");
		for (int i = 0; i < words.length; ++i) {
			if (words[i].matches(pattern1)) {
				rs.add(words[i].toLowerCase());
			}else if (words[i].matches(pattern2)) {
				rs.add(words[i].substring(0, words[i].length() - 1).toLowerCase());
			}else if (words[i].matches(pattern3)) {
				rs.add(words[i].substring(1, words[i].length()).toLowerCase());
			} else if (words[i].matches(pattern4)) {
				String[] tmp = words[i].split("-");
				rs.add(tmp[0].toLowerCase());
				rs.add(tmp[1].toLowerCase());
			}
		}
		return rs;
	}
}
