package com.example.listajesus;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdaptadorVideojuegos
        extends RecyclerView.Adapter<AdaptadorVideojuegos.ViewHolder> {

    // La lista de datos que va a pintar
    private final ArrayList<Videojuego> videojuegos;

    public AdaptadorVideojuegos(ArrayList<Videojuego> videojuegos) {
        this.videojuegos = videojuegos;
    }

    //  guarda las vistas de una fila
    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivImagen;
        TextView tvTitulo;
        TextView tvSubtitulo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImagen = itemView.findViewById(R.id.ivImagen);
            tvTitulo = itemView.findViewById(R.id.tvTitulo);
            tvSubtitulo = itemView.findViewById(R.id.tvSubtitulo);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View fila = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fila_videojuego, parent, false);
        return new ViewHolder(fila);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Videojuego videojuego = videojuegos.get(position);
        holder.tvTitulo.setText(videojuego.getTitulo());
        holder.tvSubtitulo.setText(videojuego.getPlataforma());
        holder.ivImagen.setImageResource(videojuego.getImagen());
    }

    @Override
    public int getItemCount() {
        return videojuegos.size();
    }
}