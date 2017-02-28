package com.cupdata.service;

import java.util.List;
import java.util.UUID;

import com.cupdata.domain.Notify;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cupdata.udf.jpa.JpaUtil;

/**
 * @author CUPDATA
 * @since 2016年9月10日
 */
@Service
@Transactional(readOnly = true)
public class AnnounceServiceImpl implements AnnounceService {


	@Override
	public List<Notify> load(Pageable pageable, String title) {
		return JpaUtil.linq(Notify.class)
				.equal("type", 0)
				.addIf(title)
					.like("title", "%" + title + "%")
				.endIf()
				.desc("createdAt")
				.list(pageable);
	}

}
