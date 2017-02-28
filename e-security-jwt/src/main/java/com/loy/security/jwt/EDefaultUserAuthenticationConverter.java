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
package com.loy.security.jwt;

import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;

import com.loy.security.authentication.DefaultEUser;

import com.loy.e.common.annotation.Author;

@Author(author = "Loy Fu", website = "http://www.17jee.com", contact = "qq群 540553957")
public class EDefaultUserAuthenticationConverter extends DefaultUserAuthenticationConverter {

    @SuppressWarnings("unchecked")
    public Map<String, ?> convertUserAuthentication(Authentication authentication) {
        Map<String, Object> response = (Map<String, Object>) super.convertUserAuthentication(
                authentication);
        Object principal = authentication.getPrincipal();
        if (principal instanceof DefaultEUser) {

            DefaultEUser user = (DefaultEUser) principal;
            if (user != null) {
                response.put("realName", user.getRealName());
                response.put("userId", user.getUserId());
            }
        }
        return response;
    }
}
