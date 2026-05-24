package com.novacapital.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.novacapital.R;
import com.novacapital.models.ProyectoResponse;

import java.util.List;

public class ProyectoApiAdapter extends RecyclerView.Adapter<ProyectoApiAdapter.ViewHolder> {

    private final List<ProyectoResponse> lista;
    private final OnProyectoClickListener listener;

    public interface OnProyectoClickListener {
        void onInvertirClick(ProyectoResponse proyecto);
    }

    public ProyectoApiAdapter(List<ProyectoResponse> lista, OnProyectoClickListener listener) {
        this.lista    = lista;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_proyecto, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int pos) {
        ProyectoResponse p = lista.get(pos);

        h.tvNombre.setText(p.getNombre());
        h.tvObjetivo.setText("Objetivo: " + p.getObjetivoInversion() + " Aurus");
        h.tvInvertido.setText("Invertido: " + p.getCantidadActual() + " Aurus");
        h.tvEstado.setText(p.getEstado());

        int pct = p.getPorcentajeFinanciacion();
        h.progressBar.setProgress(pct);
        h.tvPorcentaje.setText(pct + "%");

        if ("FINANCIADO".equals(p.getEstado())) {
            h.tvEstado.setTextColor(Color.parseColor("#4CAF50"));
            h.btnInvertir.setEnabled(false);
            h.btnInvertir.setText("Financiado ✓");
        } else {
            h.tvEstado.setTextColor(Color.parseColor("#2196F3"));
            h.btnInvertir.setEnabled(true);
            h.btnInvertir.setText("Invertir");

            // Guardar el proyecto en el botón como tag para recuperarlo en el click
            h.btnInvertir.setTag(p);

            // Listener sin lambda: clase anónima que llama al listener de la activity
            h.btnInvertir.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ProyectoResponse proyecto = (ProyectoResponse) v.getTag();
                    listener.onInvertirClick(proyecto);
                }
            });
        }
    }

    @Override
    public int getItemCount() { return lista.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre, tvObjetivo, tvInvertido, tvEstado, tvPorcentaje;
        ProgressBar progressBar;
        Button btnInvertir;

        ViewHolder(View v) {
            super(v);
            tvNombre     = v.findViewById(R.id.tvNombre);
            tvObjetivo   = v.findViewById(R.id.tvObjetivo);
            tvInvertido  = v.findViewById(R.id.tvInvertido);
            tvEstado     = v.findViewById(R.id.tvEstado);
            tvPorcentaje = v.findViewById(R.id.tvPorcentaje);
            progressBar  = v.findViewById(R.id.progressBar);
            btnInvertir  = v.findViewById(R.id.btnInvertir);
        }
    }
}