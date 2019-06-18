package simulator.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import simulator.control.Controller;
import simulator.misc.Vector;
import simulator.model.Body;

public class BodiesTracer implements SimulatorObserver{

	private HashMap<String,ArrayList<Vector>> data;
	public BodiesTracer(Controller ctrl)
	{
	ctrl.addObserver(this);	
	}
	@Override
	public void onRegister(List<Body> bodies, double time, double dt, String gLawsDesc) {
		data = new HashMap<String , ArrayList<Vector>>();
		for (Body b : bodies)
		{
			ArrayList<Vector> vec = new ArrayList<>();
			vec.add(b.getPosicion());
			data.put(b.getId(), vec);
		}
		
	}

	@Override
	public void onReset(List<Body> bodies, double time, double dt, String gLawsDesc) {
		data.clear();
	}

	@Override
	public void onBodyAdded(List<Body> bodies, Body b) {
		ArrayList<Vector> vec = new ArrayList<>();
		vec.add(b.getPosicion());
		data.put(b.getId(), vec);
		
	}

	@Override
	public void onAdvance(List<Body> bodies, double time) {
		for(Body b : bodies)
		{
			ArrayList<Vector> aux = data.get(b.getId());
			aux.add(b.getPosicion());
			data.put(b.getId(), aux);
		}
		
	}

	@Override
	public void onDeltaTimeChanged(double dt) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onGravityLawChanged(String gLawsDesc) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public String toString()
	{
		StringBuilder str = new StringBuilder();
		for(Map.Entry<String, ArrayList<Vector>> entry : data.entrySet())
			str.append(entry.getKey() +" -> Positions" + entry.getValue() + '\n');
		return str.toString();
	}

}
