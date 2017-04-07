package com.gopetting.rekha;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

import mehdi.sakout.fancybuttons.FancyButton;

/**
 * Created by rekha on 4/6/2017.
 */

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyViewHolder> {

    public final String TAG = getClass().getName();
    private final LayoutInflater inflater;
    public int cartcount;
    public List<ObjectsBean> cartlist = Collections.emptyList();

    Context context;

    public CartAdapter(Context context, List<ObjectsBean> cartlist, int cartcount) {
        this.cartlist = cartlist;
        this.context = context;
        this.cartcount = cartcount;
        Log.d(TAG, "i mi n adapter ");
        inflater = LayoutInflater.from(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_item_row, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(itemView);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final ObjectsBean bean = cartlist.get(position);
        holder.setIsRecyclable(false);

        holder.PersonName.setText(bean.getName());
        holder.date.setText(bean.getEndDate());
        Log.e(TAG, "USER IMAGE: " + bean.getIcon());
        if (bean.getIcon().contains(".png") || bean.getIcon().contains(".jpg")) {
            Picasso.with(context).load(bean.getIcon()).into(holder.person_pic);
            Picasso.with(context).load(bean.getIcon()).into(holder.full_guide_img);
        }
        holder.btn_remove.setVisibility(View.VISIBLE);
        holder.btn_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cartlist.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, cartlist.size());
                holder.row.setVisibility(View.GONE);
                Toast.makeText(context, "Your item removed from Cart ", Toast.LENGTH_SHORT).show();
                cartcount--;
            }
        });
    }

    @Override
    public int getItemCount() {
        Log.e(TAG, "info size " + cartlist.size());
        return cartlist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView PersonName, date;
        public ImageView person_pic, full_guide_img;
        LinearLayout row;
        FancyButton btn_remove, btn_add;

        public MyViewHolder(View view) {
            super(view);
            row = (LinearLayout) view.findViewById(R.id.row_layout);
            PersonName = (TextView) view.findViewById(R.id.PersonName);
            date = (TextView) view.findViewById(R.id.date);
            person_pic = (ImageView) view.findViewById(R.id.person_pic);
            full_guide_img = (ImageView) view.findViewById(R.id.full_guide_img);
            btn_remove = (FancyButton) view.findViewById(R.id.btn_remove);
            btn_add = (FancyButton) view.findViewById(R.id.btn_add);

        }
    }
}

