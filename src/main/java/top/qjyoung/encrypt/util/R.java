package top.qjyoung.encrypt.util;

import java.util.HashMap;
import java.util.Map;

public class R extends HashMap<String, Object> {
    private static final long serialVersionUID = 1L;
    
    public R() {
        put("code", 1);
        put("msg", "success");
    }
    
    public static R error() {
        R r = new R();
        r.put("code", 1);
        r.put("msg", "error");
        return r;
    }
    
    public static R error(BaseResultEnum resultEnum) {
        R r = new R();
        r.put("code", resultEnum.getCode());
        r.put("msg", resultEnum.getMessage());
        return r;
    }
    
    public static R error(String msg) {
        R r = new R();
        r.put("code", 1);
        r.put("msg", msg);
        return r;
    }
    
    public static R ok(String msg) {
        R r = new R();
        r.put("msg", msg);
        return r;
    }
    
    public static R ok(Map<String, Object> map) {
        R r = new R();
        r.putAll(map);
        return r;
    }
    
    /**
     * 不带数据返回
     *
     * @return
     */
    public static R ok() {
        return new R();
    }
    
    /**
     * 带数据返回
     *
     * @param data
     * @return
     */
    public static R ok(Object data) {
        R r = new R();
        r.put("data", data);
        return r;
    }
    
    public R put(String key, Object value) {
        super.put(key, value);
        return this;
    }
    
    public Object getMsg() {
        return this.get("msg");
    }
    
    public Object getCode() {
        return this.get("code");
    }
}
