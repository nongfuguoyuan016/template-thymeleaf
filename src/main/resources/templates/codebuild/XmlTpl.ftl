<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="${packageName}.dao.${className}Dao">

    <sql id="allCols">
        <#list cols as col>
            a.${col.name} as "${col.camelName}<#if col.name=="create_by"||col.name=="update_by">.id</#if>"<#if col_has_next>,</#if>
        </#list>
    </sql>
    
    <sql id="joins"></sql>
    
    <select id="get" resultType="${className}">
        select <include refid="allCols" /> from ${tableName} a
        <include refid="joins" /> where a.id = <#noparse>#</#noparse>{id} and a.del_flag = '0'
    </select>

    <select id="findList" resultType="${className}">
        select <include refid="allCols" /> from ${tableName} a <include refid="joins" />
        <where>
            a.del_flag = <#noparse>#</#noparse>{DEL_FLAG_NORMAL}
        <#list cols as col>
        <#if (col.name!='id'&&col.name!='del_flag')>
            <if test="${col.camelName} != null<#if col.type=="String"> and ${col.camelName} != ''</#if>">
                and a.${col.name} = <#noparse>#</#noparse>{${col.camelName}<#if col.name=="create_by"||col.name=="update_by">.id</#if>}
            </if>
        </#if>
        </#list>
        </where>
        order by a.create_date desc
    </select>

    <select id="findAllList" resultType="${className}">
        select <include refid="allCols" /> from ${tableName} a <include refid="joins" />
        where a.del_flag = '0'
    </select>
    
    <insert id="insert">
        insert into ${tableName}(
        <#list cols as col>
            ${col.name}<#if col_has_next>,</#if>
        </#list>
        ) values (
        <#list cols as col>
            <#noparse>#</#noparse>{${col.camelName}<#if col.name=="create_by"||col.name=="update_by">.id</#if>}<#if col_has_next>,</#if>
        </#list>
        )
    </insert>

    <update id="update">
        update ${tableName}
        <set>
        <#list cols as col>
        <#if (col.name!='id'&&col.name!='del_flag')>
            <#if col.name!="create_by"&&col.name!="create_date">
                ${col.name} = <#noparse>#</#noparse>{${col.camelName}<#if col.name=="update_by">.id</#if>},
            </#if>
        </#if>
        </#list>
        </set>
        where id = <#noparse>#</#noparse>{id}
    </update>

    <update id="delete">
        update ${tableName} set del_flag = <#noparse>#</#noparse>{DEL_FLAG_DELETE} where id = <#noparse>#</#noparse>{id}
    </update>

</mapper>