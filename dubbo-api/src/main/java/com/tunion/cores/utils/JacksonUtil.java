package com.tunion.cores.utils;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class JacksonUtil {
	private ObjectMapper objectMapper = null;

	private static JacksonUtil jsonUtil= new JacksonUtil();
	
	public static String getJackson(Object obj)
	{
		return jsonUtil.getJson(obj);
	}
	
	public static Object getJacksonObj(String json, Class clazz)
	{
		return jsonUtil.getObj(json, clazz);
	}

	public JacksonUtil() {
		objectMapper = new ObjectMapper();
	}

	public String getJson(Object obj) {
		try {
			objectMapper.setSerializationInclusion(Include.NON_NULL);
			return objectMapper.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Object getObj(String json, Class clazz){
		try {
			return objectMapper.readValue(json, clazz);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public String getAllJson(Object obj) {
		try {
			return objectMapper.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/*
	 * 获取类的所有字段信息，单元测试需要
	 */
	public static String getAllJackson(Object obj)
	{
		return jsonUtil.getAllJson(obj);
	}
}
