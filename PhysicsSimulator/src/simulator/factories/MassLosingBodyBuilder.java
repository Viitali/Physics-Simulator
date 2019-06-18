package simulator.factories;

import org.json.JSONException;
import org.json.JSONObject;

import simulator.misc.Vector;
import simulator.model.Body;
import simulator.model.MassLossingBody;

public class MassLosingBodyBuilder extends Builder<Body>{
	public static final String type = "mlb";
	public static final String description = "Mass losing body";

	public MassLosingBodyBuilder()
	{
		this.typeTag=type;
		this.desc=description;	
	}
	@Override
	protected Body createTheInstance(JSONObject a) {
		try 
		{
		JSONObject data = a;
		String id = data.getString("id");
		Vector pos = new Vector(jsonArrayTodoubleArray(data.getJSONArray("pos")));
		Vector vel = new Vector(jsonArrayTodoubleArray(data.getJSONArray("vel")));
		double mass = data.getDouble("mass");
		double[] aux= {0,0};
		Vector acce = new Vector(aux);
		double freq = data.getDouble("freq");
		double factor= data.getDouble("factor");
		
		return new MassLossingBody(id,vel,acce,pos,mass,factor,freq);
		
		
		} catch(JSONException e) {
			throw new IllegalArgumentException("Los datos estan mal");
		}
	}
	protected JSONObject createData()
	{
		JSONObject a = new JSONObject ();
		a.put("id", "id");
		a.put("pos", "Position vector");
		a.put("vel", "Velocity vector");
		a.put("mass", "Mass");
		a.put("freq", "Frequencia");
		a.put("factor", "the factor");
		return a;
		
	}

}
