package simulator.model;

import simulator.misc.Vector;

public class Body {
	protected String id;
	protected Vector v;
	protected Vector a;
	protected Vector p;
	protected double m;

	public Body(String id, Vector v, Vector a, Vector p, double m) {
		this.id = id;
		this.v = v;
		this.a = a;
		this.p = p;
		this.m = m;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public double getMass() {
		return m;
	}

	public void setMass(double m) {
		this.m = m;
	}

	public Vector getVelocity() {
		return new Vector(v);
	}

	public void setVelocity(Vector v) {
		this.v = new Vector(v);
	}

	public Vector getAcceleration() {
		return new Vector(a);
	}

	public void setAcceleration(Vector a) {
		this.a = new Vector(a);
	}

	public Vector getPosicion() {
		return new Vector(p);
	}

	public void setPosicion(Vector p) {
		this.p = new Vector(p);
	}

	protected void move(double t) {
		Vector aux1 = v.scale(t);//v*t
		Vector aux2 = a.scale(t * t * 0.5); // a*t^2*0.5
		aux1 = aux1.plus(aux2); // v*t + 0.5*a*t^2
		this.p = p.plus(aux1); // p = p + v*t+0.5*a*t^2

		aux1 = a.scale(t); // a*t
		this.v = v.plus(aux1); // v = v + a*t
	}

	public boolean equals(Body b) {
		return id == b.getId() && m == b.getMass(); //&& a == b.getAcceleration() && v == b.getVelocity()
				//&& p == b.getPosicion();
	}

	public String toString() {
		return ("{ \"id\": " + id + ", \"mass\": " + m + ", \"pos\": " + p + ", \"vel\": " + v + ", \"acc\": " + a
				+ " } ");
	}
}
