package com.novacapital.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.novacapital.R;
import com.novacapital.api.ApiClient;
import com.novacapital.api.ApiService;
import com.novacapital.models.ClienteResponse;
import com.novacapital.utils.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private TextView tvBienvenida, tvSaldo;
    private Button btnProyectos, btnCrearProyecto, btnPerfil, btnRetos, btnLogout;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sessionManager = new SessionManager(this);

        if (!sessionManager.haySesion()) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_main);

        tvBienvenida     = findViewById(R.id.tvBienvenida);
        tvSaldo          = findViewById(R.id.tvSaldo);
        btnProyectos     = findViewById(R.id.btnProyectos);
        btnCrearProyecto = findViewById(R.id.btnCrearProyecto);
        btnPerfil        = findViewById(R.id.btnPerfil);
        btnRetos         = findViewById(R.id.btnRetos);
        btnLogout        = findViewById(R.id.btnLogout);

        tvBienvenida.setText("¡Hola, " + sessionManager.getNombre() + "!");
        tvSaldo.setText("Saldo: " + sessionManager.getSaldo() + " Aurus");

        btnProyectos.setOnClickListener(v ->
                startActivity(new Intent(this, ListaProyectosActivity.class)));
        btnCrearProyecto.setOnClickListener(v ->
                startActivity(new Intent(this, CrearProyectoActivity.class)));
        btnPerfil.setOnClickListener(v ->
                startActivity(new Intent(this, PerfilActivity.class)));
        btnRetos.setOnClickListener(v ->
                startActivity(new Intent(this, RetosActivity.class)));

        btnLogout.setOnClickListener(v -> {
            sessionManager.cerrarSesion();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refrescar saldo desde la API
        String token = sessionManager.getToken();
        ApiService api = ApiClient.getService(ApiService.class, token);
        api.miPerfil().enqueue(new Callback<ClienteResponse>() {
            @Override
            public void onResponse(Call<ClienteResponse> call, Response<ClienteResponse> r) {
                if (r.isSuccessful() && r.body() != null) {
                    String saldo = r.body().getSaldoAurus().toPlainString();
                    tvSaldo.setText("Saldo: " + saldo + " Aurus");
                    sessionManager.guardarSesion(
                            token,
                            sessionManager.getIdCliente(),
                            r.body().getNombre(),
                            saldo
                    );
                }
            }
            @Override
            public void onFailure(Call<ClienteResponse> call, Throwable t) { }
        });
    }

}