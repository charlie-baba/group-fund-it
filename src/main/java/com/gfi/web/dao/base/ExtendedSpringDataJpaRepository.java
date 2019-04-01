/**
 * 
 */
package com.gfi.web.dao.base;

import javax.persistence.EntityManager;

import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import com.bis.gfi.entities.BaseEntity;

/**
 * @author Obi
 *
 */
public class ExtendedSpringDataJpaRepository<T extends BaseEntity> extends SimpleJpaRepository<T, Long> implements SpringDataJpaEntityBaseDao<T> {

    public ExtendedSpringDataJpaRepository(Class<T> domainClass, EntityManager em) {
        super(domainClass, em);
    }

}
