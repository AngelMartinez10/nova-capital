package com.novacapital.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.novacapital.R;
import com.novacapital.adapters.ProyectoApiAdapter;
import com.novacapital.api.ApiClient;
import com.novacapital.api.ApiService;
import com.novacapital.models.ProyectoResponse;
import com.novacapital.utils.SessionManager;

import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListaProyectosActivity extends AppCompatActivity
        implements ProyectoApiAdapter.OnProyectoClickListener {

    private RecyclerView recyclerView;
    private View tvSinProyectos;  // en vez de: private TextView tvSinProyectos;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_proyectos);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Proyectos");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        recyclerView   = findViewById(R.id.recyclerProyectos);
        tvSinProyectos = findViewById(R.id.tvSinProyectos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        sessionManager = new SessionManager(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarProyectos();
    }

    private void cargarProyectos() {
        ApiService api = ApiClient.getService(ApiService.class, sessionManager.getToken());
        api.listarProyectos().enqueue(new Callback<List<ProyectoResponse>>() {
            @Override
            public void onResponse(Call<List<ProyectoResponse>> call,
                                   Response<List<ProyectoResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<ProyectoResponse> proyectos = response.body();
                    if (proyectos.isEmpty()) {
                        tvSinProyectos.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    } else {
                        tvSinProyectos.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        recyclerView.setAdapter(
                                new ProyectoApiAdapter(proyectos, ListaProyectosActivity.this));
                    }
                }
            }
            @Override
            public void onFailure(Call<List<ProyectoResponse>> call, Throwable t) {
                Toast.makeText(ListaProyectosActivity.this,
                        "Error al cargar proyectos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onInvertirClick(ProyectoResponse proyecto) {
        Intent intent = new Intent(this, DetalleProyectoActivity.class);
        intent.putExtra("idProyecto", proyecto.getIdProyecto());
        startActivity(intent);
    }

    @Override
    public boolean onSupportNavigateUp() { finish(); return true; }
}