package top.qjyoung.encrypt.advice;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import top.qjyoung.encrypt.util.AESUtil;

import java.io.IOException;
import java.io.InputStream;

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
        String decryptBody = AESUtil.decrypt(content, key);
        logger.debug("body after decrypt: " + decryptBody);
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
