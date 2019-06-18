package simulator.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.model.Body;
import simulator.model.GravityLaws;
import simulator.view.SimulatorObserver;

public class PhysicsSimulator {

	private GravityLaws _gravityLaws; // leyes de la gravedad a aplicar
	private List<Body> _bodies; // cuerpos de la simulación
	private Set<SimulatorObserver> _observers; //lista de observadores
	private double _time; // incremento del tiempo.
	private double stepTime; // número de pasos que se ejecuta la simulación.

	public PhysicsSimulator(GravityLaws _gLaws, double _st) {
		if (_st <= 0)
			throw new IllegalArgumentException("Incorrect step time");
		if (_gLaws == null)
			throw new IllegalArgumentException("Gravity law needed");
		this._gravityLaws = _gLaws;
		this.stepTime = _st;
		_time = 0.0;
		_bodies = new ArrayList<Body>();
		_observers = new HashSet<>();
	}

	public void addBody(Body b) {
		boolean alreadyExists = false;
		int i = 0;
		while (i < _bodies.size() && !alreadyExists)// comprueba que ese cuerpo no este ya
		{
			if (_bodies.get(i).equals(b))
				alreadyExists = true;
			i++;
		}
		if (!alreadyExists)
		{
			_bodies.add(b);
			for(SimulatorObserver o : _observers )
				o.onBodyAdded(_bodies, b);
		}
	}

	public void advance() {
		/*
		 * Aplica la leyes de la gravedad a los cuerpos. Mueve todos los cuerpos E
		 * incrementa el tiempo.
		 */
		_gravityLaws.apply(_bodies);
		boolean collision=false;
		for (int i = 0; i < _bodies.size(); i++) {
			_bodies.get(i).move(stepTime);
			for(SimulatorObserver o : _observers )
				o.onAdvance(_bodies, _time);
		}
		_time += stepTime;
	}

	public String toString() {
		JSONObject info = new JSONObject();
		JSONArray bodies = new JSONArray();

		info.put("time", _time);
		for (int i = 0; i < _bodies.size(); i++) {
			bodies.put(new JSONObject(_bodies.get(i).toString()));
		}
		info.put("bodies", bodies);
		return info.toString();
	}

	public void reset()
	{
		this._bodies.clear();
		this._time=0.0;
		for(SimulatorObserver o : _observers )
			o.onReset(_bodies, _time, stepTime, _gravityLaws.toString());
	}
	public void setDeltaTime(double dt) {
		if(dt<0)
			throw new IllegalArgumentException("Invalid Delta time");
		else
		{
			this.stepTime=dt;
			for(SimulatorObserver o : _observers )
				o.onDeltaTimeChanged(dt);
		}
			
	}
	public void setGravityLaws(GravityLaws gravityLaws) {
		if(gravityLaws==null)
			throw new IllegalArgumentException("Invalid gravity law");
		else
		{
			this._gravityLaws=gravityLaws;
			for(SimulatorObserver o : _observers)
				o.onGravityLawChanged(_gravityLaws.toString());
		}
	}
	public void addObserver(SimulatorObserver o)
	{
			_observers.add(o);
			o.onRegister(_bodies, _time, stepTime, _gravityLaws.toString());
	}
	public String showTrace()
	{
		return _observers.toString();
	}
}
