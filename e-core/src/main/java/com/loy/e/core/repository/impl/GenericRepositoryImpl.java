package com.loy.e.core.repository.impl;

import static org.springframework.data.jpa.repository.query.QueryUtils.toOrders;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.CrudMethodMetadata;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.loy.e.common.properties.Settings;
import com.loy.e.core.entity.BaseEntity;
import com.loy.e.core.entity.Entity;
import com.loy.e.core.repository.GenericRepository;

import com.loy.e.common.annotation.Author;

@Transactional(readOnly = true)
public class GenericRepositoryImpl<T extends Entity<ID>, ID extends Serializable>
        implements GenericRepository<T, ID> {
    protected final Log logger = LogFactory.getLog(GenericRepositoryImpl.class);
    private EntityManager em = null;
    private JpaEntityInformation<T, ?> entityInformation = null;
    private Class<T> entityClass = null;
    private CrudMethodMetadata crudMethodMetadata = null;

    NamedParameterJdbcTemplate jdbcTemplate;

    public void setEntityManager(EntityManager entityManager) {
        this.em = entityManager;
    }

    public GenericRepositoryImpl(JpaEntityInformation<T, ?> entityInformation,
            EntityManager entityManager,
            NamedParameterJdbcTemplate jdbcTemplate,
            Settings settings) {
        //super(entityInformation, entityManager);
        this.entityInformation = entityInformation;
        this.entityClass = this.entityInformation.getJavaType();
        this.em = entityManager;
        this.jdbcTemplate = jdbcTemplate;

    }

    //	public GenericRepositoryImpl(Class<T> domainClass, EntityManager em) {
    //		this(JpaEntityInformationSupport.getMetadata(domainClass, em), em);
    //	}

    public void setRepositoryMethodMetadata(CrudMethodMetadata crudMethodMetadata) {
        this.crudMethodMetadata = crudMethodMetadata;
    }

    @Override
    @Transactional
    public T save(T entity) {
        if (entity.isNew() || entityInformation.isNew(entity)) {
            em.persist(entity);
            return entity;
        } else {
            if (entity instanceof BaseEntity) {
                BaseEntity baseEntity = (BaseEntity) entity;
                baseEntity.setModifiedTime(new Date());
            }
            return em.merge(entity);
        }
    }

    @Override
    public T get(ID id) {
        Class<T> domainType = entityClass;
        if (crudMethodMetadata == null) {
            return em.find(domainType, id);
        }
        LockModeType type = crudMethodMetadata.getLockModeType();
        Map<String, Object> hints = crudMethodMetadata.getQueryHints();
        return type == null ? em.find(domainType, id, hints) : em.find(domainType, id, type, hints);
    }

    @Override
    public List<T> findAll() {
        return getQuery(null, (Sort) null).getResultList();
    }

    @Transactional
    public void delete(ID id) {
        T entity = get(id);
        if (entity != null) {
            delete(entity);
        }
    }

    @Override
    @Transactional
    public void delete(T entity) {
        em.remove(em.contains(entity) ? entity : em.merge(entity));
    }

    @Override
    @Transactional
    public void delete(List<ID> ids) {
        for (ID id : ids) {
            delete(id);
        }
    }

    protected TypedQuery<T> getQuery(Specification<T> spec, Sort sort) {

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<T> query = builder.createQuery(entityClass);
        Root<T> root = applySpecificationToCriteria(spec, query);
        query.select(root);
        if (sort != null) {
            query.orderBy(toOrders(sort, root, builder));
        }
        return applyRepositoryMethodMetadata(em.createQuery(query));
    }

    private <S> Root<T> applySpecificationToCriteria(Specification<T> spec,
            CriteriaQuery<S> query) {
        Assert.notNull(query);
        Root<T> root = query.from(entityClass);
        if (spec == null) {
            return root;
        }
        CriteriaBuilder builder = em.getCriteriaBuilder();
        Predicate predicate = spec.toPredicate(root, query, builder);

        if (predicate != null) {
            query.where(predicate);
        }

        return root;
    }

    private TypedQuery<T> applyRepositoryMethodMetadata(TypedQuery<T> query) {
        if (crudMethodMetadata == null) {
            return query;
        }
        LockModeType type = crudMethodMetadata.getLockModeType();
        TypedQuery<T> toReturn = type == null ? query : query.setLockMode(type);

        for (Entry<String, Object> hint : crudMethodMetadata.getQueryHints().entrySet()) {
            query.setHint(hint.getKey(), hint.getValue());
        }
        return toReturn;
    }

}
