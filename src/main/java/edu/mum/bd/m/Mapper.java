package edu.mum.bd.m;

import java.util.ArrayList;
import java.util.List;

public abstract class Mapper<K1, V1, K2 extends Comparable<K2>, V2 extends Comparable<V2>> {

	private List<Pair<K2, V2>> rs = new ArrayList<>();

	public void init() {}

	public List<Pair<K2, V2>> result() {
		return rs;
	}

	public void close() {}

	protected void emit(Pair<K2, V2> v) {
		rs.add(v);
	}

	public abstract void map(K1 k1, V1 v1);
}
