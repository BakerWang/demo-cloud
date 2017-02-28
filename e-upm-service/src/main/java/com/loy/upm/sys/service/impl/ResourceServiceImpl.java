package com.loy.upm.sys.service.impl;

import com.loy.e.common.tree.TreeNode;
import com.loy.e.common.tree.TreeUtil;
import com.loy.e.core.annotation.ControllerLogExeTime;
import com.loy.upm.sys.domain.ResourceQueryParam;
import com.loy.upm.sys.domain.RoleQueryParam;
import com.loy.upm.sys.domain.entity.MenuI18nEntity;
import com.loy.upm.sys.domain.entity.ResourceEntity;
import com.loy.upm.sys.domain.entity.RoleEntity;
import com.loy.upm.sys.repository.MenuI18nRepository;
import com.loy.upm.sys.repository.ResourceRepository;
import com.loy.upm.sys.service.UserResourceService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.transaction.Transactional;

@RestController
@RequestMapping(value = "**/resource", method = { RequestMethod.POST, RequestMethod.GET })
@Transactional
public class ResourceServiceImpl {

    @Autowired
    ResourceRepository resourceRepository;

    @ControllerLogExeTime(description = "分页查询角色", log = false)
    @RequestMapping(value = "/page")
    public Page<ResourceEntity> queryPage(@ApiIgnore ResourceQueryParam resourceQueryParam,
                                      @ApiIgnore Pageable pageable) {
        Page<ResourceEntity> page = resourceRepository.queryResourcePage(resourceQueryParam, pageable);
        return page;
    }

}
