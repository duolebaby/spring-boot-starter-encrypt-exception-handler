package top.qjyoung.encrypt.util;

/**
 * 自定义异常
 */
public class AppException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    
    private BaseResultEnum resultEnum;
    
    public AppException(BaseResultEnum resultEnum) {
        super(resultEnum.getMessage());
        this.resultEnum = resultEnum;
    }
    
    public AppException(BaseResultEnum resultEnum, Throwable e) {
        super(resultEnum.getMessage(), e);
        this.resultEnum = resultEnum;
    }
    
    public BaseResultEnum getResultEnum() {
        return resultEnum;
    }
    
    public void setResultEnum(BaseResultEnum resultEnum) {
        this.resultEnum = resultEnum;
    }
    
    public R getR() {
        return R.error(resultEnum);
    }
}
