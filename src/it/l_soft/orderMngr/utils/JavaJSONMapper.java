package it.l_soft.orderMngr.utils;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;
import javax.json.stream.JsonGenerationException;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

//import org.codehaus.jackson.annotate.JsonIgnoreProperties;
//import org.codehaus.jackson.map.JsonMappingException;
//import org.codehaus.jackson.map.ObjectMapper;


public class JavaJSONMapper {
	public static void JavaToJSON(Object object, Class<?> objClass)
	{
	}
	
	public static JsonObject StringToJSON(String jsonString)
	{
		JsonReader jsonReader = Json.createReader(new StringReader(jsonString));
		JsonObject jObj = jsonReader.readObject();
		jsonReader.close();
		return jObj; 
	}
	
	public static Object JSONToJava(JsonObject jsonIn, Class<?> objClass)
	{
		Object object = null;
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		try
		{
			//	    	  PrintWriter out = new PrintWriter("./tmpjson");
			//	    	  out.println(jsonIn.toString());
			//	    	  out.close();
			//		         object =  mapper.readValue(new File("./tmpjson"), objectClass);
			object =  mapper.readValue(jsonIn.toString(), objClass);
		} catch (JsonGenerationException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		return(object);
	}
	
	public static ArrayList<Object> JSONArrayToJava(JsonArray jsonIn, Class<?> objClass)
	{
		ArrayList<Object> objectArray = new ArrayList<Object>();
		for (JsonValue object : jsonIn) {
			objectArray.add(JSONToJava((JsonObject) object, objClass));
		}
		return objectArray;
		
	}
}
