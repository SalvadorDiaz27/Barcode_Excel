package com.example.barcode_excel;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class CubiculoAdapter extends RecyclerView.Adapter<CubiculoAdapter.ViewHolder> {

    // Definir Cubiculo como una clase interna
    public static class Cubiculo {
        private String cubo;

        public Cubiculo(String cubo) {
            this.cubo = cubo;
        }

        public String getCubo() {
            return cubo;
        }
    }

    private List<Cubiculo> cubiculos;

    public CubiculoAdapter(List<Cubiculo> cubiculos) {
        this.cubiculos = cubiculos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_element, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Cubiculo cubiculo = cubiculos.get(position);
        holder.nameTextView.setText(cubiculo.getCubo());
        // El descTextView se puede ajustar aquí si decides añadir más datos al modelo Cubiculo

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("CubiculoAdapter", "Cubículo seleccionado: " + cubiculo.getCubo());
                // Crear un Intent para iniciar ScannerPage
                Intent intent = new Intent(v.getContext(), ScannerPage.class);
                // Pasar el cubo seleccionado a ScannerPage
                intent.putExtra("CUBICULO_SELECCIONADO", cubiculo.getCubo());
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cubiculos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView;
        public TextView descTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            descTextView = itemView.findViewById(R.id.descTextView); // Si descTextView no se usa, se puede eliminar
        }
    }
}