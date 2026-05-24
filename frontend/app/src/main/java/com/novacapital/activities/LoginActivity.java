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
import com.novacapital.models.LoginRequest;
import com.novacapital.utils.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etContrasena;
    private Button btnLogin, btnIrRegistro;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sessionManager = new SessionManager(this);

        // Si ya hay sesión activa, ir directo al main
        if (sessionManager.haySesion()) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_login);

        etEmail       = findViewById(R.id.etEmail);
        etContrasena  = findViewById(R.id.etContrasena);
        btnLogin      = findViewById(R.id.btnLogin);
        btnIrRegistro = findViewById(R.id.btnIrRegistro);

        btnLogin.setOnClickListener(v -> hacerLogin());
        btnIrRegistro.setOnClickListener(v ->
                startActivity(new Intent(this, RegistroActivity.class)));
    }

    private void hacerLogin() {
        String email = etEmail.getText().toString().trim();
        String pass  = etContrasena.getText().toString().trim();

        if (TextUtils.isEmpty(email)) { etEmail.setError("Campo obligatorio"); return; }
        if (TextUtils.isEmpty(pass))  { etContrasena.setError("Campo obligatorio"); return; }

        ApiService api = ApiClient.getRetrofit().create(ApiService.class);
        api.login(new LoginRequest(email, pass)).enqueue(new Callback<AuthResponse>() {
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
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this,
                            "Email o contraseña incorrectos", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this,
                        "Error de conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}