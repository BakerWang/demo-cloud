package com.cupdata.udf.jpa.strategy;

import javax.persistence.EntityManagerFactory;

/**
 * @author CUPDATA
 * @since 2016年8月13日
 */
public interface GetEntityManagerFactoryStrategy {
	
	EntityManagerFactory getEntityManagerFactory(Class<?> domainClass);

}
