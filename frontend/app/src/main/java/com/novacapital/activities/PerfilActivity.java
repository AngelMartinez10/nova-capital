package com.novacapital.activities;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.novacapital.R;
import com.novacapital.adapters.InversionApiAdapter;
import com.novacapital.api.ApiClient;
import com.novacapital.api.ApiService;
import com.novacapital.models.ClienteResponse;
import com.novacapital.models.InversionResponse;
import com.novacapital.utils.SessionManager;

import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PerfilActivity extends AppCompatActivity {

    private TextView tvNombreUsuario, tvSaldoAurus, tvNumInversiones;
    private RecyclerView recyclerHistorial;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Mi Perfil");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        tvNombreUsuario  = findViewById(R.id.tvNombreUsuario);
        tvSaldoAurus     = findViewById(R.id.tvSaldoAurus);
        tvNumInversiones = findViewById(R.id.tvNumInversiones);
        recyclerHistorial = findViewById(R.id.recyclerHistorial);
        recyclerHistorial.setLayoutManager(new LinearLayoutManager(this));

        sessionManager = new SessionManager(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarPerfil();
        cargarHistorial();
    }

    private void cargarPerfil() {
        ApiService api = ApiClient.getService(ApiService.class, sessionManager.getToken());
        api.miPerfil().enqueue(new Callback<ClienteResponse>() {
            @Override
            public void onResponse(Call<ClienteResponse> call, Response<ClienteResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ClienteResponse cliente = response.body();
                    tvNombreUsuario.setText(cliente.getNombre() + " " + cliente.getApellidos());
                    tvSaldoAurus.setText(cliente.getSaldoAurus() + " Aurus");
                }
            }
            @Override
            public void onFailure(Call<ClienteResponse> call, Throwable t) { }
        });
    }

    private void cargarHistorial() {
        ApiService api = ApiClient.getService(ApiService.class, sessionManager.getToken());
        api.misInversiones().enqueue(new Callback<List<InversionResponse>>() {
            @Override
            public void onResponse(Call<List<InversionResponse>> call,
                                   Response<List<InversionResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<InversionResponse> historial = response.body();
                    tvNumInversiones.setText("Inversiones realizadas: " + historial.size());
                    recyclerHistorial.setAdapter(new InversionApiAdapter(historial));
                }
            }
            @Override
            public void onFailure(Call<List<InversionResponse>> call, Throwable t) {
                Toast.makeText(PerfilActivity.this,
                        "Error al cargar historial", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() { finish(); return true; }
}