package edu.mum.bd.m;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@SuppressWarnings({"rawtypes","unchecked"})
public final class Job<K1 extends Comparable<K1>, V1 extends Comparable<V1>, K2 extends Comparable<K2>, V2 extends Comparable<V2>> {

	private static Log log = new Log(3);

	private List<String> input;
	private String output;

	private List<Mapper> mappers;
	private List<Reducer> reducers;
	private Partitioner partitioner;

	private Class<? extends Mapper> mapperClass;
	private Class<? extends Reducer> reducerClass;
	private Class<? extends Partitioner> partitionerClass;

	private int numMapper = 0;
	private int numReducer = 0;

	private Map<Integer, List<String>> mapperInput = new HashMap<>();
	private Map<Integer, List<Pair<K1, V1>>> mapperOutput = new HashMap<>();
	private Map<Integer, List<GroupByPair<K1, V1>>> reducerInput = new HashMap<>();
	private Map<Integer, List<Pair<K2, V2>>> reducerOutput = new HashMap<>();
	private Map<Pair<Integer, Integer>, List<Pair<K1, V1>>> shuffle = new HashMap<>();


	public Job(String input) throws IOException {
		this.input = IO.load(input);
		this.numMapper = this.input.size();
	}

	public Job(String input, String output) throws IOException {
		this(input);
		this.output = output;
	}

	public List<String> getInput() {
		return this.input;
	}

	public String getOutput() {
		return this.output;
	}

	public void setOutput(String output) {
		this.output = output;
	}

	public void setMapperClass(Class<? extends Mapper> clazz) {
		this.mapperClass = clazz;
	}

	public void setReducerClass(Class<? extends Reducer> clazz) {
		this.numReducer = 1;
		this.reducerClass = clazz;
	}
	public void setPartitionerClass(Class<? extends Partitioner> clazz) {
		this.partitionerClass = clazz;
	}

	public void setNumReducer(int numReducer) {
		this.numReducer = numReducer;
	}

	private int recuderIndex(Object key) {
		if (1 < numReducer) {
			return partitioner.getPartition(key);
		}
		return 0;
	}

	private void init() throws Exception {
		log.debug("Number of Input-Splits: "+numMapper);
		log.debug("Number of Reducers: "+numReducer);

		mappers = new ArrayList<>();
		if (0 < numMapper && null != mapperClass) {
			Constructor mapperConstructor = mapperClass.getConstructor();
			for (int i = 0; i < numMapper; ++i) {
				Mapper o = (Mapper)mapperConstructor.newInstance();
				o.init();
				mappers.add(o);
				mapperInput.put(i, new ArrayList<>());
				mapperOutput.put(i, new ArrayList<>());
			}
		}

		reducers = new ArrayList<>();
		if (0 < numReducer && null != reducerClass) {
			Constructor reducerConstructor = reducerClass.getConstructor();
			for (int i = 0; i < numReducer; ++i) {
				Reducer o = (Reducer)reducerConstructor.newInstance();
				o.init();
				reducers.add(o);
				reducerInput.put(i, new ArrayList<>());
				reducerOutput.put(i, new ArrayList<>());
			}
		}

		if (1 < numReducer && null != partitionerClass) {
			partitioner = (Partitioner)partitionerClass.getConstructor(int.class).newInstance(numReducer);
		}

	}

	private Callable<List<Pair<K1, V1>>> invokeMapper(List<String> lines, Mapper mapper) {
		Callable<List<Pair<K1, V1>>> callableTask = () -> {
			long lineId = 0;
			for (String line : lines) {
				mapper.map(lineId++, line);
			}
			mapper.close();
			List<Pair<K1, V1>> rs = mapper.result();
			return rs;
		};
		return callableTask;
	}

	private void doMapper(List<List<String>> files) throws InterruptedException, ExecutionException {
		if (0 == mappers.size()) return;

		int index = 0;
		ExecutorService threadPool = Executors.newFixedThreadPool(numMapper);
		List<Callable<List<Pair<K1, V1>>>> callables = new ArrayList<>();
		for (List<String> lines : files) {
			Mapper mapper = mappers.get(index);
			mapperInput.put(index, lines);
			callables.add(invokeMapper(lines, mapper));
			index++;
		}
		List<Future<List<Pair<K1, V1>>>> futures = threadPool.invokeAll(callables);
		index = 0;
		for (Future<List<Pair<K1, V1>>> future : futures) {
			mapperOutput.put(index, future.get());
			index++;
		}

		threadPool.shutdown();

		// int inputSplitNumber = lines.size() / numMapper;
		// int lineNumber = 0;
		// long lineIndex = 0;
		// int index = 0;
		// log.debug("Mapper "+ index +" input:");
		// for (String line : lines) {
		// 	if (lineNumber == inputSplitNumber) {
		// 		lineNumber = 0;
		// 		if (index < numMapper - 1) {
		// 			log.debug("Mapper "+ index +" input:");
		// 			index++;
		// 		}
		// 	}
		// 	List<String> input = mapperInput.get(index);
		// 	input.add(line);
		// 	mapperInput.put(index, input);
		// 	log.debug(line);
		// 	mappers.get(index).map(lineIndex++, line);
		// 	lineNumber++;
		// }

		// index = 0;
		// for (Mapper mapper : mappers) {
		// 	log.debug("Mapper "+ index +" output:");
		// 	mapper.close();
		// 	List<Pair<K1, V1>> rs = mapper.result();
		// 	rs.stream().forEach(log::debug);
		// 	mapperOutput.put(index, rs);
		// 	index++;
		// }
	}


