package org.xiaoxingbomei.utils;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

public class RequestParam_Utils {



    public static String getParam(String paramJson,String key){
        if(StringUtils.isEmpty(paramJson)){
            return null;
        }
        if(StringUtils.isNotEmpty(key)){
            Map<String,Object> reqMap = getParamMap(paramJson);
            if(reqMap.get(key) != null){
                return String.valueOf(reqMap.get(key));
            }
        }
        return null;
    }


    public static<T> T getRequestObj(String paramJson,Class cls){
        if(StringUtils.isEmpty(paramJson)){
            return null;
        }
        return (T) JSON.parseObject(paramJson,cls);
    }



    public static Integer getIntParam(String paramJson,String key){
        return Integer.valueOf(getParam(paramJson,key));
    }





    public static Map getParamMap(String paramJson){
        if(StringUtils.isEmpty(paramJson)){
            return null;
        }

        return JSON.parseObject(paramJson,Map.class);
    }

}
