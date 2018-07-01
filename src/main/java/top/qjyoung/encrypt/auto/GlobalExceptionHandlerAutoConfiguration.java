package top.qjyoung.encrypt.auto;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import top.qjyoung.encrypt.exception.GlobalExceptionHandler;

/**
 * 自动异常获取配置
 */
@Configuration
@Component
@EnableAutoConfiguration
@EnableConfigurationProperties(ErrorPageProperties.class)
public class GlobalExceptionHandlerAutoConfiguration {
    
    /**
     * 配置自动异常获取类
     *
     * @return
     */
    @Bean
    public GlobalExceptionHandler globalExceptionHandler() {
        return new GlobalExceptionHandler();
    }
}