	private void doReducer() {
		if (0 == reducers.size()) return;

		for (int index : reducerInput.keySet()) {
			Reducer reducer = reducers.get(index);
			List<GroupByPair<K1, V1>> gp = reducerInput.get(index);

			for (GroupByPair<K1, V1> g : gp) {
				reducer.reduce(g);
			}

			reducer.close();
			List<Pair<K2, V2>> rs = reducer.result();
			reducerOutput.put(index, rs);
		}
		// int index = 0;
		// for (Reducer reducer : reducers) {
		// 	reducer.close();
		// 	List<Pair<K2, V2>> rs = reducer.result();
		// 	reducerOutput.put(index, rs);
		// 	index++;
		// }
	}

	private void doSortAndShuffle() {
		try {
			for (int mapperIndex : mapperOutput.keySet()) {
				List<Pair<K1, V1>> output = mapperOutput.get(mapperIndex);
				for (Pair<K1, V1> o : output) {
					int index = recuderIndex(o.getLeft());
					Pair<Integer, Integer> shuffleKey = new Pair<>(mapperIndex, index);
					if (shuffle.containsKey(shuffleKey)) {
						List<Pair<K1, V1>> tmp = shuffle.get(shuffleKey);
						tmp.add(o);
					} else {
						List<Pair<K1, V1>> tmp = new ArrayList<>();
						tmp.add(o);
						shuffle.put(shuffleKey, tmp);
					}
				}
			}

			for (int r = 0; r < numReducer; ++r) {
				List<GroupByPair<K1, V1>> gp = reducerInput.get(r);
				for (int m = 0; m < numMapper; ++m) {
					Pair<Integer, Integer> shuffleKey = new Pair<>(m, r);
					if (shuffle.containsKey(shuffleKey)) {
						List<Pair<K1, V1>> tmp = shuffle.get(shuffleKey);
						for (Pair<K1, V1> p : tmp) {
							GroupByPair<K1, V1> ogp = GroupByPair.find(gp, p.getLeft());
							if (ogp == null) {
								ogp = new GroupByPair(p.getLeft(), p.getRight());
								gp.add(ogp);
							} else {
								ogp.addValue(p.getRight());
							}
						}
					}
				}
				Collections.sort(gp);
			}
		} catch (Exception ex) {
			log.error(ex.getMessage());
		}
	}

	public void run() throws Exception {
		init();
		List<List<String>> files = IO.readFiles(input);
		doMapper(files);
		doSortAndShuffle();
		doReducer();
		writeLog();
	}

	private void writeLog() {
		log.info("Number of Input-Splits: "+numMapper);
		log.info("Number of Reducers: "+numReducer);

		for (int index : mapperInput.keySet()) {
			log.info("Mapper "+ index +" input:");
			List<String> input = mapperInput.get(index);
			input.stream().forEach(log::info);
		}

		for (int index : mapperOutput.keySet()) {
			log.info("Mapper "+ index +" output:");
			List<Pair<K1, V1>> output = mapperOutput.get(index);
			output.stream().forEach(log::info);
		}

		if (numReducer > 1) {
			for (int m = 0; m < numMapper; ++m) {
				for (int r = 0; r < numReducer; ++r) {
					Pair<Integer, Integer> shuffleKey = new Pair<>(m, r);
					log.info("Pairs send from Mapper "+m+" Reducer "+r+":");
					if (shuffle.containsKey(shuffleKey)) {
						List<Pair<K1, V1>> tmp = shuffle.get(shuffleKey);
						tmp.stream().forEach(log::info);
					}
				}
			}
		}

		for (int index : reducerInput.keySet()) {
			log.info("Reducer "+ index +" input:");
			List<GroupByPair<K1, V1>> output = reducerInput.get(index);
			output.stream().forEach(log::info);
		}

		for (int index : reducerOutput.keySet()) {
			log.info("Reducer "+ index +" output:");
			List<Pair<K2, V2>> output = reducerOutput.get(index);
			output.stream().forEach(log::info);
		}
	}

}
