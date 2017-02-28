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
package com.loy.upm.sys.domain.entity;

import com.loy.e.common.annotation.Author;
@Author(author = "Loy Fu", website = "http://www.17jee.com", contact = "qq群 540553957")
public enum ResourceTypeEnum {

    MENU("菜单", "menu.upm.resource_menu"),
    BUTTON("按钮", "menu.upm.resource_button"),
    CLIENT("客户端", "menu.upm.resource_client");

    private final String info;
    private final String i18nKey;

    private ResourceTypeEnum(String info, String i18nKey) {
        this.info = info;
        this.i18nKey = i18nKey;
    }

    public String getInfo() {
        return info;
    }

    public String getI18nKey() {
        return i18nKey;
    }

}
