package top.qjyoung.encrypt.anno;

import org.springframework.context.annotation.Import;
import top.qjyoung.encrypt.auto.GlobalExceptionHandlerAutoConfiguration;

import java.lang.annotation.*;

/**
 * 启用自动异常捕获处理handler
 *
 * <p>在Spring Boot启动类上加上此注解<p>
 *
 * <pre class="code">
 * &#064;SpringBootApplication
 * &#064;EnableAutoGlobalExceptionHandler
 * public class App {
 *     public static void main(String[] args) {
 *         SpringApplication.run(App.class, args);
 *     }
 * }
 * <pre>
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import({GlobalExceptionHandlerAutoConfiguration.class})
public @interface EnableAutoGlobalExceptionHandler {

}
