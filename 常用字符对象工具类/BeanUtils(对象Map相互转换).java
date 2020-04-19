package com.chinobot.common.utils;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.base.CaseFormat;



public class BeanUtils {
	 /**
     * 将对象装换为 map,对象转成 map，key肯定是字符串
     *
     * @param bean 转换对象
     * @return 返回转换后的 map 对象
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> beanToMap(Object bean) {
        
    	return com.baomidou.mybatisplus.core.toolkit.BeanUtils.beanToMap(bean);
    }

	/**
     * <p>
     * map 装换为 java bean 对象
     * </p>
     *
     * @param map   转换 MAP
     * @param clazz 对象 Class
     * @return 返回 bean 对象
     */
    public static <T> T mapToBean(Map<String, Object> map, Class<T> clazz) {
    	Map<String, Object> newMap = new HashMap<String, Object>();
		for(String key : map.keySet()) {
    		if(key.indexOf("_")>0) {
    			String newKey = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, key.toLowerCase());
    			newMap.put(newKey, map.get(key));
    		}
    	}
		map.putAll(newMap);
    	return com.baomidou.mybatisplus.core.toolkit.BeanUtils.mapToBean(map, clazz);
    }
    
    /**
     * <p>
     * List<T> 转换为 List<Map<String, Object>>
     * </p>
     *
     * @param beans 转换对象集合
     * @return 返回转换后的 bean 列表
     */
    public static <T> List<Map<String, Object>> beansToMaps(List<T> beans) {
    	
    	return com.baomidou.mybatisplus.core.toolkit.BeanUtils.beansToMaps(beans);
    }
    
    /**
     * <p>
     * List<Map<String,Object>> 转换为 List<T>
     * </p>
     *
     * @param maps  转换 MAP 集合
     * @param clazz 对象 Class
     * @return 返回转换后的 bean 集合
     */
    public static <T> List<T> mapsToBeans(List<Map<String, Object>> maps, Class<T> clazz) {
    	for(Map<String, Object> map : maps) {
    		Map<String, Object> newMap = new HashMap<String, Object>();
    		for(String key : map.keySet()) {
        		if(key.indexOf("_")>0) {
        			String newKey = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, key.toLowerCase());
        			newMap.put(newKey, map.get(key));
        		}
        	}
    		map.putAll(newMap);
    	}
    	return com.baomidou.mybatisplus.core.toolkit.BeanUtils.mapsToBeans(maps, clazz);
    }
}
