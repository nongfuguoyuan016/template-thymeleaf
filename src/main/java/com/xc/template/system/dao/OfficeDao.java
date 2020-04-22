/**

 */
package com.xc.template.system.dao;

import com.xc.template.common.persistence.TreeDao;
import com.xc.template.system.entity.Office;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OfficeDao extends TreeDao<Office> {

    /**
     * 根据固定grade查找Office集合
     * @param grade
     * @return
     */
    List<Office> findByGrade(@Param("grade") String grade);
}
