package top.qjyoung.encrypt.advice;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;
import top.qjyoung.encrypt.anno.Decrypt;
import top.qjyoung.encrypt.auto.EncryptProperties;
import top.qjyoung.encrypt.util.AESUtil;
import top.qjyoung.encrypt.exception.AppException;
import top.qjyoung.encrypt.util.KeyContainer;
import top.qjyoung.encrypt.util.R;

import java.io.IOException;
import java.io.InputStream;
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
        List<String> uuid = inputMessage.getHeaders().get("uuid");
        if (uuid == null || uuid.size() == 0 || StringUtils.isEmpty(uuid.get(0).trim())) {
            throw new AppException(R.error("uuid错误"));
        }
        //推荐从redis缓存获取aesKey，并设置过期时间，这里只是模拟(存在静态map对象中)
        String aesKey = KeyContainer.AES_KEY_MAP.get(uuid);
        if (aesKey == null) {
            throw new AppException(R.error("密钥不存在或者已过期"));
        }
        //对于加了Decrpt注解的请求进行解密
        if (parameter.getMethod().isAnnotationPresent(Decrypt.class) && !encryptProperties.isDebug()) {
            try {
                return new DecryptHttpInputMessage(inputMessage, aesKey, encryptProperties.getCharset());
            } catch (Exception e) {
                logger.error("数据解密失败", e);
                throw new AppException(R.error("数据解密失败"));
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

/**
 * 解密
 */
class DecryptHttpInputMessage implements HttpInputMessage {
    private Logger logger = LoggerFactory.getLogger(DecryptRequestBodyAdvice.class);
    private HttpHeaders headers;
    private InputStream body;
    
    public DecryptHttpInputMessage(HttpInputMessage inputMessage, String key, String charset) throws Exception {
        this.headers = inputMessage.getHeaders();
        String content = IOUtils.toString(inputMessage.getBody(), charset);
        long startTime = System.currentTimeMillis();
        String decryptBody = AESUtil.decrypt(content, key);
        long endTime = System.currentTimeMillis();
        logger.debug("Decrypt Time:" + (endTime - startTime));
        this.body = IOUtils.toInputStream(decryptBody, charset);
    }
    
    @Override
    public InputStream getBody() throws IOException {
        return body;
    }
    
    @Override
    public HttpHeaders getHeaders() {
        return headers;
    }
}
