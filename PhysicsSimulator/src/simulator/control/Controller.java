package simulator.control;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import simulator.factories.Factory;
import simulator.model.Body;
import simulator.model.GravityLaws;
import simulator.model.PhysicsSimulator;
import simulator.view.BodiesTracer;
import simulator.view.SimulatorObserver;

public class Controller {

	PhysicsSimulator _sim; 
	Factory<Body> _bodiesFactory;
	Factory<GravityLaws> _gravityLawsFactory;
	
	
	public Controller(PhysicsSimulator simulator, Factory<Body> _bodyFactory, Factory<GravityLaws> gravityLawsFactory)
	{
		_sim=simulator;
		_bodiesFactory=_bodyFactory;
		_gravityLawsFactory=gravityLawsFactory;
		
	}
	
	public void loadBodies(InputStream in) {
		 JSONObject jsonInupt = new JSONObject(new JSONTokener(in));
		 JSONArray bodies = jsonInupt.getJSONArray("bodies");
		 for (int i = 0; i < bodies.length(); i++)
		 _sim.addBody(_bodiesFactory.createInstance(bodies.getJSONObject(i)));
		}

	public void run(Integer dsteps, OutputStream out) {
		JSONObject states = new JSONObject();
		JSONArray s= new JSONArray();
		s.put(new JSONObject(_sim.toString())); //Initial state
		for(int i=0; i<dsteps;i++)
		{
			_sim.advance();
			s.put(new JSONObject(_sim.toString()));
		}
		states.put("states", s);
		try {
			out.write(states.toString().getBytes());
		} catch(IOException e)
		{
			System.out.print("OutputStream error");
		}
	}
	public Factory<GravityLaws> getGravityLawsFactory()
	{
		return _gravityLawsFactory;
	}
	public void reset()
	{
		_sim.reset();
	}
	public void setDeltaTime(double dt)
	{
		_sim.setDeltaTime(dt);
	}
	public void addObserver(SimulatorObserver o)
	{
		_sim.addObserver(o);
	}
	public void run(int n)// ejecuta n pasos del simulador sin escribir nada en consola.
	{
		for(int i=0;i<n;i++)
		{
			_sim.advance();
		}
			
	}
	public void setGravityLaws(JSONObject info) 
	{
		_sim.setGravityLaws(_gravityLawsFactory.createInstance(info));
	}
	public Factory<Body> getBodiesFactory()
	{
		return this._bodiesFactory;
	}
	public void addBody(JSONObject body)
	{
		_sim.addBody(this._bodiesFactory.createInstance(body));
	}
	public String showTrace()
	{
		return _sim.showTrace();
	}
}
