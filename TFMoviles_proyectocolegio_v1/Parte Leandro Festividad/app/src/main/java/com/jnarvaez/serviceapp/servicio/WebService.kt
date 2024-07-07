package com.jnarvaez.serviceapp.servicio

import com.google.gson.GsonBuilder
import com.jnarvaez.serviceapp.entidad.Festividad
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST


object AppConstantes{
    const val BASE_URL = "http://192.168.56.1:3000"
}

interface WebService {
    @GET("/festividades")
    suspend fun obtenerFestividades():Response<FestividadResponse>

    @POST("/festividades/agregar")
    suspend fun agregarFestividad(@Body festividad:Festividad):Response<String>

}

object RetrofitClient{
    val webService:WebService by lazy {
        Retrofit.Builder()
            .baseUrl(AppConstantes.BASE_URL)
            .addConverterFactory(
                GsonConverterFactory.create(GsonBuilder().create()))
            .build().create(WebService::class.java)
    }
}