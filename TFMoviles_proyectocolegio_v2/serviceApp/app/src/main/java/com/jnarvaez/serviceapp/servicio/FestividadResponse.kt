package com.jnarvaez.serviceapp.servicio

import com.jnarvaez.serviceapp.entidad.Festividad

import com.google.gson.annotations.SerializedName



data class FestividadResponse(
    @SerializedName("listaFestividad") var listaFestividad:ArrayList<Festividad>
)
