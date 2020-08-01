package com.tunion.cores.utils;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @program: yueenjoy
 * @description: Jackson 工具类
 * @author: LiZhaofu
 * @create: 2020-04-26 14:41
 **/

public class JacksonUtil {

	private static Logger log = LoggerFactory.getLogger(JacksonUtil.class);

	private final static ObjectMapper objectMapper = new ObjectMapper();
	public static ObjectMapper getInstance() {
		return objectMapper;
	}

	/**
	 * bean、array、List、Map --> json
	 *
	 * @param obj
	 * @return json string
	 * @throws Exception
	 */
	public static String toJson(Object obj) {
		try {
			return getInstance().writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}

	/**
	 * string --> bean、Map、List(array)
	 *
	 * @param jsonStr
	 * @param clazz
	 * @return obj
	 * @throws Exception
	 */
	public static <T> T toObject(String jsonStr, Class<T> clazz) {
		try {
			return getInstance().readValue(jsonStr, clazz);
		} catch (JsonParseException e) {
			log.error(e.getMessage(), e);
		} catch (JsonMappingException e) {
			log.error(e.getMessage(), e);
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}
}
