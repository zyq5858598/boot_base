package com.ljwm.bootbase.kit;

import java.io.File;

/**
 * JKhaled created by yunqisong@foxmail.com 2017/11/16
 * FOR : 获取当前项目相关路径的工具类
 */
public class PathKit {


    private static String PROJECT_PATH = null;

    /**
     * 获取项目根路径
     *
     * @return
     */
    public static String getProjectPath() {
        if (PROJECT_PATH == null)
            PROJECT_PATH = new File("split").getAbsolutePath().split("split")[0];
        return PROJECT_PATH;
    }

    /**
     * 设置一个认为的项目根路径
     *
     * @param projectPath
     */
    public static void setProjectPath(String projectPath) {
        PROJECT_PATH = projectPath;
    }



}
