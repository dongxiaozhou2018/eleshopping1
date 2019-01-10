package com.neuedu.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtils {

    private static Properties properties = new Properties();

    static {

        InputStream resourceAsStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("application.properties");
        try {
            properties.load(resourceAsStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取配置文件里面的内容
     * */
    public static String readByKey(String key){
        return properties.getProperty(key);
    }

}
