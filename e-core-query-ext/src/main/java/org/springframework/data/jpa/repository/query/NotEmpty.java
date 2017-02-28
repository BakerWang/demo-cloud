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
package org.springframework.data.jpa.repository.query;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

import com.loy.e.common.annotation.Author;

@Author(author = "Loy Fu", website = "http://www.17jee.com", contact = "qq群 540553957")
@SuppressWarnings("rawtypes")
public class NotEmpty implements TemplateDirectiveModel {

    public void execute(Environment env,
            Map params,
            TemplateModel[] loopVars,
            TemplateDirectiveBody body) throws TemplateException, IOException {
        Writer out = env.getOut();
        Object name = params.get("name");
        boolean empty = false;
        if (name == null) {
            empty = true;
        } else {
            String n = name.toString();
            Object obj = env.__getitem__(n);
            if (obj == null) {
                empty = true;
            } else {
                if (obj instanceof String) {
                    if (StringUtils.isEmpty(obj.toString())) {
                        empty = true;
                    }
                }
            }
        }
        if (body != null && !empty) {
            body.render(out);
        }
    }
}
