package api;

import com.github.crab2died.annotation.ExcelField;

public class ParameterBean {
    @ExcelField(title ="loginname")
    private String loginname;
    @ExcelField(title ="loginpass")
    private String loginpass;

    @Override
    public String toString() {
        return "ParameterBean{" +
                "loginname='" + loginname + '\'' +
                ", loginpass='" + loginpass + '\'' +
                '}';
    }

    public String getLoginname() {
        return loginname;
    }

    public void setLoginname(String loginname) {
        this.loginname = loginname;
    }

    public String getLoginpass() {
        return loginpass;
    }

    public void setLoginpass(String loginpass) {
        this.loginpass = loginpass;
    }
}
