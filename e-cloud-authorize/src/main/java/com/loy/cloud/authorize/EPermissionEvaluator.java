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
package com.loy.cloud.authorize;

import java.io.Serializable;
import java.util.Collection;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import com.loy.e.common.vo.DefaultRespone;
import com.loy.e.core.api.AuthorityService;

import com.loy.e.common.annotation.Author;

@Author(author = "Loy Fu", website = "http://www.17jee.com", contact = "qq群 540553957")
public class EPermissionEvaluator implements PermissionEvaluator {

    @Autowired(required = false)
    AuthorityService authorityService;

    @Autowired(required = false)
    private ClientAuthorityService clientAuthorityService;

    public boolean hasPermission(Authentication authentication,
            Object targetDomainObject,
            Object permission) {
        return this.hasPermission(authentication, permission);
    }

    public boolean hasPermission(Authentication authentication,
            Serializable targetId,
            String targetType,
            Object permission) {
        return true;
    }

    private boolean hasPermission(Authentication authentication, Object permission) {
        String perm = (String) permission;

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        if (authorities == null || authorities.isEmpty()) {
            return false;
        }
        DefaultRespone<Set<String>> respone = null;
        if (authorityService == null) {
            respone = clientAuthorityService.getResourceRole(perm);
        } else {
            respone = authorityService.getResourceRole(perm);
        }
        if (respone == null) {
            return false;
        }
        Set<String> roles = respone.getData();
        if (roles == null || roles.isEmpty()) {
            return false;
        }

        for (GrantedAuthority authority : authorities) {
            if (roles.contains(authority.getAuthority())) {
                return true;
            }
        }
        return false;
    }

}
