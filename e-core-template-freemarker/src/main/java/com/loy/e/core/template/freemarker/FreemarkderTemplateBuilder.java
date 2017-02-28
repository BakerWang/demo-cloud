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

import java.io.IOException;
import java.io.StringReader;
import java.util.UUID;

import com.loy.e.core.template.Template;
import com.loy.e.core.template.TemplateBuilder;

import freemarker.template.Configuration;

import com.loy.e.common.annotation.Author;

@Author(author = "Loy Fu", website = "http://www.17jee.com", contact = "qq群 540553957")
public class FreemarkderTemplateBuilder extends TemplateBuilder {

    private Configuration configuration;

    public FreemarkderTemplateBuilder() {
        super();
        this.configuration = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
        this.configuration.setNumberFormat("#");
        NotEmpty notEmpty = new NotEmpty();
        this.configuration.setSharedVariable("notEmpty", notEmpty);

    }

    public FreemarkderTemplateBuilder(Configuration configuration) {
        super();
        this.configuration = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
    }

    @Override
    public String getKey() {
        return "freemarker";
    }

    @Override
    public Template build(String str) {
        String queryKey = UUID.randomUUID().toString();
        try {
            freemarker.template.Template freemarkerTemplate = new freemarker.template.Template(
                    queryKey, new StringReader(str), configuration);
            FreemarkerTemplate template = new FreemarkerTemplate(freemarkerTemplate);
            return template;
        } catch (IOException e) {
            throw new RuntimeException("Build freemarker template error", e);
        }
    }

}
