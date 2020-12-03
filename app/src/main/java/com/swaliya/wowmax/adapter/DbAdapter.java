package com.swaliya.wowmax.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.swaliya.wowmax.R;
import com.swaliya.wowmax.model.DbModel;

import java.util.ArrayList;

public class DbAdapter extends RecyclerView.Adapter<DbAdapter.ViewHolder> {
    private Context context;
    private ArrayList<DbModel> dbModels;

    public DbAdapter(Context context, ArrayList<DbModel> dbModels) {
        this.context = context;
        this.dbModels = dbModels;
    }

    @NonNull
    @Override
    public DbAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_dblist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DbAdapter.ViewHolder holder, int position) {
        DbModel dbModel = dbModels.get(position);
        holder.tvF.setText(dbModel.getName());
        holder.tvS.setText(dbModel.getDesp());
        holder.tvid.setText(dbModel.getId());

    }

    @Override
    public int getItemCount() {
        return dbModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvF, tvS, tvid;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvF = itemView.findViewById(R.id.tvF);
            tvS = itemView.findViewById(R.id.tvS);
            tvid = itemView.findViewById(R.id.tvid);
        }
    }
}
