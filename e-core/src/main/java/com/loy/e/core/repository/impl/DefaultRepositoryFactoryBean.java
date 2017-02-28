package com.loy.e.core.repository.impl;

import java.io.Serializable;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.provider.PersistenceProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.EJpaQueryLookupStrategy;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;
import org.springframework.data.repository.query.EvaluationContextProvider;
import org.springframework.data.repository.query.QueryLookupStrategy;
import org.springframework.data.repository.query.QueryLookupStrategy.Key;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.loy.e.common.properties.Settings;
import com.loy.e.core.entity.Entity;
import com.loy.e.core.repository.GenericRepository;

import com.loy.e.common.annotation.Author;

public class DefaultRepositoryFactoryBean<R extends JpaRepository<M, ID>, M extends Entity<ID>, ID extends Serializable>
        extends JpaRepositoryFactoryBean<R, M, ID> {

    @Autowired
    NamedParameterJdbcTemplate jdbcTemplate;
    @Autowired
    private Settings settings;

    public DefaultRepositoryFactoryBean() {
    }

    protected RepositoryFactorySupport createRepositoryFactory(EntityManager entityManager) {
        DefaultRepositoryFactory<M, ID> defaultRepositoryFactory = new DefaultRepositoryFactory<M, ID>(
                entityManager,
                jdbcTemplate, settings);
        return defaultRepositoryFactory;

    }
}

class DefaultRepositoryFactory<M extends Entity<ID>, ID extends Serializable>
        extends JpaRepositoryFactory {
    private EntityManager entityManager;
    private NamedParameterJdbcTemplate jdbcTemplate;
    private Settings settings;

    public DefaultRepositoryFactory(EntityManager entityManager,
            NamedParameterJdbcTemplate jdbcTemplate,
            Settings settings) {
        super(entityManager);
        this.entityManager = entityManager;
        this.jdbcTemplate = jdbcTemplate;
        this.settings = settings;
    }

    @SuppressWarnings("unchecked")
    protected Object getTargetRepository(RepositoryInformation metadata) {
        Class<?> repositoryInterface = metadata.getRepositoryInterface();
        if (isBaseRepository(repositoryInterface)) {
            JpaEntityInformation<M, ID> entityInformation = getEntityInformation(
                    (Class<M>) metadata.getDomainType());
            GenericRepository<M, ID> repository = new GenericRepositoryImpl<M, ID>(
                    entityInformation,
                    entityManager, jdbcTemplate, settings);
            return repository;
        }
        return super.getTargetRepository(metadata);
    }

    protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
        if (isBaseRepository(metadata.getRepositoryInterface())) {
            return GenericRepository.class;
        }
        return super.getRepositoryBaseClass(metadata);
    }

    private boolean isBaseRepository(Class<?> repositoryInterface) {
        return GenericRepository.class.isAssignableFrom(repositoryInterface);
    }

    @Override
    protected QueryLookupStrategy getQueryLookupStrategy(Key key,
            EvaluationContextProvider evaluationContextProvider) {
        QueryLookupStrategy queryLookupStrategy = EJpaQueryLookupStrategy.create(
                entityManager, key, PersistenceProvider.fromEntityManager(entityManager),
                evaluationContextProvider);
        return queryLookupStrategy;

    }
}
