package com.gfi.web.dao.base;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.bis.gfi.entities.BaseEntity;

/**
 * @author Obi
 *
 */
@Repository
public class StandardJpaEntityBaseDao<T extends BaseEntity> {
	
	final static Logger log = LoggerFactory.getLogger(StandardJpaEntityBaseDao.class);
	
	@PersistenceContext
    protected EntityManager em;
	
	private CriteriaBuilder criteriaBuilder;

    private SpringDataJpaEntityBaseDao genericRepo;

    public Session getSession() {
        Session session = em.unwrap(Session.class);
        session.setHibernateFlushMode(FlushMode.ALWAYS);
        return session;
    }

    protected Class<T> clazz;

    public void setClazz(Class<T> clazzToSet) {
        this.clazz = clazzToSet;
    }
    
    @Transactional
    public T create(T entity) {
    	if (entity.getLastUpdatedBy() == null) {
            entity.setLastModified(new Date());
        }
        em.persist(entity);
        return entity;
    }
    
    @Transactional
    public void update(T entity) {
        if (entity.getLastUpdatedBy() == null) {
            entity.setLastModified(new Date());
        }
        em.merge(entity);
    }

    @Transactional
    public Collection<T> updateCollection(Collection<T> entities) {
        entities.stream().forEach((t) -> {
            update(t);
        });
        return entities;
    }
    
    @Transactional
    public synchronized void syncUpdate(T entity) {
        if (entity.getLastUpdatedBy() == null) {
            entity.setLastModified(new Date());
        }
        em.merge(entity);
    }

    @Transactional
    public void delete(T t) {
        em.remove(t);
    }

    @Transactional
    public void delete(long id) {
        T t = em.find(clazz, id);
        em.remove(t);
    }
    
    @Transactional
    public void delete(Collection<T> entities) {
        entities.stream().forEach((t) -> {
        	delete(t);
        });
    }

    public List<T> findAll(int pageNum, int pageSize) {
        return em.createQuery("SELECT m FROM " + clazz.getSimpleName() + "  m", clazz).setFirstResult(pageNum).setMaxResults(pageSize).getResultList();
    }

    /*@SuppressWarnings("JPQLValidation")
    public PageResult<T> findPaAll(int pageNum, int pageSize) {
        List<T> content = em.createQuery("SELECT m FROM " + clazz.getSimpleName() + "  m", clazz).setFirstResult(pageNum).setMaxResults(pageSize).getResultList();
        long count = findAllCount();
        PageResult<T> page = new PageResult<>(content, count, count);
        return page;

    }*/

    public List<T> findAll() {
        return em.createQuery("SELECT m FROM " + clazz.getSimpleName() + "  m", clazz).getResultList();
    }

    public List<T> executeNativeQuery(String query) {
        return em.createNativeQuery(query, clazz).getResultList();
    }

    public long findAllCount() {
        return em.createQuery("SELECT COUNT(m) FROM " + clazz.getSimpleName() + " m", Long.class).getSingleResult();
    }

    public T findById(long id) {
        return em.find(clazz, id);
    }

    @SuppressWarnings({"unchecked", "hiding"})
    public <T> List<T> executeQuery(String query, Map<String, Object> paramsMap) {
        List<T> entityList = new ArrayList<>();
        Session session = null;
        try {
            session = getSession();
            Query<T> q = session.createQuery(query);
            if (paramsMap != null) {
                paramsMap.keySet().stream().forEach((key) -> {
                    q.setParameter(key, paramsMap.get(key));
                });
            }
            entityList = q.getResultList();
        } catch (Exception ex) {
//        	report the error back to the caller
            throw ex;
        }
        return entityList;
    }

