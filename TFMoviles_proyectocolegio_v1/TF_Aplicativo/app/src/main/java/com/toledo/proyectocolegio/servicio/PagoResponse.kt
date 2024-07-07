package com.toledo.proyectocolegio.servicio

import com.google.gson.annotations.SerializedName
import com.toledo.proyectocolegio.entidades.Pago

data class PagoResponse (
    @SerializedName("listaPagos") var listaPagos:ArrayList<Pago>
)