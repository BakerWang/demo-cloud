/*
 * Copyright   Loy Fu.
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

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.common.exceptions.InvalidClientException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;

import com.loy.e.common.annotation.Author;

@Author(author = "Loy Fu", website = "http://www.17jee.com", contact = "qq群 540553957")
public class EJdbcClientDetailsService extends JdbcClientDetailsService {
    @Autowired
    JdbcTemplate jdbcTemplate;

    public EJdbcClientDetailsService(DataSource dataSource) {
        super(dataSource);
    }

    public ClientDetails loadClientByClientId(String clientId) throws InvalidClientException {
        BaseClientDetails details = (BaseClientDetails) super.loadClientByClientId(clientId);
        EClientDetails clientDetails = new EClientDetails(details);

        Collection<GrantedAuthority> collection = new HashSet<GrantedAuthority>();
        clientDetails.setAuthorities(collection);
        SqlRowSet row = jdbcTemplate
                .queryForRowSet("select role_id from  e_client_role where client_id = ?",
                        clientId);

        while (row.next()) {
            String roleId = row.getString("role_id");
            SimpleGrantedAuthority grantedAuthority = new SimpleGrantedAuthority(roleId);
            collection.add(grantedAuthority);
        }

        return clientDetails;
    }
}
