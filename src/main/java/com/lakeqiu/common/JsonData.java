package com.lakeqiu.common;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lakeqiu
 */
@Getter
@Setter
public class JsonData {
    /**
     * 结果
     * 如果失败了，失败信息
     * 数据
     */
    private boolean ret;
    private String msg;
    private Object data;

    public JsonData(boolean ret) {
        this.ret = ret;
    }

    /**
     * 封装成功信息方法
     * @param object 返回数据
     * @param msg 信息
     * @return
     */
    public static JsonData success(Object object,String msg){
        JsonData jsonData = new JsonData(true);
        jsonData.data = object;
        jsonData.msg = msg;
        return jsonData;
    }

    /**
     * 可能成功返回并不需要msg，
     * 只需要data，故重载一下
     * @param object
     * @return
     */
    public static JsonData success(Object object){
        JsonData jsonData = new JsonData(true);
        jsonData.data = object;
        return jsonData;
    }

    /**
     * 只需要通知成功即可
     * @return
     */
    public static JsonData success(){
        return new JsonData(true);
    }

    /**
     * 失败，返回失败信息
     * @param msg
     * @return
     */
    public static JsonData fail(String msg){
        JsonData jsonData = new JsonData(false);
        jsonData.msg = msg;
        return jsonData;
    }

    /**
     * 这个方法的作用就是保证程序在抛出异常时返回数据格式的统一
     *  mv = new ModelAndView("jsonView", result.toMap());
     *  ModelAndView接收一个map
     * @return
     */
    public Map<String, Object> toMap(){
        HashMap<String, Object> map = new HashMap<>();
        map.put("ret", ret);
        map.put("msg", msg);
        map.put("data", data);
        return map;
    }
}
