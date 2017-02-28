package com.loy.e.core.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.loy.e.common.annotation.Author;

@RestController
@RequestMapping(value = "/", method = { RequestMethod.GET })

public class BundleService {
    protected final Log LOGGER = LogFactory.getLog(BundleService.class);

    @RequestMapping(value = "**/i18n/**/**.properties")

    public void file(HttpServletRequest request, HttpServletResponse response) {
        String uri = request.getRequestURI();
        int begin = uri.indexOf("i18n");
        uri = uri.substring(begin, uri.length());
        try {
            response.setCharacterEncoding("UTF-8");
            ClassPathResource classPathResource = new ClassPathResource(uri);
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(classPathResource.getInputStream(), "UTF-8"));
            FileCopyUtils.copy(br, response.getWriter());
        } catch (Exception e) {

        }
    }
}
