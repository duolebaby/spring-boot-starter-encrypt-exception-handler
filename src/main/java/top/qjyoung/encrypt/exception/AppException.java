package top.qjyoung.encrypt.exception;

import top.qjyoung.encrypt.util.BaseResultEnum;
import top.qjyoung.encrypt.util.R;

/**
 * 自定义异常
 */
public class AppException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    
    private R r;
    
    public AppException(BaseResultEnum resultEnum) {
        super(resultEnum.getMessage());
        this.r = R.error(resultEnum);
    }
    
    public AppException(BaseResultEnum resultEnum, Throwable e) {
        super(resultEnum.getMessage(), e);
        this.r = R.error(resultEnum);
    }
    
    public AppException(R r, Throwable e) {
        super(r.getMsg().toString(), e);
        this.r = r;
    }
    
    public AppException(R r) {
        super(r.getMsg().toString());
        this.r = r;
    }
    
    public R getR() {
        return this.r;
    }
}
