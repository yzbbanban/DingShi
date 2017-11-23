package lt.riti.com.dingshi.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import lt.riti.com.dingshi.R;


/**
 * Created by brander on 2017/9/21.
 */
@SuppressWarnings("ResourceType")
public class ToolbarHelper {
    private Context mContext;
    private FrameLayout mContentView;
    //自己定义
    private View mUserView;
    private Toolbar mToolBar;
    private LayoutInflater mInflater;
    private TextView mtv_center;
    private TextView mtv_right;
    //1.toolbar是否悬浮在窗口之上
    //2.toolbar的高度
    private static int[] ATTRS = {
            R.attr.windowActionBarOverlay,
            R.attr.actionBarSize
    };

    public ToolbarHelper(Context context, int layoutId) {
        this.mContext = context;
        mInflater = LayoutInflater.from(mContext);
        //初始化内容
        initContentView();
        //初始化用户布局
        initUserView(layoutId);
        initToolBar();
    }

    private void initContentView() {
        //创建布局作为容器
        mContentView = new FrameLayout(mContext);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        mContentView.setLayoutParams(params);
    }

    private void initUserView(int layoutId) {
        mUserView = mInflater.inflate(layoutId, null);
        //LayoutParams相当于命令传递者
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        //设置自定义属性到控件中
        TypedArray typedArray = mContext.getTheme().obtainStyledAttributes(ATTRS);
        //获取主题中定义的悬浮标志
        boolean overly = typedArray.getBoolean(0, false);
        //获取主题中定义的toolbar的高度
        int toolBarSize = (int) typedArray.getDimension(1, (int) mContext.getResources().getDimension(R.dimen.abc_action_bar_default_height_material));
        typedArray.recycle();
        //如果是悬浮状态，则不需要设置间距
        params.topMargin = overly ? 0 : toolBarSize;
        mContentView.addView(mUserView, params);
    }

    private void initToolBar() {

        View toolbar = mInflater.inflate(R.layout.toolbar, mContentView);
        mToolBar = (Toolbar) toolbar.findViewById(R.id.fl_toolbar);
        mtv_center = (TextView) toolbar.findViewById(R.id.tv_center);
        mtv_right = (TextView) toolbar.findViewById(R.id.tv_right);
    }

    public FrameLayout getContentView() {
        return mContentView;
    }

    public Toolbar getToolBar() {
        return mToolBar;
    }

    //中间标题
    public TextView getTvCenter() {
        return mtv_center;
    }

    //右侧标题
    public TextView getTvRight() {
        return mtv_right;
    }


}
