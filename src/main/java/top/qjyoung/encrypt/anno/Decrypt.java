package top.qjyoung.encrypt.anno;

import java.lang.annotation.*;

/**
 * 解密注解
 * <p>
 * <p>加了此注解的接口将进行数据解密操作<p>
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Decrypt {

}
