package com.xc.template.system.codebuild.entity;

import com.alibaba.fastjson.JSON;

public class Column {

    // 列明c
    private String name;

    private String type;

    private String comment;

    // 列名转驼峰,首字母大写
    private String upCamelName;

    // 列名转驼峰,首字母小写
    private String camelName;

    private boolean show;

    private boolean required;

    // 字符长度
    private Integer maxCharLength;

    // 整数位数
    private Integer maxNumLength;

    // 小数位数
    private Integer maxScaleLength;

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public Integer getMaxCharLength() {
        return maxCharLength;
    }

    public void setMaxCharLength(Integer maxCharLength) {
        this.maxCharLength = maxCharLength;
    }

    public Integer getMaxNumLength() {
        return maxNumLength;
    }

    public void setMaxNumLength(Integer maxNumLength) {
        this.maxNumLength = maxNumLength;
    }

    public Integer getMaxScaleLength() {
        return maxScaleLength;
    }

    public void setMaxScaleLength(Integer maxScaleLength) {
        this.maxScaleLength = maxScaleLength;
    }

    public boolean isShow() {
        return show;
    }

    public void setShow(boolean show) {
        this.show = show;
    }

    public String getCamelName() {
        return camelName;
    }

    public void setCamelName(String camelName) {
        this.camelName = camelName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getUpCamelName() {
        return upCamelName;
    }

    public void setUpCamelName(String upCamelName) {
        this.upCamelName = upCamelName;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
