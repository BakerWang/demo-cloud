/*
 * Copyright   Loy Fu.
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
package com.loy.e.core.template.impl.scripting.xmltags;

import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.loy.e.core.template.impl.Context;

import com.loy.e.common.annotation.Author;

@Author(author = "Loy Fu", website = "http://www.17jee.com", contact = "qq群 540553957")
public class NotEmptyNode implements SqlNode {

    private String name;
    private SqlNode contents;

    public NotEmptyNode(SqlNode contents, String name) {
        this.contents = contents;
        this.name = name;
    }

    public boolean apply(Context context) {
        Map<String, Object> map = context.getBinding();
        if (map == null || map.isEmpty()) {
            return false;
        }
        Object value = map.get(name);
        if (value == null) {
            return false;
        }
        if (value instanceof String) {
            if (StringUtils.isNotEmpty(value.toString())) {
                this.contents.apply(context);
                return true;
            } else {
                return false;
            }
        }
        this.contents.apply(context);
        return true;
    }

}
