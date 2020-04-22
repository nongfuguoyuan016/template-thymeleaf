
package com.xc.template.system.service;

import com.xc.template.common.service.CrudService;
import com.xc.template.common.utils.CacheUtils;
import com.xc.template.system.dao.DictDao;
import com.xc.template.system.entity.Dict;
import com.xc.template.system.utils.DictUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 字典Service
 * @author ganjinhua
 * @version 2014-05-16
 */
@Service
@Transactional(readOnly = true)
public class DictService extends CrudService<DictDao, Dict> {
	
	/**
	 * 查询字段类型列表
	 * @return
	 */
	public List<String> findTypeList(){
		return dao.findTypeList(new Dict());
	}

	@Override
	@Transactional(readOnly = false)
	public int save(Dict dict) {
		CacheUtils.remove(DictUtils.CACHE_DICT_LIST);
		return super.save(dict);
	}

	@Override
	@Transactional(readOnly = false)
	public int delete(Dict dict) {
		CacheUtils.remove(DictUtils.CACHE_DICT_LIST);
		return super.delete(dict);
	}

}
