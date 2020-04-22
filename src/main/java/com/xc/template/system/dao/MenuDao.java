/**

 */
package com.xc.template.system.dao;

import com.xc.template.common.persistence.CrudDao;
import com.xc.template.system.entity.Menu;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface MenuDao extends CrudDao<Menu> {

	List<Menu> findByParentIdsLike(Menu menu);

	List<Menu> findByUserId(Menu menu);
	
	int updateParentIds(Menu menu);
	
	int updateSort(Menu menu);
	
}
