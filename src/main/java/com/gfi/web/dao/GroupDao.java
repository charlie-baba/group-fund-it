/**
 * 
 */
package com.gfi.web.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.bis.gfi.entities.Group;
import com.gfi.web.dao.base.StandardJpaEntityBaseDao;
import com.gfi.web.pojo.GroupDto;

/**
 * @author Obi
 *
 */
@Repository
public class GroupDao extends StandardJpaEntityBaseDao<Group> {

	@PostConstruct
    public void init() {
        super.setClazz(Group.class);
	}
	
	@Transactional
	public Group findWithBanksById(Long groupId) {
		String hql = "Select g from Group g left join fetch g.groupBanks where g.id = :groupId";
		HashMap<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("groupId", groupId);
        return executeQueryUniqueResult(hql, paramsMap);
	}
		
	@Transactional
	public Group findByNameAndAdmin(String groupName, String email) {
		String hql = "Select g from Group g where g.createdBy = :email and g.name = :groupName";
		HashMap<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("email", email);
        paramsMap.put("groupName", groupName);
        return executeQueryUniqueResult(hql, paramsMap);
	}
	
	@Transactional
	public List<Group> findAllByAdmin(String email) {
		String hql = "Select g from Group g where g.createdBy = :email order by g.id desc";
		HashMap<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("email", email);
        return executeQuery(hql, paramsMap);
	}
	
	@Transactional
	public List<Group> findMemberGroupsByEmail(String email) {
		String hql = "Select g from Group g where g in (Select m.group from GroupMembers m where m.email = :email)";
		HashMap<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("email", email);
        return executeQuery(hql, paramsMap);
	}
		
	@Transactional
	public List<Group> findMemberGroupsByEmail(String email, int start, int size) {
		String hql = "Select g from Group g where g in (Select m.group from GroupMembers m where m.email = :email) order by g.createDate desc";
		HashMap<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("email", email);
        return executeQuery(hql, paramsMap, start, size);
	}
	
	@Transactional
	public List<GroupDto> findAllGroups() {
		String hql = "Select g.id, g.name, g.createdBy, g.createDate, g.active from Group g order by g.createDate desc";
		HashMap<String, Object> paramsMap = new HashMap<>();
        return executeQuery(hql, paramsMap);
	}
	
	@Transactional
	public GroupDto findGroupDetails(Long groupId) {
		String hql = "Select g.id, g.name, g.createdBy, g.createDate, g.active, g.description, g.base64Image, "
				+ "g.privateGroup, g.allowAnonymousDonations from Group g where g.id = :groupId";
		HashMap<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("groupId", groupId);
        return transformToGroup(executeQueryUniqueResult(hql, paramsMap));
	}
	
	private GroupDto transformToGroup(Object[] obj) {
		if(obj == null) {
			return null;
		}
		
		GroupDto group = new GroupDto();
		group.setId((Long) obj[0]);
		group.setName((String) obj[1]);
		group.setCreatedBy((String) obj[2]);
		group.setCreateDate((Date) obj[3]);
		group.setActive((boolean) obj[4]);
		group.setDescription((String) obj[5]);
		group.setBase64Image((String) obj[6]);
		group.setPrivateGroup((boolean) obj[7]);
		group.setAllowAnonymous((boolean) obj[8]);
		return group;
	}
	
}
