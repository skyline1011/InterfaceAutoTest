package api;

public enum CheckResult {

    SUCCESS(true, "检查成功"),
    DISABLED(true, "没有开启"),
    FAIL(false, "检查失败"),
    SKIP(false, "没有设置检查点"),
    DBSKIP(true, "没有设置数据库检查点"),
    DBFAIL(false, "数据库检查失败");


    private boolean result;
    private String msg;


    private CheckResult(boolean result, String msg) {
        this.result = result;
        this.msg = msg;
    }

    public boolean isResult() {
        return result;
    }
    public void setResult(boolean result) {
        this.result = result;
    }
    public String getMsg() {
        return msg;
    }
    public void setMsg(String msg) {
        this.msg = msg;
    }
}
