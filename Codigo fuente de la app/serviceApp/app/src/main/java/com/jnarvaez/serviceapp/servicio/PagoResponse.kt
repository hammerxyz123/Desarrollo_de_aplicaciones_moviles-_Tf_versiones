package com.toledo.proyectocolegio.servicio

import com.google.gson.annotations.SerializedName
import com.jnarvaez.serviceapp.entidad.Pago

data class PagoResponse (
    @SerializedName("listaPagos") var listaPagos:ArrayList<Pago>
)