package edu.mum.bd.m;

public class Log {

	private int level;

	public Log(int level) {
		this.level = level;
	}

	public void write(String x, int level) {
		if (this.level < level) return;
		System.out.println(x);
	}

	public void write(Object x, int level) {
		write(x.toString(), level);
	}

	public void error(Object x) {
		write("[ERROR]: "+x, 1);
	}

	public void warning(Object x) {
		write("[WARNING]: "+x, 2);
	}

	public void info(Object x) {
		write(x, 3);
	}

	public void debug(Object x) {
		write(x, 4);
	}

}
