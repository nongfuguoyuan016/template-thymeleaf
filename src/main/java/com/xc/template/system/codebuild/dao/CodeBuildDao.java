package com.xc.template.system.codebuild.dao;

import com.xc.template.system.codebuild.entity.Column;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * @author: xuchang
 * @date: 2019/9/29
 */
@Mapper
public interface CodeBuildDao {

    @Select("select column_name as name, column_comment as comment, case data_type " +
            "when 'int'  then 'Integer' when 'tinyint'  then 'Integer' when 'smallint'  then 'Integer' when 'bigint'  then 'Integer' " +
            "when 'float' then 'Float' when 'double' then 'Double' when 'decimal' then 'Double' " +
            "when 'date' then 'Date' when 'time' then 'Date' when 'year' then 'Date' " +
            "when 'datetime' then 'Date' when 'timestamp' then 'Date' else 'String' end as type, " +
            "case is_nullable when 'YES' then 'false' else 'true' end as required, " +
            "character_maximum_length as maxCharLength,numeric_precision as maxNumLength,numeric_scale as maxScaleLength " +
            "from information_schema.columns where table_name = #{0} and table_schema = 'wskj'")
    List<Column> getColumnsByTableNameForMySQL(String tableName);

    @Select("select table_name as name,date_format(create_time,'%Y-%m-%d %T') as createDate,table_comment as comment " +
            "from information_schema.tables where table_schema = 'wskj' order by createDate desc")
    List<Map<String,Object>> getTableData();
}
