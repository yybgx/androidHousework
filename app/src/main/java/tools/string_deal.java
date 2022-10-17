package tools;

public class string_deal {
    public static boolean isNotEmpty(String str) {
        if ("".equals(str) || str == null) {//会抛出异常
           return false;
        }else {
            return true;
        }
    }
}
