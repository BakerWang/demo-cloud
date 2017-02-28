package com.loy.upm.sys.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.loy.e.common.tree.TreeNode;
import com.loy.e.common.tree.TreeUtil;
import com.loy.upm.sys.domain.entity.MenuI18nEntity;
import com.loy.upm.sys.domain.entity.ResourceEntity;
import com.loy.upm.sys.repository.MenuI18nRepository;
import com.loy.upm.sys.repository.ResourceRepository;
import com.loy.upm.sys.service.UserResourceService;

import com.loy.e.common.annotation.Author;
@Author(author = "Loy Fu", website = "http://www.17jee.com", contact = "qq群 540553957")
@Service
@Transactional
public class UserResourceServiceImpl implements UserResourceService {

    @Autowired
    ResourceRepository resourceRepository;
    @Autowired
    MenuI18nRepository menuI18nRepository;

    @SuppressWarnings("rawtypes")
    public List<TreeNode> getMenuByUsername(String username, String lang) {
        List<ResourceEntity> list = resourceRepository.findMenuByUsername(username);
        if (lang == null) {
            lang = "";
        }
        for (ResourceEntity resourceEntity : list) {
            String key = resourceEntity.getLableKey();

            resourceEntity.getData().setName(resourceEntity.getName());
            if (StringUtils.isNotEmpty(key)) {
                MenuI18nEntity menuI18nEntity = menuI18nRepository.findByKeyAndLang(key, lang);
                if (menuI18nEntity == null && !lang.equals("")) {
                    menuI18nEntity = menuI18nRepository.findByKeyAndLang(key, "");
                }
                if (menuI18nEntity != null) {
                    String value = menuI18nEntity.getValue();
                    if (StringUtils.isNotEmpty(value)) {
                        resourceEntity.getData().setName(value);
                    }
                }
            }
        }

        List<TreeNode> menuData = TreeUtil.build(list);
        return menuData;
    }

}
