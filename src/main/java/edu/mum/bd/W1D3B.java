package edu.mum.bd;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.mum.bd.m.GroupByPair;
import edu.mum.bd.m.Job;
import edu.mum.bd.m.Log;
import edu.mum.bd.m.Mapper;
import edu.mum.bd.m.Pair;
import edu.mum.bd.m.Partitioner;
import edu.mum.bd.m.Reducer;
import edu.mum.bd.m.Util;

public class W1D3B {

	public static class MyPartitioner extends Partitioner<String> {

		public MyPartitioner(int num) {
			super(num);
		}

		@Override
		public int getPartition(String key) {
			return (int) Math.abs(key.hashCode()) % numReducer;
		}

	}

	public static class MyMapper extends Mapper<Long, String, String, Pair<Long, Long>> {

		private static Log log = new Log(3);
		private Map<String, Pair<Long, Long>> m = new HashMap<>();

		public MyMapper() {}

		@Override
		public void map(Long lineNumber, String line) {
			log.info("Thead name:" + Thread.currentThread().getName());
			List<String> words = Util.tokenizeSplit(line);
			for (String word: words) {
				String firstLetter = word.substring(0, 1).toLowerCase();
				if (m.containsKey(firstLetter)) {
					Pair<Long, Long> tmp = m.get(firstLetter);
					tmp.setLeft(tmp.getLeft() + word.length());
					tmp.setRight(tmp.getRight() + 1L);
				} else {
					m.put(firstLetter, new Pair<Long, Long>((long)word.length(), 1L));
				}
			}
		}

		public void close() {
			log.info("Thead name:" + Thread.currentThread().getName());
			for (String k : m.keySet()) {
				emit(new Pair<String, Pair<Long, Long>>(k, m.get(k)));
			}
		}

	}

	public static class MyReducer extends Reducer<String, Pair<Long, Long>, String, Double> {

		public MyReducer() {}

		@Override
		public void reduce(GroupByPair<String, Pair<Long, Long>> v) {
			double sum = 0.0;
			double count = 0.0;
			for (Pair<Long, Long> value : v.getValues()) {
				sum = sum + value.getLeft();
				count = count + value.getRight();
			}
			emit(new Pair<>(v.getKey(), sum / count));
		}

	}

	public static void main(String[] args) throws Exception {
		Job<String, Pair<Long, Long>, String, Double> job = new Job<>(args[0]);
		job.setMapperClass(MyMapper.class);
		job.setReducerClass(MyReducer.class);
		job.setPartitionerClass(MyPartitioner.class);
		job.setNumReducer(3);
		job.run();
	}

}
