package com.novacapital.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.novacapital.R;
import com.novacapital.models.InversionResponse;

import java.util.List;

public class InversionApiAdapter extends RecyclerView.Adapter<InversionApiAdapter.ViewHolder> {

    private final List<InversionResponse> lista;

    public InversionApiAdapter(List<InversionResponse> lista) {
        this.lista = lista;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_inversion, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int pos) {
        InversionResponse inv = lista.get(pos);
        h.tvNombreProyecto.setText(inv.getNombreProyecto());
        h.tvCantidad.setText(inv.getCantidad() + " Aurus");
        h.tvFecha.setText(inv.getFechaInversion() != null
                ? inv.getFechaInversion().substring(0, 10)
                : "");
    }

    @Override
    public int getItemCount() { return lista.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombreProyecto, tvCantidad, tvFecha;

        ViewHolder(View v) {
            super(v);
            tvNombreProyecto = v.findViewById(R.id.tvNombreProyecto);
            tvCantidad       = v.findViewById(R.id.tvCantidad);
            tvFecha          = v.findViewById(R.id.tvFecha);
        }
    }
}