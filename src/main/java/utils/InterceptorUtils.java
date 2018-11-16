package utils;

import api.TestCase;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.Map;
import java.util.Set;

/**
 * 处理MD5等加密相关
 */
public class InterceptorUtils {

    public static void doBefore(TestCase tescase) {
        if(tescase.getBeforeFunc()==null) {
            return;
        }
        tescase.setBeforeFunc(RegexMatches.replace(tescase.getBeforeFunc()));
        Map<String, Object> beforeMap=DataUtils.covertStringToMp(tescase.getBeforeFunc());
        Set<String> keys =beforeMap.keySet();
        for (String key : keys) {
            if(key.contains("md5")) {
                String codecvalue = DigestUtils.md5Hex(beforeMap.get(key).toString());
                CorrelationUtils.addCorrelationKeyValue(key, codecvalue);
            }if(key.contains("sha1")) {
                String codecvalue =DigestUtils.sha512Hex(beforeMap.get(key).toString());
                CorrelationUtils.addCorrelationKeyValue(key, codecvalue);
            }
        }
    }
}
