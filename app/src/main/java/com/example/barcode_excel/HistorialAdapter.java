package com.example.barcode_excel;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HistorialAdapter extends RecyclerView.Adapter<HistorialAdapter.HistorialViewHolder> {
    private List<HistorialRegistro> listaHistorial;

    public HistorialAdapter(List<HistorialRegistro> listaHistorial) {
        this.listaHistorial = listaHistorial;
    }

    @NonNull
    @Override
    public HistorialViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.historial_item, parent, false);
        return new HistorialViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistorialViewHolder holder, int position) {
        HistorialRegistro registro = listaHistorial.get(position);
        holder.tvBarcode.setText(registro.getBarcode());
        holder.tvCubicleCode.setText(registro.getCubicleCode());
        holder.tvDate.setText(registro.getDate());
    }

    @Override
    public int getItemCount() {
        return listaHistorial.size();
    }

    public static class HistorialViewHolder extends RecyclerView.ViewHolder {
        TextView tvBarcode, tvCubicleCode, tvDate;

        public HistorialViewHolder(View itemView) {
            super(itemView);
            tvBarcode = itemView.findViewById(R.id.tvBarcode);
            tvCubicleCode = itemView.findViewById(R.id.tvCubicleCode);
            tvDate = itemView.findViewById(R.id.tvDate);
        }
    }
}