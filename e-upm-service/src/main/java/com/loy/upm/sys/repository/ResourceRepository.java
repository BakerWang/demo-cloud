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

import com.loy.e.core.query.annotation.DynamicQuery;
import com.loy.upm.sys.domain.ResourceQueryParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import com.loy.e.core.repository.GenericRepository;
import com.loy.upm.sys.domain.entity.ResourceEntity;

import com.loy.e.common.annotation.Author;
public interface ResourceRepository extends GenericRepository<ResourceEntity, String> {

    @Query("SELECT distinct re FROM UserEntity u join u.roles r join r.resources re "
            + " where u.username = ?1 and re.systemId = ?2 and re.resourceType = 'MENU' order by  re.parentId ,re.sortNum")
    List<ResourceEntity> findMenuByUsernameAndCode(String userId, String code);

    @Query("SELECT distinct re FROM UserEntity u join u.roles r join r.resources re "
            + " where u.username = ?1  and re.resourceType = 'MENU' order by  re.parentId ,re.sortNum")
    List<ResourceEntity> findMenuByUsername(String username);

    @Query("SELECT distinct re FROM RoleEntity r join r.resources re"
            + " where r.id = ?1  order by  re.parentId ,re.sortNum")
    List<ResourceEntity> findResourceByRoleId(String roleId);

    @Query("SELECT distinct re FROM UserEntity u join u.roles r join r.resources re"
            + " where u.username = ?1")
    List<ResourceEntity> findResourceByUsername(String username);

    @Query("SELECT distinct re FROM UserEntity u join u.roles r join r.resources re"
            + " where u.username = ?1 and re.systemId = ?2")
    List<ResourceEntity> findResourceByUsernameAndCode(String username, String code);

    @Query("SELECT r FROM ResourceEntity r "
            + " where r.parentId is null  order by r.sortNum")
    List<ResourceEntity> findResourceByParentIdIsNull();

    @Query("SELECT r FROM ResourceEntity r "
            + " where r.parentId = ?1  order by r.sortNum")
    List<ResourceEntity> findResourceByParentId(String parentId);

    @Query("SELECT r FROM ResourceEntity r ")
    List<ResourceEntity> findAllResource();

    @Query("SELECT r FROM ResourceEntity r "
            + " where  r.systemId = ?1")
    List<ResourceEntity> findResourceBySystemCode(String code);

    @Query(" FROM ResourceEntity r "
            + " where  r.resourceType = 'BUTTON'")
    List<ResourceEntity> findResourceByTypeButton();

    @DynamicQuery
    Page<ResourceEntity> queryResourcePage(ResourceQueryParam resourceQueryParam, Pageable pageable);

}
