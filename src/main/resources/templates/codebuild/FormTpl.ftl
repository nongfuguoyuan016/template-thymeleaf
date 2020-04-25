<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head th:replace="common/head::common_head(~{::title},~{},~{::style})">
    <title>${comment}修改</title>
    <style type="text/css">
        body{
            min-height: 98%;
        }
        .form{
            padding: 1%;
        }

    </style>
</head>
<body>
<form id="inputForm" th:object="<#noparse>$</#noparse>{${camelTableName}}" th:action="@{/${path}/save}" method="post" class="form form-horizontal">
    <input type="hidden" th:field="*{id}"/>
    <#list cols as col>
    <#if col.show>
    <div class="row cl">
        <label class="form-label col-xs-4 col-sm-3"><#if col.required><span class="c-red">*</span></#if>${col.comment?default("undefined")}：</label>
        <div class="formControls col-xs-8 col-sm-9">
            <input th:field="*{${col.camelName}}" class="input-text" htmlEscape="false" <#if col.maxCharLength??>maxlength="${col.maxCharLength}"<#elseif col.maxNumLength??>maxlength="<#if col.type=='Integer'>10<#else>${col.maxNumLength+1}</#if>"</#if> />
        </div>
    </div>
    </#if>
    </#list>
    <div class="row cl text-c">
        <shiroPermission name="${moduleName}:edit"><input id="btnSubmit" class="btn btn-primary radius" type="submit" value="&nbsp;&nbsp;保存&nbsp;&nbsp;"/></shiroPermission>
        <input id="back_btn" class="btn btn-default radius" type="button" value="&nbsp;&nbsp;返回&nbsp;&nbsp;"/>
    </div>
</form>
    <div th:replace="common/foot::common_foot"></div>
    <div th:replace="common/foot::validator"></div>
</body>

<script type="text/javascript">
    <#noparse>$</#noparse>(document).ready(function(){

        <#noparse>$</#noparse>("#back_btn").click(function(){layer_close();});

        <#noparse>$</#noparse>("<#noparse>#</#noparse>inputForm").validate({
            onfocusout: function(element) { <#noparse>$</#noparse>(element).valid(); },
            onsubmit:function(element) { <#noparse>$</#noparse>(element).valid(); },
            rules: {
                <#list cols as col>
                <#if col.show>
                ${col.camelName}:{
                    <#if col.required>
                    required:true,
                        </#if>
                        <#if col.maxCharLength??>
                    maxlength:${col.maxCharLength},
                    <#elseif col.maxNumLength??>
                    <#if col.type=='Integer'>
                    maxlength:10,
                    digits:true,
                        <#else>
                    maxlength:${col.maxNumLength+1},
                    positiveNumber: true
                    </#if>
                    </#if>
                }<#if col_has_next>,</#if>
                </#if>
                </#list>
            },
            messages: {
                <#list cols as col>
                <#if col.show>
                ${col.camelName}:{
                    <#if col.required>
                    required:"该项是必填项",
                    </#if>
                    <#if col.maxCharLength??>
                    maxlength:"最多可输入{0}个字符",
                    <#elseif col.maxNumLength??>
                    <#if col.type=='Integer'>
                    maxlength:"最多可输入10个字符",
                    digits:"请输入正整数",
                    <#else>
                    maxlength:"最多可输入{0}个字符"
                    </#if>
                    </#if>
                }<#if col_has_next>,</#if>
                </#if>
                </#list>
            },
            submitHandler: function(form){
                submitForm(form);
            },
            errorPlacement: function(error, element) {
                error.insertAfter(element);
            }
        });
    });
</script>
</html>