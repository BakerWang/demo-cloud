package com.cupdata.service;

import java.util.List;

import com.cupdata.domain.Notify;
import org.springframework.data.domain.Pageable;


/**
 * @author CUPDATA
 * @since 2016年9月10日
 */
public interface AnnounceService {

	List<Notify> load(Pageable pageable, String title);
	
	
}
