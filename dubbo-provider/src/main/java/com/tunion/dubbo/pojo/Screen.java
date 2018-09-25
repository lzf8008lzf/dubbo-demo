package com.tunion.dubbo.pojo;

import java.io.Serializable;

/**
 * 影厅
 *
 */
public class Screen implements Serializable{
	private String screenCode;			//影厅序号
	private String screenName;			//影厅名称
	private int seatNum;					//座位总数
	private String type;					//影厅类型 (系统方默认厅类型)
	
	public String getScreenCode() {
		return screenCode;
	}
	public void setScreenCode(String screenCode) {
		this.screenCode = screenCode;
	}
	public String getScreenName() {
		return screenName;
	}
	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}
	public int getSeatNum() {
		return seatNum;
	}
	public void setSeatNum(int seatNum) {
		this.seatNum = seatNum;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
}
