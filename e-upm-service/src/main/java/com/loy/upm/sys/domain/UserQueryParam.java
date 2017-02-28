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
package com.loy.upm.sys.domain;

import com.loy.e.core.query.annotation.ConditionParam;
import com.loy.e.core.query.annotation.Order;
import com.loy.e.core.query.data.Op;
import com.loy.e.core.query.data.QueryParam;

import com.loy.e.common.annotation.Author;
@Author(author = "Loy Fu", website = "http://www.17jee.com", contact = "qq群 540553957")
public class UserQueryParam implements QueryParam {

    @ConditionParam(name = "username")
    @Order(name = "username")
    private String username;

    @ConditionParam(name = "name", op = Op.like)
    private String name;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
