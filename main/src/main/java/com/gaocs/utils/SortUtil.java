package com.gaocs.utils;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * @author ：gaocs
 * @date ：Created in 2020/4/14 17:44
 * @description：排序工具类
 * @modified By：
 * @version: $
 */
public class SortUtil {

	//升序
	public static final String ASC = "ASC";
	//降序
	public static final String DESC = "DESC";
	// property 对应的属性
	public static <T> void sortOnList(List<T> list, final String property, final String sort){
		Collections.sort(list,new Comparator<T>(){
			@Override
			public int compare(T o1, T o2) {
				String a1 = null;
				String a2 = null;
				if(o1 instanceof Map) {   //针对List<Map<String,String>类型
					Map temp1 = (Map) o1;
					Map temp2 = (Map) o2;
					Object object = temp1.get(property);
					if(object instanceof String){ //根据Map中value来进行排序String类型需要转换
						a1 = (String) temp1.get(property);
						a2 = (String) temp2.get(property);
					}
				}
				//开始排序
				if(sort.equals(ASC)){
					return a1.compareTo(a2);//升序
				} else{
					return a2.compareTo(a1);	//降序
				}

			}
		});
	}

	public static <T> void sortByIneger(List<Map<String, Object>> list, final String property, final String sort){
		Collections.sort(list, new Comparator<Map<String, Object>>() {
			public int compare(Map<String, Object> o1, Map<String, Object> o2) {
				Integer name2 = Integer.valueOf(o1.get(property).toString()) ;
				Integer name1 = Integer.valueOf(o2.get(property).toString()) ;
				if(sort.equals(DESC)) {
					return name1.compareTo(name2);
				}else {
					return name2.compareTo(name1);
				}
			}
		});
	}

	public static void main(String[] args) {
		SortUtil sortUtil = new SortUtil();

	}
}
