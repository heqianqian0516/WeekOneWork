package com.bwei.weekonework;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.stx.xhb.xbanner.XBanner;
import com.sunfusheng.marqueeview.MarqueeView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import adapter.ShowAdapter;
import api.Apis;
import bean.BannerBean;
import bean.DetailBean;
import bean.EventBean;
import bean.ShowBean;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import presenter.PresenterImp;
import view.IView;

public class MainActivity extends AppCompatActivity implements IView {
    private static final String TAG = "MainActivity++++++";
    @BindView(R.id.saoMa)
    ImageView mSaoMa;
    @BindView(R.id.banner)
    XBanner mBanner;
    @BindView(R.id.marqueeView)
    MarqueeView mMarqueeView;
    @BindView(R.id.recy)
    XRecyclerView mRecy;
    private PresenterImp presenter;
    private List<String> mImage;
    private ShowAdapter showAdapter;
    private int page;
    private int count=5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        presenter = new PresenterImp(this);
        initLoad();
        initLoadData();
        initData();
        mImage = new ArrayList<>();

        List<String> info = new ArrayList<>();
        info.add("八维电商喜迎双11，年终大促销");
        info.add(" 欢迎大家关注抢购哦！");
        mMarqueeView.startWithList(info);
        // 在代码里设置自己的动画
        mMarqueeView.startWithList(info, R.anim.anim_bottom_in, R.anim.anim_top_out);
    }

    private void initData() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecy.setLayoutManager(linearLayoutManager);
        showAdapter = new ShowAdapter(MainActivity.this);
        mRecy.setAdapter(showAdapter);
        mRecy.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                page=1;
                initLoadData();
            }

            @Override
            public void onLoadMore() {
                initLoadData();
            }
        });
    }

    private void initLoadData() {
        presenter.startRequestGet(Apis.URL_SHOW, ShowBean.class);
    }

    private void initLoad() {
        presenter.startRequestGet(Apis.URL_BANNER, BannerBean.class);
    }

    @Override
    public void onRequestSuccess(Object o) {
        if (o instanceof BannerBean) {
            BannerBean bannerBean = (BannerBean) o;
            if (bannerBean == null) {
                Toast.makeText(MainActivity.this, bannerBean.getMessage(), Toast.LENGTH_SHORT).show();
            } else {
                for (int i = 0; i < bannerBean.getResult().size(); i++) {
                    mImage.add(bannerBean.getResult().get(i).getImageUrl());
                    //加载图片
                    initImageData();
                }
            }
        } else if (o instanceof ShowBean) {
            final ShowBean showBean = (ShowBean) o;
            if (showBean == null) {
                Toast.makeText(MainActivity.this, showBean.getMessage(), Toast.LENGTH_SHORT).show();
            } else {
                showAdapter.setmDatas(showBean.getResult().getMlss().get(0).getCommodityList());
                showAdapter.setHttpOnClickListener(new ShowAdapter.HttpOnClickListener() {
                    @Override
                    public void OnClickListener(int position) {
                        int commodityId=showBean.getResult().getMlss().get(0).getCommodityList().get(position).getCommodityId();
                        getGoods(commodityId);
                    }
                });
                page++;
                mRecy.loadMoreComplete();
                mRecy.refreshComplete();
            }
        }
        else if(o instanceof DetailBean){
            DetailBean detailBean= (DetailBean) o;
            EventBus.getDefault().postSticky(new EventBean("goods",o));
            startActivity(new Intent(MainActivity.this,DetailActivity.class));
        }


    }
   private void getGoods(int id){
        presenter.startRequestGet(Apis.URL_DETAIL+"?commodityId="+id,DetailBean.class);
   }
    private void initImageData() {
        mBanner.setData(mImage, null);
        mBanner.loadImage(new XBanner.XBannerAdapter() {
            @Override
            public void loadBanner(XBanner banner, Object model, View view, int position) {
                Glide.with(MainActivity.this).load(mImage.get(position)).into((ImageView) view);
            }
        });
    }

    @Override
    public void onRequestFail(Object error) {

    }

    @OnClick(R.id.saoMa)
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.saoMa:
                Toast.makeText(MainActivity.this,"点击了",Toast.LENGTH_SHORT).show();
                if (ActivityCompat.checkSelfPermission(MainActivity.this,Manifest.permission.CAMERA)==PackageManager.PERMISSION_GRANTED){
                //扫描操作
                    IntentIntegrator integrator = new IntentIntegrator(MainActivity.this);
                    integrator.initiateScan();

                }else{
               ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.CAMERA},100);
              }
           break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==100){
            if (grantResults[0]==PackageManager.PERMISSION_GRANTED){
                Toast.makeText(MainActivity.this,"权限打开",Toast.LENGTH_SHORT).show();
            }else{
                finish();
            }
        }
    }
    @Override
 protected void onActivityResult(int requestCode, int resultCode, Intent data) {
 super.onActivityResult(requestCode, resultCode, data);
             IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
              if (scanResult != null) {
                String result = scanResult.getContents();

               //  Log.d("code", result);
               //变量result就是二维码解码后的信息。
                 Toast.makeText(this,result, Toast.LENGTH_LONG).show();
               }
         }


}
