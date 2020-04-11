package edu.mum.bd.m;

public abstract class Partitioner<K> {

	protected int numReducer;

	public Partitioner(int num) {
		numReducer = num;
	}

	public abstract int getPartition(K key);

}
