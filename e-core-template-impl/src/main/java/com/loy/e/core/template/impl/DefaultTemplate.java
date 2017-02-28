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
package com.loy.e.core.template.impl;

import com.loy.e.core.template.Template;
import com.loy.e.core.template.impl.parsing.GenericTokenParser;
import com.loy.e.core.template.impl.parsing.TokenHandler;
import com.loy.e.core.template.impl.scripting.xmltags.OgnlCache;
import com.loy.e.core.template.impl.scripting.xmltags.SqlNode;

import com.loy.e.common.annotation.Author;

@Author(author = "Loy Fu", website = "http://www.17jee.com", contact = "qq群 540553957")
public class DefaultTemplate implements Template {

    private Configuration configuration;
    private SqlNode root;

    public DefaultTemplate(Configuration configuration, SqlNode root) {
        this.configuration = configuration;
        this.root = root;
    }

    public String process(Object data) {
        Context context = new Context(configuration, data);
        this.root.apply(context);
        parseParameter(context);
        return context.getSql();
    }

    private void parseParameter(final Context context) {
        String sql = context.getSql();
        GenericTokenParser parser = new GenericTokenParser("#{", "}",
                new TokenHandler() {
                    public String handleToken(String content) {
                        Object value = OgnlCache.getValue(content,
                                context.getBinding());
                        if (value == null) {
                            throw new RuntimeException("Can not found "
                                    + content + " value");
                        }
                        context.addParameter(value);
                        return "?";
                    }
                });
        sql = parser.parse(sql);
        context.setSql(sql);
    }
}
