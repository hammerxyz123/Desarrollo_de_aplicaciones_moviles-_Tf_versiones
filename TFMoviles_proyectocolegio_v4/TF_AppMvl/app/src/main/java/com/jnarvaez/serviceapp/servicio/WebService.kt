package com.jnarvaez.serviceapp.servicio

import com.google.gson.GsonBuilder
import com.jnarvaez.serviceapp.entidad.Festividad
import com.jnarvaez.serviceapp.entidad.Alumno
import com.jnarvaez.serviceapp.entidad.Calificacion
import com.jnarvaez.serviceapp.entidad.Pago
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path


object AppConstantes{
    const val BASE_URL = "http://192.168.1.33:3000"
}

interface WebService {
    @GET("/festividad")
    suspend fun obtenerFestividades():Response<FestividadResponse>

    @POST("/festividad/agregar")
    suspend fun agregarFestividad(@Body festividad:Festividad):Response<String>

    @GET("/alumnos")
    suspend fun obtenerAlumnos(): Response<AlumnoResponse>
    @GET("/calificaciones")
    suspend fun obtenerCalificaciones(): Response<CalificacionResponse>
    @GET("/pagos")
    suspend fun obtenerPagos(): Response<PagoResponse>

    @POST("/alumnos/agregar")
    suspend fun agregarAlumnos(@Body usuario: Alumno): Response<String>
    @POST("/calificaciones/agregar")
    suspend fun agregarCalificaciones(@Body calificacion: Calificacion): Response<String>
    @POST("/pagos/agregar")
    suspend fun agregarPagos(@Body pago: Pago): Response<String>

    @PUT("/pagos/actualizar/{id}")
    suspend fun actualizarPagos(@Path("id")id:Int, @Body pago: Pago):Response<String>
    @PUT("/calificaciones/actualizar/{id}")
    suspend fun actualizarCalificacion(@Path("id")id:Int, @Body calificacion: Calificacion):Response<String>
    @PUT("/alumnos/actualizar/{id}")
    suspend fun actualizarAlumnos(@Path("id")id:Int, @Body alumno: Alumno):Response<String>

    @DELETE("/alumnos/eliminar/{id}")
    suspend fun eliminarAlumno(@Path("id")id:Int):Response<String>
    @DELETE("/calificaciones/eliminar/{id}")
    suspend fun eliminarCalificacion(@Path("id")id:Int):Response<String>
    @DELETE("/pagos/eliminar/{id}")
    suspend fun eliminarPago(@Path("id")id:Int):Response<String>
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