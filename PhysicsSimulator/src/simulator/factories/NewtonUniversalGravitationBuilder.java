package simulator.factories;

import org.json.JSONObject;

import simulator.model.GravityLaws;
import simulator.model.NewtonUniversalGravitation;

public class NewtonUniversalGravitationBuilder extends Builder<GravityLaws>{
	public static final String type = "nlug";
	public static final String description = "Newton Universal Gravitation Law";
	public NewtonUniversalGravitationBuilder()
	{
		this.typeTag=type;
		this.desc=description;
	}
	protected GravityLaws createTheInstance(JSONObject a) {
		return new NewtonUniversalGravitation();
	}

}
