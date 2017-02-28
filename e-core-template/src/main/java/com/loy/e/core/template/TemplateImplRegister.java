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
package com.loy.e.core.template;

import java.util.HashMap;
import java.util.Map;

import com.loy.e.common.annotation.Author;

@Author(author = "Loy Fu", website = "http://www.17jee.com", contact = "qq群 540553957")
public final class TemplateImplRegister {

    private static Map<String, TemplateBuilder> builders = new HashMap<String, TemplateBuilder>();

    public static void register(String key, TemplateBuilder templateBuilder) {
        builders.put(key, templateBuilder);
    }

    public static TemplateBuilder get(String key) {
        TemplateBuilder templateBuilder = builders.get(key);
        return templateBuilder;
    }
}
