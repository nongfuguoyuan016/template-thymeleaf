/**

 */
package com.xc.template.system.dao;

import com.xc.template.common.persistence.CrudDao;
import com.xc.template.system.entity.Dict;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface DictDao extends CrudDao<Dict> {

	List<String> findTypeList(Dict dict);

	List<Dict> findAllListByParentId(Dict dict);

	Dict getDictById(String id);
}
