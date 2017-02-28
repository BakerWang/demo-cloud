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
package com.loy.e.core.conf;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.loy.e.common.properties.Settings;
import com.loy.e.core.api.DefaultPasswordService;
import com.loy.e.core.api.SystemKeyService;
import com.loy.e.core.service.DefaultPasswordServiceImpl;
import com.loy.e.core.service.DefaultSystemKeyServiceImpl;

import com.loy.e.common.annotation.Author;

@Author(author = "Loy Fu", website = "http://www.17jee.com", contact = "qq群 540553957")
@Configuration
public class DefaultConf {

    @Bean
    @ConfigurationProperties("spring.application")
    public SystemKeyService systemKeyService() {
        return new DefaultSystemKeyServiceImpl();
    }

    @Bean
    @ConfigurationProperties(prefix = "e.conf")
    public Settings settings() {
        return new Settings();
    }

    @Bean
    @ConditionalOnMissingBean
    DefaultPasswordService defaultPasswordService() {
        return new DefaultPasswordServiceImpl();
    }
}
