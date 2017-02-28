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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.loy.upm.flow.service.FlowUserGroupService;
import com.loy.upm.flow.vo.FlowGroup;
import com.loy.upm.flow.vo.FlowUser;
import com.loy.upm.sys.domain.entity.RoleEntity;
import com.loy.upm.sys.domain.entity.UserEntity;
import com.loy.upm.sys.repository.UserRepository;

import com.loy.e.common.annotation.Author;
@Author(author = "Loy Fu", website = "http://www.17jee.com", contact = "qq群 540553957")

@Service(value = "flowUserGroupService")
@Transactional
public class FlowUserGroupServiceImpl implements FlowUserGroupService {

    @Autowired
    UserRepository userRepository;

    public FlowUser findUserById(String id) {
        FlowUser fu = null;
        UserEntity user = userRepository.get(id);
        if (user != null) {
            fu = new FlowUser();
            fu.setId(id);
            fu.setEmail(user.getEmail());
            fu.setLastName(user.getName());
        }
        return fu;
    }

    public List<FlowGroup> findGroupsByUser(String id) {

        UserEntity user = userRepository.get(id);
        List<FlowGroup> list = null;

        if (user != null) {
            Set<RoleEntity> roles = user.getRoles();
            if (roles != null) {
                list = new ArrayList<FlowGroup>();
                for (RoleEntity r : roles) {
                    FlowGroup fg = new FlowGroup();
                    fg.setId(r.getId());
                    fg.setName(fg.getName());
                    list.add(fg);
                }
            }
        }
        return list;
    }

}
