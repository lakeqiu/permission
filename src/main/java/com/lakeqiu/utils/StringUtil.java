package com.lakeqiu.utils;

import com.google.common.base.Splitter;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lakeqiu
 */
public class StringUtil {

    /**
     * 1， 2， 3， 4， ， ， -> #{1, 2, 3, 4}#
     * @param str
     * @return
     */
    public static List<Integer> splitToListInt(String str) {
        // 1、使用‘，’分割，2、去除前后空格 3、去除空串
        List<String> stringList = Splitter.on(",").trimResults().omitEmptyStrings().splitToList(str);
        return stringList.stream().map(Integer::parseInt).collect(Collectors.toList());
    }
}
