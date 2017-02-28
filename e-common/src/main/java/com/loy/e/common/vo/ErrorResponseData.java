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
public class ErrorResponseData extends Response {

    protected String msg;
    protected String errorCode;

    public ErrorResponseData() {
        super(false);
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String toJson() {
        StringBuilder buf = new StringBuilder();
        buf.append("{");
        buf.append("\"success\":false,");
        buf.append("\"errorCode\":").append("\"" + this.errorCode + "\",");
        buf.append("\"msg\":").append("\"" + this.msg + "\"");
        buf.append("}");
        return buf.toString();
    }
}
