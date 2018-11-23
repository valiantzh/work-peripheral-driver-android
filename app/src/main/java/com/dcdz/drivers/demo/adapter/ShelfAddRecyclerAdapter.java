package com.dcdz.drivers.demo.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.dcdz.drivers.R;
import com.dcdz.drivers.utils.Constant;
import com.hzdongcheng.drivers.peripheral.plc.model.ShelfDeskConfig;

import java.util.List;

public class ShelfAddRecyclerAdapter extends RecyclerView.Adapter<ShelfAddRecyclerAdapter.MyViewHolder> {

    Context context;
    protected LayoutInflater inflater;
    List<ShelfDeskConfig> tbDesks;
    public IAddClickListener iAddClickListener;
    public IDelClickListener iDelClickListener;
    public IShelfClickListener iShelfClickListener;
    public ISpinnerClickListener iSpinnerClickListener;
    public boolean enableEdit;

    public ShelfAddRecyclerAdapter(Context context){
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    public void setShelfData(List<ShelfDeskConfig> tbDesks){
        this.tbDesks = tbDesks;
    }

    public void setAddItemClickListener(IAddClickListener iAddClickListener) {
        this.iAddClickListener = iAddClickListener;
    }

    public void setDelItemClickListener(IDelClickListener iDelClickListener) {
        this.iDelClickListener = iDelClickListener;
    }

    public void setiShelfClickListener(IShelfClickListener iShelfClickListener){
        this.iShelfClickListener = iShelfClickListener;
    }

    public void setiSpinnerClickListener(ISpinnerClickListener iSpinnerClickListener){
        this.iSpinnerClickListener = iSpinnerClickListener;
    }

    public void setEnableEdit(boolean enableEdit){
        this.enableEdit = enableEdit;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.item_shelf_add, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        holder.spLayer.setTag(position);
        holder.ibShelfAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iAddClickListener.onItemClick(v, position);
            }
        });
        holder.ibShelfDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iDelClickListener.onItemClick(v, position);
            }
        });
        holder.ibShelf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iShelfClickListener.onItemClick(v, position);
            }
        });
        holder.tvNum.setText(Constant.UPPER_CHAR[position] + "");
        holder.spLayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tag = (int) holder.spLayer.getTag();
                iSpinnerClickListener.onItemClick(holder.spLayer, tag);
            }
        });
        holder.spLayer.setText(tbDesks.get(position).getLayerCount() + "");
        if (position == tbDesks.size()-1){
            holder.ibShelfAdd.setVisibility(View.VISIBLE);
            holder.ibShelfDel.setVisibility(View.VISIBLE);
        }else {
            holder.ibShelfAdd.setVisibility(View.INVISIBLE);
            holder.ibShelfDel.setVisibility(View.INVISIBLE);
        }
        if (tbDesks.size() == 0){
            holder.ibShelfAdd.setVisibility(View.VISIBLE);
        }
        if (enableEdit && position == tbDesks.size()-1){
            holder.spLayer.setEnabled(true);
            holder.ibShelfDel.setVisibility(View.VISIBLE);
            holder.ibShelfAdd.setVisibility(View.VISIBLE);
        }else {
            holder.spLayer.setEnabled(false);
            holder.ibShelfDel.setVisibility(View.GONE);
            holder.ibShelfAdd.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return tbDesks == null ? 0 : tbDesks.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tvNum;
        TextView spLayer;
        ImageButton ibShelfAdd;
        ImageButton ibShelfDel;
        ImageButton ibShelf;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvNum = itemView.findViewById(R.id.tv_num);
            spLayer = itemView.findViewById(R.id.sp_layer);
            ibShelfAdd = itemView.findViewById(R.id.ib_shelf_add);
            ibShelfDel = itemView.findViewById(R.id.ib_shelf_del);
            ibShelf = itemView.findViewById(R.id.ib_shelf);
        }
    }

    //点击事件的回调
    public interface IAddClickListener {
        void onItemClick(View view , int position);
    }
    public interface IDelClickListener{
        void onItemClick(View view , int position);
    }
    public interface IShelfClickListener{
        void onItemClick(View view , int position);
    }
    public interface ISpinnerClickListener{
        void onItemClick(TextView view, int shelfNo);
    }
}
