package top.qjyoung.encrypt.util;

public enum ResultEnum implements BaseResultEnum {
    
    SUCCESS(0, "success"),
    
    ERROR(1, "error"),
    
    PARAM_ERROR(2, "参数错误(缺少必要参数)"),
    
    DUPLICATE_KEY(3, "数据库中已存在该记录"),
    
    RSA_KEY_NO_FOUND(4, "密钥不存在"),
    
    UUID_HEADER_MISS(5, "uuid header 缺失"),
    
    KEY_EXPIRED(6, "密钥不存在或者已过期"),
    
    DECRYPT_FAILED(7, "数据解密失败"),
    
    ENCRYPT_FAILED(8, "数据加密失败"),
    
    PAGE_403_MISS(9, "403页面没有配置"),
    
    PAGE_404_MISS(10, "404页面没有配置"),
    
    PAGE_500_MISS(11, "500页面没有配置"),
    
    SIGN_MISS(12, "非法请求:缺少签名信息"),
    
    REQUEST_EXPIRED(13, "非法请求:请求已过期"),
    
    DIRTY_REQUEST(14, "非法请求:参数被篡改"),
    
    SIGN_ERROR(15, "非法请求:签名验证失败"),
    
    REQUEST_EXPIRED_TIME_MISSS(16, "请求签名过期时间未配置"),
    
    SIGN_TIME_MISS(17, "签名时间缺失"),
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    SERVER_ERROR(500, "服务器内部错误"),
    
    NOT_FOUND(404, "页面未找到"),
    
    NO_AUTHORIZATION(403, "没有访问权限，请联系管理员"),
    
    METHOD_NOT_ALLOWED(405, "请求方法错误");
    
    private Integer code;
    
    private String message;
    
    ResultEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
    
    @Override
    public Integer getCode() {
        return code;
    }
    
    @Override
    public String getMessage() {
        return message;
    }
}
