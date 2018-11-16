package api;

import com.github.crab2died.converter.ReadConvertible;

public class IsRunConverter implements ReadConvertible {
    public Object execRead(String s) {
        System.out.println(s);
        return "是".equals(s);
    }
}
