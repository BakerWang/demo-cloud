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
package com.loy.e.core.template.impl.scripting.xmltags;

import com.loy.e.core.template.impl.Context;
import com.loy.e.core.template.impl.parsing.GenericTokenParser;
import com.loy.e.core.template.impl.parsing.TokenHandler;

public class TextNode implements SqlNode {

    private String content;

    public TextNode(String content) {
        this.content = content;
    }

    public boolean apply(final Context context) {
        GenericTokenParser parser = new GenericTokenParser("${", "}", new TokenHandler() {

            public String handleToken(String content) {
                Object value = OgnlCache.getValue(content,
                        context.getBinding());
                return value == null ? "" : value.toString();
            }
        });
        context.appendSql(parser.parse(content));
        return true;
    }

}
