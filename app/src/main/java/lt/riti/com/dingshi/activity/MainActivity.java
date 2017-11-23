package lt.riti.com.dingshi.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import android.widget.Toast;

import com.clouiotech.port.Adapt;
import com.clouiotech.port.KerHandler;
import com.clouiotech.port.KeyManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import lt.riti.com.dingshi.R;
import lt.riti.com.dingshi.app.StockApplication;
import lt.riti.com.dingshi.fragment.NewRfidFragment;

public class MainActivity extends BaseActivity {
    private static final String TAG = "NewRfidActivity";
    @BindView(R.id.vp_new_fg)
    ViewPager viewPager;
    private FragmentPagerAdapter adapter;
    private List<Fragment> fragments;
    Fragment fg = new Fragment();
    private View newL;
    private int inputType = 0;//默认pda扫描
    private boolean isRCode = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    @Override
    public void onCreateCustomToolBar(Toolbar toolbar) {
        super.onCreateCustomToolBar(toolbar);
        tv_center.setText("");
        tv_right.setText("PDA扫描");
        tv_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isRCode) {//是扫码
                    tv_right.setText("扫码");
                    inputType = 1;
                    isRCode = false;
                } else {//PDA扫描
                    tv_right.setText("PDA扫描");
                    inputType = 0;
                    isRCode = true;
                }
            }
        });
    }

    /**
     * 按下按钮
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.i(TAG, "onKeyDown: " + keyCode);
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 初始化界面
     */
    protected void initView() {
        newL = LayoutInflater.from(this).inflate(R.layout.fragment_new_rfid, null);
        ButterKnife.bind(newL);
//        int a=1/0;
        KeyManager keyManager = Adapt.getKeymanagerInstance();
//        Toast.makeText(this,keyManager.toString(),Toast.LENGTH_SHORT).show();

        keyManager.setKeyHandler(new KerHandler() {
            @Override
            public boolean keyDownHandle(final int i) {
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ((NewRfidFragment) fg).onKeyDown(i, null, inputType);
                    }
                });
                return true;
            }

            @Override
            public boolean keyUpHandle(final int i) {
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ((NewRfidFragment) fragments.get(0)).onKeyUp( i, null);
                    }
                });
                return true;
            }
        });
    }

    /**
     * 初始化数据
     */
    protected void initData() {
        //界面数据
        fragments = new ArrayList<>();
        fragments.add(new NewRfidFragment());
        adapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }
        };

        viewPager.setAdapter(adapter);
        //第一个界面
        viewPager.setCurrentItem(0);
        StockApplication.stockType = 0;//默认入库
        fg = fragments.get(0);
        viewPager.addOnPageChangeListener(new MyOnPageChangeListener());
    }


    private class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            switch (position) {
                case 0:
                    fg = fragments.get(0);
                    break;
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

}
