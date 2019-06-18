package simulator.model;

import java.util.List;

import simulator.misc.Vector;

public class NewtonUniversalGravitation implements GravityLaws {

	private static final double G = 6.67E-11;

	public void apply(List<Body> bodies) {

		for (Body body : bodies) {
			if (body.getMass() == 0.0) { // si la masa es 0
				Vector zero = new Vector(body.getAcceleration().dim());
				body.setAcceleration(zero);
				body.setVelocity(zero);
			} else { // si el cuerpo tiene masa calcula la fuerza que el resto de cuerpos ejercen
						// sobre el
				Vector F = new Vector(body.getAcceleration().dim());
				for (Body b : bodies) {
					if (!body.equals(b)) {
						F = F.plus(force(body, b));
					}
				}
				body.setAcceleration(F.scale(1.0 / body.getMass()));
			}
		}

	}

	private Vector force(Body a, Body b) {
		Vector force = new Vector(a.getPosicion().dim());
		Vector aux;
		double f, mass;
		f = (b.getPosicion().distanceTo(a.getPosicion())); // distancia en valor absuluto entre los cuerpos
		mass = a.getMass() * b.getMass(); // masa total
		f = (G * mass) / Math.pow(f, 2); // G*M(a)*M(b) / |a-b|^2

		aux = new Vector(b.getPosicion().minus(a.getPosicion()).direction()); // la direccion que deberia tener 
																				//la aceleracion
		force = aux.scale(f);
		return force;
	}
	public String toString()
	{
		return "Newton universal gravitation law";
	}
}
