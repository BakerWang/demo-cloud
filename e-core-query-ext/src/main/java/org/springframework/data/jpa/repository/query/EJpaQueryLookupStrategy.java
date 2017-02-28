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

import java.lang.reflect.Method;

import javax.persistence.EntityManager;

import org.apache.commons.lang.StringUtils;
import org.springframework.data.jpa.provider.PersistenceProvider;
import org.springframework.data.jpa.provider.QueryExtractor;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.repository.core.NamedQueries;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.query.EvaluationContextProvider;
import org.springframework.data.repository.query.QueryLookupStrategy;
import org.springframework.data.repository.query.QueryLookupStrategy.Key;
import org.springframework.data.repository.query.RepositoryQuery;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.util.Assert;

import com.loy.e.core.query.annotation.DynamicQuery;
import com.loy.e.core.template.Template;
import com.loy.e.core.template.TemplateImplRegister;

import com.loy.e.common.annotation.Author;

@Author(author = "Loy Fu", website = "http://www.17jee.com", contact = "qq群 540553957")
public class EJpaQueryLookupStrategy {

    private EJpaQueryLookupStrategy() {
    }

    private abstract static class AbstractQueryLookupStrategy implements QueryLookupStrategy {

        private final EntityManager em;
        private final QueryExtractor provider;

        public AbstractQueryLookupStrategy(EntityManager em, QueryExtractor extractor) {

            this.em = em;
            this.provider = extractor;
        }

        public RepositoryQuery resolveQuery(Method method,
                RepositoryMetadata metadata,
                ProjectionFactory factory,
                NamedQueries namedQueries) {
            return resolveQuery(new JpaQueryMethod(method, metadata, factory, provider), em,
                    namedQueries);
        }

        protected abstract RepositoryQuery resolveQuery(JpaQueryMethod method,
                EntityManager em,
                NamedQueries namedQueries);
    }

    private static class CreateQueryLookupStrategy extends AbstractQueryLookupStrategy {

        private final PersistenceProvider persistenceProvider;

        public CreateQueryLookupStrategy(EntityManager em, QueryExtractor extractor) {

            super(em, extractor);
            this.persistenceProvider = PersistenceProvider.fromEntityManager(em);
        }

        @Override
        protected RepositoryQuery resolveQuery(JpaQueryMethod method,
                EntityManager em,
                NamedQueries namedQueries) {

            try {
                return new PartTreeJpaQuery(method, em, persistenceProvider);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException(
                        String.format("Could not create query metamodel for method %s!",
                                method.toString()),
                        e);
            }
        }

    }

    private static class DeclaredQueryLookupStrategy extends AbstractQueryLookupStrategy {

        private static final SpelExpressionParser PARSER = new SpelExpressionParser();
        private final EvaluationContextProvider evaluationContextProvider;
        private final EntityManager em;
        private final QueryExtractor provider;

        public DeclaredQueryLookupStrategy(EntityManager em, QueryExtractor extractor,
                EvaluationContextProvider evaluationContextProvider) {
            super(em, extractor);
            this.em = em;
            this.provider = extractor;
            this.evaluationContextProvider = evaluationContextProvider;
            TemplateBuilderInit.init();
        }

        public final RepositoryQuery resolveQuery(Method method,
                RepositoryMetadata metadata,
                ProjectionFactory factory,
                NamedQueries namedQueries) {
            JpaQueryMethod jpaQueryMethod = new JpaQueryMethod(method, metadata, factory, provider);

            DynamicQuery annotation = method.getAnnotation(DynamicQuery.class);
            Template queryTemplate = null;
            Template countQueryTemplate = null;
            if (annotation != null) {
                String queryString = jpaQueryMethod.getAnnotatedQuery();
                if (StringUtils.isNotEmpty(queryString)) {
                    String countQueryString = jpaQueryMethod.getCountQuery();
                    queryTemplate = TemplateImplRegister.get(annotation.language())
                            .build(queryString);
                    if (StringUtils.isNotEmpty(countQueryString)) {
                        countQueryTemplate = TemplateImplRegister.get(annotation.language())
                                .build(countQueryString);
                    }
                } else {
                    queryString = "auto ql";
                }

                if (jpaQueryMethod.isNativeQuery()) {
                    DynamicNativeJpaQuery simpleJpaQuery = new DynamicNativeJpaQuery(jpaQueryMethod,
                            em, queryString,
                            evaluationContextProvider, PARSER, queryTemplate, countQueryTemplate);
                    return simpleJpaQuery;

                } else {
                    DynamicSimpleJpaQuery simpleJpaQuery = new DynamicSimpleJpaQuery(jpaQueryMethod,
                            em, queryString,
                            evaluationContextProvider, PARSER, queryTemplate, countQueryTemplate);
                    return simpleJpaQuery;
                }
            }

            RepositoryQuery repositoryQuery = resolveQuery(
                    jpaQueryMethod, em, namedQueries);
            return repositoryQuery;
        }

