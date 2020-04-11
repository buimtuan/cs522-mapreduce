package edu.mum.bd.m;

import java.util.ArrayList;
import java.util.List;

public abstract class Reducer<K1 extends Comparable<K1>, V1, K2 extends Comparable<K2>, V2 extends Comparable<V2>>  {

	private List<Pair<K2, V2>> rs = new ArrayList<>();

	public void init() {
	}
	public abstract void reduce(GroupByPair<K1, V1> v);
	public List<Pair<K2, V2>> result() {
		return rs;
	}

	protected void emit(Pair<K2, V2> v) {
		rs.add(v);
	}
	public void close() {}
}
