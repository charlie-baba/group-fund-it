/**
 * 
 */
package com.gfi.web.dao;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Repository;

import com.bis.gfi.entities.ProjectCategory;
import com.gfi.web.dao.base.StandardJpaEntityBaseDao;

/**
 * @author Obi
 *
 */
@Repository
public class ProjectCategoryDao extends StandardJpaEntityBaseDao<ProjectCategory> {

	@PostConstruct
    public void init() {
        super.setClazz(ProjectCategory.class);
	}
	
}
