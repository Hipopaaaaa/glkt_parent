package com.ohj.glkt.utils.copy;

import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

public class CopyUtil {

    /**
     * 拷贝属性
     */
    public static <T> T copyBean(Object source, Class<T> clz) {
        if (clz == null) {
            throw new IllegalArgumentException("转换类不能为空");
        }
        if (source == null) return null;

        T target = BeanUtils.instantiateClass(clz);
        BeanUtils.copyProperties(source, target);
        return target;
    }

    /**
     * 拷贝属性
     */
    public static <T> List<T> copyList(List<Object> sourceList, Class<T> clz) {
        if (clz == null) {
            throw new IllegalArgumentException("转换类不能为空");
        }
        if (CollectionUtils.isEmpty(sourceList)) return null;

        return sourceList.stream()
                .map(item -> copyBean(item, clz))
                .collect(Collectors.toList());
//        return sourceList.stream().map(source -> {
//            T target = BeanUtils.instantiateClass(clz);
//            BeanUtils.copyProperties(source, target);
//            return target;
//        }).collect(Collectors.toList());
    }

}