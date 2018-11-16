package utils;

import api.TestCase;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONPath;
import org.apache.commons.beanutils.BeanUtils;

/**
 * 关联工具类
 */
public class CorrelationUtils {
    private static Map<String, Object> correlationMp = new LinkedHashMap<String, Object>();

    /**
     * 将从Excel中获取的字符串，可变参数替换为正常变量
     * @param testCase
     */
    public static void replace(TestCase testCase){
        if(testCase.getUrl()!=null){
            testCase.setUrl(RegexMatches.replace(testCase.getUrl()));
        }
        if (testCase.getHeader() != null) {
            testCase.setHeader(RegexMatches.replace(testCase.getHeader()));
        }
        if (testCase.getParmas() != null) {
            testCase.setParmas(RegexMatches.replace(testCase.getParmas()));
        }
    }

    /**
     * 通过key获取关联Map中的value
     * @param key
     * @return
     */
    public static Object getCorrelationValue(String key) {
        if (correlationMp.containsKey(key)) {
            return correlationMp.get(key);
        }
        return "";
    }

    /**
     * 清空关联Map
     */
    public static void clear(){
        correlationMp.clear();
    }

    /**
     * 将对应的key-value添加到关联Map中
     * @param key
     * @param value
     */
    public static void addCorrelationKeyValue(String key,Object value) {
        correlationMp.put(key, value);
        System.out.println("addCorrelationKeyValue "+correlationMp);
    }

    /**
     * 将对象放到map
     * @param bean
     */
    public static void addCorrelationObject(Object bean) {
        //反射类的属性
        Field[] fields = bean.getClass().getDeclaredFields();
        for (Field f : fields) {
            try {
                correlationMp.put(f.getName(),BeanUtils.getProperty(bean,f.getName()));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        System.out.println("addCorrelationObject "+correlationMp);
    }

    /**
     * 提取json 数据到map
     * @param json
     * @param correlation
     */
    public static void addCorrelation(String json, String correlation) {
        if (json == null || correlation == null) {
            return;
        }
        //str=method=loginMobile&loginname=test1&loginpass=test1
        Map<String, Object> paramMp = DataUtils.covertStringToMp(correlation);
        Set<String> keys = paramMp.keySet();
        for (String key : keys) {
            Object jsonObject = JSONPath.read(json, paramMp.get(key).toString());
            //多值处理
            if(jsonObject instanceof List) {
                List<Object> list =(List<Object>) jsonObject;
                int count=0;
                for (Object object : list) {
                    correlationMp.put(key+"_g"+count++, object);
                }
            }else {
                //单个
                correlationMp.put(key, jsonObject);
            }
        }
//        System.out.println(correlationMp);
    }

    public static void main(String[] args) {
//		String mString = "{\"msg\":\"登录成功\",\"uid\":\"E2BBEDC09FAA4406B8D85C96DF6281CF\",\"code\":\"1\"}";
//		String save = "id=$.uid;mymsg=$.msg";

        String jsonString2="{\"code\":\"1\",\"data\":[{\"name\":\"test0\",\"pwd\":\"pwd0\"},{\"name\":\"test1\",\"pwd\":\"pwd1\"},{\"name\":\"test2\",\"pwd\":\"pwd2\"}]}";
        String save = "name=$..name&pwd=$..pwd";
        addCorrelation(jsonString2, save);
        TestCase testCase = new TestCase();
        testCase.setUrl("http://www.baidu.com?pid=${id}&test=${name_g1}");
        replace(testCase);
        System.out.println(JSON.toJSONString(testCase));
    }
}
