package edu.mum.bd.m;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GroupByPair<K extends Comparable<K>, V> implements Serializable, Comparable<GroupByPair<K, V>> {

	private static final long serialVersionUID = -3608777750064813975L;

	private K key;
	private List<V> values;

	public GroupByPair() {
	}

	public GroupByPair(K key, List<V> values) {
		this.key = key;
		this.values = values;
	}

	public GroupByPair(K key, V value) {
		this.key = key;
		this.values = new ArrayList<>();
		this.values.add(value);
	}

	public K getKey() {
		return this.key;
	}

	public void setKey(K key) {
		this.key = key;
	}

	public List<V> getValues() {
		return this.values;
	}

	public void setValues(List<V> values) {
		this.values = values;
	}

	public void addValue(V value) {
		this.values.add(value);
	}

	@Override
	public String toString() {
		return "<" + getKey() + "," + getValues() + ">";
	}

	@Override
	public int hashCode() {
		return Objects.hash(key, values);
	}

	@Override
	public int compareTo(GroupByPair<K, V> o) {
		return key.compareTo(o.getKey());
	}

	public static  <K extends Comparable<K>, V>  GroupByPair<K, V> find(List<GroupByPair<K, V>> ls, K key) {
		GroupByPair<K, V> p = ls.stream()
			.filter(o -> key.equals(o.getKey()))
			.findFirst()
			.orElse(null);
		return p;
	}

	public static <K extends Comparable<K>, V> int binarySearch(List<GroupByPair<K, V>> input, K key) {
		int first = 0;
        int last = input.size() - 1;
        int middle = (first + last) / 2;
        while (first <= last) {
			int compare = input.get(middle).getKey().compareTo(key);
            if (compare > 0) {
                first = middle + 1;
            } else if (compare == 0) {
				return middle;
            } else {
                last = middle - 1;
            }
            middle = (first + last) / 2;
        }
		return -1;
	}
}
