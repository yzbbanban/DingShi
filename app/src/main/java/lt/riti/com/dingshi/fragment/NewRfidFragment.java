package lt.riti.com.dingshi.fragment;


import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.clouiotech.pda.rfid.EPCModel;
import com.clouiotech.pda.rfid.IAsynchronousMessage;
import com.clouiotech.util.Helper.Helper_ThreadPool;
import com.google.gson.Gson;

import java.util.List;

import lt.riti.com.dingshi.R;
import lt.riti.com.dingshi.app.StockApplication;
import lt.riti.com.dingshi.entity.Bucket;
import lt.riti.com.dingshi.entity.PublicData;
import lt.riti.com.dingshi.utils.ToastUtil;

/**
 * Created by brander on 2017/9/22.
 */

public class NewRfidFragment extends BaseFragment implements IAsynchronousMessage {
    private static final String TAG = "NewRfidFragment";
    private WebView mWebView;
    private String loginStatus;
    private int inputType;//rfid或二维码
    private static AlertDialog alertDialog;
    private static AlertDialog.Builder builder;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate: ");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.fragment_new_rfid, null);
        mWebView = view.findViewById(R.id.webview);
        initData(this);
        initView();
        initListener();
        Log.i(TAG, "onCreateView: ");
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        initData(this);
    }

    @Override
    protected void initView() {
        mWebView.requestFocus();
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        mWebView.getSettings().setAllowFileAccess(true);// 设置允许访问文件数据
        mWebView.getSettings().setSupportZoom(true);
        mWebView.getSettings().setBuiltInZoomControls(true);
        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.getSettings().setDatabaseEnabled(true);
        //添加客户端支持
        mWebView.setWebChromeClient(new MyWebChromeClient());

        mWebView.loadUrl("file:///android_asset/login.html");
        //在js中调用本地java方法
        mWebView.addJavascriptInterface(new JsInterface(getActivity()), "AndroidWebView");
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url != "") {
                    view.loadUrl(url);   //在当前的webview中跳转到新的url
                    System.out.println("url:" + url);
                }
                return true;
            }
        });
        builder = new AlertDialog.Builder(getActivity());
    }


    @Override
    protected void initListener() {
//        Toast.makeText(getActivity(), "||||||||||||||||", Toast.LENGTH_SHORT).show();

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    /**
     * 按钮按下
     *
     * @param keyCode
     * @param event
     * @return
     */
    public boolean onKeyDown(int keyCode, KeyEvent event, int inputType) {
        this.inputType = inputType;
        Log.i(TAG, "onKeyDown: ");
        Log.i(TAG, "onKeyDown inputType: " + inputType);
        if ("1".equals(loginStatus)) {
            if (keyCode == 12) { // 按下扳机 || keyCode == 4
//            Toast.makeText(getActivity(), "onKeyDown--->: " + keyCode, Toast.LENGTH_SHORT).show();
                if (inputType == 1) {//扫码
                    if (readType == 0) {
                        DeCode(this);
                    } else {//批量
//                        Log.i(TAG, "onKeyDown: 批量");
                        PingPong_Read();
                    }

                } else {//扫描
//            Toast.makeText(getActivity(), "onKeyDown 33--->: ", Toast.LENGTH_SHORT).show();
                    if (readType == 0) {//单次
                        openRfid();
                    } else {//批量
                        PingPong_Read();
                    }
                }
            }
        } else {
            Toast.makeText(getActivity(), "请先登录", Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    private void openRfid() {
        Log.i(TAG, "PingPong_Read: " + (x++));
        isSingle = false;
        if (readType == 1) {
            showList();
        }

        if (!isKeyDown) {
            isKeyDown = true; //
            StockApplication.setIsInStock(1);
            Clear(null);
            CLReader.Read_EPC(_NowReadParam);
            if (PublicData._IsCommand6Cor6B.equals("6C")) {// 读6C标签
                CLReader.Read_EPC(_NowReadParam);
            } else {// 读6B标签
                CLReader.Get6B(_NowAntennaNo + "|1" + "|1" + "|"
                        + "1,000F");
            }
        } else {
            if (keyDownCount < 10000)
                keyDownCount++;
        }
        if (keyDownCount > 100) {
            isLongKeyDown = true;
        }
    }

    int x = 0;

    // 间歇性读
    private void PingPong_Read() {
        if (isStartPingPong)
            return;
        isStartPingPong = true;
        Helper_ThreadPool.ThreadPool_StartSingle(new Runnable() {
            @Override
            public void run() {
                while (isStartPingPong) {
                    try {
                        if (!isPowerLowShow) {
                            if (usingBackBattery && !canUsingBackBattery()) {
                                ToastUtil.showShortToast("电量低");
                            }
                            handler.sendEmptyMessage(0);
                            Thread.sleep(1000); // 一秒钟刷新一次
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    // 停止间歇性读
    private void Pingpong_Stop() {
        isStartPingPong = false;
        CLReader.Stop();
        keyDownCount = 0;
        isKeyDown = false;
        isLongKeyDown = false;
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    if (inputType == 1) {
                        Log.i(TAG, "handleMessage: " + inputType);
                        DeCode(NewRfidFragment.this);
                        showView(getRCodeData());
                    } else {
                        openRfid();
                    }
                    break;
                case 1:
                    builder.setTitle("提示")
                            .setMessage((String) msg.obj)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();

                                }
                            })
                            .setNegativeButton("取消", null);
                    alertDialog = builder.create();
                    if (alertDialog.isShowing()) {
                        alertDialog.dismiss();
                    } else {
                        alertDialog.show();
                    }
                    break;
            }

        }
    };


    /**
     * 显示列表
     */
    protected void showList() {
//        Toast.makeText(getActivity(), "showList: " + getData().size(), Toast.LENGTH_SHORT).show();
        showView(getData());
    }

    /**
     * 展示界面
     *
     * @param buckets
     */
    public void showView(List<Bucket> buckets) {

//        ToastUtil.showShortToast(""+buckets);
//        Log.i(TAG, "showView: " + buckets.toString());
        Gson gson = new Gson();
        String msg = gson.toJson(buckets);
//        Log.i(TAG, "buckets--->: " + msg);
        //调用js中的函数：showInfoFromJava(msg)
        mWebView.loadUrl("javascript:sendInfoFromJava('" + msg + "')");
//        mWebView.loadUrl("javascript:sendInfoFromJava('1')");

    }

    /**
     * js调用此方法
     */
    private class JsInterface {
        private Context mContext;

        public JsInterface(Context context) {
            this.mContext = context;
        }

        //在js中调用window.AndroidWebView.showInfoFromJs(name)，便会触发此方法。
        //判断是否登录
        @JavascriptInterface
        public void showInfoFromJs(String type) {
            loginStatus = type;
//            Toast.makeText(mContext, type, Toast.LENGTH_SHORT).show();
        }

        //判断是否批量读取
        @JavascriptInterface
        public void changeRead(String type) {
            Log.i(TAG, "type: " + type);
//            loginStatus = type;
            readType = Integer.parseInt(type);
//            Toast.makeText(mContext, type, Toast.LENGTH_SHORT).show();
//            Log.i(TAG, "type: "+type);
        }
    }

    /**
     * 清除数据
     *
     * @param v
     */
    protected void Clear(View v) {
        Log.i(TAG, "Clear: ");
        totalReadCount = 0;
        readTime = 0;
        hmList.clear();
        //重新显示
        showList();
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
//        Toast.makeText(getActivity(), "sssssss", Toast.LENGTH_SHORT).show();
        bs.clear();
        hmList.clear();
        if (readType == 1) {
            Pingpong_Stop();
        }
        return super.onKeyUp(keyCode, event);
    }

    /**
     * 接收rfid信号
     *
     * @param model
     */
    @Override
    public void OutPutEPC(EPCModel model) {
        super.OutPutEPC(model, this);
    }

    @Override
    public void onPause() {
        super.onPause();
        // 释放资源
        Dispose();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        ScanDispose();
    }

    class MyWebChromeClient extends WebChromeClient {

        /**
         * 处理加载进度
         *
         * @param view
         * @param newProgress
         */
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
        }

        /**
         * alert弹框
         *
         * @return
         */
        @Override
        public boolean onJsAlert(WebView view, String url, final String message, JsResult result) {
            Log.d("main", "onJsAlert:" + message);
            Message msg = new Message();
            msg.obj = message;
            msg.what = 1;
            handler.sendMessage(msg);

            result.confirm();//这里必须调用，否则页面会阻塞造成假死
            return true;
        }

        //设置响应js 的Confirm()函数
        @Override
        public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
            AlertDialog.Builder b = new AlertDialog.Builder(NewRfidFragment.this.getActivity());
            b.setTitle("提示");
            b.setMessage(message);
            b.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    result.confirm();
                }
            });
            b.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    result.cancel();
                }
            });
            b.create().show();
            result.confirm();//这里必须调用，
            return true;
        }

    }


}
