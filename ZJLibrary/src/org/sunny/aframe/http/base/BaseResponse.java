package org.sunny.aframe.http.base;

import java.io.Serializable;

/**
 * 通用response类
 * 用于处理返回数据。将数据转化为相应的类
 * @author sunny
 * @version 1.0
 * @created 2014-7-11
 */

public class BaseResponse implements Serializable {

	protected String code;

	protected String desc;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	@Override
	public String toString() {
		return "BaseMsg [code=" + code + ", desc=" + desc + "]";
	}

}
