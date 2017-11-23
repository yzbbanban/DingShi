package lt.riti.com.dingshi.consts;


import lt.riti.com.dingshi.app.StockApplication;

/**
 * Created by brander on 2017/9/24.
 */

public class Urls {
    public static final String COOL_UPLOAD_AL = StockApplication.url;//本机服务器
    //"http://192.168.0.115:8080/rfid/"
    //    public static final String COOL_UPLOAD_AL = "http://119.23.228.4/rfid/";//阿里服务器
    public static final String COOL_USER_AL = COOL_UPLOAD_AL + "user/";//用户登录
    public static final String COOL_RFID_USER_AL = COOL_UPLOAD_AL + "rfidUser/";//rfid客户信息
    public static final String COOL_RFID_ORDER_AL = COOL_UPLOAD_AL + "rfidOrder/";//rfid上传order信息
    public static final String COOL_RFID_MANUFACTOR_AL = COOL_UPLOAD_AL + "rfidManufactor/";//rfid上传order信息
    public static final String COOL_RFID_PRODUCT_AL = COOL_UPLOAD_AL + "rfidProduct/";//rfid上传order信息

    public static final String COOL_RFID_EXCEPTION_AL = COOL_UPLOAD_AL + "exception/";//捕获rfid异常信息
}