    @SuppressWarnings({"unchecked", "hiding"})
    public <T> T executeQueryUniqueResult(String query, Map<String, Object> paramsMap) {
        T result = null;
        List<T> entityList = new ArrayList<>();
        Session session = null;
        try {
            session = getSession();
            Query<T> q = session.createQuery(query);
            if (paramsMap != null) {
                paramsMap.keySet().stream().forEach((key) -> {
                    q.setParameter(key, paramsMap.get(key));
                });
            }
            entityList = q.getResultList();
            if (entityList != null && !entityList.isEmpty()) {
                result = entityList.get(0);
            }
        } catch (NoResultException ex) {
//        	I don't understand why this warrants an exception in the JPA standard so lets just catch it quietly, return null and move on.
            log.debug("no result found. query : " + query + ", params : " + paramsMap);
        } catch (Exception ex) {
//        	report the error back to the caller
            throw ex;
        }
        return result;
    }

    @SuppressWarnings({"unchecked", "hiding"})
    public <T> List<T> executeQuery(String query,
            Map<String, Object> paramsMap, int start, int size) {
        List<T> entityList = new ArrayList<>();
        Session session = null;
        try {
            session = getSession();
            Query<T> q = session.createQuery(query);
            if (paramsMap != null) {
                paramsMap.keySet().stream().forEach((key) -> {
                    q.setParameter(key, paramsMap.get(key));
                });
            }
            q.setFirstResult(start);
            q.getResultList();
            q.setMaxResults(size);
            entityList = q.getResultList();
        } catch (Exception ex) {
//        	report the error back to the caller
            throw ex;
        }
        return entityList;
    }

    @Transactional
    public List<T> findAllViaCriteria() {
        CriteriaBuilder mcriteriaBuilder = getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = mcriteriaBuilder.createQuery(clazz);
        Root<T> root = criteriaQuery.from(clazz);
        criteriaQuery.select(root);
        return em.createQuery(criteriaQuery).getResultList();
    }

    @Transactional
    public List<T> findViaCriteria(int start, int size) {
        List<T> entityList = new ArrayList<>();
        CriteriaBuilder mcriteriaBuilder = getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = mcriteriaBuilder.createQuery(clazz).distinct(true);
        Root<T> root = criteriaQuery.from(clazz);
        criteriaQuery.select(root);
        entityList = em.createQuery(criteriaQuery).setFirstResult(start).setMaxResults(size).getResultList();
        return entityList;
    }

    @Transactional
    public long countAllViaCriteria() {
        CriteriaBuilder mcriteriaBuilder = getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = mcriteriaBuilder.createQuery(Long.class);
        criteriaQuery.select(mcriteriaBuilder.count(criteriaQuery.from(clazz))).distinct(true);
        return em.createQuery(criteriaQuery).getSingleResult();
    }

    /*@Transactional
    public PageResult<T> getPageData(int pageNum, int pageSize) {
        if (pageNum == 0) {
            pageNum = 1;
        }
        int pageNume = (pageNum - 1);
        Pageable pageAble = new PageRequest(pageNume, pageSize, Sort.Direction.DESC, "id");
        Page<T> pageResult = getGenericRepo().findAll(pageAble);
        PageResult<T> page = new PageResult<>(pageResult.getContent(), pageResult.getTotalElements(), pageResult.getTotalElements());
        return page;
    }*/

    public CriteriaBuilder getCriteriaBuilder() {
        if (criteriaBuilder == null) {
            criteriaBuilder = em.getCriteriaBuilder();
        }
        return criteriaBuilder;
    }

    public SpringDataJpaEntityBaseDao getGenericRepo() {
        if (genericRepo == null) {
            genericRepo = new ExtendedSpringDataJpaRepository(clazz, em);
        }
        return genericRepo;
    }

    /**
     *
     * @param criteriaQuery
     * @return returns a single result safely regardless of multiple matching
     * entries by setting max result to one
     */
    @Transactional
    public T getSafeUniqueResult(CriteriaQuery<T> criteriaQuery) {
        T result = null;
        List<T> resultList = em.createQuery(criteriaQuery).setMaxResults(1).getResultList();
        if (resultList != null && !resultList.isEmpty()) {
            result = resultList.get(0);
        }
        return result;
    }
    
}