// package edu.mum.bd;

// import java.io.BufferedReader;
// import java.io.BufferedWriter;
// import java.io.FileWriter;
// import java.io.IOException;
// import java.io.Serializable;
// import java.lang.reflect.Type;
// import java.nio.file.Files;
// import java.nio.file.Path;
// import java.nio.file.Paths;
// import java.util.ArrayList;
// import java.util.Collections;
// import java.util.Iterator;
// import java.util.List;

// public class W1D2Ai {

// 	private void a() {
// 		// this.getClass()
// 	}
// 	public static void main(String[] args) throws Exception {
// 		Job job = new Job(args[0], args[1]);
// 		job.setMapper(MyMapper.class);
// 		job.setReducer(MyReducer.class);
// 		// job.setMapperOutputKey(String.class);
// 		// job.setMapperOutputValue(Long.class);
// 		// job.setReducerOutputKey(String.class);
// 		// job.setReducerOutputValue(Long.class);
// 		job.run();
// 		// List<Pair<String, Integer>> rs = loadFile(args[0]);
// 		// rs.stream().forEach(System.out::println);
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

// class MyMapper extends Mapper<Long, String, String, Long> {

// 	private static String pattern1 = "([a-zA-Z])+";
// 	private static String pattern2 = "([a-zA-Z])+[.?!,\"']{1}";
// 	private static String pattern3 = "[\"']{1}([a-zA-Z])+?";
// 	private static String pattern4 = "[a-zA-Z]+-[a-zA-Z]+";

// 	List<Pair<String, Integer>> tokens = new ArrayList<>();

// 	@Override
// 	public List<Pair<String, Long>> mapper(Long lineNumber, String line) {
// 		String[] words = line.split(" ");
// 		for (int i = 0; i < words.length; ++i) {
// 			if (words[i].matches(pattern1)) {
// 				tokens.add(new Pair<String, Integer>(words[i].toLowerCase(), 1));
// 			}else if (words[i].matches(pattern2)) {
// 				tokens.add(new Pair<String, Integer>(words[i].substring(0, words[i].length() - 1).toLowerCase(), 1));
// 			}else if (words[i].matches(pattern3)) {
// 				tokens.add(new Pair<String, Integer>(words[i].substring(1, words[i].length()).toLowerCase(), 1));
// 			} else if (words[i].matches(pattern4)) {
// 				String[] tmp = words[i].split("-");
// 				tokens.add(new Pair<String, Integer>(tmp[0].toLowerCase(), 1));
// 				tokens.add(new Pair<String, Integer>(tmp[1].toLowerCase(), 1));
// 			}
// 		}
// 		return null;
// 	}

// }

// class MyReducer extends Reducer<String, Iterator<Long>, String, Long> {

// 	@Override
// 	public List<Pair<String, Long>> reducer(String k1, Iterator<Iterator<Long>> v1) {
// 		return null;
// 	}

// }


// abstract class Mapper<K1, V1, K2 extends Comparable<K2>, V2> {

// 	// public abstract void mapper(K1 k1, V1 v1, K2 k2, V2 v2);
// 	public abstract List<Pair<K2, V2>> mapper(K1 k1, V1 v1);

// }

// abstract class Reducer<K1 extends Comparable<K2>, V1, K2 extends Comparable<K2>, V2>  {

// 	// public abstract void reducer(K1 k1, Iterator<V1> v1, K2 k2, V2 v2);
// 	public abstract List<Pair<K2, V2>> reducer(K1 k1, Iterator<V1> v1);

// }

// @SuppressWarnings({"rawtypes","unchecked", "unused"})
// class Job<K1, V1, K2 extends Comparable<K2>, V2, K3 extends Comparable<K3>, V3> {

// 	private String input;
// 	private String output;
// 	private Mapper mapper;
// 	private Reducer reducer;
// 	private List<Pair<K2, V2>> tokens = new ArrayList<>();

// 	// private Class mapperOutputKey;
// 	// private Class mapperOutputValue;
// 	// private Class reducerOutputKey;
// 	// private Class reducerOutputValue;


// 	// public void setMapperOutputKey(Class mapperOutputKey) {
// 	// 	this.mapperOutputKey = mapperOutputKey;
// 	// }

// 	// public void setMapperOutputValue(Class mapperOutputValue) {
// 	// 	this.mapperOutputValue = mapperOutputValue;
// 	// }

// 	// public void setReducerOutputKey(Class reducerOutputKey) {
// 	// 	this.reducerOutputKey = reducerOutputKey;
// 	// }

// 	// public void setReducerOutputValue(Class reducerOutputValue) {
// 	// 	this.reducerOutputValue = reducerOutputValue;
// 	// }

// 	public Job(String input, String output) {
// 		this.input = input;
// 		this.output = output;
// 	}

// 	public String getInput() {
// 		return this.input;
// 	}

// 	public void setInput(String input) {
// 		this.input = input;
// 	}

// 	public String getOutput() {
// 		return this.output;
// 	}

// 	public void setOutput(String output) {
// 		this.output = output;
// 	}

// 	public void setMapper(Class mapperClass) throws Exception {
// 		this.mapper = (Mapper)mapperClass.getConstructor().newInstance();
// 	}

// 	public void setReducer(Class reducerClass) throws Exception {
// 		this.reducer = (Reducer)reducerClass.getConstructor().newInstance();
// 	}

// 	public void run() throws IOException {
// 		readFile(input);
// 		groupby();
// 		String content = content();
// 		writeFile(output, content);
// 	}

// 	private void groupby() {
// 		List<Pair<K2, Iterator<V2>>> group = new ArrayList<>();
// 		for (Pair<K2, V2> token : tokens) {
// 			// if (group.)
// 		}
// 		// mapperOutputKey.

// 	}

// 	private String content() {
// 		return null;
// 	}

// 	private void readFile(String file) throws IOException {
// 		Path path = Paths.get(file);
// 		BufferedReader reader = Files.newBufferedReader(path);
// 		long i = 0;
// 		while (true) {
// 			String line = reader.readLine();
// 			if (line == null) {
// 				break;
// 			}
// 			tokens.addAll(mapper.mapper(i++, line));
// 		}
// 		Collections.sort(tokens);
// 	}
// 	private void writeFile(String file, String content) throws IOException {
// 		// Path path = Paths.get(file);
// 		// File file = new File("/Users/pankaj/BufferedWriter.txt");
// 		// FileWriter fr = null;
// 		BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
// 		writer.write(content);
// 		writer.close();
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
