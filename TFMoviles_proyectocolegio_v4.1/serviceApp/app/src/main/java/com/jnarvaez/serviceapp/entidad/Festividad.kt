package com.jnarvaez.serviceapp.entidad
import android.os.Parcel
import android.os.Parcelable

data class Festividad(
    var fes_id:Int,
    var fes_nombre:String,
    var fes_descripcion:String,
    var fes_abono:String,
    var fes_fecha:String,
    var fes_observacion:String,
    var fes_latitud:Double,
    var fes_longitud:Double
)