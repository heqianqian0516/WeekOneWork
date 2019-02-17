package com.bwei.weekonework;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.youth.banner.Banner;
import com.youth.banner.loader.ImageLoader;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Arrays;
import java.util.List;

import bean.DetailBean;
import bean.EventBean;
import butterknife.BindView;
import butterknife.ButterKnife;
import presenter.PresenterImp;
import view.IView;

public class DetailActivity extends AppCompatActivity implements IView {

    @BindView(R.id.banner)
    Banner mBanner;
    @BindView(R.id.textTitle)
    TextView mTextTitle;
    @BindView(R.id.text_price)
    TextView mTextPrice;
    @BindView(R.id.webView)
    WebView mWebView;
    private PresenterImp presenter;
    private int commodityId;
    private DetailBean detailBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        presenter = new PresenterImp(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.POSTING, sticky = true)
    public void onEvent(EventBean eventBean) {
        if (eventBean.getName().equals("goods")) {
            detailBean = (DetailBean) eventBean.getClazz();
            commodityId = detailBean.getResult().getCommodityId();
            initLoad();
        }

    }

    private void initLoad() {
        String details = detailBean.getResult().getDetails();
        String picture = detailBean.getResult().getPicture();
        String[] split = picture.split(",");
        List<String> list = Arrays.asList(split);
        mWebView.loadDataWithBaseURL(null, details, "text/html", "utf-8", null);

        // 设置可以支持缩放
        mWebView.getSettings().setSupportZoom(true);
        // 设置出现缩放工具
        mWebView.getSettings().setBuiltInZoomControls(true);
        //扩大比例的缩放
        mWebView.getSettings().setUseWideViewPort(true);
        //自适应屏幕
        //mWebView.getSettings().setLayoutAlgorithm(lay.SINGLE_COLUMN);
        mBanner.setImageLoader(new GlideImageLoader());
        mBanner.setImages(list);
        mBanner.start();
        mTextTitle.setText(detailBean.getResult().getCommodityName());
        mTextPrice.setText("￥" + detailBean.getResult().getPrice() + "");
    }

    private class GlideImageLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            Glide.with(context).load(path).into(imageView);
        }
    }

    @Override
    public void onRequestSuccess(Object o) {

    }

    @Override
    public void onRequestFail(Object error) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
