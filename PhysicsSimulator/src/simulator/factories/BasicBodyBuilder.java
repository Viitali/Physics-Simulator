package simulator.factories;
import org.json.JSONException;
import org.json.JSONObject;
import simulator.misc.Vector;
import simulator.model.Body;

public class BasicBodyBuilder extends Builder<Body>{
	public static final String type = "basic";
	public static final String description = "Basic Body";

	public BasicBodyBuilder()
	{
		this.typeTag = type;
		this.desc = description;
	}
	protected Body createTheInstance(JSONObject a) {
		try 
		{
		JSONObject data = a;
		String id = data.getString("id");
		Vector pos = new Vector(jsonArrayTodoubleArray(data.getJSONArray("pos")));
		Vector vel = new Vector(jsonArrayTodoubleArray(data.getJSONArray("vel")));
		double mass = data.getDouble("mass");
		Vector acce = new Vector(vel.dim());
		
		return new Body(id,vel,acce,pos,mass);
		
		
		} catch(JSONException e) {
			throw new IllegalArgumentException("Los datos estan mal");
		}
	}
	protected JSONObject createData()
	{
		JSONObject a = new JSONObject ();
		a.put("id", "The id");
		a.put("pos", "Position vector");
		a.put("vel", "Velocity vector");
		a.put("mass", "Mass");
		return a;
	}

}
