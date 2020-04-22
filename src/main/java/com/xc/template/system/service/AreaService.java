
package com.xc.template.system.service;

import com.xc.template.common.service.TreeService;
import com.xc.template.system.dao.AreaDao;
import com.xc.template.system.entity.Area;
import com.xc.template.system.utils.UserUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 区域Service
 * @author ganjinhua
 * @version 2014-05-16
 */
@Service
@Transactional(readOnly = true)
public class AreaService extends TreeService<AreaDao, Area> {

	public List<Area> findAll(){
		return UserUtils.getAreaList();
	}

	@Override
	@Transactional(readOnly = false)
	public int save(Area area) {
		UserUtils.removeCache(UserUtils.CACHE_AREA_LIST);
		return super.save(area);
	}
	
	@Override
	@Transactional(readOnly = false)
	public int delete(Area area) {
		UserUtils.removeCache(UserUtils.CACHE_AREA_LIST);
		return super.delete(area);
	}
	
}
