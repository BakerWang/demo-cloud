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
package com.loy.e.common.vo;

import org.apache.commons.lang.StringUtils;

import com.loy.e.common.Constants;

import com.loy.e.common.annotation.Author;

@Author(author = "Loy Fu", website = "http://www.17jee.com", contact = "qq群 540553957")
public class NotLoginResponse extends ErrorResponseData {

    private String loginUrl = "login";

    public NotLoginResponse() {
        super();
        errorCode = Constants.NOT_LOGIN_CODE;
    }

    public String getLoginUrl() {
        return loginUrl;
    }

    public void setLoginUrl(String loginUrl) {
        this.loginUrl = loginUrl;
    }

    public String toJson() {
        StringBuilder sb = new StringBuilder("{\"errorCode\":");
        sb.append("\"");
        sb.append(errorCode);
        sb.append("\"");

        sb.append(",");
        sb.append("\"success\":");
        sb.append(this.success);

        if (StringUtils.isNotEmpty(this.loginUrl)) {
            sb.append(",");
            sb.append("\"loginUrl\":");
            sb.append("\"");
            sb.append(this.loginUrl);
            sb.append("\"");
        }
        if (StringUtils.isNotEmpty(this.msg)) {
            sb.append(",");
            sb.append("\"msg\":");
            sb.append("\"");
            sb.append(this.msg);
            sb.append("\"");
        }
        sb.append("}");
        return sb.toString();
    }
}
