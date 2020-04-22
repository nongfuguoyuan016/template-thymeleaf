/**

 */
package com.xc.template.system.dao;

import com.xc.template.common.persistence.CrudDao;
import com.xc.template.system.entity.Log;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LogDao extends CrudDao<Log> {

}
