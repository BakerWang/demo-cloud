package com.cupdata.udf.jpa;

import javax.persistence.Transient;

/**
 * @author CUPDATA
 * @since 2016年9月18日
 */
public class AdditionalSupport {

	@Transient
	private Object additional;

	public Object getAdditional() {
		return additional;
	}

	public void setAdditional(Object additional) {
		this.additional = additional;
	}
	
	
}
