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
package com.loy.cloud.conf;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.cloud.security.oauth2.client.feign.OAuth2FeignRequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

import com.loy.cloud.authorize.ClientAuthorityService;
import com.loy.cloud.authorize.EDefaultWebSecurityExpressionHandler;
import com.loy.cloud.authorize.EPermissionEvaluator;
import com.loy.e.common.vo.DefaultRespone;
import com.loy.e.core.api.AuthorityService;

import feign.RequestInterceptor;

import com.loy.e.common.annotation.Author;

@Author(author = "Loy Fu", website = "http://www.17jee.com", contact = "qq群 540553957")

@Configuration
@EnableWebSecurity
@EnableResourceServer
@EnableFeignClients(basePackages = { "com.loy" })
@EnableDiscoveryClient
@EnableHystrixDashboard
@EnableHystrix()
@EnableEurekaClient
@EnableCircuitBreaker
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

    protected final Log logger = LogFactory.getLog(ResourceServerConfiguration.class);

    @Autowired(required = false)
    private AuthorityService authorityService;

    @Autowired(required = false)
    private ClientAuthorityService clientAuthorityService;
    @Autowired(required = false)
    DiscoveryClient discoveryClient;

    @Override
    public void configure(HttpSecurity http) throws Exception {

        http.csrf().disable();
        http.headers().frameOptions().sameOrigin();
        DefaultRespone<Map<String, String>> response = null;
        if (authorityService != null) {
            response = authorityService.getResource();
        } else {
            boolean success = false;
            int maxTry = 15;
            Throwable ee = null;
            while (!success && maxTry > 0) {
                try {
                    response = clientAuthorityService.getPermissionResource();
                    if (response == null) {
                        logger.info("Attempting to obtain resources information failure :"
                                + (16 - maxTry));
                        Thread.sleep(10000);
                        continue;
                    }
                    success = true;
                } catch (Throwable e) {
                    logger.info(
                            "Attempting to obtain resources information failure :" + (16 - maxTry));
                    ee = e;
                    maxTry--;
                    Thread.sleep(10000);
                }
            }
            if (!success) {
                logger.error("clientAuthorityService", ee);
                logger.info("Attempting to obtain resources information failure");
            }
        }
        Map<String, String> permissions = response.getData();
        ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry authorizeRequests = http
                .authorizeRequests();
        if (permissions != null && !permissions.isEmpty()) {
            for (Map.Entry<String, String> p : permissions.entrySet()) {
                authorizeRequests.antMatchers("/**/" + p.getKey())
                        .access("hasPermission('','" + p.getValue() + "')");
            }
        }

        authorizeRequests.anyRequest().authenticated();

        // @formatter:on 
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.expressionHandler(webSecurityExpressionHandler());
    }

    @Bean
    EDefaultWebSecurityExpressionHandler webSecurityExpressionHandler() {
        return new EDefaultWebSecurityExpressionHandler();
    }

    @Bean
    PermissionEvaluator permissionEvaluator() {
        return new EPermissionEvaluator();
    }

    @Configuration
    protected static class FeignOAuthInterceptorConfiguration {
        @Bean
        public RequestInterceptor feignOAuthInterceptor() {
            OAuth2FeignRequestInterceptor oauth2FeignRequestInterceptor = new OAuth2FeignRequestInterceptor(
                    new DefaultOAuth2ClientContext(), oAuth2ProtectedResourceDetails());
            return oauth2FeignRequestInterceptor;
        }

        @Bean
        @ConfigurationProperties("security.oauth2.client")
        public OAuth2ProtectedResourceDetails oAuth2ProtectedResourceDetails() {
            ClientCredentialsResourceDetails resourceDetails = new ClientCredentialsResourceDetails();
            return resourceDetails;
        }
    }
}
