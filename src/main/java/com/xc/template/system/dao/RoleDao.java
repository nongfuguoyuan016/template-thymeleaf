/**

 */
package com.xc.template.system.dao;

import com.xc.template.common.persistence.CrudDao;
import com.xc.template.system.entity.Role;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RoleDao extends CrudDao<Role> {

	Role getByName(Role role);
	
	Role getByEnname(Role role);

	/**
	 * 维护角色与菜单权限关系
	 * @param role
	 * @return
	 */
	int deleteRoleMenu(Role role);

	int insertRoleMenu(Role role);
	
	/**
	 * 维护角色与公司部门关系
	 * @param role
	 * @return
	 */
	int deleteRoleOffice(Role role);

	int insertRoleOffice(Role role);

	/**
	 * 获取最大的序号数
	 * @return
	 */
	Integer getMaxSortNumber();

}
