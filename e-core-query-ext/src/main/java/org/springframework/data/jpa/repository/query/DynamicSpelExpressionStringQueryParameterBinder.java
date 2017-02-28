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
package org.springframework.data.jpa.repository.query;

import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.persistence.Parameter;
import javax.persistence.Query;

import org.springframework.data.jpa.repository.query.JpaParameters.JpaParameter;
import org.springframework.data.repository.query.EvaluationContextProvider;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import com.loy.e.core.query.data.MapQueryParam;
import com.loy.e.core.query.data.QueryParam;
import com.loy.e.core.template.Template;

import com.loy.e.common.annotation.Author;

@Author(author = "Loy Fu", website = "http://www.17jee.com", contact = "qq群 540553957")
public class DynamicSpelExpressionStringQueryParameterBinder
        extends SpelExpressionStringQueryParameterBinder {

    public DynamicSpelExpressionStringQueryParameterBinder(JpaParameters parameters,
            Object[] values,
            StringQuery query, Template queryTemplate,
            EvaluationContextProvider evaluationContextProvider,
            SpelExpressionParser parser) {
        super(parameters, values, query, evaluationContextProvider, parser);
    }

    protected void bind(Query query, JpaParameter parameter, Object value, int position) {

        if (parameter.isTemporalParameter()) {
            if (hasNamedParameter(query) && parameter.isNamedParameter()) {
                query.setParameter(parameter.getName(), (Date) value, parameter.getTemporalType());
            } else {
                query.setParameter(position, (Date) value, parameter.getTemporalType());
            }
            return;
        }

        if (value instanceof QueryParam) {

            if (value != null) {
                MapQueryParam mapQueryParam = new MapQueryParam(value);
                Map<String, Object> parameters = mapQueryParam.getValues();

                Set<Parameter<?>> paras = query.getParameters();
                Set<String> paramNames = new HashSet<String>();
                if (parameters != null) {
                    for (Parameter<?> p : paras) {
                        paramNames.add(p.getName());
                    }
                }

                for (Map.Entry<String, Object> e : parameters.entrySet()) {
                    Object val = e.getValue();
                    String paramName = e.getKey();
                    if (paramNames.contains(paramName)) {
                        if (val == null) {
                            continue;
                        }
                        if (val instanceof String) {
                            if ("".equals(val)) {
                                continue;
                            }
                        }
                        query.setParameter(e.getKey(), val);
                    }
                }
            }
            return;
        }
        if (hasNamedParameter(query) && parameter.isNamedParameter()) {
            query.setParameter(parameter.getName(), value);
        } else {
            query.setParameter(position, value);
        }
    }
}
