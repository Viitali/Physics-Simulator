package simulator.factories;
import org.json.JSONArray;
import org.json.JSONObject;

public abstract class Builder<T>{

	protected String typeTag;
	protected String desc;
	
	public Builder()
	{
		
	}
	
	protected abstract T createTheInstance(JSONObject a);
	
	
	protected double[] jsonArrayTodoubleArray(JSONArray a)
	{
		double[] array = new double[a.length()];
		for(int i=0;i<a.length();i++)
		{
			array[i]=a.getDouble(i);
		}
		return array;
	}
	
	public T createInstance(JSONObject info) {
		T b = null;
		if (typeTag != null && typeTag.equals(info.getString("type")))
			b = createTheInstance(info.getJSONObject("data"));
		return b;
	}
	public JSONObject getBuilderInfo() {
		 JSONObject info = new JSONObject();
		 info.put("type", typeTag);
		 info.put("data", createData());
		 info.put("desc", desc);
		 return info;
	}
	protected JSONObject createData()
	{
		return new JSONObject();
	}
	public String toString()
	{
		return desc + " ( "+typeTag+" )";
	}
}
