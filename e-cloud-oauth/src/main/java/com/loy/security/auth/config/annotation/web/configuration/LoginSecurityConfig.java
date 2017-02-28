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
package com.loy.security.auth.config.annotation.web.configuration;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.loy.e.common.vo.LoginSuccessResponse;
import com.loy.security.auth.authentication.EAuthenticationFailureHandler;

import com.loy.e.common.annotation.Author;

@Author(author = "Loy Fu", website = "http://www.17jee.com", contact = "qq群 540553957")
@Configuration
@Order(-20)
public class LoginSecurityConfig extends WebSecurityConfigurerAdapter {
    @Value(value = "${e.conf.loginSuccessUrl:http://localhost/}")
    private String loginSuccessUrl;

    @Value("${e.conf.webSecurityEnabledDebug:false}")
    private boolean webSecurityEnabledDebug = false;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private PasswordEncoder passwordEncoder;

    protected void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        http.csrf().disable();
        http
                .formLogin().loginPage("/login").permitAll()
                .successHandler(new AuthenticationSuccessHandler() {

                    public void onAuthenticationSuccess(HttpServletRequest request,
                            HttpServletResponse response,
                            Authentication authentication) throws IOException, ServletException {
                        LoginSuccessResponse success = new LoginSuccessResponse();
                        success.setHome(loginSuccessUrl);
                        response.getWriter().print(success.toJson());

                    }
                }).failureHandler(eauthenticationFailureHandler())
                .and().requestMatchers().antMatchers(
                        "/login",
                        "/oauth/authorize",
                        "/oauth/confirm_access")
                .and()
                .authorizeRequests().anyRequest().authenticated();
        // @formatter:on 

    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.debug(webSecurityEnabledDebug);
        web.ignoring().antMatchers(
                "/**/*.html",
                "/**/*.js",
                "/**/*.css",
                "/**/*.png",
                "/**/*.jpg",
                "/**/*.gif",
                "/**/i18n/**",
                "/**/*.woff");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.parentAuthenticationManager(authenticationManager);
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder);

    }

    @Bean
    EAuthenticationFailureHandler eauthenticationFailureHandler() {

        return new EAuthenticationFailureHandler();
    }

    public static void main(String[] args) {
        StandardPasswordEncoder a = new StandardPasswordEncoder();
        System.out.println(a.encode("123456"));
    }

}
