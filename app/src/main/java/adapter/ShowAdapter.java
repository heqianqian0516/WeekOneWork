package adapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bwei.weekonework.R;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import bean.ShowBean;

public class ShowAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {
     private List<ShowBean.ResultBean.MlssBean.CommodityListBeanXX> list;
     private Context context;
    public ShowAdapter(Context context) {
        this.context = context;
        list = new ArrayList<>();
    }
    public void setmDatas(List<ShowBean.ResultBean.MlssBean.CommodityListBeanXX> datas){
        list.clear();
        if(datas!=null){
            list.addAll(datas);
        }
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      // View view= LayoutInflater.from(context).inflate(R.layout.first_liner_layout,parent,false);
        View view = LayoutInflater.from(context).inflate(R.layout.first_liner_layout, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        ViewHolder holderLeft = (ViewHolder) holder;
       holderLeft.name.setText(list.get(position).getCommodityName());
       holderLeft.prive.setText("￥"+list.get(position).getPrice()+"");
        String image = list.get(position).getMasterPic().split("\\|")[0].replace("https","http");
        holderLeft.sdv.setImageURI(Uri.parse(image));
        holderLeft.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (httpOnClickListener!=null){
                    httpOnClickListener.OnClickListener(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    class ViewHolder extends RecyclerView.ViewHolder{

        private final SimpleDraweeView sdv;
        private final TextView name;
        private final TextView prive;

        public ViewHolder(View itemView) {
            super(itemView);
            sdv = itemView.findViewById(R.id.show_simple);
            name = itemView.findViewById(R.id.show_title);
            prive = itemView.findViewById(R.id.show_price);
        }
    }
    //定义接口
     private HttpOnClickListener httpOnClickListener;
    public void setHttpOnClickListener(HttpOnClickListener httpOnClickListener){
       this.httpOnClickListener=httpOnClickListener;
    }
    public interface HttpOnClickListener{
        void OnClickListener(int position);
    }
}
