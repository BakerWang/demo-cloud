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

import com.loy.e.common.annotation.Author;

@Author(author = "Loy Fu", website = "http://www.17jee.com", contact = "qq群 540553957")
public class LoginSuccessResponse extends SuccessResponse {

    private String version = "";
    private String home = "home.html";

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getHome() {
        return home;
    }

    public void setHome(String home) {
        this.home = home;
    }

    public String toJson() {
        StringBuilder buf = new StringBuilder();
        buf.append("{");
        buf.append("\"success\":true,");
        buf.append("\"home\":").append("\"" + this.home + "\",");
        buf.append("\"version\":").append("\"" + this.version + "\"");
        buf.append("}");
        return buf.toString();
    }
}
