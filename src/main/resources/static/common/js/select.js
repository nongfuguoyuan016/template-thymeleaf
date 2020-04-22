/**
 * 注册树形选择器
 * @param eleId 元素id
 * @param treeSelectCallBack 选择后的回调函数
 */
function registTreeSelect(eleId, treeSelectCallBack) {
    if(!eleId) {
        return ;
    }
    var eleIdSelector = "#" + eleId;
    var btnIdSelector = "#" + eleId + "Btn";
    var inputIdSelector = "#" + eleId + "Id";
    var inputNameSelector = "#" + eleId + "Name";
    // 树结构数据地址
    var url = $(eleIdSelector).data('url') || '';
    // 标题
    var title = $(eleIdSelector).data('title') || '';
    // 排除掉的编号（不能选择的编号）
    var extId = $(eleIdSelector).data('extId') || '';
    // 是否显示复选框，如果不需要返回父节点，请设置notAllowSelectParent为true
    var checked = $(eleIdSelector).data("checked") || false;
    // 是否列出全部数据，设置true则不进行数据权限过滤（目前仅对Office有效）
    var isAll = $(eleIdSelector).data("isAll") || false;
    // 不允许选择根节点
    var notAllowSelectRoot = $(eleIdSelector).data("notAllowSelectRoot") || false;
    // 不允许选择父节点
    var notAllowSelectParent = $(eleIdSelector).data("notAllowSelectParent") || false;

    $(btnIdSelector+','+inputNameSelector).click(function () {
        // 是否限制选择，如果限制，设置为disabled
        if ($(btnIdSelector).hasClass("disabled")) {
            return true;
        }
        var contextPath = getContextPath();
        var content = contextPath + '/tag/treeselect?url=' + encodeURIComponent(contextPath + url) + '&checked='+checked+'&extId='+extId+'&isAll='+isAll;
        layer.open({
            type: 2,
            fix: false, //不固定
            title: title,
            area:['300px','400px'],
            content: content ,
            btn:['确定','清空','取消'],
            yes:function(index,layero){
                var tree = layero.find("iframe")[0].contentWindow.tree;
                var ids = [], names = [], nodes = [];
                if (checked) {
                    nodes = tree.getCheckedNodes(true);
                } else {
                    nodes = tree.getSelectedNodes();
                }
                for (var i = 0; i < nodes.length; i++) {
                    // 如果为复选框选择，且不需要父节点则过滤掉父节点
                    if (checked && notAllowSelectParent && nodes[i].isParent) {
                        continue;
                    }
                    if (notAllowSelectRoot && nodes[i].level == 0) {
                        layer.msg("不能选择根节点（" + nodes[i].name + "）请重新选择。")
                        return false;
                    }
                    if (notAllowSelectParent && nodes[i].isParent) {
                        layer.msg("不能选择父节点（" + nodes[i].name + "）请重新选择。");
                        return false;
                    }
                    ids.push(nodes[i].id);
                    names.push(nodes[i].name);
                    if (!checked) { // 如果为非复选框选择，则返回第一个选择  </c:if>
                        break;
                    }
                }
                $(inputIdSelector).val(ids.join(",").replace(/u_/ig, "")).change();
                $(inputNameSelector).val(names.join(",")).change();
                if (treeSelectCallBack && typeof treeSelectCallBack == 'function') {
                    treeSelectCallBack(index,layero);
                }
                layer.close(index);
            },
            btn2:function(){
                $(inputIdSelector).val('');
                $(inputNameSelector).val('');
            },
            btnAlign: 'c'
        })
    });
}

/**
 * 注册图标选择
 * @param eleId
 */
function registIconSelect(eleId) {
    var eleIdSelector = "#" + eleId;
    var displayIdSelector = "#" + eleId + "Display";
    var btnIdSelector = "#" + eleId + "Button";
    $(btnIdSelector).click(function () {
        var contextPath = getContextPath();
        var content = contextPath + '/tag/iconselect?value=' + $(eleIdSelector).val();
        layer.open({
            type: 2,
            fix: false, //不固定
            title: '图标选择',
            area:['655px','500px'],
            content: content ,
            btn:['确定','取消'],
            yes:function(index,layero){
                var icon = layero.find("iframe")[0].contentWindow.$("#icon").val();
                $(eleIdSelector).val(icon);
                $(displayIdSelector).text(icon);
                layer.close(index);
            },
            btn2:function(){
            },
            btnAlign: 'c'
        })
    });
}