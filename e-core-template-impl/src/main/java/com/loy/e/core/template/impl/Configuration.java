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

import java.nio.charset.Charset;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.FutureTask;

import com.loy.e.core.template.Template;
import com.loy.e.core.template.impl.builder.XMLTemplateBuilder;

import com.loy.e.common.annotation.Author;

@Author(author = "Loy Fu", website = "http://www.17jee.com", contact = "qq群 540553957")
public class Configuration {

    private ConcurrentHashMap<String, FutureTask<Template>> templateCache;

    private transient boolean cacheTemplate;

    private Charset charset;

    public Configuration() {
        this(true, Charset.defaultCharset());
    }

    public Configuration(boolean cacheTemplate, Charset charset) {
        super();
        this.cacheTemplate = cacheTemplate;
        this.charset = charset;
        templateCache = new ConcurrentHashMap<String, FutureTask<Template>>();
    }

    public Template getTemplate(final String content) {
        if (cacheTemplate) {
            FutureTask<Template> f = templateCache.get(content);
            if (f == null) {
                FutureTask<Template> ft = new FutureTask<Template>(new Callable<Template>() {
                    public Template call() throws Exception {
                        return createTemplate(content);
                    }
                });
                f = templateCache.putIfAbsent(content, ft);
                if (f == null) {
                    ft.run();
                    f = ft;
                }
            }

            try {
                return f.get();
            } catch (Exception e) {
                templateCache.remove(content);
                throw new RuntimeException(e);
            }

        }

        return createTemplate(content);

    }

    private Template createTemplate(String content) {
        Template template = new XMLTemplateBuilder(this).build(content);
        return template;
    }

    public boolean isCacheTemplate() {
        return cacheTemplate;
    }

    public void setCacheTemplate(boolean cacheTemplate) {
        this.cacheTemplate = cacheTemplate;
    }

    public Charset getCharset() {
        return charset;
    }

    public void setCharset(Charset charset) {
        this.charset = charset;
    }

}
