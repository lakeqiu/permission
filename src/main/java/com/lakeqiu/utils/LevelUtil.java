package com.lakeqiu.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * @author lakeqiu
 */
public class LevelUtil {
    /**
     * 部门层级分隔符
     * 最上层部门
     */
    private final static String SEPARATOR = ".";
    public final static String ROOT = "0";

    /**
     * 0.1
     * 0.1.2
     * 0.1.2.7
     * 0.4
     * 计算部门层级
     * @param parentLevel 上级部门层级
     * @param parentId 要计算的部门的上级部门id
     * @return 结果
     */
    public static String calculateLevel(String parentLevel, Integer parentId){
        // 其上级部门没有上级部门
        if (StringUtils.isBlank(parentLevel)){
            return ROOT;
        }
        return StringUtils.join(parentLevel, SEPARATOR, parentId);
    }
}
