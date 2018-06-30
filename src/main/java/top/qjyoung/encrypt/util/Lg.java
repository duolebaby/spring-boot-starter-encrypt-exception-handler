package top.qjyoung.encrypt.util;

public class Lg {
    private static boolean open = true;
    
    public static void log(Object... msg) {
        if (!open) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (Object s : msg) {
            sb.append(" -█->> " + s);
        }
        System.err.println(sb.toString());
    }
}
