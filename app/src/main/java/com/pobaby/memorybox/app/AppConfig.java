package com.pobaby.memorybox.app;


/**
 * App 全局常用配置
 *
 * @author chenqh
 * @email 403167386@qq.com
 * @created 2017/12/12 14:41
 */
public class AppConfig {

    public final static String DB_LOCAL_NAME = "memorybox.mb";
    public final static String ROOT_CACHE = "/memorybox/";
    public final static String ROOT_DB = "/db/";
    public final static String ROOT_USERS = "/users/";
    public final static String ROOT_CRASH = "/crashLog/";
    public final static String SYSTEM_EXCEPTION_NAME = "system-exception.log";
    public static final int ROW_COUNT = 10;
    public final static String ACCOUNT_IMPORT_EXAMPLE = "{\n" +
            "\t\"modelList\": [{\n" +
            "\t\t\"name\": \"name\",\n" +
            "\t\t\"typeName\": \"typeName\",\n" +
            "\t\t\"account\": \"account\",\n" +
            "\t\t\"password\": \"password\",\n" +
            "\t\t\"address\": \"address\",\n" +
            "\t\t\"check\": true,\n" +
            "\t\t\"remark\": \"remark\"\n" +
            "\t}]\n" +
            "}";

}
