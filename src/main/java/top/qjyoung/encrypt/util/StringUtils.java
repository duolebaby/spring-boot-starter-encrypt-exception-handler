package top.qjyoung.encrypt.util;

import org.springframework.util.DigestUtils;

import java.util.Collection;
import java.util.Iterator;
import java.util.UUID;

public class StringUtils {
    
    public static String getUUID() {
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        return uuid;
    }
    
    /**
     * 获取指定内容的md5值,暂时不加盐
     *
     * @param source
     * @return
     */
    public static String getMD5(String source) {
        if (source == null) {
            return null;
        }
        return DigestUtils.md5DigestAsHex(source.getBytes());
    }
    
    /**
     * 判断字符串是否为空
     *
     * @param source
     * @return
     */
    public static boolean isEmpty(String source) {
        return source == null || source.trim().equals("");
    }
    
    public static String join(Collection collection, String split_str) {
        StringBuffer result = new StringBuffer();
        for (Iterator var3 = collection.iterator(); var3.hasNext(); result.append((String) var3.next())) {
            if (result.length() != 0) {
                result.append(split_str);
            }
        }
        return result.toString();
    }
    
    public static void main(String[] args) {
        Lg.log(getUUID());
    }
}
