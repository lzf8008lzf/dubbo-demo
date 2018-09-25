package com.tunion.cores.result;

import java.io.Serializable;

public class Results implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 错误代码
	 */
	private String status;
	
	/**
	 * 错误描述
	 */
	private String error;
	
	/**
	 * 返回结果对象
	 */
	private Object data;
	
	public Results() {
		super();
	}
	
	public Results(String status, String error, Object data) {
		super();
		this.status = status;
		this.error = error;
		this.data = data;
	}

	public Results(String status, String error) {
		super();
		this.status = status;
		this.error = error;
		this.data = null;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
	
	@Override
	public String toString()
	{
		return null;
	}
	
}
