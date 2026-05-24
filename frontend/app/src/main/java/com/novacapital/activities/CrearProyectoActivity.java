package com.novacapital.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.novacapital.R;
import com.novacapital.api.ApiClient;
import com.novacapital.api.ApiService;
import com.novacapital.models.ProyectoRequest;
import com.novacapital.models.ProyectoResponse;
import com.novacapital.utils.SessionManager;

import java.math.BigDecimal;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CrearProyectoActivity extends AppCompatActivity {

    private EditText etNombre, etDescripcion, etCategoria, etObjetivo;
    private Button btnCrear;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_proyecto);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Crear Proyecto");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        etNombre      = findViewById(R.id.etNombre);
        etDescripcion = findViewById(R.id.etDescripcion);
        etCategoria   = findViewById(R.id.etCategoria);
        etObjetivo    = findViewById(R.id.etObjetivo);
        btnCrear      = findViewById(R.id.btnCrear);

        sessionManager = new SessionManager(this);

        btnCrear.setOnClickListener(v -> crearProyecto());
    }

    private void crearProyecto() {
        String nombre      = etNombre.getText().toString().trim();
        String descripcion = etDescripcion.getText().toString().trim();
        String categoria   = etCategoria.getText().toString().trim();
        String objetivoStr = etObjetivo.getText().toString().trim();

        if (TextUtils.isEmpty(nombre))      { etNombre.setError("Campo obligatorio"); return; }
        if (TextUtils.isEmpty(descripcion)) { etDescripcion.setError("Campo obligatorio"); return; }
        if (TextUtils.isEmpty(objetivoStr)) { etObjetivo.setError("Campo obligatorio"); return; }

        BigDecimal objetivo;
        try {
            objetivo = new BigDecimal(objetivoStr);
            if (objetivo.compareTo(BigDecimal.ZERO) <= 0) {
                etObjetivo.setError("El objetivo debe ser mayor que 0");
                return;
            }
        } catch (NumberFormatException e) {
            etObjetivo.setError("Introduce un número válido");
            return;
        }

        ApiService api = ApiClient.getService(ApiService.class, sessionManager.getToken());
        api.crearProyecto(new ProyectoRequest(nombre, descripcion, categoria, objetivo))
                .enqueue(new Callback<ProyectoResponse>() {
                    @Override
                    public void onResponse(Call<ProyectoResponse> call,
                                           Response<ProyectoResponse> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(CrearProyectoActivity.this,
                                    "Proyecto creado con éxito", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(CrearProyectoActivity.this,
                                    "Error al crear el proyecto", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<ProyectoResponse> call, Throwable t) {
                        Toast.makeText(CrearProyectoActivity.this,
                                "Error de conexión", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public boolean onSupportNavigateUp() { finish(); return true; }
}