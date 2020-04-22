
package com.xc.template.system.utils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.xc.template.common.utils.CacheUtils;
import com.xc.template.common.utils.SpringContextHolder;
import com.xc.template.common.utils.StringUtils;
import com.xc.template.system.dao.DictDao;
import com.xc.template.system.entity.Dict;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 字典工具类
 * @author ganjinhua
 * @version 2013-5-29
 */
public class DictUtils {
	
	private static DictDao dictDao = null;

	public static final String CACHE_DICT_LIST = "dictList";

	private static DictDao dictDao() {
		if (dictDao == null) {
			dictDao = SpringContextHolder.getBean(DictDao.class);
		}
		return dictDao;
	}
	
	public static String getDictLabel(String value, String type, String defaultValue){
		if (StringUtils.isNotBlank(type) && StringUtils.isNotBlank(value)){
			for (Dict dict : getDictList(type)){
				if (type.equals(dict.getType()) && value.equals(dict.getValue())){
					return dict.getLabel();
				}
			}
		}
		return defaultValue;
	}
	
	public static String getDictLabels(String values, String type, String defaultValue){
		if (StringUtils.isNotBlank(type) && StringUtils.isNotBlank(values)){
			List<String> valueList = Lists.newArrayList();
			for (String value : StringUtils.split(values, ",")){
				valueList.add(getDictLabel(value, type, defaultValue));
			}
			return StringUtils.join(valueList, ",");
		}
		return defaultValue;
	}

	public static String getDictValue(String label, String type, String defaultLabel){
		if (StringUtils.isNotBlank(type) && StringUtils.isNotBlank(label)){
			for (Dict dict : getDictList(type)){
				if (type.equals(dict.getType()) && label.equals(dict.getLabel())){
					return dict.getValue();
				}
			}
		}
		return defaultLabel;
	}

	public static String getDictId(String type, String label){
		if (StringUtils.isNotBlank(type) && StringUtils.isNotBlank(label)){
			for (Dict dict : getDictList(type)){
				if (type.equals(dict.getType()) && label.equals(dict.getLabel())){
					return dict.getId();
				}
			}
		}
		return null;
	}

	public static String getDictIdByTypeAndValue(String type, String value){
		if (StringUtils.isNotBlank(type) && StringUtils.isNotBlank(value)){
			for (Dict dict : getDictList(type)){
				if (type.equals(dict.getType()) && value.equals(dict.getValue())){
					return dict.getId();
				}
			}
		}
		return null;
	}


	public static Dict getDict(String type, String id) {
		Dict dict = null;
		if (StringUtils.isNotEmpty(type) && StringUtils.isNotEmpty(id)) {
			for (Dict d : getDictList(type)){
				if (type.equals(d.getType()) && id.equals(d.getId())){
					return d;
				}
			}
		}
		return dict;
	}

	public static List<Dict> getAllDictList() {
		List<Dict> dictList = (List<Dict>)CacheUtils.get(CACHE_DICT_LIST);
		if (CollectionUtils.isEmpty(dictList)){
			dictList = dictDao().findAllList(new Dict());
			CacheUtils.put(CACHE_DICT_LIST, dictList);
		}
		return CollectionUtils.isEmpty(dictList) ? Collections.emptyList() : dictList;
	}

	public static List<Dict> getDictList(String type){
		return getAllDictList().stream().filter(d -> d.getType().equals(type)).
				sorted(Comparator.comparingInt(Dict::getSort)).collect(Collectors.toList());
	}

	public static List<Dict> getAllFirstDictList() {
		return getAllDictList().stream().filter(d -> "0".equals(d.getParentId())).
				sorted(Comparator.comparing(Dict::getType)).collect(Collectors.toList());
	}

	/**
	 * 根据id查询dict
	 * @param id
	 * @return
	 */
	public static Dict getDictById(String id){
		Dict dict = new Dict();
		if(StringUtils.isNotBlank(id)){
			dict = dictDao().getDictById(id);
		}
		return dict;
	}
}
