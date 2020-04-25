package com.xc.template.system.codebuild.utils;

import com.xc.template.common.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CodeBuildUtils {

    private static final Logger logger = LoggerFactory.getLogger(CodeBuildUtils.class);

    public static final List<String> FILTERS = new ArrayList<>();

    static{
        FILTERS.add("sys");
    }

    public static String toCamelCase(String str) {
        return toCamelCaseWithFilter(str,true);
    }

    public static String toCamelCaseWithFilter(String str, boolean useFilter){
        if (StringUtils.isEmpty(str)) { return null;}
        String[] arr = str.split("_");
        StringBuffer res = new StringBuffer();
        String sub = null;
        boolean firstUpper = false;
        for (int i = 0; i < arr.length; i++) {
            if (useFilter && FILTERS.contains(arr[i])) {
                continue;
            }
            sub = arr[i];
            sub = firstUpper ? sub.substring(0,1).toUpperCase() + sub.substring(1) : sub;
            res.append(sub);
            firstUpper = true;
        }
        return res.toString();
    }

    public static String upperFirstCase(String str){
        if (StringUtils.isEmpty(str)) { return null;}
        str = str.substring(0,1).toUpperCase() + str.substring(1);
        return str;
    }

    public static void writeToFile(String fileName, String contents) {
        logger.debug("write file: " + fileName);
        File file = new File(fileName);
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file),"UTF-8"))){
            writer.write(contents);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void createDirectoryIfNotExist(String path){
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    public static void main(String[] args) {
        String test = "sys_user";
        System.out.println(toCamelCase(test));
    }
}
