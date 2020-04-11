package edu.mum.bd;

import java.util.List;

import edu.mum.bd.m.*;

public class W1D2B {

	public static void main(String[] args) throws Exception {
		Job<String, Long, String, Long> job = new Job<>(args[0]);
		job.setMapperClass(MyMapper.class);
		job.setReducerClass(MyReducer.class);
		job.setPartitionerClass(MyPartitioner.class);
		job.setNumReducer(4);
		job.run();
	}

	public static class MyPartitioner extends Partitioner<String> {

		public MyPartitioner(int num) {
			super(num);
		}

		@Override
		public int getPartition(String key) {
			return (int) Math.abs(key.hashCode()) % numReducer;
		}

	}

	public static class MyMapper extends Mapper<Long, String, String, Long> {

		public MyMapper() {}

		@Override
		public void map(Long lineNumber, String line) {
			List<String> words = Util.tokenizeSplit(line);
			for (String word: words) {
				emit(new Pair<String, Long>(word, 1L));
			}
		}

	}

	public static class MyReducer extends Reducer<String, Long, String, Long> {

		public MyReducer() {}

		@Override
		public void reduce(GroupByPair<String, Long> v) {
			long sum = 0;
			List<Long> values = v.getValues();
			for (Long value : values) {
				sum = sum + value.longValue();
			}
			emit(new Pair<>(v.getKey(), sum));
		}

	}
}
