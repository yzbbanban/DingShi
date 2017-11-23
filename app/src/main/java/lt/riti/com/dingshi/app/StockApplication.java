package lt.riti.com.dingshi.app;

import android.app.Application;
import android.content.Context;
import android.os.SystemClock;

import lt.riti.com.dingshi.consts.Urls;
import lt.riti.com.dingshi.entity.ResultCode;
import lt.riti.com.dingshi.service.ExceptionService;
import lt.riti.com.dingshi.utils.retrofit.MyCallback;
import lt.riti.com.dingshi.utils.retrofit.RetrofitUtils;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by brander on 2017/9/21.
 */
public class StockApplication extends Application implements Thread.UncaughtExceptionHandler {
    public static long USER_ID = 0L;
    public static String USER_NAME = "";
    private static Context context;
    private static int isInStock = 0;
    public static boolean isOnAppStore = false;
    public static int stockType = 0;//0入库或1出库
//    public static String url = "http://119.23.228.4/rfid/";

      public static String url="http://192.168.0.115:8080/rfid/";
    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }


    public static int getIsInStock() {
        return isInStock;
    }

    public static void setIsInStock(int isInStock) {
        StockApplication.isInStock = isInStock;
    }

    /**
     * 获取全局上下文对象
     *
     * @return
     */
    public static Context getAppContext() {
        return context;
    }

    /**
     * 异常捕获
     *
     * @param thread
     * @param throwable
     */
    @Override
    public void uncaughtException(final Thread thread, final Throwable throwable) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                uploadExceptionToServer(thread, throwable);
            }


        }).start();
        SystemClock.sleep(2000);
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    private void uploadExceptionToServer(Thread thread, Throwable throwable) {
        String msg = "DS,currentThread:" + Thread.currentThread() + "---thread:" + thread.getId() + "---ex:" + throwable.toString();
        Retrofit retrofit = RetrofitUtils.getRetrofit(Urls.COOL_RFID_EXCEPTION_AL);
        ExceptionService request = retrofit.create(ExceptionService.class);
        Call<ResultCode<String>> call = request.call(msg);
        call.enqueue(new MyCallback<ResultCode<String>>() {
            @Override
            public void onSuc(Response<ResultCode<String>> response) {

            }

            @Override
            public void onFail(String message) {

            }
        });
    }
}
