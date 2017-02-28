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
package com.loy.upm.sys.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import com.loy.e.core.query.annotation.DynamicQuery;
import com.loy.e.core.repository.GenericRepository;
import com.loy.upm.sys.domain.RoleQueryParam;
import com.loy.upm.sys.domain.entity.RoleEntity;

import com.loy.e.common.annotation.Author;
@Author(author = "Loy Fu", website = "http://www.17jee.com", contact = "qq群 540553957")
public interface RoleRepository extends GenericRepository<RoleEntity, String> {
    @Query("SELECT distinct r FROM RoleEntity r join r.resources re where re.id  = ?1")
    public List<RoleEntity> findRoleByResourceId(String resourceId);

    @DynamicQuery
    Page<RoleEntity> findRolePage(RoleQueryParam roleQueryParam, Pageable pageable);

}
