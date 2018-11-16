package api;

import com.github.crab2died.annotation.ExcelField;

public class TestCase {
    @ExcelField(title = "是否开启",readConverter = IsRunConverter.class)
    private boolean run;

    @ExcelField(title ="用例名称")
    private String casename;

    @ExcelField(title ="类型")
    private String type;

    @ExcelField(title ="地址")
    private String url;

    @ExcelField(title ="参数")
    private String parmas;

    @ExcelField(title ="头部")
    private String header;

    @ExcelField(title ="检查点")
    private String checkpoint;

    @ExcelField(title ="关联")
    private String correlation;

    @ExcelField(title ="数据库检查sql")
    private String dbchecksql;

    @ExcelField(title ="数据库检查点")
    private String dbexpected;

    @ExcelField(title ="前置函数")
    private String beforeFunc;

    @ExcelField(title ="测试结果")
    private String result;

    @Override
    public String toString() {
        return "TestCase{" +
                "run=" + run +
                ", casename='" + casename + '\'' +
                ", type='" + type + '\'' +
                ", url='" + url + '\'' +
                ", parmas='" + parmas + '\'' +
                ", header='" + header + '\'' +
                ", checkpoint='" + checkpoint + '\'' +
                ", correlation='" + correlation + '\'' +
                ", dbchecksql='" + dbchecksql + '\'' +
                ", dbexpected='" + dbexpected + '\'' +
                ", beforeFunc='" + beforeFunc + '\'' +
                ", result='" + result + '\'' +
                '}';
    }

    public boolean isRun() {
        return run;
    }

    public void setRun(boolean run) {
        this.run = run;
    }

    public String getCasename() {
        return casename;
    }

    public void setCasename(String casename) {
        this.casename = casename;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getParmas() {
        return parmas;
    }

    public void setParmas(String parmas) {
        this.parmas = parmas;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getCheckpoint() {
        return checkpoint;
    }

    public void setCheckpoint(String checkpoint) {
        this.checkpoint = checkpoint;
    }

    public String getCorrelation() {
        return correlation;
    }

    public void setCorrelation(String correlation) {
        this.correlation = correlation;
    }

    public String getDbchecksql() {
        return dbchecksql;
    }

    public void setDbchecksql(String dbchecksql) {
        this.dbchecksql = dbchecksql;
    }

    public String getDbexpected() {
        return dbexpected;
    }

    public void setDbexpected(String dbexpected) {
        this.dbexpected = dbexpected;
    }

    public String getBeforeFunc() {
        return beforeFunc;
    }

    public void setBeforeFunc(String beforeFunc) {
        this.beforeFunc = beforeFunc;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
