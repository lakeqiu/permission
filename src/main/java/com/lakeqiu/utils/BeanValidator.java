package com.lakeqiu.utils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lakeqiu.exception.ParamException;
import org.apache.commons.collections.MapUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.*;

/**
 * @author lakeqiu
 */
public class BeanValidator {
    /**
     * ValidatorFactory工厂
     */
    private static ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();

    /**
     * 校验单个类
     * @param t 要校验的类
     * @param groups
     * @param <T>
     * @return 包含错误信息的map
     */
    public static <T>Map<String, String> validate(T t, Class... groups){
        // 获取校验器
        Validator validator = validatorFactory.getValidator();
        // 校验
        Set<ConstraintViolation<T>> validateResult = validator.validate(t, groups);
        // 为空，说明参数合法，校验通过
        if (validateResult.isEmpty()){
            return Collections.emptyMap();
        }
        // 将错误信息封装进一个map中
        LinkedHashMap error = Maps.newLinkedHashMap();
        validateResult.stream().forEach(v -> error.put(v.getPropertyPath().toString(), v.getMessage()));
        return error;
    }

    /**
     * 如果传过来的数据有多个，使用这个校验
     * @param collection 集合接口
     * @return
     */
    public static Map<String, String> validateList(Collection<?> collection){
        Validator validator = validatorFactory.getValidator();
        Iterator<?> iterator = collection.iterator();
        Map errors = null;
        do {
            if (!iterator.hasNext()){
                return Collections.emptyMap();
            }
            Object o = iterator.next();
            validate(o, new Class[0]);
        } while (errors.isEmpty());

        return errors;
    }

    /**
     * 为了方便使用，统一了上面两个方法，再根据参数选择具体方法
     * @param first
     * @param objects
     * @return
     */
    public static Map<String, String> validateObject(Object first, Object... objects){
        if (null != objects && objects.length > 0){
            return validateList(Lists.asList(first, objects));
        }
        return validate(first, new Class[0]);
    }

    /**
     * 在项目中使用了这个方法，避免了使用validateObject方法需要抛出异常
     * 造成代码可读性差
     * @param param
     */
    public static void check(Object param){
        Map<String, String> map = validateObject(param);
        // map里有值说明校验过程中出现不合法数据
        // 等于 null != map && map.entrySet().size() > 0
        if (MapUtils.isNotEmpty(map)){
            // 抛出异常错误信息
            throw new ParamException(map.toString());
        }
    }
}
