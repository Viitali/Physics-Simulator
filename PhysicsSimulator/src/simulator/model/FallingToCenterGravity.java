package simulator.model;

import java.util.List;
public class FallingToCenterGravity implements GravityLaws{

	private static final double constG = 9.81;
	
	public void apply(List<Body> bodies) {
		// TODO Auto-generated method stub
		Body body;
		for(int i=0;i<bodies.size();i++)
		{		
			body=bodies.get(i);
			body.setAcceleration(body.getPosicion().direction().scale(-constG));
		}
	}
	public String toString()
	{
		return "Falling to the center of gravity law";
	}
}