/*
 * Copyright   Loy Fu. 付厚俊
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
*/
package com.loy.upm.sys.service.impl;

import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.loy.e.common.properties.NoticeProperties;
import com.loy.e.common.properties.Settings;
import com.loy.e.common.tree.TreeNode;
import com.loy.e.common.vo.IndexData;
import com.loy.e.common.vo.LocaleVO;
import com.loy.e.common.vo.SessionUser;
import com.loy.e.core.annotation.ControllerLogExeTime;
import com.loy.e.core.api.SystemKeyService;
import com.loy.e.core.api.UserBaseService;
import com.loy.upm.sys.domain.entity.ResourceEntity;
import com.loy.upm.sys.domain.entity.ResourceTypeEnum;
import com.loy.upm.sys.domain.entity.UserEntity;
import com.loy.upm.sys.repository.ResourceRepository;
import com.loy.upm.sys.repository.UserRepository;
import com.loy.upm.sys.service.UserResourceService;

import io.swagger.annotations.ApiOperation;

import com.loy.e.common.annotation.Author;
@Author(author = "Loy Fu", website = "http://www.17jee.com", contact = "qq群 540553957")
@RestController

@SuppressWarnings("rawtypes")

public class HomePageService {

    @Autowired
    UserBaseService userBaseService;

    @Autowired
    SystemKeyService systemKeyService;

    @Autowired
    Settings settings;

    @Autowired
    UserResourceService userResourceService;
    @Autowired
    UserRepository userRepository;

    @Autowired
    ResourceRepository resourceRepository;
    @Autowired
    NoticeProperties noticeProperties;

    @RequestMapping(value = "**/indexData", method = { RequestMethod.POST, RequestMethod.GET })
    @ControllerLogExeTime(description = "主页数据", log = false)

    @ApiOperation(value = "获取主面数据", notes = "获取主面数据", httpMethod = "GET")
    public IndexData getIndexData(String lang) {
        IndexData indexData = new IndexData();
        String defaultPage = settings.getDefaultPage();
        if (StringUtils.isNotEmpty(defaultPage)) {
            indexData.setDefaultPage(defaultPage);
        }
        SessionUser simipleUser = userBaseService.getSessionUser();
        if (StringUtils.isEmpty(lang)) {
            lang = "zh_CN";
        }
        UserEntity user = userRepository.get(simipleUser.getId());
        Boolean photo = user.getPhoto();
        indexData.setPhoto(photo == null ? false : photo);
        indexData.setSimipleUser(simipleUser);

        List<String> sls = settings.getSupportLocales();
        List<LocaleVO> supportLocalesList = indexData.getSupportLocales();
        for (String lan : sls) {
            String[] temp = lan.split("_");
            Locale locale = new Locale(temp[0], temp[1]);
            String displayName = locale.getDisplayLanguage(LocaleContextHolder.getLocale());
            LocaleVO localeVO = new LocaleVO();
            localeVO.setCountry(temp[1]);
            localeVO.setLanguage(temp[0]);
            localeVO.setDisplayName(displayName);
            supportLocalesList.add(localeVO);
        }

        List<TreeNode> menuData = userResourceService.getMenuByUsername(
                userBaseService.getSessionUser().getUsername(), lang);

        indexData.setMenuData(menuData);
        Map<String, Boolean> accessCodes = indexData.getAccessCodes();
        List<ResourceEntity> list = resourceRepository.findAll();
        List<ResourceEntity> userResource = resourceRepository
                .findResourceByUsername(simipleUser.username);
        if (list != null) {
            Iterator<ResourceEntity> it = list.iterator();
            while (it.hasNext()) {
                ResourceEntity r = it.next();
                if (r.getResourceType() == ResourceTypeEnum.MENU
                        || StringUtils.isEmpty(r.getAccessCode())) {
                    it.remove();
                } else {
                    accessCodes.put(r.getAccessCode(), false);
                }
            }
        }

        if (userResource != null) {
            Iterator<ResourceEntity> it = userResource.iterator();
            while (it.hasNext()) {
                ResourceEntity r = it.next();
                if (r.getResourceType() == ResourceTypeEnum.MENU
                        || StringUtils.isEmpty(r.getAccessCode())) {
                    it.remove();
                } else {
                    accessCodes.put(r.getAccessCode(), true);
                }
            }
        }
        indexData.setNoticeProperties(this.noticeProperties);
        return indexData;
    }

}
