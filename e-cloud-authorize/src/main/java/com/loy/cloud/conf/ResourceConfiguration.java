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
package com.loy.cloud.conf;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import com.loy.e.common.Constants;
import com.loy.e.common.properties.Settings;

import com.loy.e.common.annotation.Author;

@Author(author = "Loy Fu", website = "http://www.17jee.com", contact = "qq群 540553957")
@Configuration
@Order(10)
public class ResourceConfiguration extends WebSecurityConfigurerAdapter {
    @Value("${e.conf.webSecurityEnabledDebug:false}")
    private boolean webSecurityEnabledDebug = false;

    @Autowired()
    Settings settings;

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.debug(webSecurityEnabledDebug);
        List<String> ignoreResources = new ArrayList<String>();
        if (settings != null) {
            List<String> temp = settings.getIgnoreResources();
            if (temp != null) {
                ignoreResources.addAll(temp);
            }
        }
        for (String s : Constants.DEFAULT_IGNORE_RESOURCES) {
            ignoreResources.add(s);
        }
        String[] arr = new String[ignoreResources.size()];
        arr = ignoreResources.toArray(arr);
        web.ignoring().antMatchers(arr);
    }
}
