package com.bofa.handlenullInjson;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class GSONNullCheck {

	public static void main(String[] args) {

		String jsonString = "{\"id\":1 , \"data\":null, \"data2\":\"\", " +
		    "\"data3\":{\"id\":1 , \"data\":null, \"data2\":\"\" } , " +
				" \"array\":[\"n1\",\"n2\" , null]," + 
				" \"array2\":[{\"id\":1 , \"data\":null, \"data2\":\"\" }]" +
		    "}";

		Gson gson = new Gson();
		JsonObject emps = gson.fromJson(jsonString, JsonObject.class);

		String json = gson.toJson(emps);
		System.out.println(json);
		//output {"id":1,"data2":"","data3":{"id":1,"data2":""},"array":["n1","n2",null],"array2":[{"id":1,"data2":""}]}
 
	}
}
