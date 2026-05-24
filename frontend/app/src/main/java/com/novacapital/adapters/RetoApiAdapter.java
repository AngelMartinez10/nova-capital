package com.novacapital.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.novacapital.R;
import com.novacapital.models.ClienteRetoResponse;

import java.util.ArrayList;
import java.util.List;

public class RetoApiAdapter extends RecyclerView.Adapter<RetoApiAdapter.ViewHolder> {

    private final List<ClienteRetoResponse> lista;

    public RetoApiAdapter(List<ClienteRetoResponse> listaOriginal) {
        // Primero los pendientes, luego los completados
        List<ClienteRetoResponse> pendientes  = new ArrayList<>();
        List<ClienteRetoResponse> completados = new ArrayList<>();
        for (ClienteRetoResponse r : listaOriginal) {
            if (r.isCompletado()) completados.add(r);
            else                  pendientes.add(r);
        }
        lista = new ArrayList<>();
        lista.addAll(pendientes);
        lista.addAll(completados);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_reto, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int pos) {
        ClienteRetoResponse reto = lista.get(pos);

        h.tvTitulo.setText(reto.getTitulo());
        h.tvRecompensa.setText("+" + reto.getRecompensa() + " Aurus");

        if (reto.isCompletado()) {
            h.tvEstado.setText("✅");
            h.tvTitulo.setTextColor(Color.parseColor("#AAAAAA"));
            h.tvRecompensa.setTextColor(Color.parseColor("#AAAAAA"));
            h.card.setCardBackgroundColor(Color.parseColor("#0F1F0F"));
        } else {
            h.tvEstado.setText("⏳");
            h.tvTitulo.setTextColor(Color.parseColor("#FFFFFF"));
            h.tvRecompensa.setTextColor(Color.parseColor("#F0C040"));
            h.card.setCardBackgroundColor(Color.parseColor("#1A2B3C"));
        }
    }

    @Override
    public int getItemCount() { return lista.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitulo, tvRecompensa, tvEstado;
        CardView card;

        ViewHolder(View v) {
            super(v);
            tvTitulo     = v.findViewById(R.id.tvTitulo);
            tvRecompensa = v.findViewById(R.id.tvRecompensa);
            tvEstado     = v.findViewById(R.id.tvEstado);
            card         = (CardView) v; // item_reto.xml tiene CardView como raíz
        }
    }
}