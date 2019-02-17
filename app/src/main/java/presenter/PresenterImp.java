package presenter;

import java.util.Map;

import callBack.MyCallBack;
import model.ModelImp;
import view.IView;

public class PresenterImp implements IPresenter {
    private IView mIView;
    private ModelImp model;
    public PresenterImp(IView iView){
       this.mIView=iView;
       model=new ModelImp();
    }
    @Override
    public void startRequestGet(String url, Class clazz) {
        model.onRequestGet(url, clazz, new MyCallBack() {
            @Override
            public void onSuccess(Object data) {
                mIView.onRequestSuccess(data);
            }

            @Override
            public void onFail(String error) {
              mIView.onRequestFail(error);
            }
        });
    }

    @Override
    public void startRequestPost(String url, final Map map, Class clazz) {
           model.onRequestPost(url, map, clazz, new MyCallBack() {
               @Override
               public void onSuccess(Object data) {
                   mIView.onRequestSuccess(data);
               }

               @Override
               public void onFail(String error) {
                   mIView.onRequestFail(error);
               }
           });
    }
    public void onDeath(){
        if (mIView!=null){
            mIView=null;
        }
        if (model!=null){
            model=null;
        }
    }
}
