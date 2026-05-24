package com.novacapital.api;

import com.novacapital.models.AuthResponse;
import com.novacapital.models.ClienteResponse;
import com.novacapital.models.ClienteRetoResponse;
import com.novacapital.models.InversionRequest;
import com.novacapital.models.InversionResponse;
import com.novacapital.models.LoginRequest;
import com.novacapital.models.ProyectoRequest;
import com.novacapital.models.ProyectoResponse;
import com.novacapital.models.RegistroRequest;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.*;

public interface ApiService {

    @POST("api/auth/registro")
    Call<AuthResponse> registro(@Body RegistroRequest request);

    @POST("api/auth/login")
    Call<AuthResponse> login(@Body LoginRequest request);

    @GET("api/clientes/perfil")
    Call<ClienteResponse> miPerfil();

    @GET("api/proyectos")
    Call<List<ProyectoResponse>> listarProyectos();

    @GET("api/proyectos/{id}")
    Call<ProyectoResponse> obtenerProyecto(@Path("id") int id);

    @GET("api/proyectos/mios")
    Call<List<ProyectoResponse>> misProyectos();

    @POST("api/proyectos")
    Call<ProyectoResponse> crearProyecto(@Body ProyectoRequest request);

    @POST("api/inversiones")
    Call<InversionResponse> invertir(@Body InversionRequest request);

    @GET("api/inversiones/mias")
    Call<List<InversionResponse>> misInversiones();

    @GET("api/retos/mios")
    Call<List<ClienteRetoResponse>> misRetos();
}