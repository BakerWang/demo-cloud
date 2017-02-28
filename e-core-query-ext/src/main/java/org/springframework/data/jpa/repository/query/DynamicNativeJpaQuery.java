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

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.hibernate.SQLQuery;
import org.springframework.data.repository.query.EvaluationContextProvider;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import com.loy.e.core.query.data.QueryResult;
import com.loy.e.core.template.Template;

import com.loy.e.common.annotation.Author;

@Author(author = "Loy Fu", website = "http://www.17jee.com", contact = "qq群 540553957")
public class DynamicNativeJpaQuery extends AbastractDynamicJpaQuery {

    public DynamicNativeJpaQuery(JpaQueryMethod method, EntityManager em, String queryString,
            EvaluationContextProvider evaluationContextProvider, SpelExpressionParser parser,
            Template queryTemplate, Template countQueryTemplate) {

        super(method, em, queryString, evaluationContextProvider, parser, queryTemplate,
                countQueryTemplate);

    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public Query createJpaQuery(String queryString) {
        boolean queryForEntity = getQueryMethod().isQueryForEntity();
        Class<?> returnedObjectType = getQueryMethod().getReturnedObjectType();
        if (queryForEntity) {
            return getEntityManager().createNativeQuery(queryString, returnedObjectType);
        } else {
            if (QueryResult.class.isAssignableFrom(returnedObjectType)) {
                Query query = getEntityManager().createNativeQuery(queryString);
                query.unwrap(SQLQuery.class).setResultTransformer(
                        new EAliasToBeanResultTransformer(returnedObjectType));
                return query;
            }
        }
        return getEntityManager().createNativeQuery(queryString);
    }
}
