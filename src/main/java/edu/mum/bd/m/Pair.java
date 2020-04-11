package edu.mum.bd.m;

import java.io.Serializable;
import java.util.Objects;

public class Pair<T extends Comparable<T>, V extends Comparable<V>> implements Serializable, Comparable<Pair<T,V>> {

	private static final long serialVersionUID = -3608777750064813974L;
	private T left;
	private V right;

	public Pair() {
	}

	public Pair(T left, V right) {
		this.left = left;
		this.right = right;
	}

	public T getLeft() {
		return this.left;
	}

	public V getRight() {
		return this.right;
	}

	public void setLeft(T left) {
		this.left = left;
	}

	public void setRight(V right) {
		this.right = right;
	}

	@Override
	public String toString() {
		return "<" + left + "," + right + ">";
	}

	@SuppressWarnings("rawtypes")
	@Override
	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (!(o instanceof Pair)) {
			return false;
		}
		Pair pair = (Pair) o;
		return Objects.equals(left, pair.left) && Objects.equals(right, pair.right);
	}

	@Override
	public int hashCode() {
		return Objects.hash(left, right);
	}

	@Override
	public int compareTo(Pair<T, V> o) {
		int c = left.compareTo(o.left);
		return  c != 0 ? c : right.compareTo(o.right);
	}

}
