package ru.nondoanything.promoshares.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;
import java.util.zip.Inflater;

import ru.nondoanything.promoshares.R;
import ru.nondoanything.promoshares.pojo.Data;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.DataViewHolder> {

    private List<Data> promodata;
    private OnPromoListener promoListener;

    public OnPromoListener getPromoListener() {
        return promoListener;
    }

    public void setPromoListener(OnPromoListener promoListener) {
        this.promoListener = promoListener;
    }

    public List<Data> getPromodata() {
        return promodata;
    }

    public void setPromodata(List<Data> promodata) {
        this.promodata = promodata;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.data_item, parent, false);
        return new DataViewHolder(view, promoListener);
    }

    @Override
    public void onBindViewHolder(@NonNull DataViewHolder holder, int position) {
        Data data = promodata.get(position);
        holder.promoTitle.setText(data.getTitle());

        Glide
                .with(holder.itemView.getContext())
                .load(data.getCover())
                .placeholder(R.drawable.ic_launcher_background)
                .into(holder.dataPhoto);

    }

    @Override
    public int getItemCount() {
        return promodata.size();
    }

    class DataViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView promoTitle;
        ImageView dataPhoto;
        OnPromoListener onPromoListener;

        public DataViewHolder(@NonNull View itemView, OnPromoListener onPromoListener) {
            super(itemView);

            promoTitle = itemView.findViewById(R.id.PromoTitle);
            dataPhoto = itemView.findViewById(R.id.DataPhoto);

            this.onPromoListener = onPromoListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onPromoListener.onPromoCLick(getAdapterPosition());
        }
    }

    public interface OnPromoListener {
        void onPromoCLick(int position);
    }
}
