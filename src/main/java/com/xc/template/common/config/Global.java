package com.xc.template.common.config;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import org.springframework.core.io.DefaultResourceLoader;
import com.google.common.collect.Maps;
import com.xc.template.common.utils.PropertiesLoader;
import com.xc.template.common.utils.StringUtils;

/**
 * 全局配置类
 * @author luochaoqun
 * @version 2014-06-25
 */
public class Global {

    /**
     * 当前对象实例
     */
    private static Global global = new Global();

    /**
     * 保存全局属性值
     */
    private static Map<String, String> map = Maps.newHashMap();

    /**
     * 属性文件加载对象
     */
    private static PropertiesLoader loader = new PropertiesLoader("app.properties");

    /**
     * 显示/隐藏
     */
    public static final String SHOW = "1";
    public static final String HIDE = "0";

    /**
     * 是/否
     */
    public static final String YES = "1";
    public static final String NO = "0";

    /**
     * 对/错
     */
    public static final String TRUE = "true";
    public static final String FALSE = "false";

    /**
     * 获取当前对象实例
     */
    public static Global getInstance() {
        return global;
    }

    public static String getConfig(String key) {
        String value = map.get(key);
        if (value == null){
            value = loader.getProperty(key);
            map.put(key, value != null ? value : StringUtils.EMPTY);
        }
        return value;
    }

    /**
     * 获取工程路径
     * @return
     */
    public static String getProjectPath(){
        // 如果配置了工程路径，则直接返回，否则自动获取。
        String projectPath = Global.getConfig("projectPath");
        if (StringUtils.isNotBlank(projectPath)){
            return projectPath;
        }
        try {
            File file = new DefaultResourceLoader().getResource("").getFile();
            if (file != null){
                while(true){
                    File f = new File(file.getPath() + File.separator + "src" + File.separator + "main");
                    if (f == null || f.exists()){
                        break;
                    }
                    if (file.getParentFile() != null){
                        file = file.getParentFile();
                    }else{
                        break;
                    }
                }
                projectPath = file.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return projectPath;
    }

}
