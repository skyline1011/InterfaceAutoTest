package utils;

import api.CheckResult;
import api.TestCase;
import com.alibaba.fastjson.JSON;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapHandler;

import java.sql.SQLException;
import java.util.Map;

public class DbCheck {
    public static CheckResult check(TestCase testcase) {
        if(testcase.getDbchecksql()==null||testcase.getDbexpected()==null) {
            return CheckResult.DBSKIP;
        }
        if (testcase.getDbchecksql() != null) {
            String newsql =RegexMatches.replaceForString(testcase.getDbchecksql());
            testcase.setDbchecksql(newsql);
            QueryRunner runner = new QueryRunner(JDBCUtils.getDataSource());
            try {
                System.out.println(testcase.getDbchecksql());
                Map map = runner.query(testcase.getDbchecksql(), new MapHandler());
                if(map==null||map.isEmpty()) {
                    return CheckResult.DBFAIL;
                }
                String jsonStr= JSON.toJSONString(map);
                System.out.println("数据库json---"+ jsonStr);
                CheckResult checkResult=CheckPointUtils.check(jsonStr, testcase.getDbexpected());
                return checkResult;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static void main(String[] args) {
        String mString = "{\"msg\":\"登录成功\",\"uid\":\"test\",\"code\":\"1\"}";
        String save = "id=$.uid&mymsg=$.msg";
        CorrelationUtils.addCorrelation(mString, save);

        TestCase testCase = new TestCase();
        String sql ="select uid from t_user where uid=${id}";
        testCase.setDbchecksql(sql);
        testCase.setDbexpected("$.uid=test");
        CheckResult checkResult=check(testCase);
        System.out.println(" check "+ checkResult +" msg__ " +checkResult.getMsg() );
        System.out.println(JSON.toJSONString(testCase));
    }
}
