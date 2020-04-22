/**
 * 通用js
 */
$(document).ready(function () {
    try {
        $("a").bind("focus", function () {
            if (this.blur) {
                this.blur()
            }
        });
        $("select").select2()
    } catch (a) {
    }
});

const floatOpe = function () {

    /*
     * 判断obj是否为一个整数
     */
    function isInteger(obj) {
        return Math.floor(obj) === obj
    }

    /*
     * 将一个浮点数转成整数，返回整数和倍数。如 3.14 >> 314，倍数是 100
     * @param floatNum {number} 小数
     * @return {object}
     *   {times:100, num: 314}
     */
    function toInteger(floatNum) {
        var ret = {times: 1, num: 0};
        if (isInteger(floatNum)) {
            ret.num = floatNum;
            return ret
        }
        var strfi = floatNum + '';
        var dotPos = strfi.indexOf('.');
        var len = strfi.substr(dotPos + 1).length;
        var times = Math.pow(10, len);
        var intNum = parseInt(floatNum * times + 0.5, 10);
        ret.times = times;
        ret.num = intNum;
        return ret
    }

    /*
     * 核心方法，实现加减乘除运算，确保不丢失精度
     * 思路：把小数放大为整数（乘），进行算术运算，再缩小为小数（除）
     *
     * @param a {number} 运算数1
     * @param b {number} 运算数2
     * @param op {string} 运算类型，有加减乘除（add/subtract/multiply/divide）
     *
     */
    function operation(a, b, op) {
        var o1 = toInteger(a);
        var o2 = toInteger(b);
        var n1 = o1.num;
        var n2 = o2.num;
        var t1 = o1.times;
        var t2 = o2.times;
        var max = t1 > t2 ? t1 : t2;
        var result = null;
        switch (op) {
            case 'add':
                if (t1 === t2) { // 两个小数位数相同
                    result = n1 + n2
                } else if (t1 > t2) { // o1 小数位 大于 o2
                    result = n1 + n2 * (t1 / t2)
                } else { // o1 小数位 小于 o2
                    result = n1 * (t2 / t1) + n2
                }
                return result / max;
            case 'subtract':
                if (t1 === t2) {
                    result = n1 - n2
                } else if (t1 > t2) {
                    result = n1 - n2 * (t1 / t2)
                } else {
                    result = n1 * (t2 / t1) - n2
                }
                return result / max;
            case 'multiply':
                result = (n1 * n2) / (t1 * t2);
                return result;
            case 'divide':
                result = (n1 / n2) * (t2 / t1);
                return result
        }
    }

    // 加减乘除的四个接口
    function add(a, b) {
        return operation(a, b, 'add')
    }

    function subtract(a, b) {
        return operation(a, b, 'subtract')
    }

    function multiply(a, b) {
        return operation(a, b, 'multiply')
    }

    function divide(a, b) {
        return operation(a, b, 'divide')
    }

    // exports
    return {
        add: add,
        subtract: subtract,
        multiply: multiply,
        divide: divide
    }
}();

/**
 * 获取context-path
 * @returns {string}
 */
function getContextPath() {
    var pathName = document.location.pathname;
    var index = pathName.substr(1).indexOf("/");
    var result = pathName.substr(0,index+1);
    return result;
}

/**
 * 生成唯一ID
 * @returns {string}
 * @constructor
 */
function GUID() {
    return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g,
        function(c) {
            var r = Math.random() * 16 | 0,
                v = c == 'x' ? r : (r & 0x3 | 0x8);
            return v.toString(16);
        });
}

/**
 *对日期进行格式化
 * @param date
 * @param format
 * @returns {*}
 */
