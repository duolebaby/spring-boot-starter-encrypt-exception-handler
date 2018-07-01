package top.qjyoung.encrypt.auto;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 配置错误页面跳转路径
 */
@ConfigurationProperties(prefix = "spring.error-page")
public class ErrorPageProperties {
    
    private String page_404;
    
    private String page_403;
    
    private String page_405;
    
    private String page_500;
    
    public String getPage_404() {
        return page_404;
    }
    
    public void setPage_404(String page_404) {
        this.page_404 = page_404;
    }
    
    public String getPage_403() {
        return page_403;
    }
    
    public void setPage_403(String page_403) {
        this.page_403 = page_403;
    }
    
    public String getPage_405() {
        return page_405;
    }
    
    public void setPage_405(String page_405) {
        this.page_405 = page_405;
    }
    
    public String getPage_500() {
        return page_500;
    }
    
    public void setPage_500(String page_500) {
        this.page_500 = page_500;
    }
}
