package com.novacapital.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.novacapital.R;
import com.novacapital.api.ApiClient;
import com.novacapital.api.ApiService;
import com.novacapital.models.InversionRequest;
import com.novacapital.models.InversionResponse;
import com.novacapital.models.ProyectoResponse;
import com.novacapital.utils.SessionManager;

import java.math.BigDecimal;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetalleProyectoActivity extends AppCompatActivity {

    private TextView tvNombre, tvDescripcion, tvObjetivo, tvActual, tvEstado, tvPorcentaje;
    private ProgressBar progressBar;
    private EditText etCantidad;
    private Button btnInvertir;

    private SessionManager sessionManager;
    private ProyectoResponse proyecto;
    private int idProyecto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_proyecto);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Detalle del Proyecto");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        tvNombre      = findViewById(R.id.tvNombre);
        tvDescripcion = findViewById(R.id.tvDescripcion);
        tvObjetivo    = findViewById(R.id.tvObjetivo);
        tvActual      = findViewById(R.id.tvActual);
        tvEstado      = findViewById(R.id.tvEstado);
        tvPorcentaje  = findViewById(R.id.tvPorcentaje);
        progressBar   = findViewById(R.id.progressBar);
        etCantidad    = findViewById(R.id.etCantidad);
        btnInvertir   = findViewById(R.id.btnInvertir);

        sessionManager = new SessionManager(this);

        idProyecto = getIntent().getIntExtra("idProyecto", -1);
        if (idProyecto == -1) {
            Toast.makeText(this, "Error: proyecto no encontrado", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        cargarProyecto();
        btnInvertir.setOnClickListener(v -> realizarInversion());
    }

    private void cargarProyecto() {
        ApiService api = ApiClient.getService(ApiService.class, sessionManager.getToken());
        api.obtenerProyecto(idProyecto).enqueue(new Callback<ProyectoResponse>() {
            @Override
            public void onResponse(Call<ProyectoResponse> call, Response<ProyectoResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    proyecto = response.body();
                    mostrarDatosProyecto();
                }
            }
            @Override
            public void onFailure(Call<ProyectoResponse> call, Throwable t) {
                Toast.makeText(DetalleProyectoActivity.this,
                        "Error al cargar el proyecto", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void mostrarDatosProyecto() {
        tvNombre.setText(proyecto.getNombre());
        tvDescripcion.setText(proyecto.getDescripcion());
        tvObjetivo.setText("Objetivo: " + proyecto.getObjetivoInversion() + " Aurus");
        tvActual.setText("Invertido: " + proyecto.getCantidadActual() + " Aurus");
        tvEstado.setText("Estado: " + proyecto.getEstado());
        int pct = proyecto.getPorcentajeFinanciacion();
        tvPorcentaje.setText(pct + "%");
        progressBar.setProgress(pct);

        if ("FINANCIADO".equals(proyecto.getEstado())) {
            btnInvertir.setEnabled(false);
            btnInvertir.setText("Proyecto Financiado ✓");
        }
    }

    private void realizarInversion() {
        String cantidadStr = etCantidad.getText().toString().trim();

        if (TextUtils.isEmpty(cantidadStr)) {
            etCantidad.setError("Introduce una cantidad");
            return;
        }

        BigDecimal cantidad;
        try {
            cantidad = new BigDecimal(cantidadStr);
            if (cantidad.compareTo(BigDecimal.ZERO) <= 0) {
                etCantidad.setError("La cantidad debe ser mayor que 0");
                return;
            }
        } catch (NumberFormatException e) {
            etCantidad.setError("Introduce un número válido");
            return;
        }

        ApiService api = ApiClient.getService(ApiService.class, sessionManager.getToken());
        api.invertir(new InversionRequest(idProyecto, cantidad))
                .enqueue(new Callback<InversionResponse>() {
                    @Override
                    public void onResponse(Call<InversionResponse> call,
                                           Response<InversionResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            InversionResponse inv = response.body();
                            Toast.makeText(DetalleProyectoActivity.this,
                                    "Inversión realizada. Nuevo saldo: "
                                            + inv.getNuevoSaldoAurus() + " Aurus",
                                    Toast.LENGTH_LONG).show();
                            etCantidad.setText("");
                            // Refrescar datos del proyecto
                            cargarProyecto();
                        } else if (response.code() == 400) {
                            Toast.makeText(DetalleProyectoActivity.this,
                                    "Saldo insuficiente o proyecto no disponible",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<InversionResponse> call, Throwable t) {
                        Toast.makeText(DetalleProyectoActivity.this,
                                "Error de conexión", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public boolean onSupportNavigateUp() { finish(); return true; }
}