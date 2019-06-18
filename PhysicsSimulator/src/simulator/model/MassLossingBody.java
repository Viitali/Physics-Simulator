package simulator.model;

import simulator.misc.Vector;

public class MassLossingBody extends Body{

	protected double lossFactor;
	protected double lossFrequency;
	protected double cont;
	
	public MassLossingBody(String id, Vector v, Vector a , Vector p, double m, double fact, double freq)
	{
		super(id, v, a, p, m);
		this.lossFactor=fact;
		this.lossFrequency=freq;
		cont=0.0;
	}
	
	protected void move(double t)
	{
		super.move(t);
		cont=cont+t;
		
		if(cont>=lossFrequency)
		{
			m = m*(1-lossFactor);
			cont=0.0;
		}
	}
	
}
