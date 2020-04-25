package ${packageName}.dao;

import ${packageName}.entity.${className};
import com.xc.template.common.persistence.CrudDao;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ${className}Dao extends CrudDao<${className}> {

}