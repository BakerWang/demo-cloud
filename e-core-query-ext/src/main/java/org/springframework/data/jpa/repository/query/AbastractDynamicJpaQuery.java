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

import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.EvaluationContextProvider;
import org.springframework.data.repository.query.ParameterAccessor;
import org.springframework.data.repository.query.ParametersParameterAccessor;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import com.loy.e.core.exception.LoyException;
import com.loy.e.core.query.QueryParamHelper;
import com.loy.e.core.query.data.LoyPageRequest;
import com.loy.e.core.query.data.MapQueryParam;
import com.loy.e.core.query.data.QueryParam;
import com.loy.e.core.template.Template;

import com.loy.e.common.annotation.Author;

@Author(author = "Loy Fu", website = "http://www.17jee.com", contact = "qq群 540553957")
public class AbastractDynamicJpaQuery extends AbstractStringBasedJpaQuery {

    protected final Log LOGGER = LogFactory.getLog(DynamicSimpleJpaQuery.class);
    private Template queryTemplate;
    private Template countQueryTemplate;
    private EvaluationContextProvider evaluationContextProvider;
    private SpelExpressionParser parser;

    public AbastractDynamicJpaQuery(JpaQueryMethod method, EntityManager em, String queryString,
            EvaluationContextProvider evaluationContextProvider, SpelExpressionParser parser,
            Template queryTemplate, Template countQueryTemplate) {
        super(method, em, queryString, evaluationContextProvider, parser);
        this.queryTemplate = queryTemplate;
        this.countQueryTemplate = countQueryTemplate;
        this.evaluationContextProvider = evaluationContextProvider;
        this.parser = parser;
    }

    @Override
    public Query doCreateQuery(Object[] values) {

        JpaParameters jpaParameters = getQueryMethod().getParameters();
        ParameterAccessor accessor = new ParametersParameterAccessor(jpaParameters, values);
        String queryString = processQueryTemplate(values);
        String sortedQueryString = QueryUtils.applySorting(queryString, accessor.getSort(),
                this.getQuery().getAlias());
        Query query = createJpaQuery(sortedQueryString);

        return createBinder(values).bindAndPrepare(query);
    }

    @Override
    protected Query doCreateCountQuery(Object[] values) {

        String countQueryString = this.processCountQueryTemplate(values);

        EntityManager em = getEntityManager();

        return createBinder(values).bind(
                getQueryMethod().isNativeQuery() ? em.createNativeQuery(countQueryString)
                        : em.createQuery(countQueryString, Long.class));
    }

    @Override
    protected ParameterBinder createBinder(Object[] values) {
        JpaQueryMethod jpaQueryMethod = getQueryMethod();
        if (values != null) {

            for (int i = 0; i < values.length; i++) {
                if (values[i] instanceof Pageable) {
                    if (values[i] != null) {
                        Pageable pageable = (Pageable) values[i];
                        LoyPageRequest requestPage = new LoyPageRequest(pageable);
                        values[i] = requestPage;
                    }
                }
            }
        }
        return new DynamicSpelExpressionStringQueryParameterBinder(jpaQueryMethod.getParameters(),
                values, this.getQuery(),
                queryTemplate,
                evaluationContextProvider, parser);
    }

    protected String processQueryTemplate(Object[] values) {

        Map<String, Object> parameters = null;
        MapQueryParam mapQueryParam = null;
        if (values != null) {
            for (Object o : values) {
                if (o instanceof QueryParam) {
                    if (o != null) {
                        mapQueryParam = new MapQueryParam(o);
                        parameters = mapQueryParam.getValues();
                    }
                }
            }
        }
        if (this.queryTemplate == null) {
            StringBuilder stringBuilder = QueryParamHelper.build(mapQueryParam);
            String where = stringBuilder.toString();
            String entityName = getQueryMethod().getEntityInformation().getEntityName();
            String queryString = " from  " + entityName + " x " + where;
            return queryString;
        }
        try {
            String queryString = this.queryTemplate.process(parameters);
            return queryString;
        } catch (Exception e) {
            LOGGER.error(e);
            throw new LoyException("system_error");
        }
    }

    protected String processCountQueryTemplate(Object[] values) {

        String queryString = null;
        if (this.countQueryTemplate == null) {
            if (this.queryTemplate == null) {
                MapQueryParam mapQueryParam = null;
                if (values != null) {
                    for (Object o : values) {
                        if (o instanceof QueryParam) {
                            if (o != null) {
                                mapQueryParam = new MapQueryParam(o);
                            }
                        }
                    }
                }
                StringBuilder stringBuilder = QueryParamHelper.build(mapQueryParam);
                String where = stringBuilder.toString();
                String entityName = getQueryMethod().getEntityInformation().getEntityName();
                queryString = " from  " + entityName + " x " + where;
            } else {
                queryString = this.processQueryTemplate(values);
            }
            queryString = QueryUtils.createCountQueryFor(queryString, "*");
            LOGGER.debug("QL :" + queryString);
            return queryString;
        }

        Map<String, Object> parameters = null;
        if (values != null) {
            for (Object o : values) {
                if (o instanceof QueryParam) {
                    if (o != null) {
                        MapQueryParam mapQueryParam = new MapQueryParam(o);
                        parameters = mapQueryParam.getValues();
                    }
                }
            }
        }

        try {
            queryString = this.countQueryTemplate.process(parameters);
        } catch (Exception e) {
            LOGGER.error(e);
            throw new LoyException("system_error");
        }

        return queryString;
    }
}