function dateFormat(date,format) {
    var time;
    if(!date)return "";
    if(typeof date === 'string'){
        time = new Date(date.replace(/-/g,'/').replace(/T|Z/g,' ').trim());
    }else{
        time = new Date(date) ;
    }
    var o = {
        "M+": time.getMonth() + 1, //月份
        "d+": time.getDate(), //日
        "h+": time.getHours(), //小时
        "m+": time.getMinutes(), //分
        "s+": time.getSeconds(), //秒
        "q+": Math.floor((time.getMonth() + 3) / 3), //季度
        "S": time.getMilliseconds() //毫秒
    };
    if (/(y+)/.test(format)) format = format.replace(RegExp.$1, (time.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
        if (new RegExp("(" + k + ")").test(format)) format = format.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return format;
}

/**
 * 数字格式化, ","分隔
 * @param num
 * @returns {string}
 */
function numberThousandsFormat(num) {
    var num = (num || 0).toString(), dec = '', result = '';
    if(num.indexOf('.') > 0) {
        var nums = num.split('.');
        num = nums[0];
        dec = nums[1];
    }
    while (num.length > 3) {
        result = ',' + num.slice(-3) + result;
        num = num.slice(0, num.length - 3);
    }
    if (num) {
        result = num + result + (dec ? '.' + dec : dec);

    }
    return result;
}

/**
 * layer form 提交表单
 * @param f form对象
 * @author xuchang
 */
function submitForm(f){
    var u = $(f).attr("action");
    var d = $(f).serialize() ;
    submitFormByUrlAndData(u,d);
}

/**
 * layer form 提交表单
 * @param f         form对象
 * @param callback 成功回调
 */
function submitFormAndSuccess(f,callback) {
    var u = $(f).attr("action");
    var d = $(f).serialize() ;
    $.ajax({
        type:'POST',
        url:u,
        data:d,
        beforeSend:function(){
            layer.load(1,{shade: [0.8, '#393D49']});
        },
        success:callback,
        error:function(){
            layer.close(layer.index);
            layer.msg('操作异常 ,请稍后重试');
        }
    })
}

/**
 * 根据url和data提交表单
 * @param url
 * @param data
 */
function submitFormByUrlAndData(url,data){
    $.ajax({
        type:'POST',
        url:url,
        data:data,
        beforeSend:function(){
            layer.load(1,{shade: [0.8, '#393D49']});
        },
        success:function(data){
            layer.close(layer.index);
            var index = parent.layer.getFrameIndex(window.name);
            if(0==data.code){
                layer.msg(data.msg,{time:1000})
                setTimeout(function(){
                    parent.location.replace(parent.location.href);
                    parent.layer.close(index);
                },1000);

            }else{
                layer.msg(data.msg);
            }
        },
        error:function(){
            layer.close(layer.index);
            layer.msg('操作异常 ,请稍后重试');
        }
    })
}

function submitJSONData(url,jsonData){
    submitJSONDataAndSuccess(url,jsonData,function(data){
        layer.close(layer.index);
        var index = parent.layer.getFrameIndex(window.name);
        if(0==data.code){
            layer.msg(data.msg,{time:1000})
            setTimeout(function(){
                parent.location.replace(parent.location.href);
                parent.layer.close(index);
            },1000);

        }else{
            layer.msg(data.msg);
        }
    });
}

function submitJSONDataAndSuccess(url,jsonData,callback){
    $.ajax({
        type:'POST',
        url:url,
        data:jsonData,
        contentType:'application/json;charset=utf-8',
        beforeSend:function(){
            layer.load(1,{shade: [0.8, '#393D49']});
        },
        success:callback,
        error:function(){
            layer.close(layer.index);
            layer.msg('操作异常 ,请稍后重试');
        }
    })
}

/**
 * 删除单条记录
 * @param u url
 * @param e 表格选择器
 * @author xuchang
 */
function removeItem(u,e){
    layer.confirm('确定要删除吗？',{
        btn:['确定','取消']
    },function(index,layero){
        layer.close(index);
        var load = layer.load(1,{shade: [0.8, '#393D49']});
        $.ajax({
            type:"GET",
            url:u,
            success:function(data){
                layer.close(load);
                if(0==data.code){
                    layer.msg(data.msg,{time:1000});
                    setTimeout(function(){
                        layer.close(index);
                        $(e).bootstrapTable('refresh');
                    },1000)
                }else{
                    layer.msg(data.msg);
                }
            },
            error:function(){
                layer.close(load);
                layer.msg("操作异常 ，请稍后重试") ;
            }
        });
    },function(){})
}

/**
 * 删除单条记录并刷新当前页面
 * @param u url
 * @author xuchang
 */
function removeItemAndRefresh(u){
    layer.confirm('确定要删除吗？',{
        btn:['确定','取消']
    },function(index,layero){
        layer.close(index);
        var load = layer.load(1,{shade: [0.8, '#393D49']});
        $.ajax({
            type:"GET",
            url:u,
            success:function(data){
                layer.close(load);
                if(0==data.code){
                    layer.msg(data.msg,{time:1000});
                    setTimeout(function(){
                        location.replace(location.href);
                    },1000)
                }else{
                    layer.msg(data.msg);
                }
            },
            error:function(){
                layer.close(load);
                layer.msg("操作异常 ，请稍后重试") ;
            }
        });
    },function(){});
}

/**
 * 删除单条记录并执行成功回调
 * @param u
 * @param callback
 */
function removeItemAndSuccess(u,callback){
    layer.confirm('确定要删除吗？',{
        btn:['确定','取消']
    },function(index,layero){
        layer.close(index);
        $.ajax({
            type:"GET",
            url:u,
            beforeSend:function(){
                layer.load(1,{shade: [0.8, '#393D49']});
            },
            success:callback,
            error:function(){
                layer.close(layer.index);
                layer.msg("操作异常 ，请稍后重试") ;
            }
        });
    },function(){});
}

function getDictLabel(d, c, a) {
    for (var b = 0; b < d.length; b++) {
        var e = d[b];
        if (e.value == c) {
            return e.label
        }
    }
    return a
}

/**
 * 获取下个月
 * @param date
 * @returns {string}
 */
function getNextMonth(date) {
    var myDate = date ? date : new Date();
    var year = myDate.getFullYear(); //获取当前日期的年份
    var month = myDate.getMonth();
    var m = month + 1;
    if (m.toString().length == 1) {
        m = "0" + m;
    }
    var month2 = parseInt(m) + 1;
    if (month2 == 13) {
        year = parseInt(year) + 1;
        month2 = 1;
    }
    if (month2 < 10) {
        month2 = '0' + month2;
    }
    return year + '-' + month2;
}

/**
 * 序列化元素内的数据
 * @param ele 元素选择器
 */
function serializeElement(ele) {
    var data = {};
    $(ele).each(function(index,e){
        data[$(e).attr('name')] = $(e).val();
    })
    return data;
}

/**
 * 数据转换为参数字符串
 * @param ele 元素选择器
 * @returns {string}
 */
function eleData2Param(ele) {
    var data = [];
    $(ele).each(function(index,e){
        var name = $(e).attr('name');
        if(name) {
            var leftIndex = name.indexOf("[");
            var rightIndex = name.lastIndexOf("]");
            if (leftIndex != -1 && rightIndex != -1) {
                data.push(name.slice(0,leftIndex) + "." + name.slice(leftIndex + 1,rightIndex) + "=" + $(e).val());
            } else {
                data.push(name + "=" + $(e).val());
            }
        }
    })
    return data.join("&");
}

/**
 * 对象转为参数字符串
 * @param obj      对象
 * @param prefix   递归使用的前缀, 可不传, 传了则是所有属性的前缀
 * @returns {string}
 */
function obj2Param(obj,prefix){
    if(!obj) {return ''}
    var data = [];
    prefix = prefix || ""
    for(var prop in obj) {
        if(Object.prototype.toString.call(obj[prop]) === '[object Object]'){
            var _param = obj2Param(obj[prop],prefix ? prefix + '.'+prop : prop);
            if(_param.length > 0) {
                data.push(_param);
            }
        }else if(prefix){
            data.push(prefix + '.'+prop+'='+obj[prop])
        } else {
            data.push(prop+'='+obj[prop])
        }
    }
    return data.join('&');
}

/**
 * 将对象的多层级变为一个层级
 * @param obj
 * @param prefix
 * @returns {string}
 */
function flatObj(obj,prefix){
    var data = {};
    if(!obj) {return data;}
    prefix = prefix || ""
    for(var prop in obj) {
        if(Object.prototype.toString.call(obj[prop]) === '[object Object]'){
            var _data = flatObj(obj[prop],prefix ? prefix + '.'+prop : prop);
            Object.assign(data,_data);
        }else if(prefix){
            data[prefix+'.'+prop]=obj[prop]
        } else {
            data[prop]=obj[prop]
        }
    }
    return data;
}

/**
 * 重置表尾样式
 * @param _headerSelector BSTable 表头单元格选择器
 * @param _headerSelector BSTable 表尾单元格选择器
 */
function resetFooterWidth(_headerSelector,_footerSelector) {
    var header=$(_headerSelector);
    var footer=$(_footerSelector);
    for(var i = 0 ; i < header.length; i++ ) {
        footer.eq(i).width(header.eq(i).width());
    }
}

/**
 * 合并页脚
 * @param _footerBodySelector 表尾body元素选择器
 * @param colSpanNum          合并的单元格数量
 */
function merge_footer(_footerBodySelector,colSpanNum) {
    // //获取table表中footer 并获取到这一行的所有列
    var footer_tbody = $(_footerBodySelector);
    var footer_tr = footer_tbody.find('>tr');
    var footer_td = footer_tr.find('>td');
    var footer_td_1 = footer_td.eq(0);
    var width = 0;
    for(var i=0;i<colSpanNum;i++) {
        width += parseInt(footer_td.eq(i).css('width'));
        footer_td.eq(i).hide();
    }
    //设置跨列
    footer_td_1.attr('colspan', colSpanNum).show();
    //这里可以根据自己的表格来设置列的宽度 使对齐
    footer_td_1.css('width',width);
}

/**
 * 将formControls下的输入框,select,textarea都设置为禁用
 * @param disabled
 */
function disabledFormControls(disabled) {
    if(disabled) {
        $('.formControls input').prop('disabled',true);
        $('.formControls select').prop('disabled',true);
        $('.formControls textarea').prop('disabled',true);
    }
}

/**
 * 数组差集 arr1 - arr2
 * @param arr1
 * @param arr2
 * @returns {[]}
 */
function diffArr(arr1, arr2) {
    var set2 = new Set(arr2);
    var subset = [];
    arr1.forEach(function(val, index) {
        if (!set2.has(val)) {
            subset.push(val);
        }
    });
    return subset;
}

/**
 * 校验字符串长度是否小于等于 l
 * @param s
 * @param l
 */
function checkLengthLTE(s,l){
    return s && l > 0 && s.length <= l;
}

/**
 校验字符串长度是否大于等于 l
 * @param s
 * @param l
 */
function checkLengthGTE(s,l){
    return s && l > 0 && s.length >= l;
}

/**
 * 校验中英文和下划线
 * @param s
 */
function checkChsEngUnderline(s) {
    return s && /^[a-zA-Z0-9\u4e00-\u9fa5-_]+$/.test(s);
}

/**
 * 校验是否是整数
 * @param s
 */
function checkIsDigits(s){
    return s && /^(([^0][0-9]+|0)$)|^(([1-9]+)$)/.test(s);
}

/**
 * 校验是否是非负整数
 * @param s
 */
function checkIsPositiveInteger(s){
    return s && /^[1-9]\d*$|^0$/.test(s);
}

/**
 * 校验是否是数字,最多两位小数
 * @param s
 */
function checkIsNumber(s){
    return s && (checkIsDigits(s) || /^((([^0][0-9]+|0)\.([0-9]{1,2}))$)|^(([1-9]+)\.([0-9]{1,2})$)/.test(s));
}

/**
 * 校验是否非负数, 最多两位小数
 * @param s
 */
function checkIsPositiveNumber(s){
    return s && /^([1-9]\d*(\.([0-9]{1,2}))$)|(^0(\.\d{1,2})?$)|(^[1-9]\d*$)/.test(s);
}
/**
 * 校验是否大于零, 最多两位小数
 * @param s
 */
function checkIsPositiveNumberGTZero(s){
    return s && (isNaN(s) ? false : parseFloat(s) > 0) && /^([1-9]\d*(\.([0-9]{1,2}))$)|(^0(\.\d{1,2})?$)|(^[1-9]\d*$)/.test(s);
}
/**
 * 校验是否是手机或电话
 * @param s
 */
function checkIsTel(s) {
    var mobile = /^(((13[0-9]{1})|(15[0-35-9]{1})|(17[0-9]{1})|(18[0-9]{1}))+\d{8})$/;
    var tel = /^(\d{3,4}-?)?\d{7,9}$/g;
    return  s && (tel.test(s) || (s.length==11 && mobile.test(s)));
}

/**
 * 校验是否是email
 * @param s
 */
function checkEmail(s) {
    return s && /^[a-zA-Z0-9.!#$%&'*+\/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$/.test(s);
}

function checkStartDate(startDate,endDate){
    if(startDate.length>0&&endDate.length>0){
        var startDates = startDate.splice('-');
        var endDates = endDate.splice('-');
        var start = new Date(startDates[0],startDates[1],startDates[2]);
        var end = new Date(endDates[0],endDates[1],endDates[2]);
        return start.getTime() < end.getTime();
    }
}