package com.xc.template.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializeFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;

import java.util.List;

/**
 * FASTJSON 工具类
 * @author: xuchang
 * @date: 2019-09-16
 */
public class FastJsonUtils {

    /**
     * 对象转json字符串,仅序列化指定属性
     * @param obj
     * @param props
     * @return
     */
    public static String toJsonStringIncludeProperties(Object obj, String... props){
        if (obj == null) {
            return null;
        }
        if (props != null) {
            SerializeFilter filter = new SimplePropertyPreFilter(props);
            return JSON.toJSONString(obj,filter, SerializerFeature.WriteDateUseDateFormat,SerializerFeature.WriteNullListAsEmpty);
        }
        return JSON.toJSONString(obj);
    }

    /**
     * 集合转JSONArray,仅序列化集合中对象的指定属性
     * @param list
     * @param props
     * @return
     */
    public static <T> Object toJsonArrayIncludeProperties(List<T> list, String... props){
        return JSONArray.parse(toJsonStringIncludeProperties(list,props));
    }

    /**
     * 对象转JSONObject,仅序列化指定属性
     * @param obj
     * @param props
     * @return
     */
    public static JSONObject toJsonObjectIncludeProperties(Object obj, String... props){
        if (obj == null) {
            return null;
        }
        return JSONObject.parseObject(toJsonStringIncludeProperties(obj,props));
    }

}
