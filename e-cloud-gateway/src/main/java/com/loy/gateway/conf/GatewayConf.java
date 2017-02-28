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
package com.loy.gateway.conf;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;

import com.loy.gateway.filter.LoginRedirectFilter;
import com.loy.gateway.zuul.FormBodyWrapperFilter;

import com.loy.e.common.annotation.Author;

@Author(author = "Loy Fu", website = "http://www.17jee.com", contact = "qq群 540553957")
@Configuration
@EnableOAuth2Sso
@ConditionalOnExpression(value = "${e.conf.gateway.enabled:true}")
public class GatewayConf extends WebSecurityConfigurerAdapter {
    @Value(value = "${e.conf.authServer}")
    private String authServer;
    @Value(value = "${redirect-uri:http://localhost/}")
    private String redirectUri;

    @Bean
    public FormBodyWrapperFilter formBodyWrapperFilter() {
        return new FormBodyWrapperFilter();
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.antMatcher("/**").anonymous();
        SimpleUrlLogoutSuccessHandler simpleUrlLogoutSuccessHandler = new SimpleUrlLogoutSuccessHandler();

        simpleUrlLogoutSuccessHandler
                .setDefaultTargetUrl(authServer + "/logout?redirect_uri=" + redirectUri);
        simpleUrlLogoutSuccessHandler.setAlwaysUseDefaultTargetUrl(true);
        http.logout().logoutSuccessHandler(simpleUrlLogoutSuccessHandler);

    }

    @Bean
    public FilterRegistrationBean loginFilterRegistration() {

        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new LoginRedirectFilter());
        registration.addUrlPatterns("/login");
        registration.addUrlPatterns("/login.html");
        registration.setName("loginRedirectFilter");
        registration.setOrder(-1000);
        return registration;
    }
    /*
     * @Bean public SimpleFilter simpleFilter() { return new SimpleFilter(); }
     * 
     * private Filter csrfHeaderFilter() { return new OncePerRequestFilter() {
     * 
     * @Override protected void doFilterInternal(HttpServletRequest request,
     * HttpServletResponse response, FilterChain filterChain) throws
     * ServletException, IOException { CsrfToken csrf = (CsrfToken)
     * request.getAttribute(CsrfToken.class .getName()); if (csrf != null) {
     * Cookie cookie = WebUtils.getCookie(request, "XSRF-TOKEN"); String token =
     * csrf.getToken(); if (cookie == null || token != null &&
     * !token.equals(cookie.getValue())) { cookie = new Cookie("XSRF-TOKEN",
     * token); cookie.setPath("/"); response.addCookie(cookie); } }
     * filterChain.doFilter(request, response); } }; }
     * 
     * private CsrfTokenRepository csrfTokenRepository() {
     * HttpSessionCsrfTokenRepository repository = new
     * HttpSessionCsrfTokenRepository();
     * repository.setHeaderName("X-XSRF-TOKEN"); return repository; }
     */
}
