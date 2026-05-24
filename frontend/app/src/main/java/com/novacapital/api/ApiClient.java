package com.novacapital.api;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;

public class ApiClient {

    // Emulador Android -> 10.0.2.2 apunta al localhost del PC
    // Dispositivo real -> cambia por la IP local de tu PC, ej: "http://192.168.1.100:8080/"
    private static final String BASE_URL = "http://10.0.2.2:8080/";

    private static Retrofit retrofitPublico;

    // Retrofit sin token (solo para login y registro)
    public static Retrofit getRetrofit() {
        if (retrofitPublico == null) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(logging)
                    .build();

            retrofitPublico = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofitPublico;
    }

    // Crea un ApiService con el token JWT inyectado en cada petición
    public static ApiService getServiceConToken(String token) {
        OkHttpClient client = construirClienteConToken(token);

        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService.class);
    }

    // Mantiene compatibilidad con el código existente que llama a getService(...)
    public static <T> T getService(Class<T> serviceClass, String token) {
        OkHttpClient client = construirClienteConToken(token);

        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(serviceClass);
    }

    // Construye el OkHttpClient con el interceptor que añade el token a cada petición
    private static OkHttpClient construirClienteConToken(String token) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        // Clase interna que añade el header "Authorization: Bearer <token>" a cada petición
        Interceptor tokenInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request requestOriginal = chain.request();
                Request requestConToken = requestOriginal.newBuilder()
                        .addHeader("Authorization", "Bearer " + token)
                        .build();
                return chain.proceed(requestConToken);
            }
        };

        return new OkHttpClient.Builder()
                .addInterceptor(tokenInterceptor)
                .addInterceptor(logging)
                .build();
    }
}