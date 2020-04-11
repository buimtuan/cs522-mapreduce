// package edu.mum.bd;

// import java.io.BufferedReader;
// import java.io.IOException;
// import java.io.Serializable;
// import java.nio.file.Files;
// import java.nio.file.Path;
// import java.nio.file.Paths;
// import java.util.ArrayList;
// import java.util.Collections;
// import java.util.List;

// public class W1D1 {

// 	public static void main(String[] args) throws IOException {
// 		List<Pair<String, Integer>> rs = loadFile(args[0]);
// 		rs.stream().forEach(System.out::println);
// 	}

// 	private static List<Pair<String, Integer>> loadFile(String file) throws IOException {

// 		List<Pair<String, Integer>> tokens = new ArrayList<>();

// 		Path path = Paths.get(file);
// 		BufferedReader reader = Files.newBufferedReader(path);
// 		String pattern1 = "([a-zA-Z])+";
// 		String pattern2 = "([a-zA-Z])+[.?!,]{1}";
// 		String pattern3 = "[a-zA-Z]+-[a-zA-Z]+";
// 		while (true) {
// 			String line = reader.readLine();
// 			if (line == null) {
// 				break;
// 			}
// 			String[] words = line.split(" ");
// 			for (int i = 0; i < words.length; ++i) {
// 				if (words[i].matches(pattern1)) {
// 					tokens.add(new Pair<String, Integer>(words[i].toLowerCase(), 1));
// 				}else if (words[i].matches(pattern2)) {
// 					tokens.add(new Pair<String, Integer>(words[i].substring(0, words[i].length() - 1).toLowerCase(), 1));
// 				} else if (words[i].matches(pattern3)) {
// 					String[] tmp = words[i].split("-");
// 					tokens.add(new Pair<String, Integer>(tmp[0].toLowerCase(), 1));
// 					tokens.add(new Pair<String, Integer>(tmp[1].toLowerCase(), 1));
// 				}
// 			}
// 		}
// 		// FileReader reader = new FileReader(file);
// 		// StreamTokenizer streamTokenizer = new StreamTokenizer(reader);
// 		// streamTokenizer.resetSyntax();
// 		// streamTokenizer.ordinaryChar('-');
// 		// streamTokenizer.wordChars('a', 'z');
// 		// streamTokenizer.wordChars('A', 'Z');
// 		// streamTokenizer.whitespaceChars(0, ' ');
// 		// streamTokenizer.eolIsSignificant(true);
// 		// List<Pair<String, Integer>> tokens = new ArrayList<>();
//     	// while (streamTokenizer.nextToken() != StreamTokenizer.TT_EOF) {
// 		// 	System.out.println(streamTokenizer.sval);
// 		// 	if (
// 		// 		streamTokenizer.ttype == StreamTokenizer.TT_WORD
// 		// 	) {
//         //     	tokens.add(new Pair<String, Integer>(streamTokenizer.sval.toLowerCase(), 1));
// 		// 	}
// 		// }
// 		Collections.sort(tokens);
// 		return tokens;
// 	}

// }

// class Pair<T extends Comparable<T>, V> implements Serializable, Comparable<Pair<T,V>> {

// 	private static final long serialVersionUID = -3608777750064813974L;
// 	private T left;
// 	private V right;

// 	public Pair() {
// 	}

// 	public Pair(T left, V right) {
// 		this.left = left;
// 		this.right = right;
// 	}

// 	public T getLeft() {
// 		return this.left;
// 	}

// 	public V getRight() {
// 		return this.right;
// 	}

// 	public Pair<T, V> setLeft(T left) {
// 		this.left = left;
// 		return this;
// 	}

// 	public Pair<T, V> setRight(V right) {
// 		this.right = right;
// 		return this;
// 	}

// 	@Override
// 	public String toString() {
// 		return "(" + getLeft() + "," + getRight() + ")";
// 	}

// 	@Override
// 	public int compareTo(Pair<T, V> o) {
// 		return left.compareTo(o.getLeft());
// 	}

// }
