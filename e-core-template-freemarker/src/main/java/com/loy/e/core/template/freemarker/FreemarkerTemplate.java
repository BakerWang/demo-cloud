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
package com.loy.e.core.template.freemarker;

import java.io.StringWriter;

import com.loy.e.core.template.Template;

import com.loy.e.common.annotation.Author;

@Author(author = "Loy Fu", website = "http://www.17jee.com", contact = "qq群 540553957")
public class FreemarkerTemplate implements Template {

    private freemarker.template.Template freemarkerTemplate;

    public FreemarkerTemplate(freemarker.template.Template freemarkerTemplate) {
        this.freemarkerTemplate = freemarkerTemplate;
    }

    public String process(Object data) {
        StringWriter stringWriter = new StringWriter();
        try {
            this.freemarkerTemplate.process(data, stringWriter);
            return stringWriter.toString();
        } catch (Exception e) {
            throw new RuntimeException("Fill data to template error", e);
        }
    }

}
