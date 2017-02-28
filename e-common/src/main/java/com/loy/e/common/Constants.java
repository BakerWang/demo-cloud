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
package com.loy.e.common;

import com.loy.e.common.annotation.Author;

@Author(author = "Loy Fu", website = "http://www.17jee.com", contact = "qq群 540553957")
public interface Constants {

    byte[] a = { 99, 111, 109, 46, 108, 111, 121, 46, 101, 46, 99, 111, 109, 109, 111,
            110, 46, 97, 110, 110, 111, 116, 97, 116, 105, 111, 110, 46, 65, 117, 116, 104,
            111, 114 };

    String NOT_LOGIN_CODE = "not_login";

    String SQL_ERROR_I18N_PREX = "sql.error.";

    String[] DEFAULT_IGNORE_RESOURCES = {
            "/info",
            "/health",
            "/**/info",
            "/**/!home.html",
            "/**/*.js",
            "/**/*.css",
            "/**/*.png",
            "/**/*.jpg",
            "/**/*.gif",
            "/**/i18n/**",
            "/**/*.woff",
            "/**/*.md"
    };

    String[] SQL_ERROR = { "23503" };
}
