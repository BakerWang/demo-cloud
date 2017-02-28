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
package com.loy.e.core.web.dispatch;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.ModelAndView;

import com.loy.e.common.properties.Settings;
import com.loy.e.common.vo.ErrorResponseData;
import com.loy.e.core.util.JsonUtil;
import com.loy.e.core.util.RequestUtil;

import com.loy.e.common.annotation.Author;

@Author(author = "Loy Fu", website = "http://www.17jee.com", contact = "qq群 540553957")
@SuppressWarnings("serial")
public class DefaultDispatchServlet extends DispatcherServlet {

    Map<String, Locale> locales = new HashMap<String, Locale>();

    @Autowired
    private Settings settings;

    public DefaultDispatchServlet() {
        Locale[] lcl = Locale.getAvailableLocales();
        if (lcl != null) {
            for (Locale locale : lcl) {
                String language = locale.getLanguage();
                String country = locale.getCountry();
                String lang = language + "_" + country;
                locales.put(lang, locale);
            }
        }
    }

    protected ModelAndView processHandlerException(HttpServletRequest request,
            HttpServletResponse response,
            Object handler,
            Exception ex) throws Exception {
        if (RequestUtil.isJson(request) && ex instanceof ServletException) {
            ErrorResponseData error = new ErrorResponseData();
            error.setMsg(ex.getMessage());
            String errorJson = JsonUtil.json(error);
            response.getWriter().print(errorJson);
            return null;
        } else {
            return super.processHandlerException(request, response, handler, ex);
        }
    }

    @Override
    protected LocaleContext buildLocaleContext(final HttpServletRequest request) {
        return new LocaleContext() {
            @Override
            public Locale getLocale() {
                Locale locale = null;
                Cookie[] cookies = request.getCookies();
                if (cookies != null) {
                    for (Cookie cookie : cookies) {
                        String name = cookie.getName();
                        if ("LANG".equals(name)) {
                            String value = cookie.getValue();
                            if (StringUtils.isNotEmpty(value)) {
                                List<String> supportLocales = settings.getSupportLocales();
                                if (supportLocales.contains(value)) {
                                    locale = locales.get(value);
                                    break;
                                }
                            }
                        }
                    }
                }
                if (locale != null) {
                    return locale;
                }
                String defaultLocale = settings.getDefaultLocale();

                if (StringUtils.isNotEmpty(defaultLocale)) {
                    locale = locales.get(defaultLocale);
                }
                if (locale == null) {
                    locale = Locale.getDefault();
                }
                return locale;
            }
        };
    }

}
