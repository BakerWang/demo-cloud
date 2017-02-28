package com.cupdata.controller;

import java.util.Date;
import java.util.List;

import com.cupdata.domain.Notify;
import com.cupdata.service.AnnounceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * @author CUPDATA
 * @since 2016年9月4日
 */
@Controller
public class AnnounceController {
	
	@Autowired
	private AnnounceService announceService;
	
	@RequestMapping(path = "/api/announce/load", method = RequestMethod.GET)
	@ResponseBody
	public List<Notify> load(Pageable pageable, String title) {
		return announceService.load(pageable, title);
	}

}
