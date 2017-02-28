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

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.loy.e.common.vo.DefaultRespone;
import com.loy.e.core.api.AuthorityService;
import com.loy.upm.sys.domain.entity.ResourceEntity;
import com.loy.upm.sys.domain.entity.RoleEntity;
import com.loy.upm.sys.repository.ResourceRepository;
import com.loy.upm.sys.repository.RoleRepository;

import com.loy.e.common.annotation.Author;
@Author(author = "Loy Fu", website = "http://www.17jee.com", contact = "qq群 540553957")

@RestController(value = "authorityService")
@RequestMapping(value = "**/authority", method = { RequestMethod.POST, RequestMethod.GET })
@Transactional
public class AuthorityServiceImpl implements AuthorityService {
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    ResourceRepository resourceRepository;

    @RequestMapping(value = "/resource", method = { RequestMethod.POST, RequestMethod.GET })
    public DefaultRespone<Map<String, String>> getResource() {
        List<ResourceEntity> resourceEntitys = resourceRepository.findAllResource();
        DefaultRespone<Map<String, String>> defaultRespone = new DefaultRespone<Map<String, String>>(
                true);
        if (resourceEntitys != null && !resourceEntitys.isEmpty()) {
            Map<String, String> resources = new HashMap<String, String>();
            for (ResourceEntity r : resourceEntitys) {
                if (StringUtils.isNotEmpty(r.getAccessCode())) {
                    resources.put(r.getUrl(), r.getId());
                }

            }
            defaultRespone.setData(resources);
            return defaultRespone;
        }
        return defaultRespone;
    }

    @RequestMapping(value = "/roles")
    public DefaultRespone<Set<String>> getResourceRole(String resourceId) {
        List<RoleEntity> roleEntitys = roleRepository.findRoleByResourceId(resourceId);
        DefaultRespone<Set<String>> defaultRespone = new DefaultRespone<Set<String>>(true);
        if (roleEntitys != null && !roleEntitys.isEmpty()) {
            Set<String> roles = new HashSet<String>();
            for (RoleEntity r : roleEntitys) {
                roles.add(r.getId());
            }

            defaultRespone.setData(roles);
            return defaultRespone;
        }
        return defaultRespone;
    }

}
