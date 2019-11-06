package com.lakeqiu.utils;


import lombok.extern.slf4j.Slf4j;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.ser.impl.SimpleFilterProvider;
import org.codehaus.jackson.type.TypeReference;


/**
 * @author lakeqiu
 */
@Slf4j
public class JsonMapper {
    private static ObjectMapper objectMapper = new ObjectMapper();

    // 配置
    static {
        objectMapper.disable(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.setFilters(new SimpleFilterProvider().setFailOnUnknownId(false));
        objectMapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_DEFAULT);
    }

    /**
     * 对象转json
     * @param src 对象
     * @param <T>
     * @return
     */
    public static <T> String objToJson(T src) {
        if (null == src){
            return null;
        }
        try {
            return src instanceof String ? (String) src : objectMapper.writeValueAsString(src);
        } catch (Exception e) {
            log.warn("对象转json出错，error{}", e);
            // 这里不抛出异常，返回null，让业务层自己处理
            return null;
        }
    }

    /**
     * json转对象
     * @param src
     * @param typeReference
     * @param <T>
     * @return
     */
    public static <T> T jsonToObj(String src, TypeReference<T> typeReference) {
        if (null == src || null == typeReference){
            return null;
        }
        try {
            return (T) (typeReference.getType().equals(String.class) ? src : objectMapper.readValue(src, typeReference));
        } catch (Exception e){
            log.warn("json转对象出错, String:{}, TypeReference:{}, error:{}", src, typeReference, e);
            return null;
        }
    }
}
