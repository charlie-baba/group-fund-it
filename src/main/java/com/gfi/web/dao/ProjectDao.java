/**
 * 
 */
package com.gfi.web.dao;

import static com.gfi.web.util.AppConstants.df;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.bis.gfi.entities.Project;
import com.gfi.web.dao.base.StandardJpaEntityBaseDao;
import com.gfi.web.pojo.ProjectDto;

/**
 * @author Obi
 *
 */
@Repository
public class ProjectDao extends StandardJpaEntityBaseDao<Project> {

	@PostConstruct
    public void init() {
        super.setClazz(Project.class);
	}
	
	@Transactional
	public List<Project> findByGroup(Long groupId) {
		String hql = "Select p from Project p where p.group.id = :groupId order by p.id desc";
		HashMap<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("groupId", groupId);
        return executeQuery(hql, paramsMap);
	}
	
	@Transactional
	public List<Project> findAllByAdmin(String email) {
		String hql = "Select p from Project p where p.createdBy = :email order by p.id desc";
		HashMap<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("email", email);
        return executeQuery(hql, paramsMap);
	}
	
	@Transactional
	public long countProjects(String email) {
		String hql = "Select count(p) from Project p where p.createdBy = :email";
		HashMap<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("email", email);
        return executeQueryUniqueResult(hql, paramsMap);
	}
	
	@Transactional
	public List<Project> findAllByEmail(String email) {
		//may need optimisation
		String hql = "Select p from Project p left join fetch p.group g where g in (Select m.group from GroupMembers m where m.email = :email) "
				+ "and p.active is true and p.completed is false"; // and DATE(p.startDate) <= :today and DATE(p.endDate) >= :today
		HashMap<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("email", email);
        return executeQuery(hql, paramsMap);
	}
	
	@Transactional
	public long countMyPrivateProjectsByEmail(String email) {
		String hql = "Select count(p) from Project p where p.group in (Select m.group from GroupMembers m where m.email = :email) "
				+ "and p.privateProject is true and p.active is true and p.completed is false";
		HashMap<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("email", email);
        return executeQueryUniqueResult(hql, paramsMap);
	}
	
	@Transactional
	public List<Object[]> findRecentProjectsByEmail(String email, int start, int size) {
		String hql = "Select p.id, p.name, p.targetAmount from Project p where p.group in (Select m.group from GroupMembers m where m.email = :email) "
				+ "and p.active is true and p.completed is false order by p.createDate desc";
		HashMap<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("email", email);
        return executeQuery(hql, paramsMap, start, size);
	}
	
	@Transactional
	public List<Project> findAllPublicProjects() {
		//may need optimisation (needs only group id and name)
		String hql = "Select p from Project p left join fetch p.group g where p.privateProject is false and p.active is true and p.completed is false ";
		HashMap<String, Object> paramsMap = new HashMap<>();
        return executeQuery(hql, paramsMap);
	}
	
	@Transactional
	public long countAllPublicProjects() {
		String hql = "Select count(p) from Project p where p.privateProject is false and p.active is true and p.completed is false";
		HashMap<String, Object> paramsMap = new HashMap<>();
        return executeQueryUniqueResult(hql, paramsMap);
	}
	
	@Transactional
	public long countProjectsInGroup(Long groupId) {
		String hql = "Select count(p) from Project p where p.group.id = :groupId";
		HashMap<String, Object> paramsMap = new HashMap<>();
		paramsMap.put("groupId", groupId);
        return executeQueryUniqueResult(hql, paramsMap);
	}
	
	@Transactional
	public Project findProjectEagerlyById(Long projectId) {
		String hql = "Select p from Project p left join fetch p.group where p.id = :projectId";
		HashMap<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("projectId", projectId);
        return executeQueryUniqueResult(hql, paramsMap);
	}
		
	@Transactional
	public Project findByLink(String link) {
		String hql = "Select p from Project p left join fetch p.group where p.link = :link and p.active is true and p.completed is false";
		HashMap<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("link", link);
        return executeQueryUniqueResult(hql, paramsMap);
	}
	
	@Transactional
	public List<ProjectDto> findAllProjects() {
		String hql = "Select g.id, g.name, g.createdBy, g.createDate, g.active from Project g order by g.createDate desc";
		HashMap<String, Object> paramsMap = new HashMap<>();
        return executeQuery(hql, paramsMap);
	}
	
	@Transactional
	public ProjectDto findProjectDetails(Long projectId) {
		String hql = "Select g.id, g.name, g.createdBy, g.createDate, g.active, g.description, g.category.name, g.startDate, g.endDate, g.targetAmount, "
				+ "g.privateProject, g.completed, g.projectImages from Project g where g.id = :projectId";
		HashMap<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("projectId", projectId);
        return transformToProject(executeQueryUniqueResult(hql, paramsMap));
	}

	private ProjectDto transformToProject(Object[] obj) {
		if(obj == null) {
			return null;
		}
		
		ProjectDto project = new ProjectDto();
		project.setId((Long) obj[0]);
		project.setName((String) obj[1]);
		project.setCreatedBy((String) obj[2]);
		project.setCreateDate((Date) obj[3]);
		project.setActive((boolean) obj[4]);
		project.setDescription((String) obj[5]);
		project.setCategory((String) obj[6]);
		project.setStartDate((Date) obj[7]);
		project.setEndDate((Date) obj[8]);
		Double amount = (Double) obj[9];
		project.setTargetAmount(df.format(amount == null ? 0D : amount));
		project.setPrivateProject((boolean) obj[10]);
		project.setPaidOut((boolean) obj[11]);
		project.setImageUrls((String) obj[12]);
		return project;
	}
}