        protected RepositoryQuery resolveQuery(JpaQueryMethod method,
                EntityManager em,
                NamedQueries namedQueries) {

            RepositoryQuery query = JpaQueryFactory.INSTANCE.fromQueryAnnotation(method, em,
                    evaluationContextProvider);

            if (null != query) {
                return query;
            }

            query = JpaQueryFactory.INSTANCE.fromProcedureAnnotation(method, em);

            if (null != query) {
                return query;
            }

            String name = method.getNamedQueryName();
            if (namedQueries.hasQuery(name)) {
                return JpaQueryFactory.INSTANCE.fromMethodWithQueryString(method, em,
                        namedQueries.getQuery(name),
                        evaluationContextProvider);
            }

            query = NamedQuery.lookupFrom(method, em);

            if (null != query) {
                return query;
            }

            throw new IllegalStateException(
                    String.format(
                            "Did neither find a NamedQuery nor an annotated query for method %s!",
                            method));
        }
    }

    private static class CreateIfNotFoundQueryLookupStrategy extends AbstractQueryLookupStrategy {

        private final DeclaredQueryLookupStrategy lookupStrategy;
        private final CreateQueryLookupStrategy createStrategy;

        public CreateIfNotFoundQueryLookupStrategy(EntityManager em, QueryExtractor extractor,
                CreateQueryLookupStrategy createStrategy,
                DeclaredQueryLookupStrategy lookupStrategy) {

            super(em, extractor);

            this.createStrategy = createStrategy;
            this.lookupStrategy = lookupStrategy;
        }

        @Override
        public RepositoryQuery resolveQuery(Method method,
                RepositoryMetadata metadata,
                ProjectionFactory factory,
                NamedQueries namedQueries) {

            try {
                return lookupStrategy.resolveQuery(method, metadata, factory, namedQueries);
            } catch (IllegalStateException e) {
                return createStrategy.resolveQuery(method, metadata, factory, namedQueries);
            }
        }

        @Override
        protected RepositoryQuery resolveQuery(JpaQueryMethod method,
                EntityManager em,
                NamedQueries namedQueries) {
            return null;
        }
    }

    public static QueryLookupStrategy create(EntityManager em,
            Key key,
            QueryExtractor extractor,
            EvaluationContextProvider evaluationContextProvider) {

        Assert.notNull(em, "EntityManager must not be null!");
        Assert.notNull(extractor, "QueryExtractor must not be null!");
        Assert.notNull(evaluationContextProvider, "EvaluationContextProvider must not be null!");

        switch (key != null ? key : Key.CREATE_IF_NOT_FOUND) {
        case CREATE:
            return new CreateQueryLookupStrategy(em, extractor);
        case USE_DECLARED_QUERY:
            return new DeclaredQueryLookupStrategy(em, extractor, evaluationContextProvider);
        case CREATE_IF_NOT_FOUND:
            return new CreateIfNotFoundQueryLookupStrategy(em, extractor,
                    new CreateQueryLookupStrategy(em, extractor),
                    new DeclaredQueryLookupStrategy(em, extractor, evaluationContextProvider));
        default:
            throw new IllegalArgumentException(
                    String.format("Unsupported query lookup strategy %s!", key));
        }
    }
}
