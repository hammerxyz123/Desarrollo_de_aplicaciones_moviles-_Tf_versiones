package com.jnarvaez.serviceapp.servicio

import com.google.gson.annotations.SerializedName
import com.jnarvaez.serviceapp.entidad.Festividad


data class FestividadResponse(
    @SerializedName("listaFestividad") var listaFestividad:ArrayList<Festividad>
)
