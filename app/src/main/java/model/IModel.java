package model;

import java.util.Map;

import callBack.MyCallBack;

public interface IModel<T> {
    void onRequestGet(String url, Class clazz, MyCallBack myCallBack);
    void onRequestPost(String url, Map<String,String>map,Class clazz,MyCallBack myCallBack);
}
