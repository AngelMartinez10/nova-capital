package com.novacapital.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.novacapital.R;
import com.novacapital.api.ApiClient;
import com.novacapital.api.ApiService;
import com.novacapital.models.AuthResponse;
import com.novacapital.models.RegistroRequest;
import com.novacapital.utils.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistroActivity extends AppCompatActivity {

    private EditText etNombre, etApellidos, etEmail, etContrasena, etTelefono;
    private Button btnRegistrar;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        sessionManager = new SessionManager(this);

        etNombre     = findViewById(R.id.etNombre);
        etApellidos  = findViewById(R.id.etApellidos);
        etEmail      = findViewById(R.id.etEmail);
        etContrasena = findViewById(R.id.etContrasena);
        etTelefono   = findViewById(R.id.etTelefono);
        btnRegistrar = findViewById(R.id.btnRegistrar);

        btnRegistrar.setOnClickListener(v -> hacerRegistro());
    }

    private void hacerRegistro() {
        String nombre    = etNombre.getText().toString().trim();
        String apellidos = etApellidos.getText().toString().trim();
        String email     = etEmail.getText().toString().trim();
        String pass      = etContrasena.getText().toString().trim();
        String telefono  = etTelefono.getText().toString().trim();

        if (TextUtils.isEmpty(nombre))    { etNombre.setError("Campo obligatorio"); return; }
        if (TextUtils.isEmpty(apellidos)) { etApellidos.setError("Campo obligatorio"); return; }
        if (TextUtils.isEmpty(email))     { etEmail.setError("Campo obligatorio"); return; }
        if (TextUtils.isEmpty(pass))      { etContrasena.setError("Campo obligatorio"); return; }

        ApiService api = ApiClient.getRetrofit().create(ApiService.class);
        api.registro(new RegistroRequest(nombre, apellidos, email, pass, telefono))
                .enqueue(new Callback<AuthResponse>() {
                    @Override
                    public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            AuthResponse auth = response.body();
                            sessionManager.guardarSesion(
                                    auth.getToken(),
                                    auth.getIdCliente(),
                                    auth.getNombre(),
                                    auth.getSaldoAurus().toPlainString()
                            );
                            Toast.makeText(RegistroActivity.this,
                                    "¡Cuenta creada! Bienvenido " + auth.getNombre(),
                                    Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(RegistroActivity.this, MainActivity.class));
                            finish();
                        } else {
                            Toast.makeText(RegistroActivity.this,
                                    "Error al registrar. El email ya puede estar en uso.",
                                    Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<AuthResponse> call, Throwable t) {
                        Toast.makeText(RegistroActivity.this,
                                "Error de conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}