package com.jnarvaez.serviceapp.servicio

import com.google.gson.annotations.SerializedName
import com.jnarvaez.serviceapp.entidad.Calificacion

data class CalificacionResponse (
    @SerializedName("listaCalificaciones") var listaCalificaciones:ArrayList<Calificacion>
)