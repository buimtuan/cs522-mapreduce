package edu.mum.bd.m;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public final class IO {

	public static List<String> load(String target) throws IOException {
		List<String> files = new ArrayList<>();
		Path path = Paths.get(target);
		if (Files.isDirectory(path)) {
			try (Stream<Path> paths = Files.list(path)) {
				paths
					.filter(Files::isRegularFile)
					.forEach(x -> files.add(x.normalize().toString()));
			}
		} else if (Files.isRegularFile(path)) {
			files.add(target);
		}
		if (files.size() == 0) {
			throw new IOException("Can not load input files");
		}
		return files;
	}

	public static List<List<String>> readFiles(List<String> files) throws IOException {
		List<List<String>> contents = new ArrayList<>();
		for (int i = 0; i < files.size(); ++i) {
			contents.add(readFile(files.get(i)));
		}
		return contents;
	}

	public static List<String> readFile(String file) throws IOException {
		Path path = Paths.get(file);
		BufferedReader reader = Files.newBufferedReader(path);
		List<String> lines = new ArrayList<>();
		while (true) {
			String line = reader.readLine();
			if (line == null) {
				break;
			}
			lines.add(line);
		}
		return lines;
	}

	public static void writeFile(String file, String content) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(file, false));
		writer.write(content);
		writer.close();
	}

}
