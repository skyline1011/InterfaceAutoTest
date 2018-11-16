package api;

import com.alibaba.fastjson.JSON;
import com.github.crab2died.ExcelUtils;
import com.github.crab2died.exceptions.Excel4JException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import utils.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class ApiTest {
    public static void main(String[] args) {
        if(args.length!=3) {
            args=new String[3];
            args[0]="0";
            args[1]="6";
            args[2]="-h";
        }
        System.out.println("args"+ JSON.toJSONString(args));
        ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(5);
        ScheduledFuture<?> future = scheduledThreadPool.scheduleAtFixedRate(new Runnable() {
            public void run() {
                testcase();
            }
        }, Long.valueOf(args[0]), Long.valueOf(args[1]), DataUtils.getTimeUnit(args[2]));

        try {
            future.get();//若线程出现异常，会打印异常日志
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    private static void testcase() {
        String path = System.getProperty("user.dir") + File.separator + "data" + File.separator + "apitest.xlsx";
        String path2 = System.getProperty("user.dir") + File.separator + "data" + File.separator + "apitest_result"+DataUtils.getCurrentTime()+".xlsx";
        System.out.println(path);
        // 读取
        try {
            List<TestCase> allresults = new ArrayList<TestCase>();
            List<ParameterBean> parameterBeans = ExcelUtils.getInstance().readExcel2Objects(path,ParameterBean.class,1);//第二页sheet
            for (ParameterBean parameterBean : parameterBeans) {//每个参数执行一轮测试
                List<TestCase> list = ExcelUtils.getInstance().readExcel2Objects(path,TestCase.class);//默认取第一页sheet
                CorrelationUtils.clear();
                //添加关联数据
                CorrelationUtils.addCorrelationObject(parameterBean);
                for (TestCase testCase : list) {
                    if (testCase.isRun()) {
                        String result = null;
                        //前置处理器
                        InterceptorUtils.doBefore(testCase);
                        //关联替换
                        CorrelationUtils.replace(testCase);
                        System.out.println(testCase);
                        if ("get".equalsIgnoreCase(testCase.getType())) {
                            result = HttpClientUtils.doGet(testCase.getUrl(),
                                    DataUtils.covertStringToMp(testCase.getHeader()));
                        } else if ("post".equalsIgnoreCase(testCase.getType())) {
                            result = HttpClientUtils.doPost(testCase.getUrl(),
                                    DataUtils.covertStringToMp(testCase.getParmas()),
                                    DataUtils.covertStringToMp(testCase.getHeader()));
                        } else if ("postjson".equalsIgnoreCase(testCase.getType())) {
                            result = HttpClientUtils.doPostJson(testCase.getUrl(), testCase.getParmas(),
                                    DataUtils.covertStringToMp(testCase.getHeader()));
                        }
                        System.out.println(result);

                        CorrelationUtils.addCorrelation(result, testCase.getCorrelation());
                        //返回值检查
                        CheckResult resultCheck = CheckPointUtils.check(result, testCase.getCheckpoint());
                        //数据库检查
                        CheckResult dbcheckResult=DbCheck.check(testCase);
                        String allcheckResult =resultCheck.getMsg()+";"+dbcheckResult.getMsg();
                        testCase.setResult(allcheckResult);
                        System.out.println(allcheckResult);
                    }else {
                        testCase.setResult(CheckResult.DISABLED.getMsg());
                    }
                }
                allresults.addAll(list);
            }
            //写
            ExcelUtils.getInstance().exportObjects2Excel(allresults, TestCase.class, path2);

//            EmailUtils.sendEmailsWithAttachments("测试", "请查收结果", path2);

        }catch (Excel4JException e) {
            e.printStackTrace();
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
