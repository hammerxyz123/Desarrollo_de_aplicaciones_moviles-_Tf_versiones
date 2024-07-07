package com.jnarvaez.serviceapp.servicio

import com.google.gson.annotations.SerializedName
import com.jnarvaez.serviceapp.entidad.Alumno

data class AlumnoResponse (
    @SerializedName("listaAlumnos") var listaAlumnos:ArrayList<Alumno>
)