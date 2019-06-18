package simulator.factories;

import org.json.JSONObject;

import simulator.model.GravityLaws;
import simulator.model.NoGravity;

public class NoGravityBuilder extends Builder<GravityLaws> {
	public static final String type = "ng";
	public static final String description = "No gravity law";

	public NoGravityBuilder() {
		this.typeTag = type;
		this.desc = description;
	}

	protected GravityLaws createTheInstance(JSONObject a) {
		return new NoGravity();
	}

}
