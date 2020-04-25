<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head th:replace="common/head::common_head(~{::title},~{::link},~{::style})">
    <title>${comment}管理</title>
    <link th:href="@{/static/bootstrapTable/table.css}" rel="stylesheet"/>
    <link th:href="@{/static/bootstrapTable/bootstrap-table.css}" rel="stylesheet"/>
    <style type="text/css">
        <#noparse>#</#noparse>${camelTableName}Table{
            display: table;
        }
    </style>
</head>
<body>
    <nav th:replace="common/body::breadcrumb"></nav>
    <div class="Hui-admin-content clearfix">
        <div class="text-c">
            <form class="form form-horizontal">
              <#list cols as col>
                <#if col.show>
                ${col.comment?default(col.camelName)}:<input name="${col.camelName}" id="${col.camelName}" htmlEscape="false" style="width:200px" class="input-text" type="text"/>
                </#if>
              </#list>
                <button type="button" class="btn radius btn-success" id="search_btn"><i class="Hui-iconfont"><#noparse>&#</#noparse>xe665;</i> 搜索</button>
            </form>
        </div>
        <div class="table-box">
            <shiroPermission name="${moduleName}:edit">
                <div class="cl pd-5 bg-1 bk-gray"> <span class="l"> <a class="btn btn-primary radius" href="javascript:;" id="add_btn"><i class="Hui-iconfont"><#noparse>&#</#noparse>xe600;</i> 添加${comment}</a> </span> </div>
            </shiroPermission>
            <div id="${camelTableName}Table"></div>
        </div>
    </div>
    <div th:replace="common/foot::common_foot"></div>
</body>

<script type="text/javascript" th:src="@{/static/bootstrapTable/bootstrap-table.js}"></script>
<script type="text/javascript" th:src="@{/static/bootstrapTable/bootstrap-table-zh-CN.js}"></script>
<script type="text/javascript">
    var temp = {};
    <#noparse>$</#noparse>(document).ready(function() {

        $("#add_btn").click(function(){
            var title = '添加${comment}';
            var url = "[[@{/${path}/form}]]";
            layer_show_full(title,url);
        })

        $("#search_btn").click(function(){
            temp = {
            <#list cols as col>
            <#if col.show>
            ${col.camelName}: <#noparse>$</#noparse>("<#noparse>#</#noparse>${col.camelName}").val()<#if col_has_next>,</#if>
            </#if>
            </#list>
        }
            $("<#noparse>#</#noparse>${camelTableName}Table").bootstrapTable("refresh",{pageNumber:1});
        });

        getTableData();
    });

    function edit(id){
        var title = '编辑${comment}';
        var url = "[[@{/${path}/form?id=}]]"+id;
        layer_show_full(title,url);
    }

    function remove(id){
        var url = "[[@{${path}/delete?id=}]]"+id;
        removeItem(url,"<#noparse>#</#noparse>${camelTableName}Table");
    }

    function getTableData(){
        <#noparse>$</#noparse>("<#noparse>#</#noparse>${camelTableName}Table").bootstrapTable({
            method:'get',
            cache:'false',
            dataType:'JSON',
            pagination: true,
            pageNumber:1,
            pageSize: 10,
            url:"[[@{/${path}/list/data}]]",
            sidePagination:'server',
            search:false,
            queryParams:requestParams,
            idField:"id",
            pageList: [10,15,20],
            columns:[
                {
                    title:'序号',
                    align:'center',
                    width:40,
                    formatter:function(value, row, index) {
                        var pageSize=<#noparse>$</#noparse>("<#noparse>#</#noparse>${camelTableName}Table").bootstrapTable('getOptions').pageSize;
                        var pageNumber=<#noparse>$</#noparse>("<#noparse>#</#noparse>${camelTableName}Table").bootstrapTable('getOptions').pageNumber;
                        return pageSize*(pageNumber-1)+index+1;
                    }
                },
                <#list cols as col>
                <#if col.show>
                {
                    field:'${col.camelName}',
                    title:'${col.comment?default(col.camelName)}',
                    align:'center'
                },
                </#if>
                </#list>
                {
                    field: '_operate',
                    title:'操作',
                    align:'center',
                    formatter:function(value,row,index){
                        var id = row.id ;
                        var e = '<a title="编辑" style="text-decoration:none" onclick="edit(\''+id+'\')"><i class="Hui-iconfont"><#noparse>&#</#noparse>xe6df;</i></a>';
                        var d = '<a title="删除" style="text-decoration:none" onclick="remove(\''+id+'\')"><i class="Hui-iconfont"><#noparse>&#</#noparse>xe6e2;</i></a>';
                        return e+d;
                    }
                }
            ]
        });

        var text = $("#add_btn").text() ;
        if('' == text || null == text ||'undefined' == text ){
            $("<#noparse>#</#noparse>${camelTableName}Table").bootstrapTable('hideColumn','_operate');
        }
    };

    function requestParams(params){
        temp.pageSize = params.limit;
        temp.pageNo = params.offset/params.limit+1;
        return temp ;
    }

</script>
</html>