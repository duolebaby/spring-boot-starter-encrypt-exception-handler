package top.qjyoung.encrypt.advice;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import top.qjyoung.encrypt.anno.Encrypt;
import top.qjyoung.encrypt.auto.EncryptProperties;
import top.qjyoung.encrypt.exception.AppException;
import top.qjyoung.encrypt.util.AESUtil;
import top.qjyoung.encrypt.util.KeyContainer;
import top.qjyoung.encrypt.util.ResultEnum;
import top.qjyoung.encrypt.util.StringUtils;

import java.util.List;

/**
 * 请求响应处理类<br>
 * <p>
 * 对加了@Encrypt的方法的数据进行加密操作
 */
@ControllerAdvice
public class EncryptResponseBodyAdvice implements ResponseBodyAdvice<Object> {
    
    private Logger logger = LoggerFactory.getLogger(EncryptResponseBodyAdvice.class);
    
    private ObjectMapper objectMapper = new ObjectMapper();
    
    @Autowired
    private EncryptProperties encryptProperties;
    
    private static ThreadLocal<Boolean> encryptLocal = new ThreadLocal<Boolean>();
    
    public static void setEncryptStatus(boolean status) {
        encryptLocal.set(status);
    }
    
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }
    
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        // 可以通过调用EncryptResponseBodyAdvice.setEncryptStatus(false);来动态设置不加密操作
        Boolean status = encryptLocal.get();
        if (status != null && status == false) {
            encryptLocal.remove();
            return body;
        }
        if (returnType.getMethod().isAnnotationPresent(Encrypt.class) && !encryptProperties.isDebug()) {
            List<String> uuid = request.getHeaders().get("uuid");
            if (uuid == null || uuid.size() == 0 || StringUtils.isEmpty(uuid.get(0))) {
                throw new AppException(ResultEnum.UUID_HEADER_MISS);
            }
            //推荐从redis缓存获取aesKey，并设置过期时间，这里只是模拟(存在静态map对象中)
            String aesKey = KeyContainer.AES_KEY_MAP.get(uuid.get(0));
            if (aesKey == null) {
                throw new AppException(ResultEnum.KEY_EXPIRED);
            }
            try {
                String content = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(body);
                logger.debug("body before encrypt: " + content);
                String result = AESUtil.encrypt(content, aesKey);
                response.getHeaders().add("encrypted", "true");
                return result;
            } catch (Exception e) {
                logger.error("加密数据异常", e);
                throw new AppException(ResultEnum.ENCRYPT_FAILED);
            }
        }
        return body;
    }
}
