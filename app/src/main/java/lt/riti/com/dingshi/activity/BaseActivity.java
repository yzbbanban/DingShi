package lt.riti.com.dingshi.activity;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import lt.riti.com.dingshi.utils.ToolbarHelper;


/**
 * Created by brander on 2017/9/20.
 */

public class BaseActivity extends AppCompatActivity implements View.OnClickListener {
    private ToolbarHelper mToolBarHelper;
    public Toolbar toolbar;
    public TextView tv_center;
    public TextView tv_right;

    protected void initView() {
    }

    protected void initData() {
    }

    protected void initListener() {
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        mToolBarHelper = new ToolbarHelper(this, layoutResID);
        toolbar = mToolBarHelper.getToolBar();
        toolbar.setTitle("");
        tv_center = mToolBarHelper.getTvCenter();
        tv_right = mToolBarHelper.getTvRight();
        tv_right.setOnClickListener(this);
        //返回帧布局视图
        setContentView(mToolBarHelper.getContentView());
        setSupportActionBar(toolbar);//把toolbar设置到activity中
        onCreateCustomToolBar(toolbar);
    }

    public void onCreateCustomToolBar(Toolbar toolbar) {
        //插入toolbar视图的内容的起始点与结束点
        toolbar.setContentInsetsRelative(0, 0);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static void logInfo(Class cls, String message) {
        Log.i(cls.getName(), message);
    }

    @Override
    public void onClick(View view) {

    }
}
