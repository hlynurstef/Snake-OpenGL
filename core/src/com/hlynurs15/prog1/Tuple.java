package com.hlynurs15.prog1;

public class Tuple<X,Y> {
	private X x;
	private Y y;
	
	public Tuple(X x, Y y) {
		this.x = x;
		this.y = y;
	}
	
	public Tuple(Tuple<X,Y> tuple) {
		this.x = tuple.x;
		this.y = tuple.y;
	}
	
	public X getX() { return x; }
	public Y getY() { return y; }
	public void setX(X x) { this.x = x; }
	public void setY(Y y) { this.y = y; }
	
	@Override
	public int hashCode() { return x.hashCode() ^ y.hashCode(); }
	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Tuple)) {
			return false;
		}
		@SuppressWarnings("unchecked")
		Tuple<X,Y> tuple = (Tuple<X,Y>) o;
		return this.x.equals(tuple.getX()) && this.y.equals(tuple.getY());
	}
}
