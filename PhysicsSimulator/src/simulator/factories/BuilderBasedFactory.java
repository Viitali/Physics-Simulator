package simulator.factories;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

public class BuilderBasedFactory<T> implements Factory<T>{ 
	
	List<Builder<T>> builders;
	List<JSONObject> factoryElements;
	
	public BuilderBasedFactory(List<Builder<T>> cuerpos)
	{
		builders = new ArrayList<Builder<T>>();
		factoryElements = new ArrayList<JSONObject>();
		
		builders=cuerpos;
		for(int i=0;i<builders.size();i++)
		{
			factoryElements.add(builders.get(i).getBuilderInfo());
		}
	}
	public T createInstance(JSONObject info) { 
		try {
			for(int i=0;i<builders.size();i++)
			{
				if(builders.get(i).createInstance(info)!=null)
					return builders.get(i).createInstance(info);
			}
			return null;
		}
		catch(JSONException e){
			throw new IllegalArgumentException("No se ha podido parsear correctamente");
		}
	}
	public List<JSONObject> getInfo() {
		return this.factoryElements;
	}
}
