package com.novacapital.activities;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.novacapital.R;
import com.novacapital.adapters.RetoApiAdapter;
import com.novacapital.api.ApiClient;
import com.novacapital.api.ApiService;
import com.novacapital.models.ClienteResponse;
import com.novacapital.models.ClienteRetoResponse;
import com.novacapital.utils.SessionManager;

import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RetosActivity extends AppCompatActivity {

    private RecyclerView recyclerRetos;
    private TextView tvSaldoActual;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retos);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Retos");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        recyclerRetos = findViewById(R.id.recyclerRetos);
        tvSaldoActual = findViewById(R.id.tvSaldoActual);
        recyclerRetos.setLayoutManager(new LinearLayoutManager(this));
        sessionManager = new SessionManager(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarSaldo();
        cargarRetos();
    }

    private void cargarSaldo() {
        ApiService api = ApiClient.getService(ApiService.class, sessionManager.getToken());
        api.miPerfil().enqueue(new Callback<ClienteResponse>() {
            @Override
            public void onResponse(Call<ClienteResponse> call, Response<ClienteResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    tvSaldoActual.setText("Tu saldo: "
                            + response.body().getSaldoAurus() + " Aurus");
                }
            }
            @Override
            public void onFailure(Call<ClienteResponse> call, Throwable t) { }
        });
    }

    private void cargarRetos() {
        ApiService api = ApiClient.getService(ApiService.class, sessionManager.getToken());
        api.misRetos().enqueue(new Callback<List<ClienteRetoResponse>>() {
            @Override
            public void onResponse(Call<List<ClienteRetoResponse>> call,
                                   Response<List<ClienteRetoResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    recyclerRetos.setAdapter(new RetoApiAdapter(response.body()));
                }
            }
            @Override
            public void onFailure(Call<List<ClienteRetoResponse>> call, Throwable t) {
                Toast.makeText(RetosActivity.this,
                        "Error al cargar retos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() { finish(); return true; }
}