package com.xc.template.system.codebuild.entity;

import com.alibaba.fastjson.JSON;

import javax.validation.constraints.NotNull;

public class BuildParam {

    @NotNull
    private String tableName;

    @NotNull
    private String packageName;

    @NotNull
    private String moduleName;

    @NotNull
    private String filePath;

    @NotNull
    private String dbName;

    @NotNull
    private String comment;

    @NotNull
    private boolean needEntity;

    @NotNull
    private boolean needController;

    @NotNull
    private boolean needService;

    @NotNull
    private boolean needDao;

    @NotNull
    private boolean needXml;

    @NotNull
    private boolean needListPage;

    @NotNull
    private boolean needFormPage;

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public boolean isNeedEntity() {
        return needEntity;
    }

    public void setNeedEntity(boolean needEntity) {
        this.needEntity = needEntity;
    }

    public boolean isNeedController() {
        return needController;
    }

    public void setNeedController(boolean needController) {
        this.needController = needController;
    }

    public boolean isNeedService() {
        return needService;
    }

    public void setNeedService(boolean needService) {
        this.needService = needService;
    }

    public boolean isNeedDao() {
        return needDao;
    }

    public void setNeedDao(boolean needDao) {
        this.needDao = needDao;
    }

    public boolean isNeedXml() {
        return needXml;
    }

    public void setNeedXml(boolean needXml) {
        this.needXml = needXml;
    }

    public boolean isNeedListPage() {
        return needListPage;
    }

    public void setNeedListPage(boolean needListPage) {
        this.needListPage = needListPage;
    }

    public boolean isNeedFormPage() {
        return needFormPage;
    }

    public void setNeedFormPage(boolean needFormPage) {
        this.needFormPage = needFormPage;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
