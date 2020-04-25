package com.xc.template.system.codebuild.entity;

import com.alibaba.fastjson.JSON;

import java.util.List;

public class GenData {

    private String packageName;

    private String tableName;

    private String camelTableName;

    private String className;

    private String moduleName;

    private boolean dateImport;

    private String comment;

    private String createDate;

    private String path;

    private List<Column> cols;

    public String getCamelTableName() {
        return camelTableName;
    }

    public void setCamelTableName(String camelTableName) {
        this.camelTableName = camelTableName;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public boolean isDateImport() {
        return dateImport;
    }

    public void setDateImport(boolean dateImport) {
        this.dateImport = dateImport;
    }

    public List<Column> getCols() {
        return cols;
    }

    public void setCols(List<Column> cols) {
        this.cols = cols;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
