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

import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.UserAuthenticationConverter;

import com.loy.security.authentication.EAuthentication;

import com.loy.e.common.annotation.Author;

@Author(author = "Loy Fu", website = "http://www.17jee.com", contact = "qq群 540553957")
public class EDefaultAccessTokenConverter extends DefaultAccessTokenConverter {

    public EDefaultAccessTokenConverter() {
        UserAuthenticationConverter userAuthenticationConverter = new EDefaultUserAuthenticationConverter();
        this.setUserTokenConverter(userAuthenticationConverter);
    }

    public OAuth2Authentication extractAuthentication(Map<String, ?> map) {
        OAuth2Authentication authentication = super.extractAuthentication(map);
        EAuthentication auth2Authentication = new EAuthentication(authentication.getOAuth2Request(),
                authentication.getUserAuthentication());
        String userId = (String) map.get("userId");
        String realName = (String) map.get("realName");
        auth2Authentication.setUserId(userId);
        auth2Authentication.setRealName(realName);
        return auth2Authentication;
    }

}
