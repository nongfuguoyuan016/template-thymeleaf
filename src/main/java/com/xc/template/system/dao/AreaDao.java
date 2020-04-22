/**

 */
package com.xc.template.system.dao;

import com.xc.template.common.persistence.TreeDao;
import com.xc.template.system.entity.Area;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AreaDao extends TreeDao<Area> {
	
}
