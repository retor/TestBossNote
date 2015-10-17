package com.retor.testbossnote.adapter;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.retor.testbossnote.R;
import com.retor.testbossnote.beans.Employee;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by retor on 14.10.2015.
 */
public class BaseAdapter extends RecyclerView.Adapter<BaseAdapter.SimpleHolder> {
    private List<Employee> list;

    public BaseAdapter(List<Employee> list) {
        this.list = list;
    }

    public void setItems(List<Employee> items) {
        this.list = items;
        notifyDataSetChanged();
    }

    public void addItem(Employee item) {
        if (list == null)
            list = new ArrayList<>();
        list.add(item);
        notifyItemInserted(list.indexOf(item));
    }

    public void removeItem(Employee item) {
        int pos = list.indexOf(item);
        list.remove(item);
        notifyItemRemoved(pos);
    }

    public List<Employee> getList() {
        return list;
    }

    @Override
    public SimpleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View out = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder, parent, false);
        return new SimpleHolder(out);
    }

    @Override
    public void onBindViewHolder(final SimpleHolder holder, final int position) {
        final Employee emp = list.get(position);
        String text = emp.getName() + " " + emp.getSurname();
        holder.textView.setText(text);
        Handler h = new Handler(Looper.getMainLooper());
        h.post(new Runnable() {
            @Override
            public void run() {
                if (emp.getPicture_url() != null) {
                    holder.image.setImageBitmap(null);
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                    Bitmap bitmap = BitmapFactory.decodeFile(emp.getPicture_url(), options);
                    holder.image.setImageBitmap(bitmap);
                } else {
                    holder.image.setImageBitmap(null);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class SimpleHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.holder_image)
        ImageView image;
        @Bind(R.id.holder_text)
        TextView textView;

        public SimpleHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
