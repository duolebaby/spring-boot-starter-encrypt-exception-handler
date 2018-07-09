package top.qjyoung.encrypt.advice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;
import top.qjyoung.encrypt.anno.Decrypt;
import top.qjyoung.encrypt.auto.EncryptProperties;
import top.qjyoung.encrypt.exception.AppException;
import top.qjyoung.encrypt.util.KeyContainer;
import top.qjyoung.encrypt.util.ResultEnum;
import top.qjyoung.encrypt.util.StringUtils;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

/**
 * 请求数据接收处理类<br>
 * <p>
 * 对加了@Decrypt的方法的数据进行解密操作<br>
 * <p>
 * 只对@RequestBody参数有效
 */
@ControllerAdvice
public class DecryptRequestBodyAdvice implements RequestBodyAdvice {
    
    private Logger logger = LoggerFactory.getLogger(DecryptRequestBodyAdvice.class);
    
    @Autowired
    private EncryptProperties encryptProperties;
    
    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType,
                            Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }
    
    @Override
    public Object handleEmptyBody(Object body, HttpInputMessage inputMessage, MethodParameter parameter,
                                  Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return body;
    }
    
    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType,
                                           Class<? extends HttpMessageConverter<?>> converterType) throws IOException {
        //对于加了Decrpt注解的请求进行解密
        if (parameter.getMethod().isAnnotationPresent(Decrypt.class) && !encryptProperties.isDebug()) {
            List<String> uuid = inputMessage.getHeaders().get("uuid");
            if (uuid == null || uuid.size() == 0 || StringUtils.isEmpty(uuid.get(0))) {
                throw new AppException(ResultEnum.UUID_HEADER_MISS);
            }
            //推荐从redis缓存获取aesKey，并设置过期时间，这里只是模拟(存在静态map对象中)
            String aesKey = KeyContainer.AES_KEY_MAP.get(uuid.get(0));
            if (aesKey == null) {
                throw new AppException(ResultEnum.KEY_EXPIRED);
            }
            try {
                return new DecryptHttpInputMessage(inputMessage, aesKey, encryptProperties.getCharset());
            } catch (Exception e) {
                logger.error("数据解密失败", e);
                throw new AppException(ResultEnum.DECRYPT_FAILED);
            }
        }
        return inputMessage;
    }
    
    @Override
    public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType,
                                Class<? extends HttpMessageConverter<?>> converterType) {
        return body;
    }
}