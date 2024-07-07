package com.toledo.proyectocolegio.servicio

import com.google.gson.annotations.SerializedName
import com.toledo.proyectocolegio.entidades.Alumno

data class AlumnoResponse (
    @SerializedName("listaAlumnos") var listaAlumnos:ArrayList<Alumno>
)