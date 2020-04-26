
package com.xc.template.system.service;

import com.xc.template.common.service.TreeService;
import com.xc.template.system.dao.OfficeDao;
import com.xc.template.system.entity.Office;
import com.xc.template.system.utils.UserUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 机构Service
 * @author ganjinhua
 * @version 2014-05-16
 */
@Service
@Transactional(readOnly = true)
public class OfficeService extends TreeService<OfficeDao, Office> {

	public List<Office> findAll(){
		return UserUtils.getOfficeList();
	}

	public List<Office> findList(Boolean isAll){
		if (isAll != null && isAll){
			return UserUtils.getOfficeAllList();
		}else{
			return UserUtils.getOfficeList();
		}
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<Office> findList(Office office){
		String parentIds = null == office.getParentIds() ? "" : office.getParentIds();
		office.setParentIds(parentIds  + "%");
		return dao.findByParentIdsLike(office);
	}
	
	@Override
	@Transactional(readOnly = false)
	public int save(Office office) {
		UserUtils.removeCache(UserUtils.CACHE_OFFICE_LIST);
		return super.save(office);
	}
	
	@Override
	@Transactional(readOnly = false)
	public int delete(Office office) {
		UserUtils.removeCache(UserUtils.CACHE_OFFICE_LIST);
		return super.delete(office);
	}
	
}
