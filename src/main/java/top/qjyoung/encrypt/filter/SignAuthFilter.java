package top.qjyoung.encrypt.filter;

import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpMethod;
import org.springframework.util.StringUtils;
import org.springframework.web.context.support.WebApplicationContextUtils;
import top.qjyoung.encrypt.auto.EncryptProperties;
import top.qjyoung.encrypt.util.*;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Set;

/**
 * 请求签名验证过滤器<br>
 * <p>
 * 请求头中获取sign进行校验，判断合法性和是否过期<br>
 * <p>
 * sign=加密({参数：值, 参数2：值2, signTime:签名时间戳})
 */
public class SignAuthFilter implements Filter {
    
    private EncryptProperties encryptProperties;
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        ServletContext context = filterConfig.getServletContext();
        ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(context);
        encryptProperties = ctx.getBean(EncryptProperties.class);
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        resp.addHeader("Content-Type", "application/json;charset=UTF-8");
        resp.setCharacterEncoding("UTF-8");
        
        /*存储aes 密钥的对应key值*/
        String uuid = req.getHeader("uuid");
        if (StringUtils.isEmpty(uuid)) {
            PrintWriter print = resp.getWriter();
            print.write(JsonUtils.toJson(R.error(ResultEnum.UUID_HEADER_MISS)));
            return;
        }
        String sign = req.getHeader("sign");
        if (StringUtils.isEmpty(sign)) {
            PrintWriter print = resp.getWriter();
            print.write(JsonUtils.toJson(R.error(ResultEnum.SIGN_MISS)));
            return;
        }
        try {
            String decryptKey = KeyContainer.AES_KEY_MAP.get(uuid);
            if (decryptKey == null) {
                PrintWriter print = resp.getWriter();
                print.write(JsonUtils.toJson(R.error(ResultEnum.KEY_EXPIRED)));
                return;
            }
            String decryptBody = AESUtil.decrypt(sign, decryptKey);
            Map<String, Object> signInfo = JsonUtils.getMapper().readValue(decryptBody, Map.class);
            Long signTime = (Long) signInfo.get("signTime");
            
            // 签名时间和服务器时间相差10分钟以上则认为是过期请求，此时间可以配置
            if ((System.currentTimeMillis() - signTime) > encryptProperties.getSignExpireTime() * 60000) {
                PrintWriter print = resp.getWriter();
                print.write(JsonUtils.toJson(R.error(ResultEnum.REQUEST_EXPIRED)));
                return;
            }
            
            // POST请求只处理时间(加密过的,无法篡改，若能篡改，签名无意义，密钥已经泄露【除非双密钥，加密签名采用不同密钥】)
            // GET请求处理时间和参数
            if (req.getMethod().equals(HttpMethod.GET.name())) {
                Set<String> paramsSet = signInfo.keySet();
                for (String key : paramsSet) {
                    if (!"signTime".equals(key)) {
                        String signValue = signInfo.get(key).toString();
                        String reqValue = req.getParameter(key).toString();
                        if (!signValue.equals(reqValue)) {
                            PrintWriter print = resp.getWriter();
                            print.write(JsonUtils.toJson(R.error(ResultEnum.DIRTY_REQUEST)));
                            return;
                        }
                    }
                }
            }
        } catch (Exception e) {
            PrintWriter print = resp.getWriter();
            print.write(JsonUtils.toJson(R.error("非法请求:" + e.getMessage())));
            return;
        }
        chain.doFilter(request, response);
    }
    
    @Override
    public void destroy() {
    
    }
}
