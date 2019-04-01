/**
 * 
 */
package com.gfi.web.dao.base;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import com.bis.gfi.entities.BaseEntity;

/**
 * @author Obi
 *
 */
@NoRepositoryBean
public interface SpringDataJpaEntityBaseDao<T extends BaseEntity> extends JpaRepository<T, Long>,JpaSpecificationExecutor<T> {

}

