package com.cupdata.udf.jpa.transform;
/**
 * 查询结果转换器
 * @author CUPDATA
 * @since 2016年2月21日
 */
public interface ResultTransformer {

	public Object transformTuple(Object[] tuple, String[] aliases);
}
