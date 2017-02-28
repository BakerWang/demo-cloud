/*
 * Copyright   Loy Fu. 浠樺帤淇�
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ognl.OgnlContext;
import ognl.OgnlException;
import ognl.OgnlRuntime;
import ognl.PropertyAccessor;

import com.loy.e.common.annotation.Author;

@Author(author = "Loy Fu", website = "http://www.17jee.com", contact = "qq缇� 540553957")
public class Context {

    static {
        OgnlRuntime.setPropertyAccessor(HashMap.class, new ContextAccessor());
    }

    public static final String BINDING_DATA = "_data";

    private Configuration cfg;

    private Map<String, Object> binding;

    private StringBuilder sql = new StringBuilder();

    private List<Object> parameter;

    private int uniqueNumber = 0;

    @SuppressWarnings("unchecked")
    public Context(Configuration cfg, Object data) {
        this.cfg = cfg;
        binding = new HashMap<String, Object>();
        parameter = new ArrayList<Object>();
        binding.put(BINDING_DATA, data);
        if (data instanceof Map) {
            @SuppressWarnings("rawtypes")
            Map map = (Map) data;
            binding.putAll(map);
        }
    }

    public void bind(String key, Object value) {
        binding.put(key, value);
    }

    public void appendSql(String sqlFragement) {
        sql.append(sqlFragement).append(" ");
    }

    public Map<String, Object> getBinding() {
        return this.binding;
    }

    public List<Object> getParameter() {
        return this.parameter;
    }

    public void addParameter(Object parameter) {
        this.parameter.add(parameter);
    }

    public String getSql() {
        return sql.toString();
    }

    public void setSql(String sql) {
        this.sql = new StringBuilder(sql);
    }

    public int getUniqueNumber() {
        return ++uniqueNumber;
    }

    public Configuration getConfiguration() {
        return this.cfg;
    }

    static class ContextAccessor implements PropertyAccessor {

        @SuppressWarnings("rawtypes")
        public Object getProperty(Map context, Object target, Object name)
                throws OgnlException {
            Map map = (Map) target;

            Object result = map.get(name);
            if (result != null) {
                return result;
            }

            Object parameterObject = map.get(BINDING_DATA);
            if (parameterObject instanceof Map) {
                return ((Map) parameterObject).get(name);
            }

            return null;
        }

        @SuppressWarnings({ "unchecked", "rawtypes" })
        public void setProperty(Map context,
                Object target,
                Object name,
                Object value) throws OgnlException {
            Map map = (Map) target;
            map.put(name, value);
        }

        public String getSourceAccessor(OgnlContext context, Object target, Object index) {
            return null;
        }

        public String getSourceSetter(OgnlContext context, Object target, Object index) {
            return null;
        }
    }

}
