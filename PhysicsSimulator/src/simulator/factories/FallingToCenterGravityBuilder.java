package simulator.factories;

import org.json.JSONObject;

import simulator.model.FallingToCenterGravity;
import simulator.model.GravityLaws;

public class FallingToCenterGravityBuilder extends Builder<GravityLaws>{

	public static final String type = "ftcg";
	public static final String description = "Falling to center gravity";

	public FallingToCenterGravityBuilder()
	{
		this.typeTag=type;
		this.desc=description;
	}
	
	protected GravityLaws createTheInstance(JSONObject a) {
		return new FallingToCenterGravity();
	}

}
