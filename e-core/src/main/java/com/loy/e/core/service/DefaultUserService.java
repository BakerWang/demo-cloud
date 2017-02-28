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
package com.loy.e.core.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.loy.e.authentication.EUser;
import com.loy.e.common.vo.SessionUser;
import com.loy.e.core.api.UserBaseService;

import com.loy.e.common.annotation.Author;

@Author(author = "Loy Fu", website = "http://www.17jee.com", contact = "qq群 540553957")
@Service(value = "userBaseService")
@Transactional
public class DefaultUserService implements UserBaseService {
    public SessionUser getSessionUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return null;
        }
        SessionUser sessionUser = null;
        Object principal = authentication.getPrincipal();

        if (authentication instanceof EUser) {
            EUser eauthentication = (EUser) authentication;
            if (eauthentication != null) {
                sessionUser = new SessionUser();
                sessionUser.setId(eauthentication.getUserId());
                sessionUser.setName(eauthentication.getRealName());
                sessionUser.setUsername(authentication.getPrincipal().toString());
                return sessionUser;
            }
        }

        if (principal instanceof EUser) {
            EUser eauthentication = (EUser) principal;
            if (eauthentication != null) {
                sessionUser = new SessionUser();
                sessionUser.setId(eauthentication.getUserId());
                sessionUser.setName(eauthentication.getRealName());
                sessionUser.setUsername(eauthentication.getUsername());
            }
        }

        return sessionUser;
    }
}
