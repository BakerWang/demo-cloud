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
package com.loy.e.core.query.data;

import java.beans.PropertyDescriptor;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.loy.e.core.exception.LoyException;
import com.loy.e.core.query.QueryParamAnnotationUtils;
import com.loy.e.core.query.annotation.ConditionParam;

import com.loy.e.common.annotation.Author;

@Author(author = "Loy Fu", website = "http://www.17jee.com", contact = "qq群 540553957")
public class MapQueryParam {

    protected final Log logger = LogFactory.getLog(MapQueryParam.class);

    private Map<String, Object> values;
    private Object param = null;

    public MapQueryParam(Object param) {
        this.param = param;
        this.values = new HashMap<String, Object>();
        if (param != null) {
            try {
                PropertyUtilsBean propertyUtilsBean = new PropertyUtilsBean();
                PropertyDescriptor[] descriptors = propertyUtilsBean.getPropertyDescriptors(param);
                for (int i = 0; i < descriptors.length; i++) {
                    String name = descriptors[i].getName();
                    if (!"class".equals(name)) {

                        Object value = propertyUtilsBean.getNestedProperty(param, name);
                        if (value != null) {
                            ConditionParam conditionParam = QueryParamAnnotationUtils
                                    .getAnnotationByFieldName(param.getClass(), name);
                            if (conditionParam != null) {
                                String temp = conditionParam.name();
                                if (StringUtils.isNotEmpty(temp)) {
                                    name = temp;
                                }
                                if (conditionParam.op() == Op.like) {
                                    value = "%" + value + "%";
                                }
                            }

                            values.put(name, value);
                        }
                    }
                }
            } catch (Throwable e) {
                logger.error(e);
                throw new LoyException("system_error");
            }
        }
    }

    public Map<String, Object> getValues() {
        return this.values;
    }

    public Object getParam() {
        return param;
    }

}
