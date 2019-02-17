package model;

import com.google.gson.Gson;

import java.util.Map;

import api.RetrofiteManage;
import callBack.MyCallBack;

public class ModelImp implements IModel {

    @Override
    public void onRequestGet(String url, final Class clazz, final MyCallBack myCallBack) {

       RetrofiteManage.getInstance().get(url, new RetrofiteManage.HttpListener() {
           @Override
           public void onSuccess(String data) {
               try {
                   Object o=new Gson().fromJson(data,clazz);
                   if (myCallBack!=null){
                       myCallBack.onSuccess(o);
                   }
               }catch (Exception e){
                   e.printStackTrace();
                   if (myCallBack!=null){
                       myCallBack.onFail(e.getMessage());
                   }
               }
           }

           @Override
           public void onFail(String error) {
               if (myCallBack!=null){
                   myCallBack.onFail(error);
               }
           }
       });
    }

    @Override
    public void onRequestPost(String url, Map map, final Class clazz, final MyCallBack myCallBack) {

        RetrofiteManage.getInstance().post(url, map, new RetrofiteManage.HttpListener() {
            @Override
            public void onSuccess(String data) {
                try {
                    Object o=new Gson().fromJson(data,clazz);
                    if (myCallBack!=null){
                        myCallBack.onSuccess(o);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    if (myCallBack!=null){
                        myCallBack.onFail(e.getMessage());
                    }
                }
            }

            @Override
            public void onFail(String error) {
                if (myCallBack!=null)  {
                    myCallBack.onFail(error);
                }
            }
        });
    }
}
