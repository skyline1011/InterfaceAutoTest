package utils;

import api.CheckResult;
import com.alibaba.fastjson.JSONPath;
import com.googlecode.aviator.AviatorEvaluator;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 检查点工具类
 */
public class CheckPointUtils {
    public static CheckResult check(String json, String check) {
        if (check == null || json == null ||"null".equals(json)) {
            return CheckResult.SKIP;
        }
        String[] check_array=check.split("&&|\\|\\|");
        int keycount=0;
        Map<String, Object> env = new LinkedHashMap<String, Object>();
        for (String curentcheck : check_array) {
            String oldcheck=curentcheck;
            String[] values = curentcheck.split("=|>|<|>=|<=|==");
            String key = values[0];
            String value = values[values.length - 1];
            Object checkValue = JSONPath.read(json, key);
            // key $替换
            String newKey="data"+keycount++;
            curentcheck = curentcheck.replace(key, newKey);
            env.put(newKey, checkValue);
            // value值替换
            if (checkValue instanceof String) {
                String newValue = RegexMatches.addSingleQuotes(value.toString());
                curentcheck = curentcheck.replace(value, newValue);
            }
            // =替换
            if (curentcheck.contains("=") && !curentcheck.contains(">=") && !curentcheck.contains("<=") & !curentcheck.contains("==")) {
                curentcheck = curentcheck.replace("=", "==");
            }
            check=check.replace(oldcheck, curentcheck);
            System.out.println(curentcheck + " map " + env);
        }

        System.out.println("check  "+check+" map "+env);
        Boolean result = (Boolean) AviatorEvaluator.execute(check, env);

        if(result) {
            return CheckResult.SUCCESS;
        }else {
            return CheckResult.FAIL;
        }
    }

    public static void main(String[] args) {
//		String json ="{\"code\":300,\"message\":\"SUCCESS\",\"totalCount\":0,\"totalPage\":0,\"pageSize\":0,\"page\":0,\"data\":null,\"json\":null,\"list\":null}";
//		System.out.println(check(json,"$.code==300"));
//
        String test ="{\"msg\":\"登录成功\",\"uid\":\"DAD3483647A94DBDB174C4C036CA8A80\",\"code\":\"1\",\"code2\":\"2\",\"code3\":\"3\"}";
        String expression="$.code=1&&$.code2>=2||$.code3>=4";
        //String expression="$.code=1";
        System.out.println(check(test,expression));
    }
}
