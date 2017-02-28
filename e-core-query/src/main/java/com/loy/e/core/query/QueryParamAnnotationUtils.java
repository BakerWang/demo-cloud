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
package com.loy.e.core.query;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.springframework.util.ConcurrentReferenceHashMap;

import com.loy.e.core.query.annotation.ConditionParam;

import com.loy.e.common.annotation.Author;

@Author(author = "Loy Fu", website = "http://www.17jee.com", contact = "qq群 540553957")
public class QueryParamAnnotationUtils {

    private static final Map<Class<?>, Map<String, ConditionParam>> synthesizableConditionParamAnnotationCache = new ConcurrentReferenceHashMap<Class<?>, Map<String, ConditionParam>>(
            256);

    public static ConditionParam getAnnotationByFieldName(Class<?> clazz, String fieldName) {
        Map<String, ConditionParam> map = synthesizableConditionParamAnnotationCache.get(clazz);
        if (map == null) {
            Field[] fields = {};
            fields = getBeanFields(clazz, fields);
            map = buildMap(fields);
            synthesizableConditionParamAnnotationCache.put(clazz, map);
        }
        if (map.isEmpty()) {
            return null;
        }
        ConditionParam conditionParam = map.get(fieldName);
        return conditionParam;
    }

    private static Map<String, ConditionParam> buildMap(Field[] fields) {
        if (fields == null || fields.length == 0) {
            return Collections.emptyMap();
        }
        Map<String, ConditionParam> map = new HashMap<String, ConditionParam>();
        for (int i = fields.length - 1; i >= 0; i--) {
            ConditionParam conditionParam = fields[i].getAnnotation(ConditionParam.class);
            if (conditionParam != null) {
                map.put(fields[i].getName(), conditionParam);
            }
        }
        return map;
    }

    private static Field[] getBeanFields(Class<?> cls, Field[] fields) {
        fields = (Field[]) ArrayUtils.addAll(fields, cls.getDeclaredFields());
        Class<?> sup = cls.getSuperclass();
        if (sup != null) {
            fields = getBeanFields(sup, fields);
        }
        return fields;

    }
}
