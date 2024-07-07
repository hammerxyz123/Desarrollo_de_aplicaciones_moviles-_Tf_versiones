package com.toledo.proyectocolegio.servicio

import com.google.gson.annotations.SerializedName
import com.toledo.proyectocolegio.entidades.Calificacion

data class CalificacionResponse (
    @SerializedName("listaCalificaciones") var listaCalificaciones:ArrayList<Calificacion>
)