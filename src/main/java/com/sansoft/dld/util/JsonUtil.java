package com.sansoft.dld.util;

import java.util.List;

import com.google.gson.Gson;

public class JsonUtil {
	Gson gson;

	public Gson getGson() {
		return gson;
	}

	public void setGson(Gson gson) {
		this.gson = gson;
	}
	
	public String getJsonObject(Object object) {
		return gson.toJson(object);
	}
	
	public String getJsonListOject(List<Object> listObject){
		//gson.to
		for(Object object: listObject){
			
		}
		return null;
	}
}
