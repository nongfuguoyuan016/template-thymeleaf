package ${packageName}.entity;

import com.xc.template.common.persistence.DataEntity;
import com.alibaba.fastjson.JSON;
<#if dateImport>
import java.util.Date;
</#if>

/**
 * ${comment}实体
 * @author xuchang
 * @Date ${createDate}
 */
public class ${className} extends DataEntity<${className}> {

    private static final long serialVersionUID = 1L;

<#list cols as col>
    <#if col.show>
    <#if col.comment?default("")?trim?length gt 1>
    // ${col.comment}
    </#if>
    private ${col.type} ${col.camelName};

    </#if>
</#list>
<#list cols as col>
    <#if col.show>
    public ${col.type} get${col.upCamelName}() {
        return ${col.camelName};
    }

    public void set${col.upCamelName}(${col.type} ${col.camelName}) {
        this.${col.camelName} = ${col.camelName};
    }

    </#if>
</#list>

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

}