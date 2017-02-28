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

import java.beans.PropertyDescriptor;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.transform.ResultTransformer;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.NotWritablePropertyException;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.beans.TypeMismatchException;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

@SuppressWarnings("serial")
public class EAliasToBeanResultTransformer<T> implements ResultTransformer {
    protected final Log logger = LogFactory.getLog(EAliasToBeanResultTransformer.class);

    private Class<T> mappedClass;

    private boolean checkFullyPopulated = false;

    private boolean primitivesDefaultedForNullValue = false;

    private Map<String, PropertyDescriptor> mappedFields;

    private Set<String> mappedProperties;

    public EAliasToBeanResultTransformer(Class<T> mappedClass) {
        this.mappedClass = mappedClass;
        initialize(mappedClass);
    }

    public Object transformTuple(Object[] tuple, String[] aliases) {

        Object mappedObject = BeanUtils.instantiate(this.mappedClass);
        BeanWrapper bw = PropertyAccessorFactory.forBeanPropertyAccess(mappedObject);
        int columnCount = aliases.length;
        Set<String> populatedProperties = (isCheckFullyPopulated() ? new HashSet<String>() : null);

        for (int index = 0; index < columnCount; index++) {
            String column = aliases[index];
            String field = lowerCaseName(aliases[index].replaceAll(" ", ""));
            PropertyDescriptor pd = this.mappedFields.get(field);
            if (pd != null) {
                try {
                    Object value = tuple[index];
                    try {
                        bw.setPropertyValue(pd.getName(), value);
                    } catch (TypeMismatchException ex) {
                        if (value == null && this.primitivesDefaultedForNullValue) {
                            if (logger.isDebugEnabled()) {
                                logger.debug("Intercepted TypeMismatchException for " +
                                        " and column '" + column
                                        + "' with null value when setting property '" +
                                        pd.getName() + "' of type [" +
                                        ClassUtils.getQualifiedName(pd.getPropertyType()) +
                                        "] on object: " + mappedObject, ex);
                            }
                        } else {
                            throw ex;
                        }
                    }
                    if (populatedProperties != null) {
                        populatedProperties.add(pd.getName());
                    }
                } catch (NotWritablePropertyException ex) {
                    throw new DataRetrievalFailureException("Unable to map column '" + column
                            + "' to property '" + pd.getName() + "'", ex);
                }
            } else {
                logger.debug("No property found for column '" + column + "' mapped to field '"
                        + field + "'");
            }
        }

        if (populatedProperties != null && !populatedProperties.equals(this.mappedProperties)) {
            throw new InvalidDataAccessApiUsageException(
                    "Given ResultSet does not contain all fields " +
                            "necessary to populate object of class [" + this.mappedClass.getName()
                            + "]: " +
                            this.mappedProperties);
        }

        return mappedObject;
    }

    @SuppressWarnings("rawtypes")
    public List transformList(List collection) {
        return collection;
    }

    public boolean isCheckFullyPopulated() {
        return this.checkFullyPopulated;
    }

    protected void initialize(Class<T> mappedClass) {
        this.mappedClass = mappedClass;
        this.mappedFields = new HashMap<String, PropertyDescriptor>();
        this.mappedProperties = new HashSet<String>();
        PropertyDescriptor[] pds = BeanUtils.getPropertyDescriptors(mappedClass);
        for (PropertyDescriptor pd : pds) {
            if (pd.getWriteMethod() != null) {
                this.mappedFields.put(lowerCaseName(pd.getName()), pd);
                String underscoredName = underscoreName(pd.getName());
                if (!lowerCaseName(pd.getName()).equals(underscoredName)) {
                    this.mappedFields.put(underscoredName, pd);
                }
                this.mappedProperties.add(pd.getName());
            }
        }
    }

    protected String lowerCaseName(String name) {
        return name.toLowerCase(Locale.US);
    }

    protected String underscoreName(String name) {
        if (!StringUtils.hasLength(name)) {
            return "";
        }
        StringBuilder result = new StringBuilder();
        result.append(lowerCaseName(name.substring(0, 1)));
        for (int i = 1; i < name.length(); i++) {
            String s = name.substring(i, i + 1);
            String slc = lowerCaseName(s);
            if (!s.equals(slc)) {
                result.append("_").append(slc);
            } else {
                result.append(s);
            }
        }
        return result.toString();
    }
}
