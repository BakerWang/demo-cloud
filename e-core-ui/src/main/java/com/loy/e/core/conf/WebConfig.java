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

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.loy.e.common.properties.Settings;

import com.loy.e.common.annotation.Author;
@Author(author = "Loy Fu", website = "http://www.17jee.com", contact = "qq群 540553957")
@Configuration()
public class WebConfig extends WebMvcConfigurerAdapter { //WebMvcConfigurationSupport

    protected final Log LOGGER = LogFactory.getLog(WebConfig.class);

    @Autowired
    private Settings settings;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        Map<String, List<String>> mappings = this.settings.getStaticMappings();
        if (mappings != null) {
            for (Map.Entry<String, List<String>> e : mappings.entrySet()) {
                List<String> list = e.getValue();
                if (list != null && !list.isEmpty()) {
                    String[] temp = new String[list.size()];
                    list.toArray(temp);
                    registry.addResourceHandler(e.getKey()).addResourceLocations(temp);
                }
            }
        }
    }
    
    @Bean
    @ConfigurationProperties(prefix = "e.conf")
    public Settings settings(){
        return new Settings();
    }
}
