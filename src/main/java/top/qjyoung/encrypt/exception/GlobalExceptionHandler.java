package top.qjyoung.encrypt.exception;

import org.apache.shiro.authz.AuthorizationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;
import top.qjyoung.encrypt.auto.ErrorPageProperties;
import top.qjyoung.encrypt.util.R;
import top.qjyoung.encrypt.util.ResultEnum;
import top.qjyoung.encrypt.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * 判断是否是移动端请求
 * 判断是否ajax请求
 * <p>
 * 返回不同数据
 */
@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {
    
    private Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    
    @Autowired
    private ErrorPageProperties errorPageProperties;
    
    /**
     * 自定义异常
     */
    @ExceptionHandler(AppException.class)
    public R handleBDException(AppException e) {
        e.printStackTrace();
        return e.getR();
    }
    
    /**
     * 500
     *
     * @param request
     * @param exception
     * @return
     * @throws Exception
     */
    @ExceptionHandler(Exception.class)
    public Object allExceptionHandler(HttpServletRequest request, Exception exception) throws Exception {
        exception.printStackTrace();
        StringBuffer requestURL = request.getRequestURL();
        String localizedMessage = exception.getLocalizedMessage();
        logger.error(exception.getMessage(), "error occured when access \"" + requestURL.toString() + "\", cause is : " + localizedMessage);
        if (isMobileOrAjaxRequest(request)) {
            return R.error(ResultEnum.SERVER_ERROR);
        }
        String page_500 = errorPageProperties.getPage_500();
        if (StringUtils.isEmpty(page_500)) {
            return R.error(ResultEnum.PAGE_500_MISS);
        }
        return new ModelAndView(page_500);
    }
    
    /**
     * 404
     *
     * @param request
     * @param e
     * @return
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public Object NoHandlerFoundException(HttpServletRequest request, NoHandlerFoundException e) {
        e.printStackTrace();
        logger.error(e.getMessage(), e);
        if (isMobileOrAjaxRequest(request)) {
            return R.error(ResultEnum.NOT_FOUND);
        }
        StringBuffer requestURL = request.getRequestURL();
        logger.debug(requestURL.toString());
        
        String page_404 = errorPageProperties.getPage_404();
        if (StringUtils.isEmpty(page_404)) {
            logger.error("404 页面未配置");
            return R.error(ResultEnum.PAGE_404_MISS);
        }
        return new ModelAndView(page_404);
    }
    
    /**
     * 403
     *
     * @param request
     * @param e
     * @return
     */
    @ExceptionHandler(AuthorizationException.class)
    public Object handleAuthorizationException(HttpServletRequest request, AuthorizationException e) {
        e.printStackTrace();
        logger.error(e.getMessage(), e);
        if (isMobileOrAjaxRequest(request)) {
            return R.error(ResultEnum.NO_AUTHORIZATION);
        }
        String page_403 = errorPageProperties.getPage_403();
        if (StringUtils.isEmpty(page_403)) {
            return R.error(ResultEnum.PAGE_403_MISS);
        }
        return new ModelAndView("error/403");
    }
    
    /**
     * 405
     *
     * @param e
     * @return
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Object handleAuthorizationException(HttpRequestMethodNotSupportedException e) {
        e.printStackTrace();
        logger.error(e.getMessage(), e);
        return R.error(ResultEnum.METHOD_NOT_ALLOWED);
    }
    
    /**
     * DuplicateKeyException
     *
     * @param e
     * @return
     */
    @ExceptionHandler(DuplicateKeyException.class)
    public R handleDuplicateKeyException(DuplicateKeyException e) {
        e.printStackTrace();
        logger.error(e.getMessage(), e);
        return R.error(ResultEnum.DUPLICATE_KEY);
    }
    
    /**
     * 判断请求来源
     *
     * @param request
     * @return
     */
    private boolean isMobileOrAjaxRequest(HttpServletRequest request) {
        String requestType = request.getHeader("X-Requested-With");
        if ("XMLHttpRequest".equals(requestType)) {
            logger.debug("AJAX请求..");
            return true;
        }
        String platform = request.getHeader("platform");
        if ("mobile".equals(platform)) {
            logger.debug("移动端请求");
            return true;
        }
        return false;
    }
}