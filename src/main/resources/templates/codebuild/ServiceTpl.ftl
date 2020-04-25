package ${packageName}.service;

import ${packageName}.entity.${className};
import ${packageName}.dao.${className}Dao;
import com.xc.template.common.service.CrudService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ${className}Service extends CrudService<${className}Dao,${className}> {

}