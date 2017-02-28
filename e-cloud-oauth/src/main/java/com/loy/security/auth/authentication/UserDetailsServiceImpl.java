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
package com.loy.security.auth.authentication;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.loy.security.authentication.DefaultEUser;

import com.loy.e.common.annotation.Author;

@Author(author = "Loy Fu", website = "http://www.17jee.com", contact = "qq群 540553957")

@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        DefaultEUser user = null;
        if (StringUtils.isEmpty(username)) {
            return null;
        }
        Map<String, Object> result = jdbcTemplate
                .queryForMap(
                        "select id, username,name,password,enabled from  e_user where username=?",
                        username);
        if (result != null) {
            String userId = (String) result.get("id");

            Collection<GrantedAuthority> collection = new HashSet<GrantedAuthority>();
            SqlRowSet row = jdbcTemplate
                    .queryForRowSet("select role_id from  e_user_role where user_id = ?", userId);

            while (row.next()) {
                String roleId = row.getString("role_id");
                SimpleGrantedAuthority grantedAuthority = new SimpleGrantedAuthority(roleId);
                collection.add(grantedAuthority);
            }

            Boolean enabled = (Boolean) result.get("enabled");
            user = new DefaultEUser(username, result.get("password").toString(), enabled,
                    collection);
            user.setUserId(userId);
            user.setRealName((String) result.get("name"));

        }
        return user;
    }

}
